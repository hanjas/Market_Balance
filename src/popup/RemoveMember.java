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
import model.DeleteMember;

/**
 *
 * @author Haxx
 */
public class RemoveMember extends Alert {

    public RemoveMember(AlertType alertType, ObservableList list) {
        super(alertType);
        
        deleteMember = new DeleteMember();
        diamension = Screen.getPrimary().getVisualBounds();
        removeBtn = new ButtonType("Remove");
        
        deleteMember.membersList.setPrefHeight(diamension.getHeight()/1.2-100);
        setTitle("Remove Members");
        setHeaderText(null);
        getDialogPane().setContent(deleteMember);
        getDialogPane().setPrefSize(diamension.getWidth() / 6, diamension.getHeight() / 1.2);
        getDialogPane().getButtonTypes().addAll(removeBtn, ButtonType.CANCEL);
        setListItems(list);
    }
    
    public void setListItems(ObservableList list) {
        deleteMember.setListItems(list);
    }
    public ObservableList getSelectedItems() {
        return deleteMember.getSelectedItems();
    }
    
//    Variable Decleration
    private final DeleteMember deleteMember;
    private final Rectangle2D diamension;
    public final ButtonType removeBtn;
}
