/** 
 Transform the space-time coords of any event from one inertial frame K to another inertial frame K'.
 
 <P>The {@link Transform} operations can also be viewed not as a change of inertial frame, 
 but as mapping of one event to another, all with respect to the one and the same inertial frame.
 
 <P>In Minkowski space-time, there are 4 fundamental operations:
 <ul>
  <li>{@link Displacement}: <em>displacement</em> (or translation) of the origin (see below!).
  <li>{@link Reflection}:<em>reflection</em> (or parity) in which one or more of the 4 space-time axes are reversed. 
  Reflections about an <em>even</em> number of spatial axes reduce to a rotation, so you usually  
  exclude such operations, as being already enumerated as a spatial rotation operation. 
  <li>{@link Rotate}: spatial <em>rotation</em> (3 spatial axes only in this case)
  <li>{@link Boost}: the Lorentz Transformation, modeled here as "mixing" the time axis with one of the spatial axes.
 </ul>

 <P>The {@link Displacement} operation is an oddball: it's the only operation that changes the zero-vector 
 (the origin). Displacements affect <em>events</em>, but they don't affect 4-vectors built out of a 
 difference-of-events (4-velocity, and so on). 
 <P>(In mathematical terms, a displacement is an affine transformation, but not a linear one.)
 
 <P>{@link Transform} operations can be chained together in a {@link TransformPipeline}. 
 A pipeline can also be reversed, to compute the inverse transformation from K' to K.
*/
package sr.core.vector4.transform;