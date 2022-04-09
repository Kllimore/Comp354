package WeatherProcessor;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.io.*;

public class FileHandler {
    public void storeWeatherData(@Nonnull Weather weather) {

    }

    public static JSONArray readWeatherFile(String fileName) {
        File workoutFile = new File(fileName + ".json");

        JSONParser jsonParser = new JSONParser();

        if (workoutFile.exists() && workoutFile.length() != 0) {
            // File exists and data can be extracted
            try (FileReader reader = new FileReader(workoutFile)) {

                Object obj = jsonParser.parse(reader);
                JSONArray workoutList = (JSONArray) obj;

                return workoutList;
            }
                catch(FileNotFoundException e){
                    e.printStackTrace();
                } catch(IOException e){
                    e.printStackTrace();
                } catch(org.json.simple.parser.ParseException e){
                    e.printStackTrace();
                }
            }

        else
            {
                System.out.println("Workout File does not exist.\nNo weather data to extract.");
                return new JSONArray();
            }
            return new JSONArray();
        }





    public static void deleteWeatherData( int workoutID) throws NoSuchFileException {

        String fileName = Integer.toString(workoutID);
        File historicalFile = new File(fileName+".json");
        try {
            if(historicalFile.exists())
            {
                historicalFile.delete();
                System.out.println("Successfully deleted weather data associated with this workout.");
            }
            else if(!historicalFile.exists()){

                throw new NoSuchFileException(historicalFile.getName());

            }
            else
                throw new Exception();

         } catch (NoSuchFileException x) {
            System.err.format("No data file exists for this workout\n");
        } catch (Exception x) {
            // File permission problems are caught here.
            System.err.println(x);
        }
    }

}
