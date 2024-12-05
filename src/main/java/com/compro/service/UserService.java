package com.compro.service;

import com.compro.model.User;
import com.compro.repository.UserRepository;
import com.compro.util.JwtUtil;
import com.compro.util.PasswordUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static Logger logger;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public  User register(String username, String password, String name, String email, String level){
        logger.info("=====Create New User=====");
        User user = new User();
        user.setUsername(username);
        user.setPassword(PasswordUtil.hashPassword(password));
        user.setName(name);
        user.setEmail(email);
        user.setLevel(level);
        return userRepository.save(user);
    }

    public String login(String username, String password){
        logger.info("=====Login=====");
        User user = userRepository.findByUsername(username);
        if (user != null && PasswordUtil.checkPassword(password, user.getPassword())){
            return JwtUtil.generateToken(username);
        }
        throw new RuntimeException("Username or Password is Wrong");
    }

    public boolean hasAccess(User user, String requiredLevel){
        logger.info("=====Access Checking=====");
        return user.getLevel().equals(requiredLevel);
    }

    public boolean checkAccess(String token, String requiredLevel){
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username);

        if (user == null){
            logger.info("User not found");
            return false;
        }

        boolean isValidToken = jwtUtil.validationToken(token, username);
        if (!isValidToken){
            logger.info("Token Not Valid");
            return false;
        }
        return hasAccess(user, requiredLevel);
    }
}
