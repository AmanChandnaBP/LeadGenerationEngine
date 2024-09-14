package com.example.LeadGenerator.Service;

import com.example.LeadGenerator.dao.MerchantRepository;
import com.example.LeadGenerator.entity.Merchant;
import com.example.LeadGenerator.enums.MerchantStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ApiGatewayService {

    private static final String API_KEY = "AIzaSyAa0Xo4BfmelC_rrTZr6XZKv-GPRiiq1T4";  // Replace with actual API key
    private static final String PLACE_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json";

    @Autowired
    private MerchantRepository merchantRepository;

    public List<Merchant> searchPlacesNearCoordinates(double latitude, double longitude, Long radius, String source) {
        List<Merchant> merchantList=new ArrayList<>();
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
                    String placeId = place.getString("place_id");
                    JsonNode placeDetails = null;
                    if(!ObjectUtils.isEmpty(placeId)){
                        placeDetails = getPlaceDetails(placeId);
                        log.info("place details api response for place_id: {} {}", placeId, placeDetails);
                    }
                    // Create new Merchant object
                    Merchant merchant = new Merchant();
                    merchant.setSource(source);
                    merchant.setStatus(MerchantStatus.PENDING);
                    if(!ObjectUtils.isEmpty(placeDetails) && !ObjectUtils.isEmpty(placeDetails.get("result"))) {
                        merchant.setFormatted_address(!ObjectUtils.isEmpty(placeDetails.get("result").get("formatted_address"))
                                ? String.valueOf((placeDetails.get("result").get("formatted_address"))) : null);
                        merchant.setContactNumber(!ObjectUtils.isEmpty(placeDetails.get("result").get("international_phone_number"))
                                ? String.valueOf((placeDetails.get("result").get("international_phone_number"))) : null);
                        merchant.setPlaceDetailsName(!ObjectUtils.isEmpty(placeDetails.get("result").get("name"))
                                ? String.valueOf((placeDetails.get("result").get("name"))) : null);
                        merchant.setPinCode(!ObjectUtils.isEmpty(placeDetails.get("result").get("formatted_address"))
                                ? extractPincode(String.valueOf((placeDetails.get("result").get("formatted_address")))) : null);
                    }

                    merchant.setShopName(place.getString("name"));
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
                    try {
                        merchantRepository.save(merchant);
                    } catch(Exception e){
                        log.error("Not able to save merchant details: {}", merchant);
                    }
                }
                nextPageToken = jsonResponse.optString("next_page_token", null);

            } while (nextPageToken != null);
        } catch (Exception e) {
            log.error("Error in searchPlacesNearCoordinates", e);
        }
        return merchantList;
    }

    public JsonNode getPlaceDetails(String placeId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        String url = PLACE_DETAILS_URL + "?place_id=" + placeId + "&key=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // Parse response to JsonNode
            String responseBody = response.body().string();
            return objectMapper.readTree(responseBody);
        }
    }
    public String extractPincode(String address) {
        // Split the address by commas
        String[] addressParts = address.split(",");

        // Check if the address has at least two parts
        if (addressParts.length < 2) {
            log.error("Invalid address format");
        }
        // The second last part should contain the pincode
        String pincodePart = addressParts[addressParts.length - 2].trim();
        // Return the trimmed pincode
        return pincodePart.split(" ")[pincodePart.split(" ").length - 1]; // Extract last part, which is the pincode
    }
}
