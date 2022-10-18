package wkimenai.mc-lights.newfeatures.lazer;

import java.util.ArrayList;
import org.bukkit.Material;
import wkimenai.mc-lights.utils.ColoredParticle;
import org.bukkit.util.EulerAngle;
import wkimenai.mc-lights.utils.UtilMath;
import wkimenai.mc-lights.utils.UtilItem;
import wkimenai.mc-lights.utils.SpotType;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.Color;
import org.bukkit.util.Vector;
import java.util.List;

public class LightProjector
{
    private static List<LightProjector> projectors;
    private int distance;
    private Vector direction;
    private Color color;
    private ArmorStand projector;
    private Block support;
    private Location start;
    private double angularVelocityX;
    private double angularVelocityY;
    private double angularVelocityZ;
    private double xRotation;
    private double yRotation;
    private double zRotation;
    
    public LightProjector(final Player player, final Block support) {
        this.distance = 20;
        this.angularVelocityX = 0.02243994752564138;
        this.angularVelocityY = 0.020943951023931952;
        this.angularVelocityZ = 135.0;
        this.xRotation = 0.0;
        this.yRotation = 0.0;
        this.zRotation = 0.0;
        this.support = support;
        final Location transformLocation = this.transformLocation(support);
        this.start = transformLocation.clone().add(0.0, 1.8, 0.0);
        (this.direction = player.getLocation().toVector().subtract(this.start.toVector()).normalize()).add(new Vector(0.0, Math.toRadians(20.0), 0.0));
        (this.projector = (ArmorStand)transformLocation.getWorld().spawn(transformLocation, (Class)ArmorStand.class)).setHelmet(UtilItem.getSkull(SpotType.OFF.getTexture()));
        this.projector.setVisible(false);
        this.projector.setHeadPose(new EulerAngle(-this.direction.getY(), Math.toRadians(UtilMath.getLookAtYaw(this.direction) + 90.0), 0.0));
        this.projector.setGravity(false);
        this.setOn(SpotType.YELLOW);
        LightProjector.projectors.add(this);
    }
    
    public void rotate(final int n) {
        Vector normalize = new Vector();
        for (double n2 = 0.0; n2 < this.distance; n2 += 0.3) {
            final Location clone = this.start.clone();
            clone.setDirection(this.direction);
            final Vector multiply = clone.getDirection().multiply(n2);
            this.xRotation = n * this.angularVelocityX;
            this.yRotation = n * this.angularVelocityY;
            this.zRotation = n * this.angularVelocityZ;
            UtilMath.rotateVector(multiply, this.xRotation, this.yRotation, this.zRotation);
            clone.add(multiply);
            normalize = clone.toVector().subtract(this.start.toVector()).normalize();
            new ColoredParticle(ColoredParticle.ParticleType.REDSTONE, clone, new Vector(this.color.getRed(), this.color.getGreen(), this.color.getBlue())).send();
            if (clone.clone().add(clone.getDirection().multiply(0.4)).getBlock().getType() != Material.AIR) {
                break;
            }
        }
        this.projector.setHeadPose(new EulerAngle(-normalize.getY(), Math.toRadians(UtilMath.getLookAtYaw(normalize) + 90.0), 0.0));
    }
    
    private Location transformLocation(final Block block) {
        final Location location = block.getLocation();
        location.add(0.5, 0.0, 0.5);
        switch (block.getData()) {
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
    
    public void removeProjector() {
        this.projector.remove();
        this.support.setType(Material.AIR);
    }
    
    public void setOn(final SpotType spotType) {
        this.projector.setHelmet(UtilItem.getSkull(spotType.getTexture()));
        this.color = Color.fromRGB(spotType.getR(), spotType.getG(), spotType.getB());
    }
    
    public void setOff() {
        this.projector.setHelmet(UtilItem.getSkull(SpotType.OFF.getTexture()));
        this.color = null;
    }
    
    public boolean isOn() {
        return this.color != null;
    }
    
    public ArmorStand getProjector() {
        return this.projector;
    }
    
    public Block getSupport() {
        return this.support;
    }
    
    public int getDistance() {
        return this.distance;
    }
    
    public Vector getDirection() {
        return this.direction;
    }
    
    public static List<LightProjector> getProjectors() {
        return LightProjector.projectors;
    }
    
    static {
        LightProjector.projectors = new ArrayList<LightProjector>();
    }
}
