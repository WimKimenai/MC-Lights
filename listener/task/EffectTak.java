package wkimenai.mc-lights.listener.task;

import org.bukkit.event.EventHandler;
import wkimenai.mc-lights.dj.Dj;
import wkimenai.mc-lights.visual.Visual;
import wkimenai.mc-lights.effect.Effect;
import wkimenai.mc-lights.updater.UpdateType;
import wkimenai.mc-lights.updater.event.UpdaterEvent;
import org.bukkit.event.Listener;

public class EffectTak implements Listener
{
    @EventHandler
    public void update(final UpdaterEvent updaterEvent) {
        if (updaterEvent.getType() == UpdateType.TICK) {
            Effect.getEffects().stream().filter(effect -> effect.isStarted()).forEach(effect2 -> effect2.update());
            Visual.getVisuals().stream().filter(visual -> visual.isStarted()).forEach(visual2 -> visual2.update());
            if (Dj.getCurrentDj() != null) {
                Dj.getCurrentDj().update();
            }
        }
    }
    
    @EventHandler
    public void updateCho(final UpdaterEvent updaterEvent) {
        if (updaterEvent.getType() == UpdateType.FASTEST && Dj.getCurrentDj() != null) {
            Dj.getCurrentDj().updateChoreography();
        }
    }
    
    @EventHandler
    public void updateChoType(final UpdaterEvent updaterEvent) {
        if (updaterEvent.getType() == UpdateType.SLOWER && Dj.getCurrentDj() != null) {
            Dj.getCurrentDj().updateChoreographyType();
        }
    }
}
