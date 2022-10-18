package wkimenai.mc-lights.utils;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.plugin.RegisteredServiceProvider;
import java.lang.reflect.Method;
import org.bukkit.entity.Player;
import java.util.Collection;
import org.json.simple.JSONArray;
import java.util.TimerTask;
import java.util.Timer;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONObject;
import java.util.Iterator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.Bukkit;
import java.io.IOException;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;

public class Metrics
{
    public static final int B_STATS_VERSION = 1;
    private static final String URL = "https://bStats.org/submitData/bukkit";
    private static boolean logFailedRequests;
    private static String serverUUID;
    private final JavaPlugin plugin;
    private final List<CustomChart> charts;
    
    static {
        if (System.getProperty("bstats.relocatecheck") == null || !System.getProperty("bstats.relocatecheck").equals("false")) {
            final String anObject = new String(new byte[] { 111, 114, 103, 46, 98, 115, 116, 97, 116, 115, 46, 98, 117, 107, 107, 105, 116 });
            final String anObject2 = new String(new byte[] { 121, 111, 117, 114, 46, 112, 97, 99, 107, 97, 103, 101 });
            if (Metrics.class.getPackage().getName().equals(anObject) || Metrics.class.getPackage().getName().equals(anObject2)) {
                throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
            }
        }
    }
    
    public Metrics(final JavaPlugin plugin) {
        this.charts = new ArrayList<CustomChart>();
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null!");
        }
        this.plugin = plugin;
        final File file = new File(new File(plugin.getDataFolder().getParentFile(), "bStats"), "config.yml");
        final YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(file);
        if (!loadConfiguration.isSet("serverUuid")) {
            loadConfiguration.addDefault("enabled", (Object)true);
            loadConfiguration.addDefault("serverUuid", (Object)UUID.randomUUID().toString());
            loadConfiguration.addDefault("logFailedRequests", (Object)false);
            loadConfiguration.options().header("bStats collects some data for plugin authors like how many servers are using their plugins.\nTo honor their work, you should not disable it.\nThis has nearly no effect on the server performance!\nCheck out https://bStats.org/ to learn more :)").copyDefaults(true);
            try {
                loadConfiguration.save(file);
            }
            catch (IOException ex) {}
        }
        Metrics.serverUUID = loadConfiguration.getString("serverUuid");
        Metrics.logFailedRequests = loadConfiguration.getBoolean("logFailedRequests", false);
        if (loadConfiguration.getBoolean("enabled", true)) {
            boolean b = false;
            for (final Class clazz : Bukkit.getServicesManager().getKnownServices()) {
                try {
                    clazz.getField("B_STATS_VERSION");
                    b = true;
                    break;
                }
                catch (NoSuchFieldException ex2) {}
            }
            Bukkit.getServicesManager().register((Class)Metrics.class, (Object)this, (Plugin)plugin, ServicePriority.Normal);
            if (!b) {
                this.startSubmitting();
            }
        }
    }
    
    private static void sendData(final JSONObject jsonObject) {
        if (jsonObject == null) {
            throw new IllegalArgumentException("Data cannot be null!");
        }
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalAccessException("This method must not be called from the main thread!");
        }
        final HttpsURLConnection httpsURLConnection = (HttpsURLConnection)new URL("https://bStats.org/submitData/bukkit").openConnection();
        final byte[] compress = compress(jsonObject.toString());
        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.addRequestProperty("Accept", "application/json");
        httpsURLConnection.addRequestProperty("Connection", "close");
        httpsURLConnection.addRequestProperty("Content-Encoding", "gzip");
        httpsURLConnection.addRequestProperty("Content-Length", String.valueOf(compress.length));
        httpsURLConnection.setRequestProperty("Content-Type", "application/json");
        httpsURLConnection.setRequestProperty("User-Agent", "MC-Server/1");
        httpsURLConnection.setDoOutput(true);
        final DataOutputStream dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
        dataOutputStream.write(compress);
        dataOutputStream.flush();
        dataOutputStream.close();
        httpsURLConnection.getInputStream().close();
    }
    
    private static byte[] compress(final String s) {
        if (s == null) {
            return null;
        }
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(out);
        gzipOutputStream.write(s.getBytes("UTF-8"));
        gzipOutputStream.close();
        return out.toByteArray();
    }
    
    public void addCustomChart(final CustomChart customChart) {
        if (customChart == null) {
            throw new IllegalArgumentException("Chart cannot be null!");
        }
        this.charts.add(customChart);
    }
    
    private void startSubmitting() {
        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!Metrics.this.plugin.isEnabled()) {
                    timer.cancel();
                    return;
                }
                Bukkit.getScheduler().runTask((Plugin)Metrics.this.plugin, (Runnable)new Runnable() {
                    @Override
                    public void run() {
                        Metrics.this.submitData();
                    }
                });
            }
        }, 300000L, 1800000L);
    }
    
    public JSONObject getPluginData() {
        final JSONObject jsonObject = new JSONObject();
        final String name = this.plugin.getDescription().getName();
        final String version = this.plugin.getDescription().getVersion();
        jsonObject.put((Object)"pluginName", (Object)name);
        jsonObject.put((Object)"pluginVersion", (Object)version);
        final JSONArray jsonArray = new JSONArray();
        final Iterator<CustomChart> iterator = this.charts.iterator();
        while (iterator.hasNext()) {
            final JSONObject access$0 = iterator.next().getRequestJsonObject();
            if (access$0 == null) {
                continue;
            }
            jsonArray.add((Object)access$0);
        }
        jsonObject.put((Object)"customCharts", (Object)jsonArray);
        return jsonObject;
    }
    
    private JSONObject getServerData() {
        int size;
        try {
            final Method method = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers", (Class<?>[])new Class[0]);
            size = (method.getReturnType().equals(Collection.class) ? ((Collection)method.invoke(Bukkit.getServer(), new Object[0])).size() : ((Player[])method.invoke(Bukkit.getServer(), new Object[0])).length);
        }
        catch (Exception ex) {
            size = Bukkit.getOnlinePlayers().size();
        }
        final int onlineMode = Bukkit.getOnlineMode() ? 1 : 0;
        final String version = Bukkit.getVersion();
        final String substring = version.substring(version.indexOf("MC: ") + 4, version.length() - 1);
        final String property = System.getProperty("java.version");
        final String property2 = System.getProperty("os.name");
        final String property3 = System.getProperty("os.arch");
        final String property4 = System.getProperty("os.version");
        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put((Object)"serverUUID", (Object)Metrics.serverUUID);
        jsonObject.put((Object)"playerAmount", (Object)size);
        jsonObject.put((Object)"onlineMode", (Object)onlineMode);
        jsonObject.put((Object)"bukkitVersion", (Object)substring);
        jsonObject.put((Object)"javaVersion", (Object)property);
        jsonObject.put((Object)"osName", (Object)property2);
        jsonObject.put((Object)"osArch", (Object)property3);
        jsonObject.put((Object)"osVersion", (Object)property4);
        jsonObject.put((Object)"coreCount", (Object)availableProcessors);
        return jsonObject;
    }
    
    private void submitData() {
        final JSONObject serverData = this.getServerData();
        final JSONArray jsonArray = new JSONArray();
        for (final Class clazz : Bukkit.getServicesManager().getKnownServices()) {
            try {
                clazz.getField("B_STATS_VERSION");
                for (final RegisteredServiceProvider registeredServiceProvider : Bukkit.getServicesManager().getRegistrations((Class)clazz)) {
                    try {
                        jsonArray.add(registeredServiceProvider.getService().getMethod("getPluginData", (Class[])new Class[0]).invoke(registeredServiceProvider.getProvider(), new Object[0]));
                    }
                    catch (NullPointerException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {}
                }
            }
            catch (NoSuchFieldException ex2) {}
        }
        serverData.put((Object)"plugins", (Object)jsonArray);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendData(serverData);
                }
                catch (Exception thrown) {
                    if (Metrics.logFailedRequests) {
                        Metrics.this.plugin.getLogger().log(Level.WARNING, "Could not submit plugin stats of " + Metrics.this.plugin.getName(), thrown);
                    }
                }
            }
        }).start();
    }
    
    public abstract static class CustomChart
    {
        final String chartId;
        
        CustomChart(final String chartId) {
            if (chartId == null || chartId.isEmpty()) {
                throw new IllegalArgumentException("ChartId cannot be null or empty!");
            }
            this.chartId = chartId;
        }
        
        private JSONObject getRequestJsonObject() {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put((Object)"chartId", (Object)this.chartId);
            try {
                final JSONObject chartData = this.getChartData();
                if (chartData == null) {
                    return null;
                }
                jsonObject.put((Object)"data", (Object)chartData);
            }
            catch (Throwable thrown) {
                if (Metrics.logFailedRequests) {
                    Bukkit.getLogger().log(Level.WARNING, "Failed to get data for custom chart with id " + this.chartId, thrown);
                }
                return null;
            }
            return jsonObject;
        }
        
        protected abstract JSONObject getChartData();
    }
    
    public static class SimplePie extends CustomChart
    {
        private final Callable<String> callable;
        
        public SimplePie(final String s, final Callable<String> callable) {
            super(s);
            this.callable = callable;
        }
        
        @Override
        protected JSONObject getChartData() {
            final JSONObject jsonObject = new JSONObject();
            final String s = this.callable.call();
            if (s == null || s.isEmpty()) {
                return null;
            }
            jsonObject.put((Object)"value", (Object)s);
            return jsonObject;
        }
    }
    
    public static class AdvancedPie extends CustomChart
    {
        private final Callable<Map<String, Integer>> callable;
        
        public AdvancedPie(final String s, final Callable<Map<String, Integer>> callable) {
            super(s);
            this.callable = callable;
        }
        
        @Override
        protected JSONObject getChartData() {
            final JSONObject jsonObject = new JSONObject();
            final JSONObject jsonObject2 = new JSONObject();
            final Map<String, Integer> map = this.callable.call();
            if (map == null || map.isEmpty()) {
                return null;
            }
            boolean b = true;
            for (final Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 0) {
                    continue;
                }
                b = false;
                jsonObject2.put((Object)entry.getKey(), (Object)entry.getValue());
            }
            if (b) {
                return null;
            }
            jsonObject.put((Object)"values", (Object)jsonObject2);
            return jsonObject;
        }
    }
    
    public static class DrilldownPie extends CustomChart
    {
        private final Callable<Map<String, Map<String, Integer>>> callable;
        
        public DrilldownPie(final String s, final Callable<Map<String, Map<String, Integer>>> callable) {
            super(s);
            this.callable = callable;
        }
        
        public JSONObject getChartData() {
            final JSONObject jsonObject = new JSONObject();
            final JSONObject jsonObject2 = new JSONObject();
            final Map<String, Map<String, Integer>> map = this.callable.call();
            if (map == null || map.isEmpty()) {
                return null;
            }
            boolean b = true;
            for (final Map.Entry<String, Map<String, Integer>> entry : map.entrySet()) {
                final JSONObject jsonObject3 = new JSONObject();
                boolean b2 = true;
                for (final Map.Entry<String, Integer> entry2 : map.get(entry.getKey()).entrySet()) {
                    jsonObject3.put((Object)entry2.getKey(), (Object)entry2.getValue());
                    b2 = false;
                }
                if (!b2) {
                    b = false;
                    jsonObject2.put((Object)entry.getKey(), (Object)jsonObject3);
                }
            }
            if (b) {
                return null;
            }
            jsonObject.put((Object)"values", (Object)jsonObject2);
            return jsonObject;
        }
    }
    
    public static class SingleLineChart extends CustomChart
    {
        private final Callable<Integer> callable;
        
        public SingleLineChart(final String s, final Callable<Integer> callable) {
            super(s);
            this.callable = callable;
        }
        
        @Override
        protected JSONObject getChartData() {
            final JSONObject jsonObject = new JSONObject();
            final int intValue = this.callable.call();
            if (intValue == 0) {
                return null;
            }
            jsonObject.put((Object)"value", (Object)intValue);
            return jsonObject;
        }
    }
    
    public static class MultiLineChart extends CustomChart
    {
        private final Callable<Map<String, Integer>> callable;
        
        public MultiLineChart(final String s, final Callable<Map<String, Integer>> callable) {
            super(s);
            this.callable = callable;
        }
        
        @Override
        protected JSONObject getChartData() {
            final JSONObject jsonObject = new JSONObject();
            final JSONObject jsonObject2 = new JSONObject();
            final Map<String, Integer> map = this.callable.call();
            if (map == null || map.isEmpty()) {
                return null;
            }
            boolean b = true;
            for (final Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 0) {
                    continue;
                }
                b = false;
                jsonObject2.put((Object)entry.getKey(), (Object)entry.getValue());
            }
            if (b) {
                return null;
            }
            jsonObject.put((Object)"values", (Object)jsonObject2);
            return jsonObject;
        }
    }
    
    public static class SimpleBarChart extends CustomChart
    {
        private final Callable<Map<String, Integer>> callable;
        
        public SimpleBarChart(final String s, final Callable<Map<String, Integer>> callable) {
            super(s);
            this.callable = callable;
        }
        
        @Override
        protected JSONObject getChartData() {
            final JSONObject jsonObject = new JSONObject();
            final JSONObject jsonObject2 = new JSONObject();
            final Map<String, Integer> map = this.callable.call();
            if (map == null || map.isEmpty()) {
                return null;
            }
            for (final Map.Entry<String, Integer> entry : map.entrySet()) {
                final JSONArray jsonArray = new JSONArray();
                jsonArray.add((Object)entry.getValue());
                jsonObject2.put((Object)entry.getKey(), (Object)jsonArray);
            }
            jsonObject.put((Object)"values", (Object)jsonObject2);
            return jsonObject;
        }
    }
    
    public static class AdvancedBarChart extends CustomChart
    {
        private final Callable<Map<String, int[]>> callable;
        
        public AdvancedBarChart(final String s, final Callable<Map<String, int[]>> callable) {
            super(s);
            this.callable = callable;
        }
        
        @Override
        protected JSONObject getChartData() {
            final JSONObject jsonObject = new JSONObject();
            final JSONObject jsonObject2 = new JSONObject();
            final Map<String, int[]> map = this.callable.call();
            if (map == null || map.isEmpty()) {
                return null;
            }
            boolean b = true;
            for (final Map.Entry<String, int[]> entry : map.entrySet()) {
                if (entry.getValue().length == 0) {
                    continue;
                }
                b = false;
                final JSONArray jsonArray = new JSONArray();
                int[] array;
                for (int length = (array = entry.getValue()).length, i = 0; i < length; ++i) {
                    jsonArray.add((Object)array[i]);
                }
                jsonObject2.put((Object)entry.getKey(), (Object)jsonArray);
            }
            if (b) {
                return null;
            }
            jsonObject.put((Object)"values", (Object)jsonObject2);
            return jsonObject;
        }
    }
}
