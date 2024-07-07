package sr.core;

import static sr.core.Util.*;

/** 
 Config data.
 This data is widely used, doesn't change often, and would be annoying to pass around constantly.

 <P>Implementation note:
 This class uses private static {@link ThreadLocal} objects.
 This is defensive; it protects multi-threaded code from cross-talk between threads (just in case).
 Thread-local means the same as global-to-a-single-thread, instead of global-to-all-threads. 
*/
public final class Config {

  /** 
   To override the default epsilon (0.0000000000001), set a JRE System property having this name, on the command that starts the JRE.
   
   <P>Example: 
   <pre>java -Dsr-core-eps=0.0000000001</pre>
   
   Alternative: at runtime, you can call {@link #setε(double)}, perhaps upon startup.
   Value - {@value}.
  */
  public static final String EPS = "sr-core-eps";
  
  /** 
   A tiny value, used to detect values that are 'close enough' to 0 to be treated as such.
  */
  public static Double ε() {
    return epsilon.get();
  }
  
  /** Reset ε with a new value (small and positive). */
  public static void setε(double eps) {
    checkEps(eps);
    epsilon.set(eps);
  }
  
  // PRIVATE 

  /** Default epsilon. */
  private static final Double ε = 0.0000000000001;

  private static final ThreadLocal<Double> epsilon = new ThreadLocal<>() {
    protected Double initialValue() {
      Double result = ε;
      String sysProperty = System.getProperty(EPS);
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
