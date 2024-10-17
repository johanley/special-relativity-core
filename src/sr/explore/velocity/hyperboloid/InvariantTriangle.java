package sr.explore.velocity.hyperboloid;

import sr.core.HyperbolicTriangle;
import sr.core.component.ops.Sense;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourVelocity;
import sr.explore.Exploration;
import sr.output.text.TextOutput;
import static sr.core.Util.*;

/**
 Triangles on the unit hyperboloid are invariant with respect to displacements.
 
 <P>In the context of special relativity and four-velocities, 
 displacements on the unit hyperboloid correspond to boost operations.
*/
public final class InvariantTriangle extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    Exploration invariantTriangle = new InvariantTriangle();
    invariantTriangle.explore();
  }
  
  @Override public void explore() {
    add("Build a triangle using 3 four-velocities on the unit hyperboloid.");
    add("Then boost it and see if it remains the same triangle, having the same angles and sides.");
    add(NL+"Start with a triangle having one corner at the apex (1,0,0,0) of the unit hyperboloid.");
    add("This means that one of the four-velocities corresponds to an object at rest.");
    FourVelocity u1 = FourVelocity.of(Velocity.zero());
    FourVelocity u2 = FourVelocity.of(Velocity.of(-0.2, 0.0, 0.0));
    FourVelocity u3 = FourVelocity.of(Velocity.of(0.1, 0.2, 0.3));
    HyperbolicTriangle triangle = HyperbolicTriangle.fromFourVelocities(u1, u2, u3);
    add(NL+"Four-velocity u1: " + u1 + " at rest");
    add("Four-velocity u2: " + u2);
    add("Four-velocity u3: " + u3);
    add(NL+"Hyperbolic triangle before boost: " + NL + triangle);
    
    Velocity boost_v = Velocity.of(0.5, 0.5, 0.2);
    add(NL+"Apply this boost velocity to the input four-velocities: " + boost_v);
    HyperbolicTriangle triangle_after_boost = HyperbolicTriangle.fromFourVelocities(
      u1.boost(boost_v, Sense.ChangeGrid), 
      u2.boost(boost_v, Sense.ChangeGrid), 
      u3.boost(boost_v, Sense.ChangeGrid)
    );
    add(NL+"Hyperbolic triangle after boost: " + NL + triangle_after_boost);
    add(NL+"The two triangles are equal: " + triangle.equals(triangle_after_boost));
    
    outputToConsoleAnd("invariant-triangle.txt");
  }
}