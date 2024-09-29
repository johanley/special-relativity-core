package sr.core.component.ops;

import sr.core.component.NComponents;

/**
 Low-level transformation of components, without regard to context or units. 
*/
public interface NComponentOp {
  
  /**
   Transform the given source components into a new object, with new values.
  */
  public NComponents applyTo(NComponents source);

}
