package com.sigin.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sigin.model.Country;
import com.sigin.model.State;
import com.sigin.model.Users;
import com.sigin.service.CountryService;
import com.sigin.service.EmailService;
import com.sigin.service.StateService;
import com.sigin.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private CountryService cservice;
    
    @Autowired
    private StateService stservice;

    @GetMapping("/")
    public String signup(Model model) {
        model.addAttribute("user", new Users());
        model.addAttribute("countryList", cservice.findAll());
        return "sigin";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute Users user,HttpServletRequest request,HttpSession session) throws UnsupportedEncodingException, MessagingException {

        service.saveUser(user);
        emailService.successmail(user, user.getEmail(), getSiteURL(request));

        session.setAttribute("pendingEmail", user.getEmail());

        return "redirect:/otp";
    }

    private String getSiteURL(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verify(@RequestParam("code") String code, Model model) {
        boolean verified = service.verifyUser(code);

        if (verified) {
            model.addAttribute("message", "Account verified successfully. Please login.");
        } else {
            model.addAttribute("message", "Invalid or expired verification link.");
        }
        return "verify";
    }
    
    @GetMapping("/otp")
    public String otpPage(Model model) {
        model.addAttribute("message", "OTP sent to your registered email");
        return "otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String otp,
            HttpSession session,
            Model model) {

        String email = (String) session.getAttribute("pendingEmail");

        if (email == null) {
            return "redirect:/login";
        }
        boolean verified = service.verifyOtp(email, otp);

        if (!verified) {
            model.addAttribute("error", "Invalid or expired OTP");
            return "otp";
        }
        // cleanup
        session.removeAttribute("pendingEmail");

        model.addAttribute("message", "OTP verified successfully. Please login.");
        model.addAttribute("user", new Users());
        return "login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new Users());
        return "login";
    }

    @PostMapping("/login")
    public String logincheck(
            @RequestParam("email") String email,
            @RequestParam("password") String pass,
            HttpSession session,
            Model model) {

        Users user = service.findtheuser(email);

        if (user == null || !user.getPassword().equals(pass)) {
            model.addAttribute("error", "Invalid email or password");
            model.addAttribute("user", new Users());
            return "login";
        }

        if (!user.isEnabled()) {
            model.addAttribute("error", "Please verify your email first");
            model.addAttribute("user", new Users());
            return "login";
        }

        session.setAttribute("loggedinuser", user);
        return "redirect:/dashboard";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("loggedinuser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    @GetMapping("/countries")
    public List<Country> getCounties(){
    	return cservice.findAll();
    }
    @GetMapping("/states/{cid}")
    @ResponseBody
    public List<State> getStates(@PathVariable int cid) {
        return stservice.getStateByCountry(cid);
    }

    
}
