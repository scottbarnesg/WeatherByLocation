package mc.spigot.weatherbylocation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class ServerLocator {
    private String ipAddress;
    private LocationData locationData;

    private final WeatherByLocation plugin;

    public ServerLocator(WeatherByLocation plugin) {
        this.plugin = plugin;
    }

    public static class LocationData {
        public String countryCode;
        public String region;
        public String city;
        public double latitude;
        public double longitude;
    }

    public LocationData locate() throws IOException, InterruptedException {
        ipAddress = fetchIpAddress();
        FileConfiguration config = plugin.getConfig();

        if(config.contains("ipAddress") && config.getString("ipAddress").equals(ipAddress)){
            locationData =  locationDataFromConfig();
        }else{
            locationData = geolocateIpAddress(ipAddress);
        }

        return locationData;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LocationData getLocationData() {
        return locationData;
    }

    private static String fetchIpAddress() throws IOException, InterruptedException {
        Logger logger = Logger.getLogger("WeatherByLocation");
        // Send request to Ipify API
        String requestUrlString = "https://api.ipify.org/";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrlString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Reponse is just a string containing the IP address
        String result = response.body();
        logger.info(String.format("Identified this server's public facing IP address as %s.", result));
        return response.body();
    }

    private  LocationData geolocateIpAddress(String ipAddress) throws IOException, InterruptedException {
        Logger logger = Logger.getLogger("WeatherByLocation");
        String requestUrlString = String.format("http://ip-api.com/json/%s", ipAddress);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrlString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Parse JSON response
        String responseBody = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        // Create LocationData object and return it
        LocationData locationResult = new LocationData();
        locationResult.countryCode = jsonNode.get("countryCode").asText();
        locationResult.region = jsonNode.get("region").asText();
        locationResult.city = jsonNode.get("city").asText();
        locationResult.latitude = jsonNode.get("lat").asDouble();
        locationResult.longitude = jsonNode.get("lon").asDouble();

        // copy LocationData object to config.yml
        saveDataToConfig(ipAddress,locationResult);

        // Log result
        logger.info(String.format("Server location identified as %s, %s, %s at coordinates (%.3f, %.3f)", locationResult.city, locationResult.region, locationResult.countryCode, locationResult.latitude, locationResult.longitude));
        return locationResult;
    }

    private  LocationData locationDataFromConfig() {

        Logger logger = Logger.getLogger("WeatherByLocation");

        LocationData locationResult = new LocationData();

        locationResult.countryCode = plugin.getConfig().getString("countryCode");
        locationResult.region = plugin.getConfig().getString("region");
        locationResult.city = plugin.getConfig().getString("city");
        locationResult.latitude = plugin.getConfig().getDouble("latitude");
        locationResult.longitude = plugin.getConfig().getDouble("longitude");

        // Log result
        logger.info(String.format("Server location identified as %s, %s, %s at coordinates (%.3f, %.3f)", locationResult.city, locationResult.region, locationResult.countryCode, locationResult.latitude, locationResult.longitude));
        return locationResult;
}

    private  void saveDataToConfig(String ipAddress, LocationData locationResult) {

        // set values of new locationData object to config.yml file
        plugin.getConfig().set("ipAddress", ipAddress);
        plugin.getConfig().set("countryCode",  locationResult.countryCode);
        plugin.getConfig().set("region", locationResult.region);
        plugin.getConfig().set("city",  locationResult.city);
        plugin.getConfig().set("latitude", locationResult.latitude);
        plugin.getConfig().set("longitude",  locationResult.longitude);

        // persist changes made to config.yml to disk
        plugin.saveConfig();

    }

}
