import org.junit.Before;
import org.junit.Test;

import location.coordinate.Coordinate;
import location.coordinate.CoordinateImpl;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CoordinateImpl}.
 */
public class CoordinateImplTest {

  Coordinate coordinate;

  @Before
  public void setUp() {
    coordinate = new CoordinateImpl(2,5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCoordinateNegativeX() {
    new CoordinateImpl(-3, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCoordinateNegativeY() {
    new CoordinateImpl(3, -2);
  }

  @Test
  public void getX() {
    assertEquals(2, coordinate.getX());
  }

  @Test
  public void getY() {
    assertEquals(5, coordinate.getY());
  }
}