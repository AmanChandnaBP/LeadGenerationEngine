package com.example.LeadGenerator.Service;

import com.example.LeadGenerator.entity.Merchant;
import com.example.LeadGenerator.util.GeocodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MapService {

    @Autowired
    private ApiGatewayService apiGatewayService;
    public List<Merchant> generateLeads(Long pincode, Long radius, String source){
        double[] coordinates = GeocodeUtil.getCoordinatesForPincode(pincode);
        log.info("coordinated recieved from pincode: {}", coordinates);
        System.out.println(coordinates.length);
        if (coordinates[0] != 0 && coordinates[1] != 0) {
           return apiGatewayService.searchPlacesNearCoordinates(coordinates[0], coordinates[1], radius, source);
        } else {
            System.out.println("Failed to retrieve coordinates.");
        }
        return null;
    }
    public List<Merchant> scrapDataViaLatLong(double latitude, double longitude, long radius, String source){
       return apiGatewayService.searchPlacesNearCoordinates(latitude, longitude, radius, source);
    }
}
