package sr.core.vector3.transform;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import sr.core.Axis;
import sr.core.Epsilon;
import sr.core.vector3.ThreeVector;
import sr.core.vector3.ThreeVectorImpl;

class SpatialRotationTEST {
  
  @Test void zeroRotationDoesNothing() {
    SpatialRotation rot = SpatialRotation.of(Axis.X, 0);
    ThreeVector input = ThreeVectorImpl.of(1, 2, 3);
    
    ThreeVector output = rot.changeFrame(input);
    assertNoDiff(input, output);
    
    output = rot.changeVector(input);
    assertNoDiff(input, output);
  }

  
  private double onlyTinyDiff = Epsilon.ε();
  
  private void assertNoDiff(ThreeVector input, ThreeVector output) {
    assertEquals(input.x(), output.x(), onlyTinyDiff);
    assertEquals(input.y(), output.y(), onlyTinyDiff);
    assertEquals(input.z(), output.z(), onlyTinyDiff);
  }
  
}
