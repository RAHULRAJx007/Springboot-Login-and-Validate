package com.sigin.service;

import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sigin.model.Users;
import com.sigin.repository.UserRepository;

@Service
public class UserService {

    @Autowired 
    private UserRepository repo;

    // SIGNUP
    public void saveUser(Users user) {

        // email link token
        user.setVerificationcode(UUID.randomUUID().toString());

        // OTP (6 digit)
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        user.setOtp(otp);
        user.setOtpExpiry(System.currentTimeMillis() + (10 * 60 * 1000)); // 10 min

        user.setEnabled(false);
        repo.save(user);
    }

    public Users findtheuser(String email) {
        return repo.findByEmail(email);
    }

    // EMAIL LINK VERIFICATION
    public boolean verifyUser(String code) {
        Users user = repo.findByVerificationcode(code);
        if (user == null) return false;

        enableUser(user);
        return true;
    }

    // OTP VERIFICATION
    public boolean verifyOtp(String email, String otp) {
        Users user = repo.findByEmail(email);

        if (user == null) return false;
        if (!otp.equals(user.getOtp())) return false;
        if (System.currentTimeMillis() > user.getOtpExpiry()) return false;

        enableUser(user);
        return true;
    }

    private void enableUser(Users user) {
        user.setEnabled(true);
        user.setVerificationcode(null);
        user.setOtp(null);
        user.setOtpExpiry(null);
        repo.save(user);
    }
}
