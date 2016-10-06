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
import model.RenameMember;

/**
 *
 * @author Haxx
 */
public class RenamePopup extends Alert {

    public RenamePopup(String name, String amount, String date) {
        super(AlertType.NONE);

        priceDetails = new RenameMember(name, amount, date);
        
        diamension = Screen.getPrimary().getVisualBounds();
        setTitle("Rename Member");
        setHeaderText(null);
        getDialogPane().setContent(priceDetails);
        getDialogPane().setPrefSize(diamension.getWidth() / 4, diamension.getHeight() / 3);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    }
    public String getName(){
            return priceDetails.getName();
    }
    public String getAmount(){
            return priceDetails.getAmount();
    }
    public boolean isTextEmpty() {
            return priceDetails.isTextEmpty();
    }
    public void setFocus() {
             priceDetails.setFocus();
    }
    public boolean isCorrectText(ObservableList list) {
             return priceDetails.isCorrectText(list);
    }
    public void showErrorToast() {
        Toast errorToast = new Toast("Incorrect data. please try again");
        errorToast.showAndWait();
    }

    public final RenameMember priceDetails;
    private final Rectangle2D diamension;
}
