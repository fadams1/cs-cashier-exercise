package com.fadams.cashier.discounts;

import com.fadams.cashier.model.ProductDetails;
import com.fadams.cashier.model.ShoppingBasket;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class QuantityBasedDiscountCalculator implements DiscountCalculator {
    private final BigDecimal numberNeededForFreeItem;
    private final ProductDetails productDetails;

    public QuantityBasedDiscountCalculator(BigDecimal numberNeededForFreeItem, ProductDetails productDetails) {
        this.numberNeededForFreeItem = numberNeededForFreeItem;
        this.productDetails = productDetails;
    }

    public BigDecimal calculateDiscount(ShoppingBasket shoppingBasket) {
        BigDecimal numberToDiscount = shoppingBasket.getItemsInBasket().get(productDetails).divide(numberNeededForFreeItem, RoundingMode.DOWN);

        return productDetails.getProductPrice().multiply(numberToDiscount);
    }
}
