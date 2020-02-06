package com.behaviosec.service;

import com.behaviosec.config.Constants;
import com.behaviosec.model.Role;
import com.behaviosec.model.User;
import com.behaviosec.repository.RoleRepository;
import com.behaviosec.repository.UserRepository;

import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;

@Service("userService")
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @org.springframework.beans.factory.annotation.Autowired(required=true)
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public static String generateQRUrl(User user) throws UnsupportedEncodingException {
    	
    	String username = user.getUsername();
    	String googleOauthToken = user.getGoogleOauthToken();
    	String QRUrl = Constants.QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", Constants.APP_NAME, 
        		username, googleOauthToken, Constants.APP_NAME), "UTF-8");
        return QRUrl;
    }

    public User updateUser(User user, String other) {
    	user.setOther(other);
        return userRepository.save(user);
    }
}