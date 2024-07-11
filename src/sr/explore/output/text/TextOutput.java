package sr.explore.output.text;

import java.util.ArrayList;
import java.util.List;

import sr.core.Util;

/**
 Lines of textual output.
 If the output is tabular, then {@link Table} should be used to generate the lines for table rows. 
 Intended only to be subclassed! 
*/
public class TextOutput {

  /** Lines of text output. */
  protected List<String> lines = new ArrayList<>();
  
  /** 
   Display the text output both to the console and to a text file. 
   The output file is in the same directory as the calling class.
   When finished, the lines are abandoned, and an new list of lines is created (with a single empty line).
  */
  protected void outputLines(String fileName) {
    for(String line : lines) {
      System.out.println(line);
    }
    //the 'this' reference is not this class, but the class of the caller
    //(it only works when this class is subclassed!)
    Util.writeToFile(this.getClass(), fileName, lines);
    lines = new ArrayList<>();
    lines.add(Util.NL);
  }

}
