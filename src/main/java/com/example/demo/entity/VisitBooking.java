package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.*;

@Entity
@Table(name = "visit_bookings")
public class VisitBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    private LocalDate visitDate;

    private LocalTime visitTime;

    @Column(length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    private VisitStatus status;

    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private BuyerJourneyStatus journeyStatus;

    public VisitBooking() {
    }

    @PrePersist
    public void onCreate() {

        createdAt = LocalDateTime.now();

        if (status == null) {
            status = VisitStatus.PENDING;
        }

        if (journeyStatus == null) {
            journeyStatus = BuyerJourneyStatus.VISIT_REQUESTED;
        }
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public LocalTime getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(LocalTime visitTime) {
        this.visitTime = visitTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public VisitStatus getStatus() {
        return status;
    }

    public void setStatus(VisitStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public BuyerJourneyStatus getJourneyStatus() {
        return journeyStatus;
    }

    public void setJourneyStatus(BuyerJourneyStatus journeyStatus) {
        this.journeyStatus = journeyStatus;
    }
}