/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package popup;

import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import model.InsertDetails;

/**
 *
 * @author Haxx
 */
public class AddReport extends Alert {

    public AddReport(AlertType alertType, ObservableList list) {
        super(alertType);

        priceDetails = new InsertDetails();

        priceDetails.setList(list);

        diamension = Screen.getPrimary().getVisualBounds();
        setTitle("Add Reports");
        setHeaderText(null);
        getDialogPane().setContent(priceDetails);
        getDialogPane().setPrefSize(diamension.getWidth() / 4, diamension.getHeight() / 3);
        getDialogPane().getButtonTypes().addAll(ButtonType.NEXT, ButtonType.OK, ButtonType.CANCEL);
    }
    public String getDate(){
        return priceDetails.getDate();
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

    public final InsertDetails priceDetails;
    private final Rectangle2D diamension;
}
