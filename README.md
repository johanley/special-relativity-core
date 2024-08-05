# special-relativity-core
Explore the consequences of the Lorentz Transformations.

Kinematics only, no dynamics.

In this project c=1, and the metric signature is (ct,x,y,z) = (+,-,-,-).

The book <a href='https://en.wikipedia.org/wiki/Structure_and_Interpretation_of_Classical_Mechanics'>Structure and Interpretation of Classical Mechanics</a> by Sussman, Wisdom, and Mayer
asserts that by implementing a physics theory in code, you gain deeper insight into the theory. 
I'm hoping that this is the case with this little project as well.

## What I've learned so far in this project

**To see the 3D geometry in a given frame, you always need a time-slice in the given frame (not a light-slice).**
To measure the length of a stick (in a given frame), you need a time-slice (in that frame).
To measure the direction in which a stick is pointing (in a given frame), you need a time-slice (in that frame).


**The term *length contraction* is a misleading one.**
The effect changes more than length: it changes geometry, angles, length, shape.
A better word is *flattening*.
Length contraction emphasizes a special case: when a stick is parallel to the line of the boost.
Only that special case has geometrical changes characterized solely as a change of length.
In every other case, the stick will change both length and direction.



**Strange but true**: a boost changes the *measured* rates of all clocks in the universe except for those moving the same way as you. 
A boost changes the *measured* geometry of all objects in the universe (the flattening effect) except for those moving the same way as you.
*However*, **the light-slice view is significantly different from the time-slice view.** 
In the forward direction (for the light-slice view), the flattening is replaced by an actual expansion, while in the reverse direction 
the flattening increases (and doubles in the ultra-relativistic limit). 
Similarly, the Doppler factor significantly changes how frequencies (clocks) are measured by a single detector.


**Time dilation and length contraction are two sides of the same coin.**
The traveler measures flattening-of-the-world along the line of motion (and no time dilation on the spacecraft). 
The stay-at-home measures time dilation on the spacecraft (but no flattening-of-the-world along the line of its motion).



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
This is because of the displacement-of-the-origin operation: it applies to events, but to nothing else:
it doesn't apply to differential 4-vectors, which aren't sensitive to the location of the origin.
I had forgotten this.
It's interesting how coding this led me to the same conclusion: applying a displacement-of-the-origin 
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

**Using the 4-momentum in modeling a history is more natural than using 4-velocity.**
4-momentum applies to all objects, while 4-velocity applies only to objects having mass.
For similar reasons, the proper time can parameterize the history of a massive particle,
but it can't do that for massless particles.


**Two boosts commute only when they are in the same line.**
A corner-boost (two non-collinear boosts) is equivalent to a boost plus a rotation.
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
  
**Silberstein described the rotational aspect of non-collinear boosts <a href='https://archive.org/details/theoryofrelativi00silbrich/page/n7/mode/2up'>in 1914</a>.**
Thomas and Wigner came much later. 
  
**The geometry of an object (for example, the direction in which a stick is pointing) is affected by both regular flattening and the Silberstein rotation.**
You need to be careful not to confuse the two effects.

**Characterizing a mass particle in SR requires two additional items.**
In kinematics, everything comes from the position <b>r</b>(t).
In Newtonian kinematics, there are 3 main items characterizing a mass particle:
  - <b>r</b>(t) position
  - <b>v</b>(t) velocity
  - <b>a</b>(t) acceleration
  
In SR, there are 5 main items characterizing a mass particle:   
  - <b>r</b>(t) position
  - <b>v</b>(t) velocity
  - <b>a</b>(t) acceleration
  - a proper-time (because of time dilation)
  - an orientation (because of the Silberstein rotation)
  
**Transformations can be <a href='https://en.wikipedia.org/wiki/Active_and_passive_transformation'>active or passive</a>. 
You need to always be aware of which case applies. **
It's very easy to forget this point, and to make mistakes in the sense of a transformation. 
It helps if the code makes this distinction hard to ignore.  

**Four-vectors and three-vectors are not completely analogous**.
The cross product applies only to three-vectors.

The angle between vectors has a fundamentally different meaning in Minkowski space.
It's defined only between pairs of 4-vectors that are either both time-like or both space-like. 
Angles between 4-vectors are defined as ratios of lengths, but in this case these lengths come from the Minkowski metric, not the Euclidean one.
(Minkowski distance along a hyperbolic arc, divided by the 'radius' of the hyperbola, the distance from the origin to the apex of the hyperbola.)


**The <a href='https://en.wikipedia.org/wiki/Rodrigues%27_rotation_formula'>Rodrigues' rotation formula</a> is useful and compact.**

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
This is especially true in the forward direction: a time-slice has flattening by the factor 1/Γ, but 
the light-slice shows expansion!

