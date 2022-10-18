package wkimenai.mc-lights.projector.rotations;

import org.bukkit.util.Vector;
import wkimenai.mc-lights.utils.UtilMath;
import wkimenai.mc-lights.listener.task.GlobalTime;
import wkimenai.mc-lights.projector.Projector;
import wkimenai.mc-lights.projector.ProjectorRotation;

public class RandomRotation extends ProjectorRotation
{
    public RandomRotation(final Projector projector) {
        super(projector);
    }
    
    @Override
    public void rotate() {
        super.rotate();
        if (GlobalTime.getTime() % 10 == 0) {
            final Vector vector = new Vector(UtilMath.randomRange(-20, 20), UtilMath.randomRange(-20, 20), UtilMath.randomRange(-20, 20));
            this.setLazerLocation(this.end.clone().add(vector));
            this.direction = this.end.clone().add(vector).toVector().subtract(this.start.toVector()).normalize();
        }
    }
}
