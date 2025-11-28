package com.zjgsu.lll.course2_new.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Instructor {

    @Column(name = "instructor_id", length = 20)
    private String id;

    @Column(name = "instructor_name", length = 50)
    private String name;

    @Column(name = "instructor_email", length = 100)
    private String email;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}