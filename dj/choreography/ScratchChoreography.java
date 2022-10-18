package wkimenai.mc-lights.dj.choreography;

import org.bukkit.util.EulerAngle;
import wkimenai.mc-lights.dj.Dj;
import wkimenai.mc-lights.dj.Choreography;

public class ScratchChoreography extends Choreography
{
    public ScratchChoreography(final Dj dj) {
        super(dj);
        dj.getDj().setLeftArmPose(new EulerAngle(Math.toRadians(-60.0), 0.0, Math.toRadians(-30.0)));
    }
    
    @Override
    public void updateChoreography() {
        this.dj.setRightArmPose(new EulerAngle(Math.toRadians(-60.0), Math.toRadians(-this.timer), 0.0));
        this.dj.setHeadPose(new EulerAngle(Math.toRadians(this.timer), 0.0, Math.toRadians(10.0)));
    }
}
