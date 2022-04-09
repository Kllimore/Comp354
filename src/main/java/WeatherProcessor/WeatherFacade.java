package WeatherProcessor;//import kong.unirest.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import java.io.*;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class WeatherFacade {
    /**
     * @param latitude
     * @param longitude
     * @return
     */
    public static Weather fetchCurrentWeather(double latitude, double longitude) {
        return Weather.fetchCurrentWeatherFromLatLong(latitude + "", longitude + "");
    }

    /**
     * Output and return weather data for workout at specific time of workout
     *
     * @param workoutID
     * @param dateToSearch
     */
    public static Weather readWeatherData(int workoutID, LocalDateTime dateToSearch) {
        String fileName = Integer.toString(workoutID);

        JSONArray workoutList = FileHandler.readWeatherFile(fileName);

        Weather newWeather = new Weather();
        String strDateToSearch = dateToSearch.toString();

        if (!workoutList.isEmpty()) {
            for (Object o : workoutList) {

                JSONObject workout = (JSONObject) o;

                String date = (String) workout.get("DateRecorded");

                if (date.equals(strDateToSearch)) {
                    newWeather = getWeatherfromJSON(workout);
                }
            }
        }
        System.out.println(newWeather);
        return newWeather;
    }

    /**
     * Output and return weather data for workout at specific iteration
     *
     * @param workoutID
     * @param iterationWeatherData
     */
    public static Weather readWeatherData(int workoutID, int iterationWeatherData) {
        String fileName = Integer.toString(workoutID);

        JSONArray workoutList = FileHandler.readWeatherFile(fileName);
        JSONObject workout = new JSONObject();
        Weather newWeather = new Weather();

        // new var i to get "row" in JSON file
        int i = iterationWeatherData - 1;
        int workoutSize = workoutList.size() - 1;

        if (!workoutList.isEmpty() && i > 0) {
            if (i < workoutSize) {
                workout = (JSONObject) workoutList.get(i);
            } else if (i > workoutSize) {
                workout = (JSONObject) workoutList.get(workoutSize);
            }
            newWeather = getWeatherfromJSON(workout);
        }
        System.out.println(newWeather);
        return newWeather;
    }

    /**
     * Output and return all weather data for a workout
     *
     * @param workoutID
     */
    public static ArrayList<Weather> readWeatherData(int workoutID) {
        String fileName = Integer.toString(workoutID);

        JSONArray workoutList = FileHandler.readWeatherFile(fileName);
        ArrayList<Weather> weatherList = new ArrayList<Weather>();

        if (!workoutList.isEmpty()) {
            for (Object o : workoutList) {
                JSONObject workout = (JSONObject) o;

                Weather newWeather = getWeatherfromJSON(workout);

                System.out.println(newWeather);
                weatherList.add(newWeather);
            }
        }
        return weatherList;
    }


    //Deletes all the weather data associated with a workout, future implementation will include a way to delete individual or bundled weather data
    public static void deleteAllWeatherData(int workoutID) throws NoSuchFileException {
        FileHandler.deleteWeatherData(workoutID);

    }
    //Updates WeatherProcessor.Weather data

    public static void updateWeatherData(Weather currentWeather, int workoutID) {
        WeatherFacade.storeWeatherData(currentWeather, workoutID);

    }

    /**
     * Takes the information of a weather object and adds it to the given json file
     *
     * @param data
     * @param workoutID
     */
    public static void storeWeatherData(Weather data, int workoutID) {


        String fileName = Integer.toString(workoutID);
        File historicalFile = new File(fileName + ".json");

        //WeatherProcessor.Weather details all in one JSONObject which will be connected to another JSONObject
        HashMap<String, Object> weatherDetails = new HashMap<String, Object>();
        weatherDetails.put("windDirection", data.getWindDirectionInDegrees());
        weatherDetails.put("windSpeed", data.getWindSpeedInKmPerHour());
        weatherDetails.put("temperature", data.getTemperatureInCelsius());
        weatherDetails.put("weatherCondition", data.getWeatherCondition());

        HashMap<String, Object> fullDetails = new HashMap<String, Object>();
        fullDetails.put("DateRecorded", data.getDateRecorded().toString());
        fullDetails.put("WeatherDetails", weatherDetails);

        kong.unirest.json.JSONObject fullWorkoutInfoJSON = new kong.unirest.json.JSONObject(fullDetails);

        JSONParser jsonParser = new JSONParser();

        if (historicalFile.exists() && historicalFile.length() != 0) {
            //Append the new weather data associated with a workoutID to the JSON file
            try (FileReader reader = new FileReader(historicalFile)) {
                Object obj = jsonParser.parse(reader);
                JSONArray workoutList = (JSONArray) obj;
                workoutList.add(fullWorkoutInfoJSON);

                FileWriter file = new FileWriter(historicalFile);
                file.write(workoutList.toJSONString());
                file.flush();
                file.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                JSONArray weatherDetailsArray = new JSONArray();
                weatherDetailsArray.add(fullWorkoutInfoJSON);

                FileWriter file = new FileWriter(historicalFile);
                file.write(weatherDetailsArray.toJSONString());
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Weather getWeatherfromJSON(JSONObject workoutWeather) {
        String strDate = (String) workoutWeather.get("DateRecorded");
        LocalDateTime dateLDT = LocalDateTime.parse(strDate);

        JSONObject weather = (JSONObject) workoutWeather.get("WeatherDetails");

        double windDirection = (double) weather.get("windDirection");
        double windSpeed = (double) weather.get("windSpeed");
        double temp = (double) weather.get("temperature");
        String weatherCondition = (String) weather.get("weatherCondition");

        return new Weather(windDirection, windSpeed, temp, weatherCondition, dateLDT);
    }
}