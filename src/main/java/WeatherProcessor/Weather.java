package WeatherProcessor;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import javax.annotation.Nonnull;
import java.time.LocalDateTime;

public class Weather {
    private double windDirection;
    private double windSpeed;
    private double temperature;
    private String weatherCondition;
    private LocalDateTime dateRecorded;

    /**
     * Default constructor
     */
    protected Weather() {

    }

    /**
     * Constructor with parameters
     * @param windDirection
     * @param windSpeed
     * @param temperature
     * @param weatherCondition
     */
    protected Weather(double windDirection, double windSpeed, double temperature, String weatherCondition) {
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.dateRecorded = LocalDateTime.now();
    }

    protected Weather(double windDirection, double windSpeed, double temperature, String weatherCondition, LocalDateTime dateT) {
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.dateRecorded = dateT;
    }
    /**
     * Creates a weather object with the given latitude and longitude.
     * @param latitude
     * @param longitude
     * @return currentWeather
     */
    public static @Nonnull
    Weather fetchCurrentWeatherFromLatLong(String latitude, String longitude) {
        Weather currentWeather = new Weather();
        HttpResponse<JsonNode> weatherResponse = Unirest.get("https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}")
                .routeParam("lat", latitude)
                .routeParam("lon", longitude)
                .routeParam("API key", "7fe57647bbb248cce1a1f9ad22bf42da").asJson();
        if (weatherResponse.getStatus() == 200) {
            JSONObject weatherDataInJson = weatherResponse.getBody().getObject();
            processWeatherJsonIntoWeatherObject(weatherDataInJson, currentWeather);
        }
        return currentWeather;
    }

    /**
     * Reads the JSON data and extracts the wind speed, wind direction, temperature, condition and the date and time extracted
     * @param weatherDataInJson
     * @param currentWeather
     */
    private static void processWeatherJsonIntoWeatherObject(JSONObject weatherDataInJson, Weather currentWeather) {
        //Fetches the value from the fields
        String currentWeatherDescription = (String) weatherDataInJson.getJSONArray("weather").getJSONObject(0).get("description");
        Number currentTemperatureInString = (Number) weatherDataInJson.getJSONObject("main").get("temp");
        Number currentWindSpeedInString = (Number) weatherDataInJson.getJSONObject("wind").get("speed");
        Number currentWindDirectionInString = (Number) weatherDataInJson.getJSONObject("wind").get("deg");


        currentWeather.setWeatherCondition(currentWeatherDescription);

        if (currentTemperatureInString instanceof Double) {
            double currentTemperatureInKelvin = (double) currentTemperatureInString;
            currentWeather.setTemperatureInCelsius(convertKelvinToCelsius(currentTemperatureInKelvin));
        }
        else if (currentTemperatureInString instanceof Integer)
        {
            int currentTemperatureInKelvin = (int) currentTemperatureInString;
            currentWeather.setTemperatureInCelsius(convertKelvinToCelsius(currentTemperatureInKelvin));
        }
        if (currentWindSpeedInString instanceof Double)
        {
            double currentWindSpeed = (double) currentWindSpeedInString;
            currentWeather.setWindSpeedInKmPerHour(convertMeterPerSecondToKmPerHour(currentWindSpeed));
        }
        else if (currentWindSpeedInString instanceof Integer)
        {
            int currentWindSpeed = (int) currentWindSpeedInString;
            currentWeather.setWindSpeedInKmPerHour(convertMeterPerSecondToKmPerHour(currentWindSpeed));
        }
        if (currentWindDirectionInString instanceof Double)
        {
            double currentWindDirection = (double) currentWindDirectionInString;
            currentWeather.setWindDirectionInDegrees(currentWindDirection);
        }
        else if (currentWindDirectionInString instanceof Integer)
        {
            int currentWindDirection = (int) currentWindDirectionInString;
            currentWeather.setWindDirectionInDegrees(currentWindDirection);
        }
        currentWeather.setDateRecorded(LocalDateTime.now());
    }

    /**
     * Converts temperature from kelvin to Celsius
     * @param tempInKelvin
     * @return
     */
    public static double convertKelvinToCelsius(double tempInKelvin)
    {
        return (tempInKelvin) - 273.15;
    }

    /**
     * Converts the wind speed from m/s to km/h
     * @param windSpeedInMeterPerSecond
     * @return
     */
    public static double convertMeterPerSecondToKmPerHour(double windSpeedInMeterPerSecond)
    {
        return windSpeedInMeterPerSecond * 3.6;
    }
    /**
     * Accessor for the wind speed
     *
     * @return windSpeed
     */
    public double getWindSpeedInKmPerHour() {
        return windSpeed;
    }

    /**
     * Accessor for the wind direction
     * @return
     */
    public double getWindDirectionInDegrees() {
        return windDirection;
    }

    /**
     * Accessor for the temperature
     * @return
     */
    public double getTemperatureInCelsius() {
        return temperature;
    }

    /**
     * Accessor for the weather condition
     * @return
     */
    public String getWeatherCondition() {
        return new String(weatherCondition);
    }

    /**
     * Accessor for the date recorded
     * @return
     */
    public LocalDateTime getDateRecorded() {
        return dateRecorded;
    }

    /**
     * Mutator for the date recorded
     * @param dateRecorded
     */
    private void setDateRecorded(LocalDateTime dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    /**
     * Mutator for the temperature
     * @param temperature
     */
    public void setTemperatureInCelsius(double temperature) {
        this.temperature = temperature;
    }

    /**
     * Mutator for the weather condition
     * @param weatherCondition
     */
    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = new String(weatherCondition);
    }

    /**
     * Mutator for the wind direction
     * @param windDirection
     */
    public void setWindDirectionInDegrees(double windDirection) {
        this.windDirection = windDirection;
    }

    /**
     * Mutator for the wind speed
     * @param windSpeed
     */
    public void setWindSpeedInKmPerHour(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    /**
     * toString method
     * @return
     */
    @Override
    public String toString() {
        return "WeatherProcessor.Weather{" +
                "windDirection=" + windDirection +
                ", windSpeed=" + windSpeed +
                ", temperature=" + temperature +
                ", weatherCondition='" + weatherCondition + '\'' +
                ", dateRecorded=" + dateRecorded +
                '}';
    }
}
