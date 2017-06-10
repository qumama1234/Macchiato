package com.macchiato.utility;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.macchiato.controllers.discussioncontroller.DiscussionBoardController;
import com.macchiato.general.generaldata.CourseData;
import com.macchiato.general.generaldata.CourseDataHelper;
import com.macchiato.general.discussiondata.PostData;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DiscussionBoardUtils.java
 * Purpose: This class serves as a utility class for discussion board.
 * @author Karl Jean-Brice
 */
public class DiscussionBoardUtils {

    public final static int SUCCESS = 0;
    public final static int ENROLLED = 1;
    public final static int NOT_ENROLLED = 2;
    public final static int NOT_LOGGED_IN = 3;
    public final static int DUPLICATE_ENTRY = 4;
    public final static int EMPTY_PARAMETERS = 5;
    public final static int BOARD_NOT_FOUND = 6;
    public final static int DATABASE_ERROR = 7;
    public final static int POST_NOT_FOUND = 8;
    public final static int NO_ACCESS = 9;
    public final static int INVALID_ARGS = 10;
    public final static int ALREADY_ACTIVE = 11;
    public final static int INVALID_LENGTH = 12;
    public final static int FAILURE = 12;



    /**
     * Checks if a user is found in the datastore (Enrollment table) using the forum key.
     * @author Karl Jean-Brice
     * @param forumKey a key pointing to a unique datastore Forum object.
     * @param email the email to check for enrollement
     * @return 4 (DUPLICATE_ENTRY) if the forum key for the user is found multiple times in the datastore (Enrollment table).
     *         2 (NOT_ENROLLED) if the forum key for the user is not found in the datastore (Enrollment table).
     *         1 (ENROLLED) if the forum key for the user is found in the Enrollment table.
     */
    public static int isEnrolled(Key forumKey, String email){

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter forumkey_filter = new Query.FilterPredicate("forum_key", Query.FilterOperator.EQUAL, forumKey);
        Query.Filter email_filter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email);
        Query.CompositeFilter email_forumkey_filter = Query.CompositeFilterOperator.and(forumkey_filter,email_filter);
        Query q = new Query("Enrollment").setFilter(email_forumkey_filter);
        PreparedQuery pq = datastore.prepare(q);

        int student_size = pq.asList(FetchOptions.Builder.withDefaults()).size();
        if(student_size > 1){
            return DUPLICATE_ENTRY;
        }

        if(student_size == 0){
            return NOT_ENROLLED;
        }

        return ENROLLED;
    }


    /**
     * Retrieves the list of forums the user is currently in.
     * @author Karl Jean-Brice
     * @param email the email to check for enrollment
     * @return null if no forums are found for the user. Otherwise, an object containing a list forums the user is participating in.
     */
    public static CourseDataHelper retrieveForumList(String email) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter email_filter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email);
        Query q = new Query("Enrollment").setFilter(email_filter);
        PreparedQuery pq = datastore.prepare(q);

        CourseDataHelper data = null;
        String i_email = null;
        String course = null;
        String username = null;
        String section = null;
        long course_id;
        ArrayList<CourseData> courseData = new ArrayList<>();

        for (Entity result : pq.asIterable()) {
            Key key = (Key) result.getProperty("forum_key");
            try {
                Entity forum = datastore.get(key);
                i_email = (String)forum.getProperty("email");
                course = (String)forum.getProperty("course_name");
                section = (String)forum.getProperty("section");
                course_id = (long)forum.getProperty("course_id");

                email_filter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, i_email);
                Query.Filter forumkey_filter = new Query.FilterPredicate("forum_key", Query.FilterOperator.EQUAL, key);
                Query.CompositeFilter email_forumkey_filter = Query.CompositeFilterOperator.and(forumkey_filter,email_filter);
                q = new Query("Enrollment").setFilter(email_forumkey_filter);
                pq = datastore.prepare(q);

                int list_size = pq.asList(FetchOptions.Builder.withDefaults()).size();
                if(list_size == 1){
                    Entity enroll_result = pq.asIterable().iterator().next();
                    username = (String)enroll_result.getProperty("username");
                }else{
                    return null;
                }

                CourseData newData = new CourseData(i_email,username,course,section,course_id);
                courseData.add(newData);


            } catch (EntityNotFoundException e) {
                System.out.println("Can't find group with key:" + key);
                return null;
            }
            catch (NumberFormatException e){
                System.out.println("Error parsing the course id.");
                return null;
            }


        }

        data = new CourseDataHelper(email);
        data.setCourseList(courseData);
        System.out.println(data.generateJSON());
        return data;
    }




    /**
     * Retrieves a key pointing to forum using the email address of user who created the forum, the course name, and the course section.
     * @author Karl Jean-Brice
     * @param instructor_email the email address of the user who created the forum
     * @param course the course name associated with the email address
     * @param section the course section associated with the email address
     * @return null if no forums are found for the user. Otherwise, an object containing a list forums the user is participating in.
     */
    public static Key getForumKey(String instructor_email, String course, String section){

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();


        Query.Filter email_filter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, instructor_email);
        Query.Filter course_filter = new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, course);
        Query.Filter section_filter = new Query.FilterPredicate("section", Query.FilterOperator.EQUAL, section);
        Query.CompositeFilter composite_filter = Query.CompositeFilterOperator.and(email_filter,course_filter,section_filter);

        Query q = new Query("Course").setFilter(composite_filter);
        PreparedQuery pq = datastore.prepare(q);
        int course_size = pq.asList(FetchOptions.Builder.withDefaults()).size();
        if(course_size != 1){
            return null;
        }

        long course_id = (long)pq.asList(FetchOptions.Builder.withDefaults()).get(0).getProperty("id");
        Query.Filter course_id_filter = new Query.FilterPredicate("course_id", Query.FilterOperator.EQUAL, course_id);
        q = new Query("Forum").setFilter(course_id_filter);
        pq = datastore.prepare(q);
        int forum_size = pq.asList(FetchOptions.Builder.withDefaults()).size();
        if (forum_size != 1) {
            return null;
        }

        if(forum_size > 1){
            return null;
        }

        Entity forum = pq.asIterable().iterator().next();
        System.out.println(forum.getKey());

        return forum.getKey();
    }

    /**
     * Changes the username of a user on a specific forum.
     * @author Karl Jean-Brice
     * @param forumkey the key of the forum containing the email address
     * @param email the email address of the user requesting the username change.
     * @param username the new username
     * @return 11(ALREADY_ACTIVE) if the username is already taken on the forum
     *         2 (NOT ENROLLED) if user is not currently enrolled in the course.
     *         7 (DATABASE_ERROR) Multiple or no entries are found in the datastore for the requested parameters
     *         0 (SUCCESS) the username was successfully changed for the user.
     */
    public static int updateUsername(Key forumkey, String email, String username){

        Entity user;
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        if(username == null || username.trim().length() == 0 || username.trim().length() < 3){
            return DiscussionBoardUtils.INVALID_LENGTH;
        }

        Query.Filter forumkey_filter = new Query.FilterPredicate("forum_key", Query.FilterOperator.EQUAL, forumkey);
        Query.Filter username_filter = new Query.FilterPredicate("username", Query.FilterOperator.EQUAL, username.trim());
        Query.Filter not_email_filter = new Query.FilterPredicate("email", Query.FilterOperator.NOT_EQUAL, email);

        Query.CompositeFilter new_username_filter = Query.CompositeFilterOperator.and(forumkey_filter,username_filter,not_email_filter);
        Query q = new Query("Enrollment").setFilter(new_username_filter);
        PreparedQuery pq = datastore.prepare(q);

        int list_size = pq.asList(FetchOptions.Builder.withDefaults()).size();
        if(list_size > 0){
            return DiscussionBoardUtils.ALREADY_ACTIVE;
        }

        Query.Filter email_filter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email);
        Query.CompositeFilter composite_filter = Query.CompositeFilterOperator.and(forumkey_filter,email_filter);

        q = new Query("Enrollment").setFilter(composite_filter);
        pq = datastore.prepare(q);

        int enroll_email_size = pq.asList(FetchOptions.Builder.withDefaults()).size();
        if(enroll_email_size == 0){
            return DiscussionBoardUtils.NOT_ENROLLED;
        }

        if(enroll_email_size > 1){
            return DiscussionBoardUtils.DATABASE_ERROR;
        }

        user = pq.asIterable().iterator().next();
        user.setProperty("username",username);
        user.setProperty("u_set",1);
        datastore.put(user);
        return DiscussionBoardUtils.SUCCESS;
    }




    /**
     * Creates dummy data for the forum in the datastore.
     * @author Karl Jean-Brice
     */
    public static void createDummyDiscussionData() {


        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("Dummydata");
        PreparedQuery pq = datastore.prepare(q);
        int list_size = pq.asList(FetchOptions.Builder.withDefaults()).size();
        if (list_size > 0) {
            return;
        }




        User user = GenUtils.getActiveUser();

        if(user == null){
            return;
        }

        if(!user.getEmail().equalsIgnoreCase("308.JeanBrice.Karl@gmail.com")){
            return;
        }

        String instructor_email = "308.JeanBrice.Karl@gmail.com";
        String course_name = "cse114";
        String section = "L01";
        GenUtils.createInstructor(instructor_email);


        KeyRange kr = datastore.allocateIds("Course",1);
        Key key = kr.getEnd();

        SecureRandom random = new SecureRandom();
        String course_code = new BigInteger(30, random).toString(32) + key.getId();

        Entity e = new Entity("Course");
        e.setProperty("name",course_name);
        e.setProperty("course_code",course_code);
        e.setProperty("section",section);
        e.setProperty("email",user.getEmail());
        e.setProperty("id",key.getId());
        datastore.put(e);

        int status = DiscussionBoardController.createDiscussionBoard(key.getId());
        if(status != DiscussionBoardUtils.SUCCESS){
            return;
        }





        Key forum_key = DiscussionBoardUtils.getForumKey(instructor_email,course_name,section);
        if(forum_key == null){
            return;
        }

        ArrayList<Object> obj = GenUtils.getCourseID(instructor_email,course_name,section);
        if(obj == null){
            return;
        }



        createDummyPost(course_name,instructor_email,instructor_email,"Lorem ipsum dolor sit amet. ","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris et sapien eros. Aliquam metus dolor. ",section);

        DiscussionBoardUtils.updateUsername(forum_key,instructor_email,"Ted Wilson");
        GenUtils.enrollUser(forum_key,"student1@example.com","Jake Harrison", 0,(long)obj.get(0));
        GenUtils.enrollUser(forum_key,"student2@example.com","Brian Tanner ", 0,(long)obj.get(0));
        GenUtils.enrollUser(forum_key,"student3@example.com","Emily Roberts", 0,(long)obj.get(0));
        createDummyPost(course_name,instructor_email,instructor_email,"Lorem ipsum dolor sit amet, consectetur. ","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce turpis erat, accumsan nec magna eu. ",section);
        createDummyComment(course_name,instructor_email,"student2@example.com"," Duis non laoreet enim, a dignissim tortor. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed non libero non leo pulvinar cursus quis id metus. Donec tristique venenatis eleifend. Vestibulum hendrerit finibus arcu et ornare. Cras ut mi non diam ornare feugiat. In facilisis nulla nec ante hendrerit, eu pharetra dolor malesuada. Donec quis gravida ligula. Duis purus lorem, ornare ac libero quis, congue blandit risus. Ut commodo a turpis id viverra. Curabitur aliquet, purus quis sagittis feugiat, est nisl sollicitudin urna, a fringilla nunc neque eget diam. ",section);
        createDummyComment(course_name,instructor_email,"student1@example.com", "Praesent turpis risus, varius sed enim quis, pulvinar pellentesque magna. Ut fringilla nulla a orci tempor iaculis at non erat. Cras enim quam, ornare sed fermentum vel, iaculis ac tellus. Cras pharetra metus libero, ut varius ligula commodo ac. Aliquam augue sapien, sodales vel sollicitudin nec, porta eu nibh. Curabitur eget porta est, eu semper lectus. Cras et diam quis velit laoreet posuere. Integer ac dolor lorem. Aenean elementum porta velit, et pharetra ante molestie nec. Donec rutrum velit vel lorem semper, pharetra efficitur risus pharetra. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nulla facilisi. Quisque eu molestie justo. Fusce lacinia magna consectetur lorem faucibus porta. Pellentesque luctus justo in elit consectetur, quis volutpat ligula mattis. ",section);


        Entity dummyData = new Entity("Dummydata");
        dummyData.setProperty("set",1);
        datastore.put(dummyData);

    }


    public static int createDummyPost(String course, String instructor_email, String post_email, String post_title, String post_content,String section) {


        if (instructor_email == null || course == null || post_title == null || post_content == null
                || course.trim().length() == 0 || instructor_email.trim().length() == 0 ||
                post_title.trim().length() == 0 || post_content.trim().length() == 0) {
            return DiscussionBoardUtils.EMPTY_PARAMETERS;
        }

        Key forumkey = DiscussionBoardUtils.getForumKey(instructor_email.trim(), course.trim(),section.trim());
        if (forumkey == null) {
            return DiscussionBoardUtils.BOARD_NOT_FOUND;
        }

        int enrollment_status = DiscussionBoardUtils.isEnrolled(forumkey, post_email);
        switch (enrollment_status) {
            case DiscussionBoardUtils.NOT_ENROLLED:
                return DiscussionBoardUtils.NOT_ENROLLED;
            case DiscussionBoardUtils.DUPLICATE_ENTRY:
                return DiscussionBoardUtils.DATABASE_ERROR;
            case DiscussionBoardUtils.ENROLLED:
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

                ArrayList<Object> obj = GenUtils.getCourseID(instructor_email,course,section);
                if(obj == null){
                    return DiscussionBoardUtils.DATABASE_ERROR;
                }

                Entity user = new Entity("Post");
                user.setProperty("title", post_title);
                user.setProperty("content", post_content);
                user.setProperty("email", post_email);
                user.setProperty("forum_id", forumkey.getId());
                user.setProperty("course_id", obj.get(0));
                user.setProperty("timestamp",new Date().getTime());
                datastore.put(user);
                return DiscussionBoardUtils.SUCCESS;
        }
        return DiscussionBoardUtils.DATABASE_ERROR;
    }

    public static void createDummyComment(String course, String instructor_email, String comment_email, String comment_content,String section)  {


        if (instructor_email == null || course == null || comment_content == null || comment_email == null
                || course.trim().length() == 0 || instructor_email.trim().length() == 0 ||
                comment_content.trim().length() == 0 || comment_email.trim().length() == 0) {

            return;
        }


        Key forumkey = DiscussionBoardUtils.getForumKey(instructor_email.trim(), course.trim(),section.trim());
        if (forumkey == null) {
            return;
        }



        int enrollment_status = DiscussionBoardUtils.isEnrolled(forumkey, comment_email);
        switch (enrollment_status) {
            case DiscussionBoardUtils.NOT_ENROLLED:
                break;
            case DiscussionBoardUtils.DUPLICATE_ENTRY:
                break;
            case DiscussionBoardUtils.ENROLLED:
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

                ArrayList<Object> obj = GenUtils.getCourseID(instructor_email,course,section);
                if(obj == null){
                    return;
                }

                Query.Filter forumid_filter = new Query.FilterPredicate("forum_id", Query.FilterOperator.EQUAL, forumkey.getId());

                PostData postData;
                Query q = new Query("Post").setFilter(forumid_filter);
                PreparedQuery pq = datastore.prepare(q);

                List<Entity> post_list = pq.asList(FetchOptions.Builder.withDefaults());
                int post_list_size = post_list.size();
                for (int i = 0; i < post_list_size; i++) {
                    Entity post = post_list.get(i);
                    Entity user = new Entity("Comment");
                    user.setProperty("postkey", post.getKey().getId());
                    user.setProperty("content", comment_content);
                    user.setProperty("email", comment_email);
                    user.setProperty("forum_id", forumkey.getId());
                    user.setProperty("course_id", obj.get(0));
                    user.setProperty("timestamp",new Date().getTime());
                    datastore.put(user);
                }
                break;
        }
    }



}
