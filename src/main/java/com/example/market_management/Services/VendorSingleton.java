package com.example.market_management.Services;

import com.example.market_management.Models.Department;
import com.example.market_management.Models.Vendor;

import java.util.List;

public class VendorSingleton {

    private static VendorSingleton instance;

    private List<Vendor> vendors;

    private VendorSingleton() {}

    public static VendorSingleton getInstance() {
        if (instance == null) {
            synchronized (VendorSingleton.class) {
                if (instance == null) {
                    instance = new VendorSingleton();
                }
            }
        }
        return instance;
    }

    public void setVendors(List<Vendor> list) {
        this.vendors = list;
    }

    public List<Vendor> getVendors() {
        return vendors;
    }
}
