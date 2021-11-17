package random;

/**
 * This class implements  {@link RandomGenerator} and represents a predictable random number
 * generation class.
 */
public class RandomFalse implements RandomGenerator {

  @Override
  public int getRandom(int upperBound, int lowerBound) {
    return lowerBound;
  }
}
