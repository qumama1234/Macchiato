package com.macchiato.controllers.logoutcontroller;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by li on 4/4/2017.
 */
@Controller
public class LogoutController {
    @RequestMapping(value="logout.htm", method = RequestMethod.GET)
    public String Logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        if(userService.isUserLoggedIn()){
            System.out.println("UserBean Logged out");
            return "redirect:" + userService.createLogoutURL("/");
        }
        else{
            System.out.println("UserBean already logged out");
        }
        return "/";
    }
}
