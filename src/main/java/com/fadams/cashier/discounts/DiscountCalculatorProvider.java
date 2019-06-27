package com.fadams.cashier.discounts;

import com.fadams.cashier.model.ShoppingBasket;

import java.util.List;

public interface DiscountCalculatorProvider {
    List<DiscountCalculator> getDiscountsToApply(ShoppingBasket shoppingBasket);
}