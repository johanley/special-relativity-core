package sr.core;

import static java.util.Comparator.comparing;
import static sr.core.Util.squared;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** 
 The coords of an event in a given inertial frame.
 
 Uses Cartesian spatial coords (xyz).
 
 <P>Immutable struct.
 
 <P>The natural order is by ct.
 For time-like separations, the time-order of two events is invariant.
 For time-like histories, the time coord suffices to order events in the same way across all frames, 
 regardless of inertial frame; of course, the time intervals will NOT be the same, but the time order WILL be the same.
 
 <P>For space-like separations, the time-order of two events is of course NOT an invariant.
 
 <P>Example of a style which lets you change your mind about the units being used:
 <pre>
  double c = Units.C_NATURAL_UNITS.speed();
  Event ev = Event.from(c*t, x, y, z);
 </pre>
 For most cases, using natural units is recommended.
*/
public final class Event implements Comparable<Event> {

  /** 
   Note that the time-coord is t, not ct!
   This is meant as a convenience for the caller. 
   The value for c is taken internally from {@link Config#c()}.
   
   <P>See {@link SpeedLimit}.
   
   <P>If you are working in less than 3 spatial dimensions, then pass 0 for the unused spatial coords (don't pass null!).
   @throws RuntimeException if any param is null 
  */
  public static Event from(Double t, Double x, Double y, Double z) {
    return new Event(t, x, y, z);
  }
   
  /** See {@link #Event(Double, Double, Double, Double)}. */
  public Event(Double t, Double x, Double y, Double z) {
    this.ct = Config.c()*t;
    this.x = x;
    this.y = y;
    this.z = z;
    validate();
  }
  
  /** 
   The event at the origin of a given inertial frame.
   Because of displacement operations, the coords of this event is, as always, relative to a specific inertial frame. 
  */
  public static final Event ORIGIN = Event.from(0.0, 0.0, 0.0, 0.0);
  
  /** 
   The squared-interval between this event and that event. Can be positive or negative! 
   Uses (+,-,-,-,) as the signature; all time-like intervals are real, not imaginary.  
  */
  public Double intervalSqBetween(Event that) {
    return squared(ct-that.ct) - squared(x-that.x) - squared(y-that.y) - squared(z-that.z);
  }
  
  /** Maps to the sign of the squared-interval with the given event. */
  public Separation separationBetween(Event that) {
    return Separation.between(this, that);
  }
  
  /** The spatial length between this event and another event. Always positive! */
  public Double distanceBetween(Event that) {
    return Math.sqrt(squared(x-that.x) + squared(y-that.y) + squared(z-that.z));
  }
  
  /** The ct-interval between this event and another event. Always positive! */
  public Double timeBetween(Event that) {
    return Math.abs(ct-that.ct);
  }

  /** The time-order of this event with respect to the given event b. */
  public TimeOrder relativeTo (Event b) {
    return TimeOrder.of(this, b);
  }
  
  public Double ct() { return ct; }
  public Double t() { return ct/Config.c(); }
  public Double x() { return x; }
  public Double y() { return y; }
  public Double z() { return z; }
  
  @Override public boolean equals(Object aThat) {
    //unusual: multiple return statements
    if (this == aThat) return true;
    if (!(aThat instanceof Event)) return false;
    Event that = (Event)aThat;
    for(int i = 0; i < this.getSigFields().length; ++i){
      if (!Objects.equals(this.getSigFields()[i], that.getSigFields()[i])){
        return false; 
      }
    }
    return true;
  }    
  
  @Override public int hashCode() {
    return Objects.hash(getSigFields());
  }
  
  /** Debugging only. */
  @Override public String toString() {
    String sep = ", ";
    return "[" + ct+sep+ x+sep+ y+sep+ z + "]";
  }

  @Override public int compareTo(Event that) {
    return COMPARATOR.compare(this, that);
  }
  
  // PRIVATE

  private Double ct;
  private Double x;
  private Double y;
  private Double z;
  
  private Object[] getSigFields() {
    //optimize: start with items that are most likely to differ
    return new Object[] {
      ct, x, y, z
    };
  }

  /** Static: avoid creating this object every time a comparison is made.*/
  private static Comparator<Event> COMPARATOR = getComparator();  
  
  /**
   Note the 'thenComparing' chain: when comparing, the implementation goes 
   to the next level of comparison ONLY IF the previous level has 
   returned '0' (equal).
  */
  private static Comparator<Event> getComparator(){
    //use the same fields used by the equals method, if at all possible!
    Comparator<Event> result = 
      comparing(Event::ct)
      .thenComparing(Event::x)
      .thenComparing(Event::y)
      .thenComparing(Event::z)
    ;
    return result;
  }
  
  private void validate() throws RuntimeException {
    List<String> errors = new ArrayList<>();
    checkIfNull(x, "x", errors);    
    checkIfNull(y, "y", errors);    
    checkIfNull(z, "z", errors);
    if (!errors.isEmpty()) {
      RuntimeException ex = new RuntimeException("Event not valid.");
      for(String error: errors) {
        ex.addSuppressed(new RuntimeException(error));
      }
      throw ex;
    }    
  }
  
  private void checkIfNull(Object field, String errorMsg, List<String> errors) {
    if (field == null) {
      errors.add(errorMsg + " is null");
    }
  }  
}