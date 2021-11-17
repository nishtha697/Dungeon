package random;

import java.util.Random;

/**
 * This class implements  {@link RandomGenerator} and represents a truly random number generation
 * class.
 */
public class RandomTrue implements RandomGenerator {

  private static final Random RAND = new Random();

  @Override
  public int getRandom(int upperBound, int lowerBound) {
    return RAND.nextInt(upperBound - lowerBound) + lowerBound;
  }
}
