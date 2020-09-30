//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application
package com.example.travelexperts.BusinessLayer;

import java.io.Serializable;
import java.util.Date;

public class BookingDetail implements Serializable {
    private int bookingDetailId;
    private double itineraryNo;
    private Date tripStart;
    private Date tripEnd;
    private String description;
    private String destination;
    private double basePrice;
    private double agencyCommission;
    private int bookingId;
    private String regionId;
    private String classId;
    private String feedId;
    private int productSupplierId;

    public BookingDetail() {
    }

    public BookingDetail(int bookingDetailId, double itineraryNo, Date tripStart, Date tripEnd, String description, String destination, double basePrice, double agencyCommission, int bookingId, String regionId, String classId, String feedId, int productSupplierId) {
        this.bookingDetailId = bookingDetailId;
        this.itineraryNo = itineraryNo;
        this.tripStart = tripStart;
        this.tripEnd = tripEnd;
        this.description = description;
        this.destination = destination;
        this.basePrice = basePrice;
        this.agencyCommission = agencyCommission;
        this.bookingId = bookingId;
        this.regionId = regionId;
        this.classId = classId;
        this.feedId = feedId;
        this.productSupplierId = productSupplierId;
    }

    public int getBookingDetailId() {
        return bookingDetailId;
    }

    public void setBookingDetailId(int bookingDetailId) {
        this.bookingDetailId = bookingDetailId;
    }

    public double getItineraryNo() {
        return itineraryNo;
    }

    public void setItineraryNo(double itineraryNo) {
        this.itineraryNo = itineraryNo;
    }

    public Date getTripStart() {
        return tripStart;
    }

    public void setTripStart(Date tripStart) {
        this.tripStart = tripStart;
    }

    public Date getTripEnd() {
        return tripEnd;
    }

    public void setTripEnd(Date tripEnd) {
        this.tripEnd = tripEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getAgencyCommission() {
        return agencyCommission;
    }

    public void setAgencyCommission(double agencyCommission) {
        this.agencyCommission = agencyCommission;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public int getProductSupplierId() {
        return productSupplierId;
    }

    public void setProductSupplierId(int productSupplierId) {
        this.productSupplierId = productSupplierId;
    }

    @Override
    public String toString() {
        return description+" - "+destination;
    }
}
