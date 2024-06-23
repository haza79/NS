package com.ns;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    private Connection connection;

    public CustomerService(){
        this.connection = AccessDatabaseConnection.getConnection();
    }

    public List<CustomerModel> searchCustomerByName(String searchName){
        List<CustomerModel> customers = new ArrayList<>();
        if(connection == null){
            return null;
        }

        try{
            String query = "SELECT * FROM customer where cust_name LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1,"%"+searchName+"%");

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                CustomerModel customerModel = new CustomerModel();
                customerModel.setCust_id(resultSet.getString("cust_id"));
                customerModel.setCust_name(resultSet.getString("cust_name"));
                customerModel.setCust_address(resultSet.getString("cust_address"));
                customerModel.setCust_phone(resultSet.getString("cust_phone"));
                customerModel.setCust_fax(resultSet.getString("cust_fax"));
                customerModel.setCust_email(resultSet.getString("cust_email"));
                customerModel.setCust_pricelevel(resultSet.getString("cust_pricelevel"));

                customers.add(customerModel);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return customers;
    }

    public static void main(String[] args) {
        CustomerService customerService = new CustomerService();
        List<CustomerModel> customers = customerService.searchCustomerByName("จิราพร");

        // Print the products
        for (CustomerModel customer : customers) {
            System.out.println("name: "+customer.getCust_name());
        }

    }

}
