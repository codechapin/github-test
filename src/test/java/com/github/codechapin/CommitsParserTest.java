package com.github.codechapin;

import com.github.codechapin.utils.DaysCounter;
import com.github.codechapin.utils.SortType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class CommitsParserTest {

  @Test
  public void testParseCommitDates() {
    var total = 0;

    var input = this.getClass().getResourceAsStream("/github-commits.json");

    var parser = new CommitsParser();
    parser.parseCommitDates(input);

    var counter = parser.getCounter();
    var sorted = counter.sorted(SortType.DESCENDING);

    for (DaysCounter.Entry entry : sorted) {
      var value = entry.getValue();
      total = total + value;

      switch (entry.getDay()) {
        case TUESDAY:
          assertEquals(value, 49, "wrong value for " + entry.getDisplayDay());
          break;
        case WEDNESDAY:
          assertEquals(value, 26, "wrong value for " + entry.getDisplayDay());
          break;
        case THURSDAY:
          assertEquals(value, 13, "wrong value for " + entry.getDisplayDay());
          break;
        case MONDAY:
          assertEquals(value, 10, "wrong value for " + entry.getDisplayDay());
          break;
        case FRIDAY:
          assertEquals(value, 2, "wrong value for " + entry.getDisplayDay());
          break;
        case SATURDAY:
          assertEquals(value, 0, "wrong value for " + entry.getDisplayDay());
          break;
        case SUNDAY:
          assertEquals(value, 0, "wrong value for " + entry.getDisplayDay());
          break;
      }
    }

    assertEquals(total, 100, "The sample JSON has 100 commits, so we should have 100 total at the end but we didn't.");
  }
}
