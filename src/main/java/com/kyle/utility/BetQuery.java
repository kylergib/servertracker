package com.kyle.utility;


import com.kami.lookout.Bet;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * class to query the database
 * @author Kyle Gibson
 */

public class BetQuery implements Serializable {
    public static List<Bet> getAllBets() throws SQLException {
        List<Bet> allBets = new ArrayList<>();
        String sql = "SELECT * FROM bets";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int betId = rs.getInt("Bet_ID");
            String sportsbook = rs.getString("Sportsbook");
            Timestamp date = rs.getTimestamp("date");
            String legs = rs.getString("Legs");
            int odds = rs.getInt("Odds");
            String status = rs.getString("Status");
            double stake = rs.getDouble("Stake");
            double profit = rs.getDouble("Profit");
            String tags = rs.getString("Tags");
            double freeBetStake = rs.getDouble("Free_Bet_Stake");
            double freeBetWon = rs.getDouble("Free_Bet_Won");
            double evPercent = rs.getDouble("Ev_Percent");
            double expectedProfit = rs.getDouble("Expected_Profit");
            int freeBetReceived = rs.getInt("Free_Bet_Received");
            Bet newBet = new Bet(betId,sportsbook, date, legs,odds,
                    status, stake, profit, tags, freeBetStake,
                    freeBetWon, evPercent, expectedProfit, freeBetReceived);
            allBets.add(newBet);
        }
        return allBets;

    }
    public static List<Bet> getSingleFilterBetsMonth(String likeTag) throws SQLException {
        List<Bet> filterBets = new ArrayList<>();

        String sql = "SELECT * FROM bets WHERE (Date BETWEEN ? and ?) AND (Tags LIKE \"%" + likeTag + "%\")";
        System.out.println(sql);
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);

//        ps.setTimestamp(1,firstDay);
//        ps.setTimestamp(2,lastDay);
        System.out.println(sql);
        System.out.println(ps.toString());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int betId = rs.getInt("Bet_ID");
            String sportsbook = rs.getString("Sportsbook");
            Timestamp date = rs.getTimestamp("date");
            String legs = rs.getString("Legs");
            int odds = rs.getInt("Odds");
            String status = rs.getString("Status");
            double stake = rs.getDouble("Stake");
            double profit = rs.getDouble("Profit");
            String tags = rs.getString("Tags");
            double freeBetStake = rs.getDouble("Free_Bet_Stake");
            double freeBetWon = rs.getDouble("Free_Bet_Won");
            double evPercent = rs.getDouble("Ev_Percent");
            double expectedProfit = rs.getDouble("Expected_Profit");
            int freeBetReceived = rs.getInt("Free_Bet_Received");
            Bet newBet = new Bet(betId,sportsbook, date, legs,odds,
                    status, stake, profit, tags, freeBetStake,
                    freeBetWon, evPercent, expectedProfit, freeBetReceived);
            filterBets.add(newBet);
        }
        return filterBets;
    }

    public static List<Bet> getSingleFilterBetsBetweenDates(String likeTag, Timestamp startDay, Timestamp endDay) throws SQLException {
        List<Bet> filterBets = new ArrayList<>();

        String sql = "SELECT * FROM bets WHERE (Date BETWEEN ? and ?) AND (Tags LIKE \"%" + likeTag + "%\")";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setTimestamp(1,startDay);
        ps.setTimestamp(2,endDay);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int betId = rs.getInt("Bet_ID");
            String sportsbook = rs.getString("Sportsbook");
            Timestamp date = rs.getTimestamp("date");
            String legs = rs.getString("Legs");
            int odds = rs.getInt("Odds");
            String status = rs.getString("Status");
            double stake = rs.getDouble("Stake");
            double profit = rs.getDouble("Profit");
            String tags = rs.getString("Tags");
            double freeBetStake = rs.getDouble("Free_Bet_Stake");
            double freeBetWon = rs.getDouble("Free_Bet_Won");
            double evPercent = rs.getDouble("Ev_Percent");
            double expectedProfit = rs.getDouble("Expected_Profit");
            int freeBetReceived = rs.getInt("Free_Bet_Received");
            Bet newBet = new Bet(betId,sportsbook, date, legs,odds,
                    status, stake, profit, tags, freeBetStake,
                    freeBetWon, evPercent, expectedProfit, freeBetReceived);
            filterBets.add(newBet);
        }
        return filterBets;
    }
    public static String delete(int betId) throws SQLException {
        String sql = "DELETE FROM bets WHERE Bet_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, betId);
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            return ("Deleted " + betId);
        } else {
            return "Could not delete bet";
        }

    }
    public static int insert(Bet bet) throws SQLException {
        String sql = "INSERT INTO bets (Sportsbook, Date, Legs, Odds, Status, Stake, Profit, Tags, Free_Bet_Stake, Free_Bet_Won, Ev_Percent, Expected_Profit, Free_Bet_Received) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setFetchSize(Integer.MIN_VALUE);
        ps.setString(1,bet.getSportsbook());
        ps.setTimestamp(2, bet.getDate());
        ps.setString(3, bet.getLegs());
        ps.setInt(4, bet.getOdds());
        ps.setString(5, bet.getStatus());
        ps.setDouble(6, bet.getStake());
        ps.setDouble(7, bet.getProfit());
        ps.setString(8, bet.getTags());
        ps.setDouble(9, bet.getFreeBetStake());
        ps.setDouble(10, bet.getFreeBetWon());
        ps.setDouble(11, bet.getEvPercent());
        ps.setDouble(12, bet.getExpectedProfit());
        ps.setInt(13, bet.getFreeBetReceived());
        ps.executeUpdate();

//        return rowsAffected;
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                int betId = rs.getInt(1);
                System.out.println("Bet ID: " + betId);
                return betId;
            }
        } catch (SQLException e) {
            return -100;
        }
        return -200;
    }
    public static int update(Bet bet) throws SQLException {
        System.out.println("CHECK IF VALID: " + DBConnection.connection.isValid(30));
//        String sql = "UPDATE bets SET Sportsbook = ?, Date = ?, Legs = ?, Odds = ?, Status = ?, Stake = ?, Profit = ?, Tags = ?, Free_Bet_Stake = ?, Free_Bet_Won = ?, Ev_Percent = ?, Expected_Profit = ?, Free_Bet_Received = ? WHERE Bet_ID = ?";
        String sql = "UPDATE bets SET Sportsbook = ? WHERE Bet_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setFetchSize(Integer.MIN_VALUE);
        System.out.println("SPRTOSBOOK TO SET: " + bet.getSportsbook());
        ps.setString(1,bet.getSportsbook());
//        ps.setTimestamp(2, bet.getDate());
//        ps.setString(3, bet.getLegs());
//        ps.setInt(4, bet.getOdds());
//        ps.setString(5, bet.getStatus());
//        ps.setDouble(6, bet.getStake());
//        ps.setDouble(7, bet.getProfit());
//        ps.setString(8, bet.getTags());
//        ps.setDouble(9, bet.getFreeBetStake());
//        ps.setDouble(10, bet.getFreeBetWon());
//        ps.setDouble(11, bet.getEvPercent());
//        ps.setDouble(12, bet.getExpectedProfit());
//        ps.setInt(13, bet.getFreeBetReceived());
//        ps.setInt(14, bet.getBetId());
        ps.setInt(2, bet.getBetId());
        System.out.println(bet.getBetId() + " THIS IS BET ID TJAT IS BEING UPDATED");
        int rowsAffected = ps.executeUpdate();
//        return rowsAffected;
        if (rowsAffected > 0) {
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int primaryKey = rs.getInt(1); // assuming that the primary key column is an integer
                System.out.println("Primary key of updated row: " + primaryKey);
                return rowsAffected;
            }
        }

        return rowsAffected;
    }

}


//    USE bet_tracker;
//    CREATE TABLE IF NOT EXISTS `bets` (
//        `Bet_ID` int NOT NULL AUTO_INCREMENT,
//        `Sportsbook` varchar(50) DEFAULT NULL,
//        `Date` datetime DEFAULT NULL,
//        `Legs` varchar(500) DEFAULT NULL,
//        `Odds` int NULL DEFAULT NULL,
//        `Status` varchar(50) DEFAULT NULL,
//        `Stake` double NULL DEFAULT NULL,
//        `Profit` double NULL DEFAULT NULL,
//        `Tags` varchar(500) DEFAULT NULL,
//        `Free_Bet_Stake` double NULL DEFAULT NULL,
//        `Free_Bet_Won` double NULL DEFAULT NULL,
//        `Ev_Percent` double NULL DEFAULT NULL,
//        `Expected_Profit` double NULL DEFAULT NULL,
//        `Free_Bet_Received` bool NULL DEFAULT NULL,
//        PRIMARY KEY (`Bet_ID`)
//        ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
