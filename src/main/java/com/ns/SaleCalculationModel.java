package com.ns;

import lombok.Data;

@Data
public class SaleCalculationModel {

    private Double totalProductPrice = 0.0;
    private Double totalDiscount = 0.0;
    private Double totalProductPriceBeforeVat = 0.0;
    private Double totalVat = 0.0;
    private Double totalPrice = 0.0;

}
