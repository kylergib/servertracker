package com.kyle.model;

import com.kyle.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class Config {
    private static final String CONFIG_FILE = "config.properties";
    private static InputStream input = Main.class.getResourceAsStream(CONFIG_FILE);
    private static Properties props = new Properties();

    public static void setProps() throws IOException {
        props.load(input);
    }
    public static int getServerPort() {
        return Integer.parseInt(props.getProperty("server.port"));
    }
    public static String getServerPassword() {
        return props.getProperty("server.password");
    }

    public static String getDatabaseDomain() {
        return props.getProperty("database.domain");
    }
    public static String getDatabasePort() {
        return props.getProperty("database.port");
    }
    public static String getDatabaseName() {
        return props.getProperty("database.name");
    }
    public static String getDatabaseUsername() {
        return props.getProperty("database.username");
    }
    public static String getDatabasePassword() {
        return props.getProperty("database.password");
    }
}
