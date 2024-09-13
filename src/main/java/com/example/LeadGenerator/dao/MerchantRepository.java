package com.example.LeadGenerator.dao;

import com.example.LeadGenerator.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    // Custom queries can be defined here if needed
}
