package sr.core;

import sr.core.vector.ThreeVector;
import sr.core.vector.ThreeVectorImpl;
import sr.core.vector.Velocity;
import static sr.core.TransformInto.*;

/** 
 Relativistic transformation of velocities.
 This implementation uses the {@link LorentzTransformation} directly. 
*/ 
public final class VelocityTransformation {

  /**
   You need to be careful with the exact meaning of the parameters.
   The order matters! Non-commutative.
    
   @param boost_v velocity of K' in K (the boost velocity)
   @param object_v velocity of object in K
   @return velocity of object in K' (u')
  */
  public static Velocity primedVelocity(Velocity boost_v, Velocity object_v) {
    return transform(boost_v, object_v, PRIMED);
  }
  
  /**
   You need to be careful with the exact meaning of the parameters.
   The order matters! Non-commutative.
    
   @param boost_v velocity of K' in K (the boost velocity)
   @param u' velocity of object in K'
   @return velocity of object in K (u)
  */
  public static Velocity unprimedVelocity(Velocity boost_v, Velocity object_v_prime) {
    return transform(boost_v, object_v_prime, UNPRIMED);
  }
  
  /**
   Do the transform.
   Because of the behaviour of Γ, this isn't very precise for the lowest speeds. 
   But that's okay in the context of this project.
    
   @param v boost velocity
   @param u object velocity
   @param sign +1 for returning primed-u, -1 for returning unprimed-u.
  */
  private static Velocity transform(Velocity v, Velocity u, TransformInto direction) {
    LorentzTransformation lorentzTransform = LorentzTransformation.of(v);
    //change the incoming 3-vector to a velocity 4-vector (4 rows, 1 column)
    Matrix input_4 = asFourVelocity(u);
    //now the transform is easy:
    Matrix output_4 = lorentzTransform.transformVector(input_4, direction);
    //translate the result back into a 3-vector
    return asThreeVelocity(output_4);
  }

  /** Translate into a 4-vector (a 4x1 matrix). */
  private static Matrix asFourVelocity(Velocity u) {
    double[][] result = new double[4][1];
    double Γ = Physics.Γ(u.magnitude());
    result[0][0] = Γ;
    result[1][0] = Γ * u.x();
    result[2][0] = Γ * u.y();
    result[3][0] = Γ * u.z();
    return Matrix.of(result);
  }
  
  /** Translate into a 3-vector. */
  private static Velocity asThreeVelocity(Matrix u_p_4) {
    double Γ_p = u_p_4.get(0,0);
    ThreeVector result = ThreeVectorImpl.of(
      u_p_4.get(1,0), 
      u_p_4.get(2,0), 
      u_p_4.get(3,0)
    );
    result = result.divide(Γ_p);
    return Velocity.of(result);
  }
  
  /**
   Do the transform with 3-vectors. (For testing only.)
   @param v boost velocity
   @param u object velocity
   @param sign +1 for returning unprimed-u, -1 for returning primed-u.
  */
  private static Velocity transform(Velocity v, Velocity u, int sign) {
    //Reference: https://en.wikipedia.org/wiki/Velocity-addition_formula
    double a = 1.0 / (1 + sign*u.dot(v));
    double Γ_v = Physics.Γ(v.magnitude());
    double b = Γ_v / (1 + Γ_v);
    
    ThreeVector c = v.cross(v.cross(u)).times(b);
    ThreeVector d = sign == 1 ? u.plus(v).plus(c) : u.minus(v).plus(c); 
    ThreeVector e = d.times(a);
   
    return Velocity.of(e);
  }
  
  /** Informal tests. */
  private static void main(String[] args) {
    Velocity boost = Velocity.of(Axis.X, 0.1);
    Velocity object = Velocity.of(Axis.Y, 0.1);
    Velocity test1 = VelocityTransformation.primedVelocity(boost, object);
    Velocity test2 = transform(boost, object, -1);
    System.out.println(test1);
    System.out.println(test2);
    test1 = VelocityTransformation.unprimedVelocity(boost, object);
    test2 = transform(boost, object, +1);
    System.out.println("-------");
    System.out.println(test1);
    System.out.println(test2);
  }
  
}
