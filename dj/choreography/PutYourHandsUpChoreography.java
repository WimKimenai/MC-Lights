package wkimenai.mc-lights.dj.choreography;

import org.bukkit.util.EulerAngle;
import wkimenai.mc-lights.dj.Dj;
import wkimenai.mc-lights.dj.Choreography;

public class PutYourHandsUpChoreography extends Choreography
{
    public PutYourHandsUpChoreography(final Dj dj) {
        super(dj);
    }
    
    @Override
    public void updateChoreography() {
        this.dj.setRightArmPose(new EulerAngle(Math.toRadians(-60.0), Math.toRadians(this.timer + 50), -5.0));
        this.dj.setLeftArmPose(new EulerAngle(Math.toRadians(-(this.timer + 90)), Math.toRadians(this.timer - 50), 0.0));
        this.dj.setHeadPose(new EulerAngle(Math.toRadians(this.timer), Math.toRadians(10.0), 0.0));
    }
}
