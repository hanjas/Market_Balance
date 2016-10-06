/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package popup;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.textfield.TextFields;

/**
 *
 * @author BCz
 */
public class DeleteDayList extends Alert{

    public DeleteDayList(ObservableList list) {
        super(AlertType.CONFIRMATION);
        mainBorder = new BorderPane();
        gridPane = new GridPane();
        nameLabel = new Label("Name");
        nameField = new TextField();
        checkBox = new CheckBox("delete all");
        
        gridPane.add(checkBox, 1, 0);
        gridPane.add(nameLabel, 0, 1);
        gridPane.add(nameField, 1, 1);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        
        mainBorder.setCenter(gridPane);
        mainBorder.setPadding(new Insets(10));
        
        setTitle("Delete Type");
        setHeaderText(null);
        getDialogPane().setContent(mainBorder);
        getDialogPane().setPrefSize(400,250);
        checkBoxAction();
        setList(list);
    }
    private void checkBoxAction() {
        checkBox.selectedProperty().addListener(e-> {
            if(isSelected) {
                nameLabel.setDisable(true);
                nameField.setDisable(true);
                isSelected = !isSelected;
            } else {
                nameLabel.setDisable(false);
                nameField.setDisable(false);
                isSelected = !isSelected;
            }
        });
    }
    public void setList(ObservableList list) {
        TextFields.bindAutoCompletion(nameField, list);
    }
    public String getName() {
        return nameField.getText();
    }
    public boolean nameIsNotEmpty() {
        if(nameField.getText().equals("")) {
            return false;
        }
        return true;
    }
    private final GridPane gridPane;
    private final BorderPane mainBorder;
    private final Label nameLabel;
    private final TextField nameField;
    private final CheckBox checkBox;
    public boolean isSelected = true;
}
