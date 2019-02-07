package com.github.codechapin.utils;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DaysCounter {
  //using AtomicInteger so we are thread-safe, because I'm planning to create a web UI if I have time.
  private EnumMap<DayOfWeek, AtomicInteger> counters = new EnumMap<>(DayOfWeek.class);

  public DaysCounter() {
    // start the counters for each day at 0 (zero).
    Stream.of(DayOfWeek.values())
        .forEach(day -> counters.put(day, new AtomicInteger()));
  }

  /**
   * increments the counter for the provided day by 1.
   */
  public void incrementDay(DayOfWeek day) {
    counterFor(day).incrementAndGet();
  }

  /**
   * increments the counter for the provided day by the provided amount.
   */
  public void incrementDayBy(DayOfWeek day, int amount) {
    counterFor(day).addAndGet(amount);
  }

  /**
   * Gets the counter value for the provided day.
   */
  public int getValue(DayOfWeek day) {
    return counterFor(day).get();
  }

  public List<Entry> sorted(SortType sort) {

    return counters
        .entrySet()
        .stream()
        .map(entry -> new Entry(entry.getKey(), entry.getValue()))
        .sorted(SortType.comparator(sort))
        .collect(Collectors.toList());
  }

  /**
   * Just so it doesn't look weird in the getValue method. Without this, it would be: counters.get(day).get()
   */
  private AtomicInteger counterFor(DayOfWeek day) {
    return counters.get(day);
  }

  public class Entry implements Comparable<Entry> {
    private final DayOfWeek day;
    private final String displayDay;
    private final Integer value;

    public Entry(DayOfWeek day, AtomicInteger value) {
      // hardcoding for english, but in a web environment we should get this from browser information.
      this.day = day;
      this.displayDay = day.getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);

      // do it now, otherwise comparing would go weird if another thread modifies the values
      this.value = value.get();
    }

    public String getDisplayDay() {
      return displayDay;
    }

    public DayOfWeek getDay() {
      return day;
    }

    public int getValue() {
      return value;
    }

    @Override
    public int compareTo(Entry o) {
      return this.value.compareTo(o.value);
    }
  }

}
