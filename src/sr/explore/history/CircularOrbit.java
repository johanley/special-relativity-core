package sr.explore.history;

import java.util.ArrayList;
import java.util.List;

import sr.core.Axis;
import sr.core.Speed;
import sr.core.Util;
import sr.core.history.History;
import sr.core.history.UniformCircularMotion;
import sr.core.transform.FourVector;

public final class CircularOrbit {
  
  public static void main(String... args) {
    double radius = 1.0;
    List<String> lines = new ArrayList<>();
    lines.add("Axis  r β τmin τmax      v0      mag-squared(v0)");
    lines.add("---------------------------------------------");
    for(Speed speed : Speed.nonExtremeValues()) {
      History trip = new UniformCircularMotion(Axis.Z, radius, speed.β(), 0.0, 1.0);
      FourVector v0 = trip.fourVelocity(0.0);
      lines.add(trip.toString() + " " + v0 + " " + v0.magnitudeSq());
    }
    
    lines.add("----- v with time ------------------------");
    History trip = new UniformCircularMotion(Axis.Z, radius, 0.6, 0.0, 10.0);
    double tauIncrement = 0.1;
    double tau = 0.0;
    while (tau < 10.0) {
      FourVector v = trip.fourVelocity(tau);
      lines.add("τ:" + Util.round(tau, 5)  +" v:"+ v.toString());
      tau = tau + tauIncrement;
    }
    Util.writeToFile(CircularOrbit.class, "circular-orbit-"+radius + ".txt", lines);
  }

}
