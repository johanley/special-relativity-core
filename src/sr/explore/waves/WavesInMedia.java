package sr.explore.waves;

import static sr.core.Util.radsToDegs;

import java.util.function.Function;

import sr.core.Axis;
import sr.core.VelocityTransformation;
import sr.core.Util;
import sr.core.component.Event;
import sr.core.component.Position;
import sr.core.component.ops.Sense;
import sr.core.hist.timelike.FindEvent;
import sr.core.hist.timelike.TimelikeHistory;
import sr.core.hist.timelike.UniformVelocity;
import sr.core.vec3.NDelta;
import sr.core.vec3.NDirection;
import sr.core.vec3.NPhaseGradient;
import sr.core.vec3.NThreeVector;
import sr.core.vec3.NVelocity;
import sr.core.vec4.NFourPhaseGradientSlow;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/**
 Boost of a plane wave having a single phase-gradient (wave number) <em>k</em>, whose phase velocity is less than the speed limit <em>c</em>.
 
 <P>Example: a sound wave, or a light wave in a homogeneous transparent medium with a fixed refractive index.
 
 <P>It seems that such a case can only happen when a medium of some sort is present.
 Let K be the rest frame of the medium, and K' a boosted frame.
 In K, the plane wave has a given speed, direction, and has a single frequency ("monochromatic").
 
 <P>In K, let's take the spatial geometry of wavefronts as having this general appearance, where the waves move up, 
 and the neighboring wavefronts have a fixed difference in phase:
<pre>
          Y
          ^ 
          |   ^   ^   ^  
          |   .   .   .    
          |   .   .   .    
          |   .   .   .
          |   .   .   .
          |   .   .   .
          |   .   .   .
          |   .   .   .
          |   .   .   .
          |   .   .   .
          |   .   .   .
---------------------------------&gt; X
          |
          |
</pre>


<P>The task is to find the speed, direction, and phase-gradient (wave number) <em>k</em> in a boosted frame K'.
 
<P>To calculate the speed and direction of the wave in K', just picture a mass-particle traveling along with a wavefront.
(This is allowed since the speed of the wave is less than the speed limit.)
A velocity transformation for that mass-particle gives you speed and direction of the wave in K'.

<P>To calculate the phase-gradient (wave number) <em>k</em>, picture two parallel wavefronts as two parallel <em>sticks</em>:
<pre>
          Y
          ^          .
          |       .   .  K'     
          |        .   .  
          |         .   
          |  
          |   ^    ^   
          |   .    .   
          |   .    . 1   K    
          |   .    . 0  
---------------------------------&gt; X
          |
          |       
</pre>

<P>The rectangle made by the pair of sticks has different geometry in the two frames K and K'.

<P>To make the computation simple, choose the phase difference between the two wavefront-sticks in K to be exactly 1 radian.
This phase difference is a Lorentz-invariant fact (is not changed by any boost), so the phase difference between the same wavefronts in K' is also 1. 

<P>The phase-gradient (wave number) <em>k</em> has units of radians (of phase) per meter. 
We chose a phase difference of 1 radian between the wavefront-sticks.
This means the phase-gradient in K' is numerically equal to 1/D', where D' is the distance between the sticks in K'. 
*/
public final class WavesInMedia extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    WavesInMedia wavesInMedia = new WavesInMedia();
    wavesInMedia.explore();
  }
  
  @Override public void explore() {
    NVelocity u_Kp = phaseVelocity();
    phaseGradientKp(u_Kp);
    compareWithFormula();
    outputToConsoleAnd("waves-in-media.txt");
  }
  
  private NVelocity phaseVelocity() {
    add("The medium is stationary in K.");
    showPhaseGradient("K", phaseGradientK());
    add("K: distance between neighboring wavefronts having a phase-difference of 1 rad: " + 1.0/phaseGradientK());
    showVelo("K: phase-velocity u:", phaseVelocityK());

    add(dashes(80));
    add(Util.NL + "Compute the phase-velocity u' in K':"+Util.NL);
    add("Use the velocity addition formula.");
    NVelocity boost_v = boostVelocity();
    add("Boost from K to K' is towards the -X-axis: " + boost_v);
    NVelocity u_Kp = VelocityTransformation.primedVelocity(boost_v, phaseVelocityK());
    showVelo("K': phase-velocity u':", u_Kp);
    return u_Kp;
  }

  /** Compute the phase-gradient directly using histories of objects comoving with two neigboring wavefronts. */
  private void phaseGradientKp(NVelocity u_Kp) {
    add(Util.NL + dashes(80));
    add(Util.NL + "Compute the phase-gradient k' in K':" + Util.NL);
    add("Compute using the histories of objects.");
    //In K:
    //two ends 'a' and 'b' of a horizontal stick at X=0, X=1; the stick is comoving with wavefront-0:
    TimelikeHistory wavefront_0_hist_a_K = UniformVelocity.of(Position.of(0.0, 0.0, 0.0), phaseVelocityK());
    TimelikeHistory wavefront_0_hist_b_K = UniformVelocity.of(Position.of(1.0, 0.0, 0.0), phaseVelocityK());
    
    //a single object comoving with a wavefront-1 having a phase that's exactly 1 radian ahead of wavefront_0:
    double position_of_wavefront_1_K = 1.0/phaseGradientK();  //distance per radian 
    add("K: position of wavefront-1: " + position_of_wavefront_1_K);
    TimelikeHistory wavefront_1_hist_K = UniformVelocity.of(Position.of(0.0, position_of_wavefront_1_K, 0.0), phaseVelocityK());
    
    //now take a time-slice of each history in K'
    double ct_Kp = 10.0;
    
    //in K' the stick is not perpendicular to the phase-velocity
    Event wavefront_0_a_Kp = timeSliceOf(wavefront_0_hist_a_K, ct_Kp);
    Event wavefront_0_b_Kp = timeSliceOf(wavefront_0_hist_b_K, ct_Kp);
    NDelta stick_Kp = NDelta.of(wavefront_0_a_Kp.position(), wavefront_0_b_Kp.position());
    add("K': stick: " + stick_Kp);
    NDirection stick_direction_Kp = NDirection.of(stick_Kp);
    add("K': the stick is directed at this angle from the +X-axis: " + angleFromXAxis(stick_direction_Kp));
    add("K': the stick-direction and phase-velocity u' are not perpendicular. Dot product: " + round(stick_direction_Kp.dot(u_Kp)));
    
    Event wavefront_1_Kp = timeSliceOf(wavefront_1_hist_K, ct_Kp);
    double distance_0_to_1_Kp = distancePointToStick(      
      wavefront_1_Kp.position(), 
      wavefront_0_a_Kp.position(), 
      wavefront_0_b_Kp.position()
    );
    add("K': distance between neighboring wavefronts having a phase difference of 1 rad: " + round(distance_0_to_1_Kp));
    double phase_gradient_Kp = 1.0 / distance_0_to_1_Kp;
    showPhaseGradient("K'", phase_gradient_Kp);
  }

  /**Ref: https://arxiv.org/pdf/0801.3149v2 */
  private void compareWithFormula() {
    add(Util.NL + "Compare to the formula for 4-phase-gradient.");
    NPhaseGradient phaseGradient = NPhaseGradient.of(phaseGradientK(), Axis.Y);
    
    NFourPhaseGradientSlow k_K = NFourPhaseGradientSlow.of(phaseGradient, phaseVelocityK());
    showPhaseGradient("", k_K);
    NFourPhaseGradientSlow k_Kp = k_K.boost(boostVelocity(), Sense.ChangeGrid);
    showPhaseGradient("'", k_Kp);
  }

  private void showPhaseGradient(String frame, NFourPhaseGradientSlow k) {
    add("K" + frame + ": 4-phase-gradient k" + frame + ": " + k + ", spatial mag: " + round(k.spatialMagnitude())  + ", angle from +X-axis: " + angleFromXAxis(k.spatialComponents()) );
  }

  private void showPhaseGradient(String frame, double phaseGradient) {
    add(frame + ": phase-gradient k: " + round(phaseGradient) + " radians per unit distance.");
  }
  
  private void showVelo(String preamble, NVelocity velo) {
    add(preamble + " " + velo + 
        ", mag: " + round(velo.magnitude()) +
        ", angle from +X-axis: " + angleFromXAxis(velo)
    );
  }
  
  /** Return an event in K' coords. */
  private Event timeSliceOf(TimelikeHistory stick_0_hist_K, double ct_Kp) {
    Function<Event, Double> zero = (ev) -> ev.boost(boostVelocity(), Sense.ChangeGrid).ct() - ct_Kp;
    FindEvent find = new FindEvent(stick_0_hist_K, zero);
    double ct_K = find.search(1.0 /*first guess*/);
    Event event_K = stick_0_hist_K.event(ct_K);
    return event_K.boost(boostVelocity(), Sense.ChangeGrid);
  }

  /** The phase-velocity of the wave in frame K. */
  private NVelocity phaseVelocityK() {
    return NVelocity.of(0.4, Axis.Y);
  }
  
  /** In K, the phase of the wave changes by this many radians per unit distance. */
  private double phaseGradientK() {
    return 10.0;
  }

  /** Towards the -X-axis. */
  private NVelocity boostVelocity() {
    return NVelocity.of(-0.30, 0.0, 0.0);
  }
  
  /** Unsigned. Magnitude of the angle only. */
  private String angleFromXAxis(NThreeVector vector) {
    return angleBetween(vector, wrt_the_x_axis()) + "°";
  }
  
  private NDirection wrt_the_x_axis() {
    return NDirection.of(1, 0, 0);
  }

  /** Returns degrees. */
  private double angleBetween(NThreeVector a, NThreeVector b) {
    double result = radsToDegs(
      directionOf(a).angle(directionOf(b))
    );
    return round(result);
  }
  
  private NDirection directionOf(NThreeVector vector) {
    return NDirection.of(vector.x(),vector.y(),vector.z());
  }
  
  private double round(double value) {
    return Util.round(value, 4);
  }
  
  /** Use the formula for the area of a rectangle expressed as a cross-product. Then get the height of the triangle. */
  private double distancePointToStick(Position point, Position stick_end_1, Position stick_end_2) {
    NDelta a = NDelta.of(stick_end_1, stick_end_2);
    NDelta b = NDelta.of(stick_end_1, point);
    return a.cross(b).magnitude() / a.magnitude();
  }
}