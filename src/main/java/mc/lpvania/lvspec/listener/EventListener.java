package mc.lpvania.lvspec.listener;

import mc.lpvania.lvspec.Plugin;
import mc.lpvania.lvspec.commandmanager.MainCommand;
import mc.lpvania.lvspec.util.ColorUtil;
import mc.lpvania.lvspec.util.NotifyUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class EventListener implements Listener {

    private final Plugin plugin = Plugin.getInstance();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (message.equalsIgnoreCase("spec") || message.equalsIgnoreCase("!spec") || message.equalsIgnoreCase("спек") || message.equalsIgnoreCase("!спек")) {
            long currentTime = System.currentTimeMillis();
            long lastSpecTime = Plugin.getInstance().getSpecCooldownMap().getOrDefault(player, 0L);
            if (currentTime - lastSpecTime < Plugin.getInstance().getCooldownTime()) {
                player.sendMessage(ColorUtil.hex(plugin.getConfig().getString("messages.spec_cooldown")));
                event.setCancelled(true);
                return;
            }

            Plugin.getInstance().getSpecCooldownMap().put(player, currentTime);
            MainCommand.getInstance().getPlayersWhoRequestedSpec().add(player);
            player.sendMessage(ColorUtil.hex(plugin.getConfig().getString("messages.request_player")));
            NotifyUtil.notifyPlayer(player);
            event.setCancelled(true);
        }

    }


}
