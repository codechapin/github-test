package com.github.codechapin;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.github.codechapin.utils.DaysCounter;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CommitsParser {
  private final DaysCounter counter = new DaysCounter();

  DaysCounter getCounter() {
    return counter;
  }

  void parseCommitDates(InputStream input) {
    if (input == null) {
      throw new NullPointerException("The InputStream with the commit history in JSON format is null.");
    }

    // since some of the repositories have quite a large amount of commits,
    // I decided to use a fast way to parse the JSON.
    try {


      var factory = new JsonFactory();
      var parser = factory.createParser(input);

      while (!parser.isClosed()) {
        parser.nextToken();

        if (isInField(parser, "commit")) {
          // found a commit, handle it
          handleCommit(parser, counter);
        }
      }

    } catch (IOException e) {
      throw new RuntimeException("Error reading the JSON data.", e);
    }
  }

  private void handleCommit(JsonParser parser, DaysCounter counter) throws IOException {
    parser.nextToken();
    do {

      if (isInField(parser, "author")) {
        // found an author, extract date from it.
        handleAuthor(parser, counter);
      }
    } while (isInObject(parser));
  }

  private void handleAuthor(JsonParser parser, DaysCounter counter) throws IOException {
    parser.nextToken();
    do {
      if (isInField(parser, "date")) {
        // found the commit date!!
        parser.nextToken();
        var parsed = DateTimeFormatter.ISO_DATE_TIME.parse(parser.getValueAsString());
        var date = LocalDateTime.from(parsed).atZone(ZoneId.from(parsed));

        counter.incrementDay(date.getDayOfWeek());
      }
    } while (isInObject(parser));
  }

  private boolean isInField(JsonParser parser, String name) throws IOException {
    return JsonToken.FIELD_NAME.equals(parser.currentToken()) && parser.getCurrentName().equals(name);
  }

  private boolean isInObject(JsonParser parser) throws IOException {
    return !parser.isClosed() && !JsonToken.END_OBJECT.equals(parser.nextToken());
  }
}
