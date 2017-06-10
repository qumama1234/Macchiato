package com.macchiato.beans;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Created by Raymond on 4/7/2017.
 * Edited by XiangbinZeng
 */

/**
 * This class will be used to store all the fields necessary for a question
 */
public class QuestionBean {
    private String problem;
    private String solution;
    private String id;
    private String assignmentKey;
    private String questionKey;
    private String studentanswer = "";
    private String teacherAnswer="";

    public QuestionBean(String problem, String solution, String id, String studentanswer){
        this.problem = problem;
        this.solution = solution;
        this.id = id;
        this.studentanswer = studentanswer;
    }

    public QuestionBean(){}

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionKey() {return questionKey;}

    public void setQuestionKey(String questionKey) { this.questionKey = questionKey; }

    public String getAssignmentKey() {
        return assignmentKey;
    }

    public void setAssignmentKey(String assignmentKey) {
        this.assignmentKey = assignmentKey;
    }

    public String getStudentanswer() {
        return studentanswer;
    }

    public void setStudentanswer(String studentanswer) {
        this.studentanswer = studentanswer;
    }

    public String getTeacherAnswer() {
        return teacherAnswer;
    }

    public void setTeacherAnswer(String teacherAnswer) {
        this.teacherAnswer = teacherAnswer;
    }

    /**
     * Used to allow us to bring our object to the front end through ajax
     * It would generate a json object of a question
     * @return String
     */
    public String generateJSON() {

        String outputString = "{\"problem\":\"" + StringEscapeUtils.escapeJava(problem) + "\","
                + "\"solution\":\"" + StringEscapeUtils.escapeJava(solution) + "\","
                + "\"questionKey\":\"" + questionKey + "\","
                + "\"studentanswer\":\"" + StringEscapeUtils.escapeJava(studentanswer) + "\","
                + "\"teacherAnswer\":\"" + StringEscapeUtils.escapeJava(teacherAnswer) + "\","
                + "\"assignmentKey\":\"" + assignmentKey + "\","
                + "\"id\":\"" + "Question " + id + "\"";
        outputString += "}";

        return outputString;
    }






}
