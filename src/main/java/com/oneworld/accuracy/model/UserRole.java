package com.oneworld.accuracy.model;

import java.util.stream.Stream;

public enum UserRole {
    USER("User"),ADMIN("Admin");
    private String name;

    UserRole(String name) {
        this.name = name;
    }

    public static Stream<UserRole> stream() {
        return Stream.of(UserRole.values());
    }

    public String getLevel() {
        return name;
    }
}
