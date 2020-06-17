# special-relativity-core
Core data and operations commonly needed in special relativity.

## What I've learned so far in this project

**Strange but true**: a boost changes the *measured* rates all clocks in the universe except for those moving the same way as you. 
A boost changes the *measured* geometry of all objects in the universe (the flattening effect) except for those moving the same way as you.
*But*, the light-slice view is significantly different from the time-slice view. 
In the forward direction, the flattening is replaced by an actual expansion, while in the reverse direction 
the flattening increases (doubling in the ultra-relativistic limit). 
Similarly, the Doppler factor significantly changes how frequencies (clocks) are measured by a single detector.

**Time dilation and length contraction are two sides of the same coin.**
The traveler measures flattening along the line of motion (and no time dilation on the spacecraft). 
The stay-at-home measures time dilation on the spacecraft (but no flattening along the line of its motion).


**The term *length contraction* is a misleading one.**
The effect changes more than length: it changes geometry, angles, length, shape.
A better word is *flattening*.
Length contraction emphasizes a special case: when a stick is parallel to the line of the boost.
It's only that special case in which the effects are limited to a change of length.
In every other case, the stick will change both length and direction.

**To see the 3D geometry in a given frame, you always need a time-slice in the given frame.**
To see the length of a stick (in a given frame), you need a time-slice (in that frame).
To see the direction in which the stick is pointing (in a given frame), you need a time-slice (in that frame).


**Hollywood simulations of stars streaking rapidly by a relativistic spacecraft are definitely not realistic.** 
Most of the sky would appear black to the human eye.
Stars would almost always appear only in the forward direction. 
A rare exception would be a close fly-by of a bright early-type star (spectral class O, B), which would 
remain visible in the 'backward' direction for a long time.


**Working with units in which c=1 is natural and desirable.**
You can think, for example, of light-years and years, or light-seconds and seconds.
As long as the 45-degree path for light rays is intact, you're fine.


If you can accelerate at 1 gee for a few months to a year, your gamma gets quite high rather rapidly.


**The speed β for some real objects (extreme cosmic rays) can't be represented with a Double in Java.**
It's too close to 1 (about 22 decimals). Only the BigDecimal class can represent such numbers.
However, working with BigDecimal is more painful than working with Double, so this is 
left out as a special case.


**The prototype for a 4-vector isn't an event x, it's rather
<a href='http://www.scholarpedia.org/article/Special_relativity:_mechanics'>the differential Δx</a>.**
This is because of the displacement operation: it applies to events, but to nothing else:
it doesn't apply to differential 4-vectors, which aren't sensitive to the location of the origin.
I had forgotten this.
It's interesting how coding this led me to the same conclusion: applying a displacement 
to a 4-velocity made no sense, and this became obvious in the computed result.
The displacement operation needs special handling, because it doesn't apply to differential 4-vectors (only to events).


Trying to model 4-vectors as generic classes seemed to be inferior to the simpler style of a more basic FourVector class. 
The only oddity is the need to distinguish between events (sensitive to displacement operations) and all of the differential 4-vectors (insensitive to displacement operations).


Having the core coord transforms lets you build a history out of simple building-blocks.
The building-blocks retain their simple geometry, while the coord transforms do the job of allowing 
all kinds of choices for changing the frame of reference.


**Equations are poor at expressing the geometry being used.**
The idea of an operation/transform and its inverse is almost always present in some way.
Different choices of geometry usually mean one direction of the transform is preferred over another.
The inverse is still there, it's just in the background.



**Two boosts commute only when they are in the same line.**
A dogleg pair of boosts is equivalent to a boost plus a rotation (in that order?).
The rotation is absent if the two boosts are along the same axis.
The (Thomas-Wigner) rotation angle is large when the speeds are high.
It's never larger than 90 degrees (same as the stick-flattening angle).

What's the relation between the Thomas-Wigner rotation and the regular geometry distortion of a boost?
A stick is angled at θ with respect to the direction of a boost.
After the boost, both the stick's length and orientation are different.
The formula relating the direction of the stick in the two frames is:
```
cot θ = (1/Γ) cot θ' 
```
The *change* in this angle θ is precisely the same as the Thomas-Wigner rotation angle:
```
tan θ = -(Γ1*Γ2*β1*β2)/(Γ1 + Γ2)
```
This confuses me. It seems like the breakdown of two boosts = boost + rotation no longer needs the rotation. 
  
 

**Terrell's paper *Invisibility of the Lorentz Contraction* (1959) seems to be of low quality.**
He simply asserts that the geometry is that of a rotation. He seems to be incorrect.
Here are some <a href='https://github.com/johanley/special-relativity-core/blob/master/notes/references.txt'>references</a> that contradict Terrell's assertions.

Scott and van Driel (1970):
  'The relation of the distortion to Terrel's (sic) "apparent rotation" can be appreciated but 
  certainly an interpretation merely in terms of rotation is not possible. The Lorentz contraction 
  can indeed be photographed but shows properly only in a direction perpendicular to the motion.'
  (van Driel and Scott have diagrams that are not for Terrell's case of small solid angle).
  
Mathews and Lakshmanan (1972): 
  "... the apparent shape is related to the shape at rest through a combination of nonuniform shear and 
  extension/contraction parallel to the direction of motion, which does not reduce to a rotation even 
  in the case of distant objects subtending a small angle at the observer... Clearly this picture of apparent rotation 
  cannot be right. "
  
See also Scott and Viner (1965), Eric Sheldon (1989).
  
The <a href='https://en.wikipedia.org/wiki/Terrell_rotation'>wikipedia article</a> makes no mention of these points.


Light-slice: a receding stick is always shorter than the Lorentz-Fitzgerald contraction.
At ultra-relativistic speeds, in the limit the length is half of the L-F length.
An approaching stick is always longer than the rest length.
This means that there are dramatic differences in geometry between a time-slice and a light-slice. 
This is especially in the forward direction: a time-slice has flattening by the factor 1/Γ, but 
the light-slice shows expansion!

