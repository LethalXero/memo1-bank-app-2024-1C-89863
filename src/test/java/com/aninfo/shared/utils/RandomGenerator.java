package com.aninfo.shared.utils;

import java.util.Random;

/*
 * Random generator utils.
 */
public class RandomGenerator {

  /**
   * Generate a random double value.
   *
   * @param min min value.
   * @param max max value.
   * @return Double
   */
  public static Double generateBetween(double min, double max) {
    Random random = new Random();
    Double value = min + (max - min) * random.nextDouble();

    return value;
  }

  /**
   * Generate a random int value.
   *
   * @param min min value.
   * @param max max value.
   * @return int
   */
  public static int generateBetween(int min, int max) {
    Random random = new Random();
    int value = min + (max - min) * random.nextInt();

    return value;
  }

  /**
   * Generate a random long value.
   *
   * @param min min value.
   * @param max max value.
   * @return Long
   */
  public static Long generateBetween(long min, long max) {
    Random random = new Random();
    Long value = min + (max - min) * random.nextLong();

    return value;
  }
}
