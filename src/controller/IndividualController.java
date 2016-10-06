/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import backend.DBAccess;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import model.BalanceSheetTableModel;
import model.IndividualTableModel;
import popup.AddDebit;
import popup.AddMember;
import popup.AddReport;
import popup.RemoveMember;
import popup.RenamePopup;
import printlayout.PrintIntividual;

/**
 * FXML Controller class
 *
 * @author Haxx
 */
public class IndividualController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            db = new DBAccess();
//            db.sampleDatas();
//            db.createMember("1-5-2016", "ROSHAN", "2000");
//            for(int i=30;i<70;i++){
//                db.createMember("24-5-2016", "ROSH"+i, ""+i);
//            }
            Format format = new SimpleDateFormat("M-yyyy");
            dateLabel.setText(format.format(new Date()));
            Rectangle2D d = Screen.getPrimary().getVisualBounds();
            tableList = FXCollections.observableArrayList();
            listView.setPrefHeight(d.getHeight() - 205);
            listView.setMinHeight(25);
            prevDateBtn.setText("<");
            setColumnSize((d.getWidth() - 242) / 6);
            listView.setItems(db.getMembers());
            processImageView.setVisible(false);
        } catch (Exception ex) {
            Logger.getLogger(IndividualController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setColumnSize(double width) {
        col1.setPrefWidth(width);
        col2.setPrefWidth(width);
        col3.setPrefWidth(width);
        col4.setPrefWidth(width);
        col5.setPrefWidth(width);
        col6.setPrefWidth(width);
    }

    @FXML public void addReport() {
        try {
            check = true;
            AddReport addReport = new AddReport(Alert.AlertType.NONE, db.getMembers());
            Optional<ButtonType> result = addReport.showAndWait();
            while(result.get()==ButtonType.NEXT && check==true){
                if(addReport.isTextEmpty()){
                    if(addReport.isCorrectText(db.getMembers())) {
                            db.insertPriceDetails(addReport.getDate(), addReport.getName(), addReport.getAmount());
                        setTable();
                    } else { addReport.showErrorToast(); }
                } else System.out.println("empty");
                addReport.setFocus();
                result = addReport.showAndWait();   //  ReDisplaying the alert by pressing next btn
            }
            if(result.get()==ButtonType.OK){
                if(addReport.isTextEmpty()){
                    if(addReport.isCorrectText(db.getMembers())) {
                            db.insertPriceDetails(addReport.getDate(), addReport.getName(), addReport.getAmount());
//                        else if( addReport.getTab().equals(addReport.debitDetailsTab) )
//                            db.insertDebitDetails(addReport.getDate(), addReport.getName(), addReport.getAmount());
                        setTable();
                    } else { addReport.showErrorToast(); }
                } else System.out.println("empty");
            } else { addReport.close(); }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    @FXML public void addDebit() {
        try {
            AddDebit addDebit = new AddDebit(Alert.AlertType.NONE, db.getMembers());
            Optional<ButtonType> result = addDebit.showAndWait();
            while(result.get()==ButtonType.NEXT){
                if(addDebit.isTextEmpty()){
                    if(addDebit.isCorrectText(db.getMembers())) {
                            db.insertDebitDetails(addDebit.getDate(), addDebit.getName(), addDebit.getAmount());
                        setTable();
                    } else { addDebit.showErrorToast(); }
                } else System.out.println("empty");
                addDebit.setFocus();
                result = addDebit.showAndWait();   //  ReDisplaying the alert by pressing next btn
            }
            if(result.get()==ButtonType.OK){
                if(addDebit.isTextEmpty()){
                    if(addDebit.isCorrectText(db.getMembers())) {
                            db.insertDebitDetails(addDebit.getDate(), addDebit.getName(), addDebit.getAmount());
                        setTable();
                    } else { addDebit.showErrorToast(); }
                } else System.out.println("empty");
            } else { addDebit.close(); }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    @FXML public void addMember() throws SQLException {
        AddMember addMember = new AddMember(Alert.AlertType.NONE);
        Optional<ButtonType> result = addMember.showAndWait();
        while(result.get()==ButtonType.NEXT){
            if(addMember.isNotEmpty()){
                if(addMember.isCorrectDetails()) {
                    db.createMember(addMember.getDate(), addMember.getName(), addMember.getAmount());
                    setList();
                }
            }
            addMember.setFocus();
            result = addMember.showAndWait();   //  ReDisplaying the alert by pressing next btn
        }
        if(result.get()==ButtonType.OK){
            if(addMember.isNotEmpty()){
                if(addMember.isCorrectDetails()) {
                    db.createMember(addMember.getDate(), addMember.getName(), addMember.getAmount());
                    setList();
                } else { addMember.showErrorToast(); }
            }
        } else { addMember.close(); }
    }
    @FXML public void removeMember() throws SQLException {
        RemoveMember removeMember = new RemoveMember(Alert.AlertType.NONE, db.getMembers());
        removeMember.showAndWait()
                .filter(response -> response == removeMember.removeBtn)
                .ifPresent(response -> {
            try { 
                setProcess(removeMember.getSelectedItems());
//                db.removeMember(removeMember.getSelectedItems()); 
//                setList(); 
//                setTable(); 
//                nameLabel.setText("");
            } catch (SQLException ex) { System.out.println(ex); }
        });
    }
    @FXML public void getDetails(MouseEvent mouseEvent) throws SQLException{
        if(mouseEvent.getClickCount() == 1){
            nameLabel.setText(listView.getSelectionModel().getSelectedItem().toString());
            setTable();
        } else if (mouseEvent.getClickCount() == 2){
            renameMember(listView.getSelectionModel().getSelectedItem().toString());
        }
    }
    @FXML private void prevMonth() throws SQLException{
        int monthT = Integer.parseInt(dateLabel.getText().split("-")[0]);
        int year = Integer.parseInt(dateLabel.getText().split("-")[1]);
        String month = "";
        if(monthT==1){
            month = "12";
            year--;
        } else {
            monthT--;
            month = ""+monthT;
        }
        dateLabel.setText(month+"-"+String.valueOf(year));
        setTable();
    }
    @FXML private void nextMonth() throws SQLException{
        int monthT = Integer.parseInt(dateLabel.getText().split("-")[0]);
        int year = Integer.parseInt(dateLabel.getText().split("-")[1]);
        String month = "";
        if(monthT==12){
            month = "1";
            year++;
        } else {
            monthT++;
            month = ""+monthT;
        }
        dateLabel.setText(month+"-"+String.valueOf(year));
        setTable();
    }
    @FXML public void setProcess(ObservableList memberList) throws SQLException{
        addReportBtn.setDisable(true);
        tableView.setDisable(true);
        processImageView.setVisible(true);
        nextDateBtn.setDisable(true);
        prevDateBtn.setDisable(true);
        printB.setDisable(true);
        listView.setDisable(true);
        addMemberBtn.setDisable(true);
        removeMemberBtn.setDisable(true);
        removeMemberTask = removingMember(memberList);

        removeMemberTask.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println(newValue);
            }
        });
        new Thread(removeMemberTask).start();
    }
    @FXML public void print() throws SQLException {
        PrintIntividual printIndividual = new PrintIntividual(nameLabel.getText(), db.getIndividualDetails(nameLabel.getText(), dateLabel.getText()));
    }
    @FXML private void renameMember(String name) throws SQLException {
        RenamePopup renamePopup = new RenamePopup(name, db.getRenameBalance(name), db.getRenameDate(name));
        Optional<ButtonType> result = renamePopup.showAndWait();
        if(result.get()==ButtonType.OK){
            if(renamePopup.isTextEmpty()){
                    db.renameMember(renamePopup.getName(), renamePopup.getAmount(), name, db.getRenameDate(name));
                    setList();
                    listView.getSelectionModel().select(renamePopup.getName());
                    nameLabel.setText(listView.getSelectionModel().getSelectedItem().toString());
                    setTable();
            } else System.out.println("empty");
        } else { renamePopup.close(); }
    }
    
    public void setList() throws SQLException {
        listView.getItems().clear();
        listView.setItems(db.getMembers());
    }
    public void setTable() throws SQLException{
        tableList.clear();
        rs = db.getIndividualDetails(nameLabel.getText(), dateLabel.getText());
        while(rs.next()) {
            if(rs.getLong("today") != 0 || rs.getLong("debit") != 0 )
                tableList.add(new IndividualTableModel(rs.getString("date"), rs.getString("oldBal"), rs.getString("today"), rs.getString("total"), rs.getString("debit"), rs.getString("balance")));
        }
        tableView.setItems(tableList);
    }
    public Task removingMember(ObservableList memberList) {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 1; i++) {
                    db.removeMember(memberList); 
                }
                
                return true;
            }
            @Override
            protected void succeeded() {
                try {
                    setList();
                    setTable();
                    nameLabel.setText("");
                    processImageView.setVisible(false);
                    tableView.setDisable(false);
                    addReportBtn.setDisable(false);
                    prevDateBtn.setDisable(false);
                    nextDateBtn.setDisable(false);
                    printB.setDisable(false);
                    listView.setDisable(false);
                    addMemberBtn.setDisable(false);
                    removeMemberBtn.setDisable(false);
                } catch (SQLException ex) { System.out.println(ex); }
            }
        };
    }
    
    @FXML private ListView listView;
    @FXML private Button prevDateBtn, nextDateBtn, printB, addReportBtn, addMemberBtn, removeMemberBtn;
    @FXML private Label nameLabel, dateLabel;
    @FXML private TableView tableView;
    @FXML private ImageView processImageView;
    @FXML private TableColumn col1, col2, col3, col4, col5, col6;
    private Task removeMemberTask;
    private ObservableList tableList;
    private ResultSet rs;
    private DBAccess db;
    boolean check = true;

}
