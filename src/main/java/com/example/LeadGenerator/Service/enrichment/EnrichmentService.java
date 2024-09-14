package com.example.LeadGenerator.Service.enrichment;

import com.example.LeadGenerator.Service.enrichment.ruleImpl.*;
import com.example.LeadGenerator.config.EnrichmentConfig;
import com.example.LeadGenerator.dao.ExistingMerchantsRepository;
import com.example.LeadGenerator.dao.MerchantRepository;
import com.example.LeadGenerator.dao.PotentialLeadRepository;
import com.example.LeadGenerator.entity.Merchant;
import com.example.LeadGenerator.entity.PotentialLead;
import com.example.LeadGenerator.enums.MerchantStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnrichmentService {

    List<Rule> mapsRuleList;

    List<Rule> referralRuleList;

    @Autowired
    PotentialLeadRepository potentialLeadRepository;

    @Autowired
    EnrichmentConfig enrichmentConfig;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    ExistingMerchantsRepository existingMerchantsRepository;

    @Autowired
    public EnrichmentService(PotentialLeadRepository potentialLeadRepository,
                             EnrichmentConfig enrichmentConfig,
                             ExistingMerchantsRepository existingMerchantsRepository) {

        this.potentialLeadRepository = potentialLeadRepository;
        this.enrichmentConfig = enrichmentConfig;
        this.existingMerchantsRepository = existingMerchantsRepository;

        this.mapsRuleList = new ArrayList<>();
        this.referralRuleList = new ArrayList<>();

        // Add rules for maps leads
        this.mapsRuleList.add(new NotPermanentlyClosedRule(enrichmentConfig.getNotPermanentlyClosedRuleScore()));
        this.mapsRuleList.add(new BusinessStatusOperationalRule(enrichmentConfig.getBusinessStatusOperationalRuleScore()));
        this.mapsRuleList.add(new RatingAboveThresholdRule(enrichmentConfig.getRatingThreshold(), enrichmentConfig.getRatingAboveThresholdScore()));
        this.mapsRuleList.add(new IconUrlNotEmptyRule(enrichmentConfig.getIconUriNotEmptyRuleScore()));
        this.mapsRuleList.add(new ShopNameNotEmptyRule(enrichmentConfig.getShopNameNotEmptyRuleScore()));
        this.mapsRuleList.add(new ValidContactNumberRule(enrichmentConfig.getValidContactNumberRuleScore()));
        this.mapsRuleList.add(new ValidLatitudeLongitudeRule(enrichmentConfig.getValidLatitudeLongitudeRuleScore()));
        this.mapsRuleList.add(new ValidPinCodeRule(enrichmentConfig.getValidPinCodeRuleScore()));

        // Add CheckIfExistingMerchantRule
        this.mapsRuleList.add(new CheckIfExistingMerchantRule(existingMerchantsRepository));
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
        merchantRepository.saveAll(rawData);
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
        merchant.setStatus(finalResult ? MerchantStatus.PENDING : MerchantStatus.REJECTED);
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

