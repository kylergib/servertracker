package com.kyle.utility;

import com.kyle.model.Config;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 * Class to manage the database connection
 * @author Kyle Gibson
 */
public abstract class DBConnection {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//" + Config.getDatabaseDomain();
    private static final String port = Config.getDatabasePort();
    private static final String databaseName = "/" + Config.getDatabaseName();
    private static final String timeConnection = "?connectionTimeZone=SERVER";
    private static final String jdbcUrl = protocol + vendor + location + ":" + port + databaseName + timeConnection; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = Config.getDatabaseUsername(); // Username
    private static String password = Config.getDatabasePassword(); // Password
    public static Connection connection;  // Connection Interface
    /**
     * opens a connection to the database
     */
    public static void openConnection() {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        } catch (Exception e) {
            System.out.println("Error database:" + e.getMessage());
        }
    }
}
