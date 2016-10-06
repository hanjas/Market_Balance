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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import model.DayListTableModel;
import popup.ConfirmPassword;
import popup.DeleteDayList;
import printlayout.PrintDayList;

/**
 * FXML Controller class
 *
 * @author Haxx
 */
public class DayListController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            processImage = new Image("/icons/process.gif");
            processImageView = new ImageView(processImage);
            processImageView.setVisible(false);
            Rectangle2D d = Screen.getPrimary().getVisualBounds();
            Format format = new SimpleDateFormat("d-M-yyyy");
            tableList = FXCollections.observableArrayList();
            db = new DBAccess();
            dateLabel.setText(format.format(new Date()));
            prevDateBtn.setText("<");
            refreshGrid.add(processImageView, 0, 0);
            setColumnSize((d.getWidth() - 302) / 5);
            setProcess();
        } catch (Exception ex) { System.out.println(ex); }
    }    
    private void setColumnSize(double width) {
        col1.setPrefWidth(width);
        col2.setPrefWidth(width);
        col3.setPrefWidth(width);
        col4.setPrefWidth(width);
        col5.setPrefWidth(width);
        col6.setPrefWidth(width);
    }
    @FXML public void delete() throws SQLException {
        DeleteDayList delete = new DeleteDayList(db.getMembers());
        delete.setHeight(500);
        delete.setWidth(500);
        Optional<ButtonType> result = delete.showAndWait();
        if(result.get()==ButtonType.OK){
            if(!delete.isSelected) {
                deleteProcess();
            } else if(delete.nameIsNotEmpty()) {
                ConfirmPassword confirm = new ConfirmPassword();
                confirm.setHeight(400);
                confirm.setWidth(400);
                Optional<ButtonType> result2 = confirm.showAndWait();
                if(result2.get()==ButtonType.OK){
                    if(confirm.checkPassword()) {
                        db.removeOneDetail(dateLabel.getText(), delete.getName());
                    }
                }
            }
        }
        setProcess();
    }
    @FXML public void setProcess() throws SQLException{
        refresh.setDisable(true);
        tableView.setDisable(true);
        processImageView.setVisible(true);
        nextDateBtn.setDisable(true);
        prevDateBtn.setDisable(true);
        printB.setDisable(true);
        totalSaleLabel.setText("TotalSale :");
        totalDebitLabel.setText("TotalDebit :");
        getBSDetails = createWorker();

        getBSDetails.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println(newValue);
            }
        });
        new Thread(getBSDetails).start();
    }
    @FXML public void deleteProcess() throws SQLException{
        ConfirmPassword confirm = new ConfirmPassword();
        confirm.setHeight(400);
        confirm.setWidth(400);
        Optional<ButtonType> result = confirm.showAndWait();
        if(result.get()==ButtonType.OK){
            if(confirm.checkPassword()) {
                refresh.setDisable(true);
                tableView.setDisable(true);
                processImageView.setVisible(true);
                nextDateBtn.setDisable(true);
                prevDateBtn.setDisable(true);
                printB.setDisable(true);
                totalSaleLabel.setText("TotalSale :");
                totalDebitLabel.setText("TotalDebit :");
                getBSDetails = deleteDetailsTask();

                getBSDetails.messageProperty().addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        System.out.println(newValue);
                    }
                });
                new Thread(getBSDetails).start();
                
            } else {
                System.out.println("not confirmed");
            }
        }
        
    }
    public Task deleteDetailsTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 1; i++) {
                    db.removeDayList(dateLabel.getText());
                }
                tableList.clear();
                rs = db.getDayListDetails(dateLabel.getText());
                totalSale=0;
                totalDebit=0;
                while(rs.next()){
                    if(rs.getLong("today") != 0 || rs.getLong("debit") != 0 )
                        tableList.add(new DayListTableModel(
                            rs.getString("name"),
                            rs.getString("oldBal"),
                            rs.getString("today"),
                            rs.getString("total"),
                            rs.getString("debit"),
                            rs.getString("balance")
                        ));
                    totalSale = totalSale + rs.getLong("today");
                    totalDebit = totalDebit + rs.getLong("debit");
                }
                tableView.setItems(tableList);
                processImageView.setVisible(false);
                tableView.setDisable(false);
                refresh.setDisable(false);
                prevDateBtn.setDisable(false);
                nextDateBtn.setDisable(false);
                printB.setDisable(false);
                return true;
            }
            
            @Override
            protected void succeeded() {
                totalSaleLabel.setText("TotalSale : "+totalSale);
                totalDebitLabel.setText("TotalDebit : "+totalDebit);
            }
        };
    }
    
    public Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 1; i++) {
                    tableList.clear();
                    rs = db.getDayListDetails(dateLabel.getText());
                    totalSale=0;
                    totalDebit=0;
                    while(rs.next()){
                        if(rs.getLong("today") != 0 || rs.getLong("debit") != 0 )
                            tableList.add(new DayListTableModel(
                                rs.getString("name"),
                                rs.getString("oldBal"),
                                rs.getString("today"),
                                rs.getString("total"),
                                rs.getString("debit"),
                                rs.getString("balance")
                            ));
                        totalSale = totalSale + rs.getLong("today");
                        totalDebit = totalDebit + rs.getLong("debit");
                    }
                    tableView.setItems(tableList);
                }
                processImageView.setVisible(false);
                tableView.setDisable(false);
                refresh.setDisable(false);
                prevDateBtn.setDisable(false);
                nextDateBtn.setDisable(false);
                printB.setDisable(false);
                return true;
            }
            
            @Override
            protected void succeeded() {
                totalSaleLabel.setText("TotalSale : "+totalSale);
                totalDebitLabel.setText("TotalDebit : "+totalDebit);
            }
        };
    }
    
    @FXML private void prevDate() throws SQLException{
        int day = Integer.parseInt(dateLabel.getText().split("-")[0]);
        int monthT = Integer.parseInt(dateLabel.getText().split("-")[1]);
        int year = Integer.parseInt(dateLabel.getText().split("-")[2]);
        if(day == 1){
            day = 31;
            if(monthT==1){
                monthT = 12;
                year--;
            } else {
                monthT--;
            }
        } else {
            day--;
        }
        
        dateLabel.setText(day+"-"+monthT+"-"+year);
        setProcess();
    }
    @FXML private void nextDate() throws SQLException{
        int day = Integer.parseInt(dateLabel.getText().split("-")[0]);
        int monthT = Integer.parseInt(dateLabel.getText().split("-")[1]);
        int year = Integer.parseInt(dateLabel.getText().split("-")[2]);
        if(day == 31){
            day = 1;
            if(monthT==12){
                monthT = 1;
                year++;
            } else {
                monthT++;
            }
        } else {
            day++;
        }
        dateLabel.setText(day+"-"+monthT+"-"+year);
        setProcess();
    }
    @FXML private void print() throws SQLException {
        PrintDayList printDayList = new PrintDayList(dateLabel.getText(), db.getDayListDetails(dateLabel.getText()));
    }
    
    @FXML private TableColumn col1, col2, col3, col4, col5, col6;
    @FXML private TableView tableView;
    @FXML private GridPane refreshGrid;
    @FXML private Label dateLabel, totalSaleLabel, totalDebitLabel;
    @FXML private Button refresh, prevDateBtn, nextDateBtn, printB;
    private ObservableList tableList;
    private Image processImage;
    private ImageView processImageView;
    private ResultSet rs;
    private DBAccess db;
    private Task getBSDetails;
    private long totalSale=0, totalDebit=0;
}
