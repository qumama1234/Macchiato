package com.macchiato.controllers.teachercontroller;

import com.google.appengine.api.users.User;
import com.macchiato.beans.QuestionBean;
import com.macchiato.utility.GenUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static com.macchiato.utility.TeachersUtils.QuestionListJson;
import static com.macchiato.utility.TeachersUtils.findAllQuestionBean;

/**
 * Created by Xiangbin on 5/5/2017.
 */
@Controller
public class LoadQuestion {
    //Load course function will load all the course owned by this teacher, and it will listed on teacher home page
    /**
     * this function will get assignment key from front end and find all the question
     * and display the grade for each question
     * @param  request  The servlet container to get the data from front end
     * @param  response  The servlet container to sent to the data from front end
     */
    @RequestMapping(value = "/LoadQuestion.htm", method = RequestMethod.GET)
    public void LoadQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String assignmentKey=request.getParameter("assignmentKey");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        User active_user = GenUtils.getActiveUser();
        String instructor_email = active_user.getEmail();
        if (instructor_email== null) {
            System.out.print("There is no active_user");
        }
        else{
            System.out.println("instructor email: "+instructor_email);
            ArrayList<QuestionBean> newList= findAllQuestionBean(assignmentKey);

            System.out.println(QuestionListJson(newList));
            out.println(QuestionListJson(newList));
        }
    }
}
