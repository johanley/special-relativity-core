package sr.explore.velocitytransform;

import static sr.core.Axis.X;

import sr.core.VelocityTransformation;
import sr.core.Util;
import sr.core.component.Event;
import sr.core.component.Position;
import sr.core.component.ops.Sense;
import sr.core.hist.timelike.TimelikeMoveableHistory;
import sr.core.hist.timelike.UniformVelocity;
import sr.core.vec3.NVelocity;
import sr.core.vec4.NFourDelta;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/** 
 Compare the velocity addition formula with results coming directly from the Lorentz Transformation.
*/
public final class CompareFormulaWithLT extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    CompareFormulaWithLT velocity = new CompareFormulaWithLT();
    velocity.explore();
  }

  @Override public void explore() {
    add("Compare results from the velocity transformation formula with results directly from the Lorentz transformation."+Util.NL);
    add("With no loss of generality, in K we have:");
    add(" - a object velocity v in K in any old direction");
    add(" - a frame K' boosted with respect to K along the +X-axis.");
    add("So the task is to find v_K', the velocity with respect to the boosted frame K'." + Util.NL);
    show(0.8, NVelocity.of(0.1,  0.2, 0.3));
    //show(0.8, Velocity.of(0.1, 0.0, 0.0)); //straight-line case
    outputToConsoleAnd("compare-formula-with-LT.txt");
  }

  private void show(double boost_speed, NVelocity v) {
    formula(boost_speed, v);
    lorentzTransformation(boost_speed, v);
  }

  private void formula(double boost_speed, NVelocity v) {
    add("Using the velocity transformation formula we get:");
    NVelocity boost = NVelocity.of(boost_speed, X);
    add("Boost: " + boost + " Velocity v_K:" + v);
    NVelocity sum = VelocityTransformation.primedVelocity(boost, v);
    add("v_K' from formula:" + sum + " mag:" + mag(sum));
  }
  
  private void lorentzTransformation(double boost_speed, NVelocity v) {
    add(Util.NL + "Using the Lorentz transformation directly we get:");
    //v corresponds to a history in K (through the origin)
    TimelikeMoveableHistory history_k = UniformVelocity.of(Position.origin(), v);
     
    //by differentiation, the v in K is
    double ct_K = 1.0;
    NVelocity v_K = history_k.velocity(ct_K);
    add("v_K by approximating the derivative, using a history in K: " + v_K + " mag: " + mag(v_K));
     
    //boost is a primitive-boost along a direction in K
    //without loss of generality, we can take the boost direction as the X-axis
    NVelocity boost_v = NVelocity.of(boost_speed, X);
    
    Event a_K = history_k.event(ct_K);
    Event b_K = history_k.event(ct_K + 0.001);
     
    Event a_Kp = a_K.boost(boost_v, Sense.ChangeGrid); 
    Event b_Kp = b_K.boost(boost_v, Sense.ChangeGrid);
    NFourDelta displacement_Kp = NFourDelta.of(a_Kp, b_Kp);
     
    double dt_Kp = displacement_Kp.ct();
    Position dr_Kp = Position.of(
      displacement_Kp.x(), 
      displacement_Kp.y(), 
      displacement_Kp.z()
    );
    NVelocity v_Kp = NVelocity.of(
      dr_Kp.x() / dt_Kp,
      dr_Kp.y() / dt_Kp, 
      dr_Kp.z() / dt_Kp
    );
    add("v_K' by approximating the derivative, using a history in K and its boost-transform: " + v_Kp + " mag: " + mag(v_Kp));
  }
  
  private double mag(NVelocity v) {
    return round(v.magnitude());
  }
  
  private double round(double value) {
    return Util.round(value, 5);
  }
}
