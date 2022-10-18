package wkimenai.mc-lights.projector;

import java.util.ArrayList;
import org.bukkit.Location;
import java.util.List;

public class LeverActivator
{
    private List<Integer> projectors;
    private RotationType type;
    private Location l;
    private int currentID;
    
    public LeverActivator(final Location l, final RotationType type) {
        this.projectors = new ArrayList<Integer>();
        this.l = l;
        this.type = type;
    }
    
    public int getCurrentID() {
        return this.currentID;
    }
    
    public List<Integer> getProjectors() {
        return this.projectors;
    }
    
    public Location getL() {
        return this.l;
    }
    
    public RotationType getType() {
        return this.type;
    }
    
    public void setCurrentID(final int currentID) {
        this.currentID = currentID;
    }
}
