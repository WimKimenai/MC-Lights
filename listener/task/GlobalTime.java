package wkimenai.mc-lights.listener.task;

import wkimenai.mc-lights.staff.Staff;
import org.bukkit.Location;
import wkimenai.mc-lights.visual.Visual;
import wkimenai.mc-lights.effect.Effect;
import wkimenai.mc-lights.projector.projectors.LazerProjector;
import wkimenai.mc-lights.database.Database;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.Bukkit;
import wkimenai.mc-lights.utils.UtilParticle;
import org.bukkit.util.Vector;
import org.bukkit.Particle;
import wkimenai.mc-lights.cmd.cmds.GeneralCMD;
import wkimenai.mc-lights.updater.UpdateType;
import wkimenai.mc-lights.updater.event.UpdaterEvent;
import wkimenai.mc-lights.mc-lights;
import java.util.ArrayList;
import wkimenai.mc-lights.config.mc-lightsConfig;
import org.bukkit.entity.Player;
import java.util.List;
import wkimenai.mc-lights.area.Cuboid;
import org.bukkit.event.Listener;

public class GlobalTime implements Listener
{
    public static Cuboid cuboid;
    public static List<Player> entered;
    private mc-lightsConfig config;
    private static int time;
    
    static {
        GlobalTime.entered = new ArrayList<Player>();
        GlobalTime.time = 0;
    }
    
    public GlobalTime() {
        this.config = mc-lights.getInstance().getProdigyConfig();
    }
    
    @EventHandler
    public void update(final UpdaterEvent updaterEvent) {
        if (updaterEvent.getType() == UpdateType.TICK) {
            ++GlobalTime.time;
            if (GlobalTime.time >= 9999999) {
                GlobalTime.time = 0;
            }
        }
        else if (updaterEvent.getType() == UpdateType.FAST) {
            if (GeneralCMD.getCurrentVisualizing() != null) {
                GeneralCMD.getCurrentVisualizing().getLocationsSquared().forEach(location -> UtilParticle.sendParticle(location, Particle.FIREWORKS_SPARK, 1, new Vector(0, 0, 0), 0.0f));
            }
            if (!Bukkit.getOnlinePlayers().isEmpty() && GlobalTime.cuboid != null) {
                Bukkit.getOnlinePlayers().stream().filter(player -> player.getWorld().equals(GlobalTime.cuboid.world)).forEach(player2 -> {
                    if (GlobalTime.cuboid.isIn(player2.getLocation()) && !GlobalTime.entered.contains(player2)) {
                        GlobalTime.entered.add(player2);
                        this.setupArea(player2);
                    }
                    if (!GlobalTime.cuboid.isIn(player2.getLocation()) && GlobalTime.entered.contains(player2)) {
                        GlobalTime.entered.remove(player2);
                        this.disableArea(player2);
                    }
                });
            }
        }
    }
    
    @EventHandler
    public void quit(final PlayerQuitEvent playerQuitEvent) {
        GlobalTime.entered.remove(playerQuitEvent.getPlayer());
    }
    
    private void setupArea(final Player player) {
        Database.getStaff().getStaffList().forEach(staff -> staff.spawn(player));
        LazerProjector.getLazerProjectors().stream().filter(lazerProjector -> lazerProjector.isOn()).forEach(lazerProjector2 -> lazerProjector2.getLazer().spawn(player));
        Effect.getEffects().stream().filter(effect -> effect.isStarted()).forEach(effect2 -> effect2.start(player));
        Visual.getVisuals().stream().filter(visual -> visual.isStarted()).forEach(visual2 -> visual2.start(player));
        if (this.config.titleEnable) {
            player.sendTitle(this.config.titleEnter, this.config.subtitleEnter, 10, 100, 10);
        }
    }
    
    private void disableArea(final Player player) {
        Database.getStaff().getStaffList().stream().filter(staff -> !staff.isConstant()).forEach(staff2 -> staff2.despawn(player));
        LazerProjector.getLazerProjectors().forEach(lazerProjector -> lazerProjector.getLazer().despawn(player));
        Effect.getEffects().stream().filter(effect -> effect.isStarted()).forEach(effect2 -> effect2.stop(player));
        Visual.getVisuals().stream().filter(visual -> visual.isStarted()).forEach(visual2 -> visual2.stop(player));
    }
    
    public static int getTime() {
        return GlobalTime.time;
    }
}
