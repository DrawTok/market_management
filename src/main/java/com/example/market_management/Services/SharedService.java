package com.example.market_management.Services;

import java.util.prefs.Preferences;

public class SharedService {
    private static SharedService instance;

    private Preferences prefs;

    private SharedService() {
        prefs = Preferences.userNodeForPackage(SharedService.class);
    }

    public static SharedService getInstance() {
        if (instance == null) {
            synchronized (SharedService.class) {
                if (instance == null) {
                    instance = new SharedService();
                }
            }
        }
        return instance;
    }

    public void setPreference(String key, String value) {
        prefs.put(key, value);
    }

    public String getPreference(String key) {
        return prefs.get(key, null);
    }

    public void removePreference(String key) {
        prefs.remove(key);
    }
}
