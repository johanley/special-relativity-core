package sr.output.text;

import java.util.ArrayList;
import java.util.List;

import sr.core.Util;

/**
 Output lines of text to the console and a file.
 If the output is tabular, then {@link Table} should be used to generate the lines for table rows.
 
 <P>This class can be either subclassed, or used as a field (only slightly more work).
*/
public class TextOutput {

  /** Add a line to text output. */
  public void add(String text) {
    lines.add(text);
  }
  
  /** Call the object's toString method and then add it as a line to text output. */
  public void add(Object thing) {
    lines.add(thing.toString());
  }
  
  /** Add a line to text output, preceded by '# '. */
  public void addComment(String text) {
    lines.add("# " + text);
  }

  /** 
   Display the text output both to the console and to a text file. 
   The output file is in the same directory as the calling class.
   When finished, the lines are abandoned, and an new list of lines is created (with a single empty line).
  */
  public void outputTo(String fileName, Object caller) {
    output(fileName, caller);
  }
  
  /** A line of dashes, used as a separator. */
  public String dashes(int num) {
    return Util.separator(num);
  }
  
  public void outputToConsole() {
    for(String line : lines) {
      System.out.println(line);
    }
  }
  
  /** Lines of text output. */
  protected List<String> lines = new ArrayList<>();
  
  /** 
   Display the text output both to the console and to a text file. 
   The output file is in the same directory as the calling class.
   When finished, the lines are abandoned, and an new list of lines is created (with a single empty line).
  */
  protected void outputToConsoleAnd(String fileName) {
    //the 'this' reference is not this class, but the class of the subclass
    output(fileName, this);
  }
  
  private void output(String fileName, Object caller) {
    outputToConsole();
    Util.writeToFile(caller.getClass(), fileName, lines);
    lines = new ArrayList<>();
    lines.add(Util.NL);
  }

}
