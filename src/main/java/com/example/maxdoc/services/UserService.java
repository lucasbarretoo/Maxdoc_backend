package com.example.maxdoc.services;

import com.example.maxdoc.entities.User;
import com.example.maxdoc.respositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public User findByEmail(String email) {

        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User update(Integer id, User user) {
        return userRepository.findById(id)
            .map(u -> {
                u.setName(user.getName());
                u.setPassword(user.getPassword());
                u.setEmail(user.getEmail());
                return userRepository.save(u);
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void delete(Integer id) {

        boolean userExists = userRepository.existsById(id);

        if (userExists) {

            userRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
