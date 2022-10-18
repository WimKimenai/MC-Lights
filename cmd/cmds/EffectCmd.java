package wkimenai.mc-lights.cmd.cmds;

import wkimenai.mc-lights.effect.effects.SonoEffect;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import java.util.Set;
import wkimenai.mc-lights.database.Database;
import wkimenai.mc-lights.nms.Reflection;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Sound;
import wkimenai.mc-lights.visual.Visual;
import wkimenai.mc-lights.effect.Effect;
import wkimenai.mc-lights.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import wkimenai.mc-lights.listener.task.GlobalTime;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandExecutor;

public class EffectCmd implements CommandExecutor
{
    private List<String> effects;
    private List<String> visuals;
    
    public EffectCmd() {
        this.effects = new ArrayList<String>(Arrays.asList("cube", "cyclone", "dragon", "etherneal", "ground", "smokescene", "sono", "tornado"));
        this.visuals = new ArrayList<String>(Arrays.asList("blue", "bomb", "cloud", "cloudexplosion", "confetti", "darkness", "diamondfalling", "disguise", "experiencerain", "fireworks", "flash", "jump", "lightning", "lillypad", "rainsword", "stars", "strobe"));
    }
    
    private void displayEffects(final Player player) {
        String string = "";
        for (int i = 0; i < this.effects.size(); ++i) {
            string = String.valueOf(string) + this.effects.get(i) + ((i == this.effects.size() - 1) ? "." : ", ");
        }
        player.sendMessage("§bEffect list: §3" + string);
    }
    
    private void displayVisuals(final Player player) {
        String string = "";
        for (int i = 0; i < this.visuals.size(); ++i) {
            string = String.valueOf(string) + this.visuals.get(i) + ((i == this.visuals.size() - 1) ? "." : ", ");
        }
        player.sendMessage("§bVisual list: §3" + string);
    }
    
    private boolean isCuboidAndInside(final Player player, final Location location) {
        if (GlobalTime.cuboid == null) {
            player.sendMessage("§cYou need to cuboid the nightclub's area first by using /light add area");
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
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (commandSender instanceof Player) {
            final Player player = (Player)commandSender;
            if (array.length > 0 && array[0].equalsIgnoreCase("effect")) {
                if (!player.hasPermission("light.admin")) {
                    player.sendMessage("§cYou do not have the permission !");
                    return false;
                }
                if (array.length == 1) {
                    player.sendMessage("§b/light effect clear ");
                    player.sendMessage("§b/light effect set <effect>");
                    this.displayEffects(player);
                    player.sendMessage("§bFor normal effects: ");
                    player.sendMessage(String.valueOf(CC.arrow) + " §b/light effect <effect> lever <id>");
                    player.sendMessage(String.valueOf(CC.arrow) + " §b/light effect <effect> lever @a");
                    player.sendMessage("§bYou need to target a lever");
                    player.sendMessage("§b@a is able to select every ids from the lever type");
                    player.sendMessage("");
                    player.sendMessage("§bFor visual effects: ");
                    player.sendMessage(String.valueOf(CC.arrow) + " §b/light effect lever <effect> ");
                    player.sendMessage("§bYou need to target a lever");
                    this.displayVisuals(player);
                }
                else if (array.length == 2) {
                    if (array[0].equalsIgnoreCase("effect") && array[1].equalsIgnoreCase("clear")) {
                        Effect.getEffects().forEach(effect -> effect.stop());
                        Visual.getVisuals().forEach(visual -> visual.stop());
                        player.sendMessage("§cAll effects are now cleared !");
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    }
                }
                else if (array.length == 3) {
                    if (this.isCuboidAndInside(player, player.getLocation())) {
                        if (array[1].equalsIgnoreCase("set")) {
                            if (this.effects.stream().filter(anotherString -> array[2].equalsIgnoreCase(anotherString)).findAny().isPresent()) {
                                final Player player2;
                                if (Effect.getEffects().stream().filter(effect6 -> effect6.getClass().getSimpleName().replace("Effect", "").equalsIgnoreCase(array[2]) && effect6.getLocation().getBlock().equals(player2.getLocation().getBlock())).findAny().isPresent()) {
                                    player.sendMessage("§cDuplicate effect location found ! You have probably already set this effect here...");
                                    return false;
                                }
                                final Effect effect8 = (Effect)Reflection.getConstructor("wkimenai.mc-lights.effect.effects." + WordUtils.capitalize(array[2].toLowerCase()) + "Effect", Location.class).invoke(player.getLocation());
                                Database.getEffect().registerNewEffect(effect8);
                                player.sendMessage(String.valueOf(CC.green) + array[2] + " has been saved");
                                if (array[2].equalsIgnoreCase("sono")) {
                                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.0f);
                                    player.sendMessage("§bThe sono effect is special, you need to setup another location before starting");
                                    player.sendMessage(String.valueOf(CC.arrow) + " §bPlease do: /light effect sono §6" + effect8.getCurrentID() + " §bsetend");
                                }
                            }
                            else {
                                player.sendMessage("§cError: effect not found ! ");
                                this.displayEffects(player);
                            }
                        }
                        else if (array[1].equalsIgnoreCase("lever")) {
                            if (this.visuals.stream().filter(anotherString2 -> array[2].equalsIgnoreCase(anotherString2)).findAny().isPresent()) {
                                if (player.getTargetBlock((Set)null, 5).getType() == Material.LEVER) {
                                    final Visual visual2;
                                    final Location lever;
                                    final Player player3;
                                    this.visuals.stream().filter(anotherString3 -> array[2].equalsIgnoreCase(anotherString3)).findAny().ifPresent(p3 -> {
                                        player.getTargetBlock((Set)null, 5).getLocation();
                                        visual2 = (Visual)Reflection.getConstructor("wkimenai.mc-lights.visual.visuals." + WordUtils.capitalize(array[2].toLowerCase()) + "Visual", (Class<?>[])new Class[0]).invoke(new Object[0]);
                                        visual2.setLever(lever);
                                        Database.getEffect().registerVisual(visual2);
                                        player3.sendMessage(String.valueOf(CC.green) + array[2] + " has been saved for this lever");
                                        player3.playSound(player3.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                                        return;
                                    });
                                }
                                else {
                                    player.sendMessage(String.valueOf(CC.red) + "You must target a lever");
                                }
                            }
                            else {
                                player.sendMessage("§cError: effect not found ! ");
                                this.displayVisuals(player);
                            }
                        }
                    }
                }
                else if (array.length == 4) {
                    if (GlobalTime.cuboid == null) {
                        player.sendMessage("§cYou need to cuboid the nightclub's area first by using /light setup area");
                        return false;
                    }
                    if (array[3].equalsIgnoreCase("setend") && array[1].equalsIgnoreCase("sono")) {
                        if (StringUtils.isNumeric(array[2])) {
                            final Player player4;
                            Effect.getEffects().stream().filter(effect2 -> effect2.getCurrentID() == Integer.valueOf(array[2]) && effect2.getClass().getSimpleName().equalsIgnoreCase("SonoEffect")).findAny().ifPresent(sonoEffect -> {
                                sonoEffect.setEnd(player4.getLocation());
                                player4.sendMessage("§bThe end location for the sono effect number " + sonoEffect.currentID + " is now saved !");
                                return;
                            });
                        }
                    }
                    else if (this.effects.stream().filter(anotherString4 -> array[1].equalsIgnoreCase(anotherString4)).findAny().isPresent()) {
                        if (array[2].equalsIgnoreCase("lever")) {
                            if (player.getTargetBlock((Set)null, 5).getType() == Material.LEVER) {
                                if (StringUtils.isNumeric(array[3])) {
                                    final Player player5;
                                    Effect.getEffects().stream().filter(effect3 -> effect3.getClass().getSimpleName().replace("Effect", "").equalsIgnoreCase(array[1]) && effect3.getCurrentID() == Integer.valueOf(array[3])).findAny().ifPresent(effect7 -> {
                                        effect7.setLever(player5.getTargetBlock((Set)null, 5).getLocation());
                                        player5.sendMessage("§alever Location for §b" + array[1] + " §anumber §b" + array[3] + " §ais saved !");
                                        player5.playSound(player5.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                                        return;
                                    });
                                }
                                else if (array[3].equalsIgnoreCase("@a")) {
                                    Effect.getEffects().stream().filter(effect4 -> effect4.getClass().getSimpleName().replace("Effect", "").equalsIgnoreCase(array[1])).forEach(effect5 -> effect5.setLever(player.getTargetBlock((Set)null, 5).getLocation()));
                                    player.sendMessage("§aLever Location for every " + array[1] + "'ids are saved");
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                                }
                                else {
                                    player.sendMessage("§cError: Please enter a valid ID");
                                }
                            }
                            else {
                                player.sendMessage(String.valueOf(CC.red) + "You must target a lever");
                            }
                        }
                    }
                    else {
                        player.sendMessage("§cError: effect not found ! ");
                        this.displayEffects(player);
                    }
                }
            }
        }
        return false;
    }
}
