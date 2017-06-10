package com.macchiato.general.generaldata;

import java.io.Serializable;

/**
 * CourseData.java
 * Purpose: The class represents data about a single course.
 * @author Karl Jean-Brice
 */
public class CourseData implements Serializable {
    private String instructor_email;
    private String instructor_nickname;
    private String course;
    private String section;
    private String course_code;
    private long course_id;



    public CourseData(String instructor_email, String instructor_nickname, String course, String section, long course_id) {
        this.instructor_email = instructor_email;
        this.instructor_nickname = instructor_nickname;
        this.course = course;
        this.section = section;
        this.course_id = course_id;
        this.course_code="";
    }


    public CourseData() {
        this("default", "default", "default","default",-1);
    }


    public String getCourseCode() {
        return course_code;
    }

    public void setCourseCode(String course_code) {
        this.course_code = course_code;
    }

    public long getCourseID() {
        return course_id;
    }

    public void setCourseID(long course_id) {
        this.course_id = course_id;
    }

    public String getSection() {

        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getInstructor_email() {
        return instructor_email;
    }

    public void setInstructor_email(String instructor_email) {
        this.instructor_email = instructor_email;
    }

    public String getInstructor_nickname() {
        return instructor_nickname;
    }

    public void setInstructor_nickname(String instructor_nickname) {
        this.instructor_nickname = instructor_nickname;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "CourseData{" +
                "instructor_email='" + instructor_email + '\'' +
                ", instructor_nickname='" + instructor_nickname + '\'' +
                ", course='" + course + '\'' +
                ", section='" + section + '\'' +
                ", course_code='" + course_code + '\'' +
                ", course_id=" + course_id +
                '}';
    }

    public String generateJSON() {

        String outputString = "{\"instructorEmail\":\"" + this.instructor_email + "\","
                + "\"instructorNickname\":\"" + this.instructor_nickname + "\","
                + "\"section\":\"" + this.section + "\","
                + "\"courseID\":\"" + this.course_id + "\","
                + "\"courseCode\":\"" + this.course_code + "\","
                + "\"course\":\"" + this.course + "\"";

        outputString += "}";

        return outputString;
    }
}

