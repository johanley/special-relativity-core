package sr.core.component.ops;

import sr.core.component.Components;

/**
 Low-level transformation of components, without regard to context or units. 
*/
public interface ComponentOp {
  
  /**
   Transform the given source components into a new object, with new values.
  */
  public Components applyTo(Components source);

}
