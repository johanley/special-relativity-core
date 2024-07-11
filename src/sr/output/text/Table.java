package sr.output.text;

import java.util.Formatter;

/** Output text in a tabular format. */
public final class Table {

  /**
   Constructor.
   @param columFormats as defined by the {@link Formatter} class. 
  */
  public Table(String... columnFormats) {
    this.columnFormats = columnFormats;
  }

  /** Return a formatted row of items in the table. */
  public String row(Object... values) {
    StringBuilder result = new StringBuilder();
    for(int idx=0; idx < columnFormats.length; ++idx) {
      result.append(column(idx, values[idx]));
    }
    return result.toString();
  }
  
  private String[] columnFormats;
  
  private String column(int idx, Object thing) {
    return String.format(columnFormats[idx], thing);
  }
}
