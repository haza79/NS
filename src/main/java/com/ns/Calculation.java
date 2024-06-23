package com.ns;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Calculation {

    private static final Double vat = 7.0;

    // สูตรการคำนวณหา VAT จากราคายังไม่รวม VAT
    // เช่น ของราคา 100 บาท , VAT = 100 x (7 ÷ 100), ได้ VAT = 7 บาท
    public static Double calculateVatFromPriceExcludeVat(Double priceExcludeVat){
        // สูตรหา VAT = ราคาสินค้าไม่รวม VAT x (7 ÷ 100)
        return (priceExcludeVat*(vat/100));
    }

    // สูตรการคำนวณหา VAT จากราคารวม VAT แล้ว
    // เช่น ของราคา 100 บาท , VAT = 100 - (100 ÷ 107) x 100, ได้ VAT = 6.54 บาท (ปัดทศนิยม)
    public static Double calculateVatFromPriceIncludeVat(Double priceIncludeVat){
        // สูตรหา VAT = ราคารวม VAT - (ราคารวม VAT ÷ 107) x 100)
        return ( priceIncludeVat-((priceIncludeVat/107)*100) );
    }

    //  ราคารวม VAT
    // เช่น VAT 7 บาท , ราคารวม VAT = 7 x 107 ÷ 7, ได้ ราคารวม VAT = 107 บาท
    public static Double calculatePriceIncludeVat(Double vatPrice){
        // สูตรหาราคารวม VAT = VAT x 107 ÷ 7
        return (vatPrice*107/vat);
    }

    // ราคาไม่รวม VAT
    // ราคารวม VAT - VAT = 107 - 7 ได้ ราคาไม่รวม VAT = 100 บาท
    public static Double calculatePriceNotIncludeVat(Double priceIncludeVat,Double vatPrice){
        // ราคารวม VAT - VAT = 107 - 7 ได้ ราคาไม่รวม VAT = 100 บาท
        return (priceIncludeVat-vatPrice);
    }

}
