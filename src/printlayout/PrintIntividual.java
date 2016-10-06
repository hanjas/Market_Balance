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
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import model.DayListTableModel;
import model.IndividualTableModel;

/**
 *
 * @author Haxx
 */
public class PrintIntividual extends BorderPane {

    public PrintIntividual(String name, ResultSet rs) throws SQLException {
        initialize(name);
        topBorder.setLeft(nameLabel);
//        topBorder.setCenter(dateLabel);
        topBorder.setRight(pageLabel);
        tableView.getColumns().addAll(dateColumn, oldBalColumn, todayColumn, totalColumn, debitColumn, balanceColumn);
        setPadding(new Insets(10));
        setTop(topBorder);
        setCenter(tableView);
        prefHeight(Paper.A4.getHeight());
        prefWidth(Paper.A4.getWidth());
        topBorder.setPrefHeight(30);
        setTableViewSize((Paper.A4.getWidth()-30)/6, Paper.A4.getHeight()-40);
        getStylesheets().add("/css/printstyle.css");
        print(rs);
    }
    private void initialize(String name){
        topBorder = new BorderPane();
        tableView = new TableView<DayListTableModel>();
        tableList = FXCollections.observableArrayList();
        nameLabel = new Label("EKD Shanavas - "+name);
        pageLabel = new Label("1");
        dateColumn = new TableColumn("Date");
        dateColumn.setCellValueFactory( new PropertyValueFactory("Date"));
        oldBalColumn = new TableColumn("OldBal");
        oldBalColumn.setCellValueFactory( new PropertyValueFactory("Old"));
        todayColumn = new TableColumn("Today");
        todayColumn.setCellValueFactory( new PropertyValueFactory("Today"));
        totalColumn = new TableColumn("Total");
        totalColumn.setCellValueFactory( new PropertyValueFactory("Total"));
        debitColumn = new TableColumn("Debit");
        debitColumn.setCellValueFactory( new PropertyValueFactory("Debit"));
        balanceColumn = new TableColumn("Balance");
        balanceColumn.setCellValueFactory( new PropertyValueFactory("Balance"));
    }
    private void setTableViewSize(double width, double height) {
        tableView.setPrefSize(width*6, height);
        dateColumn.setPrefWidth(width);
        oldBalColumn.setPrefWidth(width);
        todayColumn.setPrefWidth(width);
        totalColumn.setPrefWidth(width);
        debitColumn.setPrefWidth(width);
        balanceColumn.setPrefWidth(width);
    }
    private void print(ResultSet rs) throws SQLException {
        long totalSale=0, totalDebit=0, intex=1, pageNo=1;
        tableList.clear();
        while(rs.next()){
            if(intex % 33 == 0){
                tableView.setItems(tableList);
                pageLabel.setText(""+pageNo);
                Printer printer = Printer.getDefaultPrinter();
                PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
                PrinterJob job = PrinterJob.createPrinterJob();
                job.getJobSettings().setPageLayout(pageLayout);
                if(job != null){
                    boolean success = job.printPage(this);
                    if(success){
                        job.endJob();
                    }
                }
                tableList.clear();
                pageNo++;
                intex=1;
            }
            if(rs.getLong("today") != 0 || rs.getLong("debit") != 0 )
                tableList.add(new IndividualTableModel(
                    rs.getString("date"),
                    rs.getString("oldBal"),
                    rs.getString("today"),
                    rs.getString("total"),
                    rs.getString("debit"),
                    rs.getString("balance")
                ));
            totalSale = totalSale + rs.getLong("today");
            totalDebit = totalDebit + rs.getLong("debit");
            intex++;
        }
        tableList.add(new IndividualTableModel("", "", "", "", "", ""));
        tableList.add(new IndividualTableModel("", "TotalSale", ""+totalSale, "TotalDebit", ""+totalDebit, ""));
        tableView.setItems(tableList);
        pageLabel.setText(""+pageNo);
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
        PrinterJob job = PrinterJob.createPrinterJob();
        job.getJobSettings().setPageLayout(pageLayout);
        if(job != null){
            boolean success = job.printPage(this);
            if(success){
                job.endJob();
            }
        }
    }
    private BorderPane topBorder;
    private TableView tableView;
    private ObservableList tableList;
    private Label nameLabel, pageLabel;
    private TableColumn dateColumn, oldBalColumn, todayColumn, totalColumn, debitColumn, balanceColumn;
}
