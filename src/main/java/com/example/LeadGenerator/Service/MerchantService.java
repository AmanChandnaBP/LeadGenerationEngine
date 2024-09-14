package com.example.LeadGenerator.Service;

import com.example.LeadGenerator.dao.MerchantRepository;
import com.example.LeadGenerator.entity.Merchant;
import com.example.LeadGenerator.enums.MerchantStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;


    public boolean onBoardMerchant(String placeId){
        Merchant merchant=merchantRepository.findByPlaceId(placeId);
        merchant.setStatus(MerchantStatus.ONBOARDED);
        merchantRepository.save(merchant);
        return true;
    }

}
