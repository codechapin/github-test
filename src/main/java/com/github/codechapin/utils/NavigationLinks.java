package com.github.codechapin.utils;


public class NavigationLinks {
  private String first;
  private String last;
  private String next;
  private String prev;

  private NavigationLinks() {
  }

  public static NavigationLinks parse(String header) {
    if (header == null || header.isEmpty()) {
      throw new IllegalStateException("The header with the links cannot be null or empty");
    }

    /*
    sample header:

    <https://api.github.com/repositories/20580498/commits?since=2018-02-07T00%3A00%3A00Z&per_page=5&page=5>; rel="next",
    <https://api.github.com/repositories/20580498/commits?since=2018-02-07T00%3A00%3A00Z&per_page=5&page=2601>; rel="last",
    <https://api.github.com/repositories/20580498/commits?since=2018-02-07T00%3A00%3A00Z&per_page=5&page=1>; rel="first",
    <https://api.github.com/repositories/20580498/commits?since=2018-02-07T00%3A00%3A00Z&per_page=5&page=3>; rel="prev"
     */

    var nav = new NavigationLinks();
    var parts = header.split(",");
    for (var part : parts) {
      var atoms = part.split(";");
      if (atoms.length != 2) {
        continue;
      }

      var linkAtom = atoms[0].trim();
      var relAtom = atoms[1].trim();

      var relPair = relAtom.split("=");
      if (relPair.length != 2 || !"rel".equalsIgnoreCase(relPair[0].trim())) {
        // making sure that we have something like rel="something"
        continue;
      }

      // clean up the data, just in case.
      var relValue = relPair[1].trim().toLowerCase();

      // removing quotes if any
      if (relValue.startsWith("\"") && relValue.endsWith("\"")) {
        relValue = relValue.substring(1, relValue.length() - 1);
      }

      // removing the brackets around the links
      var link = linkAtom.substring(1, linkAtom.length() - 1);

      switch (relValue) {
        case "first":
          nav.first = link;
          break;
        case "last":
          nav.last = link;
          break;
        case "next":
          nav.next = link;
          break;
        case "prev":
          nav.prev = link;
          break;
      }
    }

    return nav;
  }

  public String getFirst() {
    return first;
  }

  public String getLast() {
    return last;
  }

  public String getNext() {
    return next;
  }

  public String getPrev() {
    return prev;
  }
}
