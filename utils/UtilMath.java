package wkimenai.mc-lights.utils;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import wkimenai.mc-lights.area.Cuboid;
import wkimenai.mc-lights.listener.task.GlobalTime;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import java.util.Random;

public class UtilMath
{
    public static final Random random;
    public static final BlockFace[] axis;
    public static final byte[] axisByte;
    
    static {
        random = new Random(System.nanoTime());
        axis = new BlockFace[] { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };
        axisByte = new byte[] { 3, 4, 2, 5 };
    }
    
    public static Location getRandomFromCenter() {
        final Cuboid cuboid = GlobalTime.cuboid;
        return new Location(cuboid.getCenter().getWorld(), (double)randomRange(cuboid.xMin, cuboid.xMax), (double)randomRange(cuboid.yMin, cuboid.yMax), (double)randomRange(cuboid.zMin, cuboid.zMax));
    }
    
    public static double offset(final Location location, final Location location2) {
        return offset(location.toVector(), location2.toVector());
    }
    
    public static double offset(final Vector vector, final Vector vector2) {
        return vector.subtract(vector2).length();
    }
    
    public static float randomRange(final float n, final float n2) {
        return n + (float)Math.random() * (n2 - n);
    }
    
    public static int randomRange(final int n, final int n2) {
        return new Random().nextInt(n2 - n + 1) + n;
    }
    
    public static double randomRange(final double n, final double n2) {
        return (Math.random() < 0.5) ? ((1.0 - Math.random()) * (n2 - n) + n) : (Math.random() * (n2 - n) + n);
    }
    
    public static double arrondi(final double n, final int n2) {
        return (int)(n * Math.pow(10.0, n2) + 0.5) / Math.pow(10.0, n2);
    }
    
    public static int getRandomWithExclusion(final int n, final int n2, final int... array) {
        int n3 = n + UtilMath.random.nextInt(n2 - n + 1 - array.length);
        for (int length = array.length, n4 = 0; n4 < length && n3 >= array[n4]; ++n3, ++n4) {}
        return n3;
    }
    
    public static double getLookAtYaw(final Vector vector) {
        return Math.atan2(vector.getX(), vector.getZ());
    }
    
    public static boolean elapsed(final long n, final long n2) {
        return System.currentTimeMillis() - n > n2;
    }
    
    public static Vector getBumpVector(final Entity entity, final Location location, final double n) {
        final Vector normalize = entity.getLocation().toVector().subtract(location.toVector()).normalize();
        normalize.multiply(n);
        return normalize;
    }
    
    public static Vector getPullVector(final Entity entity, final Location location, final double n) {
        final Vector normalize = location.toVector().subtract(entity.getLocation().toVector()).normalize();
        normalize.multiply(n);
        return normalize;
    }
    
    public static void bumpEntity(final Entity entity, final Location location, final double n) {
        entity.setVelocity(getBumpVector(entity, location, n));
    }
    
    public static void bumpEntity(final Entity entity, final Location location, final double n, final double y) {
        final Vector bumpVector = getBumpVector(entity, location, n);
        bumpVector.setY(y);
        entity.setVelocity(bumpVector);
    }
    
    public static void pullEntity(final Entity entity, final Location location, final double n) {
        entity.setVelocity(getPullVector(entity, location, n));
    }
    
    public static void pullEntity(final Entity entity, final Location location, final double n, final double y) {
        final Vector pullVector = getPullVector(entity, location, n);
        pullVector.setY(y);
        entity.setVelocity(pullVector);
    }
    
    public static final Vector rotateAroundAxisX(final Vector vector, final double n) {
        final double cos = Math.cos(n);
        final double sin = Math.sin(n);
        return vector.setY(vector.getY() * cos - vector.getZ() * sin).setZ(vector.getY() * sin + vector.getZ() * cos);
    }
    
    public static final Vector rotateAroundAxisY(final Vector vector, final double n) {
        final double cos = Math.cos(n);
        final double sin = Math.sin(n);
        return vector.setX(vector.getX() * cos + vector.getZ() * sin).setZ(vector.getX() * -sin + vector.getZ() * cos);
    }
    
    public static final Vector rotateAroundAxisZ(final Vector vector, final double n) {
        final double cos = Math.cos(n);
        final double sin = Math.sin(n);
        return vector.setX(vector.getX() * cos - vector.getY() * sin).setY(vector.getX() * sin + vector.getY() * cos);
    }
    
    public static final Vector rotateVector(final Vector vector, final double n, final double n2, final double n3) {
        rotateAroundAxisX(vector, n);
        rotateAroundAxisY(vector, n2);
        rotateAroundAxisZ(vector, n3);
        return vector;
    }
    
    public static Vector rotate(Vector vector, final Location location) {
        final double n = location.getYaw() / 180.0f * 3.141592653589793;
        vector = rotateAroundAxisX(vector, location.getPitch() / 180.0f * 3.141592653589793);
        vector = rotateAroundAxisY(vector, -n);
        return vector;
    }
    
    public static byte toPackedByte(final float n) {
        return (byte)(n * 256.0f / 360.0f);
    }
    
    public static Vector getRandomVector() {
        return new Vector(UtilMath.random.nextDouble() * 2.0 - 1.0, UtilMath.random.nextDouble() * 2.0 - 1.0, UtilMath.random.nextDouble() * 2.0 - 1.0).normalize();
    }
    
    public static Vector getRandomCircleVector() {
        final double a = UtilMath.random.nextDouble() * 2.0 * 3.141592653589793;
        return new Vector(Math.cos(a), Math.sin(a), Math.sin(a));
    }
    
    public static Vector getRandomVectorline() {
        final int n = -5;
        final int n2 = 5;
        final int n3 = (int)(Math.random() * (n2 - n) + n);
        final int n4 = (int)(Math.random() * (n2 - n) + n);
        final double n5 = -5.0;
        return new Vector((double)n4, Math.random() * (-1.0 - n5) + n5, (double)n3).normalize();
    }
    
    public static final Vector rotateVector(final Vector vector, final float n, final float n2) {
        final double radians = Math.toRadians(-1.0f * (n + 90.0f));
        final double radians2 = Math.toRadians(-n2);
        final double cos = Math.cos(radians);
        final double cos2 = Math.cos(radians2);
        final double sin = Math.sin(radians);
        final double sin2 = Math.sin(radians2);
        final double x = vector.getX();
        final double y = vector.getY();
        final double n3 = x * cos2 - y * sin2;
        final double n4 = x * sin2 + y * cos2;
        final double z = vector.getZ();
        final double n5 = n3;
        return new Vector(z * sin + n5 * cos, n4, z * cos - n5 * sin);
    }
}
