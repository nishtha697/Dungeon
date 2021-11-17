package random;

/**
 * The factory to generate {@link RandomGenerator} instances. It generates the instances based on
 * the required random nature of the program.
 */
public class RandomFactory {

  /**
   * Generates an instance of {@link RandomGenerator}.
   *
   * @param isRandom if the numbers generated are truly random or not.
   * @return the instance of {@link RandomGenerator}.
   */
  public RandomGenerator getRandomGenerator(boolean isRandom) {
    if (isRandom) {
      //truly random
      return new RandomTrue();
    }
    //not random
    return new RandomFalse();
  }
}