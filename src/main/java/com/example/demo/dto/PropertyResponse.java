package com.example.demo.dto;
import java.util.List;
public class PropertyResponse {

    private Long id;
    private List<String> imageUrls;

    private String title;

    private String description;

    private String propertyType;

    private Double price;
    private Double latitude;
    private Double longitude;

    private Double area;

    private Integer bedrooms;

    private Integer bathrooms;

    private Integer balconies;

    private Integer parking;

    private String furnished;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String sellerName;
	private Integer ageOfProperty;
	private Integer totalFloors;
	private Integer floor;

    public PropertyResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public Integer getBalconies() {
        return balconies;
    }

    public void setBalconies(Integer balconies) {
        this.balconies = balconies;
    }

    public Integer getParking() {
        return parking;
    }

    public void setParking(Integer parking) {
        this.parking = parking;
    }

    public String getFurnished() {
        return furnished;
    }

    public void setFurnished(String furnished) {
        this.furnished = furnished;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

	
    public Integer getAgeOfProperty() {
        return ageOfProperty;
    }

    public void setAgeOfProperty(Integer ageOfProperty) {
        this.ageOfProperty = ageOfProperty;
    }

    public Integer getTotalFloors() {
        return totalFloors;
    }

    public void setTotalFloors(Integer totalFloors) {
        this.totalFloors = totalFloors;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }
	public Double getLatitude() {
	    return latitude;
	}

	public void setLatitude(Double latitude) {
	    this.latitude = latitude;
	}

	public Double getLongitude() {
	    return longitude;
	}

	public void setLongitude(Double longitude) {
	    this.longitude = longitude;
	}
	public List<String> getImageUrls() {
	    return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
	    this.imageUrls = imageUrls;
	}
	private String status;

	public String getStatus() {
	    return status;
	}

	public void setStatus(String status) {
	    this.status = status;
	}
	private String listingType;

	public String getListingType() {
	    return listingType;
	}

	public void setListingType(String listingType) {
	    this.listingType = listingType;
	}
	private Long sellerId;
	public Long getSellerId() {
	    return sellerId;
	}

	public void setSellerId(Long sellerId) {
	    this.sellerId = sellerId;
	}

	private boolean verified;
	public boolean isVerified() {
	    return verified;
	}

	public void setVerified(boolean verified) {
	    this.verified = verified;
	}
}