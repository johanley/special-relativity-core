/** 
 Library for data and operations commonly needed in special relativity.

 <P><b>This library assumes units in which the speed limit c = 1.</b> 
 For example light-years and years, or light-seconds and seconds.
 If you're working with other units, you'll need to do conversions when 
 interacting with this library.
 
 <P>All angles in this library are in radians.
 
 <P>Almost all physical quantities are represented as type {@link Double} in this library.
 The sole exception is extreme values of β when it's near 1.
 For extreme speeds, the difference between β and 1 is very small, 
 so small that it can't be represented with a Double in Java.
 Instead, it needs to be represented with a {@link java.math.BigDecimal}.
 You need to be aware of that.
 Such cases are not simply theoretical: the speeds of the fastest cosmic rays are the best example.   
 
 <P>This library uses Cartesian coordinates (right-handed).
 If the caller needs to use different spatial coords, then the caller needs to 
 change to Cartesian coords before/after interacting with this library.
 
 <P>The metric used in the <em>Classical Theory of Fields</em> by Landau and Lifshitz (1975) is used here: (+,-,-,-,).
 With that metric, all time-like intervals are real numbers, not imaginary.
 
 <P>This library is not optimized for execution speed.
*/
package sr.core;