//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application
package com.example.travelexperts.BusinessLayer;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Booking implements Serializable {

    private int bookingId;
    private Date bookingDate;
    private String bookingNo;
    private double travelerCount;
    private int customerId;
    private char tripTypeId;
    private int packageId;

    public Booking() {
    }

    public Booking(int bookingId, Date bookingDate, String bookingNo, double travelerCount, int customerId, char tripTypeId, int packageId) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.bookingNo = bookingNo;
        this.travelerCount = travelerCount;
        this.customerId = customerId;
        this.tripTypeId = tripTypeId;
        this.packageId = packageId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public double getTravelerCount() {
        return travelerCount;
    }

    public void setTravelerCount(double travelerCount) {
        this.travelerCount = travelerCount;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public char getTripTypeId() {
        return tripTypeId;
    }

    public void setTripTypeId(char tripTypeId) {
        this.tripTypeId = tripTypeId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    @Override
    public String toString() {
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        return "# "+ bookingNo + " - " + df.format(bookingDate);
    }
}
