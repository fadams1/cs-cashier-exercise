package com.fadams.cashier;

import com.fadams.cashier.discounts.DiscountCalculator;
import com.fadams.cashier.discounts.QuantityBasedDiscountCalculator;
import com.fadams.cashier.model.ProductDetails;
import com.fadams.cashier.stubs.MapProductDetailsProvider;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;

import static com.fadams.cashier.stubs.ByProductNameDiscountCalculatorProvider.discountCalculatorProvider;
import static com.fadams.cashier.stubs.ByProductNameDiscountCalculatorProvider.emptyDiscountCalculatorProvider;
import static com.fadams.cashier.stubs.MapProductDetailsProvider.emptyProductDetailsProvider;
import static com.fadams.cashier.stubs.MapProductDetailsProvider.productDetailsProvider;
import static com.fadams.cashier.stubs.MapProductDetailsProvider.productDetailsProviderFactory;
import static com.fadams.cashier.stubs.ProductDetailsMaker.apple;
import static com.fadams.cashier.stubs.ProductDetailsMaker.banana;
import static com.fadams.cashier.stubs.ProductDetailsMaker.lime;
import static com.fadams.cashier.stubs.ProductDetailsMaker.melon;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ShoppingTotalCalculatorTest {
    // if I were using jUnit 5 I would use assertThrows
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void nullListOfItemsThrowsNullPointerException() throws ProductNotFoundException {
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("List of shopping items is mandatory");

        ShoppingTotalCalculator itemUnderTest = new ShoppingTotalCalculator(emptyProductDetailsProvider(), emptyDiscountCalculatorProvider());
        itemUnderTest.calculateTotalFromItems(null);
    }

    @Test
    public void emptyListOfItemsReturnsZero() throws ProductNotFoundException {
        ShoppingTotalCalculator itemUnderTest = emptyShoppingCalculator();
        BigDecimal calculatedTotalCost = itemUnderTest.calculateTotalFromItems(emptyList());
        assertThat(calculatedTotalCost, is(equalTo(BigDecimal.ZERO)));
    }

    @Test
    public void unknownProductNameThrowsObjectNotFoundException() throws ProductNotFoundException {
        exceptionRule.expect(ProductNotFoundException.class);
        exceptionRule.expectMessage("unknown not found");

        ShoppingTotalCalculator itemUnderTest = emptyShoppingCalculator();
        itemUnderTest.calculateTotalFromItems(Collections.singletonList("unknown"));
    }

    // not adding a lower vs upper case check as it is not in the requirements
    @Test
    public void totalValueOfItemsInListIsCalculatedCorrectly() throws ProductNotFoundException {
        ShoppingTotalCalculator itemUnderTest = new ShoppingTotalCalculator(productDetailsProviderFactory(
                apple(),
                banana(),
                melon(),
                lime()
        ), emptyDiscountCalculatorProvider());
        BigDecimal calculatedTotalCost = itemUnderTest.calculateTotalFromItems(asList("Apple", "Apple", "Lime"));

        assertThat(calculatedTotalCost, is(equalTo(new BigDecimal("85"))));
    }

    @Test
    public void buyOneGetOneDiscountIsAppliedCorrectly() throws ProductNotFoundException {
        ShoppingTotalCalculator itemUnderTest = new ShoppingTotalCalculator(productDetailsProviderFactory(
                apple(),
                banana(),
                melon(),
                lime()
        ), discountCalculatorProvider(
                buyOneGetOne(melon())
        ));
        BigDecimal calculatedTotalCost = itemUnderTest.calculateTotalFromItems(asList("Apple", "Melon", "Melon", "Melon"));

        assertThat(calculatedTotalCost, is(equalTo(new BigDecimal("135"))));
    }

    @Test
    public void buyOneGetOneDiscountIsAppliedCorrectlyTwice() throws ProductNotFoundException {
        ShoppingTotalCalculator itemUnderTest = new ShoppingTotalCalculator(productDetailsProviderFactory(
                apple(),
                banana(),
                melon(),
                lime()
        ), discountCalculatorProvider(
                buyOneGetOne(melon())
        ));
        BigDecimal calculatedTotalCost = itemUnderTest.calculateTotalFromItems(asList("Apple", "Melon", "Melon", "Melon", "Melon"));

        assertThat(calculatedTotalCost, is(equalTo(new BigDecimal("135"))));
    }

    @Test
    public void threeForTwoDiscountIsAppliedCorrectly() throws ProductNotFoundException {
        ShoppingTotalCalculator itemUnderTest = new ShoppingTotalCalculator(productDetailsProviderFactory(
                apple(),
                banana(),
                melon(),
                lime()
        ), discountCalculatorProvider(
                threeForTwo(lime())
        ));
        BigDecimal calculatedTotalCost = itemUnderTest.calculateTotalFromItems(asList("Apple", "Lime", "Lime", "Lime", "Lime"));

        assertThat(calculatedTotalCost, is(equalTo(new BigDecimal("80"))));
    }

    @Test
    public void threeForTwoDiscountIsAppliedCorrectlyTwice() throws ProductNotFoundException {
        ShoppingTotalCalculator itemUnderTest = new ShoppingTotalCalculator(productDetailsProviderFactory(
                apple(),
                banana(),
                melon(),
                lime()
        ), discountCalculatorProvider(
                threeForTwo(lime())
        ));
        BigDecimal calculatedTotalCost = itemUnderTest.calculateTotalFromItems(asList("Apple", "Lime", "Lime", "Lime", "Lime", "Lime", "Lime"));

        assertThat(calculatedTotalCost, is(equalTo(new BigDecimal("95"))));
    }

    @Test
    public void duplicateProductsWillOnlyLookupPriceOnce() throws ProductNotFoundException {
        MapProductDetailsProvider productDetailsProvider = productDetailsProvider(
                apple(),
                banana(),
                melon(),
                lime()
        );
        ShoppingTotalCalculator itemUnderTest = new ShoppingTotalCalculator(productDetailsProviderFactory(productDetailsProvider), emptyDiscountCalculatorProvider());
        itemUnderTest.calculateTotalFromItems(asList("Apple", "Apple", "Lime", "Lime", "Lime", "Lime"));

        assertThat(productDetailsProvider.getCountForProduct("Apple"), is(equalTo(1)));
        assertThat(productDetailsProvider.getCountForProduct("Lime"), is(equalTo(1)));
    }

    private ShoppingTotalCalculator emptyShoppingCalculator() {
        return new ShoppingTotalCalculator(emptyProductDetailsProvider(), emptyDiscountCalculatorProvider());
    }

    private Map.Entry<String, DiscountCalculator> buyOneGetOne(ProductDetails productDetails) {
        return quantityBasedDiscountCalculator(productDetails, new BigDecimal("2"));
    }

    private Map.Entry<String, DiscountCalculator> threeForTwo(ProductDetails productDetails) {
        return quantityBasedDiscountCalculator(productDetails, new BigDecimal("3"));
    }

    private AbstractMap.SimpleEntry<String, DiscountCalculator> quantityBasedDiscountCalculator(ProductDetails productDetails, BigDecimal numberNeededForFreeItem) {
        return new AbstractMap.SimpleEntry<>(productDetails.getProductName(), new QuantityBasedDiscountCalculator(numberNeededForFreeItem, productDetails));
    }
}