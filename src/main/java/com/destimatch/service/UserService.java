package com.destimatch.service;

import com.destimatch.common.api.request.NewUserRequest;
import com.destimatch.common.api.request.UpdatePreferencesRequest;
import com.destimatch.common.api.request.UpdateProfileRequest;
import com.destimatch.common.exception.ConflictException;
import com.destimatch.common.exception.ValidationException;
import com.destimatch.common.utils.Continent;
import com.destimatch.common.utils.Helpers;
import com.destimatch.entity.UserEntity;
import com.destimatch.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @ConfigProperty(name = "destimatch.admin.secret")
    String adminSecretConfig;

    public String authenticate(String email, String password) {
        var foundUser = userRepository.find("email", email).firstResult();
        if (foundUser == null || !BcryptUtil.matches(password, foundUser.getPassword()))
            throw new NotFoundException("Email ou mot de passe invalide.");

        return Helpers.generateUserJWT(foundUser);
    }

    public UserEntity createUser(NewUserRequest request) {
        Helpers.validateUserFullName(request.getName());
        Helpers.validatePassword(request.getPassword());
        Helpers.validateUserEmail(request.getEmail());

        if (userRepository.find("email", request.getEmail()).firstResult() != null)
            throw new ConflictException("Cet e-mail a déjà été utilisé.");

        UserEntity user = new UserEntity();
        user.setName(Helpers.cleanSpaces(request.getName()));
        user.setEmail(request.getEmail());
        user.setPassword(BcryptUtil.bcryptHash(request.getPassword()));
        // user.setPhone(request.getPhone());
        user.setLocation(request.getLocation());

        user.setPreferences(new ArrayList<>());

        List<String> roles = new ArrayList<>();
        roles.add("user");
        if (request.getAdminSecret() != null
                && adminSecretConfig != null
                && request.getAdminSecret().trim().equals(adminSecretConfig.trim())) {
            roles.add("admin");
        }
        user.setRoles(roles);

        userRepository.persist(user);
        return user;
    }

    public UserEntity updateProfile(String email, UpdateProfileRequest request) {
        UserEntity user = getUserByEmail(email);

        // Infos personnelles
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(Helpers.cleanSpaces(request.getName()));
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

    public void updateUserPreferences(String email, UpdatePreferencesRequest request) {
        UserEntity user = getUserByEmail(email);
        if (user == null)
            throw new NotFoundException("Utilisateur introuvable.");

        if (request.getTags() != null)
            user.setPreferences(request.getTags());

        if (request.getTravelStyle() != null)
            user.setTravelStyle(request.getTravelStyle());

        if (request.getBudgetLevel() != null)
            user.setBudgetLevel(request.getBudgetLevel());

        if (request.getFavoriteContinents() != null) {
            user.setFavoriteContinents(
                    request.getFavoriteContinents().stream()
                            .map(Continent::fromString)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet())
            );
        }

        userRepository.update(user);
    }

    public UserEntity getUserByEmail(String email) {
        if (email == null)
            throw new ValidationException("Email cannot be null.");

        UserEntity user = userRepository.find("email", email).firstResult();
        if (user == null)
            throw new NotFoundException("User with email " + email + " not found.");

        return user;
    }

    public void deleteUser(String email) {
        UserEntity user = getUserByEmail(email);
        userRepository.delete(user);
    }
}
