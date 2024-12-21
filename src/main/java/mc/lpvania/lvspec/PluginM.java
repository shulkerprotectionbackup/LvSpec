package mc.lpvania.lvspec;

import mc.lpvania.lvspec.commandmanager.MainCommand;
import mc.lpvania.lvspec.commandmanager.tasks.NotifyTask;
import mc.lpvania.lvspec.listener.EventListener;
import mc.lpvania.lvspec.util.NotifyUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PluginM {

    private final Plugin plugin;
    private boolean notificationsSent = false;

    public PluginM(Plugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
//        Bukkit.getLogger().warning("§f");
//        Bukkit.getLogger().warning("§f  §a§lПЛАГИН ВКЛЮЧЁН");
//        Bukkit.getLogger().warning("§f §6╔ §fПлагин: §e" + "LPVania" + "§f §7|§f Текущая версия: §e" + "v1.0.0" + "§f §7|§f Новая версия: §e" + VER_NEW + "§f");
//        Bukkit.getLogger().warning("§f §6╚ §fCоздатель: §a§n" +"LPVania"+ "§f §7|§f Телеграм: §a§n" +"t.me/LPVania" + "§f §7|§f Дискорд: §a§n" +"inventorytype" + "§f");
//        Bukkit.getLogger().warning("§f");

        new MainCommand();
        registerConfig();
        registerEvents();
        registerCommands();
        registerTasks();
    }

    public void dispose() {
        MainCommand.getInstance().getPlayersInSpecMode().forEach(player -> {
            MainCommand.getInstance().getWatchMap().remove(player);

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:vanish " + player.getName() + " off");

            player.setGameMode(GameMode.SURVIVAL);
        });

        MainCommand.getInstance().getPlayersInSpecMode().clear();
        Plugin.getInstance().getScheduler().shutdown();
        Plugin.getInstance().getLogger().info("SoulSpec plugin disabled.");
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

            this.notificationsSent = true;
        }
    }


    void registerConfig() {
        Plugin.getInstance().setSpecRequests(Plugin.getInstance().getConfig().getLong("SpecRequests"));
        Plugin.getInstance().setCooldownTime(Plugin.getInstance().getConfig().getLong("cooldown") * 60000L);
    }
}
