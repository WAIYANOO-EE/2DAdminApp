package com.ardam.admin2d3d.models;

public class Today {

    public String id;
    public String date;
    public String morning;
    public String evening;
    public String createdAt;

    public Today() {
    }

    public Today(String s, String id, String date) {
    }

    public Today(String id, String date, String morning, String evening, String createdAt) {
        this.id = id;
        this.date = date;
        this.morning = morning;
        this.evening = evening;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getEvening() {
        return evening;
    }

    public void setEvening(String evening) {
        this.evening = evening;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
