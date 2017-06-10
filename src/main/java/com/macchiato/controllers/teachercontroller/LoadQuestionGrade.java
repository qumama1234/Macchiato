package com.macchiato.controllers.teachercontroller;

import com.google.appengine.api.users.User;
import com.macchiato.beans.QuestionGradeBean;
import com.macchiato.utility.GenUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static com.macchiato.utility.TeachersUtils.QuestionGradesListJson;
import static com.macchiato.utility.TeachersUtils.findAllQuestionGradeBean;

/**
 * Created by Xiangbin on 5/17/2017.
 */
public class LoadQuestionGrade {
    /**
     * this function will get assignment key from front end and find all the question
     * and display the grade for each student
     * @param  request  The servlet container to get the data from front end
     * @param  response  The servlet container to sent to the data from front end
     */
    @RequestMapping(value = "/LoadQuestionGrade.htm", method = RequestMethod.GET)
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
            ArrayList<QuestionGradeBean> newList= findAllQuestionGradeBean(assignmentKey);
            System.out.println(QuestionGradesListJson(newList));
            out.println(QuestionGradesListJson(newList));
        }
    }


}
