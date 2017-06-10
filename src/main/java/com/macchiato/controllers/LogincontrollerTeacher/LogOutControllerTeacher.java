package com.macchiato.controllers.LogincontrollerTeacher;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Xiangbin on 4/20/2017.
 */
@Controller
public class LogOutControllerTeacher {
    @RequestMapping(value="/logoutTeacher.htm", method = RequestMethod.GET)
    public String LogOutService (){
        UserService userService = UserServiceFactory.getUserService();
        if(userService.isUserLoggedIn()){
            System.out.println("Teacher Logged out");
            return "redirect:" + userService.createLogoutURL("/");
        }
        else{
            System.out.println("Teacher already logged out");
        }
        return "/";
    }
}
