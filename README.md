# special-relativity-core
Core data and operations commonly needed in special relativity.

## What I learned in building this project

The speed β for some real objects (extreme cosmic rays) can't be represented with a Double.
It's too close to 1 (about 22 decimals). Only the BigDecimal class can represent such numbers.
However, working with BigDecimal is more painful than working with Double, so this is 
left as a special case.

Hollywood simulations of stars streaking rapidly by a relativistic spacecraft are definitely not realistic. Most of the sky would be black to the human eye. 
Stars would almost always appear only in the forward direction. 
A rare exception would be nearby early-type stars (spectral class O, B). 

The prototype for a 4-vector isn't an event x, it's rather <a href='http://www.scholarpedia.org/article/Special_relativity:_mechanics'>the differential Δx</a>.
This is because of the displacement operation: it applies to events, but to nothing else:
it doesn't apply to differential 4-vectors, which aren't sensitive to origin.
I had forgotten this.
It's interesting how coding this led me to the same conclusion: applying a displacement 
to a 4-velocity made no sense, and this became obvious in the computed result.

Trying to model 4-vectors as generic classes seemed to be inferior to the simpler 
style of a simple FourVector class. The only oddity is the need to distinguish 
between events (sensitive to displacement operations) and all of the differential 4-vectors.



