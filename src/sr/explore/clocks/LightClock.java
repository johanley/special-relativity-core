package sr.explore.clocks;

import static sr.core.Util.NL;
import static sr.core.Util.radsToDegs;
import static sr.core.Util.round;

import sr.core.Axis;
import sr.core.component.Event;
import sr.core.component.ops.Sense;
import sr.core.hist.DeltaBase;
import sr.core.hist.History;
import sr.core.hist.lightlike.MirrorReflection;
import sr.core.vec3.AxisAngle;
import sr.core.vec3.Direction;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourDelta;
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
    Exploration lightClock = new LightClock();
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
    FourDelta delta_K = FourDelta.of(a_K, b_K);
    add("K:(b-a)" + delta_K + " one full tick of the clock."+NL);
    
    double β = 0.90;
    add("Now look at how those same two events a and b transform in boosted frames K'.");
    add("Boost speed: " + β);
    add("Boost direction: various (see table below)." + NL);
    add(tableHeader.row("Boost", "Boost", "", "Ratio", "Boost"));
    add(tableHeader.row("Velocity", "Angle", "K':(b'-a')", "(Δct')/(Δct)", "Gamma"));
    add(dashes(98));
    double increment = Math.PI / 4.0;
    Velocity velocity = Velocity.of(β, Axis.X);
    for(int idx = 0 ; idx < 8; ++idx) {
      Velocity boost_velocity = rotated(velocity, idx*increment);
      Event a_Kp = a_K.boost(boost_velocity, Sense.ChangeGrid);
      Event b_Kp = b_K.boost(boost_velocity, Sense.ChangeGrid);
      FourDelta delta_Kp = FourDelta.of(a_Kp, b_Kp);
      double ratio = delta_Kp.ct() / delta_K.ct();
      add(table.row(
        boost_velocity, 
        boostAngle(boost_velocity), 
        delta_Kp, 
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
    return v.rotate(AxisAngle.of(angle, Axis.Z), Sense.ChangeComponents);
  }
  
  private double boostAngle(Velocity v) {
    return round(radsToDegs(Math.atan2(v.y(), v.x())), 1); //-pi to +pi 
  }
}
