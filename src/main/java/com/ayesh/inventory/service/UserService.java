package com.ayesh.inventory.service;

import com.ayesh.inventory.dao.UserDao;
import com.ayesh.inventory.entity.User;
import com.ayesh.inventory.model.UserPrincipal;
import com.ayesh.inventory.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService{

    @Autowired
    private UserDao userDao;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public ResponseEntity<List<User>> getAllUsers() {
        try{
            List<User> users = userDao.findAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<User> getUserById(Integer id) {
        try{
            User user = userDao.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<User> addUser(User user) {
        try {
            userDao.save(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<User> updateUser(User user) {
        try {
            User existingUser = userDao.findById(user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            existingUser.setFullName(user.getFullName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setUsername(user.getUsername());
            existingUser.setUserCategory(user.getUserCategory());

            User updatedUser = userDao.save(existingUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);

        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteUser(Integer id) {
        try {
            User user = userDao.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            userDao.deleteById(id);
            return new ResponseEntity<>(user.getFullName() + " is deleted successfully!", HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Cacheable(value = "users", key = "#userName + #fullName + #email + #userCategory")
    public ResponseEntity<List<User>> searchUser(String userName, String fullName, String email, String userCategory) {
        try {
            Specification<User> specification = Specification.where(
                    UserSpecification.hasUserName(userName)
                            .and(UserSpecification.hasUserCategory(userCategory))
                            .and(UserSpecification.hasFullName(fullName))
                            .and(UserSpecification.hasEmail(email))
            );

            List<User> users = userDao.findAll(specification);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while searching for users", e);
        }
    }

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userDao.save(user);
    }

    public String verify(User user){
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());
        return "fail";
    }

}
