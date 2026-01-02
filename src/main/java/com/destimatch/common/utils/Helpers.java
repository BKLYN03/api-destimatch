package com.destimatch.common.utils;

import com.destimatch.common.exception.ValidationException;
import com.destimatch.model.UserModel;
import io.smallrye.jwt.build.Jwt;

import java.time.Duration;
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
            throw new ValidationException("Full name cannot be null.");

        String newFullName = cleanSpaces(fullName);

        if (newFullName.isEmpty())
            throw new ValidationException("Full name cannot be empty.");

        if (newFullName.length() < 2)
            throw new ValidationException("Full name must be at least 2 characters long.");

        if (!Pattern.matches("^[a-zA-ZÀ-ÿ][a-zA-ZÀ-ÿ' \\-]+$", newFullName))
            throw new ValidationException("Full name contains invalid characters.");
    }

    public static void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password cannot be empty.");
        }

        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";

        if (!Pattern.matches(passwordRegex, password)) {
            throw new ValidationException("Password must be between 8 and 20 characters long, " +
                    "including at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        }
    }

    public static String generateUserJWT(UserModel user) {
        return Jwt.issuer("https://destimatch.com")
                .upn(user.getEmail())
                .groups("user")
                .claim("user_id", user.id.toString())
                .expiresIn(Duration.ofHours(24))
                .sign();
    }
}