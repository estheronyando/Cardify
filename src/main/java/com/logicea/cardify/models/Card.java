package com.logicea.cardify.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "cards")
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Column(name = "creation_date")
    private Date creationDate;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;


    private String color;

    @Enumerated(EnumType.STRING)
    private EStatus status = EStatus.ToDo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Card(Long id, String name, String description, String color, EStatus status, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.status = status;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Pattern(regexp = "#[0-9a-fA-F]{6}$", message = "Color should be in the format '#RRGGBB'")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    // Constructors, getters, setters, and other methods

    // Enum for CardStatus

}
