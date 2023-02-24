package com.kyle.model;


import com.kami.lookout.Bet;
import com.kyle.utility.BetQuery;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/**
 * handles receiving and sending data between server and client
 * @author Kyle Gibson
 */
public class ClientHandler implements Runnable {
    //    public Socket socket;
    public ObjectInputStream in;
    public ObjectOutputStream out;
    public Socket client;
    private boolean running = true;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        System.out.println("STarting client");
        try {
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());

            while (running) {

                Object obj = in.readObject();
                System.out.println("Received object from client: " + obj);

                if ((obj.getClass() == String.class) && (obj.equals("getBets"))) {


                    System.out.println("Get all bets");

                    List<Bet> sendSet = BetQuery.getAllBets();
                    System.out.println(sendSet.getClass());
                    out.writeObject(sendSet);
                    out.flush();
                    out.reset();
                    System.out.println("Sent object: " + obj);
                } else if ((obj instanceof List)) {


                    List<Object> recievedList = (List<Object>) obj;




                    if (recievedList.get(0).equals("betweenBetsFilter")) {
                        System.out.println("LIST: " + recievedList.get(0));
                        List<Bet> sendSet = BetQuery.getSingleFilterBetsBetweenDates((String) recievedList.get(1),
                                (Timestamp) recievedList.get(2), (Timestamp) recievedList.get(3));
                        out.writeObject(sendSet);
                        out.flush();
                        out.reset();
                        System.out.println("Sent object: " + sendSet);
                    //attempts to delete bet
                    } else if (recievedList.get(0).equals("deleteBet")) {
                        System.out.println("LIST: " + recievedList.get(0));
                        String sendString = BetQuery.delete((Integer) recievedList.get(1));
                        recievedList.remove(1);
                        out.writeObject(sendString);
                        out.flush();
                        out.reset();
                        System.out.println("Sent object: " + sendString);
                    //attempts to add bet
                    } else if (recievedList.get(0).equals("addBet")) {
                        System.out.println("LIST: " + recievedList.get(0));
                        int betId = BetQuery.insert((Bet)recievedList.get(1));

                        System.out.println("2");
                        out.writeObject(betId);
                        out.flush();
                        out.reset();
                        System.out.println("Sent object: " + betId);
                    //attempts to update bet
                    } else if (recievedList.get(0).equals("updateBet")) {
                        System.out.println("LIST: " + recievedList.get(0));
                        int rowsAffected = BetQuery.update((Bet)recievedList.get(2));

                        out.writeObject(rowsAffected);
                        out.flush();
                        out.reset();
                        System.out.println("Sent object: " + rowsAffected);
                    }
                } else {
                    out.writeObject(obj);
                    out.flush();
                    out.reset();
                    System.out.println("Sent object: in else " + obj);
                }
            }
        } catch (SocketException e) {
        } catch (EOFException e) {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }


    }
    public void close() {
        running = false;
        try {
            client.close();
//            clients.remove(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}