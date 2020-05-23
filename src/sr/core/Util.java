package sr.core;

/** 
 Utility constants and methods. 
 It's recommended that the caller use a static import to access the members of this class. 
*/
public final class Util {

  /** The square of the given number, x^2. */
  public static Double squared(double x) {
    return Math.pow(x, 2);
  }
  
  public static boolean isTiny(double val) {
    return Math.abs(val) < Config.ε();
  }
  
  public static boolean hasContent(String text) {
    return text != null && text.trim().length() > 0; 
  }

  /** Check data: if the test fails, a RuntimeException is thrown. */
  public static void mustHave(boolean test, String msg) {
    if (!test) {
      throw new RuntimeException(msg);
    }
  }
}
