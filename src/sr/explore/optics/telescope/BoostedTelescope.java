package sr.explore.optics.telescope;

import static sr.core.Util.NL;
import static sr.core.Util.radsToDegs;
import static sr.core.Util.round;

import java.util.function.Function;

import sr.core.Axis;
import sr.core.component.Event;
import sr.core.component.Position;
import sr.core.component.ops.Sense;
import sr.core.hist.timelike.FindEvent;
import sr.core.hist.timelike.TimelikeHistory;
import sr.core.hist.timelike.UniformVelocity;
import sr.core.vec3.Direction;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourDelta;
import sr.core.vec4.FourVector;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/**
<P>A telescope pointed at a star, as seen in a boosted frame.

<P>In a frame K, a telescope is stationary and pointing at a star.
The <em>spatial</em> geometry for the directions has this general appearance:
<pre>
          Y
          ^
          |                  *
          |               .
          |             .
          |           .
          |         .
          |       .       
          |     #   
          |   #      
          | #       
----------#-----------------------&gt; X
          |
          |       
</pre>
The telescope(#) is at 45° to the +X-axis.
The light (.) from a star (*) is directed into the telescope, and moves from the objective lens to the eyepiece.
(Let's take the telescope to be a refractor.)

<P>The simple fact that the light traverses the telescope, and is viewed at the eyepiece, is Lorentz-invariant: 
it's true in all frames.
No boost can change that.

<P>A boosted frame K' travels in the +X direction.
In K', the <em>spatial</em> geometry changes, and has this general appearance:
<pre>
          Y
          ^
          |                            *
          |                     .
          |                .
          |           .
          |       .
          |   .       
          |  #   
          | #      
          |#       
----------#-----------------------&gt; X
          |
          |       
</pre>
In K', the telescope (#) is flattened (Lorentz-Fitzgerald contraction), and the direction of the light (.) changes because of aberration.
The two directions don't line up anymore.
Superficially, this makes you think that it's not possible for the light to traverse the telescope.
But that of course is false, since, as stated above, the fact that the light traverses the telescope is Lorentz-invariant, 
and can't be affected by any boost.

<P>The mismatch in the directions is an interesting example of the difference between a time-slice and a light-slice.

<P>The history of the telescope forms an extended <em>sheet</em> in space-time.
One end of the sheet is for the lens, the other end is for the eyepiece.
Select a detection-event on the history of the eyepiece.
Using that detection-event as a base, make two slices across the history of the telescope:
<ul> 
 <li>a time-slice, to find the direction of the telescope in space
 <li>a light-slice (from the past light-cone of the detection-event), to find the direction of the incoming light 
</ul>  

<P>In the unboosted frame K, the two directions are the same.
In the boosted frame K', the two directions are different. 
*/
public final class BoostedTelescope extends TextOutput implements Exploration {

  public static void main(String[] args) {
    BoostedTelescope telescope = new BoostedTelescope();
    telescope.explore();
  }

  @Override public void explore() {
    
    add("All angles are with respect to the +X-axis." + NL);
    
    //the telescope is stationary in the unboosted frame K:
    TimelikeHistory eyepiece_history_K = UniformVelocity.of(Position.origin(), Velocity.zero());
    //the length of the scope is set to 1:
    double distance = 1.0/Math.sqrt(2.0); 
    Position where = Position.of(distance, distance, 0.0); //where the objective lens is  
    TimelikeHistory lens_history_K = UniformVelocity.of(where, Velocity.zero());
    
    //a photon moves into the telescope (at the lens) and out of the telescope (at the eyepiece)
    //because the geometry is simple in frame K, it's simple to find two corresponding events one the history of that photon:
    Event lens_K = lens_history_K.event(0);  //just pick one to start with
    Event eyepiece_K = eyepiece_history_K.event(1); //because the scope has unit length!
    add("K : angle of telescope: " + angleFromXAxis(FourDelta.of(eyepiece_K, lens_K)));
    
    //then boost from K to K'
    Velocity boost_v = Velocity.of(0.75, Axis.X);
    add(NL + "From K to K', use a boost " + boost_v + NL);
    Event lens_Kp = lens_K.boost(boost_v, Sense.ChangeGrid);
    Event eyepiece_Kp = eyepiece_K.boost(boost_v, Sense.ChangeGrid);
    
    Event event_from_time_slice_Kp = timeSliceEventInKp(eyepiece_Kp.ct(), lens_history_K, boost_v);
    add("K' time-slice: angle of telescope : " + angleFromXAxis(FourDelta.of(eyepiece_Kp, event_from_time_slice_Kp)) + " (the flattening effect)");
    add("K' light-slice: angle of incoming detected light ray : " + angleFromXAxis(FourDelta.of(eyepiece_Kp, lens_Kp)) + " (the aberration effect)");
    
    outputToConsoleAnd("boosted-telescope.txt");
    
  }
  
  /** Degrees. */
  private double angleBetween(FourDelta a, FourDelta b) {
    double result = radsToDegs(
      directionOf(a).angle(directionOf(b))
    );
    return roundIt(result);
  }
  
  private double roundIt(double value) {
    return round(value, 3);
  }
  
  private Direction directionOf(FourVector event) {
    return Direction.of(event.x(),event.y(),event.z());
  }
  
  private String angleFromXAxis(FourDelta delta) {
    return angleBetween(delta, wrt_the_x_axis()) + "°";
  }
  
  /** 
   Find the event in the history of the lens having the given coordinate time (ct_Kp).
   @return the event using K' coordinates. 
  */
  private Event timeSliceEventInKp(double ct_Kp, TimelikeHistory history_K, Velocity boost_v) {
    Function<Event, Double> criterion = (event_K) -> event_K.boost(boost_v, Sense.ChangeGrid).ct() - ct_Kp; 
    FindEvent find = new FindEvent(history_K, criterion);
    double guess_ct_K = 1.0;
    double ct_K = find.search(guess_ct_K); 
    Event target_K = history_K.event(ct_K);
    return target_K.boost(boost_v, Sense.ChangeGrid);
  }
  
  private FourDelta wrt_the_x_axis() {
    return FourDelta.of(Event.origin(), Event.of(0, 1, 0, 0));
  }
}