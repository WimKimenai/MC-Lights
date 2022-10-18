package wkimenai.mc-lights.dj;

import java.util.ArrayList;
import wkimenai.mc-lights.utils.CC;
import org.bukkit.Particle;
import org.bukkit.Color;
import java.util.List;

public class DJType
{
    private static List<DJType> djs;
    private String name;
    private Color armorColor;
    private Particle particle;
    private String headtexture;
    
    public DJType(final String name, final Color armorColor, final Particle particle, final String str) {
        this.name = name;
        this.armorColor = armorColor;
        this.particle = particle;
        this.headtexture = CC.headkey + str;
        DJType.djs.add(this);
    }
    
    public String getHeadtexture() {
        return this.headtexture;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Color getArmorColor() {
        return this.armorColor;
    }
    
    public Particle getParticle() {
        return this.particle;
    }
    
    public static List<DJType> getDjs() {
        return DJType.djs;
    }
    
    static {
        DJType.djs = new ArrayList<DJType>();
    }
}
