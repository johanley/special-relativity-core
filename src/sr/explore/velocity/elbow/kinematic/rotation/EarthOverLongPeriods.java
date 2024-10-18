package sr.explore.velocity.elbow.kinematic.rotation;

import static sr.core.Util.arc_cosh;

import sr.core.Axis;
import sr.core.Physics;
import static sr.core.Util.*;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourVelocity;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

public final class EarthOverLongPeriods extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    EarthOverLongPeriods idealPlanet = new EarthOverLongPeriods();
    idealPlanet.explore();
  }
  
  @Override public void explore() {
    add("For circular motion, the kinematic rotation is simply cumulative, and adds up over time.");
    add("Calculate kinematic rotation in the case of the (low-speed) Earth orbiting the barycenter of the solar system, over a long time scale.");
    add("Assume a circular orbit.");
    
    double km_per_hour = 107208;
    double km_per_sec = km_per_hour / (60.0 * 60.0);
    double meters_per_sec = km_per_sec * 1000.0;
    double β = speedFromMetersPerSecond(meters_per_sec);
    add(NL+"Average speed of the Earth with respect to the barycenter of the solar system:");
    add(" " + km_per_hour + " km/h");
    add(" " + km_per_sec + " km/s");
    add(" " + meters_per_sec + " m/s");
    add(" " + β + " light-years/year (β)");
    
    double oneRev = rotationAfterOneRevolution(β);
    add(NL+"Rotation after one revolution: " + oneRev + " rads (about " + radsToMilliArcSeconds(oneRev) + " milliarcseconds per year)");
    add("Revolutions (years) needed for a cumulative rotation of 1 arc minute: " + rounded(revsForRotationOfOneArcMinute(β)) + " years.");
    add("Revolutions (years) needed for a cumulative rotation of 1 degree: " + rounded(60 * revsForRotationOfOneArcMinute(β)) + " years.");
    
    add(NL+"For kinematic rotation to affect the Earth's orientation by 1 degree, it takes about 600,000 years.");
    add("This calculation isn't physically meaningless.");
    add("The effect is simply 'added to' whatever happens to affect the Earth's orientation.");
    
    outputToConsoleAnd("earth-over-long-periods.txt");
  }
  
  private double radsToMilliArcSeconds(double rads) {
    return rounded(radsToDegs(rads) * 3600 * 1000);
  }
  
  private double speedFromMetersPerSecond(double meters_per_second) {
    return meters_per_second / Physics.C;
  }
  
  private double revsForRotationOfOneArcMinute(double β) {
    double one_arc_minute = degsToRads(1/60.0);
    return one_arc_minute / rotationAfterOneRevolution(β);
  }

  /** Depends solely on the speed, not on the radius of the circle. */
  private double rotationAfterOneRevolution(double β) {
    return areaCircleOnUnitHyperboloid(β);
  }
  
  private double areaCircleOnUnitHyperboloid(double β) {
    //https://www.whitman.edu/Documents/Academics/Mathematics/2014/brewert.pdf
    double r = arcInterval(β);
    return 2 * Math.PI * (Math.cosh(r) - 1); 
  }
  
  private double arcInterval(double β) {
    FourVelocity at_rest = FourVelocity.of(Velocity.zero());
    FourVelocity u = FourVelocity.of(β, Axis.X);
    return arc_cosh(at_rest.dot(u));
  }
  
  private double rounded(double value) {
    return round(value, 1);
  }


}
