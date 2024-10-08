package com.example.LeadGenerator.controller;

import com.example.LeadGenerator.Service.FormService;
import com.example.LeadGenerator.Service.MapService;
import com.example.LeadGenerator.Service.MerchantService;
import com.example.LeadGenerator.dao.UserRepository;
import com.example.LeadGenerator.entity.Merchant;
import com.example.LeadGenerator.entity.User;
import com.example.LeadGenerator.request.FormData;
import com.example.LeadGenerator.request.RefereeLinkData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/leadGenerator")
public class LeadGenerationController {

    @Autowired
    private MapService mapService;
    @Autowired
    private FormService formService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    MerchantService merchantService;

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        String response = "Endpoint is active.";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getLeads")
    public List<Merchant> getPincodeBasedLeads(@RequestParam(name = "latitude") String latitude,
                                               @RequestParam(name = "longitude") String longitude,
                                               @RequestParam(name = "radius") Long radius,
                                               @RequestParam(name = "source") String source){
        log.info("Latitude: {}, Longitude: {}, radius: {}, source: {}", latitude, longitude, radius, source);
       return mapService.scrapDataViaLatLong(Double.parseDouble(latitude), Double.parseDouble(longitude),
                radius, source);
    }


    @PostMapping("/form/submit")
    public ResponseEntity<String> submitForm(@RequestBody FormData formData,
                                             @RequestParam(name = "source") String source) {
        return formService.saveFormData(formData, source);
    }



    @PostMapping("/merchant/onboard")
    public boolean merchantOnboard(@RequestParam(name = "placeId") String placeId) {
        log.info(placeId);
        return merchantService.onBoardMerchant(placeId);
    }

    @PostMapping("/consumer/referal/Link")
    public ResponseEntity<String> getReffralLink(@RequestBody RefereeLinkData refreeLinkData) {
        return formService.getUserReferLink(refreeLinkData.getRefereePhone());
    }

    @GetMapping("/test1")
    public ResponseEntity<?> test1(){
        String response = "Endpoint is active.";
        User user = new User();
        user.setName("test");
        user.setMobileNumber("test");
        user.setEmail("test");
        userRepository.save(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
