package com.example.market_management.Services;

import com.example.market_management.Models.Department;

import java.util.List;

public class DepartmentSingleton {
    private static DepartmentSingleton instance;

    private List<Department> departments;

    private DepartmentSingleton() {}

    public static DepartmentSingleton getInstance() {
        if (instance == null) {
            synchronized (UserSingleton.class) {
                if (instance == null) {
                    instance = new DepartmentSingleton();
                }
            }
        }
        return instance;
    }

    public void setDepartments(List<Department> list) {
        this.departments = list;
    }

    public List<Department> getDepartments() {
        return departments;
    }

}
