# WeatherByLocation Plugin

A Spigot/Paper plugin that sets the weather on your minecraft server based on the actual weather at a chosen location. 

## Setup

1. [Dowload the latest version of WeatherByLocation-X.Y.Z.jar](https://github.com/scottbarnesg/WeatherByLocation/releases) and copy it to the `plugins` folder of your minecraft server directory.
2. Restart the server. This will generate a `WeatherByLocation` directory in your `plugins` folder with a default configuration file.

    By default, the WeatherByLocation plugin will attempt to geolocate your server using its public IP address. If this is the desired behavior, no additional setup is required.

    You should see something like this in the server logs:

    ```
    my-server  | [18:08:29 INFO]: [WeatherByLocation] Identified this server's public facing IP address as XXX.XXX.XXX.XXX.
    my-server  | [18:08:29 INFO]: [WeatherByLocation] Server location identified as City, Region, Country at coordinates (XX.XXX, YY.YYY)
    my-server | [18:08:34 INFO]: [WeatherByLocation] Current weather for (XX.XXX, YY.YYY) is Clear. Weather data by Open-Metro.com
    my-server | [18:08:34 INFO]: [WeatherByLocation] Weather was set to Clear.
    ```

3. [Optional] Navigate into the `WeatherByLocation` folder and edit `config.yml`. 
4. [Optional] Update the values of `latitude` and `longitude` to your desired location. The server will automatically pick up these changes and update the weather accordingly. 

## External Services:

WeatherByLocation uses the following external services:

- [Open Metro Weather API](https://github.com/scottbarnesg/WeatherByLocation): An open source Weather API.**
- [Ipify API](https://www.ipify.org/): An open source API to determine the server's IP address.***
- [IP API](https://ip-api.com/): An IP address geolocation API.***

    ** Pulls weather data. Always in use.

    *** Used to geolocate the server. Only used if no location is explicitly specified in the config file.