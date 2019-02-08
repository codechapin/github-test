package com.github.codechapin.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Utils {

  public static String dateInUTCMinusWeeks(int weeks) {
    var now = LocalDateTime.now();
    var nowZoned = now.atZone(ZoneId.systemDefault());
    var utc = nowZoned.withZoneSameInstant(ZoneId.of("UTC"));

    return utc.minusWeeks(weeks).toString();
  }
}
