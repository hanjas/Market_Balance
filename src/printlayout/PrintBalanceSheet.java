/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package printlayout;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import model.BalanceSheetTableModel;
import model.DayListTableModel;

/**
 *
 * @author Haxx
 */
public class PrintBalanceSheet extends BorderPane {

    public PrintBalanceSheet(String date) throws SQLException {
        initialize(date);
        totalBalanceLabel.setVisible(false);
        topBorder.setLeft(nameLabel);
        topBorder.setCenter(dateLabel);
        topBorder.setRight(pageLabel);
        tableView.getColumns().addAll(nameColumn, dateColumn, oldBalColumn, todayColumn, totalColumn);
        setPadding(new Insets(10));
        setTop(topBorder);
        setCenter(tableView);
        setBottom(totalBalanceLabel);
        prefHeight(Paper.A4.getHeight());
        prefWidth(Paper.A4.getWidth()-10);
        topBorder.setPrefHeight(30);
        setTableViewSize((Paper.A4.getWidth()-30)/6, Paper.A4.getHeight()-60);
        getStylesheets().add("/css/printstyle.css");
    }
    private void initialize(String date){
        topBorder = new BorderPane();
        tableView = new TableView<BalanceSheetTableModel>();
        totalBalanceLabel = new Label();
        nameLabel = new Label("EKD Shanavas BALANCE-SHEET");
        dateLabel = new Label(date);
        pageLabel = new Label("1");
        nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory( new PropertyValueFactory("Name"));
        dateColumn = new TableColumn("Date");
        dateColumn.setCellValueFactory( new PropertyValueFactory("Date"));
        oldBalColumn = new TableColumn("OldBal");
        oldBalColumn.setCellValueFactory( new PropertyValueFactory("Old"));
        todayColumn = new TableColumn("Today");
        todayColumn.setCellValueFactory( new PropertyValueFactory("Today"));
        totalColumn = new TableColumn("Total");
        totalColumn.setCellValueFactory( new PropertyValueFactory("Total"));
    }
    private void setTableViewSize(double width, double height) {
        tableView.setPrefSize(width*6, height);
        nameColumn.setPrefWidth(width*2-5);
        dateColumn.setPrefWidth(width);
        oldBalColumn.setPrefWidth(width);
        todayColumn.setPrefWidth(width);
        totalColumn.setPrefWidth(width);
    }
    public void setList(ObservableList tableList, String pageNo) {
        tableView.setItems(tableList);
        pageLabel.setText(pageNo);
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
        PrinterJob job = PrinterJob.createPrinterJob();
        job.getJobSettings().setPageLayout(pageLayout);
        if(job != null){
            boolean success = job.printPage(this);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PrintBalanceSheet.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(success){
                job.endJob();
            }
        }
    }
    public void setTotal(String total) {
        totalBalanceLabel.setText("TotalBalance : "+total);
        totalBalanceLabel.setVisible(true);
    }
    private BorderPane topBorder;
    private TableView tableView;
    private Label nameLabel, dateLabel, pageLabel, totalBalanceLabel;
    private TableColumn nameColumn, dateColumn, oldBalColumn, todayColumn, totalColumn;
}
