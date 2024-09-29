package sr.core.component.ops;

import sr.core.vec3.NAxisAngle;
import sr.core.vec3.NThreeVector;
import static sr.core.Axis.*;

import sr.core.component.NComponents;

/**
 Spatial rotation around a given axis. 
*/
public final class NRotate implements NComponentOp {
  
  /**
   Factory method.
   @param sense +1 applies the rotation to the components, -1 to the frame. 
  */
  public static NRotate of(NAxisAngle axisAngle, NSense sense) {
    return new NRotate(axisAngle, sense);
  }
  
  /** Spatial rotation around a given axis. */
  @Override public NComponents applyTo(NComponents source) {
    //https://en.wikipedia.org/wiki/Rodrigues%27_rotation_formula
    NComponents result = NComponents.of(source.on(X), source.on(Y), source.on(Z));
    
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
      result = NComponents.of(rotated.x(), rotated.y(), rotated.z());
    }
    
    return result;
  }
  
  private NAxisAngle axisAngle;
  private NSense sense;

  private NRotate(NAxisAngle axisAngle, NSense sense) {
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
