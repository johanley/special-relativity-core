package sr.explore.interval.invariant;

import sr.core.Util;
import sr.core.vector3.AxisAngle;
import sr.core.vector3.Velocity;
import sr.core.vector4.Event;
import sr.core.vector4.transform.Boost;
import sr.core.vector4.transform.Displacement;
import sr.core.vector4.transform.Reflection;
import sr.core.vector4.transform.Rotation;
import sr.core.vector4.transform.Transform;
import sr.core.vector4.transform.TransformPipeline;
import sr.output.text.TextOutput;

/**
 Transformations don't change the fundamental quadratic form, 
 the squared-interval <em>s<sup>2</sup></em> between two events.
 
 <P>The prototype 4-vector is the displacement in space-time. 
 It's not an event, but rather a <em>difference</em> between events. 
*/
public final class InvariantInterval extends TextOutput {
  
  public static void main(String[] args) {
    InvariantInterval intervals = new InvariantInterval();
    intervals.explore();
  }
  
  void explore() {
    lines.add("The interval (or squared-interval) between any two events is invariant.");
    lines.add("Any combination of these fundamental transformations will leave the interval unaffected:");
    lines.add(" - boost");
    lines.add(" - rotation");
    lines.add(" - displacement");
    lines.add(" - reflection");

    Event a = Event.of(10.0, 1.0, 2.0, 1.0);
    Event b = Event.of(15.0, 3.0, 2.0, 5.0);
    showEffectOfTransformationsOnDifferenceBetween(a, b);
    lines.add(Util.NL+dashes(100));
    
    lines.add(Util.NL+"The above fails when you consider the interval between an event and the origin (0,0,0,0).");
    lines.add("You need to use the DIFFERENCE between two events.");
    lines.add("The problem is with the DISPLACEMENT operation.");
    lines.add("The prototype 4-vector is not an event, but a difference between two events.");
    
    lines.add(Util.NL+"This FAILS when a displacement operation is included:");
    showEffectOfTransformationsOnBareEvent(a, multipleTransforms());
    
    lines.add(Util.NL+"But it SUCCEEDS when the displacement operation is excluded:");
    showEffectOfTransformationsOnBareEvent(a, multipleTransformsWithoutDisplacement());
    
    outputToConsoleAnd("invariant-interval.txt");
    
  }
  
  private void showEffectOfTransformationsOnDifferenceBetween(Event a, Event b) {
    lines.add(Util.NL + "Two events in K:");
    lines.add("  " + a);
    lines.add("  " + b);
    Event displacement_K = b.minus(a);
    lines.add("Displacement in K (b - a):");
    lines.add("  " + displacement_K + " squared-interval:" + round(displacement_K.square()));
    
    Event displacement_Kp = transform(b).minus(transform(a));
    lines.add(Util.NL+"Transform to K' using a mix of several operations: ");
    lines.add("  " + multipleTransforms());
    
    lines.add(Util.NL + "Two events in K':");
    lines.add("  " + transform(a));
    lines.add("  " + transform(b));
    lines.add("Displacement in K' (b' - a'):");
    lines.add("  " + displacement_Kp + " squared-interval:" + round(displacement_Kp.square()));
    
    double interval_K = displacement_K.square();
    double interval_Kp = displacement_Kp.square();
    lines.add(Util.NL+"Difference in squared-interval between K and K': " + round(interval_Kp - interval_K));
  }
  
  private void showEffectOfTransformationsOnBareEvent(Event a, Transform transform) {
    lines.add(Util.NL + "Single event in K:");
    lines.add("  " + a);
    lines.add("Squared-interval with respect to the origin: " + round(a.square()));
    
    Event a_Kp = transform.changeVector(a);
    lines.add(Util.NL+"Transform to K' using a mix of several operations: ");
    lines.add("  " + transform);
    
    lines.add("Single event in K':");
    lines.add("  " + a_Kp);
    lines.add("Squared-interval with respect to the origin: " + round(a_Kp.square()));
    
    double interval_K = a.square();
    double interval_Kp = a_Kp.square();
    lines.add(Util.NL+"Difference in squared-interval between K and K': " + round(interval_Kp - interval_K));
  }
  
  
  private Event transform(Event event) {
    Event event_Kp = multipleTransforms().changeFrame(event);
    return event_Kp;
  }
  
  private Transform multipleTransforms() {
    return TransformPipeline.join(
      Boost.of(Velocity.of(0.5, 0.1, 0.3)),
      Rotation.of(AxisAngle.of(0.1, 0.4, 0.5)),
      Displacement.of(1.0, -2.0, -3.0, 4.0),
      Reflection.allAxes()
    );
  }
  
  private Transform multipleTransformsWithoutDisplacement() {
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
