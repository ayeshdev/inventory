package com.ayesh.inventory.controller;

import com.ayesh.inventory.entity.User;
import com.ayesh.inventory.exception.UserNotFoundException;
import com.ayesh.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        if(user.getId() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try{
            return userService.updateUser(user);
        }catch (UserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Supplier not found
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // Internal error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePurchase(@PathVariable Integer id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchProducts(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String userCategory) {
        return userService.searchUser(userName, fullName, email, userCategory);
    }
}
