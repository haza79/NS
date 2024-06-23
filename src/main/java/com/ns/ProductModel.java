package com.ns;

import lombok.Data;

@Data
public class ProductModel {

    private String pro_id;              //รหัสสินค้า
    private String brand_id;            //รหัสยี่ห้อ
    private String pro_name;            //ชื่อสินค้า
    private String pro_model;           //รายละเอียด
    private String pro_color;           //สีสินค้า (ไม่ใช้แล้ว 12-2-2555)
    private Double pro_costprice;       //ราคาต้นทุน ณ ปัจจุบัน
    private Double pro_costpriceavg;    //ราคาต้นทุนเฉลี่ย ณ ปัจจุบัน
    private Double pro_buyprice;        //ราคาขาย
    private Double pro_buypricelevel1;  //รายคาขาย Level1
    private Double pro_buypricelevel2;  //รายคาขาย Level2
    private Double pro_buypricelevel3;  //รายคาขาย Level3
    private Double pro_buypricelevel4;  //รายคาขาย Level4
    private Double pro_stock;           //จำนวนคงเหลือ
    private String pro_barcode;         //รหัสบาร์โค้ด
    private Double pro_minlevel1;       //แจ้งเตือนระดับต่ำ
    private Double pro_minlevel2;       //แจ้งเตือนระดับต่ำสุด
    private String pro_serialstatus;    //มี Serial Number หรือไม่ (YES,NO)
    private String pro_status;          //สถานะสินค้า (0=ปกติ, 1=ยกเลิก/ซ่อน)

}
