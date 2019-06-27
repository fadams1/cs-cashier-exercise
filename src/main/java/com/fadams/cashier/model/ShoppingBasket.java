package com.fadams.cashier.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ShoppingBasket {
    private final Map<ProductDetails, BigDecimal> itemsInBasket = new HashMap<>();

    public void addToBasket(ProductDetails foundProduct) {
        itemsInBasket.putIfAbsent(foundProduct, BigDecimal.ZERO);
        itemsInBasket.computeIfPresent(foundProduct, (productDetails, count) -> count.add(BigDecimal.ONE));
    }

    public Map<ProductDetails, BigDecimal> getItemsInBasket() {
        return itemsInBasket;
    }
}
