package com.haulmont.testtask.entity;

import java.util.Date;
import java.util.Objects;

public class Recipe {
    private Long id;
    private String description;
    private Patient patient;
    private Doctor doctor;
    private Date creationDate;
    private int validity;
    private PriorityOfRecipe priority;

    public Recipe(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public PriorityOfRecipe getPriority() {
        return priority;
    }

    public void setPriority(PriorityOfRecipe priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return validity == recipe.validity &&
                Objects.equals(id, recipe.id) &&
                Objects.equals(description, recipe.description) &&
                Objects.equals(patient, recipe.patient) &&
                Objects.equals(doctor, recipe.doctor) &&
                Objects.equals(creationDate, recipe.creationDate) &&
                Objects.equals(priority, recipe.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, patient, doctor, creationDate, validity, priority);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", creationDate=" + creationDate +
                ", validity=" + validity +
                ", priority=" + priority +
                '}';
    }
}
