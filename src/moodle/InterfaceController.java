/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moodle;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import model.Course;
import model.User;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.HibernateUtil;

/**
 * FXML Controller class
 *
 * @author User1
 */
public class InterfaceController implements Initializable {
    
    @FXML
    private Label username;
    
    @FXML
    private GridPane grid;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {
            URL url1 = new URL(Moodle.serverAdress+"moodle/webservice/rest/server.php?wstoken="+Moodle.adminToken+"&wsfunction=core_user_get_users_by_field&field=username&values[0]="+Moodle.username+"&moodlewsrestformat=json");
            HttpURLConnection con = (HttpURLConnection) url1.openConnection();
            con.setRequestMethod("POST");
            InputStream in = new BufferedInputStream(con.getInputStream());
            InputStreamReader insr = new InputStreamReader(in, "UTF-8");
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonarray = (JSONArray)jsonParser.parse(insr);
            JSONObject jsonObject = (JSONObject)jsonarray.get(0);
            System.out.println(jsonObject);
            username.setText(jsonObject.get("fullname").toString());
            Moodle.userId = Integer.parseInt(jsonObject.get("id").toString());
            /*BufferedReader br = new BufferedReader(insr);
            String read;
            while ((read=br.readLine()) != null) {
                System.out.println(read);
            }*/
            
            Transaction tx = null;
            Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            User cuser = new User();
            cuser.setUserid(Moodle.userId);
            cuser.setUsername(jsonObject.get("username").toString());
            cuser.setFullname(jsonObject.get("fullname").toString());
            cuser.setPassword(Moodle.password);
            System.out.println(cuser);
            session.saveOrUpdate(cuser);
            tx.commit();
            session.close();
            
            URL url2 = new URL(Moodle.serverAdress+"moodle/webservice/rest/server.php?wstoken="+Moodle.adminToken+"&wsfunction=core_enrol_get_users_courses&userid="+Moodle.userId+"&moodlewsrestformat=json");
            HttpURLConnection con1 = (HttpURLConnection) url2.openConnection();
            con1.setRequestMethod("POST");
            InputStream in1 = new BufferedInputStream(con1.getInputStream());
            InputStreamReader insr1 = new InputStreamReader(in1, "UTF-8");
            JSONParser jsonParser1 = new JSONParser();
            JSONArray jsonarray1 = (JSONArray)jsonParser1.parse(insr1);
            System.out.println(jsonarray1);
            
            Transaction tx2 = null;
            Session session2 = HibernateUtil.getSessionFactory().openSession();
            tx2 = session2.beginTransaction();
            for (int i = 0; i < jsonarray1.size(); i++) {
                JSONObject jsonobj = (JSONObject) jsonarray1.get(i);
                Course course = new Course();
                course.setIdcourse(((Long)jsonobj.get("id")).intValue());
                course.setName((String) jsonobj.get("fullname"));
                course.setShortname((String) jsonobj.get("shortname"));
                course.setSummary((String) jsonobj.get("summary"));
                course.setLastaccess((jsonobj.get("lastaccess") == null) ? 0 : ((Long)jsonobj.get("lastaccess")).intValue());
                cuser.getCourses().add(course);
                Moodle.courses.add(course);
                System.out.println(course);
                session2.saveOrUpdate(course);
            }
            session2.saveOrUpdate(cuser);
            tx2.commit();
            session2.close();
            
            Button[] db = new Button[Moodle.courses.size()];
            for(int i=0;i<Moodle.courses.size();i++){
                Text text = new Text(Moodle.courses.get(i).getName()+"\n");
                text.setFont(Font.font("Arial", FontPosture.REGULAR, 25));
                grid.add(text, 0, i);
                db[i] = new Button();
                db[i].setText("Download Resources");
                String tmp = String.valueOf(Moodle.courses.get(i).getIdcourse());
                String tmp1 = Moodle.courses.get(i).getShortname();
                //downloadbut.
                db[i].addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
                    try {
                        URL url3 = new URL(Moodle.serverAdress+"moodle/webservice/rest/server.php?wstoken="+Moodle.adminToken+"&wsfunction=core_course_get_contents&courseid="+tmp+"&moodlewsrestformat=json");
                        HttpURLConnection con3 = (HttpURLConnection) url3.openConnection();
                        con3.setRequestMethod("GET");
                        InputStream in3 = new BufferedInputStream(con3.getInputStream());
                        InputStreamReader insr3 = new InputStreamReader(in3, "UTF-8");
                        JSONParser jsonParser3 = new JSONParser();
                        JSONArray jsonarray2 = (JSONArray)jsonParser3.parse(insr3);
                        JSONObject jsonObject1 = (JSONObject) jsonarray2.get(1);
                        JSONArray jsonarray3 = (JSONArray)jsonParser3.parse(jsonObject1.get("modules").toString());
                        JSONObject jsonObject2 = (JSONObject) jsonParser3.parse(jsonarray3.get(1).toString());
                        JSONArray jsonarray4 = (JSONArray)jsonParser3.parse(jsonObject2.get("contents").toString());
                        JSONObject jsonObject3 = (JSONObject) jsonParser3.parse(jsonarray4.get(0).toString());
                        System.out.println(jsonObject3.get("fileurl")+"\n"+jsonObject3.get("filename"));
                        /*URL url4 = new URL(jsonObject3.get("fileurl").toString()+"&token="+Moodle.adminToken);
                        HttpURLConnection con4 = (HttpURLConnection) url4.openConnection();
                        con4.setRequestMethod("GET");
                        InputStream in4 = new BufferedInputStream(con4.getInputStream());
                        BufferedInputStream bis = new BufferedInputStream(in4);
                        Path path = Paths.get("C:/Users/"+System.getProperty("user.name")+"/Desktop/MoodleClient/"+tmp1);
                        Path createDirectories = Files.createDirectories(path);
                        File file = new File(createDirectories.toString()+"/"+jsonObject3.get("filename").toString());
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(file);
                        
                        byte[] data = new byte[1024];
                        int count;
                        while ((count = bis.read(data, 0, 1024)) != -1) {
                            fos.write(data, 0, count);
                        }
                        fos.flush();
                        fos.close();*/
                    }catch (MalformedURLException ex) {
                        Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
                    }catch (IOException ex) {
                        Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
                    }catch (ParseException ex) {
                        Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                
                grid.add(db[i], 1, i);
            }
            
        } catch (HibernateException ex){
            ex.printStackTrace();
        } catch (ConnectException ex){
            Transaction tx3 = null;
            Session session3 = HibernateUtil.getSessionFactory().openSession();
            tx3 = session3.beginTransaction();
            Query query = session3.createQuery("from User");
            User result = (User)query.uniqueResult();
            System.out.println(result);
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            System.out.println("1");
            Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("2");
            Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            System.out.println("3");
            Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void downloadresource(Course c){
        
    }
}
