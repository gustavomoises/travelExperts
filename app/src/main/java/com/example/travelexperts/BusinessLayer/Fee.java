//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application
package com.example.travelexperts.BusinessLayer;

import java.io.Serializable;

public class Fee implements Serializable {
    private String FeeId;
    private String FeeName;
    private double FeeAmt;
    private String FeeDesc;

    //Constructor no arguments
    public Fee() {
    }

    //Constructor 4 arguments
    public Fee(String feeId, String feeName, double feeAmt, String feeDesc) {
        FeeId = feeId;
        FeeName = feeName;
        FeeAmt = feeAmt;
        FeeDesc = feeDesc;
    }

    //Getters and Setters
    public String getFeeId() {
        return FeeId;
    }

    public void setFeeId(String feeId) {
        FeeId = feeId;
    }

    public String getFeeName() {
        return FeeName;
    }

    public void setFeeName(String feeName) {
        FeeName = feeName;
    }

    public double getFeeAmt() {
        return FeeAmt;
    }

    public void setFeeAmt(double feeAmt) {
        FeeAmt = feeAmt;
    }

    public String getFeeDesc() {
        return FeeDesc;
    }

    public void setFeeDesc(String feeDesc) {
        FeeDesc = feeDesc;
    }

    //toString Method
    @Override
    public String toString() {
        return FeeName;
    }
}
