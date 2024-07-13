package sr.explore.flyby;

import java.util.ArrayList;
import java.util.List;

/** 
 Echo every {@link DetectionEvent} to the console.
*/
final class OutputToConsole implements OutputSummary {
  
  @Override public void accept(DetectionEvent d) {
    detections.add(d);
  }

  @Override public void render() {
    for (DetectionEvent d : detections) {
      System.out.println(d);
    }
  };
  
  private List<DetectionEvent> detections = new ArrayList<>();
}
