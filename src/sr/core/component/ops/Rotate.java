package sr.core.component.ops;

import sr.core.vec3.AxisAngle;
import sr.core.vec3.ThreeVector;
import static sr.core.Axis.*;

import sr.core.component.Components;

/**
 Spatial rotation around a given axis. 
*/
public final class Rotate implements ComponentOp {
  
  /** Factory method. */
  public static Rotate of(AxisAngle axisAngle, Sense sense) {
    return new Rotate(axisAngle, sense);
  }
  
  /** Spatial rotation around a given axis. */
  @Override public Components applyTo(Components source) {
    //https://en.wikipedia.org/wiki/Rodrigues%27_rotation_formula
    Components result = Components.of(source.on(X), source.on(Y), source.on(Z));
    
    ThreeVector v = ThreeVector.of(source.on(X), source.on(Y), source.on(Z));
    double θ = sense.sign() * θ();
    if (Math.abs(θ) > 0) {
      double cosθ = Math.cos(θ);
      double sinθ = Math.sin(θ);
      ThreeVector e = unitVector();
      
      ThreeVector a = v.times(cosθ); 
      ThreeVector b = e.cross(v).times(sinθ); 
      ThreeVector c = e.times((1 - cosθ) * e.dot(v));
      ThreeVector rotated = a.plus(b).plus(c);
      result = Components.of(rotated.x(), rotated.y(), rotated.z());
    }
    
    return result;
  }
  
  private AxisAngle axisAngle;
  private Sense sense;

  private Rotate(AxisAngle axisAngle, Sense sense) {
    this.axisAngle = axisAngle;
    this.sense = sense;
  }
  
  private double θ() {
    return axisAngle.magnitude();
  }
  
  private ThreeVector unitVector() {
    //be careful of division by 0
    return axisAngle.times(1.0/θ());
  }
}
