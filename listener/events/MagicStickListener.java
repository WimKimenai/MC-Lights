package wkimenai.mc-lights.listener.events;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import wkimenai.mc-lights.visual.Visual;
import wkimenai.mc-lights.listener.task.GlobalTime;
import wkimenai.mc-lights.area.Cuboid;
import wkimenai.mc-lights.area.AreaType;
import org.bukkit.Sound;
import wkimenai.mc-lights.database.Database;
import org.bukkit.event.block.Action;
import wkimenai.mc-lights.cmd.cmds.GeneralCMD;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.Listener;

public class MagicStickListener implements Listener
{
    private int point;
    
    public MagicStickListener() {
        this.point = 0;
    }
    
    @EventHandler
    public void click(final PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getItem() == null) {
            return;
        }
        if (playerInteractEvent.getClickedBlock() == null) {
            return;
        }
        if (playerInteractEvent.getItem().equals((Object)GeneralCMD.magicStick)) {
            playerInteractEvent.setCancelled(true);
            if (playerInteractEvent.getAction() == Action.LEFT_CLICK_BLOCK) {
                Database.getGeneral().setPointA(GeneralCMD.area, playerInteractEvent.getClickedBlock().getLocation());
                playerInteractEvent.getPlayer().sendMessage("§bPoint A saved !");
                playerInteractEvent.getPlayer().playSound(playerInteractEvent.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                ++this.point;
            }
            else if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Database.getGeneral().setPointB(GeneralCMD.area, playerInteractEvent.getClickedBlock().getLocation());
                playerInteractEvent.getPlayer().sendMessage("§bPoint B saved !");
                playerInteractEvent.getPlayer().playSound(playerInteractEvent.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                ++this.point;
            }
            if (this.point > 1) {
                playerInteractEvent.getPlayer().sendMessage("§bPoint B and A saved ! The  area is now created");
                if (GeneralCMD.area == AreaType.PACKET_AREA) {
                    GlobalTime.cuboid = new Cuboid(Database.getGeneral().getPointA(GeneralCMD.area), Database.getGeneral().getPointB(GeneralCMD.area));
                }
                else {
                    Visual.setVisualCuboid(new Cuboid(Database.getGeneral().getPointA(GeneralCMD.area), Database.getGeneral().getPointB(GeneralCMD.area)));
                }
                this.remove(playerInteractEvent.getPlayer());
            }
        }
    }
    
    @EventHandler
    public void quit(final PlayerQuitEvent playerQuitEvent) {
        this.remove(playerQuitEvent.getPlayer());
    }
    
    @EventHandler
    public void drop(final PlayerDropItemEvent playerDropItemEvent) {
        if (playerDropItemEvent.getItemDrop().getItemStack().equals((Object)GeneralCMD.magicStick)) {
            playerDropItemEvent.getItemDrop().remove();
            this.remove(playerDropItemEvent.getPlayer());
        }
    }
    
    private void remove(final Player player) {
        if (player.equals(GeneralCMD.editor)) {
            player.getInventory().setItem(0, GeneralCMD.old);
            GeneralCMD.editor = null;
            GeneralCMD.old = null;
            GeneralCMD.area = null;
            this.point = 0;
            player.sendMessage("§cYou are no longer in the editor mode");
        }
    }
}
