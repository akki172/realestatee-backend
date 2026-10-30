package com.example.demo.dto;

public class AdminActivityResponse {

    private String activityType;

    private long count;


    public AdminActivityResponse() {
    }


    public AdminActivityResponse(
            String activityType,
            long count) {

        this.activityType = activityType;
        this.count = count;
    }


    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(
            String activityType) {

        this.activityType = activityType;
    }


    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}