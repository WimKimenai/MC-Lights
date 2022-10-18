package wkimenai.mc-lights.database;

import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import wkimenai.mc-lights.database.data.StaffData;
import wkimenai.mc-lights.database.data.GeneralData;
import wkimenai.mc-lights.database.data.LazerData;
import wkimenai.mc-lights.database.data.EffectData;

public abstract class Database
{
    private static EffectData effect;
    private static LazerData lazer;
    private static GeneralData general;
    private static StaffData staff;
    private String filename;
    
    public Database(final String filename) {
        this.filename = filename;
        this.getFile();
    }
    
    public void setData(final String s, final Object o) {
        final File file = this.getFile();
        final YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(file);
        try {
            ((FileConfiguration)loadConfiguration).load(file);
            ((FileConfiguration)loadConfiguration).set(s, o);
            ((FileConfiguration)loadConfiguration).save(file);
        }
        catch (InvalidConfigurationException | IOException ex) {
            final Throwable t;
            t.printStackTrace();
        }
    }
    
    public Object getData(final String s) {
        final File file = this.getFile();
        final YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(file);
        try {
            ((FileConfiguration)loadConfiguration).load(file);
            return ((FileConfiguration)loadConfiguration).get(s);
        }
        catch (InvalidConfigurationException | IOException ex) {
            final Throwable t;
            t.printStackTrace();
            return null;
        }
    }
    
    public void remove(final String s) {
        final File file = this.getFile();
        final YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(file);
        try {
            ((FileConfiguration)loadConfiguration).load(file);
            ((FileConfiguration)loadConfiguration).set(s, (Object)null);
            ((FileConfiguration)loadConfiguration).save(file);
        }
        catch (InvalidConfigurationException | IOException ex) {
            final Throwable t;
            t.printStackTrace();
        }
    }
    
    public File getFile() {
        final File file = new File("plugins/mc-lights/data", String.valueOf(this.filename) + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return file;
    }
    
    public static void setEffect(final EffectData effect) {
        Database.effect = effect;
    }
    
    public static void setLazer(final LazerData lazer) {
        Database.lazer = lazer;
    }
    
    public static void setGeneral(final GeneralData general) {
        Database.general = general;
    }
    
    public static void setStaff(final StaffData staff) {
        Database.staff = staff;
    }
    
    public static EffectData getEffect() {
        return Database.effect;
    }
    
    public static LazerData getLazer() {
        return Database.lazer;
    }
    
    public static GeneralData getGeneral() {
        return Database.general;
    }
    
    public static StaffData getStaff() {
        return Database.staff;
    }
}
