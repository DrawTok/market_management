package com.example.market_management.Services;

public class UserSingleton {
    private static UserSingleton instance;

    private String userToken;

    private UserSingleton() {}

    public static UserSingleton getInstance() {
        if (instance == null) {
            synchronized (UserSingleton.class) {
                if (instance == null) {
                    instance = new UserSingleton();
                }
            }
        }
        return instance;
    }

    public void setUserToken(String token) {
        this.userToken = token;
    }

    public String getUserToken() {
        return userToken;
    }

    public void clearUserToken() {
        this.userToken = null;
    }
}
