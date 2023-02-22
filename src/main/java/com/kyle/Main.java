package com.kyle;


import com.kyle.model.Config;
import com.kyle.model.Server;
import com.kyle.utility.DBConnection;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, SQLException {
        System.out.println("Starting Server");
        Config.setProps();
        DBConnection.openConnection();
        if (DBConnection.connection != null) {
            Server server = new Server(Config.getServerPort());
            server.start();
        }
    }

}
