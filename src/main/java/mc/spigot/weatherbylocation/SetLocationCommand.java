package mc.spigot.weatherbylocation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;

public class SetLocationCommand implements CommandExecutor {

    private final WeatherByLocation plugin;

    public SetLocationCommand(WeatherByLocation plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Validate inputs
        if (args.length != 2) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.DARK_RED + "Invalid latitude and longitude provided. Expected format is /set-weather-location XX.XXX YY.YYY");
            }
            getLogger().info("Invalid latitude and longitude provided. Expected format is /set-weather-location XX.XXX YY.YYY");
            return false;
        }
        String latString = args[0];
        String lonString = args[1];
        float lat, lon;
        try {
            lat = Float.parseFloat(latString);
            lon = Float.parseFloat(lonString);
        }
        catch (NumberFormatException e) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.DARK_RED + "Invalid latitude and longitude provided. Expected format is /set-weather-location XX.XXX YY.YYY");
            }
            getLogger().info("Invalid latitude and longitude provided. Expected format is /set-weather-location XX.XXX YY.YYY");
            return false;
        }
        // Write values to config file
        plugin.getConfig().set("latitude", lat);
        plugin.getConfig().set("longitude", lon);
        plugin.saveConfig();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.AQUA + String.format("Set weather location to (%.3f, %.3f)", lat, lon));
        }
        getLogger().info(String.format("Set weather location to (%.3f, %.3f)", lat, lon));
        plugin.reloadConfig();
        plugin.loadLocationDataFromConfig();
        return true;
    }
}
