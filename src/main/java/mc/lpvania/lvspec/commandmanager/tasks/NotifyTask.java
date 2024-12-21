package mc.lpvania.lvspec.commandmanager.tasks;

import mc.lpvania.lvspec.Plugin;
import mc.lpvania.lvspec.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Класс NotifyTask реализует задачу для отправки сообщений игрокам
 * через определённые интервалы времени.
 */
public class NotifyTask extends BukkitRunnable {
    private final Plugin plugin;

    /**
     * Конструктор NotifyTask.
     * Инициализирует задачу и автоматически запускает её.
     *
     * @param plugin основной экземпляр плагина, необходимый для доступа к конфигурации и API Bukkit.
     */
    public NotifyTask(Plugin plugin) {
        this.plugin = plugin;
        load();
    }

    /**
     * Метод для загрузки интервала задачи из конфигурации и её запуска.
     */
    void load() {
        int interval = plugin.getConfig().getInt("notification"); // Интервал в тиках
        this.runTaskTimer(plugin, interval, interval); // Запуск задачи
    }

    /**
     * Метод, который выполняется на каждом цикле задачи.
     * Отправляет сообщение всем текущим игрокам на сервере.
     */
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            new MessageUtil(player, "messages.spec_notification", true);
        }
    }
}
