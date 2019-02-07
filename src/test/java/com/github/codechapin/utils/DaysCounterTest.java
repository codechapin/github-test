package com.github.codechapin.utils;

import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.util.ArrayDeque;

import static org.testng.Assert.assertEquals;

public class DaysCounterTest {

  @Test
  public void testIncrementDay() {
    var day = DayOfWeek.FRIDAY;

    var counter = new DaysCounter();

    counter.incrementDay(day);
    counter.incrementDay(day);
    assertEquals(counter.getValue(day), 2);

  }

  @Test
  public void testIncrementDayBy() {
    var day = DayOfWeek.FRIDAY;

    var counter = new DaysCounter();
    counter.incrementDay(day);
    counter.incrementDayBy(day, 2);

    assertEquals(counter.getValue(day), 3);
  }

  @Test
  public void testSortDescending() {
    var friday = DayOfWeek.FRIDAY;
    var wednesday = DayOfWeek.WEDNESDAY;

    var counter = new DaysCounter();
    counter.incrementDayBy(friday, 23);
    counter.incrementDayBy(wednesday, 55);

    var all = new ArrayDeque<>(counter.sorted(SortType.DESCENDING));

    var first = all.removeFirst();

    assertEquals(first.getDisplayDay(), "Wednesday");
    assertEquals(first.getValue(), 55);

    var second = all.removeFirst();
    assertEquals(second.getDisplayDay(), "Friday");
    assertEquals(second.getValue(), 23);

    var third = all.removeFirst();
    assertEquals(third.getDisplayDay(), "Monday");
    assertEquals(third.getValue(), 0);

    var fourth = all.removeFirst();
    assertEquals(fourth.getDisplayDay(), "Tuesday");
    assertEquals(fourth.getValue(), 0);

  }

  @Test
  public void testSortAscending() {
    var friday = DayOfWeek.FRIDAY;
    var wednesday = DayOfWeek.WEDNESDAY;

    var counter = new DaysCounter();
    counter.incrementDayBy(friday, 23);
    counter.incrementDayBy(wednesday, 55);

    var all = new ArrayDeque<>(counter.sorted(SortType.ASCENDING));

    var first = all.removeLast();

    assertEquals(first.getDisplayDay(), "Wednesday");
    assertEquals(first.getValue(), 55);

    var second = all.removeLast();
    assertEquals(second.getDisplayDay(), "Friday");
    assertEquals(second.getValue(), 23);

    var third = all.removeLast();
    assertEquals(third.getDisplayDay(), "Sunday");
    assertEquals(third.getValue(), 0);

    var fourth = all.removeLast();
    assertEquals(fourth.getDisplayDay(), "Saturday");
    assertEquals(fourth.getValue(), 0);

  }
}
