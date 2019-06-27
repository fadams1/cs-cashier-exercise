package com.fadams.cashier.products;

import com.fadams.cashier.model.ProductDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CachingProductDetailsProvider implements ProductDetailsProvider {
    private final Map<String, Optional<ProductDetails>> productDetailsCache = new HashMap<>();
    private final ProductDetailsProvider delegate;

    public CachingProductDetailsProvider(ProductDetailsProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public Optional<ProductDetails> getProductDetails(String productName) {
        return productDetailsCache.computeIfAbsent(productName, name -> delegate.getProductDetails(productName));
    }
}
