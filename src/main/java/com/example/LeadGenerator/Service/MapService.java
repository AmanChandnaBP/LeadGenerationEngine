package com.example.LeadGenerator.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MapService {

    @Autowired
    private ApiGatewayService apiGatewayService;
    public void generateLeads(Long pincode, Long radius, String source){
        double[] coordinates = GeocodeUtil.getCoordinatesForPincode(pincode);
        log.info("coordinated recieved from pincode: {}", coordinates);
        System.out.println(coordinates.length);
        if (coordinates[0] != 0 && coordinates[1] != 0) {
            apiGatewayService.searchPlacesNearCoordinates(coordinates[0], coordinates[1], radius, source);
        } else {
            System.out.println("Failed to retrieve coordinates.");
        }
    }
    public void scrapDataViaLatLong(double latitude, double longitude, long radius, String source){
        apiGatewayService.searchPlacesNearCoordinates(latitude, longitude, radius, source);
    }
}
