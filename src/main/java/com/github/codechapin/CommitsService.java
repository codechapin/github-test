package com.github.codechapin;

import com.github.codechapin.utils.Args;
import com.github.codechapin.utils.DaysCounter;
import com.github.codechapin.utils.NavigationLinks;
import com.github.codechapin.utils.Utils;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This Class will manage all the state for the multiple calls to Github, needed for repositories
 * with a lot of commits.
 */
public class CommitsService {

  private final CommitsParser parser;
  private final HttpClient client;
  private final String urlTemplate;

  CommitsService() {
    parser = new CommitsParser();

    client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build();

    urlTemplate = "https://api.github.com/repos/%s/commits?since=%s&per_page=100&page=";
  }

  List<DaysCounter.Entry> requestAll(Args args) throws Exception {
    var since = Utils.dateInUTCMinusWeeks(args.getWeeks());
    var url = String.format(urlTemplate, args.getRepo(), since);

    var future = sendRequests(url);

    // wait until all is done.
    future.get();

    return parser.getCounter().sorted(args.getSort());
  }

  private CompletableFuture<HttpResponse<InputStream>> sendRequests(String url) {
    var request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(Duration.ofSeconds(30))
        .build();

    return client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
        .thenApply(this::handleResponse)
        .thenCompose(this::callNextRequest);
  }

  private HttpResponse<InputStream> handleResponse(HttpResponse<InputStream> response) {
    if (response.statusCode() == 200) {
      parser.parseCommitDates(response.body());

      return response;
    }

    // The requirements for this project are fighting against Github's rate limiting, they only allow
    // 50 requests per hour https://developer.github.com/v3/#rate-limiting
    // It's very easy to reach this limit with the current requirements. For example, kubernetes has
    // 130 pages when the page size is set to 100. This means that in one year they had 13,000 commits!
    // We will need to make 130 requests for this repo, which is above their limit.

    var remainingHeader = response.headers().firstValue("X-RateLimit-Remaining");
    var resetHeader = response.headers().firstValue("X-RateLimit-Reset");

    if (remainingHeader.isPresent()) {
      var remaining = Integer.parseInt(remainingHeader.get());
      if (remaining == 0 && resetHeader.isPresent()) {

        //print a message when we can try again.
        var epoch = Long.parseLong(resetHeader.get());
        var instant = Instant.ofEpochMilli(epoch * 1000);

        var ends = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
        var now = LocalDateTime.now(Clock.systemUTC());

        var difference = Duration.between(now, ends);

        System.out.printf("You are rate limited. Try again in %d minutes\n", difference.toMinutes());
      }
    }

    return response;
  }

  private CompletableFuture<HttpResponse<InputStream>> callNextRequest(HttpResponse<InputStream> response) {
    if (response.statusCode() != 200) {
      return CompletableFuture.completedFuture(null);
    }

    // check if we need to make more calls by looking at the link header
    var linkHeader = response.headers().firstValue("Link");

    if (linkHeader.isPresent()) {
      var nav = NavigationLinks.parse(linkHeader.get());

      if (nav != null && nav.getNext() != null && !nav.getNext().isEmpty()) {
        // we have more requests to make
        return sendRequests(nav.getNext());
      }
    }

    return CompletableFuture.completedFuture(null);
  }
}
