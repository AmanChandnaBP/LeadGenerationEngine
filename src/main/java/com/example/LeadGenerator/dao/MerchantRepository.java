package com.example.LeadGenerator.dao;

import com.example.LeadGenerator.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    // Custom queries can be defined here if needed

    @Query(value = "SELECT COUNT(*)  FROM merchant WHERE lat = :lat AND lng = :lng", nativeQuery = true)
    Long existsByLatLong(@Param("lat") Double lat, @Param("lng") Double lng);

    @Query(value = "SELECT COUNT(*)  FROM merchant WHERE placeId= :placeId", nativeQuery = true)
    Merchant findByPlaceId( String placeId);
}
