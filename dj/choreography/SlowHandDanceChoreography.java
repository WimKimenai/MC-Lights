package wkimenai.mc-lights.dj.choreography;

import org.bukkit.util.EulerAngle;
import wkimenai.mc-lights.dj.Dj;
import wkimenai.mc-lights.dj.Choreography;

public class SlowHandDanceChoreography extends Choreography
{
    public SlowHandDanceChoreography(final Dj dj) {
        super(dj);
    }
    
    @Override
    public void updateChoreography() {
        this.dj.setHeadPose(new EulerAngle(Math.toRadians(this.timer + 10), 0.0, 0.0));
        this.dj.setRightArmPose(new EulerAngle(Math.toRadians(-90.0), Math.toRadians(this.timer + 20), 0.0));
        this.dj.setLeftArmPose(new EulerAngle(Math.toRadians(-90.0), Math.toRadians(this.timer - 20), 0.0));
    }
}
