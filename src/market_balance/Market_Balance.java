/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package market_balance;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Haxx
 */
public class Market_Balance extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("EKD Shanavas");
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Scene myScene = new Scene(myPane,600,400);
            primaryStage.setScene(myScene);
            primaryStage.setMaximized(false);
            primaryStage.show();
            primaryStage.setMinHeight(450);
            primaryStage.setMinWidth(600);
        }catch (Exception e) { System.out.println(e); }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
