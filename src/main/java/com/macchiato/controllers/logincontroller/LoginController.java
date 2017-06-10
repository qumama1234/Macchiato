package com.macchiato.controllers.logincontroller;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.macchiato.beans.UserBean;
import com.macchiato.controllers.urlmappingcontroller.URLMapping;
import com.macchiato.utility.DiscussionBoardUtils;
import com.macchiato.utility.GenUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by li on 4/4/2017.
 */
@Controller
public class LoginController {
    @RequestMapping(value="login.htm", method = RequestMethod.GET)
    public String LoginService (HttpServletRequest request, HttpServletResponse response){
        UserService userService = UserServiceFactory.getUserService();
        //Creates dummy data for the discussion board


        if(userService.isUserLoggedIn()){
            User user = userService.getCurrentUser();
            System.out.println("Already logged in: UserBean Logged out");
            return "redirect:" + userService.createLogoutURL("/Home.htm");
        }
        else{
            String access_str = request.getParameter("access");
            if(access_str == null || access_str.trim().isEmpty()){
                return "redirect:/Home.htm";

            }
            try {
                int access = Integer.parseInt(access_str);
                return "redirect:" + userService.createLoginURL("/loginresolver.htm?access=" + access);
            }
            catch(Exception e){
                return "redirect:/Home.htm";
            }


        }
    }

    @RequestMapping(value="loginresolver.htm", method = RequestMethod.GET)
    public String LoginResolver (HttpServletRequest request, HttpServletResponse response){
        String access_str = request.getParameter("access");

        DiscussionBoardUtils.createDummyDiscussionData();
        if(access_str == null || access_str.trim().isEmpty()){
            return "redirect:/login.htm";
        }


        User user = GenUtils.getActiveUser();
        if(user == null){
            return "redirect:/login.htm";
        }

        try {
            int access = Integer.parseInt(access_str);
            if(access == 0){
                GenUtils.createStudent(user.getEmail());
                return "redirect:/Home.htm";
            }
            else if(access == 1){
                GenUtils.createInstructor(user.getEmail());
                return "redirect:/Home.htm";
            }
            else{
                return "redirect:/login.htm";
            }
        }
        catch(Exception e){
            return "redirect:/login.htm";
        }

    }

    @RequestMapping(value="loginstatus.htm", method = RequestMethod.GET)
    public void LoginStatus (HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();


        User user = GenUtils.getActiveUser();
        if(user == null){
            out.println("{}");
            return;
        }

        ArrayList<Object> credentials = GenUtils.checkCredentials();
        int access = (int)credentials.get(0);
        if(access == GenUtils.STUDENT){
            UserBean temp = new UserBean(user.getEmail(),access,UserBean.STUDENT_HOME);
            String output = "{\"User\":"+ temp.generateJSON() + "}";
            System.out.println(output);
            out.println(output);
            return;
        }

        if(access == GenUtils.INSTRUCTOR){
            UserBean temp = new UserBean(user.getEmail(),access,UserBean.INSTRUCTOR_HOME);
            String output = "{\"User\":"+ temp.generateJSON() + "}";
            System.out.println(output);
            out.println(output);
            return;
        }

        if(access == GenUtils.ADMIN){
            //To do, load admin page
            //go to admin page
            return;
        }

        out.println("{}");
        return;
    }

    @RequestMapping(value="enter.htm", method = RequestMethod.GET)
    public ModelAndView PortalResolver (HttpServletRequest request, HttpServletResponse response) throws IOException {

        User user = GenUtils.getActiveUser();
        if(user == null){
            return URLMapping.loadHomePage(request,response);
        }

        ArrayList<Object> credentials = GenUtils.checkCredentials();
        int access = (int)credentials.get(0);
        if(access == GenUtils.STUDENT){
            return URLMapping.loadStudentPage(request,response);
        }

        if(access == GenUtils.INSTRUCTOR){
            return URLMapping.loadInstructorPage(request,response);
        }

        if(access == GenUtils.ADMIN){
            //To do, load admin page
            //go to admin page
        }
        return URLMapping.loadHomePage(request,response);
    }


}
