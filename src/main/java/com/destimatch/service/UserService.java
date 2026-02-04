package com.destimatch.service;

import com.destimatch.common.api.request.NewUserRequest;
import com.destimatch.common.api.request.UpdateProfileRequest;
import com.destimatch.common.exception.ConflictException;
import com.destimatch.common.exception.ValidationException;
import com.destimatch.common.utils.Helpers;
import com.destimatch.entity.UserEntity;
import com.destimatch.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public String authenticate(String email, String password) {
        var foundUser = userRepository.find("email", email).firstResult();
        if (foundUser == null || !BcryptUtil.matches(password, foundUser.getPassword()))
            throw new NotAuthorizedException("Invalid credentials.");

        return Helpers.generateUserJWT(foundUser);
    }

    public UserEntity createUser(NewUserRequest request) {
        Helpers.validateUserFullName(request.getName());
        Helpers.validatePassword(request.getPassword());
        Helpers.validateUserEmail(request.getEmail());

        // if (userRepository.find("name", name).firstResult() != null)
            // throw new ConflictException("The full name you provided already exists.");

        if (userRepository.find("email", request.getEmail()).firstResult() != null)
            throw new ConflictException("The email you provided has already been used.");

        UserEntity user = new UserEntity();
        user.setName(Helpers.cleanSpaces(request.getName()));
        user.setEmail(request.getEmail());
        user.setPassword(BcryptUtil.bcryptHash(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setLocation(request.getLocation());

        user.setPreferences(new ArrayList<>());
        user.setWishList(new ArrayList<>());

        userRepository.persist(user);
        return user;
    }

    public UserEntity updateProfile(String email, UpdateProfileRequest request) {
        UserEntity user = getUserByEmail(email);

        // Infos personnelles
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(Helpers.cleanSpaces(request.getName()));
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getLocation() != null) {
            user.setLocation(request.getLocation());
        }

        // Profil voyageur
        if (request.getTravelStyle() != null) {
            user.setTravelStyle(request.getTravelStyle());
        }
        if (request.getBudgetLevel() != null) {
            user.setBudgetLevel(request.getBudgetLevel());
        }
        if (request.getPreferences() != null) {
            user.setPreferences(request.getPreferences());
        }

        userRepository.update(user);
        return user;
    }

    public UserEntity getUserByEmail(String email) {
        if (email == null)
            throw new ValidationException("Email cannot be null.");

        UserEntity user = userRepository.find("email", email).firstResult();
        if (user == null)
            throw new NotFoundException("User with email " + email + " not found.");

        return user;
    }

    public void addToWishlist(String email, String destinationId) {
        UserEntity user = getUserByEmail(email);

        if (user.getWishList() == null || user.getWishList().isEmpty())
            user.setWishList(new ArrayList<>());

        if (!user.getWishList().contains(destinationId)) {
            user.getWishList().add(destinationId);
            userRepository.update(user);
        }
    }

    public void removeFromWishlist(String email, String destinationId) {
        UserEntity user = getUserByEmail(email);

        if (user.getWishList() != null && user.getWishList().contains(destinationId)) {
            user.getWishList().remove(destinationId);
            userRepository.update(user);
        }
    }

    public void deleteUser(String email) {
        UserEntity user = getUserByEmail(email);
        userRepository.delete(user);
    }
}
