package com.macchiato.general.discussiondata;


import com.google.appengine.api.datastore.Key;
import com.macchiato.general.generaldata.EnrollmentData;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * DiscussionBoardData.java
 * Purpose: The class represents a forum that encapsulates comments and posts.
 * @author Karl Jean-Brice
 */
public class DiscussionBoardData implements Serializable {

    private int totalEnrollment;
    private int totalPosts;
    private int totalComments;
    private EnrollmentData user;
    private EnrollmentData currentUser;
    private ArrayList<PostData> postData;


    public DiscussionBoardData() {
        postData = new ArrayList<PostData>();
    }



    public DiscussionBoardData(Key groupID, int totalEnrollment, int totalPosts, int totalComments, EnrollmentData user, ArrayList<PostData> postData, EnrollmentData currentUser) {
        this.postData = postData;
        this.totalEnrollment = totalEnrollment;
        this.totalPosts = totalPosts;
        this.totalComments = totalComments;
        this.user = user;
        this.currentUser = currentUser;

    }


    public int getTotalEnrollment() {
        return totalEnrollment;
    }

    public void setTotalEnrollment(int totalEnrollment) {
        this.totalEnrollment = totalEnrollment;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public EnrollmentData getUser() {
        return user;
    }

    public void setUser(EnrollmentData user) {
        this.user = user;
    }

    public ArrayList<PostData> getPostData() {
        return postData;
    }

    public EnrollmentData getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(EnrollmentData currentUser) {
        this.currentUser = currentUser;
    }

    public void setPostData(ArrayList<PostData> postData) {
        this.postData = postData;
    }

    public String generateJSON() {
        String outputString = "{\"email\":\"" + StringEscapeUtils.escapeJava(user.getEmail()) + "\","
                + "\"course\":\"" + StringEscapeUtils.escapeJava(user.getCourse()) + "\","
                + "\"access\":\"" + StringEscapeUtils.escapeJava(user.getAccess()) + "\","
                + "\"totalComments\":\"" + totalComments + "\","
                + "\"totalPosts\":\"" + totalPosts + "\","
                + "\"totalEnrollment\":\"" + (totalEnrollment - 1) + "\","
                + "\"currentUsername\":\"" + StringEscapeUtils.escapeJava(currentUser.getUsername()) + "\","
                + "\"uset\":\"" + currentUser.getUset() + "\","
                + "\"username\":\"" + StringEscapeUtils.escapeJava(user.getUsername()) + "\","
                + "\"Posts\":[";

        if (postData.isEmpty()) {
            outputString = "{\"email\":\"" + StringEscapeUtils.escapeJava(user.getEmail()) + "\","
                    + "\"course\":\"" + StringEscapeUtils.escapeJava(user.getCourse()) + "\","
                    + "\"access\":\"" + StringEscapeUtils.escapeJava(user.getAccess()) + "\","
                    + "\"totalComments\":\"" + totalComments + "\","
                    + "\"totalPosts\":\"" + totalPosts + "\","
                    + "\"totalEnrollment\":\"" + (totalEnrollment - 1) + "\","
                    + "\"currentUsername\":\"" + StringEscapeUtils.escapeJava(currentUser.getUsername()) + "\","
                    + "\"uset\":\"" + currentUser.getUset() + "\","
                    + "\"username\":\"" + StringEscapeUtils.escapeJava(user.getUsername()) + "\"}";

            return outputString;
        }

        String test_post = "";
        for (int i = 0; i < postData.size(); i++) {
            if (i == postData.size() - 1) {
                test_post = postData.get(i).generateJSON() + "]";
                outputString += postData.get(i).generateJSON() + "]";
            } else {
                test_post = postData.get(i).generateJSON();
                outputString += postData.get(i).generateJSON() + ",";
            }
        }

        outputString += "}";

        return outputString;

    }
}
