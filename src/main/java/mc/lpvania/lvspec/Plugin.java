package mc.lpvania.lvspec;

import lombok.Getter;
import lombok.Setter;
import mc.lpvania.lvspec.commandmanager.MainCommand;
import mc.lpvania.lvspec.util.NotifyUtil;
import mc.lpvania.lvspec.util.UpdaterUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class Plugin extends JavaPlugin {

    @Getter
    private static Plugin instance;
    private PluginM pluginm;
    @Getter
    @Setter
    private long SpecRequests;
    @Getter
    private final Map<Player, Long> specCooldownMap = new HashMap<>();
    @Getter
    @Setter
    private long cooldownTime;
    @Getter
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.pluginm = new PluginM(this);
        pluginm.init();
    }

    @Override
    public void onDisable() {
        pluginm.dispose();
        instance = null;
    }
}
