package sr.core.vector3.transform;

import sr.core.vector3.ThreeVector;

/**
 General spatial coordinate transformations built up out of simpler parts, as {@link SpatialTransform} objects.
*/
public final class SpatialTransformPipeline implements SpatialTransform {

  /** 
   Factory method. 
   @param operations 0 or more transform operations. The order is significant! 
  */
  public static SpatialTransformPipeline join(SpatialTransform... operations) {
    return new SpatialTransformPipeline(operations);
  }
  
  /** 
   Apply the {@link SpatialTransform#changeGrid(ThreeVector)} operation to the given 3-vector.
   Use the same order as the items passed in the factory method. 
  */
  @Override public ThreeVector changeGrid(ThreeVector vec) {
    ThreeVector result = vec;
    for (SpatialTransform op : operations) {
      result = op.changeGrid(result);
    }
    return result;
  }
  
  /** 
   Apply the {@link SpatialTransform#changeVector(ThreeVector)} operation to the given 3-vector. 
   Use the same order as the items passed in the factory method. 
  */
  @Override public ThreeVector changeVector(ThreeVector vec) {
    ThreeVector result = vec;
    for (SpatialTransform op : operations) {
      result = op.changeVector(result);
    }
    return result;
  }
  
  @Override public String toString() {
    StringBuilder builder = new StringBuilder("spatial-transform-pipeline ");
    for(SpatialTransform t : operations) {
      builder.append(t.toString() + " ");
    }
    return builder.toString().trim();
  }
  
  // PRIVATE 
  
  private SpatialTransform[] operations;
  
  private SpatialTransformPipeline(SpatialTransform... operations) {
    this.operations = operations;
  }

}
