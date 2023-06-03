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

        // Validate inputs
        // check for wrong number of arguments
        if (args.length != 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.DARK_RED + "Invalid input provided. Expected format is /set-weather-update-interval XX");
            }
            getLogger().info("Invalid input provided. Expected format is /set-weather-update-interval XX");
            return false;
        }

        // check for non-numeric inputs
        String minutes = args[0];
        int interval ;
        try {
            interval = Integer.parseInt(minutes);
        }
        catch (NumberFormatException e) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.DARK_RED + "Bad input provided. Input is to be positive number of 1 or more and the expected format is /set-weather-update-interval XX");
            }
            getLogger().info("Bad input provided. Input is to be positive number of 1 or more and the expected format is /set-weather-update-interval XX");
            return false;
        }

        // check for input being outside of accepted limits
        if(interval < 1){
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.DARK_RED + "Input value too low. Input is to be positive number of 1 or more and the expected format is /set-weather-update-interval XX");
            }
            getLogger().info("Input value too low. Input is to be positive number of 1 or more and the expected format is /set-weather-update-interval XX");
            return false;
        }

        // Write values to config file
        plugin.getConfig().set("minutes-between-updates", interval);
        plugin.saveConfig();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.AQUA + String.format("Set update interval to (%d) minutes", interval));
            player.sendMessage(ChatColor.DARK_RED + String.format("New changes to be reflected upon restart of server or reload of plugin"));
        }
        getLogger().info(String.format("Set update interval to (%d) minutes", interval));
        getLogger().info(String.format("New changes to be reflected upon restart of server or reload of plugin"));

        return true;
    }

}
