package sr.explore.interval.invariant;

import static sr.core.component.ops.Sense.ChangeGrid;

import java.util.function.UnaryOperator;

import sr.core.Util;
import sr.core.component.Event;
import sr.core.vec3.AxisAngle;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourDelta;
import sr.core.vec4.FourVector;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/**
 Transformations don't change the fundamental quadratic form, {@link FourVector#square()}.   
 
 <P>The prototype four-vector is the <em>displacement</em> in space-time. 
 It's not an event, but rather a <em>difference</em> between events. 
*/
public final class InvariantInterval extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    Exploration intervals = new InvariantInterval();
    intervals.explore();
  }
  
  @Override public void explore() {
    add("The interval (or squared-interval) between any two events is invariant.");
    add("Any combination of these fundamental transformations will leave the interval unaffected:");
    add(" - boost");
    add(" - rotation");
    add(" - reversal");
    add(" - displacement");

    Event a = Event.of(10.0, 1.0, 2.0, 1.0);
    Event b = Event.of(15.0, 3.0, 2.0, 5.0);
    showEffectOfTransformationsOnDifferenceBetween(a, b);
    add(Util.NL+dashes(119));
    
    add(Util.NL+"The above fails when you consider the interval between an event and the origin (0,0,0,0) of the coordinates being used.");
    add(Util.NL+"You need to use the DIFFERENCE between two events.");
    add("The problem is with the DISPLACEMENT operation.");
    add("The prototype 4-vector is not an event, but a difference between two events.");
    
    add(Util.NL+"This FAILS when a displacement operation is included:");
    showEffectOfTransformationsOnBareEvent(a, event -> withDisplacement(event),  "  " + withDisplacementString());
    
    add(Util.NL+"But it SUCCEEDS when the displacement operation is excluded:");
    showEffectOfTransformationsOnBareEvent(a,  event -> withoutDisplacement(event), "  " + withoutDisplacementString());
    
    outputToConsoleAnd("invariant-interval.txt");
  }
  
  private void showEffectOfTransformationsOnDifferenceBetween(Event a, Event b) {
    add(Util.NL + "Two events in K:");
    add("  " + a + " a");
    add("  " + b + " b");
    FourDelta displacement_K = FourDelta.of(a, b);
    add("Difference in K (b - a):");
    add("  " + displacement_K + " squared-interval:" + round(displacement_K.square()));
    
    FourDelta displacement_Kp = FourDelta.of(withDisplacement(a), withDisplacement(b));
    add(Util.NL+"Transform to K' using a mix of several operations: ");
    add("  " + withDisplacementString() );
    
    add(Util.NL + "The same two events in K' become:");
    add("  " + withDisplacement(a) + " a'");
    add("  " + withDisplacement(b) + " b'");
    add("Displacement in K' (b' - a'):");
    add("  " + displacement_Kp + " squared-interval:" + round(displacement_Kp.square()));
    
    double interval_K = displacement_K.square();
    double interval_Kp = displacement_Kp.square();
    add(Util.NL+"Difference in squared-interval between K and K': " + round(interval_Kp - interval_K));
  }
  
  private void showEffectOfTransformationsOnBareEvent(Event a, UnaryOperator<Event> transformer, String description) {
    add(Util.NL + "Single event in K:");
    add("  " + a);
    FourDelta diff_K = FourDelta.of(Event.origin(), a);
    add("Squared-interval with respect to the origin of K: " + round(diff_K.square()));
    
    Event a_Kp = transformer.apply(a);
    add(Util.NL+"Transform to K' using a mix of several operations: ");
    add("  " + description);
    
    add("Single event in K':");
    add("  " + a_Kp);
    FourDelta diff_Kp = FourDelta.of(Event.origin(), a_Kp);
    add("Squared-interval with respect to the origin of K': " + round(diff_Kp.square()));
    
    add(Util.NL+"Difference in squared-interval between K and K': " + round(diff_Kp.square() - diff_K.square()));
  }
  
  private Event withDisplacement(Event event) {
    Event result = withoutDisplacement(event);
    return result.moveZeroPointBy(some_displacement, ChangeGrid);
  }

  private Event withoutDisplacement(Event event) {
    Event result = event.boost(some_boost_v, ChangeGrid);
    result = result.rotate(some_rotation, ChangeGrid);
    result = result.reverseSpatialAxes();
    result = result.reverseClocks();
    return result;
  }
  
  private double round(double value) {
    return Util.round(value, 10);
  }
  
  private Velocity some_boost_v = Velocity.of(0.5, 0.1, 0.3);
  private AxisAngle some_rotation = AxisAngle.of(0.1, 0.4, 0.5);
  private FourDelta some_displacement = FourDelta.of(Event.origin(), Event.of(1.0, -2.0, -3.0, 4.0));  
  
  private String withDisplacementString() {
    return withoutDisplacementString() + " displacement " + some_displacement;
  }
  
  private String withoutDisplacementString() {
    return "boost " + some_boost_v + " rotation" + some_rotation + " reverse-spatial-axes reverse-clocks"; 
  }
}
