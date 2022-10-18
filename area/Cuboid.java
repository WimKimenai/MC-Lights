package wkimenai.mc-lights.area;

import java.util.ArrayList;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import java.util.List;
import org.bukkit.World;

public class Cuboid
{
    public int xMin;
    public int xMax;
    public int yMin;
    public int yMax;
    public int zMin;
    public int zMax;
    public World world;
    public List<Location> locationsSquared;
    
    public Cuboid(final Location location, final Location location2) {
        this.xMin = Math.min(location.getBlockX(), location2.getBlockX());
        this.xMax = Math.max(location.getBlockX(), location2.getBlockX());
        this.yMin = Math.min(location.getBlockY(), location2.getBlockY());
        this.yMax = Math.max(location.getBlockY(), location2.getBlockY());
        this.zMin = Math.min(location.getBlockZ(), location2.getBlockZ());
        this.zMax = Math.max(location.getBlockZ(), location2.getBlockZ());
        this.world = location.getWorld();
        this.locationsSquared = this.getHollowCube(location, location2, 1.0);
    }
    
    public boolean isIn(final Location location) {
        return location.getWorld() == this.world && location.getBlockX() >= this.xMin && location.getBlockX() <= this.xMax && location.getBlockY() >= this.yMin && location.getBlockY() <= this.yMax && location.getBlockZ() >= this.zMin && location.getBlockZ() <= this.zMax;
    }
    
    public Vector getMinimumPoint() {
        return new Vector(this.xMin, this.yMin, this.zMin);
    }
    
    public Vector getMaximumPoint() {
        return new Vector(this.xMax, this.yMax, this.zMax);
    }
    
    public Block[] corners() {
        final Block[] array = new Block[8];
        final World world = this.world;
        array[0] = world.getBlockAt(this.xMin, this.yMin, this.zMin);
        array[1] = world.getBlockAt(this.xMin, this.yMin, this.zMax);
        array[2] = world.getBlockAt(this.xMin, this.yMax, this.zMin);
        array[3] = world.getBlockAt(this.xMin, this.yMax, this.zMax);
        array[4] = world.getBlockAt(this.xMax, this.yMin, this.zMin);
        array[5] = world.getBlockAt(this.xMax, this.yMin, this.zMax);
        array[6] = world.getBlockAt(this.xMax, this.yMax, this.zMin);
        array[7] = world.getBlockAt(this.xMax, this.yMax, this.zMax);
        return array;
    }
    
    public int getLength() {
        return (int)(this.getMaximumPoint().getZ() - this.getMinimumPoint().getZ() + 1.0);
    }
    
    public List<Location> getHollowCube(final Location location, final Location location2, final double n) {
        final ArrayList<Location> list = new ArrayList<Location>();
        final World world = location.getWorld();
        final double min = Math.min(location.getX(), location2.getX());
        final double min2 = Math.min(location.getY(), location2.getY());
        final double min3 = Math.min(location.getZ(), location2.getZ());
        final double max = Math.max(location.getX(), location2.getX());
        final double max2 = Math.max(location.getY(), location2.getY());
        final double max3 = Math.max(location.getZ(), location2.getZ());
        for (double n2 = min; n2 <= max; n2 += n) {
            for (double n3 = min2; n3 <= max2; n3 += n) {
                for (double n4 = min3; n4 <= max3; n4 += n) {
                    int n5 = 0;
                    if (n2 == min || n2 == max) {
                        ++n5;
                    }
                    if (n3 == min2 || n3 == max2) {
                        ++n5;
                    }
                    if (n4 == min3 || n4 == max3) {
                        ++n5;
                    }
                    if (n5 >= 2) {
                        list.add(new Location(world, n2, n3, n4));
                    }
                }
            }
        }
        return list;
    }
    
    public List<Location> getLocationsSquared() {
        return this.locationsSquared;
    }
    
    public Location getCenter() {
        return new Location(this.world, this.xMin + (this.xMax + 1 - this.xMin) / 2.0, this.yMin + (this.yMax + 1 - this.yMin) / 2.0, this.zMin + (this.zMax + 1 - this.zMin) / 2.0);
    }
    
    public int getXWidth() {
        return this.xMax - this.xMin;
    }
    
    public int getZWidth() {
        return this.zMax - this.zMin;
    }
    
    public int getHeight() {
        return this.yMax - this.yMin;
    }
    
    public int getArea() {
        return this.getHeight() * this.getXWidth() * this.getZWidth();
    }
}
