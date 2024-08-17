package sr.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import sr.core.event.Event;
import sr.core.vector.Velocity;

class LorentzTransformationTEST {

  @Test void testZeroMapsToZero() {
    LorentzTransformation lt = LorentzTransformation.of(Velocity.of(Axis.X, 0.256));
    Matrix output = lt.primedVector(vector(0,0,0,0));
    assertEquals(output.get(0, 0), 0.0);
    assertEquals(output.get(1, 0), 0.0);
    assertEquals(output.get(2, 0), 0.0);
    assertEquals(output.get(3, 0), 0.0);
  }
  
  @Test void speedZeroDoesNothing() {
    LorentzTransformation lt = LorentzTransformation.of(Velocity.of(Axis.X, 0.0));
    Matrix output = lt.primedVector(vector(10,1,2,3));
    assertEquals(output.get(0, 0), 10.0);
    assertEquals(output.get(1, 0), 1.0);
    assertEquals(output.get(2, 0), 2.0);
    assertEquals(output.get(3, 0), 3.0);
  }
  
  @Test void speedUnityFails() {
    Exception exception = assertThrows(RuntimeException.class, () -> 
      LorentzTransformation.of(Velocity.of(Axis.X, 1.0))
    );
  }
  
  @Test void unitVectors() {
    //this speed has a simple gamma=1.25 and -gamma*beta = -0.75
    LorentzTransformation lt = LorentzTransformation.of(Velocity.of(Axis.X, 0.6));
    
    Matrix output = lt.primedVector(vector(1,0,0,0));
    assertEquals(output.get(0,0), 1.25);
    assertEquals(output.get(1,0), -0.75);
    
    output = lt.primedVector(vector(0,1,0,0));
    assertEquals(output.get(0,0), -0.75);
    assertEquals(output.get(1,0), 1.25);
    
    output = lt.primedVector(vector(0,1,0,0));
    assertEquals(output.get(0,0), -0.75);
    assertEquals(output.get(1,0), 1.25);
    
    output = lt.primedVector(vector(0,0,1,0));
    assertEquals(output.get(0,0), 0);
    assertEquals(output.get(1,0), 0);
    assertEquals(output.get(2,0), 1);
    assertEquals(output.get(3,0), 0);
    
    output = lt.primedVector(vector(0,0,0,1));
    assertEquals(output.get(0,0), 0);
    assertEquals(output.get(1,0), 0);
    assertEquals(output.get(2,0), 0);
    assertEquals(output.get(3,0), 1);
  }
  
  @Test void unaffectedDimensions() {
    LorentzTransformation lt = LorentzTransformation.of(Velocity.of(Axis.X, 0.6));
    Matrix output = lt.primedVector(vector(10,22,15,16));
    assertEquals(output.get(2,0), 15);
    assertEquals(output.get(3,0), 16);
  }

  @Test void inAndOut() {
    LorentzTransformation lt = LorentzTransformation.of(Velocity.of(0.1, 0.2, 0.3));
    Matrix input = vector(10,22,15,16);
    Matrix output1 = lt.primedVector(input);
    Matrix output2 = lt.unPrimedVector(output1);
    assertEquals(input.get(0,0), output2.get(0,0), onlyTinyDiff);
    assertEquals(input.get(1,0), output2.get(1,0), onlyTinyDiff);
    assertEquals(input.get(2,0), output2.get(2,0), onlyTinyDiff);
    assertEquals(input.get(3,0), output2.get(3,0), onlyTinyDiff);
  }
  
  @Test void negativeVelocityMeansSwapPrimedAndUnprimed() {
    Velocity v1 = Velocity.of(0.1, 0.2, 0.3);
    Velocity v2 = Velocity.of(v1.times(-1));
    Matrix input = vector(10,22,15,16);
    
    LorentzTransformation lt1 = LorentzTransformation.of(v1);
    Matrix output1_primed = lt1.primedVector(input);
    Matrix output1_unprimed = lt1.unPrimedVector(input);
    
    LorentzTransformation lt2 = LorentzTransformation.of(v2);
    Matrix output2_primed = lt2.primedVector(input);
    Matrix output2_unprimed = lt2.unPrimedVector(input);
    
    //this works without the tiny-diff:
    assertEquals(output1_primed.get(0,0), output2_unprimed.get(0,0));
    assertEquals(output1_primed.get(1,0), output2_unprimed.get(1,0));
    assertEquals(output1_primed.get(2,0), output2_unprimed.get(2,0));
    assertEquals(output1_primed.get(3,0), output2_unprimed.get(3,0));

    assertEquals(output1_unprimed.get(0,0), output2_primed.get(0,0));
    assertEquals(output1_unprimed.get(1,0), output2_primed.get(1,0));
    assertEquals(output1_unprimed.get(2,0), output2_primed.get(2,0));
    assertEquals(output1_unprimed.get(3,0), output2_primed.get(3,0));
  }
  
  @Test void nullVectorsMapToNullVectors() {
    Matrix input = vector(1,1,0,0);
    nulls(input);
    
    input = vector(1,0,1,0);
    nulls(input);
    
    input = vector(1,0,0,1);
    nulls(input);
    
    input = vector(10,10,0,0);
    nulls(input);
    
    input = vector(-7,-7,0,0);
    nulls(input);
  }

  private void nulls(Matrix input) {
    Event input_event = asEvent(input);
    assertEquals(input_event.square(), 0);
    LorentzTransformation lt = LorentzTransformation.of(Velocity.of(0.2, 0, 0));
    Event output_event = asEvent(lt.primedVector(input));
    assertEquals(output_event.square(), 0);
  }
  
  private Matrix vector(double a, double b, double c, double d) {
    double[][] result = new double[4][1];
    result[0][0] = a;
    result[1][0] = b;
    result[2][0] = c;
    result[3][0] = d;
    return Matrix.of(result);
  }
  
  private Event asEvent(Matrix output) {
    return Event.of(
        output.get(0, 0), 
        output.get(1, 0), 
        output.get(2, 0), 
        output.get(3, 0) 
    );
  }
  
  private double onlyTinyDiff = Epsilon.ε();
}
