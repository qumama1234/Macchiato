package com.macchiato.beans;

/**
 * Created by Xiangbin on 5/17/2017.
 */
public class QuestionGradeBean {
    private  String id="";
    public double total=0;
    public double point=0;
    private String AssignmentKey="";
    private String QuestionKey="";

    public QuestionGradeBean(){}
    public QuestionGradeBean(String id,double total,double point,String AssignmentKey){
        this.id=id;
        this.total=total;
        this.point=point;
        this.AssignmentKey=AssignmentKey;
    }


    //getter and setter
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
        return  Double.toString((this.point/this.total));
    }

    /**
     * Used to allow us to bring our object to the front end through ajax
     * It would generate a json object of a question
     * @return String
     */
    public String generateJSON() {
        String outputString = "{\"point\":\"" + pointcalculator() + "\","
                + "\"id\":\"" + id + "\","
                + "\"AssignmentKey\":\"" + AssignmentKey + "\"";
        outputString += "}";

        return outputString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionKey() {
        return QuestionKey;
    }

    public void setQuestionKey(String questionKey) {
        QuestionKey = questionKey;
    }
}
