package com.example.myapp.Util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    // Define the workload for bcrypt
    private static final int workload = 12;

    // Method to hash a password
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(workload));
    }

    // Method to check a plaintext password against a hashed password
    public static boolean checkPassword(String plainTextPassword, String storedHash) {
        if (storedHash == null || !storedHash.startsWith("$2a$")) {
            throw new IllegalArgumentException("Invalid hash provided for comparison");
        }
        return BCrypt.checkpw(plainTextPassword, storedHash);
    }
}
