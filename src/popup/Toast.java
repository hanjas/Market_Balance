/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package popup;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 *
 * @author Haxx
 */
public class Toast extends Alert {

    public Toast(String content) {
        super(AlertType.ERROR);
        setHeaderText(null);
        getDialogPane().setContentText(content);
    }
    
}
