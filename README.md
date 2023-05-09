# WeatherByLocation Plugin

A Spigot/Paper plugin that sets the weather on your minecraft server based on the actual weather at a chosen location. 

## Setup

1. [Download the latest version of WeatherByLocation-X.Y.Z.jar](https://github.com/scottbarnesg/WeatherByLocation/releases) and copy it to the `plugins` folder of your minecraft server directory.
2. Restart the server. This will generate a `WeatherByLocation` directory in your `plugins` folder with a default configuration file.

    By default, the WeatherByLocation plugin will attempt to geolocate your server using its public IP address. If this is the desired behavior, no additional setup is required.

    You should see something like this in the server logs:

    ```
    my-server  | [18:08:29 INFO]: [WeatherByLocation] Identified this server's public facing IP address as XXX.XXX.XXX.XXX.
    my-server  | [18:08:29 INFO]: [WeatherByLocation] Server location identified as City, Region, Country at coordinates (XX.XXX, YY.YYY)
    my-server | [18:08:34 INFO]: [WeatherByLocation] Current weather for (XX.XXX, YY.YYY) is Clear. Weather data by Open-Metro.com
    my-server | [18:08:34 INFO]: [WeatherByLocation] Weather was set to Clear.
    ```

3. [Optional] If desired, use the `/set-weather-location` command to set a custom latitude and longitude for the server to use to pull weather data.

## Commands

- `set-weather-location`: set the location to be used to pull weather data, by latitude and longitude.

    - Example usage: `/set-weather-location 30.266 -97.733`

- `get-weather-location`: get the location currently in use to pull weather data, in latitude and longitude.

    - Example usage: `/get-weather-data`

## External Services:

WeatherByLocation uses the following external services:

- [Open Metro Weather API](https://github.com/scottbarnesg/WeatherByLocation): An open source Weather API.**
- [Ipify API](https://www.ipify.org/): An open source API to determine the server's IP address.***
- [IP API](https://ip-api.com/): An IP address geolocation API.***

    ** Pulls weather data. Always in use.

    *** Used to geolocate the server. Only used if no location is explicitly specified in the config file.


## Supported Versions

- Java 11 or greater.
- Spigot/Paper 1.13 or greater.