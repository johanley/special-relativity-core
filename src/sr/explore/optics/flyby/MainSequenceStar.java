package sr.explore.optics.flyby;

/**
 A toy model of a main sequence star, identified by its spectral class.
 Not to be taken seriously! 
*/
enum MainSequenceStar {
  
  O(30000.0,-5.6, 0.0000003), 
  B(20000.0,-1.1, 0.00125),
  A(8750.0, 1.9,  0.005), 
  F(6750.0, 3.4,  0.02),
  G(5600.0, 5.2,  0.03),
  K(4450.0, 7.5,  0.10),
  M(3050.0, 13.9, 0.7937497); //this relative frequency has spurious precision, to make sure all add up to 1.0
  
  /** Surface temperature of the star, in Kelvins (rest grid). */
  double surfaceTemperature() { return surfaceTemperature; }
  
  /** The visual magnitude of a star at a distance of 10 parsecs (rest grid).  */
  double absoluteMagnitude() { return absoluteMagnitude; }
  
  /** The relative probability of finding a star of this spectral type in a typical population of stars near the Sun. */
  double relativeFrequency() { return relativeFrequency; }
  
  /**
   A Doppler-shift applied to the effective temperature of a black-body spectrum.
   A typical star's spectrum is roughly approximated as a black-body spectrum.
   <P>Reference: 
   <pre>
   In search of the "starbow": The appearance of the starfield from a relativistic spaceship
   John M. McKinley, Paul Doherty 
   Am. J. Phys. vol 47 No. 4 April 1979 p309
  </pre>
  
   @param doppler the doppler factor corresponding to a detector moving relative to this star in some way.
  */
  double surfaceTemperature(Double doppler) { 
    return  doppler * surfaceTemperature;
  }

  /**
   The brightness of this star, at a distance of the given number of light-years. 
   No correction for extinction is applied. 
   WARNING: this method requires light-years as the distance unit.
 
   @param lightYears how far away the star is from the detector (light-years only!).
  */
  double apparentVisualMagnitude(double lightYears) {
    Double parsecs = lightYears / 3.261564; //RASC Observer's Handbook 2019
    Double m = absoluteMagnitude + 5*Math.log10(parsecs) - 5;
    return m;
  }
  
  /**
   The change in apparent visual magnitude of a star because of Doppler effects, modeled as having a black-body spectrum.
   Note that real stellar spectra only roughly approximate to that of a black-body.
   <P>Reference: 
   <pre>
   In search of the "starbow": The appearance of the starfield from a relativistic spaceship
   John M. McKinley, Paul Doherty 
   Am. J. Phys. vol 47 No. 4 April 1979 p309
   </pre>
  
   @param doppler the doppler factor corresponding to a detector moving relative to this star in some way.
  */
  double Δmagnitude(Double doppler) { 
    return 2.5*Math.log10(doppler) - 26000*(1.0/surfaceTemperature - 1.0/(doppler*surfaceTemperature));
  }

  //PRIVATE 
  
  private MainSequenceStar(double surfaceTemperature, double absoluteMagnitude, double relativeFrequency) {
    this.surfaceTemperature = surfaceTemperature;
    this.absoluteMagnitude = absoluteMagnitude;
    this.relativeFrequency = relativeFrequency;
  }
  
  private double surfaceTemperature;
  private double absoluteMagnitude;
  private double relativeFrequency;
}
