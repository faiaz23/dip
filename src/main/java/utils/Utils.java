package utils;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static void writeLogs(InputStream stream) {
        try (BufferedReader b = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = b.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Exception while wrote logs"+ e);
        }
    }
    public static void runCMD(String cmd, String threadName) throws IOException, InterruptedException {
        System.out.println("Run cmd: " + cmd);
        Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", cmd});
        Thread out = new Thread(() -> writeLogs(p.getInputStream()), threadName + "|out");
        out.start();
        p.waitFor();
        out.join();
        if (p.exitValue() != 0) {
            System.out.println("Run cmd = {} status - failed" + cmd);
            throw new IOException(String.format("Some exception while running cmd = %s. See logs for details.", cmd));
        }
    }
    public static List<Point> parsePointsFromGeoJson(String jsonString){

        List<Point> points = new ArrayList<Point>();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jE = gson.fromJson(jsonString, JsonElement.class);
        JsonObject jsonRoot = jE.getAsJsonObject();
        JsonArray arrayS = jsonRoot.getAsJsonArray("coordinates");
        for ( JsonElement e : arrayS ) {
            JsonArray array = e.getAsJsonArray();
            for(JsonElement e2 : array){
                points.add(new Point(e2.getAsJsonArray().get(0).getAsDouble(), e2.getAsJsonArray().get(1).getAsDouble()));
            }
        }
        return points;
    }

    public static String fromListPointsToWKT(List<Point> input){
        StringBuilder output = new StringBuilder();
        output.append("POLYGON((");
        for(Point p : input){
            output.append(p.getX()).append(" ").append(p.getY()).append(",");
        }
        output.deleteCharAt(output.lastIndexOf(",")).append("))");
        return output.toString();
    }
}
