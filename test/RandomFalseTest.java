import org.junit.Before;
import org.junit.Test;

import random.RandomFalse;
import random.RandomGenerator;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link RandomFalse}.
 */
public class RandomFalseTest {

  RandomGenerator rand;

  @Before
  public void setUp() {
    rand = new RandomFalse();
  }

  @Test
  public void getRandom() {
    int random = rand.getRandom(12, 6);
    assertEquals(6, random);
  }
}