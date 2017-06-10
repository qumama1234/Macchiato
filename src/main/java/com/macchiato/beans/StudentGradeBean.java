package com.macchiato.beans;

/**
 * Created by Xiangbin on 5/17/2017.
 */
public class StudentGradeBean {
    private  String email;
    public double total=0;
    public double point=0;
    private String AssignmentKey;

    public StudentGradeBean(){
    }
    public StudentGradeBean(String email,int total,int point,String AssignmentKey){
        this.email=email;
        this.total=total;
        this.point=point;
        this.AssignmentKey=AssignmentKey;
    }


    //getter and setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public String getAssignmentKey() {
        return AssignmentKey;
    }

    public void setAssignmentKey(String assignmentKey) {
        AssignmentKey = assignmentKey;
    }


    /**
     * This function will calculator point , and return the points as string
     * @return String
     */
    public String pointcalculator(){
        return  Double.toString((this.point/this.total)*100)+"%";
    }

    /**
     * Used to allow us to bring our object to the front end through ajax
     * It would generate a json object of a question
     * @return String
     */
    public String generateJSON() {
        String outputString = "{\"point\":\"" + pointcalculator() + "\","
                + "\"email\":\"" + email + "\","
                + "\"AssignmentKey\":\"" + AssignmentKey + "\"";
        outputString += "}";

        return outputString;
    }


}
