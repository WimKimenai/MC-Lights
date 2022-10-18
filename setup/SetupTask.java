package wkimenai.mc-lights.setup;

import wkimenai.mc-lights.listener.task.StaffTask;
import wkimenai.mc-lights.listener.task.EffectTak;
import wkimenai.mc-lights.listener.task.LazerTask;
import org.bukkit.event.Listener;
import wkimenai.mc-lights.listener.task.GlobalTime;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import wkimenai.mc-lights.updater.Updater;
import org.bukkit.Bukkit;
import wkimenai.mc-lights.mc-lights;
import org.bukkit.plugin.PluginManager;

public class SetupTask
{
    private PluginManager pm;
    
    public SetupTask(final mc-lights mc-lights) {
        this.pm = Bukkit.getPluginManager();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)mc-lights, (Runnable)new Updater(mc-lights), 1L, 1L);
        this.pm.registerEvents((Listener)new GlobalTime(), (Plugin)mc-lights);
        this.pm.registerEvents((Listener)new LazerTask(), (Plugin)mc-lights);
        this.pm.registerEvents((Listener)new EffectTak(), (Plugin)mc-lights);
        this.pm.registerEvents((Listener)new StaffTask(), (Plugin)mc-lights);
    }
}
