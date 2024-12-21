package mc.lpvania.lvspec.commandmanager.handlers;

import lombok.val;
import mc.lpvania.lvspec.Plugin;
import mc.lpvania.lvspec.PluginM;
import mc.lpvania.lvspec.commandmanager.MainCommand;
import mc.lpvania.lvspec.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class OffH {

    public static void init(Player player) {
        if (!MainCommand.getInstance().getWatchMap().containsKey(player)) {
            new MessageUtil(player, "messages.spec_not_watching", true);
            return;
        }

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

        MainCommand.getInstance().getWatchMap().remove(player);
        MainCommand.getInstance().getPlayersInSpecMode().remove(player);
        player.setGameMode(GameMode.SURVIVAL);

        val text = Plugin.getInstance().getConfig().getString("messages.spec_stopped").replace("{player}", player.getName());
        new MessageUtil(player, text, false);
    }

}
