/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moodle;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.HibernateUtil;



/**
 * FXML Controller class
 *
 * @author User1
 */
public class SignInController implements Initializable {
    
    @FXML
    private Button LoginButton;
    
    @FXML
    private TextField username;
    
    @FXML
    private TextField password;
    
    @FXML
    private Label error;
    
    private String token;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void login(ActionEvent event) throws MalformedURLException, IOException{
        String usrname = username.getText();
        String pssword = password.getText();
        System.out.println("Login button pressed\nUsername: "+ usrname +"\nPassword: "+pssword);
        URL url = new URL(Moodle.serverAdress+"moodle/login/token.php?username="+usrname+"&password="+pssword+"&service=moodle_mobile_app");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        try {
            InputStream in = new BufferedInputStream(con.getInputStream());
            InputStreamReader insr = new InputStreamReader(in, "UTF-8");
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(insr);
            System.out.println(jsonObject);
            this.token = jsonObject.get("token").toString();
            System.out.println("token: "+token);
            error.setText("");
            Moodle.username = usrname;
            Moodle.password = pssword;
            
            Parent root = FXMLLoader.load(getClass().getResource("Interface.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
          } catch( ParseException | NullPointerException | ConnectException e){
              con.disconnect();
              if("class java.net.ConnectException".equals(e.getClass().toString())){
                  Session session = HibernateUtil.getSessionFactory().openSession();
                  Transaction tx = null;
                  
                  try{
                    tx = session.beginTransaction();
                    Query query = session.createQuery("from User where username='"+usrname+"'");
                    
                    User result = (User)query.uniqueResult();
                    System.out.println(result);
                    session.close();
                    if(result.getPassword().equals(pssword)){
                        Moodle.username = usrname;
                        Moodle.password = pssword;

                        Parent root = FXMLLoader.load(getClass().getResource("Interface.fxml"));
                        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }
                    else{
                        error.setText("Verify password");
                    }
                  }catch(IOException ex){
                      error.setText("No Connexion and unengistered user");
                      session.getTransaction().rollback();
                  }
              }
              else{
                  if("class java.lang.NullPointerException".equals(e.getClass().toString())){
                      error.setText("Verify password and username");
                  }
                  else{
                      System.out.println(e.toString());
                  }
              }
          }
          finally {
            con.disconnect();
          }
    }
}
