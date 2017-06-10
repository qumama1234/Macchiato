package com.macchiato.beans;

/**
 * Created by raymondx on 5/13/17.
 */

import java.util.ArrayList;

/**
 * This class is made for assignment which help us the build each assignment, this assignment
 * list info include all the information we need from a assignment
 */
public class QuestionInfoListBean {
    private ArrayList<QuestionInfoBean> questionInfo;
    private String totalgrade;
    private String assignmentKey;

    //getter and setter
    public QuestionInfoListBean() {
        questionInfo = new ArrayList<QuestionInfoBean>();
    }

    public ArrayList<QuestionInfoBean> getQuestionInfo() {
        return questionInfo;
    }

    public void setQuestionInfo(ArrayList<QuestionInfoBean> questionInfo) {
        this.questionInfo = questionInfo;
    }

    public String getTotalgrade() {
        return totalgrade;
    }

    public void setTotalgrade(String totalgrade) {
        this.totalgrade = totalgrade;
    }

    public String getAssignmentKey() {
        return assignmentKey;
    }

    public void setAssignmentKey(String assignmentKey) {
        this.assignmentKey = assignmentKey;
    }


    /**
     * Used to allow us to bring our object to the front end through ajax
     * generate json object for js
     * @return String
     */
    public String generateJSON() {
        String outputString = "{\"QuestionsInfo\":[";

        if (questionInfo.isEmpty()) {
            return outputString;
        }

        for (int i = 0; i < questionInfo.size(); i++) {
            if (i == questionInfo.size() - 1) {
                outputString += questionInfo.get(i).generateJSON() + "]";
            } else {
                outputString += questionInfo.get(i).generateJSON() + ",";
            }
        }

        outputString += "}";

        return outputString;
    }


}
