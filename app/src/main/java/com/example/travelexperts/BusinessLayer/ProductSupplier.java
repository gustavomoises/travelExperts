package com.example.travelexperts.BusinessLayer;

public class ProductSupplier {
    private int ProductSupplierId;
    private int ProductId;
    private int SupplierId;

    public ProductSupplier() {
    }

    public ProductSupplier(int productSupplierId, int productId, int supplierId) {
        ProductSupplierId = productSupplierId;
        ProductId = productId;
        SupplierId = supplierId;
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

    @Override
    public String toString() {
        return "ProductSupplierId=" + ProductSupplierId +
                " ProductId=" + ProductId +
                " + SupplierId=" + SupplierId ;
    }
}
