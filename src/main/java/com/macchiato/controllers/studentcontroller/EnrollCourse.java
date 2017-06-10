package com.macchiato.controllers.studentcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by li on 4/18/2017.
 */
@Controller
public class EnrollCourse {
    @RequestMapping(value = "EnrollCourse.htm", method = RequestMethod.GET)
    public void loadStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Take the information in the text boxes
        // Find the course that has this Code.
        // Create a new EnrollmentBean and store it into the database.
    }
}
