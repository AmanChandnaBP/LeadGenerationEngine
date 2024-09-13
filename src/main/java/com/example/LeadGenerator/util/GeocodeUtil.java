package com.example.LeadGenerator.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class GeocodeUtil {
    private static final String API_KEY = "AIzaSyAa0Xo4BfmelC_rrTZr6XZKv-GPRiiq1T4"; // Replace with your actual Google API Key

    public static double[] getCoordinatesForPincode(Long pincode) {
        double[] coordinates = new double[2];
        try {
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + pincode + "&key=" + API_KEY;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.getString("status").equals("OK")) {
                JSONObject location = jsonResponse.getJSONArray("results").getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location");
                coordinates[0] = location.getDouble("lat");
                coordinates[1] = location.getDouble("lng");
            } else {
                System.out.println("Geocoding API Error: " + jsonResponse.getString("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coordinates;
    }
}



