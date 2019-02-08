package com.github.codechapin.utils;

import com.beust.jcommander.JCommander;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ArgsTest {

  @Test
  public void testDefaults() {
    Args args = new Args();
    String[] argv = {"kubernetes/kubernetes"};
    JCommander.newBuilder()
        .addObject(args)
        .build()
        .parse(argv);

    Assert.assertEquals(args.getRepo(), "kubernetes/kubernetes");
    Assert.assertEquals(args.getSort(), SortType.DESCENDING);
    Assert.assertEquals(args.getWeeks(), 52);
  }

  @Test
  public void testAscending() {
    Args args = new Args();
    String[] argv = {"-weeks", "20", "-sort", "asc", "kubernetes/kubernetes"};
    JCommander.newBuilder()
        .addObject(args)
        .build()
        .parse(argv);

    Assert.assertEquals(args.getRepo(), "kubernetes/kubernetes");
    Assert.assertEquals(args.getSort(), SortType.ASCENDING);
    Assert.assertEquals(args.getWeeks(), 20);
  }

  @Test
  public void testDescending() {
    Args args = new Args();
    String[] argv = {"-weeks", "15", "-sort", "desc", "kubernetes/kubernetes"};
    JCommander.newBuilder()
        .addObject(args)
        .build()
        .parse(argv);

    Assert.assertEquals(args.getRepo(), "kubernetes/kubernetes");
    Assert.assertEquals(args.getSort(), SortType.DESCENDING);
    Assert.assertEquals(args.getWeeks(), 15);
  }
}
