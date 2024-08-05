package sr.explore.noncolinear.velocitytransform;

import static sr.core.Axis.X;

import sr.core.Util;
import sr.core.VelocityTransformation;
import sr.core.event.Event;
import sr.core.event.transform.Boost;
import sr.core.history.MoveableHistory;
import sr.core.history.UniformVelocity;
import sr.core.vector.Position;
import sr.core.vector.Velocity;
import sr.output.text.TextOutput;

/** 
 Compare the velocity addition formula with results coming directly from the Lorentz Transformation.
*/
public final class CompareFormulaWithLT extends TextOutput {
  
  
  public static void main(String[] args) {
    CompareFormulaWithLT velocity = new CompareFormulaWithLT();
    velocity.explore();
  }

  void explore() {
    lines.add("Compare results from the velocity addition formula with results directly from the Lorentz transformation."+Util.NL);
    lines.add("With no loss of generality, in K we have:");
    lines.add(" - a velocity v in K in any old direction");
    lines.add(" - a frame K' boosted with respect to K along the +X-axis.");
    lines.add("So the task is to find v_K', the velocity with respect to the boosted frame K'." + Util.NL);
    show(0.8, Velocity.of(0.1,  0.2, 0.3));
    outputToConsoleAnd("compare-formula-with-LT.txt");
  }

  private void show(double boost_speed, Velocity v) {
    formula(boost_speed, v);
    lorentzTransformation(boost_speed, v);
  }

  private void formula(double boost_speed, Velocity v) {
    lines.add("Using the velocity addition formula we get:");
    Velocity boost = Velocity.of(X, boost_speed);
    lines.add("Boost: " + boost + " Velocity:" + v);
    Velocity sum = VelocityTransformation.primedVelocity(boost, v);
    lines.add("v_K' from formula:" + sum + " mag:" + mag(sum));
  }
  
  private void lorentzTransformation(double boost_speed, Velocity v) {
    lines.add(Util.NL + "Using the Lorentz transformation directly we get:");
    //v corresponds to a history in K (through the origin)
     MoveableHistory history_k = UniformVelocity.of(Position.origin(), v);
     
     //by differentiation, the v in K is
     double ct_K = 1.0;
     Velocity v_K = history_k.velocity(ct_K);
     lines.add("v_K by approximating the derivative, using a history in K: " + v_K + " mag: " + mag(v_K));
     
    //boost is a primitive-boost along a direction in K
    //without loss of generality, we can take the boost direction as the X-axis
     Boost boost = Boost.alongThe(X, boost_speed);
    
     Event a_K = history_k.event(ct_K);
     Event b_K = history_k.event(ct_K + 0.001);
     
     Event a_Kp = boost.changeEvent(a_K); 
     Event b_Kp = boost.changeEvent(b_K);
     Event displacement_Kp = b_Kp.minus(a_Kp);
     
     double dt_Kp = displacement_Kp.ct();
     Position dr_Kp = displacement_Kp.position();
     Velocity v_Kp = Velocity.of(
       dr_Kp.x() / dt_Kp,
       dr_Kp.y() / dt_Kp, 
       dr_Kp.z() / dt_Kp
     );
     lines.add("v_K' by approximating the derivative, using a history in K and its boost-transform: " + v_Kp + " mag: " + mag(v_Kp));
  }
  
  
  private double mag(Velocity v) {
    return round(v.magnitude());
  }
  
  private double round(double value) {
    return Util.round(value, 5);
  }
}
