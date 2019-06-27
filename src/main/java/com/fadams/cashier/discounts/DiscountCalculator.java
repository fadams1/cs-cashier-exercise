package com.fadams.cashier.discounts;

import com.fadams.cashier.model.ShoppingBasket;

import java.math.BigDecimal;

public interface DiscountCalculator {
    BigDecimal calculateDiscount(ShoppingBasket shoppingBasket);
}
