package com.ns;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private Connection connection;

    public ProductService() {
        this.connection = AccessDatabaseConnection.getConnection();
    }

    public List<ProductModel> searchProductByName(String searchName){
        List<ProductModel> products = new ArrayList<>();

        if(connection == null){
            return null;
        }

        try{
            String query = "SELECT * FROM product where pro_name LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1,"%"+searchName+"%");

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){

                ProductModel product = new ProductModel();
                product.setPro_id(resultSet.getString("pro_id"));
                product.setBrand_id(resultSet.getString("brand_id"));
                product.setPro_name(resultSet.getString("pro_name"));
                product.setPro_model(resultSet.getString("pro_model"));
                product.setPro_color(resultSet.getString("pro_color"));
                product.setPro_costprice(resultSet.getDouble("pro_costprice"));
                product.setPro_costpriceavg(resultSet.getDouble("pro_costpriceavg"));
                product.setPro_buyprice(resultSet.getDouble("pro_buyprice"));
                product.setPro_buypricelevel1(resultSet.getDouble("pro_buypricelevel1"));
                product.setPro_buypricelevel2(resultSet.getDouble("pro_buypricelevel2"));
                product.setPro_buypricelevel3(resultSet.getDouble("pro_buypricelevel3"));
                product.setPro_buypricelevel4(resultSet.getDouble("pro_buypricelevel4"));
                product.setPro_stock(resultSet.getDouble("pro_stock"));
                product.setPro_barcode(resultSet.getString("pro_barcode"));
                product.setPro_minlevel1(resultSet.getDouble("pro_minlevel1"));
                product.setPro_minlevel2(resultSet.getDouble("pro_minlevel2"));
                product.setPro_serialstatus(resultSet.getString("pro_serialstatus"));
                product.setPro_status(resultSet.getString("pro_status"));

                products.add(product);

            }

            resultSet.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return products;

    }

    public List<ProductModel> searchProductByBrandId(String brandId){
        List<ProductModel> products = new ArrayList<>();

        if(connection == null){
            return null;
        }

        try{
            String query = "SELECT * FROM product where brand_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1,brandId);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){

                ProductModel product = new ProductModel();
                product.setPro_id(resultSet.getString("pro_id"));
                product.setBrand_id(resultSet.getString("brand_id"));
                product.setPro_name(resultSet.getString("pro_name"));
                product.setPro_model(resultSet.getString("pro_model"));
                product.setPro_color(resultSet.getString("pro_color"));
                product.setPro_costprice(resultSet.getDouble("pro_costprice"));
                product.setPro_costpriceavg(resultSet.getDouble("pro_costpriceavg"));
                product.setPro_buyprice(resultSet.getDouble("pro_buyprice"));
                product.setPro_buypricelevel1(resultSet.getDouble("pro_buypricelevel1"));
                product.setPro_buypricelevel2(resultSet.getDouble("pro_buypricelevel2"));
                product.setPro_buypricelevel3(resultSet.getDouble("pro_buypricelevel3"));
                product.setPro_buypricelevel4(resultSet.getDouble("pro_buypricelevel4"));
                product.setPro_stock(resultSet.getDouble("pro_stock"));
                product.setPro_barcode(resultSet.getString("pro_barcode"));
                product.setPro_minlevel1(resultSet.getDouble("pro_minlevel1"));
                product.setPro_minlevel2(resultSet.getDouble("pro_minlevel2"));
                product.setPro_serialstatus(resultSet.getString("pro_serialstatus"));
                product.setPro_status(resultSet.getString("pro_status"));

                products.add(product);

            }

            resultSet.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return products;

    }

    public static void main(String[] args) {
        ProductService productService = new ProductService();
        List<ProductModel> products = productService.searchProductByName("เกตุเพชร");

        // Print the products
        for (ProductModel product : products) {
            System.out.println("name: "+product.getPro_name());
        }

    }

}
