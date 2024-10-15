package sr.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 
 A range of speeds β expressed as a fraction of the speed limit, intended for 
 testing a spectrum of values. 
*/
public enum SpeedValues {

  PERCENT_10("0.10"),
  PERCENT_20("0.20"),
  PERCENT_30("0.30"),
  PERCENT_40("0.40"),
  PERCENT_50("0.50"),
  PERCENT_60("0.60"),
  PERCENT_70("0.70"),
  PERCENT_80("0.80"),
  PERCENT_90("0.90"),
  PERCENT_95("0.95"),
  PERCENT_96("0.96"),
  PERCENT_97("0.97"),
  PERCENT_98("0.98"),
  PERCENT_99("0.99"),
  PERCENT_99_dot_9("0.999"),
  PERCENT_99_dot_99("0.9999"),
  
  /**
   The maximum speed of a proton in the Large Hadron Collider. 
   (This speed changes over the years, after upgrades to the facility.)
   Energy of 6.5*10^12 eV.
   WARNING: this value is very close to 1, and Map implementations can coerce it to 1. 
  */
  PROTON_LHC("0.9999999895816072"),
  
  /** 
   Speed of a proton having an energy of 10^20 eV, as seen in the most extreme cosmic rays. 
   WARNING: this speed is very close to 1, and {@link Double} cannot be used to represent it.  
   Use {@link BigDecimal} instead!
  */
  PROTON_EXTREME_COSMIC_RAY("0.99999999999999999999995598229");
  
  public BigDecimal βBigDecimal() { return β; }
  
  /** WARNING: when very close to 1, a double has insufficient precision to represent this value. */
  public Double β() { return β.doubleValue(); }
  public Double Γ() { return Γ.doubleValue(); }
  
  /** 
   All items except for the one for the extreme cosmic ray.
   Most cases will desire this method. 
  */
  public static List<SpeedValues> nonExtremeValues(){
    List<SpeedValues> result = Arrays.asList(values());
    List<SpeedValues> listRemovable = new ArrayList<>(result);
    listRemovable.remove(SpeedValues.PROTON_EXTREME_COSMIC_RAY);
    return listRemovable;
  }

  /** List of speeds up to 0.9999. */
  public static List<SpeedValues> upToFourNines(){
    List<SpeedValues> result = Arrays.asList(values());
    List<SpeedValues> listRemovable = new ArrayList<>(result);
    listRemovable.remove(SpeedValues.PROTON_EXTREME_COSMIC_RAY);
    listRemovable.remove(SpeedValues.PROTON_LHC);
    return listRemovable;
  }

  //PRIVATE 
  
  private SpeedValues(String val) {
    this.β = new BigDecimal(val, Physics.LARGE_NUM_DECIMALS);
    this.Γ = Physics.Γ(β);
  }
  
  private BigDecimal β;
  private BigDecimal Γ;
}
