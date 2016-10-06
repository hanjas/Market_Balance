/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package popup;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;

/**
 *
 * @author BCz
 */
public class ConfirmPassword extends Alert {

    public ConfirmPassword() {
        super(AlertType.CONFIRMATION);
        diamension = Screen.getPrimary().getVisualBounds();
        mainPane = new GridPane();
        passLabel = new Label("Password");
        passField = new PasswordField();
        
        mainPane.add(passLabel, 0, 0);
        mainPane.add(passField, 1, 0);
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setHgap(10);
        
        setTitle("Confirm Delete");
        setHeaderText(null);
        getDialogPane().setContent(mainPane);
        getDialogPane().setPrefSize(400,250);
        focus();
    }
    private void focus() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                passField.requestFocus();
            }
        });
    }
    public boolean checkPassword() {
        if(passField.getText().toLowerCase().equals("asd123")) {
            return true;
        }
        return false;
    }
    
    private final Rectangle2D diamension;
    private Label passLabel;
    private PasswordField passField;
    private GridPane mainPane;
}
