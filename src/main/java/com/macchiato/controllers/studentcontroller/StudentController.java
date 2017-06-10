package com.macchiato.controllers.studentcontroller;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.macchiato.beans.*;
import com.macchiato.utility.DiscussionBoardUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by li on 4/17/2017.
 */
@Controller
public class StudentController {
    /*
    This function loads the Student page.
    It gets the User data from the datastore,
    then it loads the course list,
    then it gets the current course that the user is at.
     */
    @RequestMapping(value = "LoadStudent.htm", method = RequestMethod.GET)
    public void loadStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //storeDummyData();
        //System.out.println("Dummy");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        UserBean currUser;
        CourseListBean currList;
        AssignmentListBean assignList;
        StudentBean currStudent;
        // LOAD USER
        // Get the current user email and create userType
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String email = user.getEmail();
        long userType = 0;
        // Load the user from the datastore
        // Get the datastore.
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        // Create the search keyword.
        Query.Filter email_filter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email.trim());
        // Create the query
        Query q = new Query("User").setFilter(email_filter);
        PreparedQuery pq = datastore.prepare(q);
        // Find the UserBean Entity
        Entity result = pq.asSingleEntity();
        // If no student was found on the database create a new one.
        if(result == null){
            System.out.println("No User, creating and saving one now.");
            // Create an Entity to store
            Entity userEntity = new Entity("User",email);
            userEntity.setProperty("email", email);
            userEntity.setProperty("access", userType);
            datastore.put(userEntity);
        }else{
            System.out.println("Found User, loading User in now.");
            // Extract the information from result
            email = (String)result.getProperty("email");
            userType = (Long)result.getProperty("access");
            System.out.println(email);
            System.out.println(userType);
        }
        currUser = new UserBean(email, userType);

        System.out.println(currUser.generateJSON());


        // LOAD COURSE LIST
        ArrayList<String> crsCodes = new ArrayList<String>();
        ArrayList<CourseBean> crsList =new ArrayList<CourseBean>();
        // Load the course list from the datastore
        System.out.println("No course list in session");
        // If no course, load it up
        // Find all the crsCodes the user is enrolled in.
        datastore = DatastoreServiceFactory.getDatastoreService();
        email_filter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email.trim());
        q = new Query("Enrollment").setFilter(email_filter);
        pq = datastore.prepare(q);
        for (Entity enrollmentEntity : pq.asIterable()) {
            long course_id = (long)enrollmentEntity.getProperty("course_id");
            Query.Filter courseid_filter = new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, course_id);
            q = new Query("Course").setFilter(courseid_filter);
            pq = datastore.prepare(q);

            List<Entity> course_list = pq.asList(FetchOptions.Builder.withDefaults());
            int course_list_size = course_list.size();
            if(course_list_size != 1){
                //Error, there should only be one course for this course id
                //return value should go here
                return;
            }

            String course_code = (String)course_list.get(0).getProperty("course_code");
            crsCodes.add(course_code);
            System.out.println(course_code);
        }
        if (crsCodes.size() > 0){
            System.out.println("Loading courses");
            // Find all the courses the user is enrolled in
            Query.Filter crsCode_filter;
            Entity course;
            CourseBean crsBean;
            for (int i = 0; i<crsCodes.size(); i++){
                System.out.println(crsCodes.get(i));
                crsCode_filter = new Query.FilterPredicate("course_code", Query.FilterOperator.EQUAL, crsCodes.get(i).trim());
                q = new Query("Course").setFilter(crsCode_filter);
                pq = datastore.prepare(q);
                course = pq.asSingleEntity();

                crsBean = new CourseBean((String)course.getProperty("course_code"),
                        (String)course.getProperty("name"),
                        (String)course.getProperty("email"),
                        (String)course.getProperty("description"));
                crsBean.setSection((String)course.getProperty("section"));
                crsList.add(crsBean);
            }
        }else{
            System.out.println("User not enrolled in any courses");
        }



        CourseBean currCourse = null;
        boolean enrolling = false;
        /* 0 for not enrolling
           1 for enroll success
           2 for already enrolled
           3 for course does not exist*/
        int enrollStatus = 0;
        // CHANGING THE CURR COURSE
        if(request.getParameter("crsName") != null){
            System.out.println(request.getParameter("crsName"));
            String changeCourse = request.getParameter("crsName");
            for (int i = 0; i <crsList.size(); i++){
                if(crsList.get(i).getCrsCode().compareTo(changeCourse) == 0){
                    currCourse = crsList.get(i);
                    // Update the CurrentCourse Entity
                    email_filter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email.trim());
                    // Create the query
                    q = new Query("CurrentCourse").setFilter(email_filter);
                    pq = datastore.prepare(q);
                    // Find the UserBean Entity
                    result = pq.asSingleEntity();
                    result.setProperty("course_code", currCourse.getCrsCode());
                    datastore.put(result);
                    //request.getSession().setAttribute("currCourse",currCourse);
                    System.out.println("Swapping course");
                }
            }
        }// Check if we're enrolling
        else if(request.getParameter("enroll") != null){
            enrolling = true;
            boolean newCourse = true;
            System.out.println(request.getParameter("enroll"));
            String enrollCode = request.getParameter("enroll");
            for(int i = 0; i<crsList.size(); i++){
                if(enrollCode.compareTo(crsList.get(i).getCrsCode()) == 0){
                    newCourse = false;
                    enrollStatus = 2;
                    System.out.println("Already enrolled in course");
                }
            }
            if(newCourse){
                // Look for course in database
                datastore = DatastoreServiceFactory.getDatastoreService();
                // Create the search keyword.
                Query.Filter enroll_filter = new Query.FilterPredicate("course_code", Query.FilterOperator.EQUAL, enrollCode.trim());
                // Create the query
                q = new Query("Course").setFilter(enroll_filter);
                pq = datastore.prepare(q);
                result = pq.asSingleEntity();
                // If course does not exist, return a 3
                if(result == null){
                    enrollStatus = 3;
                    System.out.println("Course does not exist");
                }else{
                    CourseBean enrollCourse = new CourseBean((String)result.getProperty("course_code"),
                            (String)result.getProperty("name"),
                            (String)result.getProperty("email"),
                            (String)result.getProperty("description"));
                    enrollCourse.setSection((String)result.getProperty("section"));
                    crsList.add(enrollCourse);
                    currCourse = enrollCourse;
                    // Set the CurrentCourse to this one
                    email_filter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email.trim());
                    // Create the query
                    q = new Query("CurrentCourse").setFilter(email_filter);
                    pq = datastore.prepare(q);
                    // Find the UserBean Entity
                    result = pq.asSingleEntity();
                    result.setProperty("course_code", currCourse.getCrsCode());
                    datastore.put(result);
                    //request.getSession().setAttribute("currCourse",currCourse);
                    long u_set = 0;
                    Entity enrollmentEntity = new Entity("Enrollment");
                    enrollmentEntity.setProperty("email", email);
                    enrollmentEntity.setProperty("username", email);
                    enrollmentEntity.setProperty("access", userType);
                    enrollmentEntity.setProperty("u_set", u_set);
                    enrollmentEntity.setProperty("course_id", result.getProperty("id"));
                    enrollmentEntity.setProperty("forum_key", DiscussionBoardUtils.getForumKey(enrollCourse.getInstrEmail(), enrollCourse.getCrsName(),enrollCourse.getSection()));
                    datastore.put(enrollmentEntity);
                    // Create a question to student point tracker

                    enrollStatus = 1;
                    System.out.println("Enrolled into new course");
                }
            }

        }else{
            // Get the current course
            email_filter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email.trim());
            // Create the query
            q = new Query("CurrentCourse").setFilter(email_filter);
            pq = datastore.prepare(q);
            // Find the UserBean Entity
            if (pq != null) {
                result = pq.asSingleEntity();
                if(result == null){
                    currCourse = null;
                }else{
                    String currentCourse = (String)result.getProperty("course_code");
                    for(int i = 0; i<crsList.size();i++){
                        if(currentCourse.equals(crsList.get(i).getCrsCode())){
                            currCourse = crsList.get(i);
                        }
                    }
                }
            }
            // currCourse = (CourseBean)request.getSession().getAttribute("currCourse");
        }
        // If no course in session,
        if(currCourse == null){
            // get the first course
            if(crsList.size() > 0){
                currCourse = crsList.get(0);
                // Create the first currCourse Entity
                datastore = DatastoreServiceFactory.getDatastoreService();
                Entity currCrsEntity = new Entity("CurrentCourse");
                currCrsEntity.setProperty("email", email.trim());
                currCrsEntity.setProperty("course_code", currCourse.getCrsCode());
                datastore.put(currCrsEntity);
                System.out.println("Creating current course");
                // request.getSession().setAttribute("currCourse",currCourse);
            }else{
                // create an empty one
                currCourse = new CourseBean();
            }
        }




        // Sort the crsList alphabetically
        Collections.sort(crsList, new Comparator<CourseBean>() {
            @Override
            public int compare(CourseBean course2, CourseBean course1)
            {

                return  course1.getCrsName().compareTo(course2.getCrsName());
            }
        });

        assignList = new AssignmentListBean(loadAssignmentList(request, currCourse,email));
        currList = new CourseListBean(crsList);
        currStudent = new StudentBean(currUser,currList,currCourse,assignList);
        String JSONoutput = "{";
        JSONoutput += currStudent.generateJSON();
        JSONoutput += ",\"Enroll\": {\"status\": \"";
        JSONoutput += Integer.toString(enrollStatus);
        JSONoutput += "\"}}";
        System.out.println(JSONoutput);
        out.println(JSONoutput);
    }
    /*
    Loads the Assignment
     */
    public ArrayList<AssignmentBean> loadAssignmentList(HttpServletRequest request, CourseBean currCourse, String email){
        // LOAD ASSIGNMENT LIST
        ArrayList<AssignmentBean> assignList = new ArrayList<AssignmentBean>();
        AssignmentBean assignment;
        // Find all the assignments for the current course the user is enrolled in.
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter code_filter = new Query.FilterPredicate("course_code", Query.FilterOperator.EQUAL, currCourse.getCrsCode().trim());
        Query q = new Query("Assignment").setFilter(code_filter);
        PreparedQuery pq = datastore.prepare(q);
        q = new Query("Assignment").setFilter(code_filter);
        pq = datastore.prepare(q);
        int total = 0;
        String grade;
        int points = 0;
        // For each Assignment
        for (Entity assignmentEntity : pq.asIterable()) {
            assignment = new AssignmentBean((String)assignmentEntity.getProperty("course_code"),
                    (String)assignmentEntity.getProperty("assignmentName"),
                    assignmentEntity.getKey().toString());
            assignment.setEnd((String)assignmentEntity.getProperty("duedate"));
            Query.Filter assignment_filter = new Query.FilterPredicate("assignmentKey", Query.FilterOperator.EQUAL, assignmentEntity.getKey().toString().trim());
            q = new Query("Question").setFilter(assignment_filter);
            pq = datastore.prepare(q);
            // For each Question
            total = 0;
            points = 0;
            for(Entity questionEntity : pq.asIterable()){
                total++;
                Query.Filter question_filter = new Query.FilterPredicate("question_key", Query.FilterOperator.EQUAL, questionEntity.getKey().toString().trim());
                Query.Filter email_filter = new Query.FilterPredicate("email_address", Query.FilterOperator.EQUAL,email.trim());
                Query.CompositeFilter question_info_filter = Query.CompositeFilterOperator.and(email_filter, question_filter);
                q = new Query("QuestionInfo").setFilter(question_info_filter);
                pq = datastore.prepare(q);
                // For each QuestionInfo
                for(Entity QIEntity : pq.asIterable()){
                    System.out.println("GOING THROUGH THIS DATATBASE");
                    if((QIEntity.getProperty("point")).equals("1")){
                        points++;
                    }
                }
            }
            grade = Integer.toString(points) + "/" + Integer.toString(total);
            assignment.setGrade(grade);
            assignList.add(assignment);
        }




        // Sort the assignList by Due Date
        Collections.sort(assignList, new Comparator<AssignmentBean>() {
            @Override
            public int compare(AssignmentBean course2, AssignmentBean course1)
            {

                return  course2.getEnd().compareTo(course1.getEnd());
            }
        });

        return assignList;
    }



    public void storeDummyData(){
        // Store 3 dummy courses
        long id = 1;
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity cse114 = new Entity("Course");
        cse114.setProperty("name", "CSE114");
        cse114.setProperty("course_code", "1111");
        cse114.setProperty("description", "This is CSE114");
        cse114.setProperty("section", "01");
        cse114.setProperty("email", "teacher1@gmail.com");
        cse114.setProperty("id", id);
        datastore.put(cse114);
        id = 2;
        Entity cse307 = new Entity("Course");
        cse307.setProperty("name", "CSE307");
        cse307.setProperty("course_code", "2222");
        cse307.setProperty("description", "This is CSE307");
        cse307.setProperty("section", "01");
        cse307.setProperty("email", "teacher1@gmail.com");
        cse307.setProperty("id", id);
        datastore.put(cse307);
        id = 3;
        Entity cse308 = new Entity("Course");
        cse308.setProperty("name", "CSE308");
        cse308.setProperty("course_code", "3333");
        cse308.setProperty("description", "This is CSE308");
        cse308.setProperty("section", "01");
        cse308.setProperty("email", "teacher1@gmail.com");
        cse308.setProperty("id", id);
        datastore.put(cse308);
        /*// Store 2 dummy enrollment
        Entity enrollmentEntity = new Entity("CourseEnrollment");
        enrollmentEntity.setProperty("course_code", "1111");
        enrollmentEntity.setProperty("email", "test@example.com");
        datastore.put(enrollmentEntity);
        //System.out.println("Enroll in 114");
        Entity enrollment307 = new Entity("CourseEnrollment");
        enrollment307.setProperty("course_code", "2222");
        enrollment307.setProperty("email", "test@example.com");
        datastore.put(enrollment307);*/
        //System.out.println("Enroll in 307");

        // Store 6 dummy assignments
    }
}
