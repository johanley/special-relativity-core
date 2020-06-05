package sr.core.transform;

/**
 Simply return the given {@link FourVector} without change.
 This was created in order to avoid check-for-null code when 
 a transform is absent. 
*/
public final class NoOpTransform implements CoordTransform {
  
  /** Return the given Vector4 without change. */
  @Override public FourVector toNewVector4(FourVector vec) {
    return vec;
  }

  /** Return the given Vector4 without change. */
  @Override public FourVector toNewFrame(FourVector vec) {
    return vec;
  }
  
}
