package sr.core.transform;

/** 
 This enum exists because {@link Displace} operations don't apply to all 4-vectors.
 The prototype 4-vector is actually not an event vector x, but rather  
 its displacement Δx, which is unaffected by changes in the origin.
 
 <P>A {@link Displace} operation leaves differential 4-vectors unchanged, but 
 will indeed alter the 4-vector representing an event.
 
 <P>General code which applies {@link CoordTransform}s to {@link FourVectors} needs 
 to know when to apply {@link Displace} operations.
*/
public enum ApplyDisplaceOp {

    /** As far as I know, this applies only to event 4-vectors. */
    YES, 
    
    NO;
  
}
