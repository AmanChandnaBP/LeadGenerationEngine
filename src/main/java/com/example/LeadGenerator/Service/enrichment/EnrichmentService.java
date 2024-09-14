package com.example.LeadGenerator.Service.enrichment;

import com.example.LeadGenerator.Service.enrichment.ruleImpl.*;
import com.example.LeadGenerator.config.EnrichmentConfig;
import com.example.LeadGenerator.dao.PotentialLeadRepository;
import com.example.LeadGenerator.entity.Merchant;
import com.example.LeadGenerator.entity.PotentialLead;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnrichmentService {

    List<Rule> mapsRuleList;

    List<Rule> referralRuleList;

    @Autowired
    PotentialLeadRepository potentialLeadRepository;

    @Autowired
    EnrichmentConfig enrichmentConfig;

    EnrichmentService() {
        mapsRuleList = new ArrayList<>();
        mapsRuleList.add(new NotPermanentlyClosedRule(enrichmentConfig.getNotPermanentlyClosedRuleScore()));
        mapsRuleList.add(new BusinessStatusOperationalRule(enrichmentConfig.getBusinessStatusOperationalRuleScore()));
        mapsRuleList.add(new RatingAboveThresholdRule(enrichmentConfig.getRatingThreshold(), enrichmentConfig.getRatingAboveThresholdScore()));
        mapsRuleList.add(new IconUrlNotEmptyRule(enrichmentConfig.getIconUriNotEmptyRuleScore()));
        mapsRuleList.add(new ShopNameNotEmptyRule(enrichmentConfig.getShopNameNotEmptyRuleScore()));
        mapsRuleList.add(new ValidContactNumberRule(enrichmentConfig.getValidContactNumberRuleScore()));
        mapsRuleList.add(new ValidLatitudeLongitudeRule(enrichmentConfig.getValidLatitudeLongitudeRuleScore()));
        mapsRuleList.add(new ValidPinCodeRule(enrichmentConfig.getValidPinCodeRuleScore()));

        referralRuleList = new ArrayList<>();
        referralRuleList.add(new MerchantNameNotEmptyRule(enrichmentConfig.getMerchantNameNotEmptyRuleScore()));
        referralRuleList.add(new ShopNameNotEmptyRule(enrichmentConfig.getShopNameNotEmptyRuleScore()));
        referralRuleList.add(new ValidContactNumberRule(enrichmentConfig.getValidContactNumberRuleScore()));
        referralRuleList.add(new ValidLatitudeLongitudeRule(enrichmentConfig.getValidLatitudeLongitudeRuleScore()));
        referralRuleList.add(new ValidPinCodeRule(enrichmentConfig.getValidPinCodeRuleScore()));
    }

    public List<PotentialLead> evaluateRawDataAndMerge(List<Merchant> rawData) {
        List<PotentialLead> potentialLeads = new ArrayList<>();
        List<String> pinCodes = new ArrayList<>();
        for (Merchant merchant : rawData) {
            RuleResult ruleResult = evaluateRules(merchant, this.mapsRuleList);
            if (ruleResult.getSuccess()) {
                potentialLeads.add(new PotentialLead(merchant, ruleResult.score));
                if (ObjectUtils.isEmpty(merchant.getPinCode())) pinCodes.add(merchant.getPinCode());
            }
        }
        potentialLeadRepository.saveAll(potentialLeads);
        List<PotentialLead> existingLeads = potentialLeadRepository.findAllByPinCodeIn(pinCodes);
        potentialLeads.addAll(existingLeads);
        return potentialLeads;
    }

    public void addReferralLead(Merchant lead) {
        RuleResult ruleResult = evaluateRules(lead, this.referralRuleList);
        PotentialLead potentialLead = new PotentialLead(lead, ruleResult.score);
        potentialLeadRepository.save(potentialLead);
    }

    private RuleResult evaluateRules(Merchant merchant, List<Rule> ruleList) {
        boolean finalResult = true;
        Integer finalScore = 0;
        StringBuilder failedRules = new StringBuilder();
        for (Rule rule : ruleList) {
            boolean result = rule.evaluate(merchant);
            if (result) {
                finalScore += rule.getScore();
            } else {
                finalResult = false;
                failedRules.append(rule.rejectReason()).append(",");
            }
        }
        merchant.setFailedRules(failedRules.toString());
        return RuleResult.builder().success(finalResult).score(finalScore).build();
    }

    @Data
    @Builder
    private static class RuleResult {
        Boolean success;
        Integer score;

        RuleResult(Boolean result, Integer score) {
            this.success = result;
            this.score = score;
        }

    }
}

