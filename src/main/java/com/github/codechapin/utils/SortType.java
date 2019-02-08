package com.github.codechapin.utils;

import java.util.Comparator;


public enum SortType {
  ASCENDING,
  DESCENDING;

  @SuppressWarnings("unchecked")
  public static <T> Comparator<T> comparator(SortType sort) {
    switch (sort) {
      case ASCENDING:
        return (Comparator<T>) Comparator.naturalOrder();
      case DESCENDING:
        return (Comparator<T>) Comparator.reverseOrder();
      default:
        return (Comparator<T>) Comparator.naturalOrder();
    }
  }

  public static SortType parse(String val) {
    if (val == null) {
      return SortType.DESCENDING;
    }

    switch (val.toLowerCase()) {
      case "asc":
        return SortType.ASCENDING;
      case "desc":
        return SortType.DESCENDING;
      default:
        return SortType.DESCENDING;
    }
  }
}
