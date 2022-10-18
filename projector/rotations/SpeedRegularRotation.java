package wkimenai.mc-lights.projector.rotations;

import org.bukkit.util.Vector;
import wkimenai.mc-lights.utils.UtilMath;
import wkimenai.mc-lights.projector.Projector;
import wkimenai.mc-lights.projector.ProjectorRotation;

public class SpeedRegularRotation extends ProjectorRotation
{
    private int angle;
    private int a;
    private int time;
    
    public SpeedRegularRotation(final Projector projector) {
        super(projector);
        this.angle = 12;
        this.a = 2;
        this.time = UtilMath.randomRange(0, 200);
    }
    
    @Override
    public void rotate() {
        super.rotate();
        this.direction = this.end.toVector().subtract(this.start.toVector()).normalize();
        this.setLazerLocation(this.end = this.end.add(new Vector(Math.cos(this.time * 3.141592653589793 / this.angle) * this.a, 0.0, Math.cos(this.time * 3.141592653589793 / this.angle) * this.a)));
        ++this.time;
        if (this.time >= 99999999) {
            this.time = 0;
        }
    }
}
