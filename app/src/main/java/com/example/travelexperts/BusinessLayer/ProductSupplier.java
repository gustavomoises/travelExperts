//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application

package com.example.travelexperts.BusinessLayer;

public class ProductSupplier {
    private int ProductSupplierId;
    private int ProductId;
    private int SupplierId;

    //Constructor no arguments
    public ProductSupplier() {
    }

    //Constructor 3 arguments
    public ProductSupplier(int productSupplierId, int productId, int supplierId) {
        ProductSupplierId = productSupplierId;
        ProductId = productId;
        SupplierId = supplierId;
    }

    //Getters and Setters
    public int getProductSupplierId() {
        return ProductSupplierId;
    }

    public void setProductSupplierId(int productSupplierId) {
        ProductSupplierId = productSupplierId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(int supplierId) {
        SupplierId = supplierId;
    }

    //toString Method
    @Override
    public String toString() {
        return "ProductSupplierId=" + ProductSupplierId +
                " ProductId=" + ProductId +
                " + SupplierId=" + SupplierId ;
    }
}
