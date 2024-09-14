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
        log.info("place id: {}", placeId);
        try {
            Merchant merchant=merchantRepository.findByPlaceId(placeId);
            log.info("merchant fetched using place id: {}", merchant);
            merchant.setStatus(MerchantStatus.ONBOARDED);
            log.info(String.valueOf(merchant));
            merchantRepository.save(merchant);
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
