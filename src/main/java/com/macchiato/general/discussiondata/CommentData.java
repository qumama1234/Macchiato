package com.macchiato.general.discussiondata;

import com.macchiato.general.generaldata.EnrollmentData;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.Serializable;

/**
 * CommentData.java
 * Purpose: The class represents data about a single comment.
 * @author Karl Jean-Brice
 */
public class CommentData implements Serializable {

    private long commentID;
    private EnrollmentData user;
    private String commentContent;
    private int commentLikes;
    private int editPermission = 0;
    private int deletePermission = 0;

    /**
     * @return the commentID
     */
    public CommentData() {

    }

    public CommentData(long commentID, EnrollmentData user, String commentContent, int commentLikes) {
        this.commentID = commentID;
        this.user = user;
        this.commentContent = commentContent;
        this.commentLikes = commentLikes;
    }

    public long getCommentID() {
        return commentID;
    }

    /**
     * @param commentID the commentID to set
     */
    public void setCommentID(long commentID) {
        this.commentID = commentID;
    }

    public EnrollmentData getUser() {
        return user;
    }

    public void setUser(EnrollmentData user) {
        this.user = user;
    }

    /**
     * @return the commentContent
     */
    public String getCommentContent() {
        return commentContent;
    }

    /**
     * @param commentContent the commentContent to set
     */
    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    /**
     * @return the commentLikes
     */
    public int getCommentLikes() {
        return commentLikes;
    }

    /**
     * @param commentLikes the commentLikes to set
     */
    public void setCommentLikes(int commentLikes) {
        this.commentLikes = commentLikes;
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
            return;
        }

        this.editPermission = 0;
        this.deletePermission = 0;

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
        String outputString = "{\"commentID\":" + commentID + ","
                + "\"commentContent\":\"" + StringEscapeUtils.escapeJava(commentContent) + "\",";

        outputString += "\"cUsername\":\"" + StringEscapeUtils.escapeJava(user.getUsername()) + "\","
                +"\"cEnrollmentID\":\"" + user.getEnrollmentID() + "\","
                +"\"cLikes\":\"" + commentLikes + "\","
                + "\"cEditPermission\":\"" + StringEscapeUtils.escapeJava(this.getEditPermission()) + "\","
                + "\"cDeletePermission\":\"" + StringEscapeUtils.escapeJava(this.getDeletePermission()) + "\","
                + "\"cAccess\":\"" + StringEscapeUtils.escapeJava(user.getAccess()) + "\"";

        outputString += "}";

        return outputString;
    }





}

