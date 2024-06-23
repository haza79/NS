package com.ns;

import lombok.Data;

@Data
public class SaleProductModel {

    private ProductModel productModel;
    private BrandModel brandModel;
    private boolean isFree;
    private Double unitPrice;
    private Double unit;
    private Double totalPrice;
    private Double discount;
    private Double priceBeforeVat;
    private Double priceAfterVat;
    private Double vatPrice;

}
