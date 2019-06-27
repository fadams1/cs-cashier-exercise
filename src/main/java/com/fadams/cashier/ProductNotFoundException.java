package com.fadams.cashier;

public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(String productCode) {
        super(productCode + " not found");
    }
}
