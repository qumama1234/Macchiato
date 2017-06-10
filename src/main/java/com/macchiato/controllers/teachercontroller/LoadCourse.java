package com.macchiato.controllers.teachercontroller;

import com.google.appengine.api.users.User;
import com.macchiato.beans.CourseBean;
import com.macchiato.utility.GenUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


import static com.macchiato.utility.TeachersUtils.CourseListJson;
import static com.macchiato.utility.TeachersUtils.isOwned;

/**
 * Created by Xiangbin on 4/19/2017.
 * Load course function will load all the course owned by this teacher,
 * and it will listed on teacher home page
 */
@Controller
public class LoadCourse {
    /**
     *Load course function will load all the course owned
     * by this teacher, and it will listed on teacher home page
     * @param  request  The servlet container to get the data from front end
     * @param  response  The servlet container to sent to the data from front end
     */
    @RequestMapping(value = "/LoadCourse.htm", method = RequestMethod.GET)
    public void LoadCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        User active_user = GenUtils.getActiveUser();
        String instructor_email = active_user.getEmail();
        if (instructor_email== null) {
            System.out.print("There is no active_user");
        }
        else{
            System.out.println("instructor email: "+instructor_email);
            ArrayList<CourseBean> newList= isOwned(instructor_email);
            System.out.println(CourseListJson(newList));
            out.println(CourseListJson(newList));
        }
    }
}
