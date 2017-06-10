package com.macchiato.beans;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by li on 5/14/2017.
 */
public class AssignmentListBean implements  Serializable{
    private ArrayList<AssignmentBean> assignments;

    public AssignmentListBean(ArrayList<AssignmentBean> assignments){
        this.assignments = assignments;
    }

    public ArrayList<AssignmentBean> getAssignments() {return assignments;}

    public void setAssignments(ArrayList<AssignmentBean> assignments) {this.assignments = assignments;}

    public String generateJSON(){
        String outputString = "{\"Assignments\":[";

        for(int i = 0; i < assignments.size() - 1; i++){
            outputString += assignments.get(i).generateEndJSON() + ",";
        }
        if (assignments.size() >0){
            outputString += assignments.get(assignments.size()-1).generateEndJSON();
        }
        outputString += "]}";
        return outputString;
    }
}
