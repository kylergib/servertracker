package com.kyle.model;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;

import java.security.*;
import java.security.cert.CertificateException;

/**
 *
 * @author Kyle Gibson
 */
public class Server {
    private SSLServerSocket sslServerSocket;


    public Server(int port) throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {


        KeyStore keyStore = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream("/app/serverKeyStore.jks");
        keyStore.load(fis, Config.getServerPassword().toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, Config.getServerPassword().toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);


        try {
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start() {
        System.out.println("Starting server?");

        while (true) {
            try {
                SSLSocket client = (SSLSocket) sslServerSocket.accept();
                ClientHandler handler = new ClientHandler(client);
                new Thread(handler).start();
                System.out.println("New client");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


