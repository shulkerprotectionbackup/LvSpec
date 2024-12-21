package mc.lpvania.lvspec.util;

import mc.lpvania.lvspec.Plugin;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtil {

    public static void playSound(Player player, String soundKey) {
        String soundName = Plugin.getInstance().getConfig().getString(soundKey);
        if (soundName != null) {
            try {
                Sound sound = Sound.valueOf(soundName);
                player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
            } catch (IllegalArgumentException e) {
                Plugin.getInstance().getLogger().warning("Invalid sound specified in config: " + soundName);
            }
        }
    }

}
