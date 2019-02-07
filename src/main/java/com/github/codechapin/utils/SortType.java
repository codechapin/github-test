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
}
