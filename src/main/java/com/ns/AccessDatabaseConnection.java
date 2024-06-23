package com.ns;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class AccessDatabaseConnection {

    private static final String CONFIG_FILE = "db.config";
    private static Connection connection;

    private AccessDatabaseConnection() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() {
        if (connection == null) {
            synchronized (AccessDatabaseConnection.class) {
                if (connection == null) {
                    connection = createConnection();
                }
            }
        }
        return connection;
    }

    private static Connection createConnection() {
        Properties properties = new Properties();

        try (InputStream input = AccessDatabaseConnection.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
             InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + CONFIG_FILE);
                return null;
            }

            // Load the properties file with UTF-8 encoding
            properties.load(reader);

            // Get the property values
            String dbPath = properties.getProperty("dbpath");
            String dbPassword = properties.getProperty("dbpass");

            // UCanAccess connection URL
            String url = "jdbc:ucanaccess://" + dbPath + ";password=" + dbPassword;

            // Load the UCanAccess driver
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            // Establish and return the connection
            return DriverManager.getConnection(url);

        } catch (IOException | ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
