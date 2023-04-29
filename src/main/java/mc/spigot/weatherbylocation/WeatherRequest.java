package mc.spigot.weatherbylocation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class WeatherRequest {
    public static int getCurrentWeather(double lat, double lon) throws IOException, InterruptedException {
        Logger logger = Logger.getLogger("WeatherByLocation");
        // Send request to Open Meteo API
        String requestUrlString = "https://api.open-meteo.com/v1/forecast?latitude=%.2f&longitude=%.2f&current_weather=true".formatted(lat, lon);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrlString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Parse JSON response
        String responseBody = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        // Get Weather codes
        int weatherCode = jsonNode.get("current_weather").get("weathercode").asInt();
        logger.info("Weather code for (%.2f, %.2f) is %d.".formatted(lat, lon, weatherCode));
        return weatherCode;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
         WeatherRequest.getCurrentWeather(30.58, 97.85);
    }
}
