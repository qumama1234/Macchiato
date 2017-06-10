package com.macchiato.utility;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.KeyFactory.Builder;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.macchiato.general.generaldata.CourseData;
import com.macchiato.general.generaldata.CourseDataHelper;
import com.macchiato.general.generaldata.EnrollmentData;

import java.util.ArrayList;
import java.util.List;

/**
 * GenUtils.java
 * Purpose: This class serves as a general utility class for the application.
 * @author Karl Jean-Brice
 */

public class GenUtils {
    public static final int STUDENT = 0;
    public static final int INSTRUCTOR = 1;
    public static final int ADMIN = 2;
    public final static int SUCCESS = 3;
    public final static int FAILURE = 4;
    public static final int NOT_LOGGED_IN = 5;
    public static final int NO_PERMISSION = 6;
    public static final int EMPTY_PARAMETERS = 7;
    public final static int DUPLICATE_COURSE = 8;
    public final static int INVALID_LENGTH = 9;
    public final static int COURSE_NOT_FOUND = 10;
    public final static int DUPLICATE_ENTRY = 11;
    public final static int DATABASE_ERROR = 12;


    /**
     * Creates an instructor account for a specified email address.
     * @author Karl Jean-Brice
     * @param email the email address to create an account for.
     */
    public static void createInstructor(String email){
        if(email == null){
            return;
        }

        if(email.trim().isEmpty()){
            return;
        }

        Entity user;
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key key = new Builder("User",email).getKey();


        try {
            user = datastore.get(key);
            user.setProperty("access",GenUtils.INSTRUCTOR);
            //user.setProperty("course","336");
            user.setProperty("email",email.trim());
            System.out.println("Create Instructor: Key: " + user.getKey());
            System.out.println("Create Instructor: User already in datastore");
        } catch (EntityNotFoundException e) {
            user = new Entity("User",email);
            user.setProperty("access",GenUtils.INSTRUCTOR);
            //user = new Entity("Course","cse336",key1);
            System.out.println("Create Instructor: Key " + user.getKey());
            //user.setProperty("course","336");
            user.setProperty("email",email.trim());
            System.out.println("Create Instructor: New Instructor in datastore.");
        }

        datastore.put(user);
        return;

    }


    /**
     * Creates an student account for a specified email address.
     * @author Karl Jean-Brice
     * @param email the email address to create an account for.
     */
    public static void createStudent(String email){
        if(email == null){
            return;
        }

        if(email.trim().isEmpty()){
            return;
        }

        Entity user;
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key key = new Builder("User",email).getKey();


        // Key key = new Builder("User",email).addChild("Course","cse336").getKey();
        //Key parentKey = KeyFactory.createKey(key,"Group");



        try {
            user = datastore.get(key);
            user.setProperty("access",GenUtils.STUDENT);
            //user.setProperty("course","336");
            user.setProperty("email",email.trim());
            System.out.println("Create Instructor: Key: " + user.getKey());
            System.out.println("Create Student: User already in datastore");
        } catch (EntityNotFoundException e) {
            user = new Entity("User",email);
            user.setProperty("access",GenUtils.STUDENT);
            //user = new Entity("Course","cse336",key1);
            System.out.println("Create Student: Key " + user.getKey());
            //user.setProperty("course","336");
            user.setProperty("email",email.trim());
            System.out.println("Create Student: New student in datastore.");
        }

        datastore.put(user);
        return;

    }

    /**
     * Checks the credentials of the user currently logged in.
     * @author Karl Jean-Brice
     * @return an object containing the credentials of the user.
     */
    public static ArrayList<Object> checkCredentials(){

        ArrayList<Object> credentials = new ArrayList<>();

        UserService userService = UserServiceFactory.getUserService();
        if(userService.isUserLoggedIn()){
            User user = userService.getCurrentUser();
            if(isInstructor(user.getEmail())){
                credentials.add(0,INSTRUCTOR);
                credentials.add(1,user);
                return credentials;
            }
            else if(isAdmin(user.getEmail())){
                credentials.add(0,ADMIN);
                credentials.add(1,user);
                return credentials;
            }
            else{
                credentials.add(0,STUDENT);
                credentials.add(1,user);
                return credentials;
            }
        }
        else{
            credentials.add(0,NOT_LOGGED_IN);
            credentials.add(1,null);
            return credentials;
        }
    }

    /**
     * Checks if an email address is associated with an instructor account.
     * @author Karl Jean-Brice
     * @param email the email address to check
     * @return true, if the user is an instructor; False, otherwise.
     */
    public static boolean isInstructor(String email){

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key key = new Builder("User",email).getKey();

        try {
            Entity user = datastore.get(key);
            long access = -1;
            access = (long)user.getProperty("access");
            if(access == 1){
                return true;
            }
            return false;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    /**
     * Checks if an email address is associated with an admin account.
     * @author Karl Jean-Brice
     * @param email the email address to check
     * @return true, if the user is an admin; False, otherwise.
     */
    public static boolean isAdmin(String email){

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key key = new Builder("User",email).getKey();

        try {
            Entity user = datastore.get(key);
            long access = -1;
            access = (long)user.getProperty("access");
            if(access == 2){
                return true;
            }
            return false;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    /**
     * Retrieves the details of the user currently logged in.
     * @author Karl Jean-Brice
     * @return a User object containing details about the user.
     */
    public static User getActiveUser(){
        UserService userService = UserServiceFactory.getUserService();
        if(userService.isUserLoggedIn()) {
            User user = userService.getCurrentUser();
            return user;
        }
        else{
            return null;
        }
    }

    /**
     * Retrieves the id of the course associated with an email address, course name, and course section.
     * @author Karl Jean-Brice
     * @param instructor_email the email address of the use that created the course
     * @param course the name of the course
     * @param section the section of the course
     * @return the id associated with a course
     */
    public static  ArrayList<Object> getCourseID(String instructor_email, String course, String section){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter email_filter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, instructor_email.trim());
        Query.Filter name_filter = new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, course.trim());
        Query.Filter section_filter = new Query.FilterPredicate("section", Query.FilterOperator.EQUAL, section.trim());
        Query.CompositeFilter composite_filter = Query.CompositeFilterOperator.and(email_filter, name_filter, section_filter);

        Query q = new Query("Course").setFilter(composite_filter);
        PreparedQuery pq = datastore.prepare(q);

        int course_size = pq.asList(FetchOptions.Builder.withDefaults()).size();
        if (course_size != 1) {
            return null;
        }

        Entity e = pq.asList(FetchOptions.Builder.withDefaults()).get(0);
        long id = (long)e.getProperty("id");

        ArrayList<Object> temp = new ArrayList<>();
        temp.add(0,id);

        return temp;
    }


    /**
     * Returns enrollment data associated with a user.
     * @author Karl Jean-Brice
     * @param email the email address of the user
     * @param course the course the user is participating in.
     * @param forumkey the key of the forum the user in involved in.
     * @return an object containing enrollment data about the user.
     */
    public static EnrollmentData getEnrolledUser(String email, String course , Key forumkey){

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter groupkey_filter = new Query.FilterPredicate("forum_key", Query.FilterOperator.EQUAL, forumkey);
        Query.Filter email_filter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email);
        Query.CompositeFilter enroll_filter = Query.CompositeFilterOperator.and(groupkey_filter,email_filter);
        Query q = new Query("Enrollment").setFilter(enroll_filter);
        PreparedQuery pq = datastore.prepare(q);

        List<Entity> enrollment_list = pq.asList(FetchOptions.Builder.withDefaults());
        EnrollmentData enrolled_user = null;
        for (int i = 0; i < enrollment_list.size(); i++) {
            Entity enrollment_data = enrollment_list.get(i);
            String enrollment_email = (String) enrollment_data.getProperty("email");
            if (enrollment_email.equalsIgnoreCase(email)) {
                enrolled_user = new EnrollmentData(enrollment_data.getKey().getId(), enrollment_email,
                        (String) enrollment_data.getProperty("username"), course, (long) enrollment_data.getProperty("access"), (long)enrollment_data.getProperty("u_set"));
                break;
            }
        }

        return enrolled_user;
    }

    /**
     * Enrolls a user in a specified course.
     * @author Karl Jean-Brice
     * @param forum_key the forum key of the course.
     * @param email the email address of the user to enroll.
     * @param nickname the initial username of the user on the forum.
     * @param access the access rights the user has on the forum.
     * @param course_id the id of the course to enroll in.
     * @return 11 (DUPLICATE_ENTRY) if the user is already enrolled in the course.
     *         12 (DATABASE_ERROR) Multiple or no entries are found in the datastore for the requested parameters
     *         0 (SUCCESS) successfully enrolled the user in the course
     */
    public static int enrollUser(Key forum_key, String email, String nickname, int access,long course_id){

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter forum_id_filter = new Query.FilterPredicate("forum_key", Query.FilterOperator.EQUAL, forum_key);
        Query.Filter email_filter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email);
        Query.CompositeFilter email_forumid_filter = Query.CompositeFilterOperator.and(forum_id_filter,email_filter);
        Query q = new Query("Enrollment").setFilter(email_forumid_filter);
        PreparedQuery pq = datastore.prepare(q);

        int student_size = pq.asList(FetchOptions.Builder.withDefaults()).size();
        if(student_size > 1){
            return DiscussionBoardUtils.DATABASE_ERROR;
        }

        if(student_size == 1){
            return DiscussionBoardUtils.DUPLICATE_ENTRY;
        }

        Entity user = new Entity("Enrollment");
        user.setProperty("email",email);
        user.setProperty("forum_key",forum_key);
        user.setProperty("access",access);
        user.setProperty("course_id",course_id);

        if(nickname == null || nickname.trim().length() == 0){
            user.setProperty("username",email);
            user.setProperty("u_set",0);
        }
        else{
            user.setProperty("username",nickname);
            user.setProperty("u_set",1);
        }
        datastore.put(user);

        return DiscussionBoardUtils.SUCCESS;

    }







}



