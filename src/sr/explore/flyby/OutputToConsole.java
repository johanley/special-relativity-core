package sr.explore.flyby;

import java.util.function.Consumer;

/** Simply echo each {@link Detection} to the console. */
final class OutputToConsole implements Consumer<Detection>, OutputFlyBy {
  
  @Override public void accept(Detection d) {
    System.out.println(d);
  }

  /** No-op. */
  public void render() {}; 


}
