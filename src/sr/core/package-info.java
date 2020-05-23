/** 
 Library for data and operations commonly needed in special relativity.
 
 <P>All physical quantities are of type Double in this library.
 
 <P>This library is not optimized for execution speed.
 
 <P>This library uses Cartesian coordinates (right-handed).
 If the caller needs to use different spatial coords, then the caller needs to 
 change to Cartesian coords before/after interacting with this library.
 
 <P>The metric used in the <em>Classical Theory of Fields</em> is used here: (+,-,-,-,).
 With that metric, all time-like intervals are real numbers, not imaginary.
 
 <P>All angles are in radians.
*/
package sr.core;