package wkimenai.mc-lights.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.util.Vector;
import org.bukkit.Bukkit;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.bukkit.Location;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Map;
import com.google.common.primitives.Primitives;
import java.lang.reflect.Modifier;
import com.google.common.base.Joiner;
import java.lang.reflect.Field;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.List;
import java.io.File;

public class Config
{
    private static final transient char DEFAULT_SEPARATOR = '_';
    private static final transient String LINE_SEPARATOR;
    private static final transient String TEMP_CONFIG_SECTION = "temp";
    private transient File configFile;
    private transient List<String> header;
    
    protected Config(final File file) {
        this(file, null);
    }
    
    protected Config(final File configFile, final List<String> header) {
        this.configFile = configFile;
        this.header = header;
    }
    
    public final void load() {
        try {
            final YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(this.configFile);
            for (final Field field : this.getClass().getFields()) {
                this.loadField(field, this.getFieldName(field), loadConfiguration);
            }
            this.saveConfig(loadConfiguration);
        }
        catch (Exception ex) {
            throw new InvalidConfigurationException((Throwable)ex);
        }
    }
    
    public final void save() {
        try {
            final YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(this.configFile);
            for (final Field field : this.getClass().getFields()) {
                this.saveField(field, this.getFieldName(field), loadConfiguration);
            }
            this.saveConfig(loadConfiguration);
        }
        catch (Exception ex) {
            throw new InvalidConfigurationException((Throwable)ex);
        }
    }
    
    private final String getFieldName(final Field field) {
        final ConfigOptions configOptions = field.getAnnotation(ConfigOptions.class);
        if (configOptions == null) {
            return field.getName().replace('_', '.');
        }
        final String name = configOptions.name();
        if (name.equals("")) {
            return field.getName().replace('_', '.');
        }
        return name;
    }
    
    private final boolean ignoreField(final Field field) {
        final ConfigOptions configOptions = field.getAnnotation(ConfigOptions.class);
        return configOptions != null && configOptions.ignore();
    }
    
    private final void saveConfig(final YamlConfiguration yamlConfiguration) {
        if (this.header != null && this.header.size() > 0) {
            yamlConfiguration.options().header(Joiner.on(Config.LINE_SEPARATOR).join((Iterable)this.header));
        }
        yamlConfiguration.save(this.configFile);
    }
    
    private final void loadField(final Field field, final String s, final YamlConfiguration yamlConfiguration) {
        if (Modifier.isTransient(field.getModifiers()) || this.ignoreField(field)) {
            return;
        }
        final Object value = yamlConfiguration.get(this.getFieldName(field));
        if (value == null) {
            this.saveField(field, s, yamlConfiguration);
        }
        else {
            field.set(this, this.deserializeObject(field.getType(), value));
        }
    }
    
    private final void saveField(final Field field, final String s, final YamlConfiguration yamlConfiguration) {
        if (Modifier.isTransient(field.getModifiers()) || this.ignoreField(field)) {
            return;
        }
        yamlConfiguration.set(s, this.serializeObject(field.get(this), yamlConfiguration));
    }
    
    private final Object deserializeObject(final Class<?> enumType, final Object o) {
        if (enumType.isPrimitive()) {
            return Primitives.wrap((Class)enumType).getMethod("valueOf", String.class).invoke(this, o.toString());
        }
        if (Primitives.isWrapperType((Class)enumType)) {
            return enumType.getMethod("valueOf", String.class).invoke(this, o.toString());
        }
        if (enumType.isEnum() || o instanceof Enum) {
            return Enum.valueOf(enumType, o.toString());
        }
        if (Map.class.isAssignableFrom(enumType) || o instanceof Map) {
            final ConfigurationSection configurationSection = (ConfigurationSection)o;
            final HashMap<String, Object> hashMap = new HashMap<String, Object>();
            for (final String s : configurationSection.getKeys(false)) {
                final Object value = configurationSection.get(s);
                hashMap.put(s, this.deserializeObject(value.getClass(), value));
            }
            final Object instance = enumType.newInstance();
            enumType.getMethod("putAll", Map.class).invoke(instance, hashMap);
            return instance;
        }
        if (List.class.isAssignableFrom(enumType) || o instanceof List) {
            final ArrayList<Object> list = new ArrayList<Object>();
            for (final Object next : (List)o) {
                list.add(this.deserializeObject(next.getClass(), next));
            }
            return list;
        }
        if (Location.class.isAssignableFrom(enumType) || o instanceof Location) {
            final JSONObject jsonObject = (JSONObject)new JSONParser().parse(o.toString());
            return new Location(Bukkit.getWorld(jsonObject.get((Object)"world").toString()), Double.parseDouble(jsonObject.get((Object)"x").toString()), Double.parseDouble(jsonObject.get((Object)"y").toString()), Double.parseDouble(jsonObject.get((Object)"z").toString()), Float.parseFloat(jsonObject.get((Object)"yaw").toString()), Float.parseFloat(jsonObject.get((Object)"pitch").toString()));
        }
        if (Vector.class.isAssignableFrom(enumType) || o instanceof Vector) {
            final JSONObject jsonObject2 = (JSONObject)new JSONParser().parse(o.toString());
            return new Vector(Double.parseDouble(jsonObject2.get((Object)"x").toString()), Double.parseDouble(jsonObject2.get((Object)"y").toString()), Double.parseDouble(jsonObject2.get((Object)"z").toString()));
        }
        return ChatColor.translateAlternateColorCodes('&', o.toString());
    }
    
    private final Object serializeObject(final Object o, final YamlConfiguration yamlConfiguration) {
        if (o instanceof String) {
            return o.toString().replace('§', '&');
        }
        if (o instanceof Enum) {
            return ((Enum)o).name();
        }
        if (o instanceof Map) {
            final ConfigurationSection section = yamlConfiguration.createSection("temp");
            for (final Map.Entry<Object, V> entry : ((Map)o).entrySet()) {
                section.set(entry.getKey().toString(), this.serializeObject(entry.getValue(), yamlConfiguration));
            }
            yamlConfiguration.set("temp", (Object)null);
            return section;
        }
        if (o instanceof List) {
            final ArrayList<Object> list = new ArrayList<Object>();
            final Iterator<Object> iterator2 = ((List)o).iterator();
            while (iterator2.hasNext()) {
                list.add(this.serializeObject(iterator2.next(), yamlConfiguration));
            }
            return list;
        }
        if (o instanceof Location) {
            final Location location = (Location)o;
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put((Object)"world", (Object)location.getWorld().getName());
            jsonObject.put((Object)"x", (Object)location.getX());
            jsonObject.put((Object)"y", (Object)location.getY());
            jsonObject.put((Object)"z", (Object)location.getZ());
            jsonObject.put((Object)"yaw", (Object)location.getYaw());
            jsonObject.put((Object)"pitch", (Object)location.getPitch());
            return jsonObject.toJSONString();
        }
        if (o instanceof Vector) {
            final Vector vector = (Vector)o;
            final JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put((Object)"x", (Object)vector.getX());
            jsonObject2.put((Object)"y", (Object)vector.getY());
            jsonObject2.put((Object)"z", (Object)vector.getZ());
            return jsonObject2.toJSONString();
        }
        return o;
    }
    
    public final List<String> getHeader() {
        return this.header;
    }
    
    public final File getFile() {
        return this.configFile;
    }
    
    public final void setHeader(final List<String> header) {
        this.header = header;
    }
    
    public final void setFile(final File configFile) {
        this.configFile = configFile;
    }
    
    static {
        LINE_SEPARATOR = System.getProperty("line.separator");
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    protected @interface ConfigOptions {
        String name() default "";
        
        boolean ignore() default false;
    }
}
