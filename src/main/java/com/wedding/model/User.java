package com.wedding.model;

import com.wedding.util.FileHandler;
import com.wedding.util.PasswordUtil;

// VIDURA IT25102240
// OOP: INHERITANCE - User extends Person
// OOP: ENCAPSULATION - password is private and HASHED — never stored plain-text
// FIX: fromFileString() now handles admin lines with 6 fields (no dept/accessLevel)
//      so an AdminUser is returned for ANY line whose role field is "admin",
//      not only when 8+ fields are present.

public class User extends Person {

    private String password; // stored as SHA-256 hex hash
    private String role;     // "customer" or "admin"

    public User() {}

    // Constructor — accepts PLAIN-TEXT password and hashes it immediately
    public User(String id, String name, String email, String phone,
                String password, String role) {
        super(id, name, email, phone);
        this.password = PasswordUtil.hash(password);
        this.role = role;
    }

    // OOP: POLYMORPHISM - overrides abstract method from Person
    @Override
    public String getRole() {
        return role;
    }

    // OOP: POLYMORPHISM - overrides abstract toFileString
    @Override
    public String toFileString() {
        return FileHandler.sanitise(getId()) + "|"
                + FileHandler.sanitise(getName()) + "|"
                + FileHandler.sanitise(getEmail()) + "|"
                + FileHandler.sanitise(getPhone()) + "|"
                + password + "|"
                + FileHandler.sanitise(role);
    }

    // Parse a line from users.txt back into the correct subtype
    // FIX: AdminUser is returned for ANY line with role == "admin",
    //      regardless of whether department/accessLevel fields exist.
    //      Previously this required parts.length >= 8, which silently
    //      fell back to a plain User when those fields were missing —
    //      causing login to fail because User.checkPassword() worked
    //      but the session object had role="admin" without AdminUser type.
    public static User fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 6) return null;

        String role = parts[5];

        if ("admin".equals(role)) {
            // Build AdminUser — use optional fields if present, defaults otherwise
            AdminUser admin = new AdminUser();
            admin.setId(parts[0]);
            admin.setName(parts[1]);
            admin.setEmail(parts[2]);
            admin.setPhone(parts[3]);
            admin.setHashedPassword(parts[4]);
            admin.setRole("admin");
            // Optional fields — only present in 8-field lines
            admin.setDepartment(parts.length >= 7 ? parts[6] : "Management");
            try {
                admin.setAccessLevel(parts.length >= 8 ? Integer.parseInt(parts[7]) : 1);
            } catch (NumberFormatException e) {
                admin.setAccessLevel(1);
            }
            return admin;
        }

        // Regular customer
        User user = new User();
        user.setId(parts[0]);
        user.setName(parts[1]);
        user.setEmail(parts[2]);
        user.setPhone(parts[3]);
        user.setHashedPassword(parts[4]);
        user.setRole(parts[5]);
        return user;
    }

    // Verify a plain-text password attempt against the stored hash
    public boolean checkPassword(String input) {
        return PasswordUtil.verify(input, this.password);
    }

    public String getPassword()                    { return password; }
    public void   setHashedPassword(String hash)   { this.password = hash; }
    public void   setPassword(String plainText)    { this.password = PasswordUtil.hash(plainText); }
    public void   setRole(String role)             { this.role = role; }
}
