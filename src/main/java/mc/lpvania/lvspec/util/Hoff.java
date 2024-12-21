package mc.lpvania.lvspec.util;

import lombok.val;
import mc.lpvania.lvspec.Plugin;
import mc.lpvania.lvspec.PluginM;
import mc.lpvania.lvspec.commandmanager.MainCommand;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Hoff {

    public static void offDispose() {
        MainCommand.getInstance().getPlayersInSpecMode().forEach(player -> {
            MainCommand.getInstance().getWatchMap().remove(player);

            val type = PluginM.getInstance().getType();

            if (type == null) {
                return;
            } else if (type.equals("cmi")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "vanish " + player.getName() + " false");
            } else if (type.equals("essx")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:vanish " + player.getName() + " off");
            }

            player.setGameMode(GameMode.SURVIVAL);
        });

        MainCommand.getInstance().getPlayersInSpecMode().clear();
        Plugin.getInstance().getScheduler().shutdown();
    }

}
