package sr.explore.flyby;

/**
 A toy model of a main sequence star.
 Not to be taken too seriously! 
*/
public enum MainSequenceStar {
  
  O(30000.0,-5.6, 0.0000003), 
  B(20000.0,-1.1, 0.00125),
  A(8750.0, 1.9,  0.005), 
  F(6750.0, 3.4,  0.02),
  G(5600.0, 5.2,  0.03),
  K(4450.0, 7.5,  0.10),
  M(3050.0, 13.9, 0.7937497); //the freq has spurious precision, to make sure all add up to 1.0
  
  double surfaceTemp() { return T; }
  double absoluteMag() { return absoluteMag; }
  double relativeFreq() { return relativeFreq; }

  //PRIVATE 
  
  private MainSequenceStar(double T, double absoluteMag, double relativeFreq) {
    this.T = T;
    this.absoluteMag = absoluteMag;
    this.relativeFreq = relativeFreq;
  }
  
  /** Surface temperature in Kelvins. */
  private double T;
  
  /** Absolute magnitude. */
  private double absoluteMag;
  
  private double relativeFreq;

}
