package com.macchiato.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by li on 4/21/2017.
 */
public class CourseListBean implements Serializable {
    private ArrayList<CourseBean> courses;

    public CourseListBean(ArrayList<CourseBean> courses){
        this.courses = courses;
    }
    // Get methods
    public ArrayList<CourseBean> getCourses() {return courses;}
    // Set methods
    public void setCourses(ArrayList<CourseBean> courses) {this.courses = courses;}
    // Generates a String in JSON format
    public String generateJSON(){
        String outputString = "{\"Courses\":[";

        for(int i = 0; i < courses.size() - 1; i++){
            outputString += courses.get(i).generateJSON() + ",";
        }
        if (courses.size() >0){
            outputString += courses.get(courses.size()-1).generateJSON();
        }
        outputString += "]}";
        return outputString;
    }
}
