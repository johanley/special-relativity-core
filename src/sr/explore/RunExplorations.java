package sr.explore;

import java.util.ArrayList;
import java.util.List;

import sr.explore.accel.speed.ConnectedRockets;
import sr.explore.accel.speed.OneGeeForever;
import sr.explore.accel.speed.OneGeeThereAndBack;
import sr.explore.accel.speed.OneGeeThereAndBackWithCruise;
import sr.explore.accel.speed.OneGeeThereAndStay;
import sr.explore.clocks.LightClock;
import sr.explore.clocks.MakeAClockRunFaster;
import sr.explore.clocks.TravelTime;
import sr.explore.clocks.Twins;
import sr.explore.elbow.boost.CornerBoostsDontCommute;
import sr.explore.elbow.boost.EquivalentBoostPlusRotation;
import sr.explore.elbow.boost.silbersteinrotation.SilbersteinRotation;
import sr.explore.elbow.boost.silbersteinrotation.SilbersteinRotationRange;
import sr.explore.geom.flattening.StickFlattening;
import sr.explore.interval.invariant.InvariantInterval;
import sr.explore.noncolinear.thomas.rotation.ThomasRotation;
import sr.explore.noncolinear.velocitytransform.BoostToRotateVelocity;
import sr.explore.noncolinear.velocitytransform.Commutation;
import sr.explore.noncolinear.velocitytransform.CompareFormulaWithLT;
import sr.explore.noncolinear.velocitytransform.MaxAngleBetweenResultVectors;
import sr.explore.noncolinear.velocitytransform.NeverExceedsSpeedLimit;
import sr.explore.noncolinear.velocitytransform.SignReversal;
import sr.explore.noncolinear.velocitytransform.ThomasPrecessionAsLimit;
import sr.explore.optics.doppler.cone.DopplerCone;
import sr.explore.optics.doppler.cone.DopplerConeElbowBoost;
import sr.explore.optics.flyby.RelativisticFlyBy;
import sr.explore.optics.kvector.WaveVectorK;
import sr.explore.optics.lightsliceofastick.LightSliceOfAStick;
import sr.explore.optics.mirror.MovingMirror;
import sr.explore.speeds.SpeedsAndGammas;

/** Run a number of explorations in sequence. */
public final class RunExplorations {
  
  public static void main(String[] args) {
    for(Exploration exploration : explorations()) {
      exploration.explore();
    }
  }
  
  private static List<Exploration> explorations(){
    List<Exploration> result = new ArrayList<>();
    result.add(new ConnectedRockets());
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
    result.add(new SilbersteinRotation());
    result.add(new SilbersteinRotationRange());
    result.add(new ThomasRotation());
    result.add(new BoostToRotateVelocity());
    result.add(new Commutation());
    result.add(new CompareFormulaWithLT());
    result.add(new MaxAngleBetweenResultVectors());
    result.add(new NeverExceedsSpeedLimit());
    result.add(new SignReversal());
    result.add(new ThomasPrecessionAsLimit());
    result.add(new DopplerCone());
    result.add(new DopplerConeElbowBoost());
    result.add(new WaveVectorK());
    result.add(new LightSliceOfAStick());
    result.add(new MovingMirror());
    result.add(new SpeedsAndGammas());
    result.add(new CornerBoostsDontCommute());
    result.add(new EquivalentBoostPlusRotation());
    result.add(new RelativisticFlyBy());
    return result;
  }

}
