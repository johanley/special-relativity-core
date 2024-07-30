package sr.explore.accel.speed;

import static sr.core.Axis.*;
import sr.core.Physics;
import sr.core.Util;
import sr.core.event.Event;
import sr.core.history.DeltaBase;
import sr.core.history.History;
import sr.core.history.MoveableHistory;
import sr.core.history.UniformAcceleration;
import sr.core.vector.Position;
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

public final class ConnectedRockets  extends TextOutput {

  public static void main(String[] args) {
    ConnectedRockets rockets = new ConnectedRockets();
    rockets.explore();
  }
  
  void explore() {
    lines.add("In a frame K, two rockets accelerate in tandem, completely in sync.");
    lines.add("By definition, in K the two rockets remain separated by a fixed distance.");
    lines.add("In K, the length of a connector between the rockets decreases with increasing speed." + Util.NL);
    lines.add(tableHeader.row("Coordinate-time", "Rocket separation", "Connector length"));
    lines.add(dashes(54));
    for(int τ = 0; τ <= 12; ++τ) {
      double rocketSeparation = rocketB().eventFromProperTime(τ).x() - rocketA().eventFromProperTime(τ).x();
      double ct = stickEndB().ct(τ);
      double connectorLength = stickEndB().event(ct).x() - stickEndA().event(ct).x();
      lines.add(table.row(ct, rocketSeparation, connectorLength));
    }
    outputToConsoleAnd("connected-rockets.txt");
  }
  
  
  private double GEE = 1.03; //1g, when using units of light-years and years
  private double CONNECTOR_LENGTH = 1.0;
  
  // ct, rocket separation, connector-length
  private Table tableHeader = new Table("%-18s", "%-20s", "%-15s");
  private Table table = new Table("%12.2f", "%18.2f", "%20.8f");
  
  private MoveableHistory rocketA() {
    return UniformAcceleration.of(Position.origin(), X, GEE);
  }
  
  /** Same as rocket-a, but displaced to the right along the X-axis. */
  private MoveableHistory rocketB() {
    return UniformAcceleration.of(DeltaBase.of(Position.of(X, CONNECTOR_LENGTH)), X, GEE);
  }
  
  /** 'Stick' is really a place-holder for any extended object. */
  private MoveableHistory stickEndA() {
    return UniformAcceleration.of(Position.origin(), X, GEE);
  }

  /** Use the stick-end-a as the starting point; then just add the contracted length of the connector to the X-coordinate. */
  private History stickEndB() {
    MoveableHistory historyA = stickEndA();
    return new History() {
      public Event event(double ct) {
        Event event = historyA.event(ct);
        double β = historyA.velocity(ct).magnitude();
        double Γ = Physics.Γ(β);
        return event.put(X, event.x() + CONNECTOR_LENGTH / Γ);
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
