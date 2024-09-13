package com.example.LeadGenerator.Service;

import com.example.LeadGenerator.dao.MerchantRepository;
import com.example.LeadGenerator.entity.Merchant;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ApiGatewayService {

    private static final String API_KEY = "AIzaSyAa0Xo4BfmelC_rrTZr6XZKv-GPRiiq1T4";  // Replace with actual API key

    @Autowired
    private MerchantRepository merchantRepository;

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
                List<Merchant> merchants = new ArrayList<>();

                for (int i = 0; i < results.length(); i++) {
                    JSONObject place = results.getJSONObject(i);
                    log.info("Place : {}", place);

                    // Create new Merchant object
                    Merchant merchant = new Merchant();
                    merchant.setName(place.getString("name"));
                    merchant.setPlaceId(place.getString("place_id"));
                    merchant.setBusinessStatus(place.optString("business_status", "N/A"));
                    merchant.setIconUrl(place.optString("icon", "No icon available"));

                    // Optional: Get rating and user ratings total
                    merchant.setRating(place.optDouble("rating", 0.0));
                    merchant.setUserRatingsTotal(place.optInt("user_ratings_total", 0));

                    // Optional: Check if permanently closed
                    merchant.setPermanentlyClosed(place.optBoolean("permanently_closed", false));

                    // Set lat and lng directly in Merchant
                    JSONObject location = place.getJSONObject("geometry").getJSONObject("location");
                    merchant.setLat(location.getDouble("lat"));
                    merchant.setLng(location.getDouble("lng"));

                    // Set viewport coordinates in merchant
                    JSONObject viewport = place.getJSONObject("geometry").getJSONObject("viewport");
                    merchant.setSouthwestLat(viewport.getJSONObject("southwest").getDouble("lat"));
                    merchant.setSouthwestLng(viewport.getJSONObject("southwest").getDouble("lng"));
                    merchant.setNortheastLat(viewport.getJSONObject("northeast").getDouble("lat"));
                    merchant.setNortheastLng(viewport.getJSONObject("northeast").getDouble("lng"));

                    merchant.setVicinity(place.optString("vicinity", "No vicinity available"));
                    log.info("saving merchant details {}",merchant);
                    merchantRepository.save(merchant);
                }

                // Save all merchants to the database


                // Check for next page token for pagination
                nextPageToken = jsonResponse.optString("next_page_token", null);

            } while (nextPageToken != null);
        } catch (Exception e) {
            log.error("Error in searchPlacesNearCoordinates", e);
        }
    }
}
