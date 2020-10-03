//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application

package com.example.travelexperts.BusinessLayer;

import java.io.Serializable;

public class Product implements Serializable {
    public int ProductId;
    public String ProdName;

    //Constructor no arguments
    public Product() {
    }

    //Constructor 2 arguments
    public Product(int productId, String prodName) {
        ProductId = productId;
        ProdName = prodName;
    }

    //Getters and Setters
    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodName) {
        ProdName = prodName;
    }

    //toString Method
    @Override
    public String toString() {
        return  ProdName;
    }
}
