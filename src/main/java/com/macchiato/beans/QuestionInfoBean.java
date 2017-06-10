package com.macchiato.beans;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Created by raymondx on 5/13/17.
 */

/**
 * This class will be used to give a point to each question and save the student answer
 */
public class QuestionInfoBean {

    private String point;
    private String email_address;
    private String question_key;
    private String assignment_key;
    private String studentanswer = "";
    private String complete;
    private String questionId;

    public QuestionInfoBean(String point, String email_address, String question_key, String assignment_key, String complete){
        this.point = point;
        this.email_address = email_address;
        this.question_key = question_key;
        this.assignment_key = assignment_key;
        this.complete = complete;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getQuestion_key() {
        return question_key;
    }

    public void setQuestion_key(String question_key) {
        this.question_key = question_key;
    }

    public String getAssignment_key() {
        return assignment_key;
    }

    public void setAssignment_key(String assignment_key) {
        this.assignment_key = assignment_key;
    }

    /**
     * Used to allow us to bring our object to the front end through ajax
     * It would generate a json object of a question
     * @return String
     */
    public String generateJSON() {

        String outputString = "{\"point\":\"" + point + "\","
                + "\"email_address\":\"" + email_address + "\","
                + "\"questionKey\":\"" + question_key + "\","
                + "\"complete\":\"" + complete + "\","
                + "\"questionId\":\"" + questionId + "\","
                + "\"assignmentKey\":\"" + assignment_key + "\"";
        outputString += "}";

        return outputString;
    }

    public String getStudentanswer() {
        return studentanswer;
    }

    public void setStudentanswer(String studentanswer) {
        this.studentanswer = studentanswer;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
