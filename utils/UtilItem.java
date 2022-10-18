package wkimenai.mc-lights.utils;

import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.Color;
import java.util.Arrays;
import org.bukkit.potion.PotionData;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.SkullType;
import java.lang.reflect.Field;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.Iterator;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.Collection;
import org.bukkit.ChatColor;
import wkimenai.mc-lights.nms.NMS;
import org.bukkit.inventory.ItemStack;
import java.util.Random;
import java.util.ArrayList;
import org.bukkit.Material;

public class UtilItem
{
    private Material m;
    private int id;
    private byte data;
    private String name;
    private ArrayList<String> lores;
    private static final int RGB_MAX = 255;
    private static Random r;
    
    public UtilItem(final Material m, final byte data, final String name) {
        this.m = m;
        this.data = data;
        this.name = name;
    }
    
    public UtilItem(final Material m, final byte data) {
        this.m = m;
        this.data = data;
        this.name = null;
    }
    
    public UtilItem(final int id, final byte data, final String name, final ArrayList<String> lores) {
        this.id = id;
        this.data = data;
        this.name = name;
        this.lores = lores;
    }
    
    public UtilItem(final int id, final byte data, final String name) {
        this.id = id;
        this.data = data;
        this.name = name;
    }
    
    public ItemStack getItem(final int n) {
        final ItemStack itemStack = new ItemStack((this.m == null) ? Material.getMaterial(this.id) : this.m, 1, (short)this.data);
        if (this.name != null) {
            setDisplayName(itemStack, CC.colored(this.name));
        }
        if (this.lores != null) {
            setLores(itemStack, this.lores, n);
        }
        NMS.hideItemTag(itemStack);
        return itemStack;
    }
    
    public ItemStack getItem() {
        final ItemStack itemStack = new ItemStack((this.m == null) ? Material.getMaterial(this.id) : this.m, 1, (short)this.data);
        if (this.name != null) {
            setDisplayName(itemStack, CC.colored(this.name));
        }
        NMS.hideItemTag(itemStack);
        return itemStack;
    }
    
    public static void setLores(final ItemStack itemStack, final ArrayList<String> list, final int n) {
        final ArrayList<String> lore = new ArrayList<String>();
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            final String colored = CC.colored(iterator.next());
            final ArrayList<String> list2 = new ArrayList<String>();
            String str = "";
            Object obj = null;
            if (colored.isEmpty()) {
                lore.add(colored);
            }
            else {
                for (int i = 0; i < colored.length(); ++i) {
                    if (colored.charAt(i) == '§' && i < colored.length() - 1) {
                        final ChatColor byChar = ChatColor.getByChar(colored.charAt(i + 1));
                        if (byChar != null) {
                            obj = byChar;
                        }
                    }
                    str += colored.charAt(i);
                    if ((ChatColor.stripColor(str).length() >= n && colored.charAt(i) == ' ' && !str.isEmpty()) || i + 1 == colored.length()) {
                        list2.add(str);
                        str = "" + obj;
                    }
                }
                lore.addAll((Collection<?>)list2);
            }
        }
        itemMeta.setLore((List)lore);
        itemStack.setItemMeta(itemMeta);
    }
    
    public static ItemStack getSkull(final String s, final String s2) {
        final ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        final SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
        final GameProfile value = new GameProfile(UUID.randomUUID(), (String)null);
        final PropertyMap properties = value.getProperties();
        if (s2 != null) {
            skullMeta.setDisplayName(CC.colored(s2));
        }
        properties.put((Object)"textures", (Object)new Property("textures", s));
        try {
            final Field declaredField = skullMeta.getClass().getDeclaredField("profile");
            declaredField.setAccessible(true);
            declaredField.set(skullMeta, value);
        }
        catch (NoSuchFieldException | IllegalAccessException ex) {
            final Throwable t;
            t.printStackTrace();
        }
        itemStack.setItemMeta((ItemMeta)skullMeta);
        return itemStack;
    }
    
    public static ItemStack getSkull(final String s) {
        return getSkull(s, null);
    }
    
    public static ItemStack getSkullOwner(final String owner) {
        final ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
        final SkullMeta itemMeta = (SkullMeta)itemStack.getItemMeta();
        itemMeta.setOwner(owner);
        itemStack.setItemMeta((ItemMeta)itemMeta);
        return itemStack;
    }
    
    public static ItemStack setDisplayName(final ItemStack itemStack, final String s) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(CC.colored(s));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public static ItemStack setDisplayNamePotion(final ItemStack itemStack, final String s, final PotionType potionType) {
        final PotionMeta itemMeta = (PotionMeta)itemStack.getItemMeta();
        itemMeta.setBasePotionData(new PotionData(potionType, false, false));
        itemMeta.setDisplayName(CC.colored(s));
        itemMeta.setLore((List)new ArrayList(Arrays.asList(" ")));
        itemStack.setItemMeta((ItemMeta)itemMeta);
        return itemStack;
    }
    
    public static ItemStack getColorArmor(final Material material, final Color color) {
        final ItemStack itemStack = new ItemStack(material, 1);
        final LeatherArmorMeta itemMeta = (LeatherArmorMeta)itemStack.getItemMeta();
        itemMeta.setColor(color);
        itemStack.setItemMeta((ItemMeta)itemMeta);
        return itemStack;
    }
    
    public static Color getRandomColor() {
        return Color.fromRGB(UtilItem.r.nextInt(255), UtilItem.r.nextInt(255), UtilItem.r.nextInt(255));
    }
    
    public static ColorData getGreatRandomColor() {
        Color color = null;
        byte b = 0;
        switch (UtilMath.randomRange(0, 8)) {
            case 0: {
                b = 0;
                color = Color.WHITE;
                break;
            }
            case 1: {
                b = 1;
                color = Color.ORANGE;
                break;
            }
            case 2: {
                b = 2;
                color = Color.FUCHSIA;
                break;
            }
            case 3: {
                b = 4;
                color = Color.YELLOW;
                break;
            }
            case 4: {
                b = 5;
                color = Color.GREEN;
                break;
            }
            case 5: {
                b = 9;
                color = Color.NAVY;
                break;
            }
            case 6: {
                b = 10;
                color = Color.PURPLE;
                break;
            }
            case 7: {
                b = 11;
                color = Color.BLUE;
                break;
            }
            case 8: {
                b = 14;
                color = Color.RED;
                break;
            }
        }
        return new ColorData(b, color);
    }
    
    public static boolean isSimilar(final ItemStack itemStack, final ItemStack itemStack2) {
        return itemStack.getItemMeta().getDisplayName().equals(CC.colored(itemStack2.getItemMeta().getDisplayName())) && itemStack.getTypeId() == itemStack2.getTypeId() && itemStack.getData().getData() == itemStack2.getData().getData();
    }
    
    static {
        UtilItem.r = new Random();
    }
    
    public static class ColorData
    {
        private byte data;
        private Color color;
        
        public ColorData(final byte data, final Color color) {
            this.data = data;
            this.color = color;
        }
        
        public byte getData() {
            return this.data;
        }
        
        public Color getColor() {
            return this.color;
        }
    }
}
