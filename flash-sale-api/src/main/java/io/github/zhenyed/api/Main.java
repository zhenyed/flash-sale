package io.github.zhenyed.api;

import io.github.zhenyed.api.product.ProductConvert;
import io.github.zhenyed.api.product.dataobject.ProductDO;

public class Main {
    public static void main(String[] args) {
        System.out.println(ProductConvert.convert(
                new ProductDO().setId(123).setName("ads").setPrice(1223).setQuantity(1)));
    }

}
