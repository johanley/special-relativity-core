package sr.core.component.ops;

import static sr.core.Axis.CT;

import sr.core.component.Components;

/** Change the sign of the CT component. */
public final class ReverseTimeComponent implements ComponentOp {
  
  /**  Change the sign of the CT component, if present. */
  @Override public Components applyTo(Components source) {
    Components result = source;
    if (source.axes().contains(CT)){
      double currentValue = source.ct();
      result = source.overwrite(CT, -1 * currentValue);
    }
    return result;
  }
}
