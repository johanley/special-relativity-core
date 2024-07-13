package sr.explore.flyby;

import static sr.core.Util.radsToDegs;

import sr.core.Physics;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/** 
 Records significant {@link DetectionEvent}s, and output to the console and to a file.
 
 Record these items:
 <ul>
  <li>the first detection 
  <li>the detection with maximum visual magnitude V 
  <li>the last visible detection (visible to the human eye, that is)
  <li>the last detection
 </ul>
*/
final class OutputHighlights extends TextOutput implements OutputSummary {

  OutputHighlights(RelativisticFlyBy flyby){
    this.flyBy = flyby;
  }

  @Override public void accept(DetectionEvent d) {
    if (first == null) {
      first = d; //only happens the first time in
    }
    
    if (maxBrightness == null) {
      maxBrightness = d;
    }
    else {
      if (d.V < maxBrightness.V) {
        maxBrightness = d;
      }
    }
    
    last = d; //will point to the last one, in the end
    
    if (lastVisible == null && d.V < Physics.LIMITING_MAG_HUMAN_EYE) {
      lastVisible = d; //first one to be visible
    }
    else if (lastVisible != null && d.V <= Physics.LIMITING_MAG_HUMAN_EYE) {
      //assumes that it passes out of visibility only once!
      //that's not always the case!
      lastVisible = d;
    }
  }
  
  DetectionEvent first() { return first; }
  DetectionEvent maxBrightness() { return maxBrightness; }
  DetectionEvent lastVisible() { return lastVisible; }
  DetectionEvent last() { return last; }
  
  /** Output the final results.  */
  @Override public void render() {
    lines.add("Relativistic fly-by of a star.");
    lines.add("Spectral Type: " + flyBy.star().name());
    lines.add("Speed β:" + flyBy.β()); 
    lines.add("Minimum distance: " + flyBy.minimumDistance() + " light-years");
    lines.add("");
    lines.add(tableHeader.row("Description", "Detection", "θ", "Doppler", "Visual", "Distance to emission event"));
    lines.add(tableHeader.row("", "Time (year)", "°", "", "Mag", "Light-years"));
    lines.add(dashes(65));
    addTableRow("First event", first);
    addTableRow("Max brightness", maxBrightness);
    addTableRow("Last visible", lastVisible);
    addTableRow("Last event", last);
    outputToConsoleAnd(fileName());
  }
  
  @Override public String toString() {
    String sep = System.getProperty("line.separator");
    return first + sep + maxBrightness + sep + lastVisible + sep + last;
  }

  // PRIVATE
  
  private DetectionEvent first;
  private DetectionEvent last;
  private DetectionEvent maxBrightness;
  private DetectionEvent lastVisible;
  
  private RelativisticFlyBy flyBy;
  // description, detectionTime, radsToDegs(θ), D, V, distanceToEmissionEvent);
  private Table table = new Table("%-16s", "%8.3fy", "%8.2f°", "%8.2f", "%8.2fV", "%8.3fly");
  private Table tableHeader = new Table("%-16s", "%-16s", "%-4s", "%-10s", "%-8s", "%-16s");


  private String fileName() {
    String s = "-";
    return "flyby" +s+ flyBy.star().name() +s+ flyBy.β() +s+ flyBy.minimumDistance() + ".txt"; 
  }
  
  private void addTableRow(String description, DetectionEvent ev) {
    lines.add(table.row(description, ev.detectionTime, radsToDegs(ev.θ), ev.D, ev.V, ev.distanceToEmissionEvent));
  }
};

