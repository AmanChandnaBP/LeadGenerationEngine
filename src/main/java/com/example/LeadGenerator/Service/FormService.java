package com.example.LeadGenerator.Service;

import com.example.LeadGenerator.dao.MerchantRepository;
import com.example.LeadGenerator.dao.UserRepository;
import com.example.LeadGenerator.entity.Merchant;
import com.example.LeadGenerator.entity.User;
import com.example.LeadGenerator.request.FormData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class FormService {
//    @Autowired
//    private HashGenerator hashGenerator;
    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MapService mapService;

    private static final String BASE_URL = "https://short.url/";
    public ResponseEntity<String> saveFormData(FormData formData, String source) {

        if (checkIfMerchantExists(formData.getLat(),formData.getLng()) > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Merchant already referred");
        }

        Merchant merchant = new Merchant();
        merchant.setShopName(formData.getShopName());
        merchant.setContactNumber(formData.getMerchantContact());
        merchant.setLng(formData.getLng());
        merchant.setLat(formData.getLat());
        merchant.setMerchantName(formData.getMerchantName());
        merchant.setPinCode(formData.getPinCode());
        merchant.setSource(source);
        // Save the form data
        Merchant savedMerchant = merchantRepository.save(merchant);
        mapService.scrapDataViaLatLong(formData.getLat(), formData.getLng(),
                150000, "FSE");
        User user = userRepository.findByHash(formData.getRefereeID());
        if(user != null){
            user.setCountOfReferred(user.getCountOfReferred()+1);
            List<Long> merchantIdsList = user.getMerchantIdsList();
            if (merchantIdsList == null) {
                merchantIdsList = new ArrayList<>();
            }
            merchantIdsList.add(savedMerchant.getId());
            user.setMerchantIdsList(merchantIdsList);
            userRepository.save(user);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Referral is successfully");
    }
    public Long checkIfMerchantExists(Double lat, Double lng) {
        return merchantRepository.existsByLatLong(lat, lng);
    }


    public ResponseEntity<String> getUserReferLink(String contactNumber) {
        // Fetch user and the stored hash
        try {
            User user = userRepository.findBymobileNumber(contactNumber);
            if (user != null) {
                if (user.getRefferallink() == null) {
                    String hash = user.getHash(); // Assuming User entity has a getHash method

                    String uuid = UUID.randomUUID().toString();
                    // Take the first 8 characters for simplicity
                    String shortUrl = uuid.substring(0, 8);
                    // Append the hash to the short URL
                    user.setRefferallink("https://short.url/" + shortUrl + "?userId=" + hash);
                    return ResponseEntity.status(HttpStatus.CREATED).body(BASE_URL + shortUrl + "?hash=" + hash);
                } else {
                    return ResponseEntity.status(HttpStatus.CREATED).body(user.getRefferallink());
                }
            }
            throw new Exception();
        } catch (Exception e) {
            log.error("Exception occured while generating refferal link for contact number {} ", contactNumber);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("User not found");
    }


}

