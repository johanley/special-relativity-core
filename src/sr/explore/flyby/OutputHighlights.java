package sr.explore.flyby;

import static sr.core.Util.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import sr.core.Physics;
import sr.core.Util;

/** 
 Records significant detections. See {@link Detection}.
 
 Record these items:
 <ul>
  <li>the first detection 
  <li>the one with maximum brightness V 
  <li>the last visible detection (visible to the human eye, that is)
  <li>the last detection
 </ul>
 
 <P>Optionally saves results to a file. 
*/
final class OutputHighlights implements OutputFlyBy {

  /** If you use this ctor, then no output to a file will occur. */
  OutputHighlights(RelativisticFlyBy flyby){
    this.flyBy = flyby;
  }

  /** @param dir where to write the output file. The name of the file is controlled by this class. */
  OutputHighlights(RelativisticFlyBy flyby, String dir){
    this.flyBy = flyby;
    this.dir = dir;
  }
  
  @Override public void accept(Detection d) {
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
    
    if (lastVisible == null && d.V < LIMITING_MAG_HUMAN_EYE) {
      lastVisible = d; //first one to be visible
    }
    else if (lastVisible != null && d.V <= LIMITING_MAG_HUMAN_EYE) {
      //assumes that it passes out of visibility once only!
      lastVisible = d;
    }
  }
  
  Detection first() { return first; }
  Detection maxBrightness() { return maxBrightness; }
  Detection lastVisible() { return lastVisible; }
  Detection last() { return last; }
  
  /**
    Output the final results. 
    Write to the console. Write to a file, if a dir was provided. 
  */
  @Override public void render() {
    System.out.println(this.toString());
    
    if (dir != null) {
      Path path = Paths.get(dir, fileName());
      try {
        Files.write(path, lines(), ENCODING);
      } 
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  @Override public String toString() {
    String sep = System.getProperty("line.separator");
    return first + sep + maxBrightness + sep + lastVisible + sep + last;
  }

  // PRIVATE
  
  private Detection first;
  private Detection last;
  private Detection maxBrightness;
  private Detection lastVisible;
  private RelativisticFlyBy flyBy;
  private String dir;

  private String fileName() {
    String s = "-";
    return "flyby" +s+ flyBy.star().name() +s+ flyBy.β() +s+ flyBy.minimumDistance() + ".txt"; 
  }
  
  private List<String> lines(){
    String comment = "# ";
    String sep  = "---------------------------------------";
    String[] res = {
      comment + "Relativistic fly-by of a star",
      comment + flyBy.star().name() + " spectral type",
      comment + flyBy.β() + "c speed " +  Util.round(Physics.Γ(flyBy.β()), 2) + " gamma", 
      comment + flyBy.minimumDistance() + " ly minimum distance", 
      comment, 
      comment + "detection-time(years), theta (degs), D, V",
      comment + sep,
      first.toString() + comment + " first computed", 
      maxBrightness.toString() + comment + " max brightness",
      lastVisible.toString() + comment + " last visible",
      last.toString() + comment + " last computed"
    };
    return Arrays.asList(res);
  }
};

