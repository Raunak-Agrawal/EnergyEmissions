package com.effix.api.util;

import com.stripe.model.Price;
import com.stripe.model.Product;

import java.math.BigDecimal;

public class PaymentUtil {
    static Product[] products;

    static {
        products = new Product[3];

        Product sampleProduct = new Product();
        Price samplePrice = new Price();

        sampleProduct.setName("Standard");
        sampleProduct.setId("standard");
        samplePrice.setCurrency("usd");
        samplePrice.setUnitAmountDecimal(BigDecimal.valueOf(6000));
        sampleProduct.setDefaultPriceObject(samplePrice);
        products[0] = sampleProduct;

        sampleProduct = new Product();
        samplePrice = new Price();

        sampleProduct.setName("Premium");
        sampleProduct.setId("premium");
        samplePrice.setCurrency("usd");
        samplePrice.setUnitAmountDecimal(BigDecimal.valueOf(8000));
        sampleProduct.setDefaultPriceObject(samplePrice);
        products[1] = sampleProduct;

        sampleProduct = new Product();
        samplePrice = new Price();

        sampleProduct.setName("Exclusive");
        sampleProduct.setId("exclusive");
        samplePrice.setCurrency("usd");
        samplePrice.setUnitAmountDecimal(BigDecimal.valueOf(10000));
        sampleProduct.setDefaultPriceObject(samplePrice);
        products[2] = sampleProduct;

    }

    public static Product getProduct(String id) {

        if ("standard".equals(id)) {
            return products[0];
        } else if ("premium".equals(id)) {
            return products[1];
        } else if ("exclusive".equals(id)) {
            return products[2];
        } else return new Product();

    }
}
