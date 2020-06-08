# special-relativity-core
Core data and operations commonly needed in special relativity.

## What I've learned so far in this project

Hollywood simulations of stars streaking rapidly by a relativistic spacecraft are definitely not realistic. 
Most of the sky would appear black to the human eye.
Stars would almost always appear only in the forward direction. 
A rare exception would be a close fly-by of a bright early-type star (spectral class O, B), which would 
remain visible in the 'backward' direction for a long time.


Working with units in which c=1 is natural and desirable.
You can think, for example, of light-years and years, or light-seconds and seconds.
As long as the 45-degree path for light rays is intact, you're fine.


If you can accelerate at 1 gee for a few months to a year, your gamma gets quite high rather rapidly.

Time dilation and length contraction are two sides of the same coin.
The traveler measures length contraction along the axis of motion (and no time dilation), 
while the stay-at-home measures time dilation on the spacecraft (but no length contraction).


The speed β for some real objects (extreme cosmic rays) can't be represented with a Double in Java.
It's too close to 1 (about 22 decimals). Only the BigDecimal class can represent such numbers.
However, working with BigDecimal is more painful than working with Double, so this is 
left out as a special case.



The prototype for a 4-vector isn't an event x, it's rather <a href='http://www.scholarpedia.org/article/Special_relativity:_mechanics'>the differential Δx</a>.
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


Equations are poor at expressing the geometry being used.
The idea of an operation/transform and its inverse is almost always present in some way.
Different choices of geometry usually mean one direction of the transform is preferred over another.
The inverse is still there, it's just in the background.
  
 

 




