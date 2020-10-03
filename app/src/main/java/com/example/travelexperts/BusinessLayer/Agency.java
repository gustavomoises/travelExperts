//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application
package com.example.travelexperts.BusinessLayer;
//Simplified Agency Class
public class Agency {
    private int AgencyId;

    //Constructor 1 argument
    public Agency(int agencyId) {
        AgencyId = agencyId;
    }

    //Getters and Setters
    public int getAgencyId() {
        return AgencyId;
    }

    public void setAgencyId(int agencyId) {
        AgencyId = agencyId;
    }

    //toString Method
    @Override
    public String toString() {
        return  AgencyId+"";
    }
}
