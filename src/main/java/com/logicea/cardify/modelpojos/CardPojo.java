package com.logicea.cardify.modelpojos;

import com.logicea.cardify.models.EStatus;
import com.logicea.cardify.models.User;
import com.logicea.cardify.utils.ValidColorFormat;
import jakarta.validation.constraints.Pattern;

public class CardPojo {
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
    @Pattern(regexp = "#[a-zA-Z0-9]{6}")
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

    public CardPojo(String name, String description,  String color, String user) {
        this.description = description;
        this.name = name;
        this.color = color;
        this.user = user;
    }

    private String description;
    private String name;
    @ValidColorFormat
    private String color;
    private String user;

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    private EStatus status = EStatus.ToDo;
}
