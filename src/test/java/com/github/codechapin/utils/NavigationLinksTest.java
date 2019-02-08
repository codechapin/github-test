package com.github.codechapin.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

public class NavigationLinksTest {
  @Test
  public void testParse() {
    var header = "<https://api.github.com/repositories/20580498/commits?since=2018-02-07T00%3A00%3A00Z&per_page=5&page=5>; rel=\"next\", " +
        "<https://api.github.com/repositories/20580498/commits?since=2018-02-07T00%3A00%3A00Z&per_page=5&page=2601>; rel=\"last\", " +
        "<https://api.github.com/repositories/20580498/commits?since=2018-02-07T00%3A00%3A00Z&per_page=5&page=1>; rel=\"first\", " +
        "<https://api.github.com/repositories/20580498/commits?since=2018-02-07T00%3A00%3A00Z&per_page=5&page=3>; rel=\"prev\"";

    var nav = NavigationLinks.parse(header);

    Assert.assertEquals(nav.getFirst(), "https://api.github.com/repositories/20580498/commits?since=2018-02-07T00%3A00%3A00Z&per_page=5&page=1");
    Assert.assertEquals(nav.getLast(), "https://api.github.com/repositories/20580498/commits?since=2018-02-07T00%3A00%3A00Z&per_page=5&page=2601");
    Assert.assertEquals(nav.getNext(), "https://api.github.com/repositories/20580498/commits?since=2018-02-07T00%3A00%3A00Z&per_page=5&page=5");
    Assert.assertEquals(nav.getPrev(), "https://api.github.com/repositories/20580498/commits?since=2018-02-07T00%3A00%3A00Z&per_page=5&page=3");
  }
}
