package wkimenai.mc-lights.projector.projectors;

import org.bukkit.Material;
import org.bukkit.util.EulerAngle;
import wkimenai.mc-lights.utils.UtilItem;
import wkimenai.mc-lights.utils.SpotType;
import wkimenai.mc-lights.mc-lights;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import org.bukkit.Location;
import wkimenai.mc-lights.nms.others.Lazer;
import org.bukkit.entity.ArmorStand;
import wkimenai.mc-lights.projector.LeverActivator;
import java.util.List;
import wkimenai.mc-lights.config.mc-lightsConfig;
import wkimenai.mc-lights.projector.Projector;

public class LazerProjector extends Projector
{
    private mc-lightsConfig config;
    private static List<LazerProjector> lazerProjectors;
    private static List<LeverActivator> levers;
    private int currentID;
    private int colorTimer;
    private int distance;
    private ArmorStand projector;
    protected Lazer lazer;
    private Location support;
    private byte position;
    
    static {
        LazerProjector.lazerProjectors = new ArrayList<LazerProjector>();
        LazerProjector.levers = new ArrayList<LeverActivator>();
    }
    
    public LazerProjector(final Vector direction, final Block block, final byte position) {
        super(direction, block);
        this.config = mc-lights.getInstance().getProdigyConfig();
        this.currentID = -1;
        this.colorTimer = 0;
        this.distance = this.config.laserDistance;
        this.support = block.getLocation();
        this.support.getBlock().setTypeId(this.config.supportID);
        this.support.getBlock().setData((byte)this.config.supportData);
        this.position = position;
        final Location transformLocation = this.transformLocation(block);
        this.start = transformLocation.clone().add(0.0, 0.7, 0.0);
        this.support.setDirection(direction);
        (this.projector = (ArmorStand)transformLocation.getWorld().spawn(transformLocation, (Class)ArmorStand.class)).setHelmet(UtilItem.getSkull(SpotType.OFF.getTexture()));
        this.projector.setVisible(false);
        this.projector.setHeadPose(new EulerAngle(Math.toRadians(this.support.getPitch()), Math.toRadians(this.support.getYaw()), 0.0));
        this.projector.setGravity(false);
        this.end = this.start.clone().add(direction.clone().multiply(40));
        this.lazer = new Lazer(this.start, this.end);
    }
    
    private Location transformLocation(final Block block) {
        final Location location = block.getLocation();
        location.add(0.5, 0.0, 0.5);
        switch (this.position) {
            case 1: {
                location.setY(location.getY() - 0.7);
                break;
            }
            case 0: {
                location.setY(location.getY() - 1.7);
                break;
            }
            case 2: {
                location.setY(location.getY() - 1.3);
                location.setZ(location.getZ() - 0.4);
                break;
            }
            case 3: {
                location.setY(location.getY() - 1.3);
                location.setZ(location.getZ() + 0.4);
                break;
            }
            case 4: {
                location.setY(location.getY() - 1.3);
                location.setX(location.getX() - 0.4);
                break;
            }
            case 5: {
                location.setY(location.getY() - 1.3);
                location.setX(location.getX() + 0.4);
                break;
            }
        }
        return location;
    }
    
    @Override
    public void removeProjector() {
        this.lazer.despawn();
        this.lazer.remove();
        this.projector.remove();
        this.support.getBlock().setType(Material.AIR);
    }
    
    public void setProjectorColor(final SpotType spotType) {
        this.projector.setHelmet(UtilItem.getSkull(spotType.getTexture()));
    }
    
    @Override
    public void setOn() {
        this.lazer.spawn();
        this.projector.setHelmet(UtilItem.getSkull(SpotType.GREEN.getTexture()));
        this.isOn = true;
    }
    
    @Override
    public void setOff() {
        this.rotation = null;
        this.lazer.setEndLocation(this.end);
        this.lazer.despawn();
        this.projector.setHeadPose(new EulerAngle(Math.toRadians(this.support.getPitch()), Math.toRadians(this.support.getYaw()), 0.0));
        this.projector.setHelmet(UtilItem.getSkull(SpotType.OFF.getTexture()));
        this.isOn = false;
    }
    
    public byte getPosition() {
        return this.position;
    }
    
    public ArmorStand getProjector() {
        return this.projector;
    }
    
    public Location getSupport() {
        return this.support;
    }
    
    public int getDistance() {
        return this.distance;
    }
    
    public static List<LazerProjector> getLazerProjectors() {
        return LazerProjector.lazerProjectors;
    }
    
    public Lazer getLazer() {
        return this.lazer;
    }
    
    @Override
    public Location getStart() {
        return this.start;
    }
    
    public void setColorTimer(final int colorTimer) {
        this.colorTimer = colorTimer;
    }
    
    public int getColorTimer() {
        return this.colorTimer;
    }
    
    public int getCurrentID() {
        return this.currentID;
    }
    
    public void setCurrentID(final int currentID) {
        this.currentID = currentID;
    }
    
    public static List<LeverActivator> getLevers() {
        return LazerProjector.levers;
    }
}
