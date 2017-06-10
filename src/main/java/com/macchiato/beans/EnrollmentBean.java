package com.macchiato.beans;

import java.io.Serializable;

/**
 * Created by li on 4/18/2017.
 */
public class EnrollmentBean implements Serializable {
    private String email;
    private String crsCode;

    public EnrollmentBean(String email, String crsCode){
        this.email = email;
        this.crsCode = crsCode;
    }

    public String getEmail() {return this.email;}

    public String getCrsCode() {return this.crsCode;}

    public void setEmail(String email) {this.email = email;}

    public void setCrsCode(String crsCode) {this.crsCode = crsCode;}

    public String generateJSON(){
        String outputString = "{\"email\":\"" + this.email + "\","
                + "\"crsCode\":\"" + this.crsCode + "\"";
        outputString += "}";

        return outputString;
    }
}
