package com.example.LeadGenerator.Service;

import com.example.LeadGenerator.dao.MerchantRepository;
import com.example.LeadGenerator.dao.UserRepository;
import com.example.LeadGenerator.entity.Merchant;
import com.example.LeadGenerator.entity.User;
import com.example.LeadGenerator.request.FormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FormService {
//    @Autowired
//    private HashGenerator hashGenerator;
    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<String> saveFormData(FormData formData) {

        if (checkIfMerchantExists(formData.getLat(),formData.getLog())) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Merchant already referred");
        }

        Merchant merchant = new Merchant();
        merchant.setShopName(formData.getShopName());
        merchant.setContactNumber(formData.getMerchantContact());
        merchant.setLng(formData.getLog());
        merchant.setLat(formData.getLat());
        merchant.setMerchantName(formData.getMerchantName());
        merchant.setPinCode(formData.getPinCode());
        // Save the form data
        Merchant savedMerchant = merchantRepository.save(merchant);
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
    public boolean checkIfMerchantExists(Double lat, Double lng) {
        return merchantRepository.existsByLatLong(lat, lng);
    }
}

