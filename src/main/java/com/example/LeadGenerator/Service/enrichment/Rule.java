package com.example.LeadGenerator.Service.enrichment;

import com.example.LeadGenerator.entity.Merchant;
import org.springframework.beans.factory.annotation.Autowired;

public interface Rule {

    static final Boolean defaultResult = Boolean.FALSE;

    Boolean evaluate(Merchant merchant);

    Integer getScore();

    default void execute(Merchant merchant) { // to execute if needed like if-then (not using for now)
    }

    default String rejectReason() {
        return this.getClass().getSimpleName();
    }

}
