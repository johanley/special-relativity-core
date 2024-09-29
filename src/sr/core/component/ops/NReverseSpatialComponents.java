package sr.core.component.ops;

import sr.core.Axis;
import sr.core.component.NComponents;

/** Change the sign of all the XYZ components. */
public final class NReverseSpatialComponents implements NComponentOp {

  /** Change the sign of all the XYZ components. */
  @Override public NComponents applyTo(NComponents source) {
    NComponents result = source;
    for(Axis spatialAxis : Axis.spatialAxes()) {
      double currentValue = source.on(spatialAxis);
      result = source.overwrite(spatialAxis, -1 * currentValue);
    }
    return result;
  }
}