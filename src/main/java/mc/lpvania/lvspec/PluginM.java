package mc.lpvania.lvspec;

import mc.lpvania.lvspec.commandmanager.MainCommand;
import mc.lpvania.lvspec.tasks.NotifyTask;
import mc.lpvania.lvspec.listener.EventListener;
import mc.lpvania.lvspec.util.NotifyUtil;
import mc.lpvania.lvspec.util.UpdaterUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PluginM {

    private final Plugin plugin;
    private boolean notificationsSent = false;
    private UpdaterUtil updater;

    public PluginM(Plugin plugin) {
        this.plugin = plugin;
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
        MainCommand.getInstance().getPlayersInSpecMode().forEach(player -> {
            MainCommand.getInstance().getWatchMap().remove(player);

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:vanish " + player.getName() + " off");

            player.setGameMode(GameMode.SURVIVAL);
        });

        MainCommand.getInstance().getPlayersInSpecMode().clear();
        Plugin.getInstance().getScheduler().shutdown();
    }




    void registerEvents() {
        Plugin.getInstance().getServer().getPluginManager().registerEvents(new EventListener(), plugin);
    }

    void registerCommands() {
        PluginCommand command = plugin.getCommand("lvspec");
        if (command != null) {
            command.setExecutor(new MainCommand());
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
