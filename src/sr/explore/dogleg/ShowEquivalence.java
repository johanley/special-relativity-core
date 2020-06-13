package sr.explore.dogleg;

import java.util.ArrayList;
import java.util.List;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Speed;
import sr.core.Util;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.Boost;
import sr.core.transform.CoordTransform;
import sr.core.transform.CoordTransformPipeline;
import sr.core.transform.FourVector;
import sr.core.transform.Rotate;

/**
 Find the boost-plus-rotation that equates to 2 perpendicular boosts.
 
 <P>Two successive boosts at right angles to each other, along 2 of the spatial axes.
 The more general case is to have the second boost at any angle with respect to the first.
 The X-Y plane can always be chosen to be the plane of the 2 boosts, with X as the direction of the first boost.
 
 <P>Formulas: 
  <ul>
   <li><a href='http://www.nucleares.unam.mx/~alberto/apuntes/ferraro.pdf'>R. Ferraro and M. Thibeault</a> 
   equation 8, and remarks on page 146 between equations 11 and 12 
   <li><a href='https://jila.colorado.edu/arey/sites/default/files/files/seven(1).pdf'>Smoot</a> 
   equations 6, 10 for two directions, whose difference is the θw rotation angle  
  </ul> 
*/
public final class ShowEquivalence {
  
  public static void main(String... args) {
    FourVector someEvent = FourVector.from(10.0, 1.0, 1.0, 1.0, ApplyDisplaceOp.YES); 
    ShowEquivalence twoBoosts = new ShowEquivalence(Axis.Z, 0.97, 0.95); 
    
    List<String> lines = new ArrayList<>();
    
    CoordTransform doglegBoost = twoBoosts.asDoglegBoost();
    FourVector one = doglegBoost.toNewFrame(someEvent);
    
    CoordTransform boostPlusRotation = twoBoosts.asBoostPlusRotation();
    FourVector two = boostPlusRotation.toNewFrame(someEvent);
    CoordTransform rotationPlusBoost = twoBoosts.asRotationPlusBoost();
    FourVector three = rotationPlusBoost.toNewFrame(someEvent);
    
    lines.add("Event:" + someEvent);
    lines.add("Dogleg transform: " + doglegBoost);
    lines.add("Dogleg:           " + one);
    lines.add("");
    lines.add("Boost+rotation transform: " + boostPlusRotation);
    lines.add("Boost + rotation ev: " + two);
    lines.add("");
    lines.add("Order matters. Rotation+boost isn't the same.");
    lines.add("Rotation+boost transform: " + rotationPlusBoost);
    lines.add("Rotation + boost ev: " + three);
    
    lines.add("");
    lines.add("βx   βy   β-equiv            β-direction-degs  θw-degs");
    lines.add("---------------------------------------------------------");
    for(Speed βx : Speed.nonExtremeValues()) {
      for (Speed βy : Speed.nonExtremeValues()) {
        ShowEquivalence cb = new ShowEquivalence(Axis.Z, βx.β(), βy.β());
        lines.add(cb.β1 + " " + cb.β2 + " " + cb.βspeed() + " " + Util.radsToDegs(cb.βdirection()) + " " + Util.radsToDegs(cb.θw()));
      }
    }

    Util.writeToFile(ShowEquivalence.class, "show-equivalence.txt", lines);
    for(String line : lines) {
      System.out.println(line);
    }
  }
  
  /**
    Constructor.
    
    The axis-order is taken from {@link Axis#rightHandRuleFor(Axis)} for the given pole.
    
    @param pole the axis that is unaffected by these operations
    @param β1 the speed for the first boost from K to K', along the first axis
    @param β2 the speed of the second boost from K' to K'', along the second axis, at a right 
    angle to the first
  */
  ShowEquivalence(Axis pole, double β1, double β2) {
    Util.mustBeSpatial(pole);
    this.β1 = β1;
    this.β2 = β2;
    this.Γ1 = Physics.Γ(β1);
    this.Γ2 = Physics.Γ(β2);
    this.pole = pole;
  }
  
  /** The equivalent boost-plus-rotation. */
  DoglegBoostEquivalent equivalent() {
    DoglegBoostEquivalent result = new DoglegBoostEquivalent(βspeed(), βdirection(), θw());
    return result;
  }
  
  /** Two boosts, the second perpendicular to the first. */
  CoordTransform asDoglegBoost() {
    CoordTransform result = CoordTransformPipeline.join(
      Boost.alongThe(Axis.rightHandRuleFor(pole).get(0), β1),
      Boost.alongThe(Axis.rightHandRuleFor(pole).get(1), β2)
    );
    return result;
  }
  
  CoordTransform asBoostPlusRotation() {
    CoordTransform result = CoordTransformPipeline.join(
      Rotate.about(pole, βdirection()), //bookkeeping rotation!: because my boost impl needs an axis to work with
      Boost.alongThe(Axis.rightHandRuleFor(pole).get(0), βspeed()), 
      Rotate.about(pole, θw()),
      Rotate.about(pole, -βdirection()) //reverse the earlier bookeeping rotation!
    );
    return result;
  }
  
  CoordTransform asRotationPlusBoost() {
    CoordTransform result = CoordTransformPipeline.join(
      Rotate.about(pole, βdirection()), //bookkeeping rotation!: because my boost impl needs an axis to work with
      Rotate.about(pole, θw()),
      Boost.alongThe(Axis.rightHandRuleFor(pole).get(0), βspeed()), 
      Rotate.about(pole, -βdirection()) //reverse the earlier bookeeping rotation!
    );
    return result;
  }

  
  //PRIVATE 
  
  private Axis pole;
  
  private double β1;
  private double Γ1;
  
  private double β2;
  private double Γ2;
  
  /** 
   Thomas-Wigner rotation angle with respect to the βdirection.
   Has the same sign as -β1*β2. 
   Range -pi..pi 
  */
  private double θw() {
    double y = Γ1 * Γ2 * β1 * β2;
    double x = Γ1 + Γ2;
    return Math.atan2(-y, x); //-pi..+pi
  }

  /** The speed of the single boost. */
  private double βspeed() {
    double Γ = Γ1 * Γ2;
    return Physics.β(Γ);
  }
  
  /**
   The direction of the single boost, with respect to the direction of the first boost.
   Has the same sign as β2.  
   Range -pi..+pi 
  */
  private double βdirection() {
    double y = β2; //-1..+1
    //if Γ1 wasn't here, then you would have a Galilean result for the direction
    double x = Γ1 * β1; //-..+ (bigger than 1); the x-boost has a bigger effect; asymmetric
    return Math.atan2(y, x); //-pi..+pi
  }
  
  /** Smoot's formulas gives the same result for θw. */
  private double thetaPrimePrime() {
    double y = Γ2 * β2;
    double x =  β1; 
    return Math.atan2(y, x); //-pi..+pi
  }
  private double smootθw() {
    double theta = βdirection();
    return theta - thetaPrimePrime();
  }
}
