package sr.core.component.ops;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.sq;

import sr.core.Matrix;
import sr.core.Util;
import sr.core.component.Components;
import sr.core.vec3.ThreeVector;
import sr.core.vec3.Velocity;

/**
 A boost (Lorentz Transformation) in any direction, with no spatial rotation.
  
 <P>Implementation note: this implementation uses a matrix to represent the Lorentz Transformation.
 This seems to be the most general way of transforming items.
 It ties together: 
 <ul>
  <li>the core event and 4-vector transformation
  <li>the velocity transformation
  <li>the transformation of the electromagnetic field
 </ul>
*/
public final class Boost implements ComponentOp {

  /** 
   Factory method for a Lorentz Transformation in any direction, of the given velocity.
   No rotation of the spatial axes occurs. 
  */
  public static Boost of(Velocity velocity, Sense sense) {
    return new Boost(velocity, sense);
  }
  
  @Override public Components applyTo(Components source) {
    Util.mustHave(source.hasSpaceAndTime(), "Doesn't have both space and time components: " + source);
    Matrix input_matrix = Matrix.asMatrix(source);
    Matrix output_matrix = boostMatrix(sense.sign()).times(input_matrix);
    return asComponents(output_matrix);
  }
  
  private Velocity velocity;
  private Sense sense;
  
  private Boost(Velocity velocity, Sense sense) {
    this.velocity = velocity;
    this.sense = sense;
  }
  
  /**
   The matrix style is bulky but completely general.
   It can be applied both to 4-vectors and to tensors such as the electromagnetic field. 
   @param sign +1 for returning primed quantities, -1 for returning unprimed quantities. Flips the sign of the boost velocity.
  */
  private Matrix boostMatrix(int sign){
    //avoid division by zero errors
    if (velocity.magnitude() == 0.0) {
      return Matrix.identity(4);
    }
    
    //https://en.wikipedia.org/wiki/Lorentz_transformation#Proper_transformations
    //BUT I CHANGE THE SENSE (REVERSE THE SIGN) of their formula, to change from passive to active
    
    //the inverse simply reverses the direction of the boost
    ThreeVector v = velocity.times(sign);
    
    double Γ = velocity.Γ();
    double vsq = v.square();
    
    //first index as the row, and the second index an the column, 0..3
    double[][] components = new double[4][4];
    components[0][0] = Γ;
    
    components[0][1] = Γ * v.x(); 
    components[0][2] = Γ * v.y();
    components[0][3] = Γ * v.z();
    
    components[1][0] = Γ * v.x();
    components[2][0] = Γ * v.y();
    components[3][0] = Γ * v.z();
    
    components[1][1] = 1 + (Γ-1) * (sq(v.x()) / vsq);
    components[2][2] = 1 + (Γ-1) * (sq(v.y()) / vsq);
    components[3][3] = 1 + (Γ-1) * (sq(v.z()) / vsq);
    
    components[2][1] = (Γ-1) * (v.y() * v.x() / vsq);
    components[3][1] = (Γ-1) * (v.z() * v.x() / vsq);
    
    components[1][2] = (Γ-1) * (v.x() * v.y() / vsq);
    components[3][2] = (Γ-1) * (v.z() * v.y() / vsq);

    components[1][3] = (Γ-1) * (v.x() * v.z() / vsq);
    components[2][3] = (Γ-1) * (v.y() * v.z() / vsq);
    
    return Matrix.of(components);
  }
  
  private Components asComponents(Matrix matrix){
    return Components.of(
      matrix.get(CT.idx(),0) , 
      matrix.get(X.idx(),0) , 
      matrix.get(Y.idx(),0) , 
      matrix.get(Z.idx(),0) 
    );
  }
  
  //rank 2
  //the tensor transform equation gets re-ordered when written as a matrix expression:
  //http://www.thphys.nuim.ie/Notes/MP465/Lectures_23-24.pdf
  //https://physicspages.com/pdf/Relativity/Electromagnetic%20field%20tensor%20-%20Lorentz%20transformations.pdf
  //https://en.wikipedia.org/wiki/Classical_electromagnetism_and_special_relativity#Field_tensor_and_4-current
  //https://hepweb.ucsd.edu/ph110b/110b_notes/node69.html
  //https://www.feynmanlectures.caltech.edu/II_26.html
}
