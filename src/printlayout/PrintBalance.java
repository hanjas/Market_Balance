/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package printlayout;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.print.Paper;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 *
 * @author Haxx
 */
public class PrintBalance extends BorderPane {
    public PrintBalance(ResultSet rs, String date) throws SQLException {
        nameList1 = FXCollections.observableArrayList();
        nameList2 = FXCollections.observableArrayList();
        dateList1 = FXCollections.observableArrayList();
        dateList2 = FXCollections.observableArrayList();
        oldBalList1 = FXCollections.observableArrayList();
        oldBalList2 = FXCollections.observableArrayList();
        todayList1 = FXCollections.observableArrayList();
        todayList2 = FXCollections.observableArrayList();
        totalList1 = FXCollections.observableArrayList();
        totalList2 = FXCollections.observableArrayList();
        
        nameListView1 = new ListView();
        nameListView2 = new ListView();
        dateListView1 = new ListView();
        dateListView2 = new ListView();
        oldBalListView1 = new ListView();
        oldBalListView2 = new ListView();
        todayListView1 = new ListView();
        todayListView2 = new ListView();
        totalListView1 = new ListView();
        totalListView2 = new ListView();
        
        mainGrid = new GridPane();
        leftGrid = new GridPane();
        rightGrid = new GridPane();
        topBorder = new BorderPane();
        
        nameLabel = new Label("BalanceSheet");
        dateLabel = new Label(date);
        
        nameLabel1 = new Label("Name");
        nameLabel2 = new Label("Name");
        dateLabel1 = new Label("Date");
        dateLabel2 = new Label("Date");
        oldBalLabel1 = new Label("OldBal");
        oldBalLabel2 = new Label("OldBal");
        todayLabel1 = new Label("Today");
        todayLabel2 = new Label("Today");
        totalLabel1 = new Label("Total");
        totalLabel2 = new Label("Total");
        
        leftGrid.add(nameLabel1, 0, 0);
        leftGrid.add(nameListView1, 0, 1);
        leftGrid.add(dateLabel1, 1, 0);
        leftGrid.add(dateListView1, 1, 1);
        leftGrid.add(oldBalLabel1, 2, 0);
        leftGrid.add(oldBalListView1, 2, 1);
        leftGrid.add(todayLabel1, 3, 0);
        leftGrid.add(todayListView1, 3, 1);
        leftGrid.add(totalLabel1, 4, 0);
        leftGrid.add(totalListView1, 4, 1);
        
        rightGrid.add(nameLabel2, 0, 0);
        rightGrid.add(nameListView2, 0, 1);
        rightGrid.add(dateLabel2, 1, 0);
        rightGrid.add(dateListView2, 1, 1);
        rightGrid.add(oldBalLabel2, 2, 0);
        rightGrid.add(oldBalListView2, 2, 1);
        rightGrid.add(todayLabel2, 3, 0);
        rightGrid.add(todayListView2, 3, 1);
        rightGrid.add(totalLabel2, 4, 0);
        rightGrid.add(totalListView2, 4, 1);
        
        
        mainGrid.add(leftGrid, 0, 0);
        mainGrid.add(rightGrid, 1, 0);
        topBorder.setLeft(nameLabel);
        topBorder.setCenter(dateLabel);
        topBorder.setRight(pageLabel);
        topBorder.setPrefHeight(30);
        prefHeight(Paper.A4.getHeight());
        prefWidth(Paper.A4.getWidth());
        setCenter(mainGrid);
        setTop(topBorder);
        setPadding(new Insets(10));
        setListSize((Paper.A4.getWidth()-20)/10, Paper.A4.getHeight()-80);
        setList(rs);
    }
    public void setListSize(double width, double height) {
        nameListView1.setPrefSize(width, height);
        nameListView2.setPrefSize(width, height);
        dateListView1.setPrefSize(width, height);
        dateListView2.setPrefSize(width, height);
        oldBalListView1.setPrefSize(width, height);
        oldBalListView2.setPrefSize(width, height);
        todayListView1.setPrefSize(width, height);
        todayListView2.setPrefSize(width, height);
        totalListView1.setPrefSize(width, height);
        totalListView2.setPrefSize(width, height);
    }
    public void setList(ResultSet rs) throws SQLException {
            int i=0;
        while(rs.next()) {
            if(i==600){
                nameList1.clear();
                nameList2.clear(); 
                dateList1.clear(); 
                dateList2.clear(); 
                oldBalList1.clear();
                oldBalList2.clear();
                todayList1.clear();
                todayList2.clear();
                totalList1.clear();
                totalList2.clear();
//                i=0;
            }
            i++;
            if(i<30){
                nameList1.add(rs.getString("name"));
                dateList1.add(rs.getString("date"));
                oldBalList1.add(rs.getString("oldBal"));
                todayList1.add(rs.getString("today"));
                totalList1.add(rs.getString("total"));
            } else if(i>=30 && i<60) {
                nameList2.add(rs.getString("name"));
                dateList2.add(rs.getString("date"));
                oldBalList2.add(rs.getString("oldBal"));
                todayList2.add(rs.getString("today"));
                totalList2.add(rs.getString("total"));
            }
        }
        nameListView1.setItems(nameList1);
        nameListView2.setItems(nameList2);
        dateListView1.setItems(dateList1);
        dateListView2.setItems(dateList2);
        oldBalListView1.setItems(oldBalList1);
        oldBalListView2.setItems(oldBalList2);
        todayListView1.setItems(todayList1);
        todayListView2.setItems(todayList2);
        totalListView1.setItems(totalList1);
        totalListView2.setItems(totalList2);
    }
    
    private ObservableList nameList1, nameList2, dateList1, dateList2, oldBalList1;
    private ObservableList oldBalList2, todayList1, todayList2, totalList1, totalList2;
    private ListView nameListView1, nameListView2, dateListView1, dateListView2, oldBalListView1;
    private ListView oldBalListView2, todayListView1, todayListView2, totalListView1, totalListView2;
    private GridPane mainGrid, leftGrid, rightGrid;
    private BorderPane topBorder;
    private Label nameLabel, dateLabel, pageLabel;
    private Label nameLabel1, nameLabel2, dateLabel1, dateLabel2, oldBalLabel1, oldBalLabel2;
    private Label todayLabel1, todayLabel2, totalLabel1, totalLabel2;
    
}
