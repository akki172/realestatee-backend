package com.example.demo.dto;

public class AdminDashboardResponse {

    private long totalUsers;
    private long totalBuyers;
    private long totalSellers;

    private long totalProperties;
    private long availableProperties;
    private long bookedProperties;
    private long soldProperties;
    private long rentedProperties;

    private long totalEnquiries;

    private long successfulPayments;
    private double totalSalesValue;


    public AdminDashboardResponse() {
    }


    public AdminDashboardResponse(
            long totalUsers,
            long totalBuyers,
            long totalSellers,
            long totalProperties,
            long availableProperties,
            long bookedProperties,
            long soldProperties,
            long rentedProperties,
            long totalEnquiries,
            long successfulPayments,
            double totalSalesValue) {

        this.totalUsers = totalUsers;
        this.totalBuyers = totalBuyers;
        this.totalSellers = totalSellers;

        this.totalProperties = totalProperties;
        this.availableProperties = availableProperties;
        this.bookedProperties = bookedProperties;
        this.soldProperties = soldProperties;
        this.rentedProperties = rentedProperties;

        this.totalEnquiries = totalEnquiries;

        this.successfulPayments = successfulPayments;
        this.totalSalesValue = totalSalesValue;
    }


    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }


    public long getTotalBuyers() {
        return totalBuyers;
    }

    public void setTotalBuyers(long totalBuyers) {
        this.totalBuyers = totalBuyers;
    }


    public long getTotalSellers() {
        return totalSellers;
    }

    public void setTotalSellers(long totalSellers) {
        this.totalSellers = totalSellers;
    }


    public long getTotalProperties() {
        return totalProperties;
    }

    public void setTotalProperties(long totalProperties) {
        this.totalProperties = totalProperties;
    }


    public long getAvailableProperties() {
        return availableProperties;
    }

    public void setAvailableProperties(long availableProperties) {
        this.availableProperties = availableProperties;
    }


    public long getBookedProperties() {
        return bookedProperties;
    }

    public void setBookedProperties(long bookedProperties) {
        this.bookedProperties = bookedProperties;
    }


    public long getSoldProperties() {
        return soldProperties;
    }

    public void setSoldProperties(long soldProperties) {
        this.soldProperties = soldProperties;
    }


    public long getRentedProperties() {
        return rentedProperties;
    }

    public void setRentedProperties(long rentedProperties) {
        this.rentedProperties = rentedProperties;
    }


    public long getTotalEnquiries() {
        return totalEnquiries;
    }

    public void setTotalEnquiries(long totalEnquiries) {
        this.totalEnquiries = totalEnquiries;
    }


    public long getSuccessfulPayments() {
        return successfulPayments;
    }

    public void setSuccessfulPayments(long successfulPayments) {
        this.successfulPayments = successfulPayments;
    }


    public double getTotalSalesValue() {
        return totalSalesValue;
    }

    public void setTotalSalesValue(double totalSalesValue) {
        this.totalSalesValue = totalSalesValue;
    }
}