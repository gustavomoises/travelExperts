//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application

package com.example.travelexperts.BusinessLayer;

import java.io.Serializable;

public class TripType implements Serializable {
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
