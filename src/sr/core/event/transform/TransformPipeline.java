package sr.core.event.transform;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import sr.core.Axis;
import sr.core.Parity;
import sr.core.Util;
import sr.core.event.Event;

/**
 General coordinate transformations built up out of simpler parts, as {@link Transform} objects.
*/
public final class TransformPipeline implements Transform {

  /** Simple test harness. */
  public static void main(String[] args) {
    Reflection reflect = Reflection.of(Parity.EVEN, Parity.ODD, Parity.EVEN, Parity.EVEN);
    Displacement displace = Displacement.of(0.0,1.0,0.0,0.0);
    Rotation rotate = Rotation.of(Axis.Z, Util.degsToRads(10));
    Boost boost = new Boost(Axis.X, 0.5);
    Transform[] ops = {rotate, displace, reflect, boost};
    TransformPipeline t = new TransformPipeline(ops);
    
    Event in = Event.of(1.0, 1.0, 0.0, 0.0);
    Event out = t.changeFrame(in);
    Event backIn = t.changeEvent(out);
    if (! in.equalsWithTinyDiff(backIn)) {
      throw new RuntimeException("Unequal after reversal.");
    }
  }
  
  /**
   Constructor.
   @param operations 0 or more transform operations, as applied in the 'forward' direction.
   The order is significant. A pipeline of operations needed to do the desired job. 
  */
  public TransformPipeline(Transform... operations) {
    this.operations = operations;
  }
  
  /** Static factory method. */
  public static TransformPipeline join(Transform... operations) {
    return new TransformPipeline(operations);
  }
  
  /** Apply the operations (in order) to the given FourVector. */
  @Override public Event changeFrame(Event vec) {
    Event result = vec;
    for (Transform op : operations) {
      result = op.changeFrame(result);
    }
    return result;
  }
  
  /** 
   Apply the operations to the given FourVector, but in <em>reverse</em> order <em>and</em> 
   with the <em>inverse</em> transform. 
  */
  @Override public Event changeEvent(Event vecPrime) {
    Event result = vecPrime;
    List<Transform> ops = Arrays.asList(operations);
    ListIterator<Transform> li = ops.listIterator(ops.size()); //start at the end
    while (li.hasPrevious()) { //go backwards
      result = li.previous().changeEvent(result);
    }
    return result;
  }
  
  @Override public String toString() {
    StringBuilder builder = new StringBuilder();
    for(Transform t : operations) {
      builder.append(t.toString() + " ");
    }
    return builder.toString().trim();
  }
  
  // PRIVATE 
  
  private Transform[] operations;

}
