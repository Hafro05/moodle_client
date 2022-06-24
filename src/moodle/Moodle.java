/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moodle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Assignment;
import model.Course;
import model.User;

/**
 *
 * @author User1
 */
public class Moodle extends Application {
    
    public static String serverAdress = "http://localhost/";
    public static String adminToken = "e222a698a176dad293d7e79617a87904";
    public static User curuser = new User();
    public static String token = new String();
    public static int userId;
    public static List<Course> courses = new ArrayList();
    public static List<Assignment> assignments = new ArrayList();

    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("Moodle Client");
        Image image = new javafx.scene.image.Image(getClass().getResource("images/Mobile-M-Icon-2-corners.png").toExternalForm());
        stage.getIcons().add(image);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

