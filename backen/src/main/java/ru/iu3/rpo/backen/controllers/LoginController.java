package ru.iu3.rpo.backen.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.iu3.rpo.backen.models.User;
import ru.iu3.rpo.backen.repositories.UserRepository;
import ru.iu3.rpo.backen.tools.Utils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Validated @RequestBody Map<String, String> credentials){
        String login = credentials.get("login");
        String pwd = credentials.get("password");
        if(!pwd.isEmpty() && !login.isEmpty()){
            Optional<User> currentUser = userRepository.findByLogin(login);
            if(currentUser.isPresent()){
                User user = currentUser.get();
                String hash1 = user.password;
                String salt = user.salt;
                String hash2 = Utils.ComputerHash(pwd, salt);
                if(hash1.equals(hash2)){
                    String token = UUID.randomUUID().toString();
                    user.token = token;
                    user.activity = LocalDateTime.now();
                    User savedUser = userRepository.saveAndFlush(user);
                    return new ResponseEntity<Object>(savedUser, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
    }

}