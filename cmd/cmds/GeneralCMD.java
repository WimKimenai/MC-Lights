package wkimenai.mc-lights.cmd.cmds;

import java.util.Set;
import org.bukkit.Sound;
import wkimenai.mc-lights.database.Database;
import wkimenai.mc-lights.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import wkimenai.mc-lights.visual.Visual;
import wkimenai.mc-lights.listener.task.GlobalTime;
import org.bukkit.Location;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import wkimenai.mc-lights.utils.UtilItem;
import org.bukkit.Material;
import wkimenai.mc-lights.area.Cuboid;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import wkimenai.mc-lights.area.AreaType;
import org.bukkit.command.CommandExecutor;

public class GeneralCMD implements CommandExecutor
{
    public static AreaType area;
    public static Player editor;
    public static ItemStack old;
    public static ItemStack magicStick;
    private static Cuboid currentVisualizing;
    
    static {
        (GeneralCMD.magicStick = new UtilItem(Material.STICK, (byte)0, "§5Magic Stick (Select the area:[LeftClick] Point A and [RightClick] B)").getItem()).addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 0);
        final ItemMeta itemMeta = GeneralCMD.magicStick.getItemMeta();
        itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        GeneralCMD.magicStick.setItemMeta(itemMeta);
    }
    
    private boolean isCuboidAndInside(final Player player, final Location location) {
        if (GlobalTime.cuboid == null) {
            player.sendMessage("§cYou need to cuboid the nightclub's area first by using /light setup area");
            return false;
        }
        if (location == null) {
            return true;
        }
        if (!GlobalTime.cuboid.isIn(location)) {
            player.sendMessage("§cYou are not in the nightclub area, please set this inside or redefine the area !");
            return false;
        }
        return true;
    }
    
    private void setupArea(final Player player, final AreaType area) {
        if (GeneralCMD.editor != null) {
            if (!GeneralCMD.editor.equals(player)) {
                player.sendMessage("§cSomeone §b(" + GeneralCMD.editor.getName() + ") §cis already editing the area");
                return;
            }
            player.sendMessage("§cYou are already in editor mode, please drop the magic stick or select point A and B");
        }
        GeneralCMD.area = area;
        GeneralCMD.editor = player;
        GeneralCMD.old = player.getInventory().getItem(0);
        player.getInventory().setItem(0, GeneralCMD.magicStick);
    }
    
    private void visualizeArea(final Player player, final AreaType areaType) {
        if (areaType == AreaType.PACKET_AREA) {
            if (GlobalTime.cuboid == null) {
                player.sendMessage("§cYou need to cuboid the nightclub's area first by using /light setup area");
                return;
            }
        }
        else if (areaType == AreaType.VISUAL_AREA && Visual.getVisualCuboid() == null) {
            player.sendMessage("§cYou need to cuboid the nightclub's area first by using /light setup area");
            return;
        }
        if (GeneralCMD.currentVisualizing != null) {
            player.sendMessage("§cYou are no longer using the Visualizer Mode ");
            GeneralCMD.currentVisualizing = null;
        }
        else {
            GeneralCMD.currentVisualizing = ((areaType == AreaType.VISUAL_AREA) ? Visual.getVisualCuboid() : GlobalTime.cuboid);
            player.teleport(GeneralCMD.currentVisualizing.getCenter());
            player.sendMessage("§aYou are now using the Visualizer Mode ");
        }
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (commandSender instanceof Player) {
            final Player player = (Player)commandSender;
            if (array.length == 0) {
                player.sendMessage("§b* §6mc-lights © Commands §b*");
                player.sendMessage("§b/light setup area");
                player.sendMessage(String.valueOf(CC.arrow) + " §bSelect 2 points to cuboid the nightclub area");
                player.sendMessage("§b/light setup visualarea");
                player.sendMessage(String.valueOf(CC.arrow) + " §bSelect 2 points to cuboid the visual area, where visual effects will spawn");
                player.sendMessage("§b/light setup dj");
                player.sendMessage(String.valueOf(CC.arrow) + " §bPlace the dj location");
                player.sendMessage("§b/light dj lever");
                player.sendMessage(String.valueOf(CC.arrow) + " §bTarget a lever, activate the dj");
                player.sendMessage("§b/light visualize area");
                player.sendMessage(String.valueOf(CC.arrow) + " §bVisualize the area (toogle mode)");
                player.sendMessage("§b/light visualize visualarea");
                player.sendMessage(String.valueOf(CC.arrow) + " §bVisualize the visual area (toggle mode)");
                player.sendMessage("§b/light staff");
                player.sendMessage("§b/light projector");
                player.sendMessage("§b/light effect");
            }
            else if (array.length == 2) {
                if (!player.hasPermission("light.admin")) {
                    player.sendMessage("§cYou do not have the permission !");
                    return false;
                }
                if (array[0].equalsIgnoreCase("visualize")) {
                    if (array[1].equalsIgnoreCase("area")) {
                        this.visualizeArea(player, AreaType.PACKET_AREA);
                    }
                    else if (array[1].equalsIgnoreCase("visualarea")) {
                        this.visualizeArea(player, AreaType.VISUAL_AREA);
                    }
                }
                else if (array[0].equalsIgnoreCase("setup")) {
                    if (array[1].equalsIgnoreCase("area")) {
                        this.setupArea(player, AreaType.PACKET_AREA);
                    }
                    else if (array[1].equalsIgnoreCase("visualarea")) {
                        this.setupArea(player, AreaType.VISUAL_AREA);
                    }
                    else if (array[1].equalsIgnoreCase("dj") && this.isCuboidAndInside(player, player.getLocation())) {
                        Database.getGeneral().setDjLocation(player.getLocation());
                        player.sendMessage("§bThe dj location has been initialized !");
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    }
                }
                else if (array[0].equalsIgnoreCase("dj") && array[1].equalsIgnoreCase("lever")) {
                    if (this.isCuboidAndInside(player, null)) {
                        if (player.getTargetBlock((Set)null, 5).getType() == Material.LEVER) {
                            Database.getGeneral().setDjLever(player.getTargetBlock((Set)null, 5).getLocation());
                            player.sendMessage(String.valueOf(CC.green) + "The location has been saved for this lever");
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        }
                    }
                    else {
                        player.sendMessage(String.valueOf(CC.red) + "You must target a lever");
                    }
                }
            }
        }
        return false;
    }
    
    public static Cuboid getCurrentVisualizing() {
        return GeneralCMD.currentVisualizing;
    }
}
