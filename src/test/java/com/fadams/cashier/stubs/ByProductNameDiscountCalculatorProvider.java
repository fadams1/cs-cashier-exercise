package com.fadams.cashier.stubs;

import com.fadams.cashier.discounts.DiscountCalculator;
import com.fadams.cashier.discounts.DiscountCalculatorProvider;
import com.fadams.cashier.model.ShoppingBasket;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;

public class ByProductNameDiscountCalculatorProvider implements DiscountCalculatorProvider {
    private final Map<String, DiscountCalculator> discountCalculatorMap;

    @SafeVarargs
    private ByProductNameDiscountCalculatorProvider(Map.Entry<String, DiscountCalculator>... discounts) {
        discountCalculatorMap = of(discounts).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public List<DiscountCalculator> getDiscountsToApply(ShoppingBasket shoppingBasket) {
        return shoppingBasket.getItemsInBasket().keySet().stream()
                .map(productDetails -> discountCalculatorMap.getOrDefault(productDetails.getProductName(), noDiscount()))
                .collect(Collectors.toList());
    }

    private DiscountCalculator noDiscount() {
        return basket -> BigDecimal.ZERO;
    }

    public static DiscountCalculatorProvider emptyDiscountCalculatorProvider() {
        return discountCalculatorProvider();
    }

    @SafeVarargs
    public static DiscountCalculatorProvider discountCalculatorProvider(Map.Entry<String, DiscountCalculator> ... discounts) {
        return new ByProductNameDiscountCalculatorProvider(discounts);
    }
}
