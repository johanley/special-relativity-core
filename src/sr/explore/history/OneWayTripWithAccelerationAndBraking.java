package sr.explore.history;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Util;
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
 One-way trip from the origin-event to a place on one of the spatial axes.
 All motion takes place on the one spatial axis.
 At τ=0 the object is at rest at the origin.
 
 <P>There are 3 {@link Leg}s to the trip:
 <ol>
  <li>uniform linear acceleration (speeding up from speed 0) in the plus-axis direction
  <li>coasting at a uniform velocity in the plus-axis direction; the speed of the coasting 
  is determined by the end-state of leg #1
  <li>uniform linear acceleration (slowing down to speed 0) in the minus-axis direction
 </ol>
 The 2 legs that accelerate have the same proper acceleration α (but in opposite directions), 
 and they also last the same length of proper-time. 
*/
public final class OneWayTripWithAccelerationAndBraking extends HistoryFromLegs {
  
  /** Run for various values, and output a file. */
  public static void main(String... args) {
    double gee = Physics.gAcceleration(1.0); //light years per year^2; be careful with the unit
    double[] ταs = {1.0,2.0,3.0,4.0,5.0};
    double τβ = 1.0;
    List<String> lines = new ArrayList<>();
    for (Double τα : ταs) {
      History trip = new OneWayTripWithAccelerationAndBraking(gee, τα, τβ, Axis.X);
      //System.out.println(trip.toString());
      lines.add(trip.toString() + Util.NL);
    }
    Util.writeToFile(OneWayTrip.class, "one-way-trip-with-acceleration"+Util.round(gee, 3)+"-" + τβ +".txt", lines);
  }
  
  /**
   Constructor.
   @param α the constant proper acceleration for the first and last legs. Be careful with the units.
   Must match the units you're using for having c=1. For example, light-years per year per year.  
   @param τα the proper-time duration of the first and last legs
   @param τβ the proper-time duration of the middle leg
   @param spatialAxis the axis along which all motion takes place
  */
  public OneWayTripWithAccelerationAndBraking(double α, double τα, double τβ, Axis spatialAxis) {
    this.α = α;
    this.τα = τα;
    this.τβ = τβ;
    this.spatialAxis = spatialAxis;
  }

  /** Proper time at the start-event is 0. */
  @Override public double τmin() { return 0.0; }
  
  @Override protected List<Leg> initLegs() {
    History hist1 = new UniformLinearAcceleration(spatialAxis, α, τmin(), τmin() + τα);
    Leg leg1 = new Leg(hist1, new NoOpTransform());
    
    CoordTransform backToPrevFrame = Displace.originTo(leg1.history().end()); 
    
    History hist2 = new UniformVelocity(spatialAxis, hist1.β(hist1.τmax()), hist1.τmax(), hist1.τmax() + τβ);
    Leg leg2 = new Leg(hist2, backToPrevFrame);
    
    backToPrevFrame = CoordTransformPipeline.join(
      Displace.originTo(leg2.history().end()),   //move origin
      Boost.alongThe(spatialAxis, hist2.β(hist2.τmax())), //match terminal speed, to have v=0 at t=0
      Reflect.the(spatialAxis)   //braking is opposite the speeding-up
    );

    History hist3 = new UniformLinearAcceleration(spatialAxis, α, hist2.τmax(), hist2.τmax() + τα);
    Leg leg3 = new Leg(hist3, backToPrevFrame);

    return Arrays.asList(leg1, leg2, leg3);
  }
  
  public double α() { return α; }
  public double τα() { return τα; }
  public double τβ() { return τβ; }
  public Axis spatialAxis() { return spatialAxis; }
  
  @Override public String toString() {
    String s = "  ";
    FourVector end1 = event(τmin() + τα);
    Double β1 = β(τmin() + τα);
    FourVector end2 = event(τmin() + τα + τβ);
    FourVector end3 = end();
    Double β3 = β(τmin() + τα + τβ + τα);
    
    String result = "α:"+α+s+ " τα:"+τα+s+ " τβ:"+τβ + Util.NL;
    result = result + "Total τ:"+(τmax() - τmin()) + Util.NL;
    result = result + "Leg1: end-β:"+β1+s+ "end-x:"+end1.x()+s+ "end-ct:"+end1.ct()+ Util.NL;
    result = result + "Leg2:     β:"+β1+s+ "end-x:"+end2.x()+s+ "end-ct:"+end2.ct()+ Util.NL; 
    result = result + "Leg3: end-β:"+β3+s+ "end-x:"+end3.x()+s+ "end-ct:"+end3.ct()+ Util.NL; 
    return result;
  }
  
  //PRIVATE 
  private double α;
  private double τα;
  private double τβ;
  private Axis spatialAxis;

}
