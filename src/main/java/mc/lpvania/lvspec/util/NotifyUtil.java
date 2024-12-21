package mc.lpvania.lvspec.util;

import mc.lpvania.lvspec.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class NotifyUtil {


    public static void notifyAdmins(String playerNames) {
        String title = Plugin.getInstance().getConfig().getString("messages.adm.title").replace("{players}", playerNames);
        String subtitle = Plugin.getInstance().getConfig().getString("messages.adm.subtitle");
        String chatMessage = Plugin.getInstance().getConfig().getString("messages.adm.helper").replace("{players}", playerNames);
        String adminSound = Plugin.getInstance().getConfig().getString("sounds.admin");
        Iterator var6 = Bukkit.getOnlinePlayers().iterator();

        while(var6.hasNext()) {
            Player onlinePlayer = (Player)var6.next();
            if (onlinePlayer.hasPermission("SoulSpec.*")) {
                onlinePlayer.sendTitle(ColorUtil.hex(title), ColorUtil.hex(subtitle), 10, 70, 20);
                onlinePlayer.sendMessage(ColorUtil.hex(chatMessage));
                if (adminSound != null && !adminSound.isEmpty()) {
                    try {
                        onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.valueOf(adminSound), 1.0F, 1.0F);
                    } catch (IllegalArgumentException var9) {
                        Plugin.getInstance().getLogger().warning("Invalid sound specified for admin: " + adminSound);
                    }
                }
            }
        }
    }

    public static void notifyPlayer(Player player) {
        String playerSound = Plugin.getInstance().getConfig().getString("sounds.player");
        SoundUtil.playSound(player, "sounds.player");

    }

}
