package com.fadams.cashier.stubs;

import com.fadams.cashier.model.ProductDetails;
import com.fadams.cashier.products.ProductDetailsProvider;
import com.fadams.cashier.products.ProductDetailsProviderFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class MapProductDetailsProvider implements ProductDetailsProvider {
    private final Map<String, AtomicInteger> interactionCounter;
    private final Map<String, Optional<ProductDetails>> productDetails;

    private MapProductDetailsProvider(List<ProductDetails> productDetailsList) {
        this.productDetails = productDetailsList.stream()
            .collect(Collectors.toMap(productName(), Optional::of));
        this.interactionCounter = productDetailsList.stream()
                .collect(Collectors.toMap(productName(), productDetails -> new AtomicInteger(0)));
    }

    private Function<ProductDetails, String> productName() {
        return ProductDetails::getProductName;
    }

    @Override
    public Optional<ProductDetails> getProductDetails(String productName) {
        interactionCounter.computeIfPresent(productName, (name, counter) -> {
            counter.getAndIncrement();
            return counter;
        });
        return productDetails.getOrDefault(productName, Optional.empty());
    }

    public int getCountForProduct(String productName) {
        return interactionCounter.get(productName).get();
    }

    public static ProductDetailsProviderFactory emptyProductDetailsProvider() {
        List<ProductDetails> productDetailsList = emptyList();
        return productDetailsProviderFactory(productDetailsList);
    }

    public static ProductDetailsProviderFactory productDetailsProviderFactory(ProductDetails... productDetailsList) {
        return productDetailsProviderFactory(asList(productDetailsList));
    }

    public static MapProductDetailsProvider productDetailsProvider(List<ProductDetails> productDetailsList) {
        return new MapProductDetailsProvider(productDetailsList);
    }

    public static MapProductDetailsProvider productDetailsProvider(ProductDetails... productDetailsList) {
        return productDetailsProvider(asList(productDetailsList));
    }

    public static ProductDetailsProviderFactory productDetailsProviderFactory(ProductDetailsProvider productDetailsProvider) {
        return new ProductDetailsProviderFactory(productDetailsProvider);
    }

    private static ProductDetailsProviderFactory productDetailsProviderFactory(List<ProductDetails> productDetailsList) {
        MapProductDetailsProvider productDetailsProvider = productDetailsProvider(productDetailsList);
        return productDetailsProviderFactory(productDetailsProvider);
    }
}
