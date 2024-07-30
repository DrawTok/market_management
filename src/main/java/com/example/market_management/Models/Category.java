package com.example.market_management.Models;

import com.google.gson.annotations.SerializedName;

public class Category {
    private int id;
    private String name;
    private String description;
    private boolean status;
    @SerializedName("createBy")
    private User createBy;
    @SerializedName("createAt")
    private String createAt;

    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Category(int id, String name, String description, boolean status, User createBy, String createAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.createBy = createBy;
        this.createAt = createAt;
    }

    public Category(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
