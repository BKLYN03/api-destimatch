package com.destimatch.service;

import com.destimatch.common.exception.ConflictException;
import com.destimatch.common.exception.ValidationException;
import com.destimatch.common.utils.Helpers;
import com.destimatch.common.utils.Location;
import com.destimatch.model.UserModel;
import com.destimatch.repository.UserRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

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

    public UserModel createUser(String name, String email, String password, Location location, String phone) {
        Helpers.validateUserFullName(name);
        Helpers.validatePassword(password);
        Helpers.validateUserEmail(email);

        // if (userRepository.find("name", name).firstResult() != null)
            // throw new ConflictException("The full name you provided already exists.");

        if (userRepository.find("email", email).firstResult() != null)
            throw new ConflictException("The email you provided has already been used.");

        UserModel user = new UserModel(
                Helpers.cleanSpaces(name),
                email,
                BcryptUtil.bcryptHash(password),
                location,
                phone,
                new ArrayList<>()
        );

        userRepository.persist(user);

        return user;
    }

    public UserModel getUserByEmail(String email) {
        if (email == null)
            throw new ValidationException("Email cannot be null.");

        UserModel user = userRepository.find("email", email).firstResult();
        if (user == null)
            throw new NotFoundException("User with email " + email + " not found.");

        return user;
    }

}
