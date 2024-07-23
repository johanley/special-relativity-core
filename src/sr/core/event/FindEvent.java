package sr.core.event;

import java.util.function.Function;

import sr.core.Util;
import sr.core.history.History;

/** 
 Find the λ value (usually a ct-coordinate) along a {@link History} for which the corresponding event satisfies a given criterion.

 This implementation is basic; it's not super-robust!
 It's highly recommended that you:
 <ul>
  <li>make an effort to provide a good first-guess to the search method. This can be done by drawing a simple rough diagram of the scenario.
  <li>inspect the number of iterations needed to find the returned result.
 </ul> 
  
 The data is expected to be simple, monotonic, and with a single root.
 This implementation uses the Newton-Raphson method.
*/
public final class FindEvent {

  /**
   Constructor.
   @param history the history for which this class is finding a special λ (and thus the corresponding event).
   @param criterion the function that returns 0.0 for the caller's target event.
   @param epsilon the difference-level down to which this class pursues the target-zero; a small positive number.
  */
  public FindEvent(History history, Function<Event, Double> criterion, Double epsilon) {
   Util.mustHave(epsilon > 0, "The epsilon interval must be positive.");
   this.history = history;
   this.criterion = criterion;
   this.epsilon = epsilon;
  }
  
  /** Call {@link #FindEvent(History, Function, Double)} with epsilon equal to {@link #EPSILON}. */
  public FindEvent(History history, Function<Event, Double> criterion) {
    this(history, criterion, EPSILON);
   }

  /** A small value used to approximate the values of derivatives: {@value}*/
  public static final double SMALL_H = 0.00001;
  
  /** 
   A small value used to as the default 'difference-level' below which we consider the event as having 
   been found (the root is 'zero enough'). Value: {@value}.
   This can be overridden by calling a constructor.
   This value is not the same as that used by the {@link Epsilon} class. 
  */
  public static final double EPSILON = 0.00001;
  
  /**  Execute the {@link #search(double, double)} using {@link #SMALL_H} as the h-value.  */
  public double search(double cτ) {
    return search(cτ, SMALL_H);
  }
  
  /** 
   Return the cτ for which the history satisfies the given criterion.
   @param λ an initial guess for the return value.
   @param h small-enough interval used to closely approximate the derivative of the criterion function passed to the constructor. Positive. 
  */
  public double search(double λ, double h) {
    Util.mustHave(h>0, "The h-interval must be positive.");
    
    numIterations = 0;
    NewtonGuess guess = new NewtonGuess(λ, h);
    while (Math.abs(guess.fλ) > epsilon) {
      guess = guess.again();
      ++numIterations;
      if (numIterations > 1000) {
        break; //avoid getting into an infinite loop, in case of unexpected conditions
      }
    }
    if (numIterations < 1) {
      //throw new RuntimeException("Probable search error. The number of iterations is under 1.");
    }
    return guess.cλ;
  }
  
  /** The number of loops used by a search method to find its answer. */
  public int numIterationsUsedBySearch() {
    return numIterations;
  }

  // PRIVATE
  
  private History history;
  private Function<Event, Double> criterion;
  private Double epsilon;
  private int numIterations;
  
  /** Newton-Raphson method. */
  private final class NewtonGuess {
    double cλ;
    double fλ;
    double derivfτ;
    double h;
    NewtonGuess(double λ, double h){
      this.cλ = λ;
      this.fλ = criterion.apply(history.event(λ));
      this.h = h;
      this.derivfτ = derivative();
    }
    /** Returns a new object. */
    NewtonGuess again() {
      double nextλ =  cλ - (fλ/derivfτ);
      return new NewtonGuess(nextλ, h);
    }
    double derivative() {
      double fλ_plus_h = criterion.apply(history.event(cλ + h));
      return (fλ_plus_h - fλ)/h;
    }
  }
}