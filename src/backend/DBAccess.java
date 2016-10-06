/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 *
 * @author Haxx
 */
public class DBAccess {
    
    public DBAccess() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:datas.db");
        stmt = connection.createStatement();
        list = FXCollections.observableArrayList();
        nameList = FXCollections.observableArrayList();
        
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Members ("       // Members Table
                + "Id INTEGER PRIMARY KEY, "
                + "Date TEXT, "
                + "Name TEXT UNIQUE, "
                + "Balance TEXT ,"
                + "lastId TEXT "
                + "); ");
        
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS AllDetails ("    // AllDetails Table
                + "Id INTEGER PRIMARY KEY, "
                + "Name TEXT, "
                + "Date TEXT, "
                + "OldBal TEXT, "
                + "Today TEXT, "
                + "Total TEXT, "
                + "Debit TEXT, "
                + "Balance TEXT, "
                + "FirstData TEXT"
                + "); ");
//        getPrevBalance("5-3-2016", "ROSHAN");
    }
    
//    Methods Decleration
    public void sampleDatas() throws SQLException {
        for(int i=0;i<100;i++){
            createMember("3-08-2016", "Nam"+i, i+"");
            insertPriceDetails("4-08-2016", "Nam"+i, i+"");
        }
    }
    
    public void createMember(String date, String name, String balance) throws SQLException{
        stmt.executeUpdate("Insert into Members (Date, Name, Balance) Values ('"+date+"', '"+name+"', '"+balance+"'); ");
        stmt.executeUpdate("Insert into AllDetails (Date, Name, OldBal, Today, Total, Debit, Balance, FirstData) Values ("
                + " '"+date+"', "
                + " '"+name+"', "
                + " '0', "
                + " '"+balance+"', "
                + " '"+balance+"', "
                + " '', "
                + " '"+balance+"', "
                + " '1' "
                + "); ");
    }
    public void removeMember(ObservableList list) throws SQLException {
        for (Object name : list) {
            stmt.executeUpdate("DELETE FROM Members WHERE Name = '"+ name +"'");
            stmt.executeUpdate("DELETE FROM AllDetails WHERE Name = '"+ name +"'");
        }
    }
    public void insertPriceDetails(String date, String name, String todayAmount) throws SQLException{
        long oldbalance, total, balance, debit = 0;
        oldbalance = Long.parseLong(getPrevBalance(prevtDate(date), name));
        
        rs = stmt.executeQuery("select * from AllDetails where Name = '"+ name +"' and Date = '"+ date +"'");
        checker = 0;
        while(rs.next()){ 
            checker = 1; 
            debit = rs.getLong("debit");
//            if(rs.getInt("FirstData") == 1)
//                oldbalance = rs.getLong("oldBal");
        }    // Checking if the data is alredy inserted
        
        total = oldbalance + Long.parseLong(todayAmount);
        balance = total - debit;
        
        if( checker==0 ){                   // If not exist insert as new
            stmt.executeUpdate("Insert into AllDetails (Date, Name, OldBal, Today, Total, Debit, Balance, FirstData) Values ("
                + " '"+date+"', "
                + " '"+name+"', "
                + " '"+oldbalance+"', "
                + " '"+todayAmount+"', "
                + " '"+total+"', "
                + " '', "
                + " '"+balance+"', "
                + " '0' "
                + "); ");
        } else if(checker == 1) {           // If already exist then update the data
            stmt.executeUpdate("UPDATE AllDetails SET oldBal='"+oldbalance+"', today='"+ todayAmount +"', total='"+ total +"', balance='"+ balance +"' where Name = '"+ name +"' and Date = '"+ date +"'");
        }
        stmt.executeUpdate("UPDATE Members set lastId='"+getLastId(name)+"' where name='"+name+"'");
//        Check the last date and this date is same or not.. If not then update the rest datas
        String lastDate = getLastDate(name);
        int dayLast = Integer.parseInt(lastDate.split("-")[0]);
        int monthLast = Integer.parseInt(lastDate.split("-")[1]);
        int yearLast = Integer.parseInt(lastDate.split("-")[2]);
        int dayToday = Integer.parseInt(date.split("-")[0]);
        int monthToday = Integer.parseInt(date.split("-")[1]);
        int yearToday = Integer.parseInt(date.split("-")[2]);
        
        if( dayToday<dayLast && monthToday<=monthLast && yearToday<=yearLast ){
            String todayDate = date;
            do {                
                todayDate = nextDate(todayDate);
                correctRestData(todayDate, name, getPrevBalance(prevtDate(todayDate), name));
            } while (!todayDate.equals(lastDate));
            if(todayDate.equals(lastDate)) 
                correctRestData(todayDate, name, getPrevBalance(prevtDate(todayDate), name));
        }
    }
    public void insertDebitDetails(String date, String name, String debit) throws SQLException{
        long oldbalance, total, balance, todayAmount = 0;
        oldbalance = Long.parseLong(getPrevBalance(prevtDate(date), name));
        
        rs = stmt.executeQuery("select * from AllDetails where Name = '"+ name +"' and Date = '"+ date +"'");
        checker = 0;
        while(rs.next()){ 
            checker = 1;
            todayAmount = rs.getLong("today"); 
            if(rs.getInt("FirstData") == 1) {
                oldbalance = rs.getLong("oldBal");
            }
        }    // Checking if the data is alredy inserted
        total = oldbalance + todayAmount;
        if(total < Long.parseLong(debit)) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Check the amount you typed");
            error.showAndWait();
            return;
        }
        balance = total - Long.parseLong(debit);
        
        if( checker==0 ){                   // If not exist insert as new
            stmt.executeUpdate("Insert into AllDetails (Date, Name, OldBal, Debit, Total, Debit, Balance, FirstData) Values ("
                + " '"+date+"', "
                + " '"+name+"', "
                + " '"+oldbalance+"', "
                + " '"+debit+"', "
                + " '"+total+"', "
                + " '', "
                + " '"+balance+"', "
                + " '0' "
                + "); ");
        } else if(checker == 1) {           // If already exist then update the data
            stmt.executeUpdate("UPDATE AllDetails SET Debit='"+ debit +"', total='"+ total +"', balance='"+ balance +"' where Name = '"+ name +"' and Date = '"+ date +"'");
        }
        stmt.executeUpdate("UPDATE Members set lastId='"+getLastId(name)+"' where name='"+name+"'");
//        Check the last date and this date is same or not.. If not then update the rest datas
        
        String lastDate = getLastDate(name);
        int dayLast = Integer.parseInt(lastDate.split("-")[0]);
        int monthLast = Integer.parseInt(lastDate.split("-")[1]);
        int yearLast = Integer.parseInt(lastDate.split("-")[2]);
        int dayToday = Integer.parseInt(date.split("-")[0]);
        int monthToday = Integer.parseInt(date.split("-")[1]);
        int yearToday = Integer.parseInt(date.split("-")[2]);
        
        if( dayToday<dayLast && monthToday<=monthLast && yearToday<=yearLast ){
            String todayDate = date;
            do {                
                todayDate = nextDate(todayDate);
                correctRestData(todayDate, name, getPrevBalance(prevtDate(todayDate), name));
            } while (!todayDate.equals(lastDate));
            if(todayDate.equals(lastDate)) 
                correctRestData(todayDate, name, getPrevBalance(prevtDate(todayDate), name));
        }
    }
    public void removeMembers(ObservableList members) throws SQLException{
        for (Object member : members) {
            stmt.executeUpdate("DELETE FROM Members WHERE name = '"+member+"'");
            stmt.executeUpdate("DELETE FROM AllDetails WHERE name = '"+member+"'");
        }
    }
    public void removeOneDetail(String date, String name) {
        String prevDate = "";
        long today =0, debit =0;
        SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
            try {
                Date fstDate = sdf.parse(getFirsttDate(name));
                Date todayDate = sdf.parse(date);
                if(!fstDate.equals(todayDate)) {
                    prevDate = getPrevDate(name.toString(), date);
//                    System.out.println(name.toString());
                    stmt.executeUpdate("delete from AllDetails where name='"+name.toString()+"' and date = '"+date+"'");
                    rs = stmt.executeQuery("select * from AllDetails where name= '"+name.toString()+ "' and date = '"+ prevDate + "'");
                    while(rs.next()) {
                        today = rs.getLong("today");
                        debit = rs.getLong("debit");
                    }
                    stmt.executeUpdate("delete from AllDetails where name='"+name.toString()+"' and date = '"+ prevDate +"'");
                    insertPriceDetails(prevDate, name.toString(), String.valueOf(today));
                    insertDebitDetails(prevDate, name.toString(), String.valueOf(debit));
                }
            } catch(Exception ex) {System.out.println(ex);}
    }
    public void removeDayList(String date) throws SQLException {
        String prevDate = "";
        long today =0, debit =0;
        nameList.clear();
        rs = stmt.executeQuery("select * from AllDetails where date = '"+ date +"'");
        while(rs.next()) {
            nameList.add(rs.getString("name"));
        }
        for(Object name : nameList) {
            SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
            try {
                Date fstDate = sdf.parse(getFirsttDate(name.toString()));
                Date todayDate = sdf.parse(date);
                if(!fstDate.equals(todayDate)) {
                    prevDate = getPrevDate(name.toString(), date);
//                    System.out.println(name.toString());
                    stmt.executeUpdate("delete from AllDetails where name='"+name.toString()+"' and date = '"+date+"'");
                    rs = stmt.executeQuery("select * from AllDetails where name= '"+name.toString()+ "' and date = '"+ prevDate + "'");
                    while(rs.next()) {
                        today = rs.getLong("today");
                        debit = rs.getLong("debit");
                    }
                    stmt.executeUpdate("delete from AllDetails where name='"+name.toString()+"' and date = '"+ prevDate +"'");
                    insertPriceDetails(prevDate, name.toString(), String.valueOf(today));
                    insertDebitDetails(prevDate, name.toString(), String.valueOf(debit));
                }
            } catch(Exception ex) {System.out.println(ex);}
            
        }
    }
    
    public void correctRestData(String date, String name, String oldBal) throws SQLException{
        long todayAmount=0, total, debit=0, balance;
        rs = stmt.executeQuery("select * from AllDetails where name='"+ name +"' and date='"+ date +"'");
        while(rs.next()){
            todayAmount = rs.getLong("today");
            debit = rs.getLong("debit");
        }
        total = todayAmount + Long.parseLong(oldBal);
        balance = total - debit;
        stmt.executeUpdate("UPDATE AllDetails SET OldBal='"+oldBal+"', Total='"+total+"', Balance='"+balance+"' where Name = '"+ name +"' and Date = '"+ date +"'");
    }
    public void renameMember(String newName, String amount, String oldName, String date) throws SQLException {
        stmt.executeUpdate("UPDATE Members SET name = '"+ newName +"', Balance = '"+ amount +"' where name = '"+ oldName +"'");
        stmt.executeUpdate("UPDATE AllDetails SET name = '"+ newName +"' where name = '"+ oldName +"'");
        long today=0, total=0, debit=0, balance=0, checker=0;
        rs = stmt.executeQuery("select * from AllDetails where name = '"+ newName +"' and date ='"+ date +"'");
        while(rs.next()) {
            checker = 1;
            today = rs.getLong("today");
            debit = rs.getLong("debit");
        }
        total = Long.parseLong(amount);
        balance = total - debit;
        if(checker == 1)
            stmt.executeUpdate("UPDATE AllDetails SET today = '"+ amount +"', total = '"+ total +"', balance = '"+ balance +"' where name = '"+ newName +"' and date = '"+ date +"'");
        else
            stmt.executeUpdate("Insert into AllDetails (Date, Name, OldBal, Today, Total, Debit, Balance, FirstData) Values ("
                + " '"+date+"', "
                + " '"+newName+"', "
                + " '0', "
                + " '"+amount+"', "
                + " '"+total+"', "
                + " '', "
                + " '"+balance+"', "
                + " '1' "
                + "); ");
        String lastDate = getLastDate(newName);
        int dayLast = Integer.parseInt(lastDate.split("-")[0]);
        int monthLast = Integer.parseInt(lastDate.split("-")[1]);
        int yearLast = Integer.parseInt(lastDate.split("-")[2]);
        int dayToday = Integer.parseInt(date.split("-")[0]);
        int monthToday = Integer.parseInt(date.split("-")[1]);
        int yearToday = Integer.parseInt(date.split("-")[2]);
        
        if( dayToday<dayLast && monthToday<=monthLast && yearToday<=yearLast ){
            String todayDate = date;
            do {                
                todayDate = nextDate(todayDate);
                correctRestData(todayDate, newName, getPrevBalance(prevtDate(todayDate), newName));
            } while (!todayDate.equals(lastDate));
            if(todayDate.equals(lastDate)) 
                correctRestData(todayDate, newName, getPrevBalance(prevtDate(todayDate), newName));
        }
    }
    
    public String getRenameBalance(String name) throws SQLException {
        String balance = "";
        rs = stmt.executeQuery("select balance from Members where name = '"+ name +"' ");
        while(rs.next()){
            balance = rs.getString("balance");
        }
        return balance;
    }
    public String getRenameDate(String name) throws SQLException {
        String date = "";
        rs = stmt.executeQuery("select date from Members where name = '"+ name +"' ");
        while(rs.next()){
            date = rs.getString("date");
        }
        return date;
    }
    public ObservableList getMembers() throws SQLException{
        list.clear();
        rs = stmt.executeQuery("SELECT Name FROM Members ORDER BY Name");
        while(rs.next()) { list.add(rs.getString("Name"));} 
        return list;
    }
    public String getPrevBalance(String date, String name) throws SQLException {
        
        SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
        try {
            Date fstDate = sdf.parse(getFirsttDate(name));
            Date todayDate = sdf.parse(date);
//            System.out.println(sdf.format(fstDate)+" "+sdf.format(todayDate));
            if(fstDate.before(todayDate)) {
                String ba = "";
                do {            
                   rs = stmt.executeQuery("select * from AllDetails where Name = '"+ name +"' and Date = '"+ date +"'");
                   while(rs.next()) { ba = rs.getString("Balance"); }
                   date = prevtDate(date);
                } while (ba.equals(""));
                return ba;
            }
            if(fstDate.equals(todayDate)) {
                return "0";
            }
        } catch (ParseException ex) { Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex); }
        
        
        return "0";
    }
    public String getPrevDate(String name, String date) throws SQLException{
        SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
        try {
            Date fstDate = sdf.parse(getFirsttDate(name));
            Date todayDate = sdf.parse(date);
//            System.out.println(sdf.format(fstDate)+" "+sdf.format(todayDate));
            if(fstDate.before(todayDate) || fstDate.equals(todayDate)) {
                String ba="", prevDate = date;
                do {      
                    prevDate = prevtDate(prevDate);
                    rs = stmt.executeQuery("select * from AllDetails where name ='"+name+"' and date = '"+ prevDate +"'");
                    while(rs.next()) {
                        ba = rs.getString("date");
                    }
                } while (ba.equals(""));
                return ba;
            }
        }catch(Exception ex) {System.out.println(ex);}
        
        return "";
    }
    public String getLastDate(String name) throws SQLException{
        rs = stmt.executeQuery("select * from AllDetails where name ='"+name+"'");
        int d=0,m=0,y=0;
        while(rs.next()){
            if(d<Integer.parseInt(rs.getString("date").split("-")[0])){
                if(m<=Integer.parseInt(rs.getString("date").split("-")[1]) && y<=Integer.parseInt(rs.getString("date").split("-")[2])){
                    d=Integer.parseInt(rs.getString("date").split("-")[0]);
                    m=Integer.parseInt(rs.getString("date").split("-")[1]);
                    y=Integer.parseInt(rs.getString("date").split("-")[2]);
                }
            }else{
                if(m<Integer.parseInt(rs.getString("date").split("-")[1]) && y<=Integer.parseInt(rs.getString("date").split("-")[2])){
                    d=Integer.parseInt(rs.getString("date").split("-")[0]);
                    m=Integer.parseInt(rs.getString("date").split("-")[1]);
                    y=Integer.parseInt(rs.getString("date").split("-")[2]);
                }
            }
        }
        if(d==0 && m==0 && y==0){
            rs = stmt.executeQuery("select * from Members where name = '"+name+"' ");
            while(rs.next()){
                d=Integer.parseInt(rs.getString("date").split("-")[0]);
                m=Integer.parseInt(rs.getString("date").split("-")[1]);
                y=Integer.parseInt(rs.getString("date").split("-")[2]);
            }
        }
//        System.out.println("getDate "+name+"  "+d+"-"+m+"-"+y);
        return ""+d+"-"+m+"-"+y;
    }
    public String getLastId(String name) throws SQLException {
        String lastId="1";
        rs = stmt.executeQuery("select max(id) as lastId from AllDetails where name='"+name+"'");
        while(rs.next()) {
            lastId = rs.getString("lastId");
        }
        return lastId;
    }
    public String getFirsttDate(String name) throws SQLException{
        String date = "";
        rs = stmt.executeQuery("select * from Members where name ='"+name+"'");
        while(rs.next()) {
            date = rs.getString("date");
        }
        return date;
    }
    public ResultSet getIndividualDetails(String name, String date) throws SQLException{
        return stmt.executeQuery("select * from AllDetails where name = '"+name+"' and date like '%"+date+"%' order by CAST(date AS INTEGER)");
    }
    public ResultSet getDayListDetails(String date) throws SQLException {
        return stmt.executeQuery("select * from AllDetails where date='"+ date +"'");
    }
    public ResultSet getDailyBalanceDetails(String name, String lastDate, String toDate) throws SQLException {
        int check = 0;
        rs = stmt.executeQuery("select * from AllDetails where name = '"+ name +"' and date ='"+ toDate +"'");
        while(rs.next()) {
            check = 1;
        }
        if(check == 1)
            return stmt.executeQuery("select * from AllDetails where name = '"+ name +"' and date ='"+ toDate +"'");
        String prevDate = getPrevDate(name, toDate);
        return stmt.executeQuery("select * from AllDetails where name = '"+ name +"' and date ='"+ prevDate +"'");
    }
    public ResultSet getBalanceSheet(String date) throws SQLException {
        return stmt.executeQuery("select ad.name, ad.date, ad.oldbal, ad.today, ad.total, ad.debit, ad.balance from AllDetails ad, Members m where m.lastId = ad.id and m.name = ad.name");
    }
    
    public String nextDate(String date){
        String nextDAte;
        int d,m,y;
        d = Integer.parseInt(date.split("-")[0]);
        m = Integer.parseInt(date.split("-")[1]);
        y = Integer.parseInt(date.split("-")[2]);
        if(d==31){
            d=1;
            if(m == 12){
                m = 1;
                y++;
            } else {
                m++;
            }
        } else {
            d++;
        }
        return ""+d+"-"+m+"-"+y;
    }
    public String prevtDate(String date){
        String nextDAte;
        int d,m,y;
        d = Integer.parseInt(date.split("-")[0]);
        m = Integer.parseInt(date.split("-")[1]);
        y = Integer.parseInt(date.split("-")[2]);
        if(d==1){
            d=31;
            if(m == 1){
                m = 12;
                y--;
            } else {
                m--;
            }
        } else {
            d--;
        }
        return ""+d+"-"+m+"-"+y;
    }
    
//    Variable Decleration
    public Connection connection;
    private Statement stmt;
    private ResultSet rs;
    private ObservableList list,nameList;
    private int checker, onlyOnePrevBal=0;
    
}
