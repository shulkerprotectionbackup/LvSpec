package mc.lpvania.lvspec.util;

import lombok.val;
import mc.lpvania.lvspec.Plugin;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageUtil {

    /**
     * Конструктор класса, который решает, как отправить сообщение игроку.
     *
     * @param player Игрок, которому нужно отправить сообщение.
     * @param m Ключ конфигурации или прямое текстовое сообщение.
     * @param b Если true, ищет сообщение в конфигурации. Если false, отправляет текст напрямую.
     *          Если ключ найден в конфиге, то отправляется это сообщение. В противном случае сообщение не отправляется.
     */
    public MessageUtil(@NotNull Player player, @NotNull String m, boolean b) {
        val pl = Plugin.getInstance().getConfig();

        if (b) {
            val message = pl.getString(m);
            if (message == null) {
                return;
            }
            send(player, message);
        } else {
            send(player, m);
        }
    }

    /**
     * Конструктор класса, который решает, как отправить сообщение отправителю команды.
     *
     * @param player Игрок или другой CommandSender, которому нужно отправить сообщение.
     * @param m Ключ конфигурации или прямое текстовое сообщение.
     * @param b Если true, ищет сообщение в конфигурации. Если false, отправляет текст напрямую.
     *          Если ключ найден в конфиге, то отправляется это сообщение. В противном случае сообщение не отправляется.
     */
    public MessageUtil(@NotNull CommandSender player, @NotNull String m, boolean b) {
        val pl = Plugin.getInstance().getConfig();

        if (b) {
            val message = pl.getString(m);
            if (message == null) {
                return;
            }
            send(player, message);
        } else {
            send(player, m);
        }
    }

    /**
     * Отправляет сообщение CommandSender с обработкой цветов.
     * Этот метод используется для отправки строки сообщения пользователю с использованием цветовых кодов.
     *
     * @param player Игрок или другой CommandSender, которому нужно отправить сообщение.
     * @param message Текст сообщения, которое будет отправлено.
     */
    public void send(@NotNull CommandSender player, String message) {
        val msg = ColorUtil.hex(message);
        player.sendMessage(msg);
    }

    /**
     * Конструктор класса, который отправляет сообщение игроку в виде объекта BaseComponent.
     * Этот метод используется для отправки более сложных сообщений с компонентами, такими как сообщения с кликабельными ссылками или изменяемыми текстами.
     *
     * @param player Игрок, которому необходимо отправить сообщение.
     * @param m Сообщение в виде объекта BaseComponent, которое будет отправлено игроку.
     */
    public MessageUtil(@NotNull Player player, @NotNull BaseComponent m) {
        player.sendMessage(m);
    }

    /**
     * Отправляет сообщение игроку с обработкой цветов.
     * Этот метод используется для отправки обычного текстового сообщения игроку с обработкой цветовых кодов.
     *
     * @param player Игрок, которому нужно отправить сообщение.
     * @param message Текст сообщения, которое будет отправлено.
     */
    public void send(Player player, String message) {
        val msg = ColorUtil.hex(message);
        player.sendMessage(msg);
    }

}
