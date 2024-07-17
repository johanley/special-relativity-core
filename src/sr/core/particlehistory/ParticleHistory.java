package sr.core.particlehistory;

import sr.core.transform.FourVector;

/** 
 The parameterized history (worldline) of an object represented as a particle.
 
 <P>The caller needs to define exactly what the parameter λ of the history represents.
 λ can represent different things:
 <ul> 
  <li>the coordinate-time t in a given frame 
  <li>the proper-time τ of an object having mass)  
  <li>any other parameter having suitable uniqueness/continuity attributes
 </ul>
 
 <P>This interface, by itself, models only particles, not extended objects.
 So, the histories it represents are curves in space-time, not extended tubes.
 
 <P>The history-tube of an extended object might be represented with N of these particle-histories.
 For example, a stick might be represented by two particle histories, one for each end of the stick. 
*/
public interface ParticleHistory {

  /**
   Return the event for the given parameter.
   The λ parameter uniquely identifies each event in the history.
  */
  FourVector event(double λ);

  /** 
   Return the 4-momentum for the given parameter.
   The λ parameter uniquely identifies each event in the history.
   
   <P>The 4-momentum is modeled here instead of the 4-velocity because the latter applies 
   only to objects having mass, while 4-momentum applies both to massive and massless objects.
  */
  FourVector fourMomentum(double λ);
  
}
