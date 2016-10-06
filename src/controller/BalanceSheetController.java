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
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.swing.SwingUtilities;
import model.BalanceSheetTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author Haxx
 */
public class BalanceSheetController implements Initializable {

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
            allMembers = FXCollections.observableArrayList();
            db = new DBAccess();
            dateLabel.setText(format.format(new Date()));
            prevDateBtn.setText("<");
            refreshGrid.add(processImageView, 0, 0);
            setColumnSize((d.getWidth() - 265) / 6);
            setProcess();
            prevDateBtn.setVisible(false);
            nextDateBtn.setVisible(false);
        } catch (Exception ex) { System.out.println(ex); }
    }    
    private void setColumnSize(double width) {
        col1.setPrefWidth(width);
        col2.setPrefWidth(width);
        col3.setPrefWidth(width);
        col4.setPrefWidth(width);
        col5.setPrefWidth(width);
        col6.setPrefWidth(width);
        col7.setPrefWidth(width);
    }
    
    @FXML public void setProcess() throws SQLException{
        refresh.setDisable(true);
        tableView.setDisable(true);
        processImageView.setVisible(true);
        nextDateBtn.setDisable(true);
        prevDateBtn.setDisable(true);
//        printB.setDisable(true);
        totalBalanceLabel.setText("TotalBalance :");
        getBSDetails = createWorker();

        getBSDetails.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println(newValue);
            }
        });
        new Thread(getBSDetails).start();
    }
    public Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 1; i++) {
                    tableList.clear();
                    totalBalance = 0;
                    rs = db.getBalanceSheet(dateLabel.getText());
                    while(rs.next()){
                        if(rs.getLong("balance") != 0 )
                            tableList.add(new BalanceSheetTableModel(
                                rs.getString("name"),
                                rs.getString("date"),
                                rs.getString("oldBal"),
                                rs.getString("today"),
                                rs.getString("total"),
                                rs.getString("debit"),
                                rs.getString("balance")
                            ));
                        totalBalance = totalBalance + rs.getLong("balance");
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
                totalBalanceLabel.setText("TotalBalance : "+totalBalance);
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
        Stage viewAdvAndPercent = new Stage();
        viewAdvAndPercent.setTitle("Adv and Percent Cutting");
//        viewAdvAndPercent.getIcons().add(new Image("/image/aas.png"));
        try{
            HashMap param = new HashMap();
            param.put("date", dateLabel.getText());
            JasperPrint print = JasperFillManager.fillReport("src\\reports\\BalanceSheet.jasper", param, db.connection);
            JRViewer viewer = new JRViewer(print);
            SwingNode node = new SwingNode();
            node.setContent(viewer);
            BorderPane jasperViewer = new BorderPane();
            jasperViewer.setCenter(node);
            Scene scene = new Scene(jasperViewer);
            Stage jasperReport = new Stage();
            jasperReport.setScene(scene);
            jasperReport.initModality(Modality.NONE);
            jasperReport.initOwner( (Stage) tableView.getScene().getWindow() );
            jasperReport.setTitle("BalanceSheet");
            jasperReport.setHeight(700);
            jasperReport.setResizable(false);
            jasperReport.show();


//            JasperViewer jasperViewer = new JasperViewer(print, false);
//            jasperViewer.setTitle("BalanceSheet");
//            jasperViewer.setAlwaysOnTop(true);
//            jasperViewer.setLocation(400, 0);
//            jasperViewer.setResizable(false);
//            jasperViewer.setVisible(true);
            
        }catch (Exception e) { System.out.println(e); }
    }
    public Task printing() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 1; i++) {
                    
                }
                return true;
            }
        };
    }
    
    @FXML private TableColumn col1, col2, col3, col4, col5, col6, col7;
    @FXML private TableView tableView;
    @FXML private GridPane refreshGrid;
    @FXML private Label dateLabel, totalBalanceLabel;
    @FXML private Button refresh, prevDateBtn, nextDateBtn, printB;
    private ObservableList tableList, allMembers, printList;
    private Image processImage;
    private ImageView processImageView;
    private ResultSet rs;
    private DBAccess db;
    private Task getBSDetails;
    private long totalBalance=0;
}
