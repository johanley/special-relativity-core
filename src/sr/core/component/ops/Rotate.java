package sr.core.component.ops;

import sr.core.vec3.NAxisAngle;
import sr.core.vec3.NThreeVector;
import static sr.core.Axis.*;

import sr.core.component.Components;

/**
 Spatial rotation around a given axis. 
*/
public final class Rotate implements ComponentOp {
  
  /** Factory method. */
  public static Rotate of(NAxisAngle axisAngle, Sense sense) {
    return new Rotate(axisAngle, sense);
  }
  
  /** Spatial rotation around a given axis. */
  @Override public Components applyTo(Components source) {
    //https://en.wikipedia.org/wiki/Rodrigues%27_rotation_formula
    Components result = Components.of(source.on(X), source.on(Y), source.on(Z));
    
    NThreeVector v = NThreeVector.of(source.on(X), source.on(Y), source.on(Z));
    double θ = sense.sign() * θ();
    if (Math.abs(θ) > 0) {
      double cosθ = Math.cos(θ);
      double sinθ = Math.sin(θ);
      NThreeVector e = unitVector();
      
      NThreeVector a = v.times(cosθ); 
      NThreeVector b = e.cross(v).times(sinθ); 
      NThreeVector c = e.times((1 - cosθ) * e.dot(v));
      NThreeVector rotated = a.plus(b).plus(c);
      result = Components.of(rotated.x(), rotated.y(), rotated.z());
    }
    
    return result;
  }
  
  private NAxisAngle axisAngle;
  private Sense sense;

  private Rotate(NAxisAngle axisAngle, Sense sense) {
    this.axisAngle = axisAngle;
    this.sense = sense;
  }
  
  private double θ() {
    return axisAngle.magnitude();
  }
  
  private NThreeVector unitVector() {
    //be careful of division by 0
    return axisAngle.times(1.0/θ());
  }
}
