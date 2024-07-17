package sr.core;

import java.util.function.Function;

import sr.core.particlehistory.ParticleHistory;
import sr.core.transform.FourVector;

/** 
 Find the cτ value along a {@link ParticleHistory} for which the corresponding event satisfies a given criterion.

 This implementation is basic; it's not super-robust! 
 The data is expected to be simple, monotonic, and with a single root.
 This implementation uses the Newton-Raphson method.
*/
public final class FindEvent {

  /**
   Constructor.
   @param history the history for which this class is finding a special cτ (and thus the corresponding event).
   @param criterion the function that returns 0.0 for the caller's target event.
   @param epsilon the difference-level down to which this class pursues the target-zero; a small positive number.
  */
  public FindEvent(ParticleHistory history, Function<FourVector, Double> criterion, Double epsilon) {
   Util.mustHave(epsilon > 0, "The epsilon interval must be positive.");
   this.history = history;
   this.criterion = criterion;
   this.epsilon = epsilon;
  }

  /** A small value used to approximate the values of derivatives. */
  public static final Double SMALL_H = 0.00001;
  
  /**  Execute the {@link #search(double, double)} using 0.0 as the starting guess, and {@link #SMALL_H} as the h-value.  */
  public double search() {
    return search(0.0, SMALL_H);
  }
  
  /**  Execute the {@link #search(double, double)} using {@link #SMALL_H} as the h-value.  */
  public double search(double cτ) {
    return search(cτ, SMALL_H);
  }
  
  /** 
   Return the cτ for which the history satisfies the given criterion.
   @param cτ an initial guess for the return value.
   @param h small-enough interval used to closely approximate the derivative of the criterion function passed to the constructor. Positive. 
  */
  public double search(double cτ, double h) {
    Util.mustHave(h>0, "The h-interval must be positive.");
    
    numIterations = 0;
    NewtonGuess guess = new NewtonGuess(cτ, h);
    while (Math.abs(guess.fτ) > epsilon) {
      guess = guess.again();
      ++numIterations;
      if (numIterations > 1000) {
        break; //avoid getting into an infinite loop, in case of unexpected conditions
      }
    }
    return guess.cτ;
  }
  
  private ParticleHistory history;
  private Function<FourVector, Double> criterion;
  private Double epsilon;
  private int numIterations;
  
  /** Newton-Raphson method. */
  private final class NewtonGuess {
    double cτ;
    double fτ;
    double derivfτ;
    double h;
    NewtonGuess(double cτ, double h){
      this.cτ = cτ;
      this.fτ = criterion.apply(history.event(cτ));
      this.h = h;
      this.derivfτ = derivative();
    }
    /** Returns a new object. */
    NewtonGuess again() {
      double nextτ =  cτ - (fτ/derivfτ);
      return new NewtonGuess(nextτ, h);
    }
    double derivative() {
      double fτplush = criterion.apply(history.event(cτ+h));
      return (fτplush - fτ)/h;
    }
  }
}