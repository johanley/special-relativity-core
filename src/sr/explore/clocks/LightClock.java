package sr.explore.clocks;

import static sr.core.Util.NL;
import static sr.core.Util.radsToDegs;
import static sr.core.Util.round;

import sr.core.Axis;
import sr.core.history.DeltaBase;
import sr.core.history.History;
import sr.core.history.lightlike.MirrorReflection;
import sr.core.vector3.Direction;
import sr.core.vector3.Velocity;
import sr.core.vector3.transform.SpatialRotation;
import sr.core.vector4.Event;
import sr.core.vector4.transform.Boost;
import sr.explore.Exploration;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/**
A light clock consists of two mirrors, facing each other, between which a pulse of light bounces back and forth.


<P>Here, in frame K, a light clock has one mirror at the origin, and the other along the +X-axis. 
A pulse of light bounces back and forth between the two mirrors.
One full tick of the light clock means the pulse of light has gone from one mirror, to the other, and then back to the start. 
<pre>
           Y
           ^       
           |     
           |    
           |    
----------[]    *--&gt;     []------------&gt; X
           |    
           |     
           |     
</pre>


<P>The space-time history of the one complete cycle of the light pulse in the light clock has this general appearance in the frame K: 
<pre>
           CT
           ^       *
           |     *
           |   *  
           | *    
-----------*-----------&gt; X
           | *    
           |   *   
           |     *
           |       * 
</pre>

<P>This class will examine one complete cycle of the light clock, both in frame K and in a boosted frame K', where the boost is in 
a variety of directions.
*/
public final class LightClock extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    LightClock lightClock = new LightClock();
    lightClock.explore();
  }

  @Override public void explore() {
    add("Compare one tick of a light clock, as seen first in its rest frame K, and then in various boosted frames K'." + NL);
    
    History lightPulse = MirrorReflection.of(DeltaBase.origin(), Direction.of(-1, 0, 0));
    double ct = 10;
    Event a_K = lightPulse.event(-ct);
    Event b_K = lightPulse.event(+ct);
    add("First, one full cycle of the light clock in K:");
    add("K:   a " + a_K);
    add("K:   b " + b_K);
    add("K:(b-a)" + b_K.minus(a_K) + " one full tick of the clock."+NL);
    
    double β = 0.90;
    add("Now look at how those same two events a and b transform in boosted frames K'.");
    add("Boost speed: " + β);
    add("Boost direction: various (see table below)." + NL);
    add(tableHeader.row("Boost", "Boost", "", "Ratio", "Boost"));
    add(tableHeader.row("Velocity", "Angle", "K':(b'-a')", "(Δct')/(Δct)", "Gamma"));
    add(dashes(98));
    double increment = Math.PI / 4.0;
    Velocity velocity = Velocity.of(Axis.X, β);
    for(int idx = 0 ; idx < 8; ++idx) {
      Velocity boost_velocity = rotated(velocity, idx*increment);
      Boost boost = Boost.of(boost_velocity);
      Event a_Kp = boost.changeGrid(a_K);
      Event b_Kp = boost.changeGrid(b_K);
      double ratio = b_Kp.minus(a_Kp).ct() / b_K.minus(a_K).ct();
      add(table.row(
        boost_velocity, 
        boostAngle(boost_velocity), 
        b_Kp.minus(a_Kp), 
        ratio, 
        boost_velocity.Γ()
      ));
    }
    add(NL+"Regardless of the direction of the boost, the time dilation effect is always the same size.");
    outputToConsoleAnd("light-clock.txt");
  }

  //Boost, Angle, K':(b-a),  Ratio of times delct'/delct, Gamma  
  private Table table = new Table("%-24s", "%8.1f°", "  %-40s", "%10.5f     ", "%8.5f");
  private Table tableHeader = new Table("%-28s", "%-8s", "%-42s", "%-15s", "%-15s");

  /** Rotate the v in the XY plane. */
  private Velocity rotated(Velocity v, double angle) {
    SpatialRotation rot = SpatialRotation.of(Axis.Z, angle);
    return Velocity.of(rot.changeVector(v));
  }
  
  private double boostAngle(Velocity v) {
    return round(radsToDegs(Math.atan2(v.y(), v.x())), 1); //-pi to +pi 
  }
}
