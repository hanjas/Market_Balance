/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.CheckListView;

/**
 *
 * @author Haxx
 */
public class DeleteMember extends GridPane {

    public DeleteMember() {
        membersList = new CheckListView();
        selectAll = new CheckBox();
        
        add(selectAll, 0, 0);
        add(membersList, 0, 1);
        
        setVgap(10);
        setAlignment(Pos.CENTER);
        setAction();
    }
    
    public void setAction(){
        selectAll.selectedProperty().addListener(e->{
            if(isSelected) {
                membersList.getCheckModel().checkAll();
                membersList.setDisable(isSelected);
                isSelected = !isSelected;
            } else {
                membersList.getCheckModel().clearChecks();
                membersList.setDisable(isSelected);
                isSelected = !isSelected;
            }
        });
    }
    
    public void setListItems(ObservableList list){
        membersList.setItems(list);
    }
    public ObservableList getSelectedItems() {
        if(selectAll.isSelected())
            return membersList.getItems();
        return membersList.getCheckModel().getCheckedItems();
    }
    
//    Variable Decleration
    public final CheckListView membersList;
    private final CheckBox selectAll;
    private boolean isSelected = true;
}
