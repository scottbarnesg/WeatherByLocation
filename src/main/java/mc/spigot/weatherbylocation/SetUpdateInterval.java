package mc.spigot.weatherbylocation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;

public class SetUpdateInterval implements CommandExecutor {

    private final WeatherByLocation plugin;

    public SetUpdateInterval(WeatherByLocation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Validate input
        if (args.length != 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.DARK_RED + "Invalid input provided. Expected format is /set-weather-update-interval XX");
            }
            getLogger().info("Invalid input provided. Expected format is /set-weather-update-interval XX");
            return false;
        }
        // Validate input type
        String minutes = args[0];
        int interval ;
        try {
            interval = Integer.parseInt(minutes);
        }
        catch (NumberFormatException e) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.DARK_RED + "Invalid input provided. Update interval must be a positive integer.");
            }
            getLogger().info("Invalid input provided. Update interval must be a positive integer.");
            return false;
        }
        if (interval < 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.DARK_RED + "Invalid input provided. Update interval must be a positive integer.");
            }
            getLogger().info("Invalid input provided. Update interval must be a positive integer.");
            return false;
        }
        // Write values to config file
        plugin.getConfig().set("minutes-between-updates", interval);
        plugin.saveConfig();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.AQUA + String.format("Set update interval to %d minutes. This will take effect after the next update.", interval));
        }
        getLogger().info(String.format("Set update interval to %d minutes. This will take effect after the next update.", interval));
        return true;
    }
}
