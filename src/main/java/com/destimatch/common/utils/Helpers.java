package com.destimatch.common.utils;

import com.destimatch.common.exception.ValidationException;
import com.destimatch.entity.UserEntity;
import io.smallrye.jwt.build.Jwt;

import java.time.Duration;
import java.util.HashSet;
import java.util.regex.Pattern;

public class Helpers {

    public static String cleanSpaces(String input) {
        if (input == null)
            return null;

        return input.trim().replaceAll("\\s+", " ");
    }

    public static void validateUserEmail(String email) {
        if (email == null || email.trim().isEmpty())
            throw new ValidationException("Email cannot be null or empty.");

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, email.trim()))
            throw new ValidationException("Email format is invalid.");
    }

    public static void validateUserFullName(String fullName) {
        if (fullName == null)
            throw new ValidationException("Le nom ne peut pas être vide.");

        String newFullName = cleanSpaces(fullName);

        if (newFullName.isEmpty())
            throw new ValidationException("Le nom ne peut pas être vide.");

        if (newFullName.length() < 2)
            throw new ValidationException("Le nom doit contenir au moins 02 caractères.");

        if (!Pattern.matches("^[a-zA-ZÀ-ÿ][a-zA-ZÀ-ÿ' \\-]+$", newFullName))
            throw new ValidationException("Le nom donné contient des caractères invalides.");
    }

    public static void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Le mot de passe ne peut pas être vide.");
        }

        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";

        if (!Pattern.matches(passwordRegex, password)) {
            throw new ValidationException("Password must be between 8 and 20 characters long, " +
                    "including at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        }
    }

    public static String generateUserJWT(UserEntity user) {
        return Jwt.issuer("https://destimatch.com")
                .upn(user.getEmail())
                .groups(new HashSet<>(user.getRoles()))
                .claim("user_id", user.id.toString())
                .expiresIn(Duration.ofHours(72))
                .sign();
    }
}