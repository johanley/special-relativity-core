package sr.core.vector3.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sr.core.Parity.ODD;

import org.junit.jupiter.api.Test;

import sr.core.vector3.AxisAngle;
import sr.core.vector3.ThreeVector;
import sr.core.vector3.ThreeVectorImpl;

class SpatialReflectionTEST {

  @Test void basicOperation() {
    SpatialReflection reflect = SpatialReflection.of(ODD, ODD, ODD);
    ThreeVector input = ThreeVectorImpl.of(1, 2, 3);
    ThreeVector expected = ThreeVectorImpl.of(-1, -2, -3);
    
    ThreeVector output = reflect.changeFrame(input);
    assertNoDiff(output, expected);
    
    output = reflect.changeVector(input);
    assertNoDiff(output, expected);
  }
  
  @Test void pseudoVector(){
    SpatialReflection reflect = SpatialReflection.of(ODD, ODD, ODD);
    AxisAngle input = AxisAngle.of(1, 2, 3);
    ThreeVector expected = ThreeVectorImpl.of(1, 2, 3);
    
    ThreeVector output = reflect.changeFrame(input);
    assertNoDiff(output, expected);
  }

  
  private void assertNoDiff(ThreeVector input, ThreeVector output) {
    assertEquals(input.x(), output.x());
    assertEquals(input.y(), output.y());
    assertEquals(input.z(), output.z());
  }
  
}
