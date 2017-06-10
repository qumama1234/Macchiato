package com.macchiato.controllers.urlmappingcontroller;

import com.google.appengine.api.datastore.*;
import com.macchiato.beans.QuestionBean;
import com.macchiato.beans.QuestionListBean;
//import com.macchiato.beans.UserBean;
import com.macchiato.utility.DiscussionBoardUtils;
import com.macchiato.utility.GenUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.hackerrank.api.client.*;
import com.hackerrank.api.hackerrank.api.*;
import com.hackerrank.api.hackerrank.model.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


/**
 * Created by Karl on 4/5/2017.
 * Edited by Raymond Xue
 * Edited by Xiangbin Zeng
 */
@Controller
public class URLMapping {

    @RequestMapping(value = "Home.htm", method = RequestMethod.GET)
    public static ModelAndView loadHomePage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        ModelAndView model = new ModelAndView("Home");
        return model;
    }

    @RequestMapping(value = "Student.htm", method = RequestMethod.GET)
    public static ModelAndView loadStudentPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        ArrayList<Object> credentials = GenUtils.checkCredentials();
        int access = (int)credentials.get(0);
        if(access == GenUtils.STUDENT || access == GenUtils.ADMIN){
            ModelAndView model = new ModelAndView("Student");
            return model;
        }

        ModelAndView model = new ModelAndView("Home");
        return model;

    }



    @RequestMapping(value = "CourseInfo.htm", method = RequestMethod.GET)
    public static ModelAndView loadCourseInfoPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        ArrayList<Object> credentials = GenUtils.checkCredentials();
        int access = (int)credentials.get(0);
        if(access == GenUtils.STUDENT || access == GenUtils.ADMIN){
            ModelAndView model = new ModelAndView("CourseInfo");
            return model;
        }

        ModelAndView model = new ModelAndView("Home");
        return model;
    }

    @RequestMapping(value = "Discussionboard.htm", method = RequestMethod.POST)
    public ModelAndView loadDiscussionBoard(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        String instructor_email = request.getParameter("i_email");
        String course = request.getParameter("course");
        String section = request.getParameter("section");
        ModelAndView model;


        if (instructor_email == null || course == null || section == null ||course.trim().length() == 0 || section.trim().length() == 0 || instructor_email.trim().length() == 0) {
            model = new ModelAndView("error");
            return model;
        }

        Key forumKey = DiscussionBoardUtils.getForumKey(instructor_email.trim(), course.trim(),section.trim());
        if (forumKey == null) {
            model = new ModelAndView("error");
            return model;
        }

        User active_user = GenUtils.getActiveUser();
        if (active_user == null) {
            model = new ModelAndView("error");
            return model;
        }

        String email = active_user.getEmail();
        int enrollment_status = DiscussionBoardUtils.isEnrolled(forumKey, email);
        switch (enrollment_status) {
            case DiscussionBoardUtils.NOT_ENROLLED:
                model = new ModelAndView("error");
                return model;
            case DiscussionBoardUtils.DUPLICATE_ENTRY:
                model = new ModelAndView("error");
                return model;
            case DiscussionBoardUtils.ENROLLED:
                model = new ModelAndView("Discussionboard");
                model.addObject("i_email",instructor_email);
                model.addObject("course",course);
                model.addObject("section",section);
                return model;
            default:
                model = new ModelAndView("error");
                return model;
        }
    }

    @RequestMapping(value = "Question.htm", method = RequestMethod.GET)
    public static ModelAndView loadQuestionPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        if(request.getParameter("key") != null){
            System.out.println(request.getParameter("key"));
            request.getSession().setAttribute("assignmentKey",request.getParameter("key") );
            response.sendRedirect("Question.htm");
        }
        ModelAndView model = new ModelAndView("Question");
        return model;
    }

    /*@RequestMapping(value = "TeacherHomePage.htm", method = RequestMethod.GET)
    public static ModelAndView loadTeacherPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        ModelAndView model = new ModelAndView("TeacherHomePage");
        return model;
    }*/

    @RequestMapping(value = "TeacherHomePage.htm", method = RequestMethod.GET)
    public static ModelAndView loadInstructorPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        ArrayList<Object> credentials = GenUtils.checkCredentials();
        int access = (int)credentials.get(0);
        if(access == GenUtils.INSTRUCTOR || access == GenUtils.ADMIN){
            User user = (User)credentials.get(1);
            ModelAndView model = new ModelAndView("TeacherHomePage");
            model.addObject("i_email",user.getEmail());
            return model;
        }


        ModelAndView model = new ModelAndView("Home");
        return model;
    }


    @RequestMapping(value = "TeacherInformationPage.htm", method = RequestMethod.GET)
    public static ModelAndView loadClassInformationPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        ModelAndView model = new ModelAndView("ClassInformationPage");
        return model;
    }

    @RequestMapping(value = "TeacherAssignmentPage.htm", method = RequestMethod.GET)
    public ModelAndView loadTeacherAssignmentsPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        ModelAndView model = new ModelAndView("TeacherAssignmentPage");
        return model;
    }

    @RequestMapping(value = "TeacherGradingPage.htm", method = RequestMethod.GET)
    public ModelAndView loadTeacherGradingPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        ModelAndView model = new ModelAndView("TeacherGradePage");
        return model;
    }

    @RequestMapping(value = "TeacherQuestionPage.htm", method = RequestMethod.GET)
    public static ModelAndView loadTeacherQuestionPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        ModelAndView model = new ModelAndView("TeacherQuestionListPage");
        return model;
    }



}
