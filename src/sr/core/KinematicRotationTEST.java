package sr.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import sr.core.vec3.AxisAngle;
import sr.core.vec3.Direction;
import sr.core.vec3.Velocity;

class KinematicRotationTEST {
  
  @Test void speedCannotBeUnity() {
    @SuppressWarnings("unused")
    Exception exception = assertThrows(RuntimeException.class, () -> 
      KinematicRotation.of(unity, some_velo)
    );
    
    exception = assertThrows(RuntimeException.class, () -> 
      KinematicRotation.of(some_velo,  unity)
    );
  }
  
  @Test void speedZeroReturnsZero() {
    KinematicRotation kr = KinematicRotation.of(zero, some_velo);
    allZero(kr);
    
    kr = KinematicRotation.of(some_velo, zero);
    allZero(kr);
  }
  
  @Test void sameLineReturnsZero() {
    KinematicRotation kr = KinematicRotation.of(veloX, veloX);
    allZero(kr);
  }
  
  @Test void methodsAgree() {
    KinematicRotation kr = KinematicRotation.of(veloX, veloY);
    assertEquals(kr.θw(), kr.θwAngleBetweenTwoResultants(), onlyTinyDiff);
    assertEquals(kr.θw(), kr.rotation().magnitude(), onlyTinyDiff);
  }
  
  @Test void thetaPositive() {
    KinematicRotation kr = KinematicRotation.of(veloX, veloY);
    assertTrue(kr.θw() > 0.0);
    assertTrue(kr.θwAngleBetweenTwoResultants() > 0.0);
  }
  
  @Test void direction() {
    KinematicRotation kr = KinematicRotation.of(veloX, veloY);
    AxisAngle rotation = kr.rotation();
    Direction direction = Direction.of(rotation);
    Direction crossProductDir = Direction.of(veloY.cross(veloX));
    assertEquals(direction.on(Axis.X), crossProductDir.x());
    assertEquals(direction.on(Axis.Y), crossProductDir.y());
    assertEquals(direction.on(Axis.Z), crossProductDir.z());
  }
  
  /** Not really a test per se. Meant to flag changes to the outputs after code changes. */
  @Test void testChangesAlterCalc() {
    KinematicRotation kr = KinematicRotation.of(veloX, veloY);
    assertEquals(kr.θw(), 0.0822333198675178);
    assertEquals(kr.θwAngleBetweenTwoResultants(), 0.08223331986751621);
  }

  private static final Velocity unity = Velocity.unity(Direction.of(1.0, 0.0, 0.0));
  private static final Velocity zero = Velocity.zero();
  private static final Velocity some_velo = Velocity.of(0.5, 0.25, 0.1);
  private static final Velocity veloX = Velocity.of(0.5, 0.0, 0.0);
  private static final Velocity veloY = Velocity.of(0.0, 0.3, 0.0);
  private static double onlyTinyDiff = Epsilon.ε();
  
  private void allZero(KinematicRotation kr) {
    assertEquals(kr.θw(), 0.0);
    assertEquals(kr.θwAngleBetweenTwoResultants(), 0.0);
    assertEquals(kr.rotation().x(), 0.0);
    assertEquals(kr.rotation().y(), 0.0);
    assertEquals(kr.rotation().z(), 0.0);
  }
}