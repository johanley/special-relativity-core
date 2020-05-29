package sr.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/** 
 Utility constants and methods. 
 It's recommended that the caller use a static import to access the members of this class. 
*/
public final class Util {
  
  /** 6.0 */
  public static final Double LIMITING_MAG_HUMAN_EYE = 6.0;
  
  /** UTF-8. */
  public final static Charset ENCODING = StandardCharsets.UTF_8;

  /** The square of the given number, x^2. */
  public static Double sq(double x) {
    return Math.pow(x, 2);
  }
  
  /** The square root of the given number, x^0.5. */
  public static Double sqroot(double x) {
    return Math.pow(x, 0.5);
  }
  
  public static double degsToRads(double degs) {
    return degs * CONVERT_RADS;
  }
  
  public static double radsToDegs(double rads) {
    return rads * 1.0/CONVERT_RADS;
  }
  
  public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();
    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_EVEN);
    return bd.doubleValue();
  }
  
  /** Compare to {@link Config#ε()}. */
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

  // PRIVATE 
  
  private static final double CONVERT_RADS = Math.PI/180.0;
  
}
