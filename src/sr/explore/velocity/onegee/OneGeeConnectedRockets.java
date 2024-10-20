package sr.explore.velocity.onegee;

import static sr.core.Axis.X;

import sr.core.Physics;
import sr.core.Util;
import sr.core.component.Event;
import sr.core.component.Position;
import sr.core.hist.timelike.TimelikeDeltaBase;
import sr.core.hist.timelike.TimelikeHistory;
import sr.core.hist.timelike.TimelikeMoveableHistory;
import sr.core.hist.timelike.UniformAcceleration;
import sr.explore.Exploration;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/**
Two rockets accelerate at the same uniform rate, in tandem, one behind the other.
<P>The two rockets are initially attached by a light, flimsy connector that doesn't stretch. Does the connector eventually break?

<P>The histories of the two rockets have this general appearance:
<pre>
        CT
        ^
        |           *     *
        |        *     *
        |      *     *
        |    *     *
        |  *     * 
        | *     *
        |*     *
--------*-----*------------&gt; X
        |
        | 
</pre>
<P>These two histories are identical, except for being displaced along the X-axis.
<P>This means that a time-slice having <em>ct=constant</em> always gives the same distance between the two histories.

<P>When you consider the histories of an accelerated stick (or, indeed, the two ends of one of the rockets), then 
the histories have a different form, because of flattening (Lorentz-Fitzgerald contraction):
<pre>
        CT
        ^
        |           *  *
        |        *   *
        |      *   *
        |    *    *
        |  *     * 
        | *     *
        |*     *
--------*-----*------------&gt; X
        |
        | 
</pre>
<P>In this case, a time slice having <em>ct=constant</em> gives <em>different</em> values for the distance between the two histories.
<P>The top pair of histories gives the distance between the two rockets, while the bottom pair gives the length of the object connecting the 
two rockets. The second is smaller. This means that the connector must break.
*/

public final class OneGeeConnectedRockets extends TextOutput implements Exploration {

  public static void main(String[] args) {
    Exploration rockets = new OneGeeConnectedRockets();
    rockets.explore();
  }
  
  @Override public void explore() {
    add("In a frame K, two rockets accelerate in tandem, completely in sync.");
    add("By definition, in K the two rockets remain separated by a fixed distance.");
    add("In K, the length of a connector between the rockets decreases with increasing speed." + Util.NL);
    add(tableHeader.row("Coordinate-time", "Rocket separation", "Connector length"));
    add(dashes(54));
    for(int τ = 0; τ <= 12; ++τ) {
      double rocketSeparation = rocketB().eventFromProperTime(τ).x() - rocketA().eventFromProperTime(τ).x();
      double ct = stickEndB().ct(τ);
      double connectorLength = stickEndB().event(ct).x() - stickEndA().event(ct).x();
      add(table.row(ct, rocketSeparation, connectorLength));
    }
    outputToConsoleAnd("one-gee-connected-rockets.txt");
  }
  
  private static final double CONNECTOR_LENGTH = 1.0;
  
  // ct, rocket separation, connector-length
  private Table tableHeader = new Table("%-18s", "%-20s", "%-15s");
  private Table table = new Table("%12.2f", "%18.2f", "%20.8f");
  
  private TimelikeMoveableHistory rocketA() {
    return UniformAcceleration.of(Position.origin(), X, Physics.ONE_GEE);
  }
  
  /** Same as rocket-a, but displaced to the right along the X-axis. */
  private TimelikeMoveableHistory rocketB() {
    return UniformAcceleration.of(TimelikeDeltaBase.of(Position.of(X, CONNECTOR_LENGTH)), X, Physics.ONE_GEE);
  }
  
  /** 'Stick' is really a place-holder for any extended object. */
  private TimelikeMoveableHistory stickEndA() {
    return UniformAcceleration.of(Position.origin(), X, Physics.ONE_GEE);
  }

  /** Use the stick-end-a as the starting point; then just add the contracted length of the connector to the X-coordinate. */
  private TimelikeHistory stickEndB() {
    TimelikeMoveableHistory historyA = stickEndA();
    return new TimelikeHistory() {
      public Event event(double ct) {
        Event event = historyA.event(ct);
        double Γ = historyA.velocity(ct).Γ();
        return Event.of(event.ct(), event.x() + CONNECTOR_LENGTH / Γ, event.y(), event.z());
      }
      public double ct(double τ) {
        return historyA.ct(τ);
      }
      public double τ(double ct) {
        return historyA.τ(ct);
      }
    };
  }
}
