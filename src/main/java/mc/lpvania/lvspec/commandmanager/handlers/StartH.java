package mc.lpvania.lvspec.commandmanager.handlers;

import lombok.val;
import mc.lpvania.lvspec.Plugin;
import mc.lpvania.lvspec.commandmanager.MainCommand;
import mc.lpvania.lvspec.util.MessageUtil;
import mc.lpvania.lvspec.util.SoundUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class StartH {

    public static void init(Player player, String[] args) {
        if (args.length != 2) {
            new MessageUtil(player, "messages.adm.help", true);
            return;
        }

        String targetName = args[1];
        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            val text = Plugin.getInstance().getConfig().getString("messages.spec_no_request").replace("{player}", targetName);
            new MessageUtil(player, text, false);
            return;
        }

        if (MainCommand.getInstance().getPlayersInSpecMode().contains(player)) {
            new MessageUtil(player, "messages.spec_already_in_use", true);
            return;
        }

        if (!MainCommand.getInstance().getContest().contains(target)) {
            val text = Plugin.getInstance().getConfig().getString("messages.spec_no_request").replace("{player}", targetName);
            new MessageUtil(player, text, false);
            return;
        }

        MainCommand.getInstance().getWatchMap().put(player, target);
        MainCommand.getInstance().getPlayersInSpecMode().add(player);
        MainCommand.getInstance().getContest().remove(target);

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:vanish " + player.getName() + " on");
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(target.getLocation());

        SoundUtil.playSound(player, "sounds.admin");
        val text = Plugin.getInstance().getConfig().getString("messages.spec_started").replace("{player}", targetName);
        new MessageUtil(player, text, false);
    }

}
