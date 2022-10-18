package wkimenai.mc-lights.database.data;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.Iterator;
import org.bukkit.configuration.ConfigurationSection;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.Bukkit;
import wkimenai.mc-lights.nms.Reflection;
import org.bukkit.Location;
import java.util.NoSuchElementException;
import org.bukkit.configuration.file.YamlConfiguration;
import wkimenai.mc-lights.visual.Visual;
import wkimenai.mc-lights.effect.Effect;
import wkimenai.mc-lights.database.Database;

public class EffectData extends Database
{
    public EffectData() {
        super("effect");
        Database.setEffect(this);
    }
    
    public void unregisterEffect(final Effect effect) {
        this.remove("effect." + effect.getClass().getSimpleName().replace("Effect", "") + ".id." + effect.getCurrentID());
    }
    
    public void registerVisual(final Visual visual) {
        this.setData("visual." + visual.getClass().getSimpleName().replace("Visual", "") + ".lever", visual.getLever());
        Visual.getVisuals().add(visual);
    }
    
    public void unregisterVisual(final Effect effect) {
        this.remove("visual." + effect.getClass().getSimpleName().replace("Visual", ""));
    }
    
    public void registerNewEffect(final Effect effect) {
        int n = 100;
        int length = 0;
        while (((FileConfiguration)YamlConfiguration.loadConfiguration(this.getFile())).isConfigurationSection("effect." + effect.getClass().getSimpleName().replace("Effect", "") + ".id." + String.valueOf(length))) {
            ++length;
            if (--n <= 0) {
                try {
                    length = Effect.getEffects().stream().filter(effect2 -> effect2.getClass().getSimpleName().equalsIgnoreCase(effect.getClass().getSimpleName())).toArray().length;
                }
                catch (NoSuchElementException ex) {}
                break;
            }
        }
        effect.setCurrentID(length);
        effect.serialize();
        Effect.getEffects().add(effect);
    }
    
    public void deserializeData() {
        final File file = this.getFile();
        final YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(file);
        try {
            ((FileConfiguration)loadConfiguration).load(file);
            final ConfigurationSection configurationSection = ((FileConfiguration)loadConfiguration).getConfigurationSection("effect");
            if (configurationSection != null) {
                for (final String s : configurationSection.getKeys(false)) {
                    for (final String s2 : ((FileConfiguration)loadConfiguration).getConfigurationSection("effect." + s + ".id").getKeys(false)) {
                        final Location location = (Location)configurationSection.get(String.valueOf(s) + ".id." + s2 + ".location", (Object)null);
                        final Location lever = (Location)configurationSection.get(String.valueOf(s) + ".id." + s2 + ".lever", (Object)null);
                        try {
                            final Effect effect = (Effect)Reflection.getConstructor("wkimenai.mc-lights.effect.effects." + s + "Effect", Location.class).invoke(location);
                            effect.setCurrentID(Integer.valueOf(s2));
                            effect.setLever(lever);
                            effect.deserialize();
                            Effect.getEffects().add(effect);
                        }
                        catch (IllegalArgumentException ex) {
                            Bukkit.getServer().getConsoleSender().sendMessage("§4Corrupted data found in effect.yml, the section §6" + s + " §4has been removed");
                            this.remove("effect." + s);
                        }
                    }
                }
            }
            final ConfigurationSection configurationSection2 = ((FileConfiguration)loadConfiguration).getConfigurationSection("visual");
            if (configurationSection2 != null) {
                for (final String s3 : configurationSection2.getKeys(false)) {
                    try {
                        final Location lever2 = (Location)configurationSection2.get(String.valueOf(s3) + ".lever", (Object)null);
                        final Visual visual = (Visual)Reflection.getConstructor("wkimenai.mc-lights.visual.visuals." + s3 + "Visual", (Class<?>[])new Class[0]).invoke(new Object[0]);
                        visual.setLever(lever2);
                        Visual.getVisuals().add(visual);
                    }
                    catch (IllegalArgumentException ex2) {
                        Bukkit.getServer().getConsoleSender().sendMessage("§4Corrupted data found in effect.yml, the section §6" + s3 + " §4has been removed");
                        this.remove("visual." + s3);
                    }
                }
            }
        }
        catch (InvalidConfigurationException | IOException ex3) {
            final Throwable t;
            t.printStackTrace();
        }
    }
}
