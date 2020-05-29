package sr.core;

/** 
 Convenience enum for the speed limit.
 
 This enum doesn't prevent you from using any units you wish.
 See {@link Config} for important info. 
*/
public enum SpeedLimit {
  
  /** 
   Natural units have c=1.
   You are encouraged to use natural units.
   This lets you work with smaller numbers, with absolute values typically in the range 1-100.
   It's usually easier to see the results more clearly when the numbers aren't really large.
   
   <P>Using c=1 can be interpreted in various ways:
   <ul>
    <li>imagining that the speed limit is changed to 1 meter per second
    <li>using seconds for time, and light-seconds for distance (or years/light-years)
   </ul>
   
   <P>As long as photons propagate at 45 degrees to the ct axis, everything should be fine.
  */
  C_NATURAL_UNITS(1.0),
  
  C_METERS_PER_SECOND(SpeedLimit.c),
  
  C_KILOMETERS_PER_SECOND(SpeedLimit.c/1000.0),
  
  C_MILES_PER_SEC(SpeedLimit.c/1609.344 /* RASC Observer's Handbook 2019 */);
  
  public double speed() { return speed; }
  
  /** Nature's speed limit for all signals (meters per second). Value: {@value} */
  public static final double c = 299792458.0D;
  
  // PRIVATE
  
  private SpeedLimit(double speed) {
    this.speed = speed;
  }
  private double speed;
}
