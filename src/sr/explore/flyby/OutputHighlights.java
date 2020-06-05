package sr.explore.flyby;

import java.util.Arrays;
import java.util.List;

import sr.core.Physics;
import sr.core.Util;

/** 
 Records significant {@link Detection}s.
 
 Record these items:
 <ul>
  <li>the first detection 
  <li>the detection with maximum visual magnitude V 
  <li>the last visible detection (visible to the human eye, that is)
  <li>the last detection
 </ul>
 
 <P>Saves results to a file. 
*/
final class OutputHighlights implements OutputFlyBy {

  OutputHighlights(RelativisticFlyBy flyby){
    this.flyBy = flyby;
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
    
    if (lastVisible == null && d.V < Physics.LIMITING_MAG_HUMAN_EYE) {
      lastVisible = d; //first one to be visible
    }
    else if (lastVisible != null && d.V <= Physics.LIMITING_MAG_HUMAN_EYE) {
      //assumes that it passes out of visibility only once!
      lastVisible = d;
    }
  }
  
  Detection first() { return first; }
  Detection maxBrightness() { return maxBrightness; }
  Detection lastVisible() { return lastVisible; }
  Detection last() { return last; }
  
  /**
   Output the final results. 
   Write to the console. 
   Write to a file, by default located next to this class. See {@link Util#writeToFile(Class, String, List)}. 
  */
  @Override public void render() {
    System.out.println(this.toString());
    Util.writeToFile(this.getClass(), fileName(), lines());
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

