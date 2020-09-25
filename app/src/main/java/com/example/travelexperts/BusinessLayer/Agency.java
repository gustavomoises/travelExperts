//Author: Gustavo Lourenco Moises
//CPMPP 264 - Java Programming - ANDROID
//Date: 9/24/2020
//Lab 12
package com.example.travelexperts.BusinessLayer;
//Simplified Agency Class
public class Agency {
    private int AgencyId;

    public Agency(int agencyId) {
        AgencyId = agencyId;
    }

    public int getAgencyId() {
        return AgencyId;
    }

    public void setAgencyId(int agencyId) {
        AgencyId = agencyId;
    }

    @Override
    public String toString() {
        return  AgencyId+"";
    }
}
