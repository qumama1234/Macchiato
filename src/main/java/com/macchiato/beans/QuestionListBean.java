package com.macchiato.beans;

import javax.xml.crypto.Data;
import java.util.ArrayList;

/**
 * Created by Raymond on 4/7/2017.
 * Edited by Xiangbin Zeng
 * This class is made for assignment which help us the build each assignment, this assignment
 * list include all the information we need from a assignment
 */
public class QuestionListBean {
    private ArrayList<QuestionBean> problems;
    private String  CrsCode;
    private String  assignmentKey;
    private Data dueData;

    public QuestionListBean(){
        this.problems = new ArrayList<QuestionBean>();
    }

    public ArrayList<QuestionBean> getProblems() {
        return problems;
    }

    public void setProblems(ArrayList<QuestionBean> problems) {
        this.problems = problems;
    }

    // generate json object for js
    public String generateJSON() {
        String outputString = "{\"Questions\":[";

        if (problems.isEmpty()) {
            return outputString;
        }

        for (int i = 0; i < problems.size(); i++) {
            if (i == problems.size() - 1) {
                outputString += problems.get(i).generateJSON() + "]";
            } else {
                outputString += problems.get(i).generateJSON() + ",";
            }
        }

        outputString += "}";

        return outputString;
    }

    //getter and setter
    public String getCrsCode() {
        return CrsCode;
    }

    public void setCrsCode(String crsCode) {
        CrsCode = crsCode;
    }

    public String getAssignmentKey() {
        return assignmentKey;
    }

    public void setAssignmentKey(String assignmentKey) {
        this.assignmentKey = assignmentKey;
    }

    public Data getDueData() {
        return dueData;
    }

    public void setDueData(Data dueData) {
        this.dueData = dueData;
    }
}
