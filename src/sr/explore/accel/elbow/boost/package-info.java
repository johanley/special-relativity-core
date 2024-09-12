/**
 A change in velocity which has a change of direction.
 
 <P> Here, we define an <em>elbow-boost</em> as a <b>pair</b> of boosts that aren't in the same line (not collinear).
 
 <P>A pair of boosts commute only if they're in the same line.
 Elbow-boosts don't commute: switching the order of the boosts changes the outcome.
 
 <P>An elbow-boost equates to a boost plus a rotation (here called the kinematic rotation, also known as Thomas-Wigner rotation).
 
 <P>An elbow-boost always needs three grids - K, K', and K''.
 Example:
 <ul>
  <li>in K, first boost along X to K'.
  <li>in K', second boost along Y to K''.
 </ul>
 
 <P>In the intermediate K' grid, the axes of K and K'' are all parallel to the axes of K'.
 
 <P>But K and K'' <b>don't</b> see their respective X and Y axes as being parallel. 
 For that pair, only the Z axes are parallel, in the above case. 
 That is kinematic rotation.

 <P>Here, we define a <em>corner-boost</>: if, in the intermediate grid K', the directions of the two velocities are perpendicular.
 This is a simpler case, which illustrates well the basic effects.
  
 <P>An approximate way of describing kinematic rotation is that <em>a revolution imparts a rotation</em>. 
 
<P>References:
<ul>
 <li><a href='http://www.nucleares.unam.mx/~alberto/apuntes/ferraro.pdf'>Rafael Ferraro and Marc Thibeault</a>
 <li><a href='https://en.wikipedia.org/wiki/Wigner_rotation'>wikipedia</a>
 <li><a href='https://arxiv.org/pdf/1102.2001.pdf'>O'Donnell and Visser</a>
</ul> 

<P>Thomas precession (radians per second, for an object with a changing velocity) is related to kinematic rotation.
 <a href='https://jila.colorado.edu/arey/sites/default/files/files/seven(1).pdf'>Smoot</a>: 
<em> "The effect is connected with the fact that two successive Lorentz
transformations in different directions are equivalent to a Lorentz transformation plus
a three dimensional rotation. This rotation of the local frame of rest is the kinematic
effect that causes the Thomas precession."</em>

References for kinematic precession:
<ul>
 <li><a href='https://jila.colorado.edu/arey/sites/default/files/files/seven(1).pdf'>Smoot</a>
 <li><a href='https://demonstrations.wolfram.com/ThomasPrecessionInAcceleratedPlanarMotion/'>Rodd-Routley</a>
 <li><em>Gravitation</em> by Misner, Thorne, Wheeler. Equations 6.29, 41.46
</ul> 

<P>
 The kinematic precession vector is proportional to the 
 cross product of the 3-acceleration and the 3-velocity (in that order!).
 So, the magnitude of the precession vector is 0 when that cross-product is 
 the 0-vector (no acceleration, no velocity, or both are in the same line).
 The change in angle after one orbit is 2*pi(1-gamma), and it's retrograde, that is, opposite to the sense of the circular motion.
*/
package sr.explore.accel.elbow.boost;