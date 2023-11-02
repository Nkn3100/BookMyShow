package com.scaler.BookMyShow.service;

import com.scaler.BookMyShow.exception.InvalidCredentialException;
import com.scaler.BookMyShow.exception.UserAlreadyExistException;
import com.scaler.BookMyShow.exception.UserNotFoundException;
import com.scaler.BookMyShow.models.User;
import com.scaler.BookMyShow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public User login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException("User with given email does not exist : " + email);
        }
        User user = userOptional.get();
        if(user.getPassword().equals(password)){
            return user;
        }else{
            throw new InvalidCredentialException("Credentials are invalid");
        }

    }

    @Override
    public User signUp(String name, String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            throw new UserAlreadyExistException("User with given email is already present");
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPassword(password);

        return userRepository.save(newUser);
    }
}
