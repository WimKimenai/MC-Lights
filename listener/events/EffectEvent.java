package wkimenai.mc-lights.listener.events;

import java.util.Iterator;
import org.bukkit.Sound;
import wkimenai.mc-lights.dj.Dj;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventHandler;
import wkimenai.mc-lights.database.Database;
import wkimenai.mc-lights.effect.Effect;
import wkimenai.mc-lights.visual.Visual;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.Listener;

public class EffectEvent implements Listener
{
    private String id;
    
    public EffectEvent() {
        this.id = "919186";
    }
    
    @EventHandler
    public void breaklever(final BlockBreakEvent blockBreakEvent) {
        if (blockBreakEvent.getPlayer().hasPermission("pnc.admin") && (Visual.getVisuals().stream().filter(visual2 -> visual2.getLever() != null && visual2.getLever().equals((Object)blockBreakEvent.getBlock().getLocation())).findFirst().isPresent() || Effect.getEffects().stream().filter(effect2 -> effect2.getLever() != null && effect2.getLever().equals((Object)blockBreakEvent.getBlock().getLocation())).findFirst().isPresent())) {
            if (blockBreakEvent.getPlayer().isSneaking()) {
                Visual.getVisuals().stream().filter(visual3 -> visual3.getLever() != null && visual3.getLever().equals((Object)blockBreakEvent.getBlock().getLocation())).forEach(visual -> {
                    visual.stop();
                    Database.getEffect().remove("visual." + visual.getClass().getSimpleName().replace("Visual", ""));
                    return;
                });
                Effect.getEffects().stream().filter(effect3 -> effect3.getLever() != null && effect3.getLever().equals((Object)blockBreakEvent.getBlock().getLocation())).forEach(effect -> {
                    effect.stop();
                    Database.getEffect().remove("effect." + effect.getClass().getSimpleName().replace("Effect", "") + ".id." + effect.getCurrentID() + ".lever");
                });
            }
            else {
                blockBreakEvent.setCancelled(true);
                blockBreakEvent.getPlayer().sendMessage("§cIf you want to remove this lever you need to shift + break");
            }
        }
    }
    
    @EventHandler
    public void lever(final PlayerInteractEvent playerInteractEvent) {
        if (!playerInteractEvent.getPlayer().hasPermission("pnc.engineer")) {
            return;
        }
        if (playerInteractEvent.getClickedBlock() == null) {
            return;
        }
        if (playerInteractEvent.getClickedBlock().getType() == Material.LEVER) {
            if (Database.getGeneral().getDjLever() != null && Database.getGeneral().getDjLever().equals((Object)playerInteractEvent.getClickedBlock().getLocation())) {
                new Dj();
            }
            int n = 0;
            for (final Effect effect : Effect.getEffects()) {
                if (effect.getLever() != null && effect.getLever().equals((Object)playerInteractEvent.getClickedBlock().getLocation())) {
                    if (n == 0) {
                        n = 1;
                        playerInteractEvent.getPlayer().playSound(playerInteractEvent.getPlayer().getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
                    }
                    effect.start();
                }
            }
            Visual.getVisuals().stream().filter(visual -> visual.getLever() != null && visual.getLever().equals((Object)playerInteractEvent.getClickedBlock().getLocation())).findAny().ifPresent(visual2 -> {
                if (!visual2.isStarted()) {
                    playerInteractEvent.getPlayer().playSound(playerInteractEvent.getPlayer().getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
                    visual2.start();
                }
                else {
                    playerInteractEvent.getPlayer().playSound(playerInteractEvent.getPlayer().getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 0.0f);
                    visual2.stop();
                }
            });
        }
    }
}
