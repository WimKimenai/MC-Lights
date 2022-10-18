package wkimenai.mc-lights;

import java.net.URLConnection;
import java.io.IOException;
import wkimenai.mc-lights.staff.Staff;
import wkimenai.mc-lights.dj.Dj;
import wkimenai.mc-lights.database.Database;
import wkimenai.mc-lights.visual.Visual;
import wkimenai.mc-lights.effect.Effect;
import wkimenai.mc-lights.projector.projectors.LazerProjector;
import org.bukkit.command.ConsoleCommandSender;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import wkimenai.mc-lights.nms.disguise.DisguiseListener;
import org.bukkit.plugin.Plugin;
import wkimenai.mc-lights.setup.SetupCommand;
import wkimenai.mc-lights.setup.SetupEvent;
import wkimenai.mc-lights.setup.SetupTask;
import wkimenai.mc-lights.setup.SetupDjs;
import wkimenai.mc-lights.setup.SetupDatabase;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.configuration.InvalidConfigurationException;
import java.io.File;
import wkimenai.mc-lights.utils.CC;
import org.bukkit.Bukkit;
import wkimenai.mc-lights.config.mc-lightsConfig;
import wkimenai.mc-lights.utils.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class mc-lights extends JavaPlugin
{
    private static mc-lights instance;
    private String id;
    private Metrics metrics;
    private boolean old;
    private mc-lightsConfig config;
    
    public mc-lights() {
        this.id = "919186";
        this.old = false;
    }
    
    public void onEnable() {
        loadConfig0();
        final ConsoleCommandSender consoleSender = Bukkit.getServer().getConsoleSender();
        mc-lights.instance = this;
        this.metrics = new Metrics(this);
        consoleSender.sendMessage(String.valueOf(CC.aqua) + "DMX LIGHTS PLUGIN ACTIVATED");
        try {
            (this.config = new mc-lightsConfig(new File("plugins/mc-lights", "mc-lights.yml"))).load();
        }
        catch (InvalidConfigurationException ex) {
            ex.printStackTrace();
        }
        new BukkitRunnable() {
            public void run() {
                new SetupDatabase(mc-lights.getInstance());
                new SetupDjs();
                new SetupTask(mc-lights.getInstance());
                new SetupEvent(mc-lights.getInstance());
                new SetupCommand();
            }
        }.runTaskLater((Plugin)this, 100L);
        new DisguiseListener((Plugin)this);
        try {
            final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("https://www.spigotmc.org/api/general.php").openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.getOutputStream().write("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=58234".getBytes("UTF-8"));
            final String line = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())).readLine();
            if (line.length() <= 7 && !line.equals(this.getDescription().getVersion())) {
                this.old = true;
                consoleSender.sendMessage("§b A new version of mc-lights is available here: ");
                consoleSender.sendMessage("§3 https://www.spigotmc.org/resources/mc-lights.58234/");
            }
        }
        catch (Exception ex2) {}
    }
    
    public void onDisable() {
        LazerProjector.getLazerProjectors().forEach(lazerProjector -> lazerProjector.removeProjector());
        Effect.getEffects().forEach(effect -> effect.stop());
        Effect.getEffects().clear();
        Visual.getVisuals().forEach(visual -> visual.stop());
        Visual.getVisuals().clear();
        Database.getStaff().getStaffList().forEach(staff -> staff.despawn());
        if (Dj.getCurrentDj() != null) {
            Dj.getCurrentDj().removeDj();
        }
    }
    
    public boolean isOld() {
        return this.old;
    }
    
    public void setOld(final boolean old) {
        this.old = old;
    }
    
    public mc-lightsConfig getProdigyConfig() {
        return this.config;
    }
    
    public static mc-lights getInstance() {
        return mc-lights.instance;
    }
}
