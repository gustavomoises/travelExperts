//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application
package com.example.travelexperts.BusinessLayer;

import java.io.Serializable;

public class Affiliation implements Serializable {
    private String AffiliationId;
    private String AffName;
    private String AffDesc;

    //Constructor no arguments
    public Affiliation() {
    }

    //Constructor 3 arguments
    public Affiliation(String affiliationId, String affName, String affDesc) {
        AffiliationId = affiliationId;
        AffName = affName;
        AffDesc = affDesc;
    }

    //Getters and Setters
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

    //toString Method
    @Override
    public String toString() {
        return  AffiliationId;
    }
}
