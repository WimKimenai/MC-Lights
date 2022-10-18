package wkimenai.mc-lights.utils;

import wkimenai.mc-lights.nms.NMSPlayer;
import wkimenai.mc-lights.listener.task.GlobalTime;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import wkimenai.mc-lights.nms.Reflection;

public class ColoredParticle
{
    private static Class<?> enumParticleClass;
    private static Reflection.ConstructorInvoker particleConstructor;
    private static Reflection.MethodInvoker getParticleMethod;
    private ParticleType type;
    private Location location;
    private double offsetX;
    private double offsetY;
    private double offsetZ;
    
    public ColoredParticle(final ParticleType type, final Location location, final Vector vector) {
        this.type = type;
        this.location = location;
        this.offsetX = vector.getX();
        this.offsetY = vector.getY();
        this.offsetZ = vector.getZ();
    }
    
    public void send() {
        NMSPlayer.sendPacket(GlobalTime.entered, ColoredParticle.particleConstructor.invoke(ColoredParticle.getParticleMethod.invoke(ColoredParticle.enumParticleClass, this.type.getName()), true, (float)this.location.getX(), (float)this.location.getY(), (float)this.location.getZ(), (float)this.color(this.offsetX), (float)this.color(this.offsetY), (float)this.color(this.offsetZ), 1.0f, 0, new int[0]));
    }
    
    private double color(double n) {
        n = ((n <= 0.0) ? -1.0 : n);
        return n / 255.0;
    }
    
    static {
        ColoredParticle.enumParticleClass = Reflection.getMinecraftClass("EnumParticle");
        ColoredParticle.particleConstructor = Reflection.getConstructor(Reflection.getMinecraftClass("PacketPlayOutWorldParticles"), ColoredParticle.enumParticleClass, Boolean.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE, int[].class);
        ColoredParticle.getParticleMethod = Reflection.getMethod(ColoredParticle.enumParticleClass, "a", String.class);
    }
    
    public enum ParticleType
    {
        SPELL_MOB("mobSpell"), 
        NOTE("note"), 
        REDSTONE("reddust");
        
        private String name;
        
        private ParticleType(final String name2) {
            this.name = name2;
        }
        
        public String getName() {
            return this.name;
        }
    }
}
