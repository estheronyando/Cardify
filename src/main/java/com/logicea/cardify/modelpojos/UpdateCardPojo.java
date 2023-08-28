package com.logicea.cardify.modelpojos;

import com.logicea.cardify.utils.ValidColorFormat;

public class UpdateCardPojo {
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public UpdateCardPojo(String description, String name, String color, String user,String status) {
        this.description = description;
        this.name = name;
        this.color = color;
        this.user = user;
        this.status = status;
    }

    private String description;
    private String name;
    @ValidColorFormat
    private String color;
    private String user;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;
}
