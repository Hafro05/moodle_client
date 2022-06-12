package model;
// Generated 9 juin 2022 04:45:24 by Hibernate Tools 4.3.1


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * Course generated by hbm2java
 */
@Entity(name="Course")
public class Course  implements java.io.Serializable {

    
    @Id
    private int idcourse;
    private String name;
    private String shortname;
    private Integer lastaccess;
    private String summary;
    @ManyToMany(mappedBy = "courses")
    private Set<User> users = new HashSet(0);
    private Set<Assignment> assignments = new HashSet(0);

    public Course() {
    }

	
    public Course(int idcourse) {
        this.idcourse = idcourse;
    }
    public Course(int idcourse, String name, String shortname, Integer lastaccess, String summary, Set users, Set assignments) {
       this.idcourse = idcourse;
       this.name = name;
       this.shortname = shortname;
       this.lastaccess = lastaccess;
       this.summary = summary;
       this.users = users;
       this.assignments = assignments;
    }
   
    public int getIdcourse() {
        return this.idcourse;
    }
    
    public void setIdcourse(int idcourse) {
        this.idcourse = idcourse;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getShortname() {
        return this.shortname;
    }
    
    public void setShortname(String shortname) {
        this.shortname = shortname;
    }
    public Integer getLastaccess() {
        return this.lastaccess;
    }
    
    public void setLastaccess(Integer lastaccess) {
        this.lastaccess = lastaccess;
    }
    public String getSummary() {
        return this.summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public Set getUsers() {
        return this.users;
    }
    
    public void setUsers(Set users) {
        this.users = users;
    }
    public Set getAssignments() {
        return this.assignments;
    }
    
    public void setAssignments(Set assignments) {
        this.assignments = assignments;
    }
    
    @Override
    public String toString(){
        Long tmp = new Long(this.lastaccess) * 1000;
        Date date = new Date(tmp);
        DateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm:ss zzz");
        return "CourseId :"+Integer.toString(this.idcourse)+"\nShortname :"+this.shortname+"\nName :"+this.name+"\nLast access :"+(this.lastaccess == 0?"Never" : df.format(date));
    }

}


