package sr.core;

import static sr.core.TransformInto.PRIMED;
import static sr.core.TransformInto.UNPRIMED;
import static sr.core.Util.sq;

import java.util.LinkedHashMap;
import java.util.Map;

import sr.core.vector3.ThreeVector;
import sr.core.vector3.Velocity;
import sr.core.vector4.Builder;
import sr.core.vector4.FourVector;

/**
 Lorentz Transformation of 4-vectors and tensors, all represented as a matrix.
 
 <P>The matrix implementation of the Lorentz Transformation is the most general way of transforming items.
 The same basic mechanism can replace many formulas that are specific to certain physical quantities.
 The 3-vector transformations for different physical quantities make it seem like they are unrelated, but in fact
 all of these transformations are connected by the same underlying mechanism, implemented here:
 <ul>
  <li>core event and 4-vector transformation
  <li>velocity transformation
  <li>electromagnetic field transformation
 </ul>
*/
public final class LorentzTransformation3 {

  /** 
   Factory method for a Lorentz Transformation in any direction, of the given velocity.
   No rotation of the spatial axes occurs. 
  */
  public static LorentzTransformation3 of(Velocity boostVelocity) {
    return new LorentzTransformation3(boostVelocity);
  }
  
  /** Return a 4-vector in the primed system. */
  public <T extends FourVector  & Builder<T>> T primedVector(T unprimed) {
    return transformVector(unprimed, PRIMED);
  }
  
  /** Return a 4-vector in the unprimed system. */
  public <T extends FourVector & Builder<T>> T unPrimedVector(T primed) {
    return transformVector(primed, UNPRIMED);
  }
  
  /**
   Apply the transform using the given sign.
   @param input the four-vector
   @param sign +1 for returning primed quantities, -1 for returning unprimed quantities.
  */
  public <T extends FourVector & Builder<T>> T transformVector(T input, TransformInto direction) {
    //the core calculation uses matrices, so we convert back and forth like so
    Matrix input_matrix = Matrix.asMatrix(input);
    Matrix output_matrix = Λ(direction.sign()).times(input_matrix);
    return input.build(fromComponents(output_matrix));
  }

  @Override public String toString() {
    return boostVelocity.toString();
  }

  private Velocity boostVelocity;
  
  private LorentzTransformation3(Velocity boostVelocity) {
    this.boostVelocity = boostVelocity;
  }
  
  /**
   The matrix style is bulkier but completely general.
   It can be applied both to 4-vectors and to tensors such as the electromagnetic field. 
   @param sign +1 for returning primed quantities, -1 for returning unprimed quantities. Flips the sign of the boost velocity.
  */
  private Matrix Λ(int sign){
    //avoid division by zero errors
    if (boostVelocity.magnitude() == 0.0) {
      return Matrix.identity(4);
    }
    
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
  
  private Map<Axis, Double> fromComponents(Matrix matrix){
    Map<Axis, Double> result = new LinkedHashMap<>();
    for(Axis axis : Axis.values()) {
      result.put(axis, matrix.get(axis.idx(),0));
    }
    return result;
  }
  
  //rank 2
  //the tensor transform equation gets re-ordered when written as a matrix expression:
  //chrome-extension://efaidnbmnnnibpcajpcglclefindmkaj/http://www.thphys.nuim.ie/Notes/MP465/Lectures_23-24.pdf
  //chrome-extension://efaidnbmnnnibpcajpcglclefindmkaj/https://physicspages.com/pdf/Relativity/Electromagnetic%20field%20tensor%20-%20Lorentz%20transformations.pdf
  //https://en.wikipedia.org/wiki/Classical_electromagnetism_and_special_relativity#Field_tensor_and_4-current
  //https://hepweb.ucsd.edu/ph110b/110b_notes/node69.html
  //https://www.feynmanlectures.caltech.edu/II_26.html
}
