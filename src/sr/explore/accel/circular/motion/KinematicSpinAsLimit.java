package sr.explore.accel.circular.motion;

import static sr.core.Util.NL;
import static sr.core.Util.radsToDegs;
import static sr.core.Util.round;

import sr.core.Axis;
import sr.core.VelocityTransformation;
import sr.core.component.ops.Sense;
import sr.core.hist.timelike.CircularMotion;
import sr.core.hist.timelike.TimelikeDeltaBase;
import sr.core.hist.timelike.TimelikeMoveableHistory;
import sr.core.vec3.AxisAngle;
import sr.core.vec3.Velocity;
import sr.explore.Exploration;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/***
 Kinematic spin (Thomas precession) for circular motion as the <em>limit</em> of moving around a circuit shaped as an N-sided regular polygon.
 
 <P>An object goes in a circuit at constant speed. 
 The circuit returns back to its starting point.
 The circuit is in the shape of a regular polygon with N sides:
 <ul>
  <li>N=3: equilateral triangle
  <li>N=4: square
  <li>and so on
 </ul>   
 
 <P>As N gets large, the circuit approaches a circular shape.
 
 <P>This class will compare the kinematic rotation (Wigner rotation) resulting from one completion of such a circuit with the 
 comparable result derived from the kinematic spin (Thomas precession) formula (using a {@link CircularMotion}).
*/
public final class KinematicSpinAsLimit extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    KinematicSpinAsLimit thomas = new KinematicSpinAsLimit();
    thomas.explore();
  }
  
  @Override public void explore() {
    add("The kinematic rotation (Wigner rotation) for circular motion as the limit of moving around a circuit shaped as an N-sided regular polygon." + NL);
    double circuitLength = 10.0;
    double β = 0.50;
    tableFor(β, circuitLength);
    rotationFromThomasPrecessionFormula(circuitLength / (2*Math.PI), β);
    outputToConsoleAnd("kinematic-spin-as-limit.txt");
  }
  
  // N bends in the circuit, Total kinematic rotation (Wigner rotation) in one circuit
  private Table table = new Table("%-10s", "%-10s");
  
  private void tableFor(double β, double circuitLength) {
    add("Circuit Length: " + circuitLength);
    add("Speed: " + β + NL);
    add(table.row("N sides", "Kinematic rotation (Wigner rotation)"));
    add(table.row("", "after 1 circuit"));
    add(dashes(45));
    Velocity v = Velocity.of(β, Axis.X);
    for(int numSides = 3; numSides <= 360; ++numSides) {
      add(table.row(numSides, rotationAfterOneCircuit(numSides, v)));
    }
  }
  
  /**
   First calculate the the velocity transform for rotating the given v by 2pi/N radians.
   Then calculate the kinematic rotation (Wigner rotation) using the angle needed to turn (b+a) into (a+b). 
  */
  private String rotationAfterOneCircuit(int numSides, Velocity v_K) {
    double angle = 2*Math.PI/numSides;
    Velocity v_K_rotated = rotated(v_K, angle);
    Velocity boost_in_Kp_needed_to_rotate_v = VelocityTransformation.primedVelocity(v_K, v_K_rotated);
    
    //add the two velocities, v_K and boost_in_Kp_needed_to_rotate_v, in two different ways
    //for clarity, let's use temp aliases 'a' and 'b' 
    Velocity a = v_K;
    Velocity b = boost_in_Kp_needed_to_rotate_v;
    Velocity a_plus_b = VelocityTransformation.unprimedVelocity(a, b);
    Velocity b_plus_a = VelocityTransformation.unprimedVelocity(b, a);
    double angleBetweenAandB = b_plus_a.turnsTo(a_plus_b);
    return degrees(angleBetweenAandB * numSides);
  }
  
  /** Rotate in the XY-plane. */
  private Velocity rotated(Velocity boost_v, double angle) {
    return boost_v.rotate(AxisAngle.of(angle,  Axis.Z), Sense.ChangeComponents);
  }
  
  /** Use a {@link NCircularHistory}. */
  private void rotationFromThomasPrecessionFormula(double radius, double β) {
    add(NL+"Compare with the kinematic rotation (Wigner rotation) formula for circular motion."+NL);
    TimelikeMoveableHistory circle = CircularMotion.of(TimelikeDeltaBase.origin(), radius, β, Axis.Z, 0.0);
    double circumference = 2*Math.PI*radius;
    double timeForOneCircuit = circumference/β;
    AxisAngle rotation = circle.rotation(timeForOneCircuit);
    add("Circle circumference: " + roundIt(circumference));
    add("Circle radius: " + roundIt(radius));
    add("β: " + β);
    add("Time for 1 circuit: " + roundIt(timeForOneCircuit));
    add("Kinematic rotation (Wigner rotation) for 1 circuit: " + degrees(-rotation.magnitude()));
  }
  
  private String degrees(double rads) {
    return roundIt(radsToDegs(rads)) + "°";
  }
  
  private double roundIt(double value) {
    return round(value, 5);
  }
}
