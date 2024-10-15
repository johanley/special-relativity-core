package sr.core;

import static java.lang.Math.cos;
import static java.lang.Math.cosh;
import static java.lang.Math.sinh;

import sr.core.vec4.FourVelocity;

/**
 A triangle in hyperbolic geometry, as represented as a triangle on a unit hyperboloid.
 
 <P>Hyperbolic geometries have a constant negative curvature.
 (Here that curvature is -1, because we take <em>c = 1</em>.)
 
 <P>The geometry on the unit hyperboloid is conformal: angles have the usual meaning as in Euclidean geometry.
 
 <P>Hyperbolic triangles can be "solved" in much the same way as in Euclidean geometry: side-angle-side, and so on.
 This is done using analogues of the cosine rule and sine rule. 
 
 <P>Here, the three sides of the triangle are named <em>A</em>, <em>B</em>, and <em>C</em>, 
 and the three angles are named <em>a</em>, <em>b</em>, and <em>c</em>.
 Angle <em>a</em> is opposite the side <em>A</em>, and so on.
 
 <P><b>In special relativity, the unit hyperboloid arises naturally because four-velocity is a unit vector (when <em>c = 1</em>).</b> 
 This means that the four-velocity is "confined" to the future-branch of the unit hyperboloid in four-velocity space.
 This space-like surface is sometimes called <em>H<sup>+</sup></em>.
 
 <P><b>This class models triangles on <em>H<sup>+</sup></em>, 
 and it allows one to reduce some calculations with velocities to simple problems in hyperbolic geometry.</b>
 
 <P>Here, the number attached to the side of a triangle equates to the total space-time interval 
 integrated along a space-like arc on <em>H<sup>+</sup></em>.
 With our convention for <em>(ct,x,y,z)</em> as <em>(+---)</em>, it's necessary to take the <em>negative</em> of the quadratic form, 
 <em>- (Δs)<sup>2</sup></em>, in order to get a non-negative value.  
 With this change, <em>H<sup>+</sup></em> has a truly Riemannian geometry, where all "distances" are indeed greater than or equal to 0.
 
  <P>The arc-interval is simple to calculate, given two four-velocities <em>u1</em> and <em>u1</em> representing the endpoints of the arc: 
 <pre>arccosh(u1.dot(u2))</pre>

  <P><b>It's interesting to note that even though space-time itself is flat, a curved Riemannian geometry still has a non-trivial role 
  to play in the description of velocity.</b>
*/
public final class HyperbolicTriangle {
  
  /** 
   Return the full data for a triangle, starting with its vertices on the unit hyperboloid, as defined by 
   a triplet of four-velocities.
   IMPORTANT: the triplet of four-velocities must be with respect to the same frame of reference!
  */
  public static HyperbolicTriangle fromFourVelocities(FourVelocity a, FourVelocity b, FourVelocity c) {
    double A = arcIntervalBetween(b, c);
    double B = arcIntervalBetween(a, c);
    double C = arcIntervalBetween(a, b);
    return fromSideSideSide(A, B, C);
  }
  
  /**  
   Return the full data for a hyperbolic triangle, starting with side-side-side information, 
   and computing its angles. 
  */
  public static HyperbolicTriangle fromSideSideSide(double A, double B, double C) {
    HyperbolicTriangle t = new HyperbolicTriangle();
    t.A = A;
    t.B = B;
    t.C = C;
    t.b = cosh_law_b(t.A, t.B, t.C);
    t.a = cosh_law_b(t.B, t.A, t.C);
    t.c = cosh_law_b(t.A, t.C, t.B);
    return t;
  }
  
  /** 
   Return the full data for a hyperbolic triangle, starting with side-angle-side information, 
   and computing the remainder. 
 */
  public static HyperbolicTriangle fromSideAngleSide(double A, double b, double C) {
    HyperbolicTriangle t = new HyperbolicTriangle();
    t.A = A;
    t.b = b;
    t.C = C;
    t.B = cosh_law_B(t.A, t.b, t.C);
    t.a = cosh_law_b(t.B, t.A, t.C);
    t.c = cosh_law_b(t.A, t.C, t.B);
    return t;
  }

  /**
   Returns π - (sum of the interior angles of the triangle), always positive.
  
   <p>In hyperbolic geometry, the sum of the angles in a triangle is always less than π.
   Remarkably, the angular defect is the same as the area of the triangle!
   
   <P><b>In our case, the angular-defect is also the same as the magnitude of the 
   kinematic (Wigner) rotation.</b>
   For low speeds, the value is near zero. 
   The maximum possible value is π.
  */
  public double angularDefect() {
    return Math.PI - (a + b + c);
  }
  
  /** Angle opposite side A, range 0..π. */
  public double a;
  /** Angle opposite side B, range 0..π. */
  public double b;
  /** Angle opposite side C, range 0..π. */
  public double c;

  /** Side opposite angle a. Arc-interval along the unit hyperboloid. */
  public double A;
  /** Side opposite angle b. Arc-interval along the unit hyperboloid. */
  public double B;
  /** Side opposite angle c. Arc-interval along the unit hyperboloid. */
  public double C;

  /** This implementation applies rounding. */
  @Override public String toString() {
    String result = "ABC[";
    result = result + append(A);
    result = result + append(B);
    result = result + rounded(C) + "] abc[";
    result = result + append(a);
    result = result + append(b);
    result = result + rounded(c) + "]";
    result = result + " angular-defect:" + rounded(angularDefect());
    return result;
  }
  
  /** Return the interval for the side B. */
  private static double cosh_law_B(double A, double b, double C) {
    //https://en.wikipedia.org/wiki/Hyperbolic_law_of_cosines
    double cosh_B = cosh(A)*cosh(C) - sinh(A)*sinh(C)*cos(b);
    return Util.arc_cosh(cosh_B);
  }
  
  /** Return the angle b, in the range 0..π. */
  private static double cosh_law_b(double A, double B, double C) {
    double numer = cosh(A)*cosh(C) - cosh(B);
    double denom = sinh(A)*sinh(C);
    double cos_b = numer / denom;
    return Math.acos(cos_b); //0..pi
  }
  
  /*
  DANGEROUS because you need to think about quadrants. Unnecessary?
  private static double sinh_law(double X, double b, double B) {
    //https://en.wikipedia.org/wiki/Law_of_sines#Hyperbolic_case
    double sin_x =  sinh(X) * (sin(b) / sinh(B));
    double result = asin(sin_x); //-pi/2..+pi/2
    if (result < 0) {
      result = Math.PI + result;
    }
    return result;
  }*/

  /** 
   The returned value is is unaffected by changing the order of the params.
   
   <P>Along a space-like surface, the squared-interval is negative using the conventions of in this project.
   Here, that negative sign is simply discarded, in order to return a value that's real and positive.
   
   @param endpoint_a an end-point of an arc along the unit hyperboloid 
   @param endpoint_b an end-point of an arc along the unit hyperboloid
   @return zero or more 
  */
  private static double arcIntervalBetween(FourVelocity endpoint_a, FourVelocity endpoint_b) {
    //https://en.wikipedia.org/wiki/Hyperboloid_model#Minkowski_quadratic_form
    //very compact!
    return Util.arc_cosh(endpoint_a.dot(endpoint_b));
  }
 
  private static final String SEP = ",";
  
  private String append(double val) {
    return rounded(val) + SEP;
  }
  
  private double rounded(double x) {
    return Util.round(x, 5);
  }
}