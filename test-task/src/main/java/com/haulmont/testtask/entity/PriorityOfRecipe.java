package com.haulmont.testtask.entity;

import java.util.Objects;

public class PriorityOfRecipe {

    private Long id;
    private String priorityName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriorityOfRecipe that = (PriorityOfRecipe) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(priorityName, that.priorityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, priorityName);
    }

    @Override
    public String toString() {
        return "PriorityOfRecipe{" +
                "priority_id=" + id +
                ", priorityName='" + priorityName + '\'' +
                '}';
    }

    //    NORMAL("НОРМАЛЬНЫЙ"),
//    CITO("СРОЧНЫЙ"),
//    STATIM("НЕМЕДЛЕННЫЙ");
//
//        PriorityOfRecipe(String priorityName){
//        this.priorityName = priorityName;
//
//        }
//
//    private Long id;
//    private String priorityName;
//
//
//    public Long getId() {
//        return Long.valueOf(ordinal());
//    }
//
//    public String getPriorityName() {
//        return priorityName;
//    }


}
