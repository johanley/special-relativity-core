package sr.explore.interval.invariant;

import java.util.function.UnaryOperator;

import sr.core.Util;
import sr.core.vector3.AxisAngle;
import sr.core.vector3.Velocity;
import sr.core.vector4.Event;
import sr.core.vector4.FourVector;
import sr.core.vector4.transform.Boost;
import sr.core.vector4.transform.Displacement;
import sr.core.vector4.transform.Reflection;
import sr.core.vector4.transform.Rotation;
import sr.core.vector4.transform.Transform;
import sr.core.vector4.transform.TransformPipeline;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/**
 Transformations don't change the fundamental quadratic form {@link FourVector#square()}. 
 
 <P>The prototype four-vector is the <em>displacement</em> in space-time. 
 It's not an event, but rather a <em>difference</em> between events. 
*/
public final class InvariantInterval extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    InvariantInterval intervals = new InvariantInterval();
    intervals.explore();
  }
  
  @Override public void explore() {
    add("The interval (or squared-interval) between any two events is invariant.");
    add("Any combination of these fundamental transformations will leave the interval unaffected:");
    add(" - boost");
    add(" - rotation");
    add(" - reflection");
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
    showEffectOfTransformationsOnBareEvent(a, event -> transformWithDisplacement(event),  "  " + transformsWithDisplacement());
    
    add(Util.NL+"But it SUCCEEDS when the displacement operation is excluded:");
    showEffectOfTransformationsOnBareEvent(a,  event -> transformWithoutDisplacement(event), "  " + transformsWithoutDisplacement());
    
    outputToConsoleAnd("invariant-interval2.txt");
  }
  
  private void showEffectOfTransformationsOnDifferenceBetween(Event a, Event b) {
    add(Util.NL + "Two events in K:");
    add("  " + a + " a");
    add("  " + b + " b");
    Event displacement_K = b.minus(a);
    add("Difference in K (b - a):");
    add("  " + displacement_K + " squared-interval:" + round(displacement_K.square()));
    
    Event displacement_Kp = transformWithDisplacement(b).minus(transformWithDisplacement(a));
    add(Util.NL+"Transform to K' using a mix of several operations: ");
    add("  " + transformsWithDisplacement() );
    
    add(Util.NL + "The same two events in K' become:");
    add("  " + transformWithDisplacement(a) + " a'");
    add("  " + transformWithDisplacement(b) + " b'");
    add("Displacement in K' (b' - a'):");
    add("  " + displacement_Kp + " squared-interval:" + round(displacement_Kp.square()));
    
    double interval_K = displacement_K.square();
    double interval_Kp = displacement_Kp.square();
    add(Util.NL+"Difference in squared-interval between K and K': " + round(interval_Kp - interval_K));
  }
  
  private void showEffectOfTransformationsOnBareEvent(Event a, UnaryOperator<Event> transformer, String description) {
    add(Util.NL + "Single event in K:");
    add("  " + a);
    add("Squared-interval with respect to the origin of K: " + round(a.square()));
    
    Event a_Kp = transformer.apply(a);
    add(Util.NL+"Transform to K' using a mix of several operations: ");
    add("  " + description);
    
    add("Single event in K':");
    add("  " + a_Kp);
    add("Squared-interval with respect to the origin of K': " + round(a_Kp.square()));
    
    double interval_K = a.square();
    double interval_Kp = a_Kp.square();
    add(Util.NL+"Difference in squared-interval between K and K': " + round(interval_Kp - interval_K));
  }
  
  private Event transformWithDisplacement(Event event) {
    Event event_Kp = transformsWithDisplacement().changeGrid(event);
    return event_Kp;
  }
  
  private Event transformWithoutDisplacement(Event event) {
    Event event_Kp = transformsWithoutDisplacement().changeGrid(event);
    return event_Kp;
  }

  private Transform transformsWithDisplacement() {
    return TransformPipeline.join(
      transformsWithoutDisplacement(),
      Displacement.of(1.0, -2.0, -3.0, 4.0)
    );
  }

  private Transform transformsWithoutDisplacement() {
    return TransformPipeline.join(
      Boost.of(Velocity.of(0.5, 0.1, 0.3)),
      Rotation.of(AxisAngle.of(0.1, 0.4, 0.5)),
      Reflection.allAxes()
    );
  }
  
  private double round(double value) {
    return Util.round(value, 10);
  }
}
