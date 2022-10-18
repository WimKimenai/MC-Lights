package wkimenai.mc-lights.dj.choreography;

import org.bukkit.util.EulerAngle;
import wkimenai.mc-lights.utils.UtilMath;
import wkimenai.mc-lights.dj.Dj;
import wkimenai.mc-lights.dj.Choreography;

public class CrazyChoreography extends Choreography
{
    public CrazyChoreography(final Dj dj) {
        super(dj);
    }
    
    @Override
    public void updateChoreography() {
        this.dj.setRightArmPose(new EulerAngle(Math.toRadians(-(60 + UtilMath.randomRange(0, 40))), Math.toRadians(this.timer + 50 + UtilMath.randomRange(0, 40)), Math.toRadians(-5 + UtilMath.randomRange(0, 40))));
        this.dj.setLeftArmPose(new EulerAngle(Math.toRadians(-(this.timer + 90)), Math.toRadians(this.timer - 50), Math.toRadians(UtilMath.randomRange(0, 40))));
        this.dj.setHeadPose(new EulerAngle(Math.toRadians(this.timer), Math.toRadians(10 + UtilMath.randomRange(0, 40)), 0.0));
    }
}
