package com.sigin.service;

import java.io.UnsupportedEncodingException;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.sigin.model.Users;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	@Autowired
    private JavaMailSender mailSender;
    
    public void successmail(Users user,String toEmail,String siteUrl) throws MessagingException, UnsupportedEncodingException{
//      *****sending plain text*****
//      SimpleMailMessage message=new SimpleMailMessage();
//      message.setTo(toEmail);
//      message.setSubject("registered succesSfully");
//      message.setText("your details have submitted successfully!!...");
//      mailSender.send(message);
    	
//    	*****sending a html email*****
//    	MimeMessage message = mailSender.createMimeMessage();
//    	MimeMessageHelper helper = new MimeMessageHelper(message);
//    	helper.setSubject("Registration success email");
//    	helper.setFrom("blessingfredy2000@gmail.com","Admin Team");
//    	helper.setTo(toEmail);
//    	boolean html = true;
//    	helper.setText("<b>Welcome new user</b><br><i>We are so happy to have you</i>", html);
//    	mailSender.send(message);
    	
//    	*****sending a file attachment*****
//    	MimeMessage message = mailSender.createMimeMessage();
//    	MimeMessageHelper helper = new MimeMessageHelper(message, true);
//    	helper.setTo(toEmail);
//    	helper.setSubject("Registration Success");
//    	helper.setText(
//    	    "<b>Welcome new user</b><br><i>We are so happy to have you</i>",
//    	    true
//    	);
//    	InputStreamSource attachment = new ClassPathResource("attach/sql.pdf");
//    	helper.addAttachment("sql.pdf", attachment);
//    	mailSender.send(message);
    	
//    	****email with verification link****
    	String toaddr = toEmail;
    	String fromaddr = "blessingfredy2000@gmail.com";
    	String senderName = "Night Owl";
    	String subject = "Verify Registration";
    	String message =
    		    "Dear [[name]],<br><br>" +
    		    "Your OTP is: <b>[[OTP]]</b><br>" +
    		    "OTP valid for 10 minutes.<br><br>" +
    		    "Or click below link to verify:<br>" +
    		    "<h3><a href=\"[[URL]]\" target=\"_blank\">VERIFY</a></h3>";
    	MimeMessage msg = mailSender.createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(msg,true);
    	helper.setFrom(fromaddr,senderName);
    	helper.setTo(toaddr);
    	helper.setSubject(subject);
    	message = message.replace("[[name]]", user.getName());
    	message = message.replace("[[OTP]]", user.getOtp());
    	String url = siteUrl + "/verify?code=" + user.getVerificationcode();
    	message = message.replace("[[URL]]", url);
    	helper.setText(message,true);
    	mailSender.send(msg);

    }

}