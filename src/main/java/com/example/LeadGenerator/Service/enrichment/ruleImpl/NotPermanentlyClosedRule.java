package com.example.LeadGenerator.Service.enrichment.ruleImpl;

import com.example.LeadGenerator.Service.enrichment.Rule;
import com.example.LeadGenerator.entity.Merchant;
import lombok.Getter;

@Getter
public class NotPermanentlyClosedRule implements Rule {

    Integer score;

    public NotPermanentlyClosedRule(Integer score) {
        this.score = score;
    }

    public Boolean evaluate(Merchant merchant) {
        return !merchant.getPermanentlyClosed();
    }

}
