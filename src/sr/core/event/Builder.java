package sr.core.event;

import java.util.Map;

import sr.core.Axis;

/** 
 This interface was created in order to experience less pain when creating new T ({@link FourVector}) objects being returned from a generic method.
 
 <P>This interface helps preserve immutability of objects. Instead of changing the state of a single object, you produce 
 a new object with the desired state (using a given T object).
 
 <P>This interface is a bit strange: it uses an object method to create a new object of the same class, instead of a constructor or a 
 factory method.
*/
public interface Builder<T> {
  
  /** Build a new object of this class out of the given components. */
  public T build(Map<Axis, Double> components);

}
