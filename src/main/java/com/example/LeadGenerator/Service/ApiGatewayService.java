package com.example.LeadGenerator.Service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiGatewayService {
    private static final String API_KEY = "AIzaSyAa0Xo4BfmelC_rrTZr6XZKv-GPRiiq1T4"; // Replace with your actual Google API Key

    public void searchPlacesNearCoordinates(double latitude, double longitude, Long radius) {
        try {
            String nextPageToken = null;
            do {
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                        + latitude + "," + longitude
                        + "&radius=" + radius + "&type=store" // Radius in meters and type of place
                        + "&key=" + API_KEY;

                if (nextPageToken != null) {
                    url += "&pagetoken=" + nextPageToken; // Add the pagination token if it exists
                    Thread.sleep(2000); // Google requires a small delay before using the next_page_token
                }

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
                JSONArray results = jsonResponse.getJSONArray("results");
                log.info("Results size: {}", results.length());
                for (int i = 0; i < results.length(); i++) {
                    JSONObject place = results.getJSONObject(i);
                    log.info("Place : {}", place);
                    String name = place.getString("name");
                    String placeId = place.getString("place_id");
                    String businessStatus = place.optString("business_status", "N/A");

                    JSONObject location = place.getJSONObject("geometry").getJSONObject("location");
                    double placeLatitude = location.getDouble("lat");
                    double placeLongitude = location.getDouble("lng");

                    // Optional: get opening hours
                    JSONObject openingHours = place.optJSONObject("opening_hours");
                    boolean openNow = openingHours != null && openingHours.optBoolean("open_now", false);

                    // Optional: get photos
                    JSONArray photos = place.optJSONArray("photos");
                    String photoReference = (photos != null && photos.length() > 0) ? photos.getJSONObject(0).getString("photo_reference") : "No photo available";

                    // Get the icon
                    String icon = place.optString("icon", "No icon available");

                    System.out.println("Place Name: " + name);
                    System.out.println("Place ID: " + placeId);
                    System.out.println("Business Status: " + businessStatus);
                    System.out.println("Open Now: " + openNow);
                    System.out.println("Photo Reference: " + photoReference);
                    System.out.println("Icon URL: " + icon);
                    System.out.println("Latitude: " + placeLatitude);
                    System.out.println("Longitude: " + placeLongitude);
                    System.out.println();
                }

                // Check for next page token for pagination
                nextPageToken = jsonResponse.optString("next_page_token", null);

            } while (nextPageToken != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

