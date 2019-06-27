package com.fadams.cashier.discounts;

import com.fadams.cashier.model.ProductDetails;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculateQuantityDiscount {
    private final BigDecimal numberNeededForFreeItem;

    public CalculateQuantityDiscount(BigDecimal numberNeededForFreeItem) {
        this.numberNeededForFreeItem = numberNeededForFreeItem;
    }

    public BigDecimal calculateDiscount(ProductDetails productDetails, BigDecimal count) {
        BigDecimal numberToDiscount = count.divide(numberNeededForFreeItem, RoundingMode.DOWN);

        return productDetails.getProductPrice().multiply(numberToDiscount);
    }
}
