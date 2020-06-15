package sr.core;

import java.util.function.Function;

import sr.core.history.History;
import sr.core.transform.FourVector;

/** 
 Find the τ value in a {@link History} for which the event satisfies a given criterion.

 A very basic implementation, not super-robust!
 Assumes there's one root in the range τmin..τmax for the given {@link History}.
 Assumes that, initially, the signs of the data at the endpoints are different (at least 1 zero)
 The data is expected to be simple, monotonic, and with a single root.
*/
public final class EventFinder {

  /**
   Constructor.
   @param history the history for which this class is finding a special τ (and thus the corresponding event)
   @param criterion the function that returns 0.0 for the caller's target event
   @param epsilon the difference-level down to which this class pursues the target zero
  */
  public EventFinder(History history, Function<FourVector, Double> criterion, Double epsilon) {
    this.history = history;
    this.criterion = criterion;
    this.epsilon = epsilon;
  }
  
  /** 
   Return the τ for which the history satisfies the given criterion.
   Usually much faster than the bisection method.
   @param h used in finding the derivative of the criterion function passed to the constructor. 
  */
  public double searchWithNewtonsMethod(double h) {
    numIterations = 0;
    NewtonGuess guess = new NewtonGuess(history.τmin(), h);
    while (Math.abs(guess.fτ) > epsilon) {
      guess = guess.next();
      ++numIterations;
      if (numIterations > 1000) {
        break; //avoid getting into an infinite loop, in case of unexpected conditions
      }
    }
    return guess.τ;
  }
  
  /**
   Return the τ for which the history satisfies the given criterion. 
   Binary search. This method seems to converge much more slowly than the Newton-Raphson method. 
  */
  public double searchWithBisection() {
    numIterations = 0;
    Range range = new Range(history.τmin(), history.τmax());
    while (Math.abs(range.startVal) > epsilon) {
      range = range.next();
      ++numIterations;
      if (numIterations > 1000) {
        break; //avoid getting into an infinite loop, in case of unexpected conditions
      }
    }
    return range.midτ();
  }
  
  /** The number of iterations used in the implementation of the search. */
  public int numIterations() { 
    return numIterations; 
  }
  
  // PRIVATE
  
  private History history;
  private Function<FourVector, Double> criterion;
  private Double epsilon;
  private int numIterations;
  
  /** Newton-Raphson method. */
  private final class NewtonGuess {
    double τ;
    double fτ;
    double h;
    double derivfτ;
    NewtonGuess(double τ, double h){
      this.τ = τ;
      this.fτ = criterion.apply(history.event(τ));
      this.h = h;
      this.derivfτ = derivative();
    }
    NewtonGuess next() {
      double nextτ =  τ - (fτ/derivfτ);
      return new NewtonGuess(nextτ, h);
    }
    double derivative() {
      double fτplush = criterion.apply(history.event(τ+h));
      return (fτplush - fτ)/h;
    }
  }

  /** Bisection. The values at the ends of the range always need to be of opposite sign. */
  private final class Range {
    double startτ;
    double endτ;
    
    double startVal;
    double endVal;
    
    Range(double startτ, double endτ){
      this.startτ = startτ;
      this.endτ = endτ;
      this.startVal = criterion.apply(history.event(startτ));
      this.endVal = criterion.apply(history.event(endτ));
      if (!straddlesZero(startVal, endVal)) {
        throw new RuntimeException("Algorithm not able to find root. No change in sign between the endpoints of the initial range.");
      }
    }
    
    Range next() {
      double midVal = criterion.apply(history.event(midτ()));
      Range result = null;
      if (straddlesZero(startVal, midVal)) {
        result = new Range(startτ, midτ());
      }
      else if (straddlesZero(midVal, endVal)){
        result = new Range(midτ(), endτ);
      }
      return result;
    }
    
    boolean straddlesZero(double v1, double v2) {
      return sign(v1) != sign(v2);
    }
    int sign(double val) {
      return val < 0 ? -1 : +1;
    }
    double midτ() { 
      return (endτ + startτ)/2.0; 
    }
  }
}
