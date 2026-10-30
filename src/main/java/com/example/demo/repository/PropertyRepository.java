package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ListingType;
import com.example.demo.entity.Property;
import com.example.demo.entity.PropertyStatus;
import com.example.demo.entity.PropertyType;
import com.example.demo.entity.User;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByCityIgnoreCase(String city);

    List<Property> findByPropertyType(PropertyType propertyType);

    List<Property> findBySeller(User seller);

    List<Property> findByStatus(PropertyStatus status);

    List<Property> findByPriceBetween(Double minPrice, Double maxPrice);
    long countBySeller(User seller);
    List<Property> findByCityIgnoreCaseAndPropertyTypeAndStatus(
            String city,
            PropertyType propertyType,
            PropertyStatus status
    );
    
    List<Property> findByCityIgnoreCaseAndPropertyTypeAndStatusAndPriceBetween(
            String city,
            PropertyType propertyType,
            PropertyStatus status,
            Double minPrice,
            Double maxPrice
    );
    long countByStatus(PropertyStatus status);
    @Query("""
    	    SELECT p FROM Property p
    	    WHERE  (
    :city IS NULL
    OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%'))
    OR LOWER(p.state) LIKE LOWER(CONCAT('%', :city, '%'))
)
    	    AND (:listingType IS NULL OR p.listingType = :listingType)
    	    AND (:propertyType IS NULL OR p.propertyType = :propertyType)
    	    AND (:minPrice IS NULL OR p.price >= :minPrice)
    	    AND (:maxPrice IS NULL OR p.price <= :maxPrice)
    	    AND (:bedrooms IS NULL OR p.bedrooms >= :bedrooms)
    	""")
    	List<Property> searchProperties(
    	        @Param("city") String city,
    	        @Param("listingType") ListingType listingType,
    	        @Param("propertyType") PropertyType propertyType,
    	        @Param("minPrice") Double minPrice,
    	        @Param("maxPrice") Double maxPrice,
    	        @Param("bedrooms") Integer bedrooms
    	);

    @Query("""
    	    SELECT p
    	    FROM Property p
    	    WHERE p.status = com.example.demo.entity.PropertyStatus.AVAILABLE
    	    AND (:city IS NULL OR LOWER(p.city) = LOWER(:city))
    	    AND (:listingType IS NULL OR p.listingType = :listingType)
    	    AND (:propertyType IS NULL OR p.propertyType = :propertyType)
    	    AND (:minPrice IS NULL OR p.price >= :minPrice)
    	    AND (:maxPrice IS NULL OR p.price <= :maxPrice)
    	    AND (:bedrooms IS NULL OR p.bedrooms = :bedrooms)
    	""")
    	List<Property> searchAvailableProperties(
    	        String city,
    	        ListingType listingType,
    	        PropertyType propertyType,
    	        Double minPrice,
    	        Double maxPrice,
    	        Integer bedrooms
    	);
}
