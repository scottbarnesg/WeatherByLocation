package mc.spigot.weatherbylocation;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WeatherByLocation extends JavaPlugin {
    public enum WeatherType {
        CLEAR,
        RAIN,
        THUNDERSTORM
    }
    List<Integer> clearWeatherCodes = Arrays.asList(0, 1, 2, 3);
    List<Integer> rainWeatherCodes = Arrays.asList(45, 48, 51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 71, 73, 75, 77, 80,
            81, 82, 85, 86);
    List<Integer> thunderstormWeatherCodes = Arrays.asList(95, 96, 99);
    BukkitTask updateWeatherTask;
    // Config
    double latitude;
    double longitude;
    int minutesBetweenUpdates;

    @Override
    public void onEnable() {
        getLogger().info("WeatherByLocation was enabled.");
        // Create default configuration file, if it doesn't exist
        saveDefaultConfig();
        // Load static configuration
        minutesBetweenUpdates = getConfig().getInt("minutes-between-updates");
        // Schedule task to run every minutesBetweenUpdates minutes
        BukkitScheduler scheduler = getServer().getScheduler();
        updateWeatherTask = scheduler.runTaskTimerAsynchronously(this, () -> {
            try {
                // Get latest values from config
                reloadConfig();
                latitude = getConfig().getDouble("latitude");
                longitude = getConfig().getDouble("longitude");
                // Fetch weather data
                WeatherType weatherType = getCurrentWeather(latitude, longitude);
                // Update weather on server
                setWeather(weatherType);
            } catch (IOException | InterruptedException e) {
                getLogger().warning("Error fetching weather data.");
                getLogger().warning(e.toString());
            }
        }, 0L, (20L * 60 * minutesBetweenUpdates));
    }
    @Override
    public void onDisable() {
        updateWeatherTask.cancel();
        getLogger().info("WeatherByLocation was disabled.");
    }

    private void setWeather(WeatherType weatherType) {
        World world = Bukkit.getWorlds().get(0);
        if (weatherType.equals(WeatherType.CLEAR)) {
            world.setStorm(false);
            world.setThundering(false);
            getLogger().info("Weather was set to Clear.");
        }
        else if (weatherType.equals(WeatherByLocation.WeatherType.RAIN)) {
            world.setStorm(true);
            world.setThundering(false);
            getLogger().info("Weather was set to Rain.");
        }
        else if (weatherType.equals(WeatherByLocation.WeatherType.THUNDERSTORM)) {
            world.setStorm(true);
            world.setThundering(false);
            getLogger().info("Weather was set to Thunderstorm.");
        }
    }

    private WeatherType getCurrentWeather(double lat, double lon) throws IOException, InterruptedException {
        // Send request to Open Meteo API
        int weatherCode = WeatherRequest.getCurrentWeather(lat, lon);
        if (rainWeatherCodes.contains(weatherCode)) {
            return WeatherType.RAIN;
        }
        else if (thunderstormWeatherCodes.contains(weatherCode)) {
            return WeatherType.THUNDERSTORM;
        }
        else {
            return WeatherType.CLEAR;
        }
    }

    public static void main(String[] args) {
        WeatherByLocation weatherByLocation = new WeatherByLocation();
        weatherByLocation.onEnable();
    }
}
