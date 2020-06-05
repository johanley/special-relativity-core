package sr.core.transform;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import sr.core.Axis;
import sr.core.Parity;
import sr.core.Util;

/**
 General coordinate transformations built up out of simpler parts, as {@link CoordTransform} objects.
*/
public final class CoordTransformPipeline implements CoordTransform {

  /** Simple test harness. */
  public static void main(String[] args) {
    Reflect reflect = new Reflect(Parity.EVEN, Parity.ODD, Parity.EVEN, Parity.EVEN);
    Displace displace = new Displace(0.0,1.0,0.0,0.0);
    Rotate rotate = new Rotate(Axis.Z, Util.degsToRads(10));
    Boost boost = new Boost(Axis.X, 0.5);
    CoordTransform[] ops = {rotate, displace, reflect, boost};
    CoordTransformPipeline t = new CoordTransformPipeline(ops);
    
    FourVector in = FourVector.from(1.0, 1.0, 0.0, 0.0);
    FourVector out = t.toNewFrame(in);
    FourVector backIn = t.toNewVector4(out);
    if (! in.equalsWithEpsilon(backIn)) {
      throw new RuntimeException("Unequal after reversal.");
    }
  }
  
  /**
   Constructor.
   @param operations 0 or more transform operations, as applied in the 'forward' direction.
   The order is significant. A pipeline of operations needed to do the desired job. 
  */
  public CoordTransformPipeline(CoordTransform[] operations) {
    this.operations = operations;
  }
  
  /** Apply the operations (in order) to the given Vector4. */
  @Override public FourVector toNewFrame(FourVector vec) {
    FourVector result = vec;
    for (CoordTransform op : operations) {
      result = op.toNewFrame(result);
    }
    return result;
  }
  
  /** 
   Apply the operations to the given Vector4, but in <em>reverse</em> order <em>and</em> 
   with the <em>inverse</em> transform. 
  */
  @Override public FourVector toNewVector4(FourVector vecPrime) {
    FourVector result = vecPrime;
    List<CoordTransform> reversedOps = Arrays.asList(operations);
    Collections.reverse(reversedOps);
    for (CoordTransform op : reversedOps) {
      result = op.toNewVector4(result);
    }
    return result;
  }
  
  // PRIVATE 
  
  private CoordTransform[] operations;

}
