package com.example.demo.dto;

public class SellerDashboardResponse {

    private long totalProperties;
    private long pendingVisits;
    private long totalEnquiries;

    private long availableProperties;
    private long bookedProperties;
    private long soldProperties;
    private long rentedProperties;

    private long saleListings;
    private long rentListings;

    private long apartmentCount;
    private long villaCount;
    private long independentHouseCount;
    private long plotCount;
    private long commercialCount;
   
    private double totalSalesValue;


    public SellerDashboardResponse() {
    }


    public SellerDashboardResponse(
            long totalProperties,
            long pendingVisits,
            long totalEnquiries,
            long availableProperties,
            long bookedProperties,
            long soldProperties,
            long rentedProperties,
            long saleListings,
            long rentListings,
            long apartmentCount,
            long villaCount,
            long independentHouseCount,
            long plotCount,
            long commercialCount,
            double totalSalesValue) {

        this.totalProperties = totalProperties;
        this.pendingVisits = pendingVisits;
        this.totalEnquiries = totalEnquiries;
        this.availableProperties = availableProperties;
        this.bookedProperties = bookedProperties;
       this.soldProperties = soldProperties;
        this.rentedProperties = rentedProperties;
        this.saleListings = saleListings;
        this.rentListings = rentListings;
        this.apartmentCount = apartmentCount;
        this.villaCount = villaCount;
        this.independentHouseCount = independentHouseCount;
        this.plotCount = plotCount;
        this.commercialCount = commercialCount;
        this.totalSalesValue = totalSalesValue;
    }
    public long getTotalProperties() {
        return totalProperties;
    }

    public void setTotalProperties(long totalProperties) {
        this.totalProperties = totalProperties;
    }


    public long getPendingVisits() {
        return pendingVisits;
    }

    public void setPendingVisits(long pendingVisits) {
        this.pendingVisits = pendingVisits;
    }


    public long getTotalEnquiries() {
        return totalEnquiries;
    }

    public void setTotalEnquiries(long totalEnquiries) {
        this.totalEnquiries = totalEnquiries;
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


    public long getRentedProperties() {
        return rentedProperties;
    }

    public void setRentedProperties(long rentedProperties) {
        this.rentedProperties = rentedProperties;
    }


    public long getSaleListings() {
        return saleListings;
    }

    public void setSaleListings(long saleListings) {
        this.saleListings = saleListings;
    }


    public long getRentListings() {
        return rentListings;
    }

    public void setRentListings(long rentListings) {
        this.rentListings = rentListings;
    }


    public long getApartmentCount() {
        return apartmentCount;
    }

    public void setApartmentCount(long apartmentCount) {
        this.apartmentCount = apartmentCount;
    }


    public long getVillaCount() {
        return villaCount;
    }

    public void setVillaCount(long villaCount) {
        this.villaCount = villaCount;
    }


    public long getIndependentHouseCount() {
        return independentHouseCount;
    }

    public void setIndependentHouseCount(long independentHouseCount) {
        this.independentHouseCount = independentHouseCount;
    }


    public long getPlotCount() {
        return plotCount;
    }

    public void setPlotCount(long plotCount) {
        this.plotCount = plotCount;
    }


    public long getCommercialCount() {
        return commercialCount;
    }

    public void setCommercialCount(long commercialCount) {
        this.commercialCount = commercialCount;
    }


    public double getTotalSalesValue() {
        return totalSalesValue;
    }

    public void setTotalSalesValue(double totalSalesValue) {
        this.totalSalesValue = totalSalesValue;
    }


	public long getSoldProperties() {
		return soldProperties;
	}


	public void setSoldProperties(long soldProperties) {
		this.soldProperties = soldProperties;
	}
    
    
}