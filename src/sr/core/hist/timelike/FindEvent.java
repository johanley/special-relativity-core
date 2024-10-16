package sr.core.hist.timelike;

import java.util.function.Function;

import sr.core.Util;
import sr.core.component.Event;

/** 
 Find the λ value (usually a ct-coordinate) along a {@link TimelikeHistory} for which the corresponding event satisfies a given criterion.

 <P> This implementation is basic; it's not super-robust!
 It's highly recommended that you:
 <ul>
  <li>make an effort to provide a good first-guess to the search method. 
  This can usually be done by drawing a simple sketch of the scenario.
  <li>inspect the number of iterations needed to find the returned result.
 </ul> 
  
 <P>The data is expected to be simple, monotonic, and with a single root.
 <P>This implementation uses the Newton-Raphson method.
*/
public final class FindEvent {

  /**
   Constructor.
   @param history the history for which this class is finding a special λ (and thus the corresponding event).
   @param criterion the function that returns 0.0 for the caller's target event.
   @param epsilon the difference-level down to which this class pursues the target-zero; a small positive number.
  */
  public FindEvent(TimelikeHistory history, Function<Event, Double> criterion, Double epsilon) {
   Util.mustHave(epsilon > 0, "The epsilon interval must be positive.");
   this.history = history;
   this.criterion = criterion;
   this.epsilon = epsilon;
  }
  
  /** Call {@link #FindEvent(TimelikeHistory, Function, Double)} with epsilon equal to {@link #EPSILON}. */
  public FindEvent(TimelikeHistory history, Function<Event, Double> criterion) {
    this(history, criterion, EPSILON);
   }

  /** A small value used to approximate the values of derivatives: {@value}*/
  public static final double SMALL_H = 0.00001;
  
  /** 
   A small value used to as the default 'difference-level' below which we consider the event as having 
   been found (the root is 'zero enough'). Value: {@value}.
   This can be overridden by calling a constructor.
   This value is not the same as that used by the {@link sr.core.Epsilon} class. 
  */
  public static final double EPSILON = 0.00001;
  
  /**  Execute the {@link #search(double, double)} using {@link #SMALL_H} as the h-value.  */
  public double search(double λ) {
    return search(λ, SMALL_H);
  }
  
  /** 
   Return the λ for which the history satisfies the given criterion.
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
    return guess.λ;
  }
  
  /** The number of loops used by a search method to find its answer. */
  public int numIterationsUsedBySearch() {
    return numIterations;
  }

  // PRIVATE
  
  private TimelikeHistory history;
  private Function<Event, Double> criterion;
  private Double epsilon;
  private int numIterations;
  
  /** Newton-Raphson method. */
  private final class NewtonGuess {
    double λ;
    double fλ;
    double derivfτ;
    double h;
    NewtonGuess(double λ, double h){
      this.λ = λ;
      this.fλ = criterion.apply(history.event(λ));
      this.h = h;
      this.derivfτ = derivative();
    }
    /** Returns a new object. */
    NewtonGuess again() {
      double nextλ =  λ - (fλ/derivfτ);
      return new NewtonGuess(nextλ, h);
    }
    double derivative() {
      double fλ_plus_h = criterion.apply(history.event(λ + h));
      return (fλ_plus_h - fλ)/h;
    }
  }
}