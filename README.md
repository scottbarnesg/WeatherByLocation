# WeatherByLocation Plugin

A Spigot/Paper plugin that sets the weather on your minecraft server based on the actual weather at a chosen location. 

## Setup

1. Dowload WeatherByLocation.jar (coming soon!) to the `plugins` folder of your minecraft server directory.
2. Restart the server. This will generate a `WeatherByLocation` directory in your `plugins` folder.
3. Navigate into the `WeatherByLocation` folder and edit `config.yml`. 
4. Update the values of `latitude` and `longitude` to your desired location. The server will automatically pick up these changes and update the weather accordingly. You should see something like this in the server logs:

    ```
    my-server  | [03:45:11 INFO]: [WeatherByLocation] Weather code for (30.58, 97.85) is 3.
    my-server  | [03:45:11 INFO]: [WeatherByLocation] Weather was set to Clear.
    ```