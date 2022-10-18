package wkimenai.mc-lights.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;

public class PluginFile
{
    private File file;
    private FileConfiguration fileConfig;
    private String fileName;
    private JavaPlugin plugin;
    
    public PluginFile(final JavaPlugin plugin, final String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.createFile();
    }
    
    private void createFile() {
        this.file = new File(this.plugin.getDataFolder(), this.fileName);
        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            this.plugin.saveResource(this.fileName, false);
        }
        this.fileConfig = (FileConfiguration)new YamlConfiguration();
        try {
            this.fileConfig.load(this.file);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public FileConfiguration getFileConfig() {
        return this.fileConfig;
    }
}
