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
    
    //small equilateral, 60 degrees
    t = HyperbolicTriangle.fromSideAngleSide(SMALL_SIDE, DEGS_60, SMALL_SIDE);
    assertEquals(t.A, SMALL_SIDE, TINY_DIFF);
    assertEquals(t.B, SMALL_SIDE, TINY_DIFF);
    assertEquals(t.C, SMALL_SIDE, TINY_DIFF);
    assertEquals(t.a, DEGS_60, TINY_DIFF);
    assertEquals(t.b, DEGS_60, TINY_DIFF);
    assertEquals(t.c, DEGS_60, TINY_DIFF);
    
    //small right triangle, 45 degrees
    t = HyperbolicTriangle.fromSideAngleSide(SMALL_SIDE, DEGS_90, SMALL_SIDE);
    assertEquals(t.A, SMALL_SIDE, TINY_DIFF);
    assertEquals(t.B, ROOT_2 * SMALL_SIDE, TINY_DIFF);
    assertEquals(t.C, SMALL_SIDE, TINY_DIFF);
    assertEquals(t.a, DEGS_45, TINY_DIFF);
    assertEquals(t.b, DEGS_90, TINY_DIFF);
    assertEquals(t.c, DEGS_45, TINY_DIFF);
    
    //small obtuse triangle, 120 degrees
    t = HyperbolicTriangle.fromSideAngleSide(SMALL_SIDE, DEGS_120, SMALL_SIDE);
    assertEquals(t.A, SMALL_SIDE, TINY_DIFF);
    assertEquals(t.B, ROOT_3 * SMALL_SIDE, TINY_DIFF);
    assertEquals(t.C, SMALL_SIDE, TINY_DIFF);
    assertEquals(t.a, DEGS_30, TINY_DIFF);
    assertEquals(t.b, DEGS_120, TINY_DIFF);
    assertEquals(t.c, DEGS_30, TINY_DIFF);
  }
  
  @Test void sideSideSide() {
    //small equilateral, 60 degrees
    HyperbolicTriangle t = HyperbolicTriangle.fromSideSideSide(SMALL_SIDE, SMALL_SIDE, SMALL_SIDE);
    assertEquals(t.A, SMALL_SIDE);
    assertEquals(t.B, SMALL_SIDE);
    assertEquals(t.C, SMALL_SIDE);
    assertEquals(t.a, DEGS_60, TINY_DIFF);
    assertEquals(t.b, DEGS_60, TINY_DIFF);
    assertEquals(t.c, DEGS_60, TINY_DIFF);
    
    //small right triangle, 45 degrees
    t = HyperbolicTriangle.fromSideSideSide(SMALL_SIDE, ROOT_2* SMALL_SIDE, SMALL_SIDE);
    assertEquals(t.A, SMALL_SIDE);
    assertEquals(t.B, ROOT_2*SMALL_SIDE);
    assertEquals(t.C, SMALL_SIDE);
    assertEquals(t.a, DEGS_45, TINY_DIFF);
    assertEquals(t.b, DEGS_90, TINY_DIFF);
    assertEquals(t.c, DEGS_45, TINY_DIFF);
    
    //small obtuse triangle, 120 degrees
    t = HyperbolicTriangle.fromSideSideSide(SMALL_SIDE, ROOT_3* SMALL_SIDE, SMALL_SIDE);
    assertEquals(t.A, SMALL_SIDE);
    assertEquals(t.B, ROOT_3*SMALL_SIDE);
    assertEquals(t.C, SMALL_SIDE);
    assertEquals(t.a, DEGS_30, TINY_DIFF);
    assertEquals(t.b, DEGS_120, TINY_DIFF);
    assertEquals(t.c, DEGS_30, TINY_DIFF);
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
