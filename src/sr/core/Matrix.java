package sr.core;

import sr.core.event.FourVector;

/**
 Basic matrix operations. 
 Based on https://introcs.cs.princeton.edu/java/95linear/Matrix.java.html
*/
public final class Matrix {

  /** 
   Factory method based on an array of arrays.
   The first level has rows, and the second level has columns.
   @param data has double[1][2] for row 1, column 2, for example.
  */ 
  public static Matrix of(double[][] data) {
    return new Matrix(data);  
  }

  /** Return an nxn identity matrix. */
  public static Matrix identity(int n) {
    Matrix result = new Matrix(n, n);
    for (int i = 0; i < n; i++) {
      result.data[i][i] = 1;
    }
    return result;
  }  
  
  /** 
   Matrix multiplication, return this * that.
  */
  public Matrix times(Matrix that) {
    if (this.n != that.m) throw new RuntimeException("Can't multiply. Bad matrix dimensions.");
    Matrix result = new Matrix(this.m, that.n);
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
  public Matrix plus(Matrix that) {
    if (that.m != this.m || that.n != this.n) throw new RuntimeException("Can't add. Bad matrix dimensions.");
    Matrix result = new Matrix(m, n);
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
          result.data[i][j] = this.data[i][j] + that.data[i][j];
        }
    }
    return result;
  }
  
  /** Return new matrix, each component of which is multiplied by the given scalar. */
  public Matrix scalarMultiple(double val) {
    Matrix result = new Matrix(m, n);
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        result.data[i][j] = val * this.data[i][j];
      }
    }
    return result;
  }

  /** Return the transpose of this matrix. */
  public Matrix transpose() {
    Matrix result = new Matrix(n, m);
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
  
  /** Convert a 4-vector into a {@link Matrix}. */
  public static Matrix asMatrix(FourVector v) {
    double[][] result = new double[4][1];
    for(Axis axis : Axis.values()) {
      result[axis.idx()][0] = v.components().get(axis);
    }
    return Matrix.of(result);
  }
  
  /** Number of rows. */ 
  private final int m;
  
  /** Number of columns */
  private final int n;
  
  /** mxn array of data. */
  private final double[][] data;
  
  private Matrix(double[][] data) {
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
  private Matrix(int m, int n) {
    this.m = m;
    this.n = n;
    data = new double[m][n];
  }
}
