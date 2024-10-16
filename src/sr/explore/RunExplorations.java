package sr.explore;

import java.util.ArrayList;
import java.util.List;

import sr.explore.accel.circular.motion.KinematicSpinAsLimit;
import sr.explore.accel.circular.motion.OneRevolution;
import sr.explore.accel.elbow.boost.CornerBoostsDontCommute;
import sr.explore.accel.elbow.boost.EquivalentBoostPlusRotation;
import sr.explore.accel.elbow.boost.kinematic.rotation.KinematicRotationFromSimpleHistories;
import sr.explore.accel.elbow.boost.kinematic.rotation.KinematicRotationRange;
import sr.explore.clocks.LightClock;
import sr.explore.clocks.MakeAClockRunFaster;
import sr.explore.clocks.TravelTime;
import sr.explore.clocks.Twins;
import sr.explore.geom.flattening.StickFlattening;
import sr.explore.hyperboloid.FourVelocityUnitHyperboloid;
import sr.explore.interval.invariant.InvariantInterval;
import sr.explore.optics.doppler.cone.DopplerCone;
import sr.explore.optics.doppler.cone.DopplerConeElbowBoost;
import sr.explore.optics.flyby.RelativisticFlyBy;
import sr.explore.optics.kvector.WaveVectorK;
import sr.explore.optics.lightsliceofastick.LightSliceOfAStick;
import sr.explore.optics.mirror.MovingMirror;
import sr.explore.optics.telescope.BoostedTelescope;
import sr.explore.speeds.SpeedsAndGammas;
import sr.explore.velocity.onegee.OneGeeConnectedRockets;
import sr.explore.velocity.onegee.OneGeeForever;
import sr.explore.velocity.onegee.OneGeeThereAndBack;
import sr.explore.velocity.onegee.OneGeeThereAndBackWithCruise;
import sr.explore.velocity.onegee.OneGeeThereAndStay;
import sr.explore.velocity.transform.BoostToRotateVelocity;
import sr.explore.velocity.transform.Commutation;
import sr.explore.velocity.transform.CompareFormulaWithLT;
import sr.explore.velocity.transform.MaxAngleBetweenResultVectors;
import sr.explore.velocity.transform.NeverExceedsSpeedLimit;
import sr.explore.velocity.transform.SignReversal;
import sr.explore.waves.InvariantPhaseDifference;
import sr.explore.waves.WavesInMedia;

/** 
 Run all explorations in sequence.
 
 <P>This class helps finding errors. 
 Running all explorations after code changes have been made, and then checking 
 for differences in the output, is an effective means of testing.  
*/
public final class RunExplorations {
  
  public static void main(String[] args) {
    List<Exploration> list = explorations();
    for(Exploration exploration : list) {
      exploration.explore();
    }
  }

  private static List<Exploration> explorations(){
    List<Exploration> result = new ArrayList<>();
    result.add(new OneGeeConnectedRockets());
    result.add(new OneGeeForever());
    result.add(new OneGeeThereAndBack());
    result.add(new OneGeeThereAndBackWithCruise());
    result.add(new OneGeeThereAndStay());
    result.add(new LightClock());
    result.add(new MakeAClockRunFaster());
    result.add(new TravelTime());
    result.add(new Twins());
    result.add(new StickFlattening());
    result.add(new InvariantInterval());
    result.add(new KinematicRotationFromSimpleHistories());
    result.add(new KinematicRotationRange());
    result.add(new OneRevolution());
    result.add(new BoostToRotateVelocity());
    result.add(new Commutation());
    result.add(new CompareFormulaWithLT());
    result.add(new MaxAngleBetweenResultVectors());
    result.add(new NeverExceedsSpeedLimit());
    result.add(new SignReversal());
    result.add(new KinematicSpinAsLimit());
    result.add(new DopplerCone());
    result.add(new DopplerConeElbowBoost());
    result.add(new WaveVectorK());
    result.add(new LightSliceOfAStick());
    result.add(new MovingMirror());
    result.add(new SpeedsAndGammas());
    result.add(new CornerBoostsDontCommute());
    result.add(new EquivalentBoostPlusRotation());
    result.add(new RelativisticFlyBy());
    result.add(new BoostedTelescope());
    result.add(new WavesInMedia());
    result.add(new InvariantPhaseDifference());
    result.add(new FourVelocityUnitHyperboloid());
    return result;
  }
}