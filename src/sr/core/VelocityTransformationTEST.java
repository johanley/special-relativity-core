package sr.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import sr.core.component.ops.Sense;
import sr.core.vec3.Velocity;

/** Unit test. */
class VelocityTransformationTEST {

  @Test void testAllZeroes() {
    Velocity output = VelocityTransformation.primedVelocity(ZERO, ZERO);
    assertZero(output);

    output = VelocityTransformation.unprimedVelocity(ZERO, ZERO);
    assertZero(output);
  }
  
  @Test void zeroBoostDoesNothing() {
    Velocity object_v = Velocity.of(0.1, 0.2, 0.3);
    Velocity output = VelocityTransformation.primedVelocity(ZERO, object_v);
    assertNoDiff(object_v, output);
    output = VelocityTransformation.unprimedVelocity(ZERO, object_v);
    assertNoDiff(object_v, output);
  }
  
  @Test void boostSpeedUnityFails() {
    Exception exception = assertThrows(RuntimeException.class, () -> 
      VelocityTransformation.primedVelocity(UNITY, ZERO)
    );
    exception = assertThrows(RuntimeException.class, () -> 
      VelocityTransformation.unprimedVelocity(UNITY, ZERO)
    );
  }

  /** This corresponds to the velocity transformation for light, and is related to aberration. */
  @Test void objectSpeedUnity() {
    Velocity boost_v = Velocity.of(0.1, 0.52, 0.8);
    Velocity output = VelocityTransformation.primedVelocity(boost_v, UNITY);
    assertEquals(output.magnitude(), 1.0);
    output = VelocityTransformation.unprimedVelocity(boost_v, UNITY);
    assertEquals(output.magnitude(), 1.0);
  }
  
  @Test void unaffectedDimensions() {
    Velocity boost_v = Velocity.of(0.2, Axis.X);
    Velocity object_v = Velocity.of(0.4, Axis.X);
    Velocity output = VelocityTransformation.primedVelocity(boost_v, object_v);
    assertEquals(output.y(), 0);
    assertEquals(output.z(), 0);
  }
  
  @Test void inAndOut() {
    Velocity boost_v = Velocity.of(0.1, 0.25, 0.75);
    Velocity input = Velocity.of(0.1, 0.9, 0.25);
    Velocity output1 = VelocityTransformation.primedVelocity(boost_v, input);
    Velocity output2 = VelocityTransformation.unprimedVelocity(boost_v, output1);
    assertNoDiff(input, output2);
    output1 = VelocityTransformation.unprimedVelocity(boost_v, input);
    output2 = VelocityTransformation.primedVelocity(boost_v, output1);
    assertNoDiff(input, output2);
  }

  /** Not really a test; ensures future changes which alter this typical output are flagged. */
  @Test void anchorResults() {
    Velocity boost_v = Velocity.of(0.1, 0.25, 0.75);
    Velocity input = Velocity.of(0.2, 0.9, 0.25);
    Velocity output1 = VelocityTransformation.primedVelocity(boost_v, input);
    Velocity output2 = VelocityTransformation.unprimedVelocity(boost_v, input);
    assertNoDiff(output1, Velocity.of(0.08421452014132239, 0.6363705196870354, -0.6991230343579868));
    assertNoDiff(output2, Velocity.of(0.17297852717640522, 0.6011450400854399, 0.7701554471217051));
  }
  
  @Test void exampleFromAnotherSource() {
    //From this source:
    //https://phys.libretexts.org/Bookshelves/College_Physics/College_Physics_1e_(OpenStax)/28%3A_Special_Relativity/28.04%3A_Relativistic_Addition_of_Velocities
    Velocity boost_v = Velocity.of(0.5, Axis.X);
    Velocity u = Velocity.of(0.75, Axis.X);
    Velocity v = VelocityTransformation.unprimedVelocity(boost_v, u);
    assertNoDiff(v, Velocity.of(0.9090909090909091, 0, 0));
  }
  
  @Test void alwaysLessThanTheSpeedLimit() {
    Velocity boost_v = Velocity.of(0.99, Axis.Y);
    Velocity u_p = Velocity.of(0.99, Axis.X);
    Velocity v = VelocityTransformation.unprimedVelocity(boost_v, u_p);
    assertTrue(isInRange(v));
    
    boost_v = Velocity.of(0.99, Axis.Y);
    v = Velocity.of(0.99, Axis.X);
    u_p = VelocityTransformation.primedVelocity(boost_v, v);
    assertTrue(isInRange(u_p));
  }
  
  private boolean isInRange(Velocity v) {
    return v.magnitude() < 1.0;
  }

  private void assertZero(Velocity v) {
    assertEquals(v.x(), 0.0);
    assertEquals(v.y(), 0.0);
    assertEquals(v.z(), 0.0);
  }
  
  private void assertNoDiff(Velocity input, Velocity output) {
    assertEquals(input.x(), output.x(), onlyTinyDiff);
    assertEquals(input.y(), output.y(), onlyTinyDiff);
    assertEquals(input.z(), output.z(), onlyTinyDiff);
  }
  
  private double onlyTinyDiff = Epsilon.ε();
  
  private Velocity ZERO = Velocity.zero();
  private Velocity UNITY = Velocity.unity(Axis.X, Sense.Plus);

}
