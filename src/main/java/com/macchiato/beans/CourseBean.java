package com.macchiato.beans;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by li on 4/21/2017.
 * Edited by Xiangbin Zeng
 * This class is for the bean of course which help us the build each course,
 * it include all the information we needed from a course
 */
public class CourseBean implements Serializable {
    //name of the class
    private String name = "";
    //random class code for students to enroll
    private String course_code = "";
    //instr's Email
    private String email = "";
    //description of this class
    private String description = "";
    //valuable to help random generator
    private SecureRandom random = new SecureRandom();
    //this value is for different seciont of class
    private  String section = "";

    public CourseBean(String course_code, String crsName, String instrEmail, String description){
        this.course_code = course_code;
        this. name = crsName;
        this.email = instrEmail;
        this.description = description;
    }

    public CourseBean(){}

    public CourseBean( String crsName, String instrEmail, String description){
        this.course_code =  nextSessionId();
        this. name = crsName;
        this.email = instrEmail;
        this.description = description;
    }
    // Get methods
    public String getCrsCode() {return course_code;}
    public String getCrsName() {return  name;}
    public String getInstrEmail() {return email;}
    public String getDescription() {return description;}
    // Set methods
    public void setCrsCode(String crsCode) {this.course_code = crsCode;}
    public void setCrsName(String crsName) {this. name = crsName;}
    public void setInstrEmail(String instrEmail) {this.email = instrEmail;}
    public void setDescription(String description) {this.description = description;}

    /**
     * Used to allow us to bring our object to the front end through ajax
     * generate json object for js
     * @return Json
     */
    public String generateJSON(){
        return "{\"course_code\":\"" + course_code + "\","
                + "\"name\":\"" +  name + "\","
                + "\"email\":\"" + email + "\","
                + "\"section\":\"" + section + "\","
                + "\"description\":\"" + description + "\"}";
    }

    //random generator for crsCode
    public String nextSessionId() {
        return new BigInteger(25, random).toString(32);
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
