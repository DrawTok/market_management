package com.example.market_management.Models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Employee {
    private int id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
    private String fullName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private String role;
    private Department department;
    private boolean status;

    public Employee() {
    }

    public Employee(String email, String phoneNumber, String username, String address, String fullName, LocalDate birthday, String role, Department department, boolean status) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.address = address;
        this.fullName = fullName;
        this.birthday = birthday;
        this.role = role;
        this.department = department;
        this.status = status;
    }



    public Employee(int id, String username, String password, String email, String phoneNumber, String address, String fullName, LocalDate birthday, String role, Department department, boolean status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.fullName = fullName;
        this.birthday = birthday;
        this.role = role;
        this.department = department;
        this.status = status;
    }

    public Employee(String username, String email, String phoneNumber, String address, String fullName, LocalDate birthday, Department department, boolean status) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.fullName = fullName;
        this.birthday = birthday;
        this.department = department;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
