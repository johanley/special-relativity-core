package sr.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import sr.core.component.ops.Sense;
import sr.core.vec3.NVelocity;

class VelocityTransformationTEST {

  @Test void testAllZeroes() {
    NVelocity output = VelocityTransformation.primedVelocity(ZERO, ZERO);
    assertZero(output);

    output = VelocityTransformation.unprimedVelocity(ZERO, ZERO);
    assertZero(output);
  }
  
  @Test void zeroBoostDoesNothing() {
    NVelocity object_v = NVelocity.of(0.1, 0.2, 0.3);
    NVelocity output = VelocityTransformation.primedVelocity(ZERO, object_v);
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
    NVelocity boost_v = NVelocity.of(0.1, 0.52, 0.8);
    NVelocity output = VelocityTransformation.primedVelocity(boost_v, UNITY);
    assertEquals(output.magnitude(), 1.0);
    output = VelocityTransformation.unprimedVelocity(boost_v, UNITY);
    assertEquals(output.magnitude(), 1.0);
  }
  
  @Test void unaffectedDimensions() {
    NVelocity boost_v = NVelocity.of(0.2, Axis.X);
    NVelocity object_v = NVelocity.of(0.4, Axis.X);
    NVelocity output = VelocityTransformation.primedVelocity(boost_v, object_v);
    assertEquals(output.y(), 0);
    assertEquals(output.z(), 0);
  }
  
  @Test void inAndOut() {
    NVelocity boost_v = NVelocity.of(0.1, 0.25, 0.75);
    NVelocity input = NVelocity.of(0.1, 0.9, 0.25);
    NVelocity output1 = VelocityTransformation.primedVelocity(boost_v, input);
    NVelocity output2 = VelocityTransformation.unprimedVelocity(boost_v, output1);
    assertNoDiff(input, output2);
    output1 = VelocityTransformation.unprimedVelocity(boost_v, input);
    output2 = VelocityTransformation.primedVelocity(boost_v, output1);
    assertNoDiff(input, output2);
  }

  /** Not really a test; ensures future changes which alter this typical output are flagged. */
  @Test void anchorResults() {
    NVelocity boost_v = NVelocity.of(0.1, 0.25, 0.75);
    NVelocity input = NVelocity.of(0.2, 0.9, 0.25);
    NVelocity output1 = VelocityTransformation.primedVelocity(boost_v, input);
    NVelocity output2 = VelocityTransformation.unprimedVelocity(boost_v, input);
    assertNoDiff(output1, NVelocity.of(0.08421452014132239, 0.6363705196870354, -0.6991230343579868));
    assertNoDiff(output2, NVelocity.of(0.17297852717640522, 0.6011450400854399, 0.7701554471217051));
  }
  
  @Test void exampleFromAnotherSource() {
    //From this source:
    //https://phys.libretexts.org/Bookshelves/College_Physics/College_Physics_1e_(OpenStax)/28%3A_Special_Relativity/28.04%3A_Relativistic_Addition_of_Velocities
    NVelocity boost_v = NVelocity.of(0.5, Axis.X);
    NVelocity u = NVelocity.of(0.75, Axis.X);
    NVelocity v = VelocityTransformation.unprimedVelocity(boost_v, u);
    assertNoDiff(v, NVelocity.of(0.9090909090909091, 0, 0));
  }
  
  @Test void alwaysLessThanTheSpeedLimit() {
    NVelocity boost_v = NVelocity.of(0.99, Axis.Y);
    NVelocity u_p = NVelocity.of(0.99, Axis.X);
    NVelocity v = VelocityTransformation.unprimedVelocity(boost_v, u_p);
    assertTrue(isInRange(v));
    
    boost_v = NVelocity.of(0.99, Axis.Y);
    v = NVelocity.of(0.99, Axis.X);
    u_p = VelocityTransformation.primedVelocity(boost_v, v);
    assertTrue(isInRange(u_p));
  }
  
  private boolean isInRange(NVelocity v) {
    return v.magnitude() < 1.0;
  }

  private void assertZero(NVelocity v) {
    assertEquals(v.x(), 0.0);
    assertEquals(v.y(), 0.0);
    assertEquals(v.z(), 0.0);
  }
  
  private void assertNoDiff(NVelocity input, NVelocity output) {
    assertEquals(input.x(), output.x(), onlyTinyDiff);
    assertEquals(input.y(), output.y(), onlyTinyDiff);
    assertEquals(input.z(), output.z(), onlyTinyDiff);
  }
  
  private double onlyTinyDiff = Epsilon.ε();
  
  private NVelocity ZERO = NVelocity.zero();
  private NVelocity UNITY = NVelocity.unity(Axis.X, Sense.Plus);

}
