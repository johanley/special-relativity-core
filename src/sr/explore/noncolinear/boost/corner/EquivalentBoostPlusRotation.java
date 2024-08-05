package sr.explore.noncolinear.boost.corner;

import sr.core.Axis;
import sr.core.SpeedValues;
import sr.core.Util;
import sr.core.VelocityTransformation;
import sr.core.event.Event;
import sr.core.event.transform.Boost;
import sr.core.event.transform.Rotation;
import sr.core.event.transform.Transform;
import sr.core.event.transform.TransformPipeline;
import sr.core.vector.Velocity;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/**
 Find the boost-plus-rotation that equates to 2 perpendicular boosts.
 
 <P>Here, two successive boosts are at right angles to each other, along 2 of the spatial axes.
 The more general case is to have the second boost at any angle with respect to the first.
 The X-Y plane can always be chosen to be the plane of the 2 boosts, with X as the direction of the first boost.
*/
public final class EquivalentBoostPlusRotation extends TextOutput {
  
  public static void main(String... args) {
    EquivalentBoostPlusRotation twoBoosts = new EquivalentBoostPlusRotation(Axis.Z, 0.97, 0.95); 
    twoBoosts.explore();
  }
  
  /**
   Constructor.
    
   The specific axes taken for each successive boost is taken from {@link Axis#rightHandRuleFor(Axis)}, for the given pole.
    
   @param pole the axis that is unaffected by the two boost operations
   @param β1 the speed for the first boost from K to K', along the first axis
   @param β2 the speed of the second boost from K' to K'', along the second axis, at a right 
   angle to the first
  */
  public EquivalentBoostPlusRotation(Axis pole, double β1, double β2) {
    Util.mustBeSpatial(pole);
    checkSpeeds(β1, β2);
    this.β1 = β1;
    this.β2 = β2;
    this.pole = pole;
  }
  
  public void explore() {
    example();
    spectrumOfSpeeds();
    outputToConsoleAnd("equivalent-boost-plus-rotation.txt");
  }

  /** The equivalent boost-plus-rotation. */
  public AngleBoostEquivalent equivalent() {
    AngleBoostEquivalent result = new AngleBoostEquivalent(singleBoostSpeed(), direction(), θw());
    return result;
  }
  
  /** Two boosts, the second perpendicular to the first (see class description). */
  public Transform asCornerBoost() {
    Transform result = TransformPipeline.join(
      Boost.alongThe(Axis.rightHandRuleFor(pole).get(0), β1),
      Boost.alongThe(Axis.rightHandRuleFor(pole).get(1), β2)
    );
    return result;
  }
  
  /** A single boost followed by a single rotation. */
  Transform asBoostPlusRotation() {
    Transform result = TransformPipeline.join(
      Rotation.of(pole, direction()), //bookkeeping rotation!: because my boost impl needs an axis to work with
      
      Boost.alongThe(Axis.rightHandRuleFor(pole).get(0), singleBoostSpeed()), 
      Rotation.of(pole, θw()),
      
      Rotation.of(pole, -direction()) //reverse the earlier bookeeping rotation!
    );
    return result;
  }
  
  /** A single rotation followed by a single boost. */
  Transform asRotationPlusBoost() {
    Transform result = TransformPipeline.join(
      Rotation.of(pole, direction()), //bookkeeping rotation!: because my boost impl needs an axis to work with
      
      Rotation.of(pole, θw()),
      Boost.alongThe(Axis.rightHandRuleFor(pole).get(0), singleBoostSpeed()),
      
      Rotation.of(pole, -direction()) //reverse the earlier bookeeping rotation!
    );
    return result;
  }
  
  //PRIVATE 
  
  private Axis pole;
  private double β1;
  private double β2;

  //βx   βy   β-equiv direction-degs  θw-degs, (v = vx + vy), (v = vy + vx)
  private Table tableHeader = new Table("%-21s", "%-21s", "%-21s", "%-14s", "%-8s", "%-22s", "%-18s");
  private Table table = new Table("%-21s", "%-21s", "%10.16f", "%10.3f°", "%10.3f°   ", "%-22s", "%-22s");

  private void example() {
    Event event_K = Event.of(10.0, 1.0, 1.0, 1.0); 
    
    Transform cornerBoost = asCornerBoost();
    Event event_Kpp_corner_boost = cornerBoost.changeFrame(event_K);
    
    Transform boostPlusRotation = asBoostPlusRotation();
    Event event_Kpp_boost_plus_rot = boostPlusRotation.changeFrame(event_K);
    Transform rotationPlusBoost = asRotationPlusBoost();
    Event event_Kpp_rot_plus_boost = rotationPlusBoost.changeFrame(event_K);
    
    lines.add("Find the boost-plus-rotation that equates to 2 perpendicular boosts."+Util.NL);
    lines.add("Event:" + event_K);
    lines.add("Corner-boost transform: " + cornerBoost);
    lines.add(" Event after corner-boost: " + event_Kpp_corner_boost);
    lines.add(Util.NL +"Boost+rotation transform: " + boostPlusRotation);
    lines.add(" Event after boost+rotation: " + event_Kpp_boost_plus_rot);
    lines.add("Note the presence here of a 'housekeeping' rotation at the start/end. That's an artifact of the boosts here always using an axis.");
    lines.add(Util.NL + "Order matters. Rotation+boost isn't the same. The operations don't commute.");
    lines.add("But there is apparently a DIFFERENT rotation+boost that is indeed the same (not implemented here).");
    lines.add("Rotation+boost transform: " + rotationPlusBoost);
    lines.add(" Event from rotation+boost: " + event_Kpp_rot_plus_boost);
  }
  
  private void spectrumOfSpeeds() {
    lines.add(Util.NL + tableHeader.row("β", "β", "Equivalent", "Equivalent", "θw", "(v = vx + vy)", "(v = vy + vx)"));
    lines.add(tableHeader.row("X-axis", "Y-axis", "β", "direction", "", "", ""));
    lines.add(dashes(125));
    for(SpeedValues βx : SpeedValues.nonExtremeValues()) {
      for (SpeedValues βy : SpeedValues.nonExtremeValues()) {
        EquivalentBoostPlusRotation cb = new EquivalentBoostPlusRotation(Axis.Z, βx.β(), βy.β());
        lines.add(
          table.row(cb.β1, cb.β2, cb.singleBoostSpeed(), degs(cb.direction()), degs(cb.θw()), cb.singleBoostVelocity(), cb.singleBoostVelocityReversed())
        );
      }
    }
  }
  
  /** 
   Silberstein rotation angle.
   Range -pi..pi.
  */
  private double θw() {
    Velocity a = singleBoostVelocity();
    Velocity b = singleBoostVelocityReversed();
    //should this be a.turnsTo(b) ? No, I believe this is correct. 
    //For circular motion, this angle is 'retrograde' with respect to the sense of the given circular motion.
    return b.turnsTo(a);
  }

  private double singleBoostSpeed() {
    return singleBoostVelocity().magnitude();
  }

  private Velocity singleBoostVelocity() {
    return VelocityTransformation.unprimedVelocity(
      velocityOne(), 
      velocityTwo() 
    );
  }
  
  private Velocity singleBoostVelocityReversed() {
    return VelocityTransformation.unprimedVelocity(
      velocityTwo(), 
      velocityOne() 
    );
  }
  
  private Velocity velocityOne() {
    return Velocity.of(Axis.rightHandRuleFor(pole).get(0), β1); 
  }
  
  private Velocity velocityTwo() {
    return Velocity.of(Axis.rightHandRuleFor(pole).get(1), β2); 
  }
  
  /**
   The direction of the single boost, with respect to the direction of the first boost.
   Range -pi..pi. 
  */
  private double direction() {
    return velocityOne().turnsTo(singleBoostVelocity());
  }
  
  private double degs(double value) {
    return Util.round(Util.radsToDegs(value), 4);
  }
  
  private void checkSpeeds(Double... speeds) {
    for(double β: speeds) {
      Util.mustHaveSpeedRange(β);
      Util.mustHave(Math.abs(β) > 0, "Speed must be non-zero.");
    }
  }
}
