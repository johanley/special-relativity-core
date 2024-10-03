package sr.explore.accel.elbow.boost;

import sr.core.Axis;
import sr.core.VelocityTransformation;
import sr.core.SpeedValues;
import sr.core.Util;
import sr.core.component.Event;
import static sr.core.component.ops.Sense.ChangeGrid;
import sr.core.vec3.NAxisAngle;
import sr.core.vec3.NVelocity;
import sr.explore.Exploration;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/**
 Find the boost-plus-rotation that equates to two perpendicular boosts (a corner-boost).
 
 <P>Here, in the intermediate frame K', two successive boosts are at right angles to each other, along 2 of the spatial axes.
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
    
    Event event_Kpp_corner_boost = doCornerBoostOn(event_K);
    Event event_Kpp_boost_plus_rot = doBoostPlusRotationOn(event_K);
    Event event_Kpp_rot_plus_boost = doRotationPlusBoostOn(event_K);
    
    add("Find the boost-plus-rotation that equates to 2 perpendicular boosts."+Util.NL);
    add("Event:" + event_K);
    add("Corner-boost transform: boost " + velocityOne() + " boost " + velocityTwo());
    add(" Event after corner-boost: " + event_Kpp_corner_boost);
    add(Util.NL +"Boost+rotation transform: boost " + singleBoostVelocity() + " rotation" + rotation());
    add(" Event after boost+rotation: " + event_Kpp_boost_plus_rot);
    add(Util.NL + "Order matters. Rotation+boost isn't the same. The operations don't commute.");
    add("But there is apparently a DIFFERENT rotation+boost that is indeed the same (not implemented here).");
    add("Rotation+boost transform: rotation" + rotation() + " boost " + singleBoostVelocity());
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
  private Event doCornerBoostOn(Event event) {
    Event result = event.boost(velocityOne(), ChangeGrid);
    return result.boost(velocityTwo(), ChangeGrid);
  }
  
  /** A single boost followed by a single rotation. */
  private Event doBoostPlusRotationOn(Event event) {
    Event result = event.boost(singleBoostVelocity(), ChangeGrid);
    return result.rotate(rotation(), ChangeGrid);
  }
  
  /** A single rotation followed by a single boost. */
  private Event doRotationPlusBoostOn(Event event) {
    Event result = event.rotate(rotation(), ChangeGrid);
    return result.boost(singleBoostVelocity(), ChangeGrid);
  }
  
  /** Kinematic (Wigner) rotation angle. Range -pi..pi.  */
  private double θw() {
    NVelocity a = singleBoostVelocity();
    NVelocity b = singleBoostVelocityReversed();
    //should this be a.turnsTo(b) ? No, I believe this is correct. 
    //For circular motion, this angle is 'retrograde' with respect to the sense of the given circular motion.
    return b.turnsTo(a);
  }

  private double singleBoostSpeed() {
    return singleBoostVelocity().magnitude();
  }

  private NVelocity singleBoostVelocity() {
    return VelocityTransformation.unprimedVelocity(
      velocityOne(), 
      velocityTwo() 
    );
  }
  
  /** Reverse the order of parameters to the transformation formula. */
  private NVelocity singleBoostVelocityReversed() {
    return VelocityTransformation.unprimedVelocity(
      velocityTwo(), 
      velocityOne() 
    );
  }
  
  private NVelocity velocityOne() {
    return NVelocity.of(β1,Axis.rightHandRuleFor(pole).get(0)); 
  }
  
  private NVelocity velocityTwo() {
    return NVelocity.of(β2, Axis.rightHandRuleFor(pole).get(1)); 
  }
  
  private NAxisAngle rotation() {
    return NAxisAngle.of(θw(), pole);
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
