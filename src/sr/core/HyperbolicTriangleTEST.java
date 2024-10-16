package sr.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Unit test. Note that small hyperbolic triangles are quasi-Euclidean. */
class HyperbolicTriangleTEST {
  
  @Test void sideAngleSide() {
    //https://www.whitman.edu/Documents/Academics/Mathematics/2014/brewert.pdf
    //Example 5.2
    HyperbolicTriangle t = HyperbolicTriangle.fromSideAngleSide(1.0, DEGS_90, 2.0);
    assertEquals(t.A, 1.0);
    assertEquals(t.C, 2.0);
    assertEquals(t.b, DEGS_90);
    assertEquals(t.B, 2.45, SMALL_DIFF);
    
    //Example 5.3
    t = HyperbolicTriangle.fromSideAngleSide(4.0, DEGS_90, 8.0);
    assertEquals(t.A, 4.0);
    assertEquals(t.C, 8.0);
    assertEquals(t.b, DEGS_90);
    assertEquals(t.B, 11.31, SMALL_DIFF);
    
    t = HyperbolicTriangle.fromSideAngleSide(SMALL_SIDE, DEGS_60, SMALL_SIDE);
    smallEquilateralTriangle(t, TINY_DIFF);
    
    t = HyperbolicTriangle.fromSideAngleSide(SMALL_SIDE, DEGS_90, SMALL_SIDE);
    smallRightTriangle(t, TINY_DIFF);
    
    t = HyperbolicTriangle.fromSideAngleSide(SMALL_SIDE, DEGS_120, SMALL_SIDE);
    smallObtuse(t, TINY_DIFF);
  }
  
  @Test void sideSideSide() {
    HyperbolicTriangle t = HyperbolicTriangle.fromSideSideSide(SMALL_SIDE, SMALL_SIDE, SMALL_SIDE);
    smallEquilateralTriangle(t, TINY_DIFF);
    
    t = HyperbolicTriangle.fromSideSideSide(SMALL_SIDE, ROOT_2* SMALL_SIDE, SMALL_SIDE);
    smallRightTriangle(t, TINY_DIFF);
    
    t = HyperbolicTriangle.fromSideSideSide(SMALL_SIDE, ROOT_3* SMALL_SIDE, SMALL_SIDE);
    smallObtuse(t, TINY_DIFF);
  }

  /** It seems to be  hard to make a small triangle like this: the scale is absolute in hyperbolic geometry! */
  @Test void angleAngleAngle() {
    HyperbolicTriangle t = HyperbolicTriangle.fromAngleAngleAngle(DEGS_60, DEGS_60, DEGS_60);
    smallEquilateralTriangle(t, SMALL_DIFF); //not TINY_DIFF!
    
    t = HyperbolicTriangle.fromAngleAngleAngle(DEGS_90, DEGS_45, DEGS_45);
    //smallRightTriangle(t, SMALL_DIFF); //FAILS!
    
    t = HyperbolicTriangle.fromAngleAngleAngle(DEGS_120, DEGS_30, DEGS_30);
    //smallObtuse(t, SMALL_DIFF); //FAILS
    
    //Just compare to triangles built using other means
    compareAAA_to_SSS(1, 1, 1);
    compareAAA_to_SSS(0.1, 0.1, 0.1);
    compareAAA_to_SSS(0.2, 0.2, 0.2);
    
    compareAAA_to_SAS(1, 1, 1);
    compareAAA_to_SAS(1, 2, 1);
    compareAAA_to_SAS(1, 3, 1);
    compareAAA_to_SAS(0.1, 1, 0.1);
  }

  /** Small equilateral, 60 degrees. */
  private void smallEquilateralTriangle(HyperbolicTriangle t, double tolerance) {
    assertEquals(t.A, SMALL_SIDE, tolerance);
    assertEquals(t.B, SMALL_SIDE, tolerance);
    assertEquals(t.C, SMALL_SIDE, tolerance);
    assertEquals(t.a, DEGS_60, tolerance);
    assertEquals(t.b, DEGS_60, tolerance);
    assertEquals(t.c, DEGS_60, tolerance);
  }

  /** Small right triangle, 45 degrees. */
  private void smallRightTriangle(HyperbolicTriangle t, double tolerance) {
    assertEquals(t.A, SMALL_SIDE, tolerance);
    assertEquals(t.B, ROOT_2*SMALL_SIDE, tolerance);
    assertEquals(t.C, SMALL_SIDE, tolerance);
    assertEquals(t.a, DEGS_45, tolerance);
    assertEquals(t.b, DEGS_90, tolerance);
    assertEquals(t.c, DEGS_45, tolerance);
  }

  /** Small obtuse triangle, 120 degrees. */
  private void smallObtuse(HyperbolicTriangle t, double tolerance) {
    assertEquals(t.A, SMALL_SIDE, tolerance);
    assertEquals(t.B, ROOT_3*SMALL_SIDE, tolerance);
    assertEquals(t.C, SMALL_SIDE, tolerance);
    assertEquals(t.a, DEGS_30, tolerance);
    assertEquals(t.b, DEGS_120, tolerance);
    assertEquals(t.c, DEGS_30, tolerance);
  }
  
  private void compareAAA_to_SSS(double A, double B, double C) {
    HyperbolicTriangle precomputed = HyperbolicTriangle.fromSideSideSide(A, B, C);
    HyperbolicTriangle t = HyperbolicTriangle.fromAngleAngleAngle(precomputed.a, precomputed.b, precomputed.c);
    areEqualTriangles(t, precomputed, TINY_DIFF);
  }
  
  private void compareAAA_to_SAS(double A, double b, double C) {
    HyperbolicTriangle precomputed = HyperbolicTriangle.fromSideAngleSide(A, b, C);
    HyperbolicTriangle t = HyperbolicTriangle.fromAngleAngleAngle(precomputed.a, precomputed.b, precomputed.c);
    areEqualTriangles(t, precomputed, TINY_DIFF);
  }
  
  private void areEqualTriangles(HyperbolicTriangle one, HyperbolicTriangle two, double tolerance) {
    assertEquals(one.A, two.A, tolerance);
    assertEquals(one.B, two.B, tolerance);
    assertEquals(one.C, two.C, tolerance);
    assertEquals(one.a, two.a, tolerance);
    assertEquals(one.b, two.b, tolerance);
    assertEquals(one.c, two.c, tolerance);
  }
  
  private static final double DEGS_30 = Util.degsToRads(30);
  private static final double DEGS_45 = Util.degsToRads(45);
  private static final double DEGS_60 = Util.degsToRads(60);
  private static final double DEGS_90 = Math.PI/2;
  private static final double DEGS_120 = Util.degsToRads(120);
  
  private static final double SMALL_SIDE = 0.001;
  private static final double ROOT_2 = Math.pow(2, 0.5);
  private static final double ROOT_3 = Math.pow(3, 0.5);
  
  private static final double SMALL_DIFF = 0.01;
  private static final double TINY_DIFF = 0.000001;
}
