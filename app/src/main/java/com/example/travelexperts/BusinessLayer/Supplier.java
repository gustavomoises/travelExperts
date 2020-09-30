package com.example.travelexperts.BusinessLayer;

public class Supplier {
    private int SupplierId;
    private String SupName;

    public Supplier() {
    }

    public Supplier(int supplierId, String supName) {
        SupplierId = supplierId;
        SupName = supName;
    }

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

    @Override
    public String toString() {
        return  SupName ;
    }
}
