package wkimenai.mc-lights.projector.rotations;

import org.bukkit.util.Vector;
import wkimenai.mc-lights.utils.UtilMath;
import wkimenai.mc-lights.projector.Projector;
import wkimenai.mc-lights.projector.ProjectorRotation;

public class SlowRotation extends ProjectorRotation
{
    private int angle;
    private double a;
    private int time;
    
    public SlowRotation(final Projector projector) {
        super(projector);
        this.angle = 20;
        this.a = UtilMath.randomRange(0.2, 1.0);
        this.time = UtilMath.randomRange(0, 200);
    }
    
    @Override
    public void rotate() {
        super.rotate();
        this.end = this.end.add(new Vector(Math.cos(this.time * 7.141592653589793 / this.angle) * this.a, 0.0, Math.cos(this.time * 7.141592653589793 / this.angle) * this.a));
        this.direction = this.end.toVector().subtract(this.start.toVector());
        this.setLazerLocation(this.end);
        ++this.time;
        if (this.time >= 99999999) {
            this.time = 0;
        }
    }
}
