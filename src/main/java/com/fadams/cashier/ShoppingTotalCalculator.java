package com.fadams.cashier;

import com.fadams.cashier.model.ProductDetails;
import com.fadams.cashier.model.ShoppingBasket;
import com.fadams.cashier.products.ProductDetailsProvider;
import com.fadams.cashier.products.ProductDetailsProviderFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class ShoppingTotalCalculator {
    private final Function<Map.Entry<ProductDetails, BigDecimal>, BigDecimal> calculateItemTotal;
    private ProductDetailsProviderFactory productDetailsProviderFactory;

    public ShoppingTotalCalculator(ProductDetailsProviderFactory productDetailsProviderFactory) {
        this.productDetailsProviderFactory = productDetailsProviderFactory;
        this.calculateItemTotal = calculateItemTotal();
    }

    public BigDecimal calculateTotalFromItems(List<String> items) throws ProductNotFoundException {
        Objects.requireNonNull(items, "List of shopping items is mandatory");
        ProductDetailsProvider productDetailsProvider = productDetailsProviderFactory.build();
        ShoppingBasket shoppingBasket = new ShoppingBasket();
        for (String item : items) {
            Optional<ProductDetails> matchingProduct = productDetailsProvider.getProductDetails(item);
            ProductDetails foundProduct = matchingProduct.orElseThrow(() -> new ProductNotFoundException(item));
            shoppingBasket.addToBasket(foundProduct);
        }
        return calculateTotal(shoppingBasket);
    }

    private BigDecimal calculateTotal(ShoppingBasket shoppingBasket) {
        return shoppingBasket.getItemsInBasket()
                .entrySet().stream().map(calculateItemTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Function<Map.Entry<ProductDetails, BigDecimal>, BigDecimal> calculateItemTotal() {
        return productDetailsCountEntry -> productDetailsCountEntry.getValue().multiply(productDetailsCountEntry.getKey().getProductPrice());
    }
}
