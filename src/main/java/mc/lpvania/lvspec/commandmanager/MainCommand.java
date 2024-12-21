package mc.lpvania.lvspec.commandmanager;

import lombok.Getter;
import lombok.Setter;
import mc.lpvania.lvspec.Plugin;
import mc.lpvania.lvspec.commandmanager.handlers.OffH;
import mc.lpvania.lvspec.commandmanager.handlers.StartH;
import mc.lpvania.lvspec.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainCommand implements CommandExecutor {

    private final Plugin plugin = Plugin.getInstance();
    @Getter
    private static MainCommand instance;
    @Getter
    private final Set<Player> playersInSpecMode = new HashSet<>();
    @Getter
    private final Set<Player> playersWhoRequestedSpec = new HashSet<>();
    @Getter
    private final Map<Player, Player> watchMap = new HashMap<>();

    public MainCommand() {
        instance = this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("SoulSpec.*")) {
            sendMessage(player, "messages.no_permission");
            return true;
        }

        if (args.length < 1) {
            sendMessage(player, "messages.adm.help");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "start":
                StartH.init(player, args);
                break;

            case "off":
                OffH.init(player);
                break;

            default:
                sendMessage(player, "messages.adm.help");
                break;
        }

        return true;
    }

    private void sendMessage(Player player, String configKey, String... replacements) {
        String message = plugin.getConfig().getString(configKey);
        if (message != null) {
            for (int i = 0; i < replacements.length; i += 2) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
            player.sendMessage(ColorUtil.hex(message));
        }
    }
}
