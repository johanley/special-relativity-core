/**
 A <em>dogleg boost</em> is a pair of boosts that aren't in the same line (not collinear).
 
 <P>A pair of boosts commute if and only if they are in the same line.
 Dogleg boosts don't commute: switching the order of the boosts changes the outcome.
 
 <P>A dogleg boost equates to a boost plus a rotation (Silberstein rotation, also known as Thomas-Wigner rotation).
 
 <P>In this case three frames are needed - K, K', then K'':
 <ul>
  <li>in K, first boost along X to K'.
  <li>in K', second boost along Y to K''.
 </ul>
 
 <P>In K' (in the middle): the axes of K and K'' are all parallel to the axes of K'.
 
 <P>But K and K'' DON'T see their respective X and Y axes as being parallel. 
 For that pair, only the Z axes are parallel, in the above case. 
 That is Silberstein (Thomas-Wigner) rotation.
 
 <P>An interesting way of describing part of the Silberstein rotation is that <em>a revolution necessarily implies a rotation</em>. 
 Caveat: a complete revolution isn't necessary for the effect to occur. A more exact phrase would be <em>a change in direction of motion implies a rotation</em>.
 
 <P>Space-time has an inherently <em>twisted</em> aspect to it.
 
 
References:
<ul>
 <li><a href='http://www.nucleares.unam.mx/~alberto/apuntes/ferraro.pdf'>Rafael Ferraro and Marc Thibeault</a>
 <li><a href='https://en.wikipedia.org/wiki/Wigner_rotation'>wikipedia</a>
 <li><a href='https://arxiv.org/pdf/1102.2001.pdf'>O'Donnell and Visser</a>
</ul> 

<P>Thomas precession (radians per second, for an object with a changing velocity) is related but different.
 <a href='https://jila.colorado.edu/arey/sites/default/files/files/seven(1).pdf'>Smoot</a>: 
<em> "The effect is connected with the fact that two successive Lorentz
transformations in different directions are equivalent to a Lorentz transformation plus
a three dimensional rotation. This rotation of the local frame of rest is the kinematic
effect that causes the Thomas precession."</em>

References for Thomas precession:
<ul>
 <li><a href='https://jila.colorado.edu/arey/sites/default/files/files/seven(1).pdf'>Smoot</a>
 <li><a href='https://demonstrations.wolfram.com/ThomasPrecessionInAcceleratedPlanarMotion/'>Rodd-Routley</a>
 <li><em>Gravitation</em> by Misner, Thorne, Wheeler. Equations 6.29, 41.46
</ul> 

 
<P>
 The Thomas precession vector is proportional to the 
 cross product of the 3-acceleration and the 3-velocity (in that order!).
 So, the magnitude of the precession vector is 0 when that cross-product is 
 the 0-vector (no acceleration, no velocity, or both are in the same line).
 The change in angle after one orbit is 2*pi(1-gamma), and its 'retrograde', opposite to the sense of the circular motion.
 It approaches a one full rotation in the relativistic limit.
*/
package sr.explore.dogleg;