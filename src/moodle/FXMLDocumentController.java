/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moodle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Assignment;
import model.Course;
import model.User;
import org.apache.commons.text.WordUtils;
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
 *
 * @author TCHAZEU
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label username;
    
    @FXML
    private AnchorPane anchor;
    
    @FXML
    private ScrollPane pscroll;
    
    @FXML
    private Button LogoutButton;
    
    @FXML
    private Label title;
    
    private AnchorPane assanchor = new AnchorPane();
    
    @FXML
    private Pane cpane;
    
    @FXML
    private Button bcourse;
    
    @FXML
    private Button bassignment;
    
    @FXML
    private Button bcalendar;
    
    String[] colors = { "#92D16D", "#CF2718", "#007DFF", "#197b96","#fb6368"};
    String unclickedstyle = "-fx-background-color: #ED7A11";
    String clickedstyle = "-fx-background-color: \n" +
                        "        linear-gradient(#ffd65b, #e68400),\n" +
                        "        linear-gradient(#ffef84, #f2ba44),\n" +
                        "        linear-gradient(#ffea6a, #efaa22),\n" +
                        "        linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),\n" +
                        "        linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));\n" +
                        "    -fx-background-insets: 0,1,2,3,0;";
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {
            bcourse.setStyle(clickedstyle);
            bcourse.setOnMouseEntered(mouseEvent -> {
                bcourse.setStyle("-fx-background-color: #f2ba44;");
            });
            bcourse.setOnMouseExited(mouseEvent -> {
                bcourse.setStyle(clickedstyle);
            });
            bassignment.setOnMouseEntered(mouseEvent -> {
                bassignment.setStyle("-fx-background-color: #f2ba44;");
            });
            bassignment.setOnMouseExited(mouseEvent -> {
                bassignment.setStyle("-fx-background-color: #ed7a11;");
            });
            bcalendar.setOnMouseEntered(mouseEvent -> {
                bcalendar.setStyle("-fx-background-color: #f2ba44;");
            });
            bcalendar.setOnMouseExited(mouseEvent -> {
                bcalendar.setStyle("-fx-background-color: #ed7a11;");
            });
            bassignment.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event)->{
                if(Moodle.assignments.isEmpty()){
                    getAssignments();
                }
                for(int i=0;i<Moodle.assignments.size();i++){
                   assanchor.setPrefSize(307, 770);
                   Pane pane = getAssignmentPane(i);
                   AnchorPane.setTopAnchor(pane, 14.0);
                   AnchorPane.setLeftAnchor(pane, 23.0+ i*(230));
                   assanchor.getChildren().add(pane);
                }
                title.setText("User Assignments");
                pscroll.setContent(assanchor);
                bassignment.setStyle(clickedstyle);
                bassignment.setOnMouseExited(mouseEvent -> {
                   bassignment.setStyle(clickedstyle);
                });
                bcourse.setStyle(unclickedstyle);
                bcourse.setOnMouseEntered(mouseEvent -> {
                   bcourse.setStyle("-fx-background-color: #f2ba44;");
                });
                bcourse.setOnMouseExited(mouseEvent -> {
                    bcourse.setStyle("-fx-background-color: #ed7a11;");
                });
            });
            bcourse.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event)->{
                   String nam = "";
                   for(int i=0;i<Moodle.courses.size();i++){
                       nam = WordUtils.wrap(Moodle.courses.get(i).getName(), 22);  
                       Pane pane = getCoursePane(nam,Moodle.courses.get(i).getShortname(), i);
                       AnchorPane.setTopAnchor(pane, 14.0);
                       AnchorPane.setLeftAnchor(pane, 23.0+ i*(230));
                       anchor.getChildren().add(pane);
                   }
                   title.setText("User Courses");
                   pscroll.setContent(anchor);
                   bcourse.setStyle(clickedstyle);
                   bcourse.setOnMouseExited(mouseEvent -> {
                       bcourse.setStyle(clickedstyle);
                   });
                   bassignment.setStyle(unclickedstyle);
                   bassignment.setOnMouseEntered(mouseEvent -> {
                       bassignment.setStyle("-fx-background-color: #f2ba44;");
                   });
                   bassignment.setOnMouseExited(mouseEvent -> {
                        bassignment.setStyle("-fx-background-color: #ed7a11;");
                   });
            });
            URL url1 = new URL(Moodle.serverAdress+"moodle/webservice/rest/server.php?wstoken="+Moodle.adminToken+"&wsfunction=core_user_get_users_by_field&field=username&values[0]="+Moodle.curuser.getUsername()+"&moodlewsrestformat=json");
            HttpURLConnection con = (HttpURLConnection) url1.openConnection();
            con.setRequestMethod("POST");
            InputStream in = new BufferedInputStream(con.getInputStream());
            InputStreamReader insr = new InputStreamReader(in, "UTF-8");
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonarray = (JSONArray)jsonParser.parse(insr);
            JSONObject jsonObject = (JSONObject)jsonarray.get(0);
            System.out.println(jsonObject);
            username.setText(jsonObject.get("fullname").toString());
            Moodle.curuser.setUserid(Integer.parseInt(jsonObject.get("id").toString()));
            Moodle.curuser.setFullname(jsonObject.get("fullname").toString());
            //Moodle.userId = Integer.parseInt(jsonObject.get("id").toString());
            /*BufferedReader br = new BufferedReader(insr);
            String read;
            while ((read=br.readLine()) != null) {
                System.out.println(read);
            }*/
            
            Transaction tx = null;
            Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            User cuser = new User();
            cuser.setUserid(Moodle.curuser.getUserid());
            cuser.setUsername(Moodle.curuser.getUsername());
            cuser.setFullname(Moodle.curuser.getFullname());
            cuser.setPassword(Moodle.curuser.getPassword());
            System.out.println(cuser);
            session.saveOrUpdate(cuser);
            tx.commit();
            session.close();
            
            URL url2 = new URL(Moodle.serverAdress+"moodle/webservice/rest/server.php?wstoken="+Moodle.adminToken+"&wsfunction=core_enrol_get_users_courses&userid="+Moodle.curuser.getUserid()+"&moodlewsrestformat=json");
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
            
            anchor.getChildren().remove(cpane);
            String nam = "";
            for(int i=0;i<Moodle.courses.size();i++){
                nam = WordUtils.wrap(Moodle.courses.get(i).getName(), 22);  
                Pane pane = getCoursePane(nam,Moodle.courses.get(i).getShortname(), i);
                AnchorPane.setTopAnchor(pane, 14.0);
                AnchorPane.setLeftAnchor(pane, 23.0+ i*(230));
                anchor.getChildren().add(pane);
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
    
    private Pane getCoursePane(String fullname, String shortname, int i){
        
        Pane pane = new Pane();
        Pane p1 = new Pane();
        Button b1 = new Button("Download Resources");
        Label l1 = new Label(shortname);
        Label l2 = new Label(fullname);
        pane.setPrefSize(200,231);
        p1.setPrefSize(200,126);
        b1.setPrefSize(142,25);
        pane.setStyle("-fx-background-color: "+colors[i%colors.length]+";");
        p1.setStyle("-fx-background-color: white;");
        b1.setLayoutX(29);
        b1.setLayoutY(87);
        b1.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            try {
                String tmp = String.valueOf(Moodle.courses.get(i).getIdcourse());
                String tmp1 = Moodle.courses.get(i).getShortname();
                URL url3 = new URL(Moodle.serverAdress+"moodle/webservice/rest/server.php?wstoken="+Moodle.adminToken+"&wsfunction=core_course_get_contents&courseid="+tmp+"&moodlewsrestformat=json");
                HttpURLConnection con3 = (HttpURLConnection) url3.openConnection();
                con3.setRequestMethod("GET");
                InputStream in3 = new BufferedInputStream(con3.getInputStream());
                InputStreamReader insr3 = new InputStreamReader(in3, "UTF-8");
                JSONParser jsonParser3 = new JSONParser();
                JSONArray jsonarray2 = (JSONArray)jsonParser3.parse(insr3);
                for(int j=0;j< jsonarray2.size();j++){
                    JSONObject jsonObject1 = (JSONObject) jsonarray2.get(j);
                    JSONArray jsonarray3 = (JSONArray)jsonParser3.parse(jsonObject1.get("modules").toString());
                    for(int k=0;k<jsonarray3.size();k++){
                        JSONObject jsonObject2 = (JSONObject) jsonParser3.parse(jsonarray3.get(k).toString());
                        JSONArray dates = (JSONArray)jsonParser3.parse(jsonObject2.get("dates").toString());
                        if(jsonObject2.containsKey("contents") && dates.isEmpty()){
                            JSONArray jsonarray4 = (JSONArray)jsonParser3.parse(jsonObject2.get("contents").toString());
                            JSONObject jsonObject3 = (JSONObject) jsonParser3.parse(jsonarray4.get(0).toString());
                            System.out.println(jsonObject3.get("fileurl")+"\n"+jsonObject3.get("filename"));
                            URL url4 = new URL(jsonObject3.get("fileurl").toString()+"&token="+Moodle.adminToken);
                            HttpURLConnection con4 = (HttpURLConnection) url4.openConnection();
                            con4.setRequestMethod("GET");
                            InputStream in4 = new BufferedInputStream(con4.getInputStream());
                            BufferedInputStream bis = new BufferedInputStream(in4);
                            Path path = Paths.get("C:/Users/"+System.getProperty("user.name")+"/Desktop/MoodleClient/"+Moodle.curuser.getFullname()+"/"+tmp1);
                            Path createDirectories = Files.createDirectories(path);
                            File file = new File(createDirectories.toString()+"/"+jsonObject3.get("filename").toString());
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            try (FileOutputStream fos = new FileOutputStream(file)) {
                                byte[] data = new byte[1024];
                                int count;
                                while ((count = bis.read(data, 0, 1024)) != -1) {
                                    fos.write(data, 0, count);
                                }
                                fos.flush();
                                fos.close();
                            }
                        }
                        else{
                            if(jsonObject2.containsKey("contents")){
                                Transaction tx = null;
                                Session session = HibernateUtil.getSessionFactory().openSession();
                                tx = session.beginTransaction();
                                Assignment ass = new Assignment();
                                ass.setIdassignment(Integer.parseInt(jsonObject2.get("id").toString()));
                                ass.setName(jsonObject2.get("name").toString());
                                JSONObject jsonObject3 = (JSONObject)jsonParser3.parse(dates.get(0).toString());
                                ass.setStartdate(Integer.parseInt(jsonObject2.get("timestamp").toString()));
                                jsonObject3 = (JSONObject)jsonParser3.parse(dates.get(1).toString());
                                ass.setDuedate(Integer.parseInt(jsonObject2.get("timestamp").toString()));
                                ass.getCourses().add(Moodle.courses.get(i));
                                System.out.println(ass);
                                session.saveOrUpdate(ass);
                                tx.commit();
                                session.close();
                            }
                        }
                    }
                }
            }catch (MalformedURLException ex) {
                Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IOException ex) {
                Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
            }catch (ParseException ex) {
                Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        l1.setLayoutX(23);
        l1.setLayoutY(14);
        l2.setLayoutX(22);
        l2.setLayoutY(26);
        l1.setFont(new Font("Arial Bold", 15));
        l1.setTextFill(Color.web("#197b96"));
        l2.setFont(new Font("Arial Bold", 15));
        l2.setTextFill(Color.WHITE);
        p1.setLayoutY(102);
        p1.getChildren().addAll(b1,l1);
        pane.getChildren().addAll(p1,l2);
        
        return pane;
    }
    
    private void getAssignments(){
        for(int i=0; i<Moodle.courses.size();i++){
            try {
                Transaction tx = null;
                Session session = HibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();
                String tmp = String.valueOf(Moodle.courses.get(i).getIdcourse());
                String tmp1 = Moodle.courses.get(i).getShortname();
                URL url3 = new URL(Moodle.serverAdress+"moodle/webservice/rest/server.php?wstoken="+Moodle.token+"&wsfunction=mod_assign_get_assignments&courseids[0]="+tmp+"&moodlewsrestformat=json");
                HttpURLConnection con3 = (HttpURLConnection) url3.openConnection();
                con3.setRequestMethod("GET");
                InputStream in3 = new BufferedInputStream(con3.getInputStream());
                InputStreamReader insr3 = new InputStreamReader(in3, "UTF-8");
                JSONParser jsonParser3 = new JSONParser();
                JSONObject jsonobj = (JSONObject)jsonParser3.parse(insr3);
                JSONArray jsonarray2 = (JSONArray)jsonParser3.parse(jsonobj.get("courses").toString());
                for(int j=0;j< jsonarray2.size();j++){
                    JSONObject jsonObject1 = (JSONObject) jsonarray2.get(j);
                    JSONArray jsonarray3 = (JSONArray)jsonParser3.parse(jsonObject1.get("assignments").toString());
                    for(int k=0;k<jsonarray3.size();k++){
                        JSONObject jsonObject2 = (JSONObject) jsonParser3.parse(jsonarray3.get(k).toString());
                        Assignment ass = new Assignment();
                        ass.setIdassignment(Integer.parseInt(jsonObject2.get("id").toString()));
                        ass.setName(jsonObject2.get("name").toString());
                        ass.setStartdate(Integer.parseInt(jsonObject2.get("allowsubmissionsfromdate").toString()));
                        ass.setDuedate(Integer.parseInt(jsonObject2.get("duedate").toString()));
                        JSONArray files = (JSONArray)jsonParser3.parse(jsonObject2.get("introattachments").toString());
                        if(!files.isEmpty()){
                            JSONObject jsonObject3 = (JSONObject) jsonParser3.parse(files.get(0).toString());
                            ass.setFilename(jsonObject3.get("filename").toString());
                            ass.setLink(jsonObject3.get("fileurl").toString());
                        }
                        ass.getCourses().add(Moodle.courses.get(i));
                        Moodle.courses.get(i).getAssignments().add(ass);
                        System.out.println(ass);
                        Moodle.assignments.add(ass);
                        session.saveOrUpdate(ass);  
                    }
                }
                session.saveOrUpdate(Moodle.courses.get(i));
                tx.commit();
                session.close(); 
            }catch (MalformedURLException ex) {
                Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IOException ex) {
                Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
            }catch (ParseException ex) {
                Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private Pane getAssignmentPane(int i){
        
        Pane pane = new Pane();
        Pane p1 = new Pane();
        Button b1 = new Button("Download Files");
        Button b2 = new Button("Submit");
        Label l1 = new Label(Moodle.assignments.get(i).getName());
        Label l2 = new Label();
        Long tmp = new Long(Moodle.assignments.get(i).getDuedate()) * 1000;
        Date date = new Date(tmp);
        /*Long tmp1 = new Long(Moodle.assignments.get(i).getStartdate()) * 1000;
        Date date1 = new Date(tmp1);*/
        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        Label l3 = new Label("Due Date: "+df.format(date));
        for (Iterator it = Moodle.assignments.get(i).getCourses().iterator(); it.hasNext();) {
            Course c = (Course) it.next();
            l2.setText(c.getShortname());
        }
        pane.setPrefSize(200,231);
        p1.setPrefSize(200,126);
        b1.setPrefSize(142,25);
        b2.setPrefSize(142,25);
        pane.setStyle("-fx-background-color: "+colors[(i+2)%colors.length]+";");
        p1.setStyle("-fx-background-color: white;");
        b1.setLayoutX(29);
        b1.setLayoutY(90);
        b1.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            try {
                URL url4 = new URL(Moodle.assignments.get(i).getLink()+"?token="+Moodle.token);
                HttpURLConnection con4 = (HttpURLConnection) url4.openConnection();
                con4.setRequestMethod("GET");
                InputStream in4 = new BufferedInputStream(con4.getInputStream());
                BufferedInputStream bis = new BufferedInputStream(in4);
                Path path = Paths.get("C:/Users/"+System.getProperty("user.name")+"/Desktop/MoodleClient/"+Moodle.curuser.getFullname()+"/"+l2.getText()+"/Assignments");
                Path createDirectories = Files.createDirectories(path);
                File file = new File(createDirectories.toString()+"/"+Moodle.assignments.get(i).getFilename());
                if (!file.exists()) {
                    file.createNewFile();
                }
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] data = new byte[1024];
                    int count;
                    while ((count = bis.read(data, 0, 1024)) != -1) {
                        fos.write(data, 0, count);
                    }
                    fos.flush();
                    fos.close();
                }
            }catch (MalformedURLException ex) {
                Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IOException ex) {
                Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        b2.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a file");
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                JSONObject item = sendPOSTRequest(Moodle.serverAdress+"moodle/webservice/upload.php?moodlewsrestformat=json&token="+Moodle.token,file.toString() ,file.getName());
                System.out.println(file.toString());
                try {
                    URL url = new URL (Moodle.serverAdress+"moodle/webservice/rest/server.php?wstoken="+Moodle.token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_submission&plugindata[files_manager]="+item.get("itemid")+"assignmentid="+Moodle.assignments.get(i).getIdassignment());
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "application/json");
                    con.setDoOutput(true);
                    System.out.println(con.getResponseCode());
                } catch (MalformedURLException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        b2.setLayoutX(29);
        b2.setLayoutY(60);
        l1.setLayoutX(23);
        l1.setLayoutY(14);
        l2.setLayoutX(22);
        l2.setLayoutY(26);
        l3.setLayoutX(23);
        l3.setLayoutY(31);
        l1.setFont(new Font("Arial Bold", 15));
        l1.setTextFill(Color.web("#197b96"));
        l2.setFont(new Font("Arial Bold", 15));
        l2.setTextFill(Color.WHITE);
        p1.setLayoutY(102);
        p1.getChildren().addAll(b1,b2,l1,l3);
        pane.getChildren().addAll(p1,l2);
        
        return pane;
    }
    
    @FXML
    private void logout(ActionEvent event) {
        try {
            Moodle.curuser = new User();
            Moodle.token = new String();
            Moodle.courses.clear();
            Moodle.assignments.clear();
            
            Parent root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public JSONObject sendPOSTRequest(String url, String attachmentFilePath, String outputFilePathName)
    {
        String charset = "UTF-8";
        File binaryFile = new File(attachmentFilePath);
        String boundary = "------------------------" + Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.
        int    responseCode = 0;

        try 
        {
            //Set POST general headers along with the boundary string (the seperator string of each part)
            URLConnection connection = new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream output = connection.getOutputStream();
            PrintWriter writer  = new PrintWriter(new OutputStreamWriter(output, charset), true);

            // Send binary file - part
            // Part header
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: application/octet-stream").append(CRLF);// + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
            writer.append(CRLF).flush();

            // File data
            Files.copy(binaryFile.toPath(), output);
            output.flush(); 

            // End of multipart/form-data.
            writer.append(CRLF).append("--" + boundary + "--").flush();

            responseCode = ((HttpURLConnection) connection).getResponseCode();


            if(responseCode !=200) //We operate only on HTTP code 200
                return null;

            InputStream Instream = ((HttpURLConnection) connection).getInputStream();

            // Write PDF file 
            BufferedInputStream BISin = new BufferedInputStream(Instream);
            FileOutputStream FOSfile  = new FileOutputStream(outputFilePathName);
            BufferedOutputStream out  = new BufferedOutputStream(FOSfile);
            InputStreamReader insr3 = new InputStreamReader(BISin, "UTF-8");
            JSONParser jsonParser3 = new JSONParser();
            JSONArray jsonarray2 = (JSONArray)jsonParser3.parse(insr3);
            System.out.println(jsonarray2);

            int i;
            while ((i = BISin.read()) != -1) {
                out.write(i);
            }

            // Cleanup
            out.flush();
            out.close();
            
            JSONObject jo = (JSONObject)jsonarray2.get(0);
            return jo;
            } catch (IOException ex) {
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
