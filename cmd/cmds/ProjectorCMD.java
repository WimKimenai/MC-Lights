package wkimenai.mc-lights.cmd.cmds;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import java.util.NoSuchElementException;
import wkimenai.mc-lights.utils.CC;
import wkimenai.mc-lights.projector.LeverActivator;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import wkimenai.mc-lights.database.Database;
import wkimenai.mc-lights.projector.projectors.LazerProjector;
import org.bukkit.Sound;
import wkimenai.mc-lights.listener.task.GlobalTime;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import wkimenai.mc-lights.utils.UtilItem;
import org.bukkit.Material;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import wkimenai.mc-lights.projector.RotationType;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandExecutor;

public class ProjectorCMD implements CommandExecutor
{
    public static Player selector;
    public static Player place;
    public static Player remove;
    public static RotationType currentRotationLever;
    public static ItemStack old;
    public static ItemStack magicStick;
    public static ItemStack lever;
    public static List<Integer> selectedProjectors;
    
    static {
        ProjectorCMD.magicStick = new UtilItem(Material.END_ROD, (byte)0, "§5Magic Stick (Place it where you want a projector )").getItem();
        ProjectorCMD.lever = new UtilItem(Material.LEVER, (byte)0, "§5Magic Lever (Place it where you want)").getItem();
        ProjectorCMD.magicStick.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 0);
        final ItemMeta itemMeta = ProjectorCMD.magicStick.getItemMeta();
        itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        ProjectorCMD.magicStick.setItemMeta(itemMeta);
        ProjectorCMD.selectedProjectors = new ArrayList<Integer>();
    }
    
    private void displayTypes(final Player player) {
        String string = "";
        for (int i = 0; i < RotationType.values().length; ++i) {
            string = String.valueOf(string) + RotationType.values()[i] + ((i == RotationType.values().length - 1) ? "." : ", ");
        }
        player.sendMessage(string);
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (commandSender instanceof Player) {
            final Player player = (Player)commandSender;
            if (array.length > 0) {
                if (!player.hasPermission("light.admin")) {
                    player.sendMessage("§cYou do not have the permission !");
                    return false;
                }
                if (array[0].equalsIgnoreCase("projector")) {
                    if (GlobalTime.cuboid == null) {
                        player.sendMessage("§cYou need to cuboid the nightclub's area first by using /light setup area");
                        return false;
                    }
                    if (array.length == 1) {
                        player.sendMessage("§f* §bWelcome to the command projector setup system §f*");
                        player.sendMessage(" Place projectors (toggle mode):");
                        player.sendMessage(" §b/light projector place");
                        player.sendMessage(" Place lever:");
                        player.sendMessage(" §b/light projector lever <type>");
                        this.displayTypes(player);
                        player.sendMessage("Select projectors (toggle mode):");
                        player.sendMessage(" §b/light projector select");
                        player.sendMessage("Forget all selected projectors:");
                        player.sendMessage(" §b/light projector forget");
                        player.sendMessage("Remove projectors (toggle mode):");
                        player.sendMessage(" §b/light projector remove");
                        player.sendMessage("Reset projectors and levers:");
                        player.sendMessage(" §b/light projector reset");
                        player.sendMessage("Display your enabled modes:");
                        player.sendMessage(" §b/light projector mode");
                        player.sendMessage("Transfer any projector to a lever:");
                        player.sendMessage(" §b/light projector transfer <id>");
                        player.sendMessage("Transfer any projector to a new lever:");
                        player.sendMessage(" §b/light projector transfer <id> <type>");
                    }
                    else if (array.length == 2) {
                        if (array[1].equalsIgnoreCase("select")) {
                            if (ProjectorCMD.selector != null) {
                                if (ProjectorCMD.selector.equals(player)) {
                                    ProjectorCMD.selector = null;
                                    player.sendMessage("§cYou are no longer in select mode !");
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.0f);
                                    LazerProjector.getLazerProjectors().forEach(lazerProjector -> {
                                        lazerProjector.getProjector().setCustomName("");
                                        lazerProjector.getProjector().setCustomNameVisible(false);
                                        return;
                                    });
                                }
                                else {
                                    player.sendMessage("§cSomeone else (" + ProjectorCMD.selector.getName() + ") is already using the select mode");
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.0f);
                                }
                                return false;
                            }
                            (ProjectorCMD.selector = player).sendMessage("§aYou are now in select mode !");
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                            String customName;
                            final ArmorStand armorStand;
                            LazerProjector.getLazerProjectors().forEach(lazerProjector2 -> {
                                lazerProjector2.getProjector();
                                if (ProjectorCMD.selectedProjectors.contains(lazerProjector2.getCurrentID())) {
                                    customName = "§b" + lazerProjector2.getCurrentID();
                                }
                                else {
                                    customName = "§f" + lazerProjector2.getCurrentID();
                                }
                                armorStand.setCustomName(customName);
                                lazerProjector2.getProjector().setCustomNameVisible(true);
                                return;
                            });
                        }
                        else if (array[1].equalsIgnoreCase("forget")) {
                            player.sendMessage("§cAll projector previously selected are now removed !");
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                            LazerProjector.getLazerProjectors().forEach(lazerProjector3 -> {
                                lazerProjector3.getProjector().setCustomName("§f" + lazerProjector3.getCurrentID());
                                lazerProjector3.getProjector().setCustomNameVisible(true);
                                return;
                            });
                            ProjectorCMD.selectedProjectors.clear();
                        }
                        else if (array[1].equalsIgnoreCase("reset")) {
                            Database.getLazer().remove("projectors");
                            Database.getLazer().remove("levers");
                            LazerProjector.getLazerProjectors().forEach(lazerProjector4 -> lazerProjector4.removeProjector());
                            LazerProjector.getLevers().clear();
                            player.sendMessage("§cAll lazers and levers are now removed !");
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        }
                        else if (array[1].equalsIgnoreCase("place")) {
                            if (ProjectorCMD.place != null) {
                                if (ProjectorCMD.place.equals(player)) {
                                    player.getInventory().setItem(0, ProjectorCMD.old);
                                    ProjectorCMD.old = null;
                                    ProjectorCMD.place = null;
                                    player.sendMessage("§cYou are no longer in place mode !");
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.0f);
                                }
                                else {
                                    player.sendMessage("§cSomeone else (" + ProjectorCMD.place.getName() + ") is already using the place mode");
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.0f);
                                }
                                return false;
                            }
                            ProjectorCMD.old = player.getInventory().getItem(0);
                            player.getInventory().setItem(0, ProjectorCMD.magicStick);
                            (ProjectorCMD.place = player).sendMessage("§aYou are now in place mode !");
                            player.sendMessage("§bYou can now place each projector you want where you want by right-clicking with the magic stick");
                            player.sendMessage("§bAfter that just use /light projector lever <type>");
                            this.displayTypes(player);
                            player.sendMessage("§bYou will receive a lever able to perform projectors previously selected !");
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        }
                        else if (array[1].equalsIgnoreCase("remove")) {
                            if (ProjectorCMD.remove != null) {
                                if (ProjectorCMD.remove.equals(player)) {
                                    ProjectorCMD.remove = null;
                                    player.sendMessage("§cYou are no longer in remove mode !");
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.0f);
                                }
                                else {
                                    player.sendMessage("§cSomeone else (" + ProjectorCMD.remove.getName() + ") is already using the remove mode");
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.0f);
                                }
                                return false;
                            }
                            (ProjectorCMD.remove = player).sendMessage("§aYou are now in remove mode !");
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        }
                        else if (array[1].equalsIgnoreCase("mode")) {
                            player.sendMessage("§bYour enabled modes: ");
                            player.sendMessage("Select: " + ((ProjectorCMD.selector != null && ProjectorCMD.selector.equals(player)) ? "§2Enabled" : "§4Disabled"));
                            player.sendMessage("Remove: " + ((ProjectorCMD.remove != null && ProjectorCMD.remove.equals(player)) ? "§2Enabled" : "§4Disabled"));
                            player.sendMessage("Place: " + ((ProjectorCMD.place != null && ProjectorCMD.place.equals(player)) ? "§2Enabled" : "§4Disabled"));
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        }
                    }
                    else if (array.length == 3) {
                        if (array[1].equalsIgnoreCase("lever")) {
                            final RotationType[] values;
                            final int length = (values = RotationType.values()).length;
                            int i = 0;
                            while (i < length) {
                                final RotationType currentRotationLever = values[i];
                                if (currentRotationLever.name().equalsIgnoreCase(array[2])) {
                                    if (!ProjectorCMD.selectedProjectors.isEmpty()) {
                                        if (ProjectorCMD.place != null) {
                                            if (!ProjectorCMD.place.equals(player)) {
                                                player.sendMessage("§cSomeone else (" + ProjectorCMD.place.getName() + ") is already using the place mode");
                                                player.sendMessage("§cThis player is no longer in place mode you can now setup the lever");
                                            }
                                            else {
                                                player.sendMessage("§cYou are no longer in place mode !");
                                            }
                                        }
                                        player.getInventory().setItem(0, ProjectorCMD.lever);
                                        ProjectorCMD.currentRotationLever = currentRotationLever;
                                        ProjectorCMD.place = null;
                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                                        break;
                                    }
                                    player.sendMessage("§cError: you did not selected any projectors...");
                                    if (ProjectorCMD.selector.equals(player)) {
                                        player.sendMessage("§cPlease right or left click to any projector");
                                    }
                                    else if (ProjectorCMD.place.equals(player)) {
                                        player.sendMessage("§cPlease place projectors by right-clicking with your magic stick");
                                    }
                                    else {
                                        player.sendMessage("§cPlease use /light projector select");
                                    }
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.0f);
                                    break;
                                }
                                else {
                                    ++i;
                                }
                            }
                        }
                        else if (array[1].equalsIgnoreCase("transfer")) {
                            if (StringUtils.isNumeric(array[2]) && LazerProjector.getLazerProjectors().stream().filter(lazerProjector5 -> lazerProjector5.getCurrentID() == Integer.valueOf(array[2])).findAny().isPresent()) {
                                if (player.getTargetBlock((Set)null, 5).getType() == Material.LEVER) {
                                    player.getTargetBlock((Set)null, 5).getLocation();
                                    try {
                                        final Location location;
                                        final LeverActivator leverActivator3 = LazerProjector.getLevers().stream().filter(leverActivator -> leverActivator.getL().equals((Object)location)).findAny().get();
                                        if (leverActivator3.getProjectors().contains(Integer.valueOf(array[2]))) {
                                            player.sendMessage("§cError: This lever is already able to perform this projector number " + array[2]);
                                            return false;
                                        }
                                        leverActivator3.getProjectors().add(Integer.valueOf(array[2]));
                                        Database.getLazer().setData("levers." + leverActivator3.getType().name() + "." + String.valueOf(leverActivator3.getCurrentID()) + ".projectors", leverActivator3.getProjectors());
                                        player.sendMessage("§aID number §2" + array[2] + " §ahas been transfered to the lever number §B" + leverActivator3.getCurrentID());
                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                                    }
                                    catch (NoSuchElementException ex) {
                                        player.sendMessage("§cError: this lever is not registered, you need to include a rotation type");
                                        player.sendMessage(String.valueOf(CC.arrow) + " §b/light projector transfer <id> <type>");
                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.0f);
                                        this.displayTypes(player);
                                    }
                                }
                                else {
                                    player.sendMessage(String.valueOf(CC.red) + "You must target a lever");
                                }
                            }
                            else {
                                player.sendMessage(String.valueOf(CC.red) + "Error: invalid ID");
                            }
                        }
                    }
                    else if (array.length == 4 && array[1].equalsIgnoreCase("transfer")) {
                        if (StringUtils.isNumeric(array[2]) && LazerProjector.getLazerProjectors().stream().filter(lazerProjector6 -> lazerProjector6.getCurrentID() == Integer.valueOf(array[2])).findAny().isPresent()) {
                            if (player.getTargetBlock((Set)null, 5).getType() == Material.LEVER) {
                                final Location location2 = player.getTargetBlock((Set)null, 5).getLocation();
                                if (!LazerProjector.getLevers().stream().filter(leverActivator2 -> leverActivator2.getL().equals((Object)location2)).findAny().isPresent()) {
                                    try {
                                        Database.getLazer().registerLever(location2, RotationType.valueOf(array[3]), Integer.valueOf(array[2]));
                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                                    }
                                    catch (IllegalArgumentException ex2) {
                                        player.sendMessage("§cError rotation not found !");
                                        player.sendMessage(String.valueOf(CC.arrow) + " §b/light projector transfer <id> <type>");
                                        this.displayTypes(player);
                                    }
                                }
                            }
                            else {
                                player.sendMessage(String.valueOf(CC.red) + "You must target a lever");
                            }
                        }
                        else {
                            player.sendMessage(String.valueOf(CC.red) + "Error: invalid ID");
                        }
                    }
                }
            }
        }
        return false;
    }
}
