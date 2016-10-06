/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package popup;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import model.InsertDetails;

/**
 *
 * @author Haxx
 */
public class AddMember extends Alert {

    public AddMember(AlertType alertType) {
        super(alertType);
        memberDetails = new InsertDetails();
        diamension = Screen.getPrimary().getVisualBounds();

        setTitle("Add Members");
        setHeaderText(null);
        getDialogPane().setContent(memberDetails);
        getDialogPane().setPrefSize(diamension.getWidth() / 4, diamension.getHeight() / 3);
        getDialogPane().getButtonTypes().addAll(ButtonType.NEXT, ButtonType.OK, ButtonType.CANCEL);

    }
    
    public String getDate() { return memberDetails.getDate(); }
    public String getName() { return memberDetails.getName(); }
    public String getAmount() { return memberDetails.getAmount(); }
    public boolean isNotEmpty(){ return memberDetails.isTextEmpty(); }
    public boolean isCorrectDetails() {
        return memberDetails.isCorrectDetails();
    }
    public void showErrorToast() {
        Toast errorToast = new Toast("Member is not created. ' "+ getName() +" ' or ' "+ getAmount() +" ' contains special charectors like {.,/!+-><?}");
        errorToast.showAndWait();
    }
    public void setFocus() {
        memberDetails.setFocus();
    }

//    Variable Decleration
    private final InsertDetails memberDetails;
    private final Rectangle2D diamension;
}
