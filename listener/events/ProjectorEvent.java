package wkimenai.mc-lights.listener.events;

import wkimenai.mc-lights.projector.RotationType;
import org.bukkit.event.player.PlayerQuitEvent;
import wkimenai.mc-lights.projector.rotations.RandomFastRotation;
import wkimenai.mc-lights.projector.rotations.SpeedRegularRotation;
import wkimenai.mc-lights.projector.rotations.RandomRotation;
import wkimenai.mc-lights.projector.ProjectorRotation;
import wkimenai.mc-lights.projector.Projector;
import wkimenai.mc-lights.projector.rotations.SlowRotation;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.Sound;
import wkimenai.mc-lights.cmd.cmds.ProjectorCMD;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventHandler;
import java.util.NoSuchElementException;
import wkimenai.mc-lights.database.Database;
import wkimenai.mc-lights.projector.projectors.LazerProjector;
import wkimenai.mc-lights.projector.LeverActivator;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.Listener;

public class ProjectorEvent implements Listener
{
    @EventHandler
    public void breaklever(final BlockBreakEvent blockBreakEvent) {
        if (blockBreakEvent.getPlayer().hasPermission("pnc.admin")) {
            try {
                final LeverActivator leverActivator2 = LazerProjector.getLevers().stream().filter(leverActivator -> leverActivator.getL() != null && leverActivator.getL().equals((Object)blockBreakEvent.getBlock().getLocation())).findFirst().get();
                if (blockBreakEvent.getPlayer().isSneaking()) {
                    Database.getLazer().remove("levers." + leverActivator2.getType().name() + "." + leverActivator2.getCurrentID());
                    LazerProjector.getLevers().remove(leverActivator2);
                    blockBreakEvent.getPlayer().sendMessage("§cAll registered projector to this lever are now removed !");
                }
                else {
                    blockBreakEvent.setCancelled(true);
                    blockBreakEvent.getPlayer().sendMessage("§cIf you want to remove this lever you need to shift + break");
                }
            }
            catch (NoSuchElementException ex) {}
        }
    }
    
    @EventHandler
    public void place(final BlockPlaceEvent blockPlaceEvent) {
        if (blockPlaceEvent.getPlayer().hasPermission("pnc.admin")) {
            if (ProjectorCMD.place != null && blockPlaceEvent.getItemInHand().equals((Object)ProjectorCMD.magicStick) && blockPlaceEvent.getPlayer().equals(ProjectorCMD.place)) {
                final LazerProjector lazerProjector = new LazerProjector(blockPlaceEvent.getPlayer().getEyeLocation().toVector().subtract(blockPlaceEvent.getBlockPlaced().getLocation().add(0.5, 0.5, 0.5).toVector()).normalize(), blockPlaceEvent.getBlockPlaced(), blockPlaceEvent.getBlockPlaced().getData());
                Database.getLazer().registerNewProjector(lazerProjector);
                blockPlaceEvent.getPlayer().sendMessage("§aProjector " + lazerProjector.getCurrentID() + " has been placed and selected !");
                lazerProjector.getProjector().setCustomName("§b" + lazerProjector.getCurrentID());
                blockPlaceEvent.getPlayer().playSound(blockPlaceEvent.getPlayer().getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1.0f, 0.0f);
                ProjectorCMD.selectedProjectors.add(lazerProjector.getCurrentID());
            }
            if (blockPlaceEvent.getItemInHand().equals((Object)ProjectorCMD.lever)) {
                blockPlaceEvent.getPlayer().getInventory().setItem(0, ProjectorCMD.old);
                if (ProjectorCMD.currentRotationLever == null) {
                    blockPlaceEvent.getPlayer().sendMessage("§cError: something goes wrong, please use /pnc projector lever <type> again");
                    return;
                }
                if (ProjectorCMD.selectedProjectors.isEmpty()) {
                    blockPlaceEvent.getPlayer().sendMessage("§cError: something goes wrong, please use /pnc projector select");
                    return;
                }
                Database.getLazer().registerLever(blockPlaceEvent.getBlockPlaced().getLocation(), ProjectorCMD.currentRotationLever, ProjectorCMD.selectedProjectors);
                blockPlaceEvent.getPlayer().sendMessage("§aAll projectors are still selected, use /pnc projector forget or place lever again with /pnc projector lever <type> ");
            }
        }
    }
    
    @EventHandler
    public void select(final PlayerInteractAtEntityEvent playerInteractAtEntityEvent) {
        if (!playerInteractAtEntityEvent.getPlayer().hasPermission("pnc.admin")) {
            return;
        }
        if (LazerProjector.getLazerProjectors().stream().filter(lazerProjector -> lazerProjector.getProjector().equals(playerInteractAtEntityEvent.getRightClicked())).findAny().isPresent()) {
            playerInteractAtEntityEvent.setCancelled(true);
        }
        if (ProjectorCMD.selector != null && playerInteractAtEntityEvent.getPlayer().equals(ProjectorCMD.selector)) {
            LazerProjector.getLazerProjectors().stream().filter(lazerProjector2 -> lazerProjector2.getProjector().equals(playerInteractAtEntityEvent.getRightClicked())).findAny().ifPresent(lazerProjector3 -> {
                if (ProjectorCMD.selectedProjectors.contains(lazerProjector3.getCurrentID())) {
                    playerInteractAtEntityEvent.getPlayer().sendMessage("§cProjector " + lazerProjector3.getCurrentID() + " is now unselected !");
                    lazerProjector3.getProjector().setCustomName("§f" + lazerProjector3.getCurrentID());
                    ProjectorCMD.selectedProjectors.remove((Object)lazerProjector3.getCurrentID());
                }
                else {
                    playerInteractAtEntityEvent.getPlayer().sendMessage("§aProjector " + lazerProjector3.getCurrentID() + " is now selected !");
                    ProjectorCMD.selectedProjectors.add(lazerProjector3.getCurrentID());
                    lazerProjector3.getProjector().setCustomName("§b" + lazerProjector3.getCurrentID());
                    playerInteractAtEntityEvent.getPlayer().playSound(playerInteractAtEntityEvent.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                }
                return;
            });
        }
        if (ProjectorCMD.remove != null && playerInteractAtEntityEvent.getPlayer().equals(ProjectorCMD.remove)) {
            try {
                final LazerProjector lazerProjector5 = LazerProjector.getLazerProjectors().stream().filter(lazerProjector4 -> lazerProjector4.getProjector().equals(playerInteractAtEntityEvent.getRightClicked())).findAny().get();
                ProjectorCMD.selectedProjectors.remove((Object)lazerProjector5.getCurrentID());
                Database.getLazer().unregisterProjector(lazerProjector5.getCurrentID());
                lazerProjector5.removeProjector();
                LazerProjector.getLazerProjectors().remove(lazerProjector5);
                playerInteractAtEntityEvent.getPlayer().sendMessage("§aProjector " + lazerProjector5.getCurrentID() + " is now removed !");
            }
            catch (NoSuchElementException ex) {}
        }
    }
    
    @EventHandler
    public void lever(final PlayerInteractEvent playerInteractEvent) {
        if (!playerInteractEvent.getPlayer().hasPermission("pnc.engineer")) {
            return;
        }
        if (playerInteractEvent.getClickedBlock() == null && playerInteractEvent.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        if (playerInteractEvent.getClickedBlock().getType() == Material.LEVER) {
            LazerProjector.getLevers().stream().filter(leverActivator2 -> leverActivator2.getL() != null && leverActivator2.getL().equals((Object)playerInteractEvent.getClickedBlock().getLocation())).findFirst().ifPresent(leverActivator -> leverActivator.getProjectors().forEach(n -> LazerProjector.getLazerProjectors().stream().filter(lazerProjector -> lazerProjector.getCurrentID() == n).findFirst().ifPresent(lazerProjector2 -> {
                if (!lazerProjector2.isOn()) {
                    lazerProjector2.setOn();
                    switch (leverActivator.getType()) {
                        case SLOW: {
                            lazerProjector2.setRotation(new SlowRotation(lazerProjector2));
                            break;
                        }
                        case RANDOM: {
                            lazerProjector2.setRotation(new RandomRotation(lazerProjector2));
                            break;
                        }
                        case SPEEDREGULAR: {
                            lazerProjector2.setRotation(new SpeedRegularRotation(lazerProjector2));
                            break;
                        }
                        case RANDOMFAST: {
                            lazerProjector2.setRotation(new RandomFastRotation(lazerProjector2));
                            break;
                        }
                    }
                }
                else {
                    lazerProjector2.setOff();
                }
            })));
        }
    }
    
    @EventHandler
    public void quit(final PlayerQuitEvent playerQuitEvent) {
        if (playerQuitEvent.getPlayer().equals(ProjectorCMD.selector)) {
            ProjectorCMD.selector = null;
            ProjectorCMD.selectedProjectors.clear();
            LazerProjector.getLazerProjectors().forEach(lazerProjector -> {
                lazerProjector.getProjector().setCustomName("");
                lazerProjector.getProjector().setCustomNameVisible(false);
                return;
            });
        }
        if (playerQuitEvent.getPlayer().equals(ProjectorCMD.remove)) {
            ProjectorCMD.remove = null;
            ProjectorCMD.selectedProjectors.clear();
        }
        if (playerQuitEvent.getPlayer().equals(ProjectorCMD.place)) {
            ProjectorCMD.place = null;
            ProjectorCMD.selectedProjectors.clear();
            playerQuitEvent.getPlayer().getInventory().setItem(0, ProjectorCMD.old);
        }
        if (playerQuitEvent.getPlayer().getInventory().getItem(0) != null && playerQuitEvent.getPlayer().getInventory().getItem(0).equals((Object)ProjectorCMD.lever)) {
            playerQuitEvent.getPlayer().getInventory().setItem(0, ProjectorCMD.old);
        }
    }
}
