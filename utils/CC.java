package wkimenai.mc-lights.utils;

import org.bukkit.Color;
import org.bukkit.ChatColor;
import java.util.Random;

public class CC
{
    public static String aqua;
    public static String black;
    public static String blue;
    public static String d_aqua;
    public static String d_blue;
    public static String d_gray;
    public static String d_green;
    public static String d_purple;
    public static String d_red;
    public static String gold;
    public static String gray;
    public static String green;
    public static String l_purple;
    public static String red;
    public static String white;
    public static String yellow;
    public static String bold;
    public static String italic;
    public static String magic;
    public static String reset;
    public static String underline;
    public static String strike;
    public static String arrow;
    public static String headkey;
    
    public static String rainbowlize(final String s) {
        final int n = 0;
        String string = "";
        final String s2 = "123456789abcde";
        for (int i = 0; i < s.length(); ++i) {
            int j;
            do {
                j = new Random().nextInt(s2.length() - 1) + 1;
            } while (j == n);
            string = string + ChatColor.RESET.toString() + ChatColor.getByChar(s2.charAt(j)) + "" + s.charAt(i);
        }
        return string;
    }
    
    public static String colored(final String s) {
        return s.replace("&", "§");
    }
    
    public static String getGreatRandomColor() {
        String s = "§0";
        switch (UtilMath.randomRange(0, 8)) {
            case 0: {
                s = "§f";
                break;
            }
            case 1: {
                s = "§6";
                break;
            }
            case 2: {
                s = "§5";
                break;
            }
            case 3: {
                s = "§e";
                break;
            }
            case 4: {
                s = "§a";
                break;
            }
            case 5: {
                s = "§b";
                break;
            }
            case 6: {
                s = "§d";
                break;
            }
            case 7: {
                s = "§2";
                break;
            }
            case 8: {
                s = "§4";
                break;
            }
        }
        return s;
    }
    
    public static Color getColor(final String s) {
        Color color = null;
        switch (s) {
            case "AQUA": {
                color = Color.AQUA;
                break;
            }
            case "BLACK": {
                color = Color.BLACK;
                break;
            }
            case "BLUE": {
                color = Color.BLUE;
                break;
            }
            case "FUCHSIA": {
                color = Color.FUCHSIA;
                break;
            }
            case "GRAY": {
                color = Color.GRAY;
                break;
            }
            case "GREEN": {
                color = Color.GREEN;
                break;
            }
            case "LIME": {
                color = Color.LIME;
                break;
            }
            case "MAROON": {
                color = Color.MAROON;
                break;
            }
            case "NAVY": {
                color = Color.NAVY;
                break;
            }
            case "OLIVE": {
                color = Color.OLIVE;
                break;
            }
            case "ORANGE": {
                color = Color.ORANGE;
                break;
            }
            case "PURPLE": {
                color = Color.PURPLE;
                break;
            }
            case "RED": {
                color = Color.RED;
                break;
            }
            case "SILVER": {
                color = Color.SILVER;
                break;
            }
            case "TEAL": {
                color = Color.TEAL;
                break;
            }
            case "WHITE": {
                color = Color.WHITE;
                break;
            }
            case "YELLOW": {
                color = Color.YELLOW;
                break;
            }
            default: {
                color = Color.WHITE;
                break;
            }
        }
        return color;
    }
    
    static {
        CC.aqua = ChatColor.AQUA + "";
        CC.black = ChatColor.BLACK + "";
        CC.blue = ChatColor.BLUE + "";
        CC.d_aqua = ChatColor.DARK_AQUA + "";
        CC.d_blue = ChatColor.DARK_BLUE + "";
        CC.d_gray = ChatColor.DARK_GRAY + "";
        CC.d_green = ChatColor.DARK_GREEN + "";
        CC.d_purple = ChatColor.DARK_PURPLE + "";
        CC.d_red = ChatColor.DARK_RED + "";
        CC.gold = ChatColor.GOLD + "";
        CC.gray = ChatColor.GRAY + "";
        CC.green = ChatColor.GREEN + "";
        CC.l_purple = ChatColor.LIGHT_PURPLE + "";
        CC.red = ChatColor.RED + "";
        CC.white = ChatColor.WHITE + "";
        CC.yellow = ChatColor.YELLOW + "";
        CC.bold = ChatColor.BOLD + "";
        CC.italic = ChatColor.ITALIC + "";
        CC.magic = ChatColor.MAGIC + "";
        CC.reset = ChatColor.RESET + "";
        CC.underline = ChatColor.UNDERLINE + "";
        CC.strike = ChatColor.STRIKETHROUGH + "";
        CC.arrow = "\u27bd";
        CC.headkey = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv";
    }
}
