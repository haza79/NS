package com.ns;

import lombok.Data;

@Data
public class CustomerModel {

    private String cust_id; //รหัสผลิต/ตัวแทนจำหน่วย
    private String cust_name; //ชื่อกิจการ/บริษัท ผลิต/ตัวแทนจำหน่วย
    private String cust_address; //ที่อยู่
    private String cust_phone; //เบอร์โทร
    private String cust_fax; //FAX
    private String cust_email; //E-Mail
    private String cust_pricelevel; //ระดับราคาลูกค้า


}
