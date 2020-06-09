package sr.explore.commutation;

import java.util.ArrayList;
import java.util.List;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Util;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.Boost;
import sr.core.transform.FourVector;
import sr.explore.history.CircularOrbit;

/**
 Behaviour of multiple boosts, applied one after the other.
 
 <P>The behaviour differs according to whether or not the boosts are in the same direction (colinear).
 
 <P>Colinear: two boosts in sequence along an axis are equivalent to a third boost along the same axis.
 The velocity for the equivalent boost can be found from the transformation of velocity 
 {@link Physics#transformVelocityColinear(double, double)}. 

 <P>Colinear: switching the order of two boosts doesn't change the result.
   
 <P>Not colinear: switching the order of two boosts changes the result. 
*/
public final class MultipleBoosts {
  
  public static void main(String... args) {
    MultipleBoosts test = new MultipleBoosts();
    test.twoBoostsAlongTheSameAxis();
  }
  
  private void twoBoostsAlongTheSameAxis() {
    double β1 = -0.6;
    double β2 = 0.8;
    //any arbitrary 4-vector will do
    FourVector event = FourVector.from(2.32, -15.79, 0.0, 0.0, ApplyDisplaceOp.YES);
    Axis axis = Axis.X;
    List<String> lines = new ArrayList<>();
    
    FourVector firstBoost = boostThe(event, axis, β1);
    FourVector secondBoost = boostThe(firstBoost, axis, β2);
    
    lines.add("--- Two colinear boosts are equivalent to one boost ---");
    lines.add("First boost :  " + β1 + " " + firstBoost);
    lines.add("Second boost:  " + β2 + " " + secondBoost);

    double βequiv = Physics.transformVelocityColinear(β1, β2);
    FourVector afterEquiv = boostThe(event, axis, βequiv);
    lines.add("Equivalent boost: " + βequiv + " " + afterEquiv);

    lines.add(" ");
    lines.add("--- Reversing the order of two colinear boosts makes no difference---");
    firstBoost = boostThe(event, axis, β2);
    secondBoost = boostThe(firstBoost, axis, β1);
    lines.add("First boost :  " + β2 + " " + firstBoost);
    lines.add("Second boost:  " + β1 + " " + secondBoost);
    
    lines.add(" ");
    lines.add("--- Boosts don't commute if they aren't colinear ----");
    firstBoost = boostThe(event, Axis.X, β1);
    secondBoost = boostThe(firstBoost, Axis.Y, β2);
    lines.add("First boost  along X:  " + β1 + " " + firstBoost);
    lines.add("Second boost along Y:  " + β2 + " " + secondBoost);
    //switch the order
    firstBoost = boostThe(event, Axis.Y, β2);
    secondBoost = boostThe(firstBoost, Axis.X, β1);
    lines.add("First boost  along Y:  " + β2 + " " + firstBoost);
    lines.add("Second boost along X:  " + β1 + " " + secondBoost);
    
    Util.writeToFile(MultipleBoosts.class, "multiple-boosts.txt", lines);
  }
  
  private FourVector boostThe(FourVector event, Axis axis, double β) {
    return Boost.alongThe(axis, β).toNewFourVector(event);
  }
}