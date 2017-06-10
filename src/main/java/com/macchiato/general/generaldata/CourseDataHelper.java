package com.macchiato.general.generaldata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * CourseDataHelper.java
 * Purpose: The helper class that encapsulates a Course object.
 * @author Karl Jean-Brice
 */
public class CourseDataHelper implements Serializable {
    private String current_user;
    private ArrayList<CourseData> courseList;

    public CourseDataHelper(String current_user){
        this.current_user = current_user;
        this.courseList = new ArrayList<>();
    }

    public CourseDataHelper(){
        this("default");
    }


    public String getCurrent_user() {
        return current_user;
    }

    public void addCourse(CourseData newFile){
        courseList.add(newFile);
    }
    public ArrayList<CourseData> getFileList() {
        return this.courseList;
    }

    public void setCourseList(ArrayList<CourseData> courseList) {
        this.courseList = courseList;
    }

    public void setCurrent_user(String current_user) {
        this.current_user = current_user;
    }



    public String generateJSON(){
        String outputString = "{\"currentUser\":\"" + this.current_user + "\","
                + "\"courseSize\":\"" + courseList.size() + "\","
                + "\"Courses\":[";

        if (courseList.isEmpty()) {
            outputString = "{\"currentUser\":\"" + current_user + "\","
            + "\"courseSize\":\"" + courseList.size() + "\"}";
            return outputString;
        }

        for (int i = 0; i < courseList.size(); i++) {
            if (i == courseList.size() - 1) {
                outputString += courseList.get(i).generateJSON() + "]";
            } else {
                outputString += courseList.get(i).generateJSON() + ",";
            }
        }

        outputString += "}";

        return outputString;
    }
}

