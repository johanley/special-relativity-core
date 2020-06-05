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

  /** Write to the console. */
  public static void log(Object msg) {
    System.out.println(msg.toString());
  }

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
  
  /** Return true only if the absolute value of the difference is less than {@link Config#ε()}. */
  public static boolean equalsWithEpsilon(double a, double b) {
    return Math.abs(a - b) < Config.ε();
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

  // PRIVATE 
  
  private static final double CONVERT_RADS = Math.PI/180.0;
  
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
