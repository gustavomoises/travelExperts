package com.example.travelexperts.BusinessLayer;

import java.io.Serializable;

public class Affiliation implements Serializable {
    private String AffiliationId;
    private String AffName;
    private String AffDesc;

    public Affiliation() {
    }

    public Affiliation(String affiliationId, String affName, String affDesc) {
        AffiliationId = affiliationId;
        AffName = affName;
        AffDesc = affDesc;
    }

    public String getAffiliationId() {
        return AffiliationId;
    }

    public void setAffiliationId(String affiliationId) {
        AffiliationId = affiliationId;
    }

    public String getAffName() {
        return AffName;
    }

    public void setAffName(String affName) {
        AffName = affName;
    }

    public String getAffDesc() {
        return AffDesc;
    }

    public void setAffDesc(String affDesc) {
        AffDesc = affDesc;
    }

    @Override
    public String toString() {
        return  AffiliationId;
    }
}
