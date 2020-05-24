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

  public static LorentzTransformation along(Axis spatialAxis, double β) {
    return new LorentzTransformation(spatialAxis, β);
  }

  /** Apply the transformation. Input one 4-vector, and output another. */
  public <T extends Vector<T>> T applyTo(T vector) {
    T result = null;
    int space = spatialAxis.idx();
    EntangledPair pair = entangle(part(CT.idx(), vector), part(space, vector));
    List<Double> args = new ArrayList<>();
    args.add(pair.time);
    for (Axis a : Axis.values()) {
      if (spatialAxis.idx() == a.idx()) {
        args.add(pair.space);
      }
      else if (a.idx() > CT.idx()){
        args.add(part(a.idx(), vector));
      }
    }
    result = vector.build(args.get(CT.idx()), args.get(X.idx()), args.get(Y.idx()), args.get(Z.idx()));
    return result;
  }
  
  //PRIVATE
  
  private Axis spatialAxis;
  private double β;
  
  private LorentzTransformation(Axis spatialAxis, double β) {
    this.spatialAxis = spatialAxis;
    this.β = β;
  }
  
  /** The time-component gets entangled with a single given spatial component. */
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
  
  private <T> double part(int idx, Vector<T> v) {
    return v.vectorParts()[idx].execute();
  }
}
