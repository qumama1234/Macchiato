package com.macchiato.controllers.teachercontroller;

import com.google.appengine.api.users.User;
import com.macchiato.beans.StudentGradeBean;
import com.macchiato.utility.GenUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static com.macchiato.utility.TeachersUtils.StudentGradesListJson;
import static com.macchiato.utility.TeachersUtils.findAllStudentGradeBean;

/**
 * Created by Xiangbin on 5/17/2017.
 */
@Controller
public class LoadStudentGrade {
    /**
     * this function will get assignment key from front end and find all the question
     * and display the grade for each question
     * @param  request  The servlet container to get the data from front end
     * @param  response  The servlet container to sent to the data from front end
     */
    @RequestMapping(value = "/LoadStudentGrade.htm", method = RequestMethod.GET)
    public void LoadCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        User active_user = GenUtils.getActiveUser();
        String instructor_email = active_user.getEmail();
        String assignmentKey=request.getParameter("assignmentKey");
        if (instructor_email== null) {
            System.out.print("There is no active_user");
        }
        else{
            ArrayList<StudentGradeBean> newList= findAllStudentGradeBean(assignmentKey);
            System.out.println(StudentGradesListJson(newList));
            out.println(StudentGradesListJson(newList));
        }
    }

}
