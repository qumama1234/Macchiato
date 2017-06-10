package com.macchiato.services;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by li on 4/5/2017.
 */
@Controller
public class ServiceManager {
    @RequestMapping(value = "checkuserstatus.htm", method = RequestMethod.GET)
    public void checkUserStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        UserService userService = UserServiceFactory.getUserService();
        if (userService.isUserLoggedIn()) {
            User user = userService.getCurrentUser();
            out.println("" + user.getEmail());

        } else {
            out.println("NOT_LOGGED_IN");
        }
    }
}
