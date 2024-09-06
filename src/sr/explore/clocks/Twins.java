package sr.explore.clocks;

import static sr.core.Axis.X;

import sr.core.Util;
import sr.core.history.timelike.ThereAndBack;
import sr.core.history.timelike.TimelikeDeltaBase;
import sr.core.history.timelike.TimelikeHistory;
import sr.core.history.timelike.UniformVelocity;
import sr.core.vector3.Position;
import sr.core.vector3.Velocity;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/**
 Compare the proper-time elapsed in the case of the famous twins.
 One twin stays at home, the other leaves home and then later returns.
 Outbound, the traveling twin moves with uniform velocity; on the return trip, the twin moves  and with an opposite uniform velocity.
 
 <P>A sketch of the histories:
<pre>
            CT
            ^
        *   |  
        **  | 
        * * |   
        *  *|    
 -------*---*-----------&gt; X
        *  *|  
        * * |    
        **  |  
        *   |    
 </pre>
  
*/
public final class Twins extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    Twins twins = new Twins();
    twins.explore();
  }
  
  @Override public void explore() {
    add("One twin stays at home, the other twin takes a trip and returns home.");
    add("The voyage outbound is at a given uniform velocity.");
    add("The voyage inbound is at the opposite (uniform) velocity." + Util.NL);
    exploreIt(0.5);
    exploreIt(0.98);
    outputToConsoleAnd("twins.txt");
  }

  private void exploreIt(double β) {
    //these two values must be the same, in order to 
    //easily get the intersection points of two histories
    double LEFT_OF_ORIGIN = -50.0;
    double HALF_TIME = 50.0; 
    
    Velocity velocity = Velocity.of(X, β);
    
    //travel in from -X infinity to the origin, then back out to -X infinity:
    TimelikeHistory thereAndBack = ThereAndBack.of(TimelikeDeltaBase.origin(), velocity);
    
    //don't move from the given position 
    TimelikeHistory stayPut = UniformVelocity.stationary((Position.of(X, LEFT_OF_ORIGIN)));
    
    // ct = -/+ HALF_TIME identify the two events where the histories meet
    double τStay = properTimeInterval(stayPut, -HALF_TIME, HALF_TIME); 
    double τThereAndBack = properTimeInterval(thereAndBack, -HALF_TIME, HALF_TIME);
    
    add("β: " + round(β) + Util.NL + "Γ from formula: " + round(velocity.Γ()));
    add("Stay-at-home elapsed proper-time: " + τStay);
    add("There-and-back elapsed proper-time: " + round(τThereAndBack));
    add("Ratio of the proper-times: " + round(τStay/τThereAndBack) + Util.NL);
  }
  
  private double properTimeInterval(TimelikeHistory history, double ctStart, double ctEnd) {
    return history.τ(ctEnd) - history.τ(ctStart); 
  }
  
  private double round(double value) {
    return Util.round(value, 6);
  }
}