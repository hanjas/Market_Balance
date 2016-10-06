/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author BCz
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    @FXML private void login() {
        if(userNameField.getText().toLowerCase().equals("a") && passField.getText().toLowerCase().equals("a")) {
            Stage stage = (Stage) passField.getScene().getWindow();
            stage.close();
            Stage primaryStage = new Stage();
            primaryStage.setTitle("EKD Shanavas");
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
            Scene myScene = new Scene(myPane,900,400);
            primaryStage.setScene(myScene);
            primaryStage.setMaximized(true);
            primaryStage.show();
            primaryStage.setMinHeight(450);
            primaryStage.setMinWidth(1000);
            primaryStage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            System.exit(0);
                        }
                    });
                }
            });
        }catch (Exception e) { System.out.println(e); }
        } else {
            incorrectLabel.setVisible(true);
        }
    }
    @FXML private void tabKey() throws AWTException{
        robot.keyPress(KeyEvent.VK_TAB);
    }
    
    @FXML private TextField userNameField, passField;
    @FXML private Label incorrectLabel;
    private Robot robot;
}
