package wkimenai.mc-lights.projector.projectors;

import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import wkimenai.mc-lights.nms.others.EnderLazer;
import wkimenai.mc-lights.nms.entity.ReflectedArmorStand;
import wkimenai.mc-lights.projector.LeverActivator;
import java.util.List;
import wkimenai.mc-lights.projector.Projector;

public class EndProjector extends Projector
{
    private static List<EndProjector> projectors;
    private static List<LeverActivator> levers;
    private ReflectedArmorStand nameID;
    private int currentID;
    private int distance;
    protected EnderLazer lazer;
    private boolean isOn;
    
    static {
        EndProjector.projectors = new ArrayList<EndProjector>();
        EndProjector.levers = new ArrayList<LeverActivator>();
    }
    
    public EndProjector(final Vector vector, final Block block) {
        super(vector, block);
        this.currentID = -1;
        this.distance = 20;
        this.isOn = false;
        this.start = block.getLocation();
        this.end = this.start.clone().add(vector.clone().multiply(40));
        this.lazer = new EnderLazer(this.start);
    }
    
    @Override
    public void removeProjector() {
        this.lazer.remove();
    }
    
    @Override
    public void setOn() {
        this.lazer.spawn();
        this.lazer.setEndLocation(this.end);
        this.isOn = true;
    }
    
    @Override
    public void setOff() {
        this.rotation = null;
        this.lazer.remove();
        this.isOn = false;
    }
    
    @Override
    public boolean isOn() {
        return this.isOn;
    }
    
    public int getDistance() {
        return this.distance;
    }
    
    public static List<EndProjector> getProjectors() {
        return EndProjector.projectors;
    }
    
    public EnderLazer getLazer() {
        return this.lazer;
    }
    
    public int getCurrentID() {
        return this.currentID;
    }
    
    public void setCurrentID(final int currentID) {
        this.currentID = currentID;
    }
    
    public static List<LeverActivator> getLevers() {
        return EndProjector.levers;
    }
    
    public void setNameID(final ReflectedArmorStand nameID) {
        this.nameID = nameID;
    }
    
    public ReflectedArmorStand getNameID() {
        return this.nameID;
    }
}
