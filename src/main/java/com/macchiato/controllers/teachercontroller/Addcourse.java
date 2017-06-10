package com.macchiato.controllers.teachercontroller;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.macchiato.beans.CourseBean;
import com.macchiato.utility.GenUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Xiangbin on 4/19/2017.
 * this class can add the Course to the database
 */
@Controller
public class Addcourse {
    /**
     *The function will use  login email to get the email and get
     * all the information from the front end(imputed by user)
     * and add course to the database
     * @param  request  The servlet container to get the data from front end
     * @param  response  The servlet container to sent to the data from front end
     */

    //this function add the new course to the database
    @RequestMapping(value="addCourse.htm", method = RequestMethod.POST)
    public void addCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User active_user = GenUtils.getActiveUser();
        String instructor_email =active_user.getEmail();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        if (instructor_email == null ) {
            System.out.println("active_user is null");
        }
        else{
            String ClassName=request.getParameter("name");
            String ClassDis=request.getParameter("description");
            String ClassSection=request.getParameter("section");
            String email=instructor_email;
            CourseBean newClass=new  CourseBean(ClassName,email,ClassDis);
            Entity user = new Entity("Course");
            user.setProperty("course_code",newClass.getCrsCode());
            user.setProperty("name", ClassName);
            user.setProperty("description", ClassDis);
            user.setProperty("email",email);
            user.setProperty("section",ClassSection);
            datastore.put(user);
            System.out.print("From add: "+" "+newClass.generateJSON());
        }

    }


}


