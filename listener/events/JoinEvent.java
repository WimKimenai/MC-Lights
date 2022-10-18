package wkimenai.mc-lights.listener.events;

import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import wkimenai.mc-lights.staff.Staff;
import wkimenai.mc-lights.database.Database;
import wkimenai.mc-lights.listener.task.GlobalTime;
import wkimenai.mc-lights.mc-lights;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;

public class JoinEvent implements Listener
{
    @EventHandler
    public void join(final PlayerJoinEvent playerJoinEvent) {
        new BukkitRunnable() {
            public void run() {
                if (mc-lights.getInstance().isOld() && (playerJoinEvent.getPlayer().isOp() || playerJoinEvent.getPlayer().hasPermission("pnc.admin"))) {
                    playerJoinEvent.getPlayer().sendMessage("§bA new version mc-lights is available !");
                    mc-lights.getInstance().setOld(false);
                }
                if (GlobalTime.cuboid != null && playerJoinEvent.getPlayer().getWorld().equals(GlobalTime.cuboid.getCenter().getWorld())) {
                    Database.getStaff().getStaffList().forEach(p0 -> {});
                    Database.getStaff().getStaffList().stream().filter(staff -> staff.isConstant()).forEach(staff2 -> staff2.spawn(playerJoinEvent.getPlayer()));
                }
            }
        }.runTaskLater((Plugin)mc-lights.getInstance(), 10L);
    }
}
