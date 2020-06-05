/** 
 Histories (worldlines) of objects.
 
 <P>
 The idea here is to be able to model many different histories out of simpler building blocks. 
 The building blocks provided here are: 
 <ul>
  <li>uniform motion at constant velocity
  <li>uniform motion at constant linear acceleration
  <li>uniform circular motion at constant speed
 </ul>
 
 These building blocks have histories that represent 
 the simplest case: passing through the origin at t=0.
 In the case of linear acceleration, the velocity is also 0 at t=0.  
 
 <P>
 Combined with different {@link CoordTransform}s, you can 
 add simple pieces of a history together to make different kinds of motions. 
*/
package sr.core.history;