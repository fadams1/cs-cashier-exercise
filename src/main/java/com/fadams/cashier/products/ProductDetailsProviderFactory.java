package com.fadams.cashier.products;

public class ProductDetailsProviderFactory {

    private final ProductDetailsProvider productDetailsProvider;

    public ProductDetailsProviderFactory(ProductDetailsProvider productDetailsProvider) {
        this.productDetailsProvider = productDetailsProvider;
    }

    public ProductDetailsProvider build() {
        return new CachingProductDetailsProvider(productDetailsProvider);
    }
}
