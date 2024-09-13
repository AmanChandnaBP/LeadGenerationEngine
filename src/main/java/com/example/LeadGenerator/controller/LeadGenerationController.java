package com.example.LeadGenerator.controller;

import com.example.LeadGenerator.Service.FormService;
import com.example.LeadGenerator.Service.MapService;
import com.example.LeadGenerator.request.FormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leadGenerator")
public class LeadGenerationController {

    @Autowired
    private MapService mapService;
    @Autowired
    private FormService formService;

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        String response = "Endpoint is active.";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getLeads")
    public void getPincodeBasedLeads(@RequestParam(name = "pincode") Long pincode,
                                     @RequestParam(name = "radius") Long radius){
        mapService.generateLeads(pincode, radius);
    }


    @PostMapping("/submit")
    public ResponseEntity<String> submitForm(@RequestBody FormData formData) {
        return formService.saveFormData(formData);
    }
}
