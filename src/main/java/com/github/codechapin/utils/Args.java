package com.github.codechapin.utils;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class Args {
  @Parameter(description = "Repository name", required = true)
  private List<String> repo = new ArrayList<>(1);

  @Parameter(names = "-weeks", description = "Number of weeks to calculate")
  private Integer weeks = 52;

  @Parameter(names = "-sort", description = "Sorting order: asc or desc (Ascending or Descending)")
  private String sort = "desc";

  public String getRepo() {
    return repo.get(0);
  }

  public int getWeeks() {
    return weeks;
  }

  public SortType getSort() {
    return SortType.parse(sort);
  }
}
