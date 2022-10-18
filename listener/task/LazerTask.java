package wkimenai.mc-lights.listener.task;

import org.bukkit.event.EventHandler;
import wkimenai.mc-lights.projector.projectors.LazerProjector;
import wkimenai.mc-lights.projector.projectors.EndProjector;
import wkimenai.mc-lights.updater.UpdateType;
import wkimenai.mc-lights.updater.event.UpdaterEvent;
import org.bukkit.event.Listener;

public class LazerTask implements Listener
{
    @EventHandler
    public void update(final UpdaterEvent updaterEvent) {
        if (updaterEvent.getType() == UpdateType.TICK) {
            EndProjector.getProjectors().forEach(endProjector -> {
                if (endProjector.isOn() && endProjector.getRotation() != null) {
                    endProjector.getRotation().rotate();
                }
                return;
            });
            LazerProjector.getLazerProjectors().forEach(lazerProjector -> {
                if (lazerProjector.isOn() && lazerProjector.getRotation() != null) {
                    lazerProjector.getRotation().rotate();
                }
            });
        }
    }
    
    @EventHandler
    public void update2(final UpdaterEvent updaterEvent) {
        if (updaterEvent.getType() == UpdateType.SEC) {
            LazerProjector.getLazerProjectors().forEach(lazerProjector -> {
                if (lazerProjector.isOn() && lazerProjector.getRotation() != null && lazerProjector.getLazer() != null) {
                    lazerProjector.setColorTimer(lazerProjector.getColorTimer() + 1);
                    if (lazerProjector.getColorTimer() >= 8) {
                        lazerProjector.getLazer().showLazer();
                        lazerProjector.setColorTimer(0);
                    }
                }
            });
        }
    }
}
