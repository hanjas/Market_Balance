/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Haxx
 */
public class DayListTableModel {

    private StringProperty Name,Old,Today,Total,Debit,Balance;
    
    public DayListTableModel(String name,String old,String today,String total,String debit,String balance){
        this.Name = new SimpleStringProperty(name);
        this.Old = new SimpleStringProperty(old);
        this.Total = new SimpleStringProperty(total);
        this.Today = new SimpleStringProperty(today);
        this.Debit = new SimpleStringProperty(debit);
        this.Balance = new SimpleStringProperty(balance);
        
    }
    public String getName(){
        return Name.get();
    }
    public void setName(String name){
        Name.set(name);
    }
    
    public String getOld(){
        return Old.get();
    }
    public void setOld(String old){
        Old.set(old);
    }
    
    public String getTotal(){
        return Total.get();
    }
    public void setTotal(String total){
        Total.set(total);
    }
    
    public String getToday(){
        return Today.get();
    }
    public void setToday(String today){
        Today.set(today);
    }
    
    public String getDebit(){
        return Debit.get();
    }
    public void setDebit(String debit){
        Debit.set(debit);
    }
    
    public String getBalance(){
        return Balance.get();
    }
    public void setBalance(String balance){
        Balance.set(balance);
    }
}
