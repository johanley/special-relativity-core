package sr.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;

import java.util.Map;

import org.junit.jupiter.api.Test;

import sr.core.vector3.Velocity;
import sr.core.vector4.Builder;
import sr.core.vector4.FourVector;
import sr.core.vector4.WaveVector;

class LorentzTransformation3TEST {

  @Test void testZeroMapsToZero() {
    LorentzTransformation3 lt = LorentzTransformation3.of(Velocity.of(X, 0.256));
    TestVector input = vector(0,0,0,0);
    TestVector output = lt.primedVector(input);
    assertSame(input, output);
  }
  
  @Test void speedZeroDoesNothing() {
    LorentzTransformation3 lt = LorentzTransformation3.of(Velocity.of(X, 0.0));
    TestVector input = vector(10,1,2,3);
    TestVector output = lt.primedVector(input);
    assertSame(input, output);
  }
  
  @Test void speedUnityFails() {
    Exception exception = assertThrows(RuntimeException.class, () -> 
      LorentzTransformation3.of(Velocity.of(X, 1.0))
    );
  }
  
  @Test void unitVectors() {
    //this speed has a simple gamma=1.25 and -gamma*beta = -0.75
    LorentzTransformation3 lt = LorentzTransformation3.of(Velocity.of(X, 0.6));
    
    TestVector output = lt.primedVector(vector(1,0,0,0));
    assertEquals(output.on(CT), 1.25);
    assertEquals(output.on(X), -0.75);
    
    output = lt.primedVector(vector(0,1,0,0));
    assertEquals(output.on(CT), -0.75);
    assertEquals(output.on(X), 1.25);
    
    output = lt.primedVector(vector(0,1,0,0));
    assertEquals(output.on(CT), -0.75);
    assertEquals(output.on(X), 1.25);
    
    TestVector input = vector(0,0,1,0);
    output = lt.primedVector(input);
    assertSame(input, output);

    input = vector(0,0,0,1);
    output = lt.primedVector(input);
    assertSame(input, output);
  }
  
  @Test void unaffectedDimensions() {
    LorentzTransformation3 lt = LorentzTransformation3.of(Velocity.of(X, 0.6));
    TestVector output = lt.primedVector(vector(10,22,15,16));
    assertEquals(output.on(Y), 15);
    assertEquals(output.on(Z), 16);
  }

  @Test void inAndOut() {
    LorentzTransformation3 lt = LorentzTransformation3.of(Velocity.of(0.1, 0.2, 0.3));
    TestVector input = vector(10,22,15,16);
    TestVector output1 = lt.primedVector(input);
    TestVector output2 = lt.unPrimedVector(output1);
    assertSame(input, output2);
  }
  
  @Test void negativeVelocityMeansSwapPrimedAndUnprimed() {
    Velocity v1 = Velocity.of(0.1, 0.2, 0.3);
    Velocity v2 = Velocity.of(v1.times(-1));
    TestVector input = vector(10,22,15,16);
    
    LorentzTransformation3 lt1 = LorentzTransformation3.of(v1);
    TestVector output1_primed = lt1.primedVector(input);
    TestVector output1_unprimed = lt1.unPrimedVector(input);
    
    LorentzTransformation3 lt2 = LorentzTransformation3.of(v2);
    TestVector output2_primed = lt2.primedVector(input);
    TestVector output2_unprimed = lt2.unPrimedVector(input);
    
    assertSame(output1_primed, output2_unprimed);
    assertSame(output1_unprimed, output2_primed);
  }
  
  @Test void nullVectorsMapToNullVectors() {
    nulls(vector(1,1,0,0));
    nulls(vector(1,0,1,0));
    nulls(vector(1,0,0,1));
    nulls(vector(10,10,0,0));
    nulls(vector(-7,-7,0,0));
  }
  
  @Test void oneLTcanTransformMultipleTypes() {
    LorentzTransformation3 lt = LorentzTransformation3.of(Velocity.of(0.1, 0.2, 0.3));
    
    TestVector input = vector(1,2,3,4);
    TestVector output = lt.primedVector(input);
    
    WaveVector k_in = WaveVector.of(10, Axis.X);
    WaveVector k_out = lt.primedVector(k_in);
  }

  private void nulls(TestVector input) {
    assertEquals(input.square(), 0);
    LorentzTransformation3 lt = LorentzTransformation3.of(Velocity.of(0.2, 0, 0));
    TestVector output = lt.primedVector(input);
    assertEquals(output.square(), 0);
  }
  
  private TestVector vector(double a, double b, double c, double d) {
    return TestVector.of(a, b, c, d);
  }
  
  private static class TestVector extends FourVector implements Builder<TestVector> {
    public static TestVector of(double a, double b, double c, double d) {
      TestVector result = new TestVector();
      result.components.put(CT, a);
      result.components.put(X, b);
      result.components.put(Y, c);
      result.components.put(Z, d);
      return result;
    }
    @Override public TestVector build(Map<Axis, Double> parts) {
      TestVector result = new TestVector();
      for(Axis axis : Axis.values()) {
        result.components.put(axis, parts.get(axis));
      }
      return result;
    }
  }
  
  private void assertSame(TestVector a, TestVector b) {
    assertTrue(a.equalsWithTinyDiff(b));
  }
}
