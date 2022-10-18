package wkimenai.mc-lights.database.data;

import org.bukkit.configuration.file.FileConfiguration;
import wkimenai.mc-lights.visual.Visual;
import wkimenai.mc-lights.listener.task.GlobalTime;
import wkimenai.mc-lights.area.Cuboid;
import org.bukkit.configuration.file.YamlConfiguration;
import wkimenai.mc-lights.area.AreaType;
import org.bukkit.Location;
import wkimenai.mc-lights.database.Database;

public class GeneralData extends Database
{
    private Location djLocation;
    private Location djLever;
    private Location visualA;
    private Location visualB;
    private Location pointA;
    private Location pointB;
    
    public GeneralData() {
        super("general");
        Database.setGeneral(this);
    }
    
    public void setPointA(final AreaType areaType, final Location location) {
        this.setData(String.valueOf(areaType.name()) + "A.location", location);
        if (areaType == AreaType.PACKET_AREA) {
            this.pointA = location;
        }
        else {
            this.visualA = location;
        }
    }
    
    public void setPointB(final AreaType areaType, final Location location) {
        this.setData(String.valueOf(areaType.name()) + "B.location", location);
        if (areaType == AreaType.PACKET_AREA) {
            this.pointB = location;
        }
        else {
            this.visualB = location;
        }
    }
    
    public void setDjLocation(final Location djLocation) {
        this.setData("dj.location", djLocation);
        this.djLocation = djLocation;
    }
    
    public void setDjLever(final Location djLever) {
        this.setData("djlever.location", djLever);
        this.djLever = djLever;
    }
    
    public void registerData() {
        final YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(this.getFile());
        AreaType[] values;
        for (int length = (values = AreaType.values()).length, i = 0; i < length; ++i) {
            final AreaType areaType = values[i];
            final Location location = (Location)((FileConfiguration)loadConfiguration).get(String.valueOf(areaType.name()) + "A.location");
            final Location location2 = (Location)((FileConfiguration)loadConfiguration).get(String.valueOf(areaType.name()) + "B.location");
            if (location != null && location2 != null) {
                if (areaType == AreaType.PACKET_AREA) {
                    GlobalTime.cuboid = new Cuboid(location, location2);
                }
                else if (areaType == AreaType.VISUAL_AREA) {
                    Visual.setVisualCuboid(new Cuboid(location, location2));
                }
            }
        }
        final Location djLocation = (Location)((FileConfiguration)loadConfiguration).get("dj.location");
        final Location djLever = (Location)((FileConfiguration)loadConfiguration).get("djlever.location");
        this.djLocation = djLocation;
        this.djLever = djLever;
    }
    
    public Location getDjLocation() {
        return this.djLocation;
    }
    
    public Location getPointA(final AreaType areaType) {
        return (areaType == AreaType.PACKET_AREA) ? this.pointA : this.visualA;
    }
    
    public Location getPointB(final AreaType areaType) {
        return (areaType == AreaType.PACKET_AREA) ? this.pointB : this.visualB;
    }
    
    public Location getDjLever() {
        return this.djLever;
    }
}
