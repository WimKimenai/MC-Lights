package wkimenai.mc-lights.listener.task;

import org.bukkit.entity.Player;
import wkimenai.mc-lights.staff.Staff;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import wkimenai.mc-lights.nms.entity.ReflectedLivingEntity;
import wkimenai.mc-lights.utils.UtilLocation;
import wkimenai.mc-lights.database.Database;
import wkimenai.mc-lights.updater.UpdateType;
import wkimenai.mc-lights.updater.event.UpdaterEvent;
import org.bukkit.event.Listener;

public class StaffTask implements Listener
{
    @EventHandler
    public void update(final UpdaterEvent updaterEvent) {
        if (updaterEvent.getType() == UpdateType.TICK) {
            Database.getStaff().getStaffList().forEach(staff -> staff.update());
            final ReflectedLivingEntity reflectedLivingEntity;
            final Location location;
            final Location location2;
            final Vector vector;
            final ReflectedLivingEntity reflectedLivingEntity2;
            final Vector vector2;
            final double angrad;
            Database.getStaff().getStaffList().stream().filter(staff2 -> staff2.lookAtPlayer && staff2.getEntity() != null).forEach(staff3 -> {
                staff3.getEntity();
                reflectedLivingEntity.getLocation();
                if (UtilLocation.getClosestPlayersFromLocation(location, 10.0).stream().filter(player -> player.isValid()).findFirst().isPresent()) {
                    UtilLocation.getClosestPlayersFromLocation(location, 10.0).stream().filter(player2 -> player2.isValid()).findFirst().ifPresent(player3 -> {
                        player3.getLocation().toVector().subtract(location2.toVector());
                        location2.toVector().subtract(player3.getLocation().toVector());
                        Math.atan2(vector.getZ(), vector.getX());
                        reflectedLivingEntity2.setPitchRotation((float)Math.toDegrees(Math.atan2(Math.sqrt(vector2.getZ() * vector2.getZ() + vector2.getX() * vector2.getX()), vector2.getY()) + 3.141592653589793) + 110.0f);
                        reflectedLivingEntity2.setYawRotation((float)Math.toDegrees(angrad) + 90.0f);
                    });
                }
                else {
                    reflectedLivingEntity.setYawRotation(staff3.getLocation().getYaw());
                    reflectedLivingEntity.setPitchRotation(0.0f);
                }
            });
        }
    }
}
