package sr.core;

/**
 Utility methods related to the physics of a star. 
*/
public final class Star {

  /**
    The change in apparent visual magnitude of a star because of Doppler effects, modeled as having a black-body spectrum.
    (Note that real stellar spectra only roughly approximate to that of a black-body.
    <P>Reference: 
    <pre>
    In search of the "starbow": The appearance of the starfield from a relativistic spaceship
    John M. McKinley, Paul Doherty 
    Am. J. Phys. vol 47 No. 4 April 1979 p309
    </pre>
    
    @param D the doppler factor
    @param T0 in kelvins, the absolute temperature of the object in its rest frame
   */
   public static final Double Δmag(Double D, Double T0) { 
     return 2.5*Math.log10(D) - 26000*(1.0/T0 - 1.0/(D*T0));
   }

  /*** 
   No correction for extinction is applied. 
   WARNING: this method requires light-years as the distance unit.
  
   @param M absolute magnitude 
   @param lightYears how far away the star is from the detector (light-years only!).
  */
  public static final Double apparentVisualMagnitude(Double M, double lightYears) {
    Double parsecs = lightYears / 3.261564; //RASC Observer's Handbook 2019
    Double m = M + 5*Math.log10(parsecs) - 5;
    return m;
  }

  /**
   The Doppler-shift applied to the effective temperature of a black-body spectrum.
   <P>Reference: 
   <pre>
   In search of the "starbow": The appearance of the starfield from a relativistic spaceship
   John M. McKinley, Paul Doherty 
   Am. J. Phys. vol 47 No. 4 April 1979 p309
   </pre>

  @param D the doppler factor
  @param T0 in kelvins, the absolute temperature of the object in its rest frame
  */
  public static final Double T(Double D, Double T0) { 
    return  D*T0;
  }

}
