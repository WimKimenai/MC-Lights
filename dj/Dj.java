package wkimenai.mc-lights.dj;

import wkimenai.mc-lights.dj.choreography.SlowHandDanceChoreography;
import wkimenai.mc-lights.dj.choreography.ScratchChoreography;
import wkimenai.mc-lights.dj.choreography.PutYourHandsUpChoreography;
import wkimenai.mc-lights.dj.choreography.JumpAroundChoreography;
import wkimenai.mc-lights.dj.choreography.HandsInTheSkyChoreography;
import wkimenai.mc-lights.dj.choreography.CrazyChoreography;
import wkimenai.mc-lights.utils.UtilMath;
import org.bukkit.Location;
import wkimenai.mc-lights.nms.others.FakeLightningStrike;
import org.bukkit.Sound;
import wkimenai.mc-lights.utils.UtilParticle;
import org.bukkit.util.Vector;
import org.bukkit.Particle;
import org.bukkit.Material;
import wkimenai.mc-lights.utils.UtilItem;
import wkimenai.mc-lights.database.Database;
import org.bukkit.entity.ArmorStand;
import java.util.Random;

public class Dj
{
    private static Dj currentDj;
    private Choreography choreography;
    private Random r;
    private ArmorStand dj;
    private DJType type;
    private boolean jump;
    private boolean flying;
    
    public Dj() {
        this.r = new Random();
        this.jump = false;
        this.flying = false;
        if (Dj.currentDj != null) {
            Dj.currentDj.removeDj();
            return;
        }
        final Location djLocation = Database.getGeneral().getDjLocation();
        this.type = DJType.getDjs().get(this.r.nextInt(DJType.getDjs().size()));
        final Location add = djLocation.getBlock().getLocation().add(0.5, 0.0, 0.5);
        add.setPitch(0.0f);
        add.setYaw(djLocation.getYaw());
        (this.dj = (ArmorStand)djLocation.getWorld().spawn(add, (Class)ArmorStand.class)).setHelmet(UtilItem.getSkull(this.type.getHeadtexture()));
        this.dj.setBoots(UtilItem.getColorArmor(Material.LEATHER_BOOTS, this.type.getArmorColor()));
        this.dj.setChestplate(UtilItem.getColorArmor(Material.LEATHER_CHESTPLATE, this.type.getArmorColor()));
        this.dj.setLeggings(UtilItem.getColorArmor(Material.LEATHER_LEGGINGS, this.type.getArmorColor()));
        this.dj.setBasePlate(false);
        this.dj.setArms(true);
        this.dj.setInvulnerable(true);
        this.dj.setRemoveWhenFarAway(false);
        this.dj.setGravity(true);
        this.dj.setMarker(true);
        this.choreography = this.getRandom();
        UtilParticle.sendParticle(this.dj.getLocation(), Particle.EXPLOSION_HUGE, 1, new Vector(0.1f, 0.1f, 0.1f), 0.02f);
        this.dj.getWorld().playSound(this.dj.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.0f);
        new FakeLightningStrike(this.dj.getLocation()).spawn();
        Dj.currentDj = this;
    }
    
    private Choreography getRandom() {
        Choreography choreography = null;
        switch (UtilMath.randomRange(1, 6)) {
            case 1: {
                choreography = new CrazyChoreography(this);
                break;
            }
            case 2: {
                choreography = new HandsInTheSkyChoreography(this);
                break;
            }
            case 3: {
                choreography = new JumpAroundChoreography(this);
                break;
            }
            case 4: {
                choreography = new PutYourHandsUpChoreography(this);
                break;
            }
            case 5: {
                choreography = new ScratchChoreography(this);
                break;
            }
            case 6: {
                choreography = new SlowHandDanceChoreography(this);
                break;
            }
        }
        return choreography;
    }
    
    public void updateChoreographyType() {
        this.jump = false;
        this.flying = false;
        this.choreography = this.getRandom();
        if (UtilMath.randomRange(0, 100) >= 90) {
            this.flying = true;
        }
        if (this.flying) {
            return;
        }
        if (UtilMath.randomRange(0, 100) >= 50) {
            this.jump = true;
        }
    }
    
    public void update() {
        UtilParticle.sendParticle(this.dj.getLocation().add((double)UtilMath.randomRange(-2.0f, 2.0f), (double)UtilMath.randomRange(0.5f, 1.5f), (double)UtilMath.randomRange(-2.0f, 2.0f)), this.type.getParticle(), 1, new Vector(0.1f, 0.1f, 0.1f), 0.02f);
    }
    
    public void updateChoreography() {
        if (this.choreography != null) {
            this.choreography.updateTimer();
            this.choreography.updateChoreography();
        }
    }
    
    public void removeDj() {
        if (this.dj != null) {
            this.dj.remove();
        }
        Dj.currentDj = null;
    }
    
    public boolean isJump() {
        return this.jump;
    }
    
    public boolean isFlying() {
        return this.flying;
    }
    
    public ArmorStand getDj() {
        return this.dj;
    }
    
    public static Dj getCurrentDj() {
        return Dj.currentDj;
    }
}
