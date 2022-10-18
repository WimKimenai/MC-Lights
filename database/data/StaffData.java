package wkimenai.mc-lights.database.data;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.Iterator;
import org.bukkit.configuration.ConfigurationSection;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import wkimenai.mc-lights.staff.teams.Barman;
import org.bukkit.Location;
import java.util.NoSuchElementException;
import org.bukkit.configuration.file.YamlConfiguration;
import wkimenai.mc-lights.staff.teams.Guard;
import java.util.ArrayList;
import wkimenai.mc-lights.staff.Staff;
import java.util.List;
import wkimenai.mc-lights.database.Database;

public class StaffData extends Database
{
    private List<Staff> staff;
    
    public StaffData() {
        super("staff");
        this.staff = new ArrayList<Staff>();
        Database.setStaff(this);
    }
    
    public void setConstant(final Staff staff, final boolean b) {
        this.setData("staff." + staff.getClass().getSimpleName() + ".id." + String.valueOf(staff.getCurrentID()) + ".constant", b);
        staff.setConstant(b);
    }
    
    public void setGuardPermission(final Guard guard, final String perm) {
        if (((FileConfiguration)YamlConfiguration.loadConfiguration(this.getFile())).isConfigurationSection("staff.Guard.id." + String.valueOf(guard.getCurrentID()))) {
            this.setData("staff.Guard.id." + String.valueOf(guard.getCurrentID()) + ".permission", perm);
            guard.setPerm(perm);
        }
    }
    
    public void setGuardProtectionRadius(final Guard guard, final int i) {
        if (((FileConfiguration)YamlConfiguration.loadConfiguration(this.getFile())).isConfigurationSection("staff.Guard.id." + String.valueOf(guard.getCurrentID()))) {
            this.setData("staff.Guard.id." + String.valueOf(guard.getCurrentID()) + ".radius", i);
            guard.setRadius(i);
        }
    }
    
    public void unregisterNewStaffMember(final Staff staff) {
        this.remove("staff." + staff.getClass().getSimpleName() + ".id." + staff.getCurrentID());
    }
    
    public int registerNewStaffMember(final Staff staff) {
        int n = 50;
        int length = 0;
        while (((FileConfiguration)YamlConfiguration.loadConfiguration(this.getFile())).isConfigurationSection("staff." + staff.getClass().getSimpleName() + ".id." + String.valueOf(length))) {
            ++length;
            if (--n <= 0) {
                try {
                    length = this.staff.stream().filter(staff2 -> staff2.getClass().getSimpleName().equalsIgnoreCase(staff.getClass().getSimpleName())).toArray().length;
                }
                catch (NoSuchElementException ex) {}
                break;
            }
        }
        this.setData("staff." + staff.getClass().getSimpleName() + ".id." + String.valueOf(length) + ".location", staff.getLocation());
        this.setData("staff." + staff.getClass().getSimpleName() + ".id." + String.valueOf(length) + ".constant", staff.isConstant());
        this.staff.add(staff);
        return length;
    }
    
    public void deserializeData() {
        final File file = this.getFile();
        final YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(file);
        try {
            ((FileConfiguration)loadConfiguration).load(file);
            final ConfigurationSection configurationSection = ((FileConfiguration)loadConfiguration).getConfigurationSection("staff");
            if (configurationSection != null) {
                for (final String s : configurationSection.getKeys(false)) {
                    if (s.equalsIgnoreCase("Guard")) {
                        for (final String s2 : ((FileConfiguration)loadConfiguration).getConfigurationSection("staff.Guard.id").getKeys(false)) {
                            final Location location = (Location)configurationSection.get("Guard.id." + s2 + ".location", (Object)null);
                            final Object value = configurationSection.get("Guard.id." + s2 + ".radius", (Object)2);
                            final String perm = (String)configurationSection.get("Guard.id." + s2 + ".permission", (Object)null);
                            final boolean booleanValue = (boolean)configurationSection.get("Guard.id." + s2 + ".constant", (Object)false);
                            final Guard guard = new Guard(location);
                            guard.setCurrentID(Integer.valueOf(s2));
                            guard.setPerm(perm);
                            guard.setConstant(booleanValue);
                            guard.setRadius((int)value);
                            guard.spawn();
                            this.staff.add(guard);
                        }
                    }
                    else {
                        if (!s.equalsIgnoreCase("Barman")) {
                            continue;
                        }
                        for (final String s3 : ((FileConfiguration)loadConfiguration).getConfigurationSection("staff.Barman.id").getKeys(false)) {
                            final Location location2 = (Location)configurationSection.get("Barman.id." + s3 + ".location", (Object)null);
                            final boolean booleanValue2 = (boolean)configurationSection.get("Barman.id." + s3 + ".constant", (Object)false);
                            final Barman barman = new Barman(location2);
                            barman.setCurrentID(Integer.valueOf(s3));
                            barman.setConstant(booleanValue2);
                            barman.spawn();
                            this.staff.add(barman);
                        }
                    }
                }
            }
        }
        catch (InvalidConfigurationException | IOException ex) {
            final Throwable t;
            t.printStackTrace();
        }
    }
    
    public List<Staff> getStaffList() {
        return this.staff;
    }
}
