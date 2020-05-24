package sr.core;

import static java.util.Comparator.comparing;
import static sr.core.Util.sq;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** 
 The coords of an event in a given inertial frame.
 
 Uses right-handed Cartesian spatial coords (xyz) only.
 If the caller is using a different spatial coord system, then the caller will need to convert 
 before/after interacting with this class.
 
 <P>Immutable struct.
 
 <P>The natural order of these objects is by the time-component, ct.
 For time-like separations, the time-order of two events is invariant.
 For time-like histories, the time coord suffices to order events in the same way across all frames, 
 regardless of inertial frame; of course, the time intervals will NOT be the same, but the time order WILL be the same.
 
 <P>For space-like separations, the time-order of two events is of course NOT an invariant.
 
 <P>
 See {@link Config} and {@link SpeedLimit} regarding the units used for c.
 For most cases, using natural units is recommended.
*/
public final class Event implements FourVector<Event>, Comparable<Event> {
  
  /** The object methods that define the 4 parts of the 4-vector, in the conventional order. */  
  public VectorPart[] parts() {
    VectorPart[] result = {this::ct, this::x, this::y, this::z}; 
    return result;
  }
  
  /** Build this object out of 4 doubles. This instance method forwards to the static factory method. */
  public VectorBuilder<Event> buildit(){
    return Event::from; 
  }
  
  /** 
   Note that the time-coord is t, not ct!
   This is meant as a convenience for the caller. 
   This class takes the value for c from {@link Config#c()}. See {@link SpeedLimit} as well.
   
   <P>If you are working in less than 3 spatial dimensions, then pass 0 for the unused spatial coords (don't pass null).
   @throws RuntimeException if any param is null 
  */
  public static Event from(Double t, Double x, Double y, Double z) {
    return new Event(t, x, y, z);
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
  public Double intervalSquaredBetween(Event that) {
    return sq(ct-that.ct) - sq(x-that.x) - sq(y-that.y) - sq(z-that.z);
  }
  
  /** Maps to the sign of the squared-interval with the given event. */
  public Separation separationBetween(Event that) {
    return Separation.between(this, that);
  }
  
  /** The spatial length between this event and another event. Always positive! */
  public Double distanceBetween(Event that) {
    return Math.sqrt(sq(x-that.x) + sq(y-that.y) + sq(z-that.z));
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

  private Event(Double t, Double x, Double y, Double z) {
    this.ct = Config.c()*t;
    this.x = x;
    this.y = y;
    this.z = z;
    validate();
  }
  
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