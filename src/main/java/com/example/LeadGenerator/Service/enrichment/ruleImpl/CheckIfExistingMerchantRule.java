package com.example.LeadGenerator.Service.enrichment.ruleImpl;

import com.example.LeadGenerator.Service.enrichment.Rule;
import com.example.LeadGenerator.dao.ExistingMerchantsRepository;
import com.example.LeadGenerator.entity.Merchant;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Getter
@Component
public class CheckIfExistingMerchantRule implements Rule {

    private final ExistingMerchantsRepository existingMerchantsRepository;

    @Autowired
    public CheckIfExistingMerchantRule(ExistingMerchantsRepository existingMerchantsRepository) {
        this.existingMerchantsRepository = existingMerchantsRepository;
    }

    @Override
    public Boolean evaluate(Merchant merchant) {
        return ObjectUtils.isEmpty(existingMerchantsRepository.findByMobile(merchant.getContactNumber()));
    }

    public Integer getScore(){
        return 0;
    }
}
