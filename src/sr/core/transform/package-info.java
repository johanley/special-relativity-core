/** 
 Transform the space-time coords of any 4-vector from one inertial frame K to another frame K'.
 
 <P>The {@link CoordTransform} operations can also be viewed not as a change of inertial frame, 
 but as mapping of one 4-vector to another, all with respect to the one and the same inertial frame.
 
 <P>In Minkowski spacetime, there are 4 fundamental operations:
 <ul>
  <li>{@link Displace}: <em>displacement</em> (or translation) of the origin (see below!).
  <li>{@link Reflect}:<em>reflection</em> (or parity) in which one or more of the 4 space-time axes are reversed. 
  Reflections about an <em>even</em> number of spatial axes reduce to a rotation, so you usually  
  exclude such operations, as being already enumerated as a spatial rotation operation. 
  <li>{@link Rotate}: spatial <em>rotation</em> (3 spatial axes only in this case)
  <li>{@link Boost}: the Lorentz Transformation.
 </ul>

 <P>The {@link Displace} operation is an oddball: it's the only operation that changes the zero-vector 
 (the origin). Displacements affect <em>event</em> 4-vectors, but they don't affect 4-vectors built out of a 
 difference-of-events (4-velocity, and so on). 
 <P>(In mathematical terms, a displacement is an affine transformation, but not a linear one.)
 
 <P>{@CoordTransform} operations can be chained together in a {@link CoordTransformPipeline}. 
 A pipeline can also be reversed, to compute the inverse transformation from K' to K.
 
*/
package sr.core.transform;