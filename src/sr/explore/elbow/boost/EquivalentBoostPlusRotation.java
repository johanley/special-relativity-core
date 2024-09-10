package sr.explore.elbow.boost;

import sr.core.Axis;
import sr.core.SpeedValues;
import sr.core.Util;
import sr.core.VelocityTransformation;
import sr.core.vector3.Velocity;
import sr.core.vector4.Event;
import sr.core.vector4.transform.Boost;
import sr.core.vector4.transform.Rotation;
import sr.core.vector4.transform.Transform;
import sr.core.vector4.transform.TransformPipeline;
import sr.explore.Exploration;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/**
 Find the boost-plus-rotation that equates to two perpendicular boosts (a corner-boost).
 
 <P>Here, in the intermediate grid K', two successive boosts are at right angles to each other, along 2 of the spatial axes.
 (Please see the package documentation for more exact details.)
 
 <P>The more general case is to have the second boost at any angle with respect to the first.
 The X-Y plane can always be chosen to be the plane of the 2 boosts, with X as the direction of the first boost.
*/
public final class EquivalentBoostPlusRotation extends TextOutput implements Exploration {
  
  public static void main(String... args) {
    EquivalentBoostPlusRotation twoPerpendicularBoosts = new EquivalentBoostPlusRotation(); 
    twoPerpendicularBoosts.explore();
  }
  
  /**
   Constructor.
    
   The specific axes taken for each successive boost is taken from {@link Axis#rightHandRuleFor(Axis)}, for the given pole.
    
   @param pole the axis that is unaffected by the two boost operations
   @param β1 the speed for the first boost from K to K', along the first axis
   @param β2 the speed of the second boost from K' to K'', along the second axis, at a right angle to the first
  */
  public EquivalentBoostPlusRotation(Axis pole, double β1, double β2) {
    Util.mustBeSpatial(pole);
    checkSpeeds(β1, β2);
    this.β1 = β1;
    this.β2 = β2;
    this.pole = pole;
  }
  
  /** No-arg constructor with default data. */
  public EquivalentBoostPlusRotation() {
    this(Axis.Z, 0.97, 0.95);
  }
  
  @Override public void explore() {
    example();
    spectrumOfSpeeds();
    outputToConsoleAnd("equivalent-boost-plus-rotation.txt");
  }
  
  /** The equivalent boost-plus-rotation. */
  public ElbowBoostEquivalent equivalent() {
    ElbowBoostEquivalent result = new ElbowBoostEquivalent(singleBoostSpeed(), direction(), θw());
    return result;
  }  

  //PRIVATE 
  
  /**  The axis that is unaffected by the two boost operations.  */
  private Axis pole;
  
  /** The speed for the first boost from K to K', along the first axis. */
  private double β1;
  
  /** The speed of the second boost from K' to K'', along the second axis, at a right angle to the first. */
  private double β2;

  //βx   βy   β-equiv direction-degs  θw-degs, (v = vx + vy), (v = vy + vx)
  private Table tableHeader = new Table("%-21s", "%-21s", "%-21s", "%-14s", "%-8s", "%-22s", "%-18s");
  private Table table = new Table("%-21s", "%-21s", "%10.16f", "%10.3f°", "%10.3f°   ", "%-22s", "%-22s");

  private void example() {
    Event event_K = Event.of(10.0, 1.0, 1.0, 1.0); 
    
    Transform cornerBoost = asCornerBoost();
    Event event_Kpp_corner_boost = cornerBoost.changeGrid(event_K);
    
    Event event_Kpp_boost_plus_rot = boostPlusRotation().changeGrid(event_K);
    Event event_Kpp_rot_plus_boost = rotationPlusBoost().changeGrid(event_K);
    
    add("Find the boost-plus-rotation that equates to 2 perpendicular boosts."+Util.NL);
    add("Event:" + event_K);
    add("Corner-boost transform: " + cornerBoost);
    add(" Event after corner-boost: " + event_Kpp_corner_boost);
    add(Util.NL +"Boost+rotation transform: " + boostPlusRotation());
    add(" Event after boost+rotation: " + event_Kpp_boost_plus_rot);
    add(Util.NL + "Order matters. Rotation+boost isn't the same. The operations don't commute.");
    add("But there is apparently a DIFFERENT rotation+boost that is indeed the same (not implemented here).");
    add("Rotation+boost transform: " + rotationPlusBoost());
    add(" Event from rotation+boost: " + event_Kpp_rot_plus_boost);
  }
  
  private void spectrumOfSpeeds() {
    add(Util.NL + tableHeader.row("β", "β", "Equivalent", "Equivalent", "θw", "(v = vx + vy)", "(v = vy + vx)"));
    add(tableHeader.row("X-axis", "Y-axis", "β", "direction", "", "", ""));
    add(dashes(125));
    for(SpeedValues βx : SpeedValues.nonExtremeValues()) {
      for (SpeedValues βy : SpeedValues.nonExtremeValues()) {
        EquivalentBoostPlusRotation cb = new EquivalentBoostPlusRotation(Axis.Z, βx.β(), βy.β());
        add(
          table.row(cb.β1, cb.β2, cb.singleBoostSpeed(), degs(cb.direction()), degs(cb.θw()), cb.singleBoostVelocity(), cb.singleBoostVelocityReversed())
        );
      }
    }
  }
  
  /** Two boosts, the second perpendicular to the first (see class description). */
  private Transform asCornerBoost() {
    Transform result = TransformPipeline.join(
      Boost.of(Axis.rightHandRuleFor(pole).get(0), β1),
      Boost.of(Axis.rightHandRuleFor(pole).get(1), β2)
    );
    return result;
  }
  
  /** A single boost followed by a single rotation. */
  private Transform boostPlusRotation() {
    Transform result = TransformPipeline.join(
      Boost.of(singleBoostVelocity()),  
      Rotation.of(pole, θw())
    );
    return result;
  }
  
  /** A single rotation followed by a single boost. */
  private Transform rotationPlusBoost() {
    Transform result = TransformPipeline.join(
      Rotation.of(pole, θw()),
      Boost.of(singleBoostVelocity())  
    );
    return result;
  }
  
  /** Silberstein rotation angle. Range -pi..pi.  */
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
  
  /** Reverse the order of parameters to the transformation formula. */
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
   The direction of the single-boost, with respect to the direction of the first boost. Range -pi..pi.
   All velocities must be in the XY plane.  
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
