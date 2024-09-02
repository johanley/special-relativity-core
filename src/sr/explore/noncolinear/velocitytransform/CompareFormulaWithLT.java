package sr.explore.noncolinear.velocitytransform;

import static sr.core.Axis.X;

import sr.core.Util;
import sr.core.VelocityTransformation;
import sr.core.history.timelike.TimelikeMoveableHistory;
import sr.core.history.timelike.UniformVelocity;
import sr.core.vector3.Position;
import sr.core.vector3.Velocity;
import sr.core.vector4.Event;
import sr.core.vector4.transform.Boost;
import sr.core.vector4.transform.Transform;
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
    add("Compare results from the velocity transformation formula with results directly from the Lorentz transformation."+Util.NL);
    add("With no loss of generality, in K we have:");
    add(" - a object velocity v in K in any old direction");
    add(" - a frame K' boosted with respect to K along the +X-axis.");
    add("So the task is to find v_K', the velocity with respect to the boosted frame K'." + Util.NL);
    show(0.8, Velocity.of(0.1,  0.2, 0.3));
    //show(0.8, Velocity.of(0.1, 0.0, 0.0)); //straight-line case
    outputToConsoleAnd("compare-formula-with-LT.txt");
  }

  private void show(double boost_speed, Velocity v) {
    formula(boost_speed, v);
    lorentzTransformation(boost_speed, v);
  }

  private void formula(double boost_speed, Velocity v) {
    add("Using the velocity transformation formula we get:");
    Velocity boost = Velocity.of(X, boost_speed);
    add("Boost: " + boost + " Velocity v_K:" + v);
    Velocity sum = VelocityTransformation.primedVelocity(boost, v);
    add("v_K' from formula:" + sum + " mag:" + mag(sum));
  }
  
  private void lorentzTransformation(double boost_speed, Velocity v) {
    add(Util.NL + "Using the Lorentz transformation directly we get:");
    //v corresponds to a history in K (through the origin)
    TimelikeMoveableHistory history_k = UniformVelocity.of(Position.origin(), v);
     
    //by differentiation, the v in K is
    double ct_K = 1.0;
    Velocity v_K = history_k.velocity(ct_K);
    add("v_K by approximating the derivative, using a history in K: " + v_K + " mag: " + mag(v_K));
     
    //boost is a primitive-boost along a direction in K
    //without loss of generality, we can take the boost direction as the X-axis
    Transform boost = Boost.of(X, boost_speed);
    
    Event a_K = history_k.event(ct_K);
    Event b_K = history_k.event(ct_K + 0.001);
     
    Event a_Kp = boost.changeFrame(a_K); 
    Event b_Kp = boost.changeFrame(b_K);
    Event displacement_Kp = b_Kp.minus(a_Kp);
     
    double dt_Kp = displacement_Kp.ct();
    Position dr_Kp = displacement_Kp.position();
    Velocity v_Kp = Velocity.of(
      dr_Kp.x() / dt_Kp,
      dr_Kp.y() / dt_Kp, 
      dr_Kp.z() / dt_Kp
    );
    add("v_K' by approximating the derivative, using a history in K and its boost-transform: " + v_Kp + " mag: " + mag(v_Kp));
  }
  
  private double mag(Velocity v) {
    return round(v.magnitude());
  }
  
  private double round(double value) {
    return Util.round(value, 5);
  }
}
