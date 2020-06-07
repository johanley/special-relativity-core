package sr.explore.history;

import java.util.Arrays;
import java.util.List;

import sr.core.Axis;
import sr.core.history.History;
import sr.core.history.HistoryFromLegs;
import sr.core.history.Leg;
import sr.core.history.UniformLinearAcceleration;
import sr.core.history.UniformVelocity;
import sr.core.transform.Boost;
import sr.core.transform.CoordTransform;
import sr.core.transform.CoordTransformPipeline;
import sr.core.transform.Displace;
import sr.core.transform.FourVector;
import sr.core.transform.NoOpTransform;
import sr.core.transform.Reflect;

/**
 One-way trip from the origin-event to a place B (on one of the spatial axes).
 All motion takes place on the one spatial axis.
 At τ=0 the object is at rest at the origin.
 
 <P>There are 3 {@link Leg}s to the trip:
 <ol>
  <li>uniform linear acceleration (speeding up from speed 0) in the plus-axis direction
  <li>coasting at a uniform velocity in the plus-axis direction; the speed of the coasting 
  is determined by the end-state of leg #1
  <li>uniform linear acceleration (slowing down to speed 0) in the minus-axis direction
 </ol>
 The 2 legs that accelerate have the same proper acceleration α (but in opposite directions); 
 they also last the same length of proper-time. 
*/
public final class OneWayTripWithAccelerationAndBraking extends HistoryFromLegs {
  
  /**
   Constructor.
   @param α the constant proper acceleration for the first and last legs  
   @param τα the proper-time duration of the first and last legs
   @param τβ the proper-time duration of the middle leg
  */
  public OneWayTripWithAccelerationAndBraking(double α, double τα, double τβ, Axis axis) {
    this.α = α;
    this.τα = τα;
    this.τβ = τβ;
    this.axis = axis;
  }

  /** Proper time at the start-event is 0. */
  @Override public double τmin() { return 0.0; }
  
  @Override protected List<Leg> initLegs() {
    History hist1 = new UniformLinearAcceleration(axis, α, τmin(), τmin() + τα);
    Leg leg1 = new Leg(hist1, new NoOpTransform());
    
    CoordTransform backToLeg1 = Displace.originTo(leg1.history().end()); //move origin
    
    double terminalβ1 = hist1.β(hist1.τmax());
    History hist2 = new UniformVelocity(axis, terminalβ1, hist1.τmax(), hist1.τmax() + τβ);
    Leg leg2 = new Leg(hist2, backToLeg1);
    
    double terminalβ2 = hist2.β(hist2.τmax());
    CoordTransform displace = Displace.originTo(leg2.history().end()); //move origin
    CoordTransform boost = Boost.alongThe(axis, terminalβ2); //match speed, to have v=0 at t=0
    CoordTransform reflect = Reflect.the(axis); //to have braking along the axis, not acceleration
    CoordTransform backToLeg2 = new CoordTransformPipeline(displace, boost, reflect);

    History hist3 = new UniformLinearAcceleration(axis, α, hist2.τmax(), hist2.τmax() + τα);
    Leg leg3 = new Leg(hist3, backToLeg2);

    return Arrays.asList(leg1, leg2, leg3);
  }
  
  public double α() { return α; }
  public double τα() { return τα; }
  public double τβ() { return τβ; }
  
  //PRIVATE 
  private double α;
  private double τα;
  private double τβ;
  private Axis axis;

}
