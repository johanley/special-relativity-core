package sr.core;

import static sr.core.Util.*;

/**
 A tiny value used to treat nearly-zero values as being zero.
 The default value is 0.0000000000001.
 
 <P>Implementation note:
 This class uses private static {@link ThreadLocal} objects.
 This is defensive; it protects multi-threaded code from cross-talk between threads (just in case).
 Thread-local means the same as global-to-a-single-thread, instead of global-to-all-threads. 
*/
public final class Epsilon {

  /** 
   Allows override of the default epsilon value, by using a System property.
   
   To override the default epsilon (0.0000000000001), set a JRE System property having this name, on the command that starts the JRE.
   The largest allowable value is 0.001.
   
   <P>Example: 
   <pre>java -Dsr-core-epsilon=0.0000000001</pre>
   
   Value - {@value}.
  */
  public static final String EPSILON = "sr-core-epsilon";
  
  /**  A tiny value, used to detect values that are 'close enough' to 0 to be treated as such.  */
  public static Double ε() {
    return epsilon.get();
  }
  
  // PRIVATE 

  /** Default epsilon: {@value}. */
  private static final Double ε = 0.0000000000001;
  
  private static final ThreadLocal<Double> epsilon = new ThreadLocal<>() {
    protected Double initialValue() {
      Double result = ε;
      String sysProperty = System.getProperty(EPSILON);
      if (Util.hasContent(sysProperty)) {
        result = Double.valueOf(sysProperty);
        checkEps(result);
      }
      return result;
    }; 
  };
  private static void checkEps(double eps) {
    mustHave(eps > 0.0, "Epsilon isn't positive");
    mustHave(eps < 0.001, "Epsilon isn't small enough");
  }
}
