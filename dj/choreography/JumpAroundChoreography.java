package wkimenai.mc-lights.dj.choreography;

import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import wkimenai.mc-lights.listener.task.GlobalTime;
import wkimenai.mc-lights.dj.Dj;
import wkimenai.mc-lights.dj.Choreography;

public class JumpAroundChoreography extends Choreography
{
    public JumpAroundChoreography(final Dj dj) {
        super(dj);
    }
    
    @Override
    public void updateChoreography() {
        if (GlobalTime.getTime() % 2 == 0) {
            this.dj.setVelocity(new Vector(0.0, 0.1, 0.0));
        }
        this.dj.setLeftArmPose(new EulerAngle(Math.toRadians(-60.0), Math.toRadians(-this.timer), 0.0));
        this.dj.setRightArmPose(new EulerAngle(Math.toRadians(-(this.timer + 140)), 0.0, 0.0));
        this.dj.setHeadPose(new EulerAngle(Math.toRadians(this.timer), 0.0, Math.toRadians(10.0)));
    }
}
