package sr.core;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/** 
 Utility constants and methods. 
 It's recommended that the caller use a static import to access the members of this class. 
*/
public final class Util {
  
  /** UTF-8 encoding for reading and writing text files. */
  public final static Charset ENCODING = StandardCharsets.UTF_8;
  
  /** Newline character. */
  public static final String NL = System.getProperty("line.separator");

  /** Write to the console. */
  public static void log(Object msg) {
    System.out.println(msg.toString());
  }

  /** The square of the given number, x^2. */
  public static Double sq(double x) {
    return Math.pow(x, 2);
  }
  
  /** The square root of the given number. */
  public static Double sqroot(double x) {
    return isTiny(x) ? 0.0 /*avoid NaN*/: Math.sqrt(x); 
  }
  
  public static int sign(double value){
    return value >=0 ? 1 : -1;
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
  
  /** Compare to {@link Epsilon#ε()}. */
  public static boolean isTiny(double val) {
    return Math.abs(val) < Epsilon.ε();
  }
  
  /** Return true only if the absolute value of the difference is less than {@link Epsilon#ε()}. */
  public static boolean equalsWithEpsilon(double a, double b) {
    return Math.abs(a - b) < Epsilon.ε();
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
  
  /** Check data: the given axis must be spatial, not temporal. */
  public static void mustBeSpatial(Axis axis) {
    mustHave(axis.isSpatial(), "Cannot use the time axis for this operation.");
  }
  
  /** Check data: the speed must be in the range (-1,1). */
  public static void mustHaveSpeedRange(double β) {
    mustHave(β > -1 && β < 1, "Speed β=" + β + " is not in the range (-1,1).");
  }

  /**
   Write a file line by line.
   By default, the dir built from the user.dir System property, 'src', plus a package name (from the class param).
   You can override the user.dir directory location by setting a system property named 'sr-output-dir'. 
  */
  public static void writeToFile(Class<?> aClass, String fileName, List<String> lines) {
    String dir = outputDirectory(aClass);
    Path path = Paths.get(dir, fileName);
    try {
      Files.write(path, lines, ENCODING);
    } 
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** For logging, a separation line for titles and so on. */
  public static String separator(int length) {
    StringBuilder result = new StringBuilder();
    for(int idx = 0; idx < length; ++idx) {
      result.append(SEP);
    }
    return result.toString();
  }
  
  /** @param x must be greater than 1. */
  public static double arc_cosh(double x) {
    //https://en.wikipedia.org/wiki/Inverse_hyperbolic_functions
    if (x < 1) {
      throw new IllegalArgumentException("x must be >= 1: " + x);
    }
    return Math.log( x + sqroot(x*x - 1) );
  }
  
  public static double arc_tanh(double x) {
    //https://en.wikipedia.org/wiki/Inverse_hyperbolic_functions
    if (x < -1 || x > 1) {
      throw new IllegalArgumentException("x must be in range (-1,1): " + x);
    }
    double numer = 1 + x;
    double denom = 1 - x;
    return 0.5 * Math.log(numer / denom);
  }


  // PRIVATE 
  
  private static final double CONVERT_RADS = Math.PI/180.0;
  private static final String SEP = "-";

  
  /** The default is the directory of the given class. Override the base dir with a System property 'sr-output-dir'. */
  private static String outputDirectory(Class<?> aClass) {
    String sep = System.getProperty("file.separator");
    String baseDir = System.getProperty("user.dir") + sep + "src";
    String overrideBaseDir = System.getProperty("sr-output-dir");
    if (hasContent(overrideBaseDir)) {
      baseDir = overrideBaseDir;
    }
    String packageName = aClass.getPackage().getName().replace(".", sep);
    return baseDir + sep + packageName;
  }
}
