package sr.core;

import sr.core.component.NComponents;

/**
 Basic matrix operations. 
 Based on https://introcs.cs.princeton.edu/java/95linear/Matrix.java.html
*/
public final class NMatrix {

  /** 
   Factory method based on an array of arrays.
   The first level has rows, and the second level has columns.
   @param data has double[1][2] for row 1, column 2, for example.
  */ 
  public static NMatrix of(double[][] data) {
    return new NMatrix(data);  
  }

  /** Return an nxn identity matrix. */
  public static NMatrix identity(int n) {
    NMatrix result = new NMatrix(n, n);
    for (int i = 0; i < n; i++) {
      result.data[i][i] = 1;
    }
    return result;
  }  
  
  /** 
   Matrix multiplication, return this * that.
  */
  public NMatrix times(NMatrix that) {
    if (this.n != that.m) throw new RuntimeException("Can't multiply. Bad matrix dimensions.");
    NMatrix result = new NMatrix(this.m, that.n);
    for (int i = 0; i < result.m; i++) {
      for (int j = 0; j < result.n; j++) {
        for (int k = 0; k < this.n; k++) {
          result.data[i][j] += (this.data[i][k] * that.data[k][j]);
        }
      }
    }
    return result;
  }
  
  /** Matrix addition this + that. */
  public NMatrix plus(NMatrix that) {
    if (that.m != this.m || that.n != this.n) throw new RuntimeException("Can't add. Bad matrix dimensions.");
    NMatrix result = new NMatrix(m, n);
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
          result.data[i][j] = this.data[i][j] + that.data[i][j];
        }
    }
    return result;
  }
  
  /** Return new matrix, each component of which is multiplied by the given scalar. */
  public NMatrix scalarMultiple(double val) {
    NMatrix result = new NMatrix(m, n);
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        result.data[i][j] = val * this.data[i][j];
      }
    }
    return result;
  }

  /** Return the transpose of this matrix. */
  public NMatrix transpose() {
    NMatrix result = new NMatrix(n, m);
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        result.data[j][i] = this.data[i][j];
      }
    }
    return result;
  }
  
  /** Return an element of the matrix. */
  public double get(int row, int col) {
    return data[row][col];
  }
  
  /** Convert components into a 4x1 {@link NMatrix}. */
  public static NMatrix asMatrix(NComponents v) {
    double[][] result = new double[4][1];
    for(Axis axis : Axis.values()) {
      result[axis.idx()][0] = v.on(axis);
    }
    return NMatrix.of(result);
  }
  
  /** Number of rows. */ 
  private final int m;
  
  /** Number of columns */
  private final int n;
  
  /** mxn array of data. */
  private final double[][] data;
  
  private NMatrix(double[][] data) {
    m = data.length;
    n = data[0].length;
    this.data = new double[m][n];
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        this.data[i][j] = data[i][j];
      }
    }
  }
  
  /** Create a matrix of 0's. */
  private NMatrix(int m, int n) {
    this.m = m;
    this.n = n;
    data = new double[m][n];
  }
}
