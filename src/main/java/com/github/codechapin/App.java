package com.github.codechapin;


import com.beust.jcommander.JCommander;
import com.github.codechapin.utils.Args;

public class App {

  public static void main(String[] argv) throws Exception {
    var args = new Args();
    JCommander.newBuilder()
        .addObject(args)
        .build()
        .parse(argv);

    var service = new CommitsService();
    var result = service.requestAll(args);

    result.forEach(entry -> System.out.printf("%-10s %d\n", entry.getDisplayDay(), entry.getValue()));
  }
}
