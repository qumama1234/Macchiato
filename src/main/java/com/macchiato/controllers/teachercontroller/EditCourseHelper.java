package com.macchiato.controllers.teachercontroller;

import com.google.appengine.api.datastore.*;
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

/**
 * Created by Xiangbin on 5/9/2017.
 */
@Controller
public class EditCourseHelper {
    /**
     *The function will get course code from the front end,
     * and find information of this course database
     * and use response to sent the  it back to front end
     * @param  request  The servlet container to get the data from front end
     * @param  response  The servlet container to sent to the data from front end
     */
    @RequestMapping(value="/findDes.htm", method = RequestMethod.GET)
    public void editCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        User active_user = GenUtils.getActiveUser();
        String instructor_email =active_user.getEmail();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        if (instructor_email == null ) {
            System.out.println("active_user is null");
        }
        else{
            String ClassCode=request.getParameter("course_code");
            Query.Filter CrsCode_filter = new Query.FilterPredicate("course_code", Query.FilterOperator.EQUAL,ClassCode );
            Query q = new Query("Course").setFilter(CrsCode_filter);
            PreparedQuery pq = datastore.prepare(q);
            int numberOfClass = pq.asList(FetchOptions.Builder.withDefaults()).size();
            if(numberOfClass!=1){
                System.out.println("Course code error");
            }
            else{
                for (Entity result : pq.asIterable()) {
                    String email = (String) result.getProperty("email");
                    String CrsName = (String) result.getProperty("name");
                    String CrsCode = (String) result.getProperty("course_code");
                    String Crsdis = (String) result.getProperty("description");
                    String Section = (String) result.getProperty("section");
                    CourseBean newBean = new CourseBean();
                    newBean.setInstrEmail(email);
                    newBean.setCrsName(CrsName);
                    newBean.setCrsCode(CrsCode);
                    newBean.setDescription(Crsdis);
                    newBean.setSection(Section);
                    out.println(newBean.generateJSON());
                    System.out.print("Load description success");
                }
            }
        }
    }
}
