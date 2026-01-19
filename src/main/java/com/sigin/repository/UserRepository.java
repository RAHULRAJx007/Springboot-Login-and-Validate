package com.sigin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sigin.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Users findByEmail(String email);

    Users findByVerificationcode(String verificationcode);
    
    Users findByOtp(String otp);

}
