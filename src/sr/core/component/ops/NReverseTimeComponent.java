package sr.core.component.ops;

import static sr.core.Axis.CT;

import sr.core.component.NComponents;

/** Change the sign of the CT component. */
public final class NReverseTimeComponent implements NComponentOp {
  
  /**  Change the sign of the CT component, if present. */
  @Override public NComponents applyTo(NComponents source) {
    NComponents result = source;
    if (source.axes().contains(CT)){
      double currentValue = source.ct();
      result = source.overwrite(CT, -1 * currentValue);
    }
    return result;
  }
}
