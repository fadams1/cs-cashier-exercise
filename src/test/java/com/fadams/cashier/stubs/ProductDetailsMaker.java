package com.fadams.cashier.stubs;

import com.fadams.cashier.model.ProductDetails;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;
import com.natpryce.makeiteasy.PropertyValue;

import java.math.BigDecimal;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;

@SuppressWarnings("WeakerAccess")
public class ProductDetailsMaker {
    public static final Property<ProductDetails, String> PRODUCT_NAME = Property.newProperty();
    public static final Property<ProductDetails, BigDecimal> PRODUCT_PRICE = Property.newProperty();

    public static final Instantiator<ProductDetails> PRODUCT_DETAILS = propertyLookup ->
            new ProductDetails(
                    propertyLookup.valueOf(PRODUCT_NAME, "productName"),
                    propertyLookup.valueOf(PRODUCT_PRICE, BigDecimal.TEN)
            );

    @SafeVarargs
    public static ProductDetails productDetails(PropertyValue<? super ProductDetails, ?>... properties) {
        return make(a(PRODUCT_DETAILS, properties));
    }

    // common test constructs
    public static ProductDetails lime() {
        return productDetails(with(PRODUCT_NAME, "Lime"), with(PRODUCT_PRICE, new BigDecimal("15")));
    }

    public static ProductDetails melon() {
        return productDetails(with(PRODUCT_NAME, "Melon"), with(PRODUCT_PRICE, new BigDecimal("50")));
    }

    public static ProductDetails banana() {
        return productDetails(with(PRODUCT_NAME, "Banana"), with(PRODUCT_PRICE, new BigDecimal("20")));
    }

    public static ProductDetails apple() {
        return productDetails(with(PRODUCT_NAME, "Apple"), with(PRODUCT_PRICE, new BigDecimal("35")));
    }

}
