package com.example.LeadGenerator.dao;

import com.example.LeadGenerator.entity.ExistingMerchants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExistingMerchantsRepository extends JpaRepository<ExistingMerchants, Long> {

    ExistingMerchants findByMobile(String mobile);

}
