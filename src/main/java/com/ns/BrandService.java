package com.ns;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrandService {

    private Connection connection;

    public BrandService() {
        this.connection = AccessDatabaseConnection.getConnection();
    }

    public List<BrandModel> getAllBrand(){
        List<BrandModel> brandModels = new ArrayList<>();

        if(connection == null){
            return null;
        }

        try{
            String query = "SELECT * FROM brand";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){

                BrandModel brandModel = new BrandModel();
                brandModel.setBrand_id(resultSet.getString("brand_id"));
                brandModel.setBrand_name(resultSet.getString("brand_name"));

                brandModels.add(brandModel);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return brandModels;


    }

    public Map<String,String> getAsMap(List<BrandModel> brands){
        Map<String, String> brandMap = new HashMap<>();
        for(BrandModel brand:brands){
            brandMap.put(brand.getBrand_id(),brand.getBrand_name());
        }
        return brandMap;
    }

}
