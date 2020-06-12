/**
 A <em>dogleg boost</em> is a pair of boosts that aren't in the same line (not collinear).
 
 <P>A pair of boosts commute if and only if they are in the same line.
 Dogleg boosts don't commute: switching the order of the boosts changes the outcome.
 
 <P>A dogleg boost equates to a boost plus a rotation (Thomas-Wigner rotation).
 
 <P>Use three frames K, K', then K'':
 <ul>
  <li>in K, first boost along X to K'.
  <li>in K', second boost along Y to K''.
 </ul>
 
 <P>In K' (in the middle): the axes of K and K'' are all parallel to the axes of K'.
 
 <P>But K and K'' DON'T see their respective X and Y axes as being parallel. 
 For that pair, only the Z axes are parallel, in the above case. 
 That is Thomas-Wigner rotation.
 
 
References:
<ul>
 <li><a href='http://www.nucleares.unam.mx/~alberto/apuntes/ferraro.pdf'>Rafael Ferraro and Marc Thibeault</a>
 <li><a href='https://en.wikipedia.org/wiki/Wigner_rotation'>wikipedia</a>
 <li><a href='https://arxiv.org/pdf/1102.2001.pdf'>O'Donnell and Visser</a>
</ul> 

<P>Thomas precession (radians per second, for an object with a changing velocity) is different.
 <a href='https://jila.colorado.edu/arey/sites/default/files/files/seven(1).pdf'>Smoot</a>: 
<em> "The effect is connected with the fact that two successive Lorentz
transformations in different directions are equivalent to a Lorentz transformation plus
a three dimensional rotation. This rotation of the local frame of rest is the kinematic
effect that causes the Thomas precession."</em>
 
<P>
 The Thomas precession vector is proportional to the 
 cross product of the 3-velocity and its time-derivative (its 3-acceleration).
 So, the magnitude of the precession vector is 0 when that cross-product is 
 the 0-vector (no acceleration, no velocity, or both are in the same line).
 The spin rate is very small at low speeds, and increases rapidly as speeds approach the limit 
 (even more so than gamma).
*/
package sr.explore.dogleg;