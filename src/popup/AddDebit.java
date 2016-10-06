/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package popup;

import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import model.InsertDetails;

/**
 *
 * @author Haxx
 */
public class AddDebit extends Alert {

    public AddDebit(Alert.AlertType alertType, ObservableList list) {
        super(alertType);

        debitDetails = new InsertDetails();

        debitDetails.setList(list);

        diamension = Screen.getPrimary().getVisualBounds();
        setTitle("Add Debits");
        setHeaderText(null);
        getDialogPane().setContent(debitDetails);
        getDialogPane().setPrefSize(diamension.getWidth() / 4, diamension.getHeight() / 3);
        getDialogPane().getButtonTypes().addAll(ButtonType.NEXT, ButtonType.OK, ButtonType.CANCEL);
    }
    public String getDate(){
        return debitDetails.getDate();
    }
    public String getName(){
            return debitDetails.getName();
    }
    public String getAmount(){
            return debitDetails.getAmount();
    }
    public boolean isTextEmpty() {
            return debitDetails.isTextEmpty();
    }
    public void setFocus() {
             debitDetails.setFocus();
    }
    public boolean isCorrectText(ObservableList list) {
             return debitDetails.isCorrectText(list);
    }
    public void showErrorToast() {
        Toast errorToast = new Toast("Incorrect data. please try again");
        errorToast.showAndWait();
    }
    public final InsertDetails debitDetails;
    private final Rectangle2D diamension;
}
