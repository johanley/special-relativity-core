package sr.explore.speeds.rapidity;

import static sr.core.Util.NL;
import static sr.core.Util.sq;
import static sr.core.Util.sqroot;

import java.util.function.Function;

import sr.core.Axis;
import sr.core.component.Event;
import sr.core.component.Position;
import sr.core.hist.timelike.FindEvent;
import sr.core.hist.timelike.TimelikeHistory;
import sr.core.hist.timelike.UniformVelocity;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourDelta;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/**
 Rapidity equals a (Lorentzian) arc length on the unit hyperbola.
 
 <P>Start with: 
 <ul>
  <li>the history of a mass-particle having a given uniform velocity and passing through the origin.
  <li>the unit hyperboloid <em>H</em> having unit time-like interval with respect to the origin.
 </ul>
 
 <P> <em>H</em> has two branches.
  Find the intersection point <em>P</em> between the particle's history and <em>H<sup>+</sup></em>, the branch of <em>H</em> in the future-direction.
  
 <P>Compute the Lorentzian distance from (1,0,0,0) to the intersection point <em>P</em>.
 That value is the rapidity, arctanh β.
 Compare with the Euclidean distance.
  
*/
public final class RapidityAsLorentzianArcLength extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    Exploration rapidityAsArcLength = new RapidityAsLorentzianArcLength();
    rapidityAsArcLength.explore();
  }
  
  @Override public void explore() {
    forSpeed(0.8);
    outputToConsoleAnd("rapidity-as-lorentzian-arc-length");
  }
  
  private void forSpeed(double β) {
    double v_r = rapidity(β);
    Velocity velo = Velocity.of(β, Axis.X);
    add("Speed: " + β + ", rapidity: " + v_r + ", tanh(rapidity): " + Math.tanh(v_r));
    TimelikeHistory history = UniformVelocity.of(Position.origin(), velo);
    add(NL+"History through the origin with uniform velocity " + velo);
    Event intersection = intersection(history);
    add("Intersection point with unit hyperbola: " + intersection);
    add(NL+"Arc length along the unit hyperbola, from " + APEX + " to the intersection point:");
    add(" - Lorentzian: " + arcLength(intersection, LORENTZIAN) + "   (the rapidity)");
    add(" - Euclidean: " + arcLength(intersection, EUCLIDEAN) + "    (NOT the rapidity)");
    add(NL+"The arc is space-like, not time-like.");
    add("Along the arc, the fundamental quadratic form is negative.");
    add("To find the Lorentzian arc length, use the absolute value of the fundamental quadratic form.");
  }

  /** The apex of the H+ hyperboloid, on the future-directed time axis.  */
  private static final Event APEX = Event.of(1.0, Position.origin());
  private static final int EUCLIDEAN = +1;
  private static final int LORENTZIAN = -1;
  
  /** 
   @param β in the range (-1, +1).
   @return number in range (-infin, +infin). 
  */
  private double rapidity(double β) {
    //https://en.wikipedia.org/wiki/Inverse_hyperbolic_functions
    return 0.5*Math.log((1+β)/(1-β));
  }
  
  private Event intersection(TimelikeHistory history) {
    Function<Event, Double> criterion = (event -> FourDelta.withRespectToOrigin(event).square() - 1);
    FindEvent find = new FindEvent(history, criterion);
    double initial_guess = 1.0;
    double ct  = find.search(initial_guess);
    return history.event(ct);
  }
  
  /**
   Arc length from the apex (1,0,0,0) to the intersection point, as measured by a piece of 
   string along the unit hyperboloid (which here is a hyperbola).
   @param sign +1 for Euclidean length, -1 for Lorentzian length
  */
  private double arcLength(Event intersection, int sign) {
    double result = 0.0;
    double x = 0.0;
    double dx = 0.0001;
    while (x < intersection.x()) {
      //the derivative of the y(x) that describes the hyperbola y^2 = 1 + x^2.
      double derivative = x * Math.pow((1+sq(x)), -0.5);
      //Lorentz case : the tangent is always space-like! Hence the need for the absolute value
      double hypoteneuse = sqroot(
        Math.abs(
          sq(derivative * dx) + sign * sq(dx)
        )
      );
      result = result + hypoteneuse;
      x = x + dx;
    }
    return result;
  }
}
