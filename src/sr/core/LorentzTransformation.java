package sr.core;

import java.util.ArrayList;
import java.util.List;
import static sr.core.Axis.*;

/**
 Lorentz Transformation.
 
 It's important to note that the Lorentz transformation applies not just to events, but 
 to all 4-vectors. This is related to the fact that the transformation uses dimensionless 
 coefficients (β and Γ). That means that output-units are always the same as input-units, 
 regardless of the input. 
*/
public class LorentzTransformation {

  /*
  public static void main(String[] args) {
    Event a = Event.from(1.0, 0.0, 0.0, 0.0);
    LorentzTransformation lt = LorentzTransformation.along(Axis.X, 0.87);
    Event b = lt.applyTo(a, Event::from);
    System.out.println(b);
    System.out.println(b.intervalSquaredBetween(Event.ORIGIN));
  }
  */

  /** 
   Factory method.
   Transforms from K to K'. K' is moving with respect to K along the given axis with speed β.
   If the speed is positive (negative), then the relative motion is along the positive (negative) direction of the axis.    
  */
  public static LorentzTransformation along(Axis spatialAxis, double β) {
    return new LorentzTransformation(spatialAxis, β);
  }

  /** 
   Apply the transformation. Input one 4-vector, and output another (a new object).
   
   @param builder the policy for building the new 4-vector returned by this method. 
   Usually a method-reference to a static factory method.
  */
  public <T extends FourVector<T>> T applyTo(T vector, VectorBuilder<T> builder) {
    int space = spatialAxis.idx();
    EntangledPair pair = entangle(part(CT.idx(), vector), part(space, vector));
    List<Double> parts = new ArrayList<>();
    parts.add(pair.time);
    for (Axis a : Axis.values()) {
      if (spatialAxis.idx() == a.idx()) {
        parts.add(pair.space);
      }
      else if (a.idx() > CT.idx()){
        parts.add(part(a.idx(), vector));
      }
    }
    return builder.from(parts.get(CT.idx()), parts.get(X.idx()), parts.get(Y.idx()), parts.get(Z.idx()));
  }
  
  //PRIVATE
  
  private Axis spatialAxis;
  private double β;
  
  private LorentzTransformation(Axis spatialAxis, double β) {
    this.spatialAxis = spatialAxis;
    this.β = β;
  }
  
  /** 
   The time-component gets entangled with a single given spatial component.
   This is the core of the Lorentz Transformation. 
  */
  private EntangledPair entangle(double ct, double space) {
    double Γ = Physics.Γ(β);
    EntangledPair result = new EntangledPair();
    result.time =     Γ*ct - Γ*β*space;
    result.space = -Γ*β*ct +   Γ*space;
    return result;
  }

  /** This was created to remove code repetition. */
  private static final class EntangledPair {
    double time;
    double space;
  }
  
  private <T> double part(int idx, FourVector<T> v) {
    return v.parts()[idx].val();
  }
}
