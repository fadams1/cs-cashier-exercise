package com.fadams.cashier;

import com.fadams.cashier.discounts.DiscountCalculator;
import com.fadams.cashier.discounts.DiscountCalculatorProvider;
import com.fadams.cashier.model.ProductDetails;
import com.fadams.cashier.model.ShoppingBasket;
import com.fadams.cashier.products.ProductDetailsProvider;
import com.fadams.cashier.products.ProductDetailsProviderFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ShoppingTotalCalculator {
    private final Function<Map.Entry<ProductDetails, BigDecimal>, BigDecimal> calculateItemTotal;
    private final BiFunction<DiscountCalculator, ShoppingBasket, BigDecimal> calculateDiscount;
    private final ProductDetailsProviderFactory productDetailsProviderFactory;
    private final DiscountCalculatorProvider discountCalculatorProvider;

    public ShoppingTotalCalculator(ProductDetailsProviderFactory productDetailsProviderFactory, DiscountCalculatorProvider discountCalculatorProvider) {
        this.productDetailsProviderFactory = productDetailsProviderFactory;
        this.discountCalculatorProvider = discountCalculatorProvider;
        this.calculateItemTotal = calculateItemTotal();
        this.calculateDiscount = calculateDiscount();
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
        BigDecimal totalBeforeDiscount = calculateTotal(shoppingBasket);
        BigDecimal totalDiscount = calculateTotalDiscount(shoppingBasket);
        return totalBeforeDiscount.subtract(totalDiscount);
    }

    private BigDecimal calculateTotalDiscount(ShoppingBasket shoppingBasket) {
        List<DiscountCalculator> discountsToApply = getDiscountCalculators(shoppingBasket);
        return discountsToApply.stream().map(discount -> calculateDiscount.apply(discount, shoppingBasket))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<DiscountCalculator> getDiscountCalculators(ShoppingBasket shoppingBasket) {
        return discountCalculatorProvider.getDiscountsToApply(shoppingBasket);
    }

    private BiFunction<DiscountCalculator, ShoppingBasket, BigDecimal> calculateDiscount() {
        return DiscountCalculator::calculateDiscount;
    }

    private BigDecimal calculateTotal(ShoppingBasket shoppingBasket) {
        return shoppingBasket.getItemsInBasket()
                .entrySet().stream().map(calculateItemTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Function<Map.Entry<ProductDetails, BigDecimal>, BigDecimal> calculateItemTotal() {
        return productDetailsCountEntry -> productDetailsCountEntry.getValue().multiply(productDetailsCountEntry.getKey().getProductPrice());
    }
}
