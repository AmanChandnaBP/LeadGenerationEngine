package com.example.LeadGenerator.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapService {

    @Autowired
    private ApiGatewayService apiGatewayService;
    public void generateLeads(Long pincode, Long radius){
        double[] coordinates = GeocodeUtil.getCoordinatesForPincode(pincode);
        if (coordinates[0] != 0 && coordinates[1] != 0) {
            apiGatewayService.searchPlacesNearCoordinates(coordinates[0], coordinates[1]);
        } else {
            System.out.println("Failed to retrieve coordinates.");
        }
    }
}
