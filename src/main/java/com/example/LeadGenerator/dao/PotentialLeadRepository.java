package com.example.LeadGenerator.dao;

import com.example.LeadGenerator.entity.Merchant;
import com.example.LeadGenerator.entity.PotentialLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PotentialLeadRepository extends JpaRepository<PotentialLead, Long> {

    List<PotentialLead> findAllByPinCodeIn(List<String> pinCodes);
}
