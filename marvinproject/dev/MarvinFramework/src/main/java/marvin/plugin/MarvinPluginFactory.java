package marvin.plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hlw
 * @date 2016/9/25.
 */
public class MarvinPluginFactory {
    private static final Map<Class<? extends MarvinPlugin>, MarvinPlugin> plugins = new HashMap<>();

    public static <T extends MarvinPlugin> T get(Class<T> type) {
        T plugin = (T) plugins.get(type);
        if (plugin == null) {
            try {
                plugin = type.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            plugins.put(type, plugin);
            plugin.load();
        }
        return plugin;
    }
}
