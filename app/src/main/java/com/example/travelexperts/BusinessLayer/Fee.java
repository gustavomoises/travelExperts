package com.example.travelexperts.BusinessLayer;

public class Fee {
    private String FeeId;
    private String FeeName;
    private double FeeAmt;
    private String FeeDesc;

    public Fee() {
    }

    public Fee(String feeId, String feeName, double feeAmt, String feeDesc) {
        FeeId = feeId;
        FeeName = feeName;
        FeeAmt = feeAmt;
        FeeDesc = feeDesc;
    }

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

    @Override
    public String toString() {
        return FeeName;
    }
}
