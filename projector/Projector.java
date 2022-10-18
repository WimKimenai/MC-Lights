package wkimenai.mc-lights.projector;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public abstract class Projector
{
    protected Vector dir;
    protected Block b;
    protected ProjectorRotation rotation;
    protected Location end;
    protected Location start;
    protected boolean isOn;
    
    public Projector(final Vector dir, final Block b) {
        this.isOn = false;
        this.b = b;
        this.dir = dir;
    }
    
    public void removeProjector() {
    }
    
    public void setOn() {
    }
    
    public void setOff() {
    }
    
    public Block getB() {
        return this.b;
    }
    
    public Vector getDir() {
        return this.dir;
    }
    
    public void setRotation(final ProjectorRotation rotation) {
        this.rotation = rotation;
    }
    
    public Location getStart() {
        return this.start;
    }
    
    public void setOn(final boolean isOn) {
        this.isOn = isOn;
    }
    
    public boolean isOn() {
        return this.isOn;
    }
    
    public Location getEnd() {
        return this.end;
    }
    
    public ProjectorRotation getRotation() {
        return this.rotation;
    }
}
