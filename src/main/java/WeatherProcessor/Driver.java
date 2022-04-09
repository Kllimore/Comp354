package WeatherProcessor;

import java.nio.file.NoSuchFileException;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import java.time.LocalDateTime;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;

public class Driver {
    public static void main (String[] args) throws NoSuchFileException {
        Weather currentWeather = Weather.fetchCurrentWeatherFromLatLong("45.5017", "73.5673");
        LocalDateTime mostRecent = currentWeather.getDateRecorded();
        int workoutID = 8888888;
//        //WeatherProcessor.Weather someWeather = new WeatherProcessor.Weather(45.7, 7, 15, "Rainy", now);
        WeatherFacade.storeWeatherData(currentWeather, workoutID);
//        System.out.println(currentWeather);
//        //*/
//
//        ///*
//        //TESTING storeWeatherData from the WeatherProcessor.WeatherFacade class
        Weather someWeather = new Weather(45.3, 14.5, 21.5, "Clear Skies");
//        int workoutID = 888888;
        int faultyID = 888888;
       String faultyIDString = Integer.toString(faultyID);
        WeatherFacade.storeWeatherData(someWeather, workoutID);
//
//        WeatherProcessor.Driver.testdeleteWeather();

        //testReadWeather(mostRecent, workoutID);
        testFetchCurrentWeather();
    }
    public static void testFetchCurrentWeather()
    {
        //Montreal
        Weather currentWeather = Weather.fetchCurrentWeatherFromLatLong("45.5017", "73.5673");
        System.out.println(currentWeather);

        //Ottawa
        currentWeather = Weather.fetchCurrentWeatherFromLatLong("45.4215", "75.6972");
        System.out.println(currentWeather);

        //Quebec City
        currentWeather = WeatherFacade.fetchCurrentWeather(46.8139, 71.2080);
        System.out.println(currentWeather);

        //Testing max values
        currentWeather = WeatherFacade.fetchCurrentWeather(90, 180);
        System.out.println(currentWeather);


        //Testing min values
        currentWeather = WeatherFacade.fetchCurrentWeather(-90, -180);
        System.out.println(currentWeather);

        //Testing over limit for latitude. Should yield an empty weather object
        currentWeather = WeatherFacade.fetchCurrentWeather(92, 90);
        System.out.println(currentWeather);

        //Testing over limit for longitude. Should yield an empty weather object
        currentWeather = WeatherFacade.fetchCurrentWeather(90,182);
        System.out.println(currentWeather);


    }
    /*
    public static @Nonnull String valueOf(@Nonnull final Integer i)
    {
        String test = Integer.valueOf(i).toString();
        return test;
    }
    */

    public static void testReadWeather(LocalDateTime ldt, int workoutID){
        System.out.println("TESTING OF readWeatherData FUNCTION:");

        System.out.println("WeatherProcessor.Weather list:");
        WeatherFacade.readWeatherData(workoutID);
        System.out.println("And now at "+ ldt + ":");
        WeatherFacade.readWeatherData(workoutID, ldt);
        System.out.println("At iteration -1 (null weather):");
        WeatherFacade.readWeatherData(workoutID, -1);
        System.out.println("At iteration 1000 (not in range so should output last row):");
        WeatherFacade.readWeatherData(workoutID, 1000);
        System.out.println("At iteration 5 (5th row):");
        WeatherFacade.readWeatherData(workoutID, 5);

        int faultyID = 888888;
        System.out.println("\nAll should be null (using faultyID):");
        WeatherFacade.readWeatherData(faultyID);
        WeatherFacade.readWeatherData(faultyID, ldt);
        WeatherFacade.readWeatherData(faultyID, -1);
        WeatherFacade.readWeatherData(faultyID, 1000);
        WeatherFacade.readWeatherData(faultyID, 5);
    }


    private static void testdeleteWeather() throws NoSuchFileException {
        System.out.println("Creating Sample WeatherProcessor.Weather Data/Object");
        Weather someWeather = new Weather(45.3, 14.5, 21.5, "Clear Skies");
        int workoutID = 888888;
        System.out.println("Storing Sample WeatherProcessor.Weather Data in File");
        WeatherFacade.storeWeatherData(someWeather, workoutID);
        System.out.println("File Found and deleted Succesfully example");
        WeatherFacade.deleteAllWeatherData(888888);
        System.out.println("File Not Found example");
        WeatherFacade.deleteAllWeatherData(900);
        System.out.println("Null Value or other errors");
        int nullID= Integer.parseInt(null);
        WeatherFacade.deleteAllWeatherData(nullID);
    }
}

