package sr.core.component.ops;

import sr.core.Axis;
import sr.core.component.Components;

/** Change the sign of all the XYZ components. */
public final class ReverseSpatialComponents implements ComponentOp {

  /** Change the sign of all the XYZ components. */
  @Override public Components applyTo(Components source) {
    Components result = source;
    for(Axis spatialAxis : Axis.spatialAxes()) {
      double currentValue = source.on(spatialAxis);
      result = result.overwrite(spatialAxis, -1 * currentValue);
    }
    return result;
  }
}