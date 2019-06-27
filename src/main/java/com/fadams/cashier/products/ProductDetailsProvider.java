package com.fadams.cashier.products;

import com.fadams.cashier.model.ProductDetails;

import java.util.Optional;

public interface ProductDetailsProvider {
    Optional<ProductDetails> getProductDetails(String productName);
}
