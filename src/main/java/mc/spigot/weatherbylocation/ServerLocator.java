package mc.spigot.weatherbylocation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class ServerLocator {
    private String ipAddress;
    private LocationData locationData;

    public static class LocationData {
        public String countryCode;
        public String region;
        public String city;
        public double latitude;
        public double longitude;
    }

    public LocationData locate() throws IOException, InterruptedException {
        ipAddress = fetchIpAddress();
        locationData = geolocateIpAddress(ipAddress);
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
        logger.info("Identified this server's public facing IP address as %s.".formatted(result));
        return response.body();
    }

    private static LocationData geolocateIpAddress(String ipAddress) throws IOException, InterruptedException {
        Logger logger = Logger.getLogger("WeatherByLocation");
        String requestUrlString = "http://ip-api.com/json/%s".formatted(ipAddress);
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
        // Log result
        logger.info("Server location identified as %s, %s, %s at coordinates (%.3f, %.3f)".formatted(locationResult.city, locationResult.region, locationResult.countryCode, locationResult.latitude, locationResult.longitude));
        return locationResult;
    }
}
