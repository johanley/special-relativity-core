package sr.explore.optics.mirror;

import static sr.core.Util.NL;
import static sr.core.Util.radsToDegs;
import static sr.core.Util.round;

import sr.core.Axis;
import static sr.core.component.ops.Sense.*;
import sr.core.vec3.Direction;
import sr.core.vec3.PhaseGradient;
import sr.core.vec3.ThreeVector;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourPhaseGradient;
import sr.explore.Exploration;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/**
 <P>Reflection from a moving mirror.

 <P>In the rest frame K of a mirror, a plane monochromatic light wave hits a mirror and bounces off.
 The angle of incidence equals the angle of reflection.

 <P>Let's take the spatial geometry as:
<pre>
           Y
 reflected ^       
     *     |     
       *   #     
         * # mirror   
-----------#-----------&gt; X
         * #     
       *   #    
     *     | 
  incident |  
</pre>

<P>In a boosted frame K' viewing the same phenomena, however, the geometry is different.
You can explore this using a pair of {@link FourPhaseGradient} objects and a {LorentzTransformation}.
The two {@link FourPhaseGradient} objects correspond to the incoming and outgoing monochromatic plane waves.

<P>This class doesn't model the polarization of the wave.
*/
public final class MovingMirror extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    Exploration movingMirror = new MovingMirror();
    movingMirror.explore();
  }
  
  @Override public void explore() {
    add("A monochromatic plane wave reflects from a flat mirror in the YZ plane.");
    add("In frame K, the mirror is at rest.");
    add("In a boosted frame K', with the boost along the +/-X-axis, the mirror is moving with some velocity." + NL);
    add("Compare the wave-vectors for the incident and reflected waves, between K and K'.");
    
    double ω = 1.0;
    FourPhaseGradient incident_K = FourPhaseGradient.of(PhaseGradient.of(ω, Direction.of(1,1,0)));
    FourPhaseGradient reflected_K = FourPhaseGradient.of(PhaseGradient.of(ω, Direction.of(-1,1,0)));
    
    add(NL+ "In K the pair of waves have directions that agree with the law of reflection:");
    add("K: incident  " + incident_K + "  θi = " + angleFromXAxis(incident_K.spatialComponents()) + "°");
    add("K: reflected " + reflected_K + " θr = " + complement(angleFromXAxis(reflected_K.spatialComponents())) + "°");

    add(NL+"Now view the same wave-vectors in a boosted frame K'." + NL);

    add("For boosts in the -X-axis direction:" + NL);
    tableFor(incident_K, reflected_K, -1);
    
    add(NL+ "For boosts in the +X-axis direction:" + NL);
    tableFor(incident_K, reflected_K, +1);
    
    add(NL+"In the K' frame, the wave-vectors have different directions and frequencies.");
    
    outputToConsoleAnd("moving-mirror.txt");
  }

  private void tableFor(FourPhaseGradient incident_K, FourPhaseGradient reflected_K, int sign) {
    add(tableHeader.row("Boost", "K':ω-in", "K':ω-ref", "K':θ-in", "K':θ-ref"));
    add(dashes(57));
    for(int idx = 1 ; idx <= 6; ++idx) {
      Velocity boost_v = Velocity.of(sign*idx/10.0, Axis.X);
      FourPhaseGradient incident_Kp = incident_K.boost(boost_v, ChangeGrid);
      FourPhaseGradient reflected_Kp = reflected_K.boost(boost_v, ChangeGrid);
      add(table.row(
        boost_v, 
        incident_Kp.ct(),
        reflected_Kp.ct(),
        angleFromXAxis(incident_Kp.spatialComponents()),
        complement(angleFromXAxis(reflected_Kp.spatialComponents()))
      ));
    }
  }

  //Boost, K':incoming-ω,  K':outgoing-ω, K':incoming-θ,  K':outgoing-θ
  private Table table = new Table("%-17s", "  %8.5f", " %8.5f", "%8.1f° ", "%8.1f°");
  private Table tableHeader = new Table("%-20s", "%-9s", "%-11s", "%-10s", "%-12s");
  
  private double angleFromXAxis(ThreeVector v) {
    return round(radsToDegs(Math.atan2(v.y(), v.x())), 1); //-pi to +pi 
  }
  
  private double complement(double value) {
    return 180.0 - value;
  }
}