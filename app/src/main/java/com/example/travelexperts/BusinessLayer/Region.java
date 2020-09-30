package com.example.travelexperts.BusinessLayer;

public class Region {
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
