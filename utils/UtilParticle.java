package wkimenai.mc-lights.utils;

import wkimenai.mc-lights.nms.NMSPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutGameStateChange;
import org.bukkit.material.MaterialData;
import wkimenai.mc-lights.listener.task.GlobalTime;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.Particle;
import org.bukkit.Location;

public class UtilParticle
{
    public static void sendParticle(final Location location, final Particle particle, final int n, final Vector vector, final float n2) {
        location.getWorld().spawnParticle(particle, location, n, vector.getX(), vector.getY(), vector.getZ(), (double)n2);
    }
    
    public static void sendParticle(final Location location, final Particle particle, final int n, final Vector vector, final float n2, final Player player) {
        player.spawnParticle(particle, location, n, vector.getX(), vector.getY(), vector.getZ(), (double)n2);
    }
    
    public static void sendAreaParticles(final Location location, final Particle particle, final int n, final Vector vector, final float n2) {
        GlobalTime.entered.forEach(player -> player.spawnParticle(particle, location, n, vector.getX(), vector.getY(), vector.getZ(), (double)n2));
    }
    
    public static void sendParticle(final Location location, final Particle particle, final int n, final Vector vector, final MaterialData materialData) {
        location.getWorld().spawnParticle(particle, location, n, vector.getX(), vector.getY(), vector.getZ(), 1.0, (Object)materialData);
    }
    
    public static void sendAreaParticle(final Location location, final Particle particle, final int n, final Vector vector, final MaterialData materialData) {
        GlobalTime.entered.forEach(player -> player.spawnParticle(particle, location, n, vector.getX(), vector.getY(), vector.getZ(), 1.0, (Object)materialData));
    }
    
    public static void setWeirdWeather(final float n) {
        NMSPlayer.sendPacket(GlobalTime.entered, new PacketPlayOutGameStateChange(8, 80.0f), new PacketPlayOutGameStateChange(7, n));
    }
    
    public static void setWeirdWeather(final Player player, final float n) {
        NMSPlayer.sendPacket(player, new PacketPlayOutGameStateChange(8, 80.0f), new PacketPlayOutGameStateChange(7, n));
    }
}
