package sr.explore.cornerboost;

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
import sr.output.text.Table;
import sr.output.text.TextOutput;

/**
 Find the boost-plus-rotation that equates to 2 perpendicular boosts.
 
 <P>Here, two successive boosts are at right angles to each other, along 2 of the spatial axes.
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
public final class EquivalentBoostPlusRotation extends TextOutput {
  
  public static void main(String... args) {
    EquivalentBoostPlusRotation twoBoosts = new EquivalentBoostPlusRotation(Axis.Z, 0.97, 0.95); 
    twoBoosts.explore();
  }
  
  public void explore() {
    FourVector someEvent = FourVector.from(10.0, 1.0, 1.0, 1.0, ApplyDisplaceOp.YES); 
    
    CoordTransform cornerBoost = asCornerBoost();
    FourVector evOne = cornerBoost.toNewFrame(someEvent);
    
    CoordTransform boostPlusRotation = asBoostPlusRotation();
    FourVector evTwo = boostPlusRotation.toNewFrame(someEvent);
    CoordTransform rotationPlusBoost = asRotationPlusBoost();
    FourVector evThree = rotationPlusBoost.toNewFrame(someEvent);
    
    lines.add("Find the boost-plus-rotation that equates to 2 perpendicular boosts."+Util.NL);
    lines.add("Event:" + someEvent);
    lines.add("Corner-boost transform: " + cornerBoost);
    lines.add(" Event after corner-boost: " + evOne);
    lines.add(Util.NL +"Boost+rotation transform: " + boostPlusRotation);
    lines.add(" Event after boost+rotation: " + evTwo);
    lines.add("Note the presence here of a 'housekeeping' rotation at the start/end. That's an artifact of this code always using an axis.");
    lines.add(Util.NL + "Order matters. Rotation+boost isn't the same.");
    lines.add("But there is apparently a DIFFERENT rotation+boost that is indeed the same (not implemented here).");
    lines.add("Rotation+boost transform: " + rotationPlusBoost);
    lines.add(" Event from rotation+boost: " + evThree);
    
    lines.add(Util.NL + tableHeader.row("βx", "βy", "equivβ", "equivDirection", "θw"));
    lines.add(dashes(100));
    for(Speed βx : Speed.nonExtremeValues()) {
      for (Speed βy : Speed.nonExtremeValues()) {
        EquivalentBoostPlusRotation cb = new EquivalentBoostPlusRotation(Axis.Z, βx.β(), βy.β());
        lines.add(
          table.row(cb.β1, cb.β2, cb.βspeed(), degs(cb.βdirection()), degs(cb.θw()))
        );
      }
    }
    
    outputToConsoleAnd("equivalent-boost-plus-rotation.txt");
  }
  
  /**
    Constructor.
    
    The specific axes taken for each successive boost is taken from {@link Axis#rightHandRuleFor(Axis)}, for the given pole.
    
    @param pole the axis that is unaffected by the two boost operations
    @param β1 the speed for the first boost from K to K', along the first axis
    @param β2 the speed of the second boost from K' to K'', along the second axis, at a right 
    angle to the first
  */
  EquivalentBoostPlusRotation(Axis pole, double β1, double β2) {
    Util.mustBeSpatial(pole);
    this.β1 = β1;
    this.β2 = β2;
    this.Γ1 = Physics.Γ(β1);
    this.Γ2 = Physics.Γ(β2);
    this.pole = pole;
  }
  
  /** The equivalent boost-plus-rotation. */
  CornerBoostEquivalent equivalent() {
    CornerBoostEquivalent result = new CornerBoostEquivalent(βspeed(), βdirection(), θw());
    return result;
  }
  
  /** Two boosts, the second perpendicular to the first (see class description). */
  CoordTransform asCornerBoost() {
    CoordTransform result = CoordTransformPipeline.join(
      Boost.alongThe(Axis.rightHandRuleFor(pole).get(0), β1),
      Boost.alongThe(Axis.rightHandRuleFor(pole).get(1), β2)
    );
    return result;
  }
  
  /** A single boost followed by a single rotation. */
  CoordTransform asBoostPlusRotation() {
    CoordTransform result = CoordTransformPipeline.join(
      Rotate.about(pole, βdirection()), //bookkeeping rotation!: because my boost impl needs an axis to work with
      Boost.alongThe(Axis.rightHandRuleFor(pole).get(0), βspeed()), 
      Rotate.about(pole, θw()),
      Rotate.about(pole, -βdirection()) //reverse the earlier bookeeping rotation!
    );
    return result;
  }
  
  /** A single rotation followed by a single boost. */
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

  //βx   βy   β-equiv β-direction-degs  θw-degs");
  private Table tableHeader = new Table("%-21s", "%-21s", "%-21s", "%-12s", "%6s");
  private Table table = new Table("%-21s", "%-21s", "%-21s", "%10.3f°", "%10.3f°");
  
  
  /** 
   Silberstein rotation angle with respect to the βdirection.
   Has the same sign as -β1*β2. 
   Range -pi..pi. 
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
  
  private double degs(double value) {
    return Util.round(Util.radsToDegs(value), 4);
  }
}