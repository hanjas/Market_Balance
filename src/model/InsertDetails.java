/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.TextFields;

/**
 *
 * @author Haxx
 */
public class InsertDetails extends GridPane {
    
    public InsertDetails() {
        
        dateLabel = new Label("Date");
        nameLabel = new Label("Name");
        amountLabel = new Label("Amount");
        
        datePicker = new DatePicker();
        nameField = new TextField();
        amountField = new TextField();
        
        add(dateLabel, 0, 0);
        add(datePicker, 1, 0);
        
        add(nameLabel, 0, 1);
        add(nameField, 1, 1);
        
        add(amountLabel, 0, 2);
        add(amountField, 1, 2);
        
        this.format = DateTimeFormatter.ofPattern("d-M-yyyy");
        pattern = "d-M-yyyy";
        
        setDate();
        setAlignment(Pos.CENTER);
        setHgap(20);
        setVgap(5);
        Platform.runLater(new Runnable() {
            @Override public void run() { nameField.requestFocus(); }
        });
    }
    
    public String getDate() { return datePicker.getValue().format(format); }
    public String getName() { return nameField.getText().toUpperCase(); }
    public String getAmount() { return amountField.getText(); }
    
    public boolean isTextEmpty() {
        return !( nameField.getText().equals("")
                ||amountField.getText().equals("") );
    }
    public void clearFields() {
        nameField.setText("");
        amountField.setText("");
    }
    
    public void setFocus() {
        amountField.setText("");
        Platform.runLater(new Runnable() {
            @Override public void run() { nameField.requestFocus(); }
        });
    }
    
    public void setAmountLabel(String text){
        amountLabel.setText(text);
    }
    public void setDate(){
        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override 
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override 
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        datePicker.setValue(LocalDate.now());
    }
    public void setList(ObservableList list) { TextFields.bindAutoCompletion(nameField, list); }
    public boolean isCorrectText(ObservableList list) {
        Pattern namePattern = Pattern.compile("[^a-z0-9A-Z_:/. ]", Pattern.CASE_INSENSITIVE);
        Pattern amountPattern = Pattern.compile("[^0-9]", Pattern.CASE_INSENSITIVE);
        Matcher nameMatcher = namePattern.matcher(getName());
        Matcher amountMatcher = amountPattern.matcher(getAmount());
        boolean nameMatch = nameMatcher.find(), amountMatch = amountMatcher.find();
        if(nameMatch || amountMatch)
            return false;
        if(!list.contains(getName()))
            return false;
        return true;
    }
    public boolean isCorrectDetails() {
        Pattern namePattern = Pattern.compile("[^a-z0-9A-Z_/. ]", Pattern.CASE_INSENSITIVE);
        Pattern amountPattern = Pattern.compile("[^0-9]", Pattern.CASE_INSENSITIVE);
        Matcher nameMatcher = namePattern.matcher(getName());
        Matcher amountMatcher = amountPattern.matcher(getAmount());
        boolean nameMatch = nameMatcher.find(), amountMatch = amountMatcher.find();
        if(nameMatch || amountMatch)
            return false;
        return true;
    }

    private Label dateLabel, nameLabel, amountLabel;
    private TextField nameField, amountField;
    private DatePicker datePicker;
    private final DateTimeFormatter format;
    private String pattern;
}
