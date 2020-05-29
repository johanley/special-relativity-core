package sr.explore.flyby;

/**
 Policies for handling the computed data for a fly-by. 
*/
public interface OutputFlyBy {

  /** Process each data point. */
  void accept(Detection d);

  /** Do something with the data at the end. */
  void render();
  
}
