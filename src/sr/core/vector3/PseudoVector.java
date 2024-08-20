package sr.core.vector3;

/**
 Identifies non-axial vectors.
 Most pseudo-vectors result from the cross product of two axial vectors.
 This is a <em>tag</em> interface with no methods.
 
  <P>Design note: using this interface means that an <em>instanceof</em> is used to determine 
  behaviour, which usually isn't the best technique.
*/
public interface PseudoVector {

}
