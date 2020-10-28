package com.example.travelexperts.BusinessLayer;

/*
 * Author: Suvanjan Shrestha
 * Date: 02/10/2020
 * TravelExperts Android App
 */
public class RecyclerViewData {
    public int ProductSupplierId;
    public int ProductId;
    public int SupplierId;
    public String ProdName;
    public String SupName;

    public RecyclerViewData() {
    }

    public RecyclerViewData(int productSupplierId, int productId, int supplierId, String prodName, String supName) {
        ProductSupplierId = productSupplierId;
        ProductId = productId;
        SupplierId = supplierId;
        ProdName = prodName;
        SupName = supName;
    }

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

    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodName) {
        ProdName = prodName;
    }

    public String getSupName() {
        return SupName;
    }

    public void setSupName(String supName) {
        SupName = supName;
    }
}
