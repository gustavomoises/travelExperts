//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application

package com.example.travelexperts.BusinessLayer;

import java.io.Serializable;

public class Supplier implements Serializable {
    public int SupplierId;
    public String SupName;

    //Constructor no arguments
    public Supplier() {
    }

    //Constructor 2 arguments
    public Supplier(int supplierId, String supName) {
        SupplierId = supplierId;
        SupName = supName;
    }

    //Getters and Setters
    public int getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(int supplierId) {
        SupplierId = supplierId;
    }

    public String getSupName() {
        return SupName;
    }

    public void setSupName(String supName) {
        SupName = supName;
    }

    //toString Method
    @Override
    public String toString() {
        return  SupName ;
    }
}
