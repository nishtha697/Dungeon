package random;

/**
 * The random.RandomGenerator represents a random number generator which generates a random number
 * within a given range.
 */
public interface RandomGenerator {

  /**
   * Generates a random number within the range.
   *
   * @param upperBound the exclusive upper bound of the range.
   * @param lowerBound the inclusive lower bound of the range.
   * @return the random number.
   */
  int getRandom(int upperBound, int lowerBound);
}
