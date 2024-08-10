package sr.core;

import static sr.core.Util.sq;

import sr.core.vector.ThreeVector;
import sr.core.vector.Velocity;
import static sr.core.TransformInto.*;

/**
 Lorentz Transformation of 4-vectors and tensors, all represented as a matrix.
 
 <P>The caller often needs to format/parse data into a (1x4) or (4x1) matrix.
 
 <P>The matrix implementation of the Lorentz Transformation is the most general way of transforming items.
 The same basic mechanism can replace many formulas that are specific to certain physical quantities.
 The 3-vector transformations for different physical quantities make it seem like they are unrelated, but in fact
 all of these transformations are connected by the same underlying mechanism, implemented here:
 <ul>
  <li>core event and 4-vector transformation
  <li>velocity transformation
  <li>electromagnetic field transformation
 </ul> 
 
 <P>The caller usually needs to transform 3-vectors into corresponding 4-vectors (or 4-tensors) before using this class.
*/
public final class LorentzTransformation {

  /** 
   Factory method for a Lorentz Transformation in any direction, of the given velocity.
   No rotation of the spatial axes occurs. 
  */
  public static LorentzTransformation of(Velocity boostVelocity) {
    return new LorentzTransformation(boostVelocity);
  }
  
  /** Return a 4-vector in the primed system. */
  public Matrix primedVector(Matrix unprimed) {
    return transformVector(unprimed, PRIMED);
  }
  
  /** Return a 4-vector in the unprimed system. */
  public Matrix unPrimedVector(Matrix primed) {
    return transformVector(primed, UNPRIMED);
  }

  /**
   Apply the transform using the given sign.
   @param input the four-vector
   @param sign +1 for returning primed quantities, -1 for returning unprimed quantities.
  */
  public Matrix transformVector(Matrix input, TransformInto direction) {
    return Λ(direction.sign()).times(input);
  }
  
  private Velocity boostVelocity;
  
  private LorentzTransformation(Velocity boostVelocity) {
    this.boostVelocity = boostVelocity;
  }
  
  /**
   The matrix style is bulkier but completely general.
   It can be applied both to 4-vectors and to tensors such as the electromagnetic field. 
   @param sign +1 for returning primed quantities, -1 for returning unprimed quantities. Flips the sign of the boost velocity.
  */
  private Matrix Λ(int sign){
    
    //https://en.wikipedia.org/wiki/Lorentz_transformation#Proper_transformations
    
    //the inverse simply reverses the direction of the boost
    ThreeVector v = boostVelocity.times(sign);
    
    double Γ = Physics.Γ(v.magnitude());
    double vsq = v.square();
    
    //first index as the row, and the second index an the column, 0..3
    double[][] components = new double[4][4];
    components[0][0] = Γ;
    
    components[0][1] = -Γ * v.x();
    components[0][2] = -Γ * v.y();
    components[0][3] = -Γ * v.z();
    
    components[1][0] = -Γ * v.x();
    components[2][0] = -Γ * v.y();
    components[3][0] = -Γ * v.z();
    
    components[1][1] = 1 + (Γ-1) * (sq(v.x()) / vsq);
    components[2][2] = 1 + (Γ-1) * (sq(v.y()) / vsq);
    components[3][3] = 1 + (Γ-1) * (sq(v.z()) / vsq);
    
    components[2][1] =  (Γ-1) * (v.y() * v.x() / vsq);
    components[3][1] =  (Γ-1) * (v.z() * v.x() / vsq);
    
    components[1][2] =  (Γ-1) * (v.x() * v.y() / vsq);
    components[3][2] =  (Γ-1) * (v.z() * v.y() / vsq);

    components[1][3] =  (Γ-1) * (v.x() * v.z() / vsq);
    components[2][3] =  (Γ-1) * (v.y() * v.z() / vsq);
    
    return Matrix.of(components);
  }
  
  //rank 2
  //the tensor transform equation gets re-ordered when written as a matrix expression:
  //chrome-extension://efaidnbmnnnibpcajpcglclefindmkaj/http://www.thphys.nuim.ie/Notes/MP465/Lectures_23-24.pdf
  //chrome-extension://efaidnbmnnnibpcajpcglclefindmkaj/https://physicspages.com/pdf/Relativity/Electromagnetic%20field%20tensor%20-%20Lorentz%20transformations.pdf
  //https://en.wikipedia.org/wiki/Classical_electromagnetism_and_special_relativity#Field_tensor_and_4-current
  //https://hepweb.ucsd.edu/ph110b/110b_notes/node69.html
  //https://www.feynmanlectures.caltech.edu/II_26.html
}
