package wkimenai.mc-lights.projector;

import org.bukkit.util.EulerAngle;
import wkimenai.mc-lights.projector.projectors.EndProjector;
import wkimenai.mc-lights.projector.projectors.LazerProjector;
import wkimenai.mc-lights.nms.others.EnderLazer;
import wkimenai.mc-lights.nms.others.Lazer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;
import org.bukkit.Location;

public abstract class ProjectorRotation
{
    protected Location end;
    protected Vector direction;
    private ArmorStand projector;
    protected Location start;
    protected Lazer lazer;
    protected EnderLazer endLazer;
    
    public ProjectorRotation(final Projector projector) {
        this.end = projector.getEnd().clone();
        this.direction = projector.getDir().clone();
        this.start = projector.getStart();
        if (projector instanceof LazerProjector) {
            this.lazer = ((LazerProjector)projector).getLazer();
            this.projector = ((LazerProjector)projector).getProjector();
        }
        else if (projector instanceof EndProjector) {
            this.endLazer = ((EndProjector)projector).getLazer();
        }
    }
    
    protected void setLazerLocation(final Location location) {
        if (this.lazer != null) {
            this.lazer.setEndLocation(location);
        }
        else {
            this.endLazer.setEndLocation(location);
        }
    }
    
    public void rotate() {
        if (this.projector != null) {
            this.projector.setHeadPose(new EulerAngle(-(Math.atan2(Math.sqrt(this.direction.getZ() * this.direction.getZ() + this.direction.getX() * this.direction.getX()), this.direction.getY()) + 3.141592653589793) + Math.toRadians(90.0), Math.atan2(this.direction.getZ(), this.direction.getX()) + Math.toRadians(90.0), 0.0));
        }
    }
}
