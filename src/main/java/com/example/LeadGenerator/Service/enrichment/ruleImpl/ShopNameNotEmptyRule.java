package com.example.LeadGenerator.Service.enrichment.ruleImpl;

import com.example.LeadGenerator.Service.enrichment.Rule;
import com.example.LeadGenerator.entity.Merchant;
import lombok.Getter;

@Getter
public class ShopNameNotEmptyRule implements Rule {

    Integer score;

    public ShopNameNotEmptyRule(Integer score) {
        this.score = score;
    }

    public Boolean evaluate(Merchant merchant) {
        return merchant.getShopName() != null && !merchant.getShopName().isEmpty();
    }
}
