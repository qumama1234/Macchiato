package com.macchiato.general.discussiondata;



import com.macchiato.general.generaldata.EnrollmentData;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * PostData.java
 * Purpose: The class represents data about a single post.
 * @author Karl Jean-Brice
 */


public class PostData implements Serializable {

    private long postID;
    private EnrollmentData user;
    private String postTitle;
    private String postContent;
    private int postLikes;
    private int editPermission = 0;
    private int deletePermission = 0;
    private ArrayList<CommentData> commentData;

    public PostData() {
        commentData = new ArrayList<CommentData>();
    }

    public PostData(long postID, EnrollmentData user, String postTitle, String postContent, int postLikes, ArrayList<CommentData> commentData) {
        this.postID = postID;
        this.user = user;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.commentData = commentData;
        this.postLikes = postLikes;
    }

    /**
     * Returns the postID associated with post.
     * @return the postID
     */
    public long getPostID() {
        return postID;
    }

    /**
     *
     * @param postID the postID to set
     */
    public void setPostID(long postID) {
        this.postID = postID;
    }

    /**
     * @return the user
     */
    public EnrollmentData getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(EnrollmentData user) {
        this.user = user;
    }

    /**
     * @return the postDate
     */
    public String getPostTitle() {
        return postTitle;
    }

    /**
     * @param postDate the postDate to set
     */
    public void setPostTitle(String postDate) {
        this.postTitle = postDate;
    }

    /**
     * @return the postContent
     */
    public String getPostContent() {
        return postContent;
    }

    /**
     * @param postContent the postContent to set
     */
    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    /**
     * @return the commentData
     */
    public ArrayList<CommentData> getCommentData() {
        return commentData;
    }

    /**
     * @param commentData the commentData to set
     */
    public void setCommentData(ArrayList<CommentData> commentData) {
        this.commentData = commentData;
    }

    /**
     * @return the postLikes
     */
    public int getPostLikes() {
        return postLikes;
    }

    /**
     * @param postLikes the postLikes to set
     */
    public void setPostLikes(int postLikes) {
        this.postLikes = postLikes;
    }

    public void determinePermissions(EnrollmentData activeUser){
        if(activeUser == null || user == null){
            this.editPermission = 0;
            this.deletePermission = 0;
            return;
        }

        if(activeUser.getAccess().equals("INSTRUCTOR") || activeUser.getAccess().equals("ADMIN")){
            this.deletePermission = 1;
            this.editPermission = 1;
            return;
        }

        if(activeUser.getEmail().equalsIgnoreCase(user.getEmail())){
            this.deletePermission = 0;
            this.editPermission = 1;
        }
        else{
            this.editPermission = 0;
            this.deletePermission = 0;
        }
    }

    public String getDeletePermission() {
        if(this.deletePermission == 1){
            return "delete";
        }
        else{
            return "no_delete";
        }
    }

    public String getEditPermission(){
        if(this.editPermission == 1){
            return "edit";
        }
        else{
            return "no_edit";
        }
    }

    public String generateJSON() {

        String outputString = "{\"postID\":" + postID + ","
                + "\"postContent\":\"" + StringEscapeUtils.escapeJava(postContent) + "\","
                + "\"postTitle\":\"" + StringEscapeUtils.escapeJava(postTitle) + "\","
                + "\"pEnrollmentID\":\"" + user.getEnrollmentID() + "\","
                + "\"pLikes\":\"" + postLikes + "\","
                + "\"pEditPermission\":\"" + StringEscapeUtils.escapeJava(this.getEditPermission()) + "\","
                + "\"pDeletePermission\":\"" + StringEscapeUtils.escapeJava(this.getDeletePermission()) + "\","
                + "\"pAccess\":\"" + user.getAccess() + "\","
                + "\"pCommentCount\":\"" + commentData.size() + "\","
                + "\"pUsername\":\"" + StringEscapeUtils.escapeJava(user.getUsername()) + "\","
                + "\"Comments\":[";

        if (commentData.isEmpty()) {
            outputString = "{\"postID\":" + postID + ","
                    + "\"postContent\":\"" + StringEscapeUtils.escapeJava(postContent) + "\","
                    + "\"postTitle\":\"" + StringEscapeUtils.escapeJava(postTitle) + "\","
                    + "\"pEnrollmentID\":\"" + user.getEnrollmentID() + "\","
                    + "\"pLikes\":\"" + postLikes + "\","
                    + "\"pEditPermission\":\"" + StringEscapeUtils.escapeJava(this.getEditPermission()) + "\","
                    + "\"pDeletePermission\":\"" + StringEscapeUtils.escapeJava(this.getDeletePermission()) + "\","
                    + "\"pAccess\":\"" + user.getAccess() + "\","
                    + "\"pCommentCount\":\"" + commentData.size() + "\","
                    + "\"pUsername\":\"" + StringEscapeUtils.escapeJava(user.getUsername()) + "\"}";
            return outputString;
        }

        for (int i = 0; i < commentData.size(); i++) {
            if (i == commentData.size() - 1) {
                outputString += commentData.get(i).generateJSON() + "]";
            } else {
                outputString += commentData.get(i).generateJSON() + ",";
            }
        }

        outputString += "}";

        return outputString;

    }


}
