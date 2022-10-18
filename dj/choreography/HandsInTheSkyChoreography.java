package wkimenai.mc-lights.dj.choreography;

import org.bukkit.util.EulerAngle;
import wkimenai.mc-lights.dj.Dj;
import wkimenai.mc-lights.dj.Choreography;

public class HandsInTheSkyChoreography extends Choreography
{
    public HandsInTheSkyChoreography(final Dj dj) {
        super(dj);
    }
    
    @Override
    public void updateChoreography() {
        this.dj.setRightArmPose(new EulerAngle(Math.toRadians(-90.0), Math.toRadians(this.timer + 50), Math.toRadians(this.timer + 50)));
        this.dj.setLeftArmPose(new EulerAngle(Math.toRadians(-90 + this.timer), Math.toRadians(this.timer + 50), Math.toRadians(this.timer + 120)));
        this.dj.setHeadPose(new EulerAngle(Math.toRadians(this.timer), 0.0, Math.toRadians(10.0)));
    }
}
