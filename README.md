# special-relativity-core
Explore the consequences of the Lorentz Transformations.

Mostly kinematics.

The book <em><a href='https://en.wikipedia.org/wiki/Structure_and_Interpretation_of_Classical_Mechanics'>Structure and Interpretation of Classical Mechanics</a></em> by Sussman, Wisdom, and Mayer
asserts that by implementing a physics theory in code, you gain deeper insight into the theory. 
I'm hoping that this is the case with this little project as well.

*"The purpose of computing is insight, not numbers."* - attributed to R. W. Hamming

*"If you could see it then you'd understand."* - from the song *Speed of Sound*, by *Cold Play*

In this project: 
 - c=1
 - the metric signature is (ct,x,y,z) = (+,-,-,-)

## Jargon

This project uses the following non-standard words. 

I hope you don't find this too bothersome.
**I often use parens to make the jargon "bilingual" for the reader** - for example, *kinematic (Wigner) rotation*.

**Grid** is used (in the code, not in the documentation) instead of *inertial frame of reference*.

**Elbow-boost** is used instead of *a non-collinear pair of boosts.* 

**Corner-boost** is an elbow-boost with an angle of 90° between boosts (as seen in the intermediate frame).

**Kinematic rotation** is used instead of *Wigner rotation* and *Thomas-Wigner rotation*.

**Kinematic spin** is used instead of *Thomas precession*. This has units of radians per second, not angular momentum.

**Flattening** is used instead of *Lorentz-Fitgerald contraction* and *length contraction*.

**Phase-gradient** <em>k</em> is used here instead of *wave vector* <em>k</em>. 
This matches up with <em>phase-velocity</em>. 

**Boost** here refers to a boost along any direction in space, not just along a coordinate axis.

**Time-slice** is used instead of *the intersection of an object's history with a space-like hypersurface of constant ct*.

**Light-slice** is used instead of *the intersection of an object's history with the past light cone of a detection-event*.

**Doppler cone** is used to refer to the right circular cone generated by a set of wave vectors *k<sup>i</sup>* having, in some frame, the same time component, and all possible directions.
When a Doppler cone is boosted it retains its conical form, but is no longer a *right circular* cone; it's still referred to as a Doppler cone in that case.

**Lorentz transformation** here means a boost in any direction, excluding spatial rotations.
(The exact definition of this term varies between authors; that's why I'm stating it here.) 

**Invariance** here means invariance of the fundamental quadratic form. 
That is, it's related to the transformations of the <a href='https://en.wikipedia.org/wiki/Poincar%C3%A9_group'>Poincaré group</a>.
This includes boosts, spatial rotations, displacements (in time and space), and reversals (of time and space).
 
## References

 - *<a href='https://archive.org/details/spacetimephysics00edwi_0'>Spacetime Physics</a>*, by Taylor and Wheeler, is a good introduction.
 - *<a href='https://archive.org/details/isbn_9788181477873_2/mode/2up'>The Classical Theory of Fields</a>*, by Landau and Lifshitz, has deep insight.
 - *<a href='https://archive.org/details/theoryofrelativi00silbrich/page/n7/mode/2up'>The Theory of Relativity</a>* by Ludwik Silberstein discusses the rotation related to non-linear acceleration.
 - <a href='http://specialrelativity.net/'>specialrelativity.net</a> is a website created by the author of this project, which explains the theory in elementary terms. It has numerous graphics and animations. 

## What I've learned so far in this project

**Working with units in which c=1 is natural and desirable.**
You can think, for example, of light-years and years, or light-seconds and seconds.
Numerically, its pleasant to work with numbers that are usually near 1.
It emphasizes the physics, and you aren't distracted by large or small numbers simply because of a choice of units.

**The speed β for some real objects (extreme cosmic rays) can't be represented with a Double in Java.**
It's too close to 1 (about 22 decimals). Only the *BigDecimal* class can represent such numbers.
However, working with *BigDecimal* is more painful than working with *Double*, so this is left out as a special case.

**Formulas: many-formulas versus one-formula is a very interesting question.**
This project implements as many calculations as possible with the core Lorentz Transformation, instead of using a new formula for each phenomenon. 
The sole exception is kinematic spin (Thomas precession). 

Advantages: 
 - it *directly* manifests how the Lorentz Transformation generates diverse phenomena.
 - it gives you more confidence in the *correctness* of your code, since each new use case acts as a test of the core formula.
 - it pushes one towards seeing the beautiful unity in the physics in as direct a manner as possible.

Having a new formula for each phenomenon is both useful and desirable; but such formulas can be viewed as *short-cuts for human computation*. 
A computer program doesn't need such short-cuts. 
It actually benefits by avoiding them, since each new formula is a new source of potential error. 


**Formulas: textbooks and computer programs have opposing biases.**
Textbooks are biased towards the derivation of formulas for each and every phenomenon.
Computer programs are biased in the opposite direction, towards using as few formulas as possible.

**Computation is a teaching tool.**
It can be argued that computation provides an effective way of increasing one's understanding of physics, that could be used 
for one's first encounters with a theory.

**The jargon of the subject is not strictly uniform.**
For example, the exact meaning of <em>Lorentz group</em> changes according to who's doing the talking. 
I prefer to refer to the <em>Poincaré group</em>, which includes all the transformations
which leave the fundamental quadratic form invariant: boost, rotation, displacement, and reversal.
 
**All phenomena connected with the theory are *aspects of one single thing*: the structure of space-time.**
We can take the Poincaré group and the invariance of the fundamental quadratic form as the definition of that structure.

<!--
IS THIS CORRECT?
A beautiful example of this is the connection between aberration and the Doppler effect on the one hand (which are optical effects), and the flattening effect on the other hand (which is a geometrical effect).
To see the connection, look at the boost of a Doppler cone. 
(A Doppler cone is generated by a wave vector k<sup>i</sup> having a given frequency, but different spatial directions.)
In an unboosted frame K, the top of the Doppler cone forms a circle.
In a boosted frame K', that circle morphs into an ellipse.
The exact same morphing applies to events that correspond one-to-one to the wave vectors.
(The Lorentz Transformations are *agnostic*, in the sense that they apply to different sets of physical quantities.)
You can view the same circle and ellipse shapes as a time-slices across a history.
A corresponding boost from K' to K transforms the ellipse (a time-slice in K') into a circle (a time-slice in K).
That's just the flattening effect applied to an ellipse.
--> 

**The <a href='https://en.wikipedia.org/wiki/Rodrigues%27_rotation_formula'>Rodrigues' rotation formula</a> is a useful and compact way of rotating a 3-vector about an arbitrary axis.**

**The Java programming language is capable of modeling the concepts of special relativity very cleanly.**

**Vector formulas usually have simpler implementations than component formulas.**
Moving from components to vectors can simplify a formula.
I found this with: 
 - the axis-angle approach to rotation (Rodrigues' formula)
 - the velocity transformation
 - the Lorentz transformation.

*However, I haven't retained this simplicity.*
What's more important to me here is to ensure correctness.
The main way of doing that is to have the core of almost all phenomena implemented by the Lorentz Transformations.
I have succeeded in doing so for all phenomena except for kinematic spin (Thomas precession) for circular motion. 
In that case, an implementation using the Lorentz Transformation requires numerical integration along the object's history.
I have included an example which shows how using N boosts to approximate a circular orbit with regular-sided polygons 
closely approximates the formula for kinematic spin (Thomas precession).


**Kinematic rotation is important.**
Most texts either ignore this effect altogether, or don't emphasize it.
This is unfortunate. 
This effect is one of the basic consequences of the Lorentz Transformations.


**Silberstein described the rotational aspect of elbow-boosts <a href='https://archive.org/details/theoryofrelativi00silbrich/page/n7/mode/2up'>in 1914</a>.**
Borel mentioned it (without much analysis) in 1913. Thomas and Wigner came much later. 

**The geometry of an object (for example, the direction in which a stick is pointing) is in general affected by both regular flattening (Lorentz-Fitzgerald contraction) and kinematic rotation.**
In general, when examining the geometry of an object, you need to disentangle these two effects.

**Kinematic rotation can 'spin-up' an object, but conservation laws aren't violated in doing so.**
The effect is kinematic. 
No work is required to spin-up the object.
The boost equations change geometry. 
Geometry means distances and angles. 
The orientation of an object is defined by angles.
So boosts can change the orientation of an object (as seen in a rest frame), and the multiple, continuous boosts seen in circular motion (for example) will continuously alter the orientation of an object.
No work is done on the object in the process.

**Kinematic spin is a spin which has no associated angular momentum or rotational energy.**
This is very strange!


**Characterizing the motion of a mass-particle in SR requires two additional items.**
In the Newtonian world, the history of a mass-particle in a given frame is described by its position vector <b>r</b>(t) and its derivatives, velocity <b>v</b>(t) and acceleration <b>a</b>(t).
  
In SR, there are 2 additional items one can attach to the history of a mass-particle with respect to a given frame:   
  - a proper-time (because *time* is relative)
  - an orientation (because *geometry* is relative)

Both proper-time and orientation are defined only as *differences* with respect to some chosen initial condition.



**Transformations almost always come in two variants, characterized by the sign of some quantity.**
(For example, see this article on <a href='https://en.wikipedia.org/wiki/Active_and_passive_transformation'>active and passive transformations</a>.) 
You need to always be aware of which case applies. 
It's very easy to forget this point, and to make mistakes in the sense of a transformation. 
It helps if the code makes this distinction hard to ignore.
(An example of a transformation that only has a single variant is the parity transformation: in order to reverse it, you apply the exact same operation a second time.)  

**Some textbooks do a poor job of expressing the two-fold nature of transformations.**
Sometimes a transformation is written in a book in only one of its two possible forms, without stating its inverse.
This is misleading.

**Hollywood simulations of stars streaking rapidly by a relativistic spacecraft are definitely not realistic.** 
In the ultra-relativistic case, most of the sky would appear black to the human eye.
Stars would almost always appear only in the forward direction. 
A (very) rare exception would be a close fly-by of a bright early-type star (spectral class O, B), which would 
remain visible in the 'backward' direction for a long time.

**If you could accelerate at 1 gee for a year, you could cross the Milky Way in about a year of proper-time.**

**The prototype for a 4-vector isn't an event x, it's rather
<a href='http://www.scholarpedia.org/article/Special_relativity:_mechanics'>the differential Δx</a>.**
This is because of the displacement-of-the-origin operation: it applies to events, but to nothing else:
it doesn't apply to differential 4-vectors, which aren't sensitive to the location of the origin.
I had forgotten this.
It's interesting how coding this led me to the same conclusion: applying a displacement-of-the-origin 
to a 4-velocity made no sense, and this became obvious in the computed result.
The displacement operation needs special handling, because it doesn't apply to differential 4-vectors (only to events).
This corresponds to the distinction made by mathematicians between *affine* and *linear*.
(In the code, the displacement operation is a no-operation for all four-vectors except for events.)

This all goes back to the fact that the fundamental quadratic form is defined in terms of <em>differences</em> between events.

**Four-vectors and three-vectors are not completely analogous**.
The cross product applies only to three-vectors.

The angle between vectors has a fundamentally different meaning in Minkowski space.
It's defined only between pairs of 4-vectors that are either both time-like or both space-like. 
Angles in Euclidean geometry are defined as the ratio of an arc along the circumference of a circle, divided by the radius of the circle.
Angles between 4-vectors can also be seen as ratios of lengths, but in this case these lengths come from the Minkowski metric, not the Euclidean one.
(That is, Minkowski-distance along a hyperbolic arc, divided by the 'radius' of the hyperbola, the distance from the origin to the hyperbola's apex.)

**Light is simpler than matter.**
The physics of plane monochromatic waves is simpler than the physics of mass-particles.
Their speed is always the same. 
They don't have properties that apply only to mass-particles: acceleration, proper-time, and orientation (related to kinematic rotation).
The effects of aberration and the Doppler effect are simpler to understand than the effects attached to matter (the distortions of geometry and time).
It can be argued that aberration and the Doppler effect should be investigated first when one begins to explore the consequences of the Lorentz Transformation.
 

**Time dilation and length contraction are two sides of the same coin.**
The traveler measures flattening along the line of motion (and no time dilation on the spacecraft). 
The stay-at-home measures time dilation on the spacecraft (but no flattening-of-the-world along the line of its motion).


**The spatial geometry of an object is defined by a time-slice in the given frame (not a light-slice).**
Spatial geometry consists of two things: distances and angles.
To measure the length of a stick (in a given frame), you need a time-slice, a surface with <em>ct=constant</em>, (in that frame).
To measure the direction in which a stick is pointing (in a given frame), you also need a time-slice (in that frame).

**Change-of-angles is just as significant as change-of-dimensions. The term *length contraction* is a misleading one.**
The effect changes more than length: it changes *geometry*, and geometry consists of angles and lengths.
A better word is *flattening*.
Length contraction emphasizes a special case: when a stick is parallel to the line of the boost.
Only that special case has geometrical changes characterized solely as a change of length.
In every other case, the stick will change both its length and its direction.


**A history intersects in dramatically different ways with a time-slice versus a light-slice.**
The intersection of a time-slice surface (with <em>ct=constant</em>) with an object's history defines the spatial geometry of an object, relative to a frame.
The intersection of the past light cone of a detector with an object's history (a light-slice) defines the optical appearance of the object at the detector.

For a receding stick travelling parallel to its longest dimension, the light-slice length is always shorter than the time-slice length when it's receding from the detector.
At ultra-relativistic recession speeds, the light-slice length approaches 0.5 times the time-slice length.

For an approaching stick, the light-slice length is always longer than the time-slice length.
Indeed, the light-slice length shows an expansion over the rest-length.
 
**Matrices are useful for expressing the Lorentz Transformation.**
With rearrangement of the covariant/contravariant indices, some standard expressions based on index gymnastics can be 
brought into a form which maps exactly to matrix multiplication. 
Example: the transformation of the electromagnetic field tensor.

**Aberration and the Doppler effect are two aspects of one phenomenon.**
Both come directly from the Lorentz Transformation of the wave vector k<sup>i</sup>.

**Two boosts commute only when they are in the same line.**
An elbow-boost (two non-collinear boosts) is equivalent to a boost plus a rotation.
The rotation is absent if the two boosts are along the same axis.
The kinematic (Thomas-Wigner) rotation angle is small for low speeds, but for 
ultra-relativistic speeds it increases rapidly without bound.

What's the relation between kinematic rotation and the regular geometry distortion of a boost?
A stick is angled at θ with respect to the direction of a boost.
After the boost, both the stick's length and orientation are different.
The formula relating the direction of the stick in the two frames is:
```
cot θ = (1/Γ) cot θ' 
```
The *change* in this angle θ is precisely the same as the kinematic rotation angle:
```
tan θ = -(Γ1*Γ2*β1*β2)/(Γ1 + Γ2)
```
This confuses me. It seems like the breakdown of two boosts = boost + rotation no longer needs the rotation. 
  

**The change in phase related to any wave phenomenon is an invariant scalar because it describes a fact in the world.**
Picture an electric clock having hands.
You plug it in, it runs for while, and then you unplug it.
Take the phase as describing the angular position of the minute hand. 
The *total change in the phase*, between the plug-it-in event and the unplug-it event, is an objective fact.
It doesn't depend on the coordinate frame in any way.
Every frame must agree on the size of the *change* in the phase.


These remarks apply to *any wave phenomenon*, not just to plane waves of light of a single frequency traveling in a vacuum.


**It's a common misconception that the dot product <em>k<sup>i</sup>x<sub>i</sub></em> of the phase-gradient (wave vector) <em>k<sup>i</sup></em> with the coordinates <em>x<sub>i</sub></em> is Poincaré-invariant.**
This is related to the fact that <em>&Delta;x<sub>i</sub></em> is the true 4-vector, not <em>x<sub>i</sub></em>.
The dot product <em>k<sup>i</sup>x<sub>i</sub></em> changes during displacement of the origin of coordinates.
The dot product <em>k<sup>i</sup>&Delta;x<sub>i</sub></em> does not change during displacement of the origin of coordinates.


**After a boost, a  plane wave (of any speed and nature) remains a plane wave.**
*"The concept of a plane wave is invariant under the Lorentz transformation: a plane wave remains a plane wave in all coordinate systems."* - 
<a href='https://archive.org/details/matveev-the-principles-of-electrodynamics/page/344/mode/2up?q=%22concept+of+a+plane+wave%22'>Principles of Electrodynamics</a>, Matveyev.
This is no surprise, but it's worth remembering. 


**There's a generalized form for the phase-gradient (wave vector) <em>k</em> which also allows for waves whose speed is less than <em>c</em>**.
With this expression (see <a href='https://arxiv.org/pdf/0801.3149v2'>formula (18)</a>), you can model, for example light waves in media, or sound waves.

For phase-velocity of <em>c</em>, the phase-gradient 4-vector is null.
 
For phase-velocity less than <em>c</em>, the phase-gradient 4-vector is space-like, because its time component is smaller.
In addition, after a boost from the frame in which the medium is at rest, the (3D) phase-gradient <b>k</b> is not generally parallel with the phase-velocity <b>u</b>.



<!--

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

-->
