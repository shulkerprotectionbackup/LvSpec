package mc.lpvania.lvspec;

import lombok.Getter;
import lombok.val;
import mc.lpvania.lvspec.commandmanager.MainCommand;
import mc.lpvania.lvspec.tasks.NotifyTask;
import mc.lpvania.lvspec.listener.EventListener;
import mc.lpvania.lvspec.util.Hoff;
import mc.lpvania.lvspec.util.NotifyUtil;
import mc.lpvania.lvspec.util.UpdaterUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PluginM {

    private final Plugin plugin;
    @Getter
    private static PluginM instance;
    private boolean notificationsSent = false;
    private UpdaterUtil updater;
    @Getter
    private String type;

    public PluginM(Plugin plugin) {
        this.plugin = plugin;
        instance = this;
    }

    public void init() {
        this.updater = new UpdaterUtil();
        updater.init();
        onInfo();
        new MainCommand();

        registerConfig();
        registerEvents();
        registerCommands();
        registerTasks();

        checkSystem();
    }

    void checkSystem() {
        val cmi = Bukkit.getPluginManager().getPlugin("CMI");
        val essx = Bukkit.getPluginManager().getPlugin("Essentials");

        if (cmi != null && cmi.isEnabled()) {
            type = "cmi";
        } else if (essx != null && essx.isEnabled()) {
            type = "essx";
        } else {
            Bukkit.getLogger().info("Не найдены ни CMI, ни Essentials.");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    void onInfo() {
        Bukkit.getLogger().warning("§f");
        Bukkit.getLogger().warning("§f  §a§lПЛАГИН ВКЛЮЧЁН");
        Bukkit.getLogger().warning("§f §6╔ §fПлагин: §e" + updater.name_h + "§f §7|§f Текущая версия: §e" + updater.version_h + "§f §7|§f Новая версия: §e" + updater.getVersion() + "§f");
        Bukkit.getLogger().warning("§f §6╚ §fCоздатель: §a§n" + updater.getAuthor() + "§f §7|§f Телеграм: §a§n" + updater.getTelegram() + "§f §7|§f Дискорд: §a§n" + updater.getDiscord() + "§f");
        Bukkit.getLogger().warning("§f");
        Bukkit.getLogger().warning(" §8§n|§f Студия по разработке > §e" + updater .getTelegramStudio());
        Bukkit.getLogger().warning(" §8§n|§f Послепдний раз обновление");
        Bukkit.getLogger().warning(" §8§n|§f Плагина было > §a" + updater.getTimeLastUpdate());
        Bukkit.getLogger().warning("§f");
    }

    void offInfo() {
        Bukkit.getLogger().warning("§f");
        Bukkit.getLogger().warning("§f  §c§lПЛАГИН ВЫКЛЮЧЕН");
        Bukkit.getLogger().warning("§f §6╔ §fПлагин: §e" + updater.name_h + "§f §7|§f Текущая версия: §e" + updater.version_h + "§f §7|§f Новая версия: §e" + updater.getVersion() + "§f");
        Bukkit.getLogger().warning("§f §6╚ §fCоздатель: §a§n" +updater.getAuthor()+ "§f §7|§f Телеграм: §a§n" +updater.getTelegram() + "§f §7|§f Дискорд: §a§n" +updater.getDiscord() + "§f");
        Bukkit.getLogger().warning("§f");
        Bukkit.getLogger().warning(" §8§n|§f Студия по разработке > §e" + updater .getTelegramStudio());
        Bukkit.getLogger().warning(" §8§n|§f Последний раз обновление");
        Bukkit.getLogger().warning(" §8§n|§f Плагина было > §a" + updater.getTimeLastUpdate());
        Bukkit.getLogger().warning("§f");
    }

    public void dispose() {
        offInfo();
        Hoff.offDispose();
    }




    void registerEvents() {
        Plugin.getInstance().getServer().getPluginManager().registerEvents(new EventListener(), plugin);
    }

    void registerCommands() {
        PluginCommand command = plugin.getCommand("lvspec");
        if (command != null) {
            command.setExecutor(new MainCommand());
            command.setAliases(Arrays.asList("spec", "cheat"));
        }
    }
    void registerTasks() {
        new NotifyTask(plugin);
        plugin.getScheduler().scheduleAtFixedRate(this::handleSpecRequests, 0L, plugin.getSpecRequests(), TimeUnit.SECONDS);
    }

    public void handleSpecRequests() {
        if (MainCommand.getInstance().getPlayersWhoRequestedSpec().isEmpty()) {
            this.notificationsSent = false;
        } else if (!this.notificationsSent) {
            String names = String.join(", ",
                    MainCommand.getInstance().getPlayersWhoRequestedSpec()
                            .stream()
                            .map(Player::getName)
                            .collect(Collectors.toList())
            );


            NotifyUtil.notifyAdmins(names);
            MainCommand.getInstance().getPlayersWhoRequestedSpec().clear();

            this.notificationsSent = true;
        }
    }


    void registerConfig() {
        Plugin.getInstance().setSpecRequests(Plugin.getInstance().getConfig().getLong("SpecRequests"));
        Plugin.getInstance().setCooldownTime(Plugin.getInstance().getConfig().getLong("cooldown") * 60000L);
    }
}
