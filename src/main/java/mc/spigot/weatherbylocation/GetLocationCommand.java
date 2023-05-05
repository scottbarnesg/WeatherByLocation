package mc.spigot.weatherbylocation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetLocationCommand implements CommandExecutor {

    private final WeatherByLocation plugin;

    public GetLocationCommand(WeatherByLocation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ServerLocator.LocationData locationData = plugin.getLocationData();
        String message = "Location used for weather data is (%.3f, %.3f)".formatted(locationData.latitude, locationData.longitude);
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.AQUA + message);
        }
        else {
            plugin.getLogger().info(message);
        }
        return true;
    }
}
