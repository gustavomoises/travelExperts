package com.example.travelexperts.BusinessLayer;

public class TripType {
    private char tripTypeId;
    private String tTName;

    public TripType(char tripTypeId, String tTName) {
        this.tripTypeId = tripTypeId;
        this.tTName = tTName;
    }

    public char getTripTypeId() {
        return tripTypeId;
    }

    public void setTripTypeId(char tripTypeId) {
        this.tripTypeId = tripTypeId;
    }

    public String gettTName() {
        return tTName;
    }

    public void settTName(String tTName) {
        this.tTName = tTName;
    }

    @Override
    public String toString() {
        return tTName;
    }
}
