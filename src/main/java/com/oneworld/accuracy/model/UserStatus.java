package com.oneworld.accuracy.model;

import java.util.stream.Stream;

public enum UserStatus {
    REGISTERED("Registered"),VERIFIED("Verified"),DEACTIVATED("Deactivated");
    private String name;

    UserStatus(String name) {
        this.name = name;
    }

    public static Stream<UserStatus> stream() {
        return Stream.of(UserStatus.values());
    }

    public String getName() {
        return name;
    }


}
