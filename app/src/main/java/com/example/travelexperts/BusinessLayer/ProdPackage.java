//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application

package com.example.travelexperts.BusinessLayer;

import java.io.Serializable;
import java.util.Date;

public class ProdPackage implements Serializable {
    private int packageId;
    private String pkgName;
    private String pkgStartDate;
    private String pkgEndDate;
    private String pkgDec;
    private double pkgBasePrice;
    private double pkgAgencyCommission;

    //Constructor no arguments
    public ProdPackage() {
    }

    //Constructor 7 arguments
    public ProdPackage(int packageId, String pkgName, String pkgStartDate, String pkgEndDate, String pkgDec, double pkgBasePrice, double pkgAgencyCommission) {
        this.packageId = packageId;
        this.pkgName = pkgName;
        this.pkgStartDate = pkgStartDate;
        this.pkgEndDate = pkgEndDate;
        this.pkgDec = pkgDec;
        this.pkgBasePrice = pkgBasePrice;
        this.pkgAgencyCommission = pkgAgencyCommission;
    }

    //Constructor 6 arguments
    public ProdPackage(String pkgName, String pkgStartDate, String pkgEndDate, String pkgDec, double pkgBasePrice, double pkgAgencyCommission) {
        this.pkgName = pkgName;
        this.pkgStartDate = pkgStartDate;
        this.pkgEndDate = pkgEndDate;
        this.pkgDec = pkgDec;
        this.pkgBasePrice = pkgBasePrice;
        this.pkgAgencyCommission = pkgAgencyCommission;
    }


    //Getters and Setters
    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getPkgStartDate() {
        return pkgStartDate;
    }

    public void setPkgStartDate(String pkgStartDate) {
        this.pkgStartDate = pkgStartDate;
    }

    public String getPkgEndDate() {
        return pkgEndDate;
    }

    public void setPkgEndDate(String pkgEndDate) {
        this.pkgEndDate = pkgEndDate;
    }

    public String getPkgDec() {
        return pkgDec;
    }

    public void setPkgDec(String pkgDec) {
        this.pkgDec = pkgDec;
    }

    public double getPkgBasePrice() {
        return pkgBasePrice;
    }

    public void setPkgBasePrice(double pkgBasePrice) {
        this.pkgBasePrice = pkgBasePrice;
    }

    public double getPkgAgencyCommission() {
        return pkgAgencyCommission;
    }

    public void setPkgAgencyCommission(double pkgAgencyCommission) {
        this.pkgAgencyCommission = pkgAgencyCommission;
    }

    //toString Method
    @Override
    public String toString() {
        return  pkgName + "  "+pkgDec;
    }
}
