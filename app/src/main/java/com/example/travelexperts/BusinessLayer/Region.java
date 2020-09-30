//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application

package com.example.travelexperts.BusinessLayer;

import java.io.Serializable;

public class Region implements Serializable {
    private String RegionId;
    private String RegionName;

    public Region() {
    }

    public Region(String regionId, String regionName) {
        RegionId = regionId;
        RegionName = regionName;
    }

    public String getRegionId() {
        return RegionId;
    }

    public void setRegionId(String regionId) {
        RegionId = regionId;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
    }

    @Override
    public String toString() {
        return  RegionName;
    }
}
