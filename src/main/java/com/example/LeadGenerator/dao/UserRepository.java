package com.example.LeadGenerator.dao;


import com.example.LeadGenerator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Find a user by their hash
    User findByHash(String hash);


    User findBymobileNumber(String mobileNumber);
}

