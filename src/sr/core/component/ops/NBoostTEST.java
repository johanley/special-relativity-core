package sr.core.component.ops;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.equalsWithEpsilon;

import org.junit.jupiter.api.Test;

import sr.core.Axis;
import sr.core.component.NEvent;
import sr.core.vec3.NVelocity;
import sr.core.vec4.NFourDelta;

class NBoostTEST {

  @Test void testZeroMapsToZero() {
    NEvent input = event(0,0,0,0);
    NEvent output = input.boost(NVelocity.of(0.256, X), NSense.Plus);
    assertSame(input, output);
  }
  
  @Test void speedZeroDoesNothing() {
    NEvent input = event(10,1,2,3);
    NEvent output = input.boost(NVelocity.of(0.0, X), NSense.Plus);
    assertSame(input, output);
    assertSame(input, output);
  }
  
  @Test void speedUnityFails() {
    NEvent input = event(10,1,2,3);
    Exception exception = assertThrows(RuntimeException.class, () -> 
      input.boost(NVelocity.of(1.0, X), NSense.Primed)
    );
  }
  
  @Test void unitVectors() {
    //the speed 0.6 has a simple gamma=1.25 and -gamma*beta = -0.75
    NVelocity v = NVelocity.of(0.6,  X);
    
    NEvent input = event(1,0,0,0);
    NEvent output = input.boost(v, NSense.Primed);
    assertEquals(output.on(CT), 1.25);
    assertEquals(output.on(X), -0.75);
    
    input = event(0,1,0,0);
    output = input.boost(v, NSense.Primed);
    assertEquals(output.on(CT), -0.75);
    assertEquals(output.on(X), 1.25);
    
    input = event(0,0,1,0);
    output = input.boost(v, NSense.Primed);
    assertSame(input, output);

    input = event(0,0,0,1);
    output = input.boost(v, NSense.Primed);
    assertSame(input, output);
  }
  
  @Test void unaffectedDimensions() {
    NVelocity v = NVelocity.of(0.6,  X);
    NEvent input = event(10,22,15,16);
    NEvent output = input.boost(v, NSense.Primed);
    assertEquals(output.on(Y), 15);
    assertEquals(output.on(Z), 16);
  }

  @Test void inAndOut() {
    NVelocity v = NVelocity.of(0.1, 0.2, 0.3);
    NEvent input = event(10,22,15,16);
    NEvent output1 = input.boost(v, NSense.Primed);
    NEvent output2 = output1.boost(v, NSense.Unprimed);
    assertSame(input, output2);
  }
  
  @Test void negativeVelocityMeansSwapPrimedAndUnprimed() {
    NVelocity v1 = NVelocity.of(0.1, 0.2, 0.3);
    NVelocity v2 = NVelocity.of(v1.times(-1));
    
    NEvent input = event(10,22,15,16);
    
    NEvent output1_primed = input.boost(v1, NSense.Primed);
    NEvent output1_unprimed = input.boost(v1, NSense.Unprimed);
    
    NEvent output2_primed = input.boost(v2, NSense.Primed);
    NEvent output2_unprimed = input.boost(v2, NSense.Unprimed);
    
    assertSame(output1_primed, output2_unprimed);
    assertSame(output1_unprimed, output2_primed);
  }
  
  @Test void nullVectorsMapToNullVectors() {
    nulls(fourDiff(1,1,0,0));
    nulls(fourDiff(1,0,1,0));
    nulls(fourDiff(1,0,0,1));
    nulls(fourDiff(10,10,0,0));
    nulls(fourDiff(-7,-7,0,0));
  }
  
  private void nulls(NFourDelta input) {
    assertEquals(input.square(), 0);
    NFourDelta output = input.boost(NVelocity.of(0.2, 0, 0), NSense.Primed);
    assertEquals(output.square(), 0);
  }
  
  private NEvent event(double a, double b, double c, double d) {
    return NEvent.of(a, b, c, d);
  }
  
  private NFourDelta fourDiff(double a, double b, double c, double d) {
    NEvent one = event(0,0,0,0);
    NEvent two = event(a,b,c,d);
    return NFourDelta.of(one, two);
  }
  
  private void assertSame(NEvent a, NEvent b) {
    assertTrue(equalsWithTinyDiff(a, b));
  }
  
  private boolean equalsWithTinyDiff(NEvent a, NEvent b) {
    if (a == b) return true;
    for(Axis axis : Axis.values()) {
      if (!equalsWithEpsilon(a.on(axis), b.on(axis))){
        return false; 
      }
    }
    return true;
  }
}
