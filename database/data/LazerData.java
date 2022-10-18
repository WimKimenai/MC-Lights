package wkimenai.mc-lights.database.data;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.Iterator;
import org.bukkit.configuration.ConfigurationSection;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.Bukkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import wkimenai.mc-lights.projector.LeverActivator;
import wkimenai.mc-lights.projector.RotationType;
import org.bukkit.Location;
import java.util.NoSuchElementException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.List;
import wkimenai.mc-lights.projector.projectors.LazerProjector;
import wkimenai.mc-lights.database.Database;

public class LazerData extends Database
{
    public LazerData() {
        super("projector");
        Database.setLazer(this);
    }
    
    public void unregisterProjector(final int i) {
        this.remove("projectors." + i);
        final List list;
        LazerProjector.getLevers().forEach(leverActivator -> {
            leverActivator.getProjectors();
            if (list.contains(i)) {
                list.remove((Object)i);
                Database.getLazer().setData("levers." + leverActivator.getType().name() + "." + String.valueOf(leverActivator.getCurrentID()) + ".projectors", list);
            }
        });
    }
    
    public void registerNewProjector(final LazerProjector lazerProjector) {
        int n = 200;
        int size = 0;
        while (((FileConfiguration)YamlConfiguration.loadConfiguration(this.getFile())).isConfigurationSection("projectors." + String.valueOf(size))) {
            ++size;
            if (--n <= 0) {
                try {
                    size = LazerProjector.getLazerProjectors().size();
                }
                catch (NoSuchElementException ex) {}
                break;
            }
        }
        lazerProjector.setCurrentID(size);
        this.setData("projectors." + String.valueOf(size) + ".location", lazerProjector.getSupport());
        this.setData("projectors." + String.valueOf(size) + ".byte", lazerProjector.getPosition());
        LazerProjector.getLazerProjectors().add(lazerProjector);
    }
    
    public void registerLever(final Location location, final RotationType rotationType, final List<Integer> list) {
        int n = 100;
        int size = 0;
        while (((FileConfiguration)YamlConfiguration.loadConfiguration(this.getFile())).isConfigurationSection("levers." + rotationType.name() + "." + String.valueOf(size))) {
            ++size;
            if (--n <= 0) {
                try {
                    size = LazerProjector.getLevers().size();
                }
                catch (NoSuchElementException ex) {}
                break;
            }
        }
        this.setData("levers." + rotationType.name() + "." + String.valueOf(size) + ".location", location);
        this.setData("levers." + rotationType.name() + "." + String.valueOf(size) + ".projectors", list);
        final LeverActivator leverActivator = new LeverActivator(location, rotationType);
        leverActivator.setCurrentID(size);
        leverActivator.getProjectors().addAll(list);
        LazerProjector.getLevers().add(leverActivator);
    }
    
    public void registerLever(final Location location, final RotationType rotationType, final int n) {
        int n2 = 100;
        int size = 0;
        while (((FileConfiguration)YamlConfiguration.loadConfiguration(this.getFile())).isConfigurationSection("levers." + rotationType.name() + String.valueOf(size))) {
            ++size;
            if (--n2 <= 0) {
                try {
                    size = LazerProjector.getLevers().size();
                }
                catch (NoSuchElementException ex) {}
                break;
            }
        }
        this.setData("levers." + rotationType.name() + "." + String.valueOf(size) + ".location", location);
        this.setData("levers." + rotationType.name() + "." + String.valueOf(size) + ".projectors", new ArrayList(Arrays.asList(n)));
        final LeverActivator leverActivator = new LeverActivator(location, rotationType);
        leverActivator.setCurrentID(size);
        leverActivator.getProjectors().add(n);
        LazerProjector.getLevers().add(leverActivator);
    }
    
    public void deserializeData() {
        final File file = this.getFile();
        final YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(file);
        try {
            ((FileConfiguration)loadConfiguration).load(file);
            final ConfigurationSection configurationSection = ((FileConfiguration)loadConfiguration).getConfigurationSection("projectors");
            if (configurationSection != null) {
                for (final String str : configurationSection.getKeys(false)) {
                    final Location location = (Location)configurationSection.get(String.valueOf(str) + ".location", (Object)null);
                    final int intValue = (int)configurationSection.get(String.valueOf(str) + ".byte", (Object)null);
                    try {
                        final LazerProjector lazerProjector = new LazerProjector(location.getDirection(), location.getBlock(), (byte)intValue);
                        lazerProjector.setCurrentID(Integer.valueOf(str));
                        LazerProjector.getLazerProjectors().add(lazerProjector);
                    }
                    catch (IllegalArgumentException ex) {
                        Bukkit.getServer().getConsoleSender().sendMessage("§4Corrupted data found in projector.yml, the section projector §6" + str + " §4has been removed");
                        this.remove("projectors." + str);
                    }
                }
            }
            final ConfigurationSection configurationSection2 = ((FileConfiguration)loadConfiguration).getConfigurationSection("levers");
            if (configurationSection2 != null) {
                for (final String str2 : configurationSection2.getKeys(false)) {
                    final ConfigurationSection configurationSection3 = ((FileConfiguration)loadConfiguration).getConfigurationSection("levers." + str2);
                    for (final String s : configurationSection3.getKeys(false)) {
                        final Location location2 = (Location)configurationSection3.get("." + s + ".location", (Object)null);
                        final List integerList = configurationSection3.getIntegerList("." + s + ".projectors");
                        try {
                            final LeverActivator leverActivator = new LeverActivator(location2, RotationType.valueOf(str2));
                            leverActivator.setCurrentID(Integer.valueOf(s));
                            leverActivator.getProjectors().addAll(integerList);
                            LazerProjector.getLevers().add(leverActivator);
                        }
                        catch (IllegalArgumentException ex2) {
                            Bukkit.getServer().getConsoleSender().sendMessage("§4Corrupted data found in projector.yml, the section Lever §6" + str2 + " §4has been removed");
                            this.remove("levers." + str2);
                        }
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
