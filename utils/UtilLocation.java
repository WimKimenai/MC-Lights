package wkimenai.mc-lights.utils;

import java.util.HashSet;
import org.bukkit.entity.Entity;
import java.util.Iterator;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Block;
import java.util.List;

public class UtilLocation
{
    private static List<Block> unbreakableBlocks;
    private static final BlockFace[] axis;
    private static final BlockFace[] radial;
    
    public static List<Player> getClosestPlayersFromLocation(final Location location, final double n) {
        final ArrayList<Player> list = new ArrayList<Player>();
        final double n2 = n * n;
        for (final Player player : location.getWorld().getPlayers()) {
            if (player.getLocation().add(0.0, 0.85, 0.0).distanceSquared(location) <= n2) {
                list.add(player);
            }
        }
        return list;
    }
    
    public static Entity[] getNearbyEntities(final Location location, final int n) {
        final int n2 = (n < 16) ? 1 : ((n - n % 16) / 16);
        final HashSet<Entity> set = new HashSet<Entity>();
        for (int i = 0 - n2; i <= n2; ++i) {
            for (int j = 0 - n2; j <= n2; ++j) {
                for (final Entity e : new Location(location.getWorld(), (double)((int)location.getX() + i * 16), (double)(int)location.getY(), (double)((int)location.getZ() + j * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distance(location) <= n && e.getLocation().getBlock() != location.getBlock()) {
                        set.add(e);
                    }
                }
            }
        }
        return set.toArray(new Entity[set.size()]);
    }
    
    public static List<Block> getIn2DRadius(final Location location, final double n) {
        final ArrayList<Block> list = new ArrayList<Block>();
        for (int n2 = (int)n + 1, i = -n2; i <= n2; ++i) {
            for (int j = -n2; j <= n2; ++j) {
                final Block block = location.getWorld().getBlockAt((int)(location.getX() + i), (int)location.getY(), (int)(location.getZ() + j));
                if (UtilMath.offset(location, block.getLocation().add(0.5, 0.5, 0.5)) <= n) {
                    list.add(block);
                }
            }
        }
        return list;
    }
    
    public static BlockFace yawToFace(final float n, final boolean b) {
        if (b) {
            return UtilLocation.radial[Math.round(n / 45.0f) & 0x7].getOppositeFace();
        }
        return UtilLocation.axis[Math.round(n / 90.0f) & 0x3].getOppositeFace();
    }
    
    public static List<Block> getUnbreakableBlocks() {
        return UtilLocation.unbreakableBlocks;
    }
    
    static {
        UtilLocation.unbreakableBlocks = new ArrayList<Block>();
        axis = new BlockFace[] { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
        radial = new BlockFace[] { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };
    }
}
