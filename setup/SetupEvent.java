package wkimenai.mc-lights.setup;

import wkimenai.mc-lights.listener.events.MagicStickListener;
import wkimenai.mc-lights.listener.events.JoinEvent;
import wkimenai.mc-lights.listener.events.ProjectorEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import wkimenai.mc-lights.listener.events.EffectEvent;
import org.bukkit.Bukkit;
import wkimenai.mc-lights.mc-lights;
import org.bukkit.plugin.PluginManager;

public class SetupEvent
{
    private PluginManager pm;
    
    public SetupEvent(final mc-lights mc-lights) {
        (this.pm = Bukkit.getPluginManager()).registerEvents((Listener)new EffectEvent(), (Plugin)mc-lights);
        this.pm.registerEvents((Listener)new ProjectorEvent(), (Plugin)mc-lights);
        this.pm.registerEvents((Listener)new JoinEvent(), (Plugin)mc-lights);
        this.pm.registerEvents((Listener)new MagicStickListener(), (Plugin)mc-lights);
    }
}
