package com.fadams.cashier.discounts;

import org.junit.Test;

import java.math.BigDecimal;

import static com.fadams.cashier.stubs.ProductDetailsMaker.melon;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CalculateQuantityDiscountTest {
    private static final CalculateQuantityDiscount unitUnderTest = new CalculateQuantityDiscount(new BigDecimal("3"));

    @Test
    public void discountCorrectlyCalculated() {
        assertThat(unitUnderTest.calculateDiscount(melon(), BigDecimal.ZERO), is(equalTo(BigDecimal.ZERO)));
        assertThat(unitUnderTest.calculateDiscount(melon(), BigDecimal.ONE), is(equalTo(BigDecimal.ZERO)));
        assertThat(unitUnderTest.calculateDiscount(melon(), new BigDecimal("2")), is(equalTo(BigDecimal.ZERO)));
        assertThat(unitUnderTest.calculateDiscount(melon(), new BigDecimal("3")), is(equalTo(melon().getProductPrice())));
        assertThat(unitUnderTest.calculateDiscount(melon(), new BigDecimal("4")), is(equalTo(melon().getProductPrice())));
        assertThat(unitUnderTest.calculateDiscount(melon(), new BigDecimal("5")), is(equalTo(melon().getProductPrice())));
        assertThat(unitUnderTest.calculateDiscount(melon(), new BigDecimal("6")), is(equalTo(twoMelons())));
        assertThat(unitUnderTest.calculateDiscount(melon(), new BigDecimal("7")), is(equalTo(twoMelons())));
        assertThat(unitUnderTest.calculateDiscount(melon(), new BigDecimal("8")), is(equalTo(twoMelons())));
        assertThat(unitUnderTest.calculateDiscount(melon(), new BigDecimal("9")), is(equalTo(threeMelons())));
    }

    private BigDecimal threeMelons() {
        return twoMelons().add(melon().getProductPrice());
    }

    private BigDecimal twoMelons() {
        return melon().getProductPrice().add(melon().getProductPrice());
    }

}