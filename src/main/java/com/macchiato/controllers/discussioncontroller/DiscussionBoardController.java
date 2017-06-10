package com.macchiato.controllers.discussioncontroller;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;

import com.macchiato.general.discussiondata.*;
import com.macchiato.general.generaldata.CourseDataHelper;
import com.macchiato.general.generaldata.EnrollmentData;
import com.macchiato.utility.DiscussionBoardUtils;
import com.macchiato.utility.GenUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Karl on 3/28/2017.
 */
@Controller
public class DiscussionBoardController {

    public static int createDiscussionBoard(long course_id) {
        final int STUDENT = 0;
        final int NOT_LOGGED_IN = 1;
        final int INSTRUCTOR = 2;

        int status = (int) (GenUtils.checkCredentials().get(0));
        switch (status) {
            case GenUtils.STUDENT:
                return DiscussionBoardUtils.NO_ACCESS;
            case GenUtils.NOT_LOGGED_IN:
                return DiscussionBoardUtils.NOT_LOGGED_IN;
            case GenUtils.INSTRUCTOR:
                User user = GenUtils.getActiveUser();
                if (user == null) {
                    return DiscussionBoardUtils.NOT_LOGGED_IN;
                }
                String email = user.getEmail();
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                Key key = new KeyFactory.Builder("User", email).getKey();
                Query.Filter user_key_filter = new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, key);

                Query q = new Query("User").setFilter(user_key_filter);
                PreparedQuery pq = datastore.prepare(q);

                int user_size = pq.asList(FetchOptions.Builder.withDefaults()).size();
                if (user_size != 1) {
                    return DiscussionBoardUtils.DATABASE_ERROR;
                }


                Query.Filter course_id_filter = new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, course_id);
                q = new Query("Course").setFilter(course_id_filter);
                pq = datastore.prepare(q);
                int course_size = pq.asList(FetchOptions.Builder.withDefaults()).size();
                if (course_size != 1) {
                    return DiscussionBoardUtils.DATABASE_ERROR;
                }

                String course_name = (String)pq.asList(FetchOptions.Builder.withDefaults()).get(0).getProperty("name");
                String section = (String)pq.asList(FetchOptions.Builder.withDefaults()).get(0).getProperty("section");
                course_id_filter = new Query.FilterPredicate("course_id", Query.FilterOperator.EQUAL, course_id);
                q = new Query("Forum").setFilter(course_id_filter);
                pq = datastore.prepare(q);
                int group_size = pq.asList(FetchOptions.Builder.withDefaults()).size();
                if (group_size != 0) {
                    return DiscussionBoardUtils.DATABASE_ERROR;
                }


                KeyRange kr = datastore.allocateIds("Forum",1);
                key = kr.getEnd();
                Entity forum = new Entity("Forum");
                System.out.println("forum_key" + forum.getKey());
                forum.setProperty("email",email);
                forum.setProperty("course_name",course_name);
                forum.setProperty("section",section);
                forum.setProperty("course_id",course_id);
                forum.setProperty("id",key.getId());
                datastore.put(forum);

                GenUtils.enrollUser(forum.getKey(), email,"", GenUtils.INSTRUCTOR,course_id);
                return DiscussionBoardUtils.SUCCESS;
        }

        return DiscussionBoardUtils.FAILURE;

    }

    @RequestMapping(value = "getforumlist.htm" ,method = RequestMethod.GET)
    public void getForumList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        User active_user = GenUtils.getActiveUser();
        if(active_user == null){
            out.println("{}");
            return;
        }

        CourseDataHelper data = DiscussionBoardUtils.retrieveForumList(active_user.getEmail());
        if(data == null){
            out.println("{}");
        }
        else {
            out.println(data.generateJSON());
        }

    }


    @RequestMapping(value = "viewtest.htm", produces = "text/html;charset=UTF-8" ,method = RequestMethod.GET)
    public String viewTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int i = 5;
        return "discussionboard";
    }



    @RequestMapping(value = "discussion_addpost.htm", method = RequestMethod.GET)
    public void addPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String instructor_email = request.getParameter("i_email");
        String course = request.getParameter("course");
        String section = request.getParameter("section");
        String post_title = request.getParameter("title");
        String post_content = request.getParameter("content");


        if (instructor_email == null || course == null || section == null ||post_title == null || post_content == null
                || course.trim().length() == 0 || section.trim().length() == 0 || instructor_email.trim().length() == 0 ||
                post_title.trim().length() == 0 || post_content.trim().length() == 0) {
            out.println(DiscussionBoardUtils.EMPTY_PARAMETERS);
            return;
        }

        Key forumkey = DiscussionBoardUtils.getForumKey(instructor_email.trim(), course.trim(),section.trim());
        if (forumkey == null) {
            out.println(DiscussionBoardUtils.BOARD_NOT_FOUND);
            return;
        }

        User active_user = GenUtils.getActiveUser();
        if (active_user == null) {
            out.println(DiscussionBoardUtils.NOT_LOGGED_IN);
            return;
        }

        String email = active_user.getEmail();
        //DiscussionBoardUtils.enrollUser(groupkey,"kjeanbri@yahoo.com","Jake Wilson ", 0);
        //DiscussionBoardUtils.updateUsername(groupkey,"kjeanbri@yahoo.com", "Karl Wilson");
        //DiscussionBoardUtils.updateUsername(groupkey,email,"Karl Wilson");
        //DiscussionBoardUtils.updateUsername(groupkey,email,"Paul Fodor");
        int enrollment_status = DiscussionBoardUtils.isEnrolled(forumkey, email);
        switch (enrollment_status) {
            case DiscussionBoardUtils.NOT_ENROLLED:
                out.println(DiscussionBoardUtils.NOT_ENROLLED);
                return;
            case DiscussionBoardUtils.DUPLICATE_ENTRY:
                out.println(DiscussionBoardUtils.DATABASE_ERROR);
                return;
            case DiscussionBoardUtils.ENROLLED:
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

                ArrayList<Object> obj = GenUtils.getCourseID(instructor_email,course,section);
                if(obj == null){
                    out.println(DiscussionBoardUtils.FAILURE);
                    return;
                }

                Entity user = new Entity("Post");
                user.setProperty("title", post_title);
                user.setProperty("content", post_content);
                user.setProperty("email", email);
                user.setProperty("forum_id", forumkey.getId());
                user.setProperty("course_id", obj.get(0));
                user.setProperty("timestamp",new Date().getTime());
                datastore.put(user);
                out.println(DiscussionBoardUtils.SUCCESS);
                break;
        }
    }


    @RequestMapping(value = "discussion_addcomment.htm", method = RequestMethod.GET)
    public void addComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();


        String instructor_email = request.getParameter("i_email");
        String course = request.getParameter("course");
        String section = request.getParameter("section");
        String post_id = request.getParameter("post_id");
        String comment_content = request.getParameter("content");


        if (instructor_email == null || course == null || section == null ||comment_content == null || post_id == null
                || course.trim().length() == 0 || section.trim().length() == 0 ||instructor_email.trim().length() == 0 ||
                comment_content.trim().length() == 0 || post_id.trim().length() == 0) {
            out.println(DiscussionBoardUtils.EMPTY_PARAMETERS);
            return;
        }

        Key forumkey = DiscussionBoardUtils.getForumKey(instructor_email.trim(), course.trim(),section.trim());
        if (forumkey == null) {
            out.println(DiscussionBoardUtils.BOARD_NOT_FOUND);
            return;
        }

        User active_user = GenUtils.getActiveUser();
        if (active_user == null) {
            out.println(DiscussionBoardUtils.NOT_LOGGED_IN);
            return;
        }

        String email = active_user.getEmail();
        int enrollment_status = DiscussionBoardUtils.isEnrolled(forumkey, email);
        switch (enrollment_status) {
            case DiscussionBoardUtils.NOT_ENROLLED:
                out.println(DiscussionBoardUtils.NOT_LOGGED_IN);
                break;
            case DiscussionBoardUtils.DUPLICATE_ENTRY:
                out.println(DiscussionBoardUtils.DUPLICATE_ENTRY);
                break;
            case DiscussionBoardUtils.ENROLLED:
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                long id = Long.parseLong(post_id);
                Key post_key = KeyFactory.createKey("Post", id);

                try {
                    Entity valid_post = datastore.get(post_key);
                } catch (EntityNotFoundException e) {
                    out.println(DiscussionBoardUtils.POST_NOT_FOUND);
                    return;
                }

                ArrayList<Object> obj = GenUtils.getCourseID(instructor_email,course,section);
                if(obj == null){
                    out.println(DiscussionBoardUtils.FAILURE);
                    return;
                }

                Entity user = new Entity("Comment");
                user.setProperty("postkey", post_key.getId());
                user.setProperty("content", comment_content);
                user.setProperty("email", email);
                user.setProperty("forum_id", forumkey.getId());
                user.setProperty("course_id", obj.get(0));
                user.setProperty("timestamp",new Date().getTime());
                datastore.put(user);
                out.println(DiscussionBoardUtils.SUCCESS);
                break;
        }
    }

    @RequestMapping(value = "discussion_deletepost.htm", method = RequestMethod.GET)
    public void deletePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String instructor_email = request.getParameter("i_email");
        String course = request.getParameter("course");
        String section = request.getParameter("section");
        String post_id = request.getParameter("post_id");



        if (instructor_email == null || course == null || post_id == null || section == null) {
            out.println(DiscussionBoardUtils.EMPTY_PARAMETERS);
            return;
        }

        Key forumkey = DiscussionBoardUtils.getForumKey(instructor_email.trim(), course.trim(),section.trim());
        if (forumkey == null) {
            out.println(DiscussionBoardUtils.BOARD_NOT_FOUND);
            return;
        }

        User active_user = GenUtils.getActiveUser();
        if (active_user == null) {
            out.println(DiscussionBoardUtils.NOT_LOGGED_IN);
            return;
        }

        String email = active_user.getEmail();
        int enrollment_status = DiscussionBoardUtils.isEnrolled(forumkey, email);
        switch (enrollment_status) {
            case DiscussionBoardUtils.NOT_ENROLLED:
                out.println(DiscussionBoardUtils.NOT_ENROLLED);
                return;
            case DiscussionBoardUtils.DUPLICATE_ENTRY:
                out.println(DiscussionBoardUtils.DATABASE_ERROR);
                return;
            case DiscussionBoardUtils.ENROLLED:
                EnrollmentData user = GenUtils.getEnrolledUser(email,course,forumkey);
                if(user.getAccess().equals("INSTRUCTOR")){
                    try {
                        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                        long id = Long.parseLong(post_id);
                        Key post_key = KeyFactory.createKey("Post", id);

                        Entity valid_post = datastore.get(post_key);
                        Query.Filter postid_filter = new Query.FilterPredicate("postkey", Query.FilterOperator.EQUAL, id);
                        Query q = new Query("Comment").setFilter(postid_filter);
                        PreparedQuery pq = datastore.prepare(q);

                        List<Entity> comment_list = pq.asList(FetchOptions.Builder.withDefaults());
                        int comment_list_size = comment_list.size();
                        for (int i = 0; i < comment_list_size; i++) {
                            datastore.delete(comment_list.get(i).getKey());
                        }
                        datastore.delete(post_key);
                    } catch (EntityNotFoundException e) {
                        out.println(DiscussionBoardUtils.POST_NOT_FOUND);
                        return;
                    }
                    out.println(DiscussionBoardUtils.SUCCESS);
                }
                else{
                    out.println(DiscussionBoardUtils.NO_ACCESS);
                }
                break;
        }
        return;
    }


    @RequestMapping(value = "discussion_deletecomment.htm", method = RequestMethod.GET)
    public void deleteComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String instructor_email = request.getParameter("i_email");
        String course = request.getParameter("course");
        String comment_id = request.getParameter("comment_id");
        String section = request.getParameter("section");


        if (instructor_email == null || course == null || comment_id == null) {
            out.println(DiscussionBoardUtils.EMPTY_PARAMETERS);
            return;
        }

        Key forumkey = DiscussionBoardUtils.getForumKey(instructor_email.trim(), course.trim(),section.trim());
        if (forumkey == null) {
            out.println(DiscussionBoardUtils.BOARD_NOT_FOUND);
            return;
        }

        User active_user = GenUtils.getActiveUser();
        if (active_user == null) {
            out.println(DiscussionBoardUtils.NOT_LOGGED_IN);
            return;
        }

        String email = active_user.getEmail();
        int enrollment_status = DiscussionBoardUtils.isEnrolled(forumkey, email);
        switch (enrollment_status) {
            case DiscussionBoardUtils.NOT_ENROLLED:
                out.println(DiscussionBoardUtils.NOT_ENROLLED);
                return;
            case DiscussionBoardUtils.DUPLICATE_ENTRY:
                out.println(DiscussionBoardUtils.DATABASE_ERROR);
                return;
            case DiscussionBoardUtils.ENROLLED:
                EnrollmentData user = GenUtils.getEnrolledUser(email,course,forumkey);
                if(user.getAccess().equals("INSTRUCTOR")){
                    try {
                        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                        long id = Long.parseLong(comment_id);
                        Key comment_key = KeyFactory.createKey("Comment", id);

                        Entity valid_comment = datastore.get(comment_key);
                        datastore.delete(comment_key);
                    } catch (EntityNotFoundException | NumberFormatException e) {
                        out.println(DiscussionBoardUtils.POST_NOT_FOUND);
                        return;
                    }
                    out.println(DiscussionBoardUtils.SUCCESS);
                }
                else{
                    out.println(DiscussionBoardUtils.NO_ACCESS);
                }
                break;
        }
        return;
    }


    @RequestMapping(value = "discussion_editpost.htm", method = RequestMethod.GET)
    public void editPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String instructor_email = request.getParameter("i_email");
        String course = request.getParameter("course");
        String section = request.getParameter("section");
        String post_id = request.getParameter("post_id");
        String post_title = request.getParameter("title");
        String post_content = request.getParameter("content");


        if (instructor_email == null || course == null || post_id == null || post_title == null || section == null || post_content == null
                || instructor_email.trim().isEmpty() || course.trim().isEmpty() || section.trim().isEmpty() || post_id.trim().isEmpty()
                || post_title.trim().isEmpty() || post_content.trim().isEmpty() ) {
            out.println(DiscussionBoardUtils.EMPTY_PARAMETERS);
            return;
        }

        Key forumkey = DiscussionBoardUtils.getForumKey(instructor_email.trim(), course.trim(),section.trim());
        if (forumkey == null) {
            out.println(DiscussionBoardUtils.BOARD_NOT_FOUND);
            return;
        }

        User active_user = GenUtils.getActiveUser();
        if (active_user == null) {
            out.println(DiscussionBoardUtils.NOT_LOGGED_IN);
            return;
        }

        String email = active_user.getEmail();
        int enrollment_status = DiscussionBoardUtils.isEnrolled(forumkey, email);
        switch (enrollment_status) {
            case DiscussionBoardUtils.NOT_ENROLLED:
                out.println(DiscussionBoardUtils.NOT_ENROLLED);
                return;
            case DiscussionBoardUtils.DUPLICATE_ENTRY:
                out.println(DiscussionBoardUtils.DATABASE_ERROR);
                return;
            case DiscussionBoardUtils.ENROLLED:
                EnrollmentData user = GenUtils.getEnrolledUser(email,course,forumkey);
                try {
                    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                    long id = Long.parseLong(post_id);
                    Key post_key = KeyFactory.createKey("Post", id);

                    Entity valid_post = datastore.get(post_key);
                    String post_email = (String)valid_post.getProperty("email");
                    if(user.getAccess().equals("INSTRUCTOR") || post_email.equalsIgnoreCase(email)){
                        valid_post.setProperty("title",post_title);
                        valid_post.setProperty("content",post_content);
                        datastore.put(valid_post);
                        out.println(DiscussionBoardUtils.SUCCESS);
                    }
                    else{
                        out.println(DiscussionBoardUtils.NO_ACCESS);
                    }
                } catch (EntityNotFoundException | NumberFormatException e) {
                    out.println(DiscussionBoardUtils.POST_NOT_FOUND);
                    return;
                }
                break;
        }
        return;
    }


    @RequestMapping(value = "discussion_editcomment.htm", method = RequestMethod.GET)
    public void editComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String instructor_email = request.getParameter("i_email");
        String course = request.getParameter("course");
        String section = request.getParameter("section");
        String comment_id = request.getParameter("comment_id");
        String comment_content = request.getParameter("content");


        if (instructor_email == null || course == null || comment_id == null || section == null||comment_content == null
                || instructor_email.trim().isEmpty() || course.trim().isEmpty() || section.trim().isEmpty() ||comment_id.trim().isEmpty()
                || comment_content.trim().isEmpty() ) {
            out.println(DiscussionBoardUtils.EMPTY_PARAMETERS);
            return;
        }

        Key forumkey = DiscussionBoardUtils.getForumKey(instructor_email.trim(), course.trim(),section.trim());
        if (forumkey == null) {
            out.println(DiscussionBoardUtils.BOARD_NOT_FOUND);
            return;
        }

        User active_user = GenUtils.getActiveUser();
        if (active_user == null) {
            out.println(DiscussionBoardUtils.NOT_LOGGED_IN);
            return;
        }

        String email = active_user.getEmail();
        int enrollment_status = DiscussionBoardUtils.isEnrolled(forumkey, email);
        switch (enrollment_status) {
            case DiscussionBoardUtils.NOT_ENROLLED:
                out.println(DiscussionBoardUtils.NOT_ENROLLED);
                return;
            case DiscussionBoardUtils.DUPLICATE_ENTRY:
                out.println(DiscussionBoardUtils.DATABASE_ERROR);
                return;
            case DiscussionBoardUtils.ENROLLED:
                EnrollmentData user = GenUtils.getEnrolledUser(email,course,forumkey);
                try {
                    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                    long id = Long.parseLong(comment_id);
                    Key comment_key = KeyFactory.createKey("Comment", id);

                    Entity valid_comment = datastore.get(comment_key);
                    String post_email = (String)valid_comment.getProperty("email");
                    if(user.getAccess().equals("INSTRUCTOR") || post_email.equalsIgnoreCase(email)){
                        valid_comment.setProperty("content",comment_content);
                        datastore.put(valid_comment);
                        out.println(DiscussionBoardUtils.SUCCESS);
                    }
                    else{
                        out.println(DiscussionBoardUtils.NO_ACCESS);
                    }
                } catch (EntityNotFoundException | NumberFormatException e) {
                    out.println(DiscussionBoardUtils.POST_NOT_FOUND);
                    return;
                }
                break;
        }
        return;
    }


    @RequestMapping(value = "discussion_voterequest.htm", method = RequestMethod.GET)
    public void voteRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String instructor_email = request.getParameter("i_email");
        String course = request.getParameter("course");
        String section = request.getParameter("section");
        String request_for = request.getParameter("for");
        String request_type = request.getParameter("type");
        String request_id = request.getParameter("id");


        if (instructor_email == null || course == null || request_id == null || request_type == null || request_for == null || section == null
                || instructor_email.trim().isEmpty() || course.trim().isEmpty() || section.trim().isEmpty() ||request_id.trim().isEmpty() || request_type.trim().isEmpty() || request_for.trim().isEmpty()) {
            out.println(DiscussionBoardUtils.EMPTY_PARAMETERS);
            return;
        }

        Key forumkey = DiscussionBoardUtils.getForumKey(instructor_email.trim(), course.trim(),section.trim());
        if (forumkey == null) {
            out.println(DiscussionBoardUtils.BOARD_NOT_FOUND);
            return;
        }

        User active_user = GenUtils.getActiveUser();
        if (active_user == null) {
            out.println(DiscussionBoardUtils.NOT_LOGGED_IN);
            return;
        }

        ArrayList<Object> obj = GenUtils.getCourseID(instructor_email,course,section);
        if(obj == null){
            out.println(DiscussionBoardUtils.FAILURE);
            return;
        }

        String email = active_user.getEmail();
        int enrollment_status = DiscussionBoardUtils.isEnrolled(forumkey, email);
        switch (enrollment_status) {
            case DiscussionBoardUtils.NOT_ENROLLED:
                out.println(DiscussionBoardUtils.NOT_ENROLLED);
                return;
            case DiscussionBoardUtils.DUPLICATE_ENTRY:
                out.println(DiscussionBoardUtils.DATABASE_ERROR);
                return;
            case DiscussionBoardUtils.ENROLLED:
                EnrollmentData user = GenUtils.getEnrolledUser(email,course,forumkey);
                try {
                    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                    long id = Long.parseLong(request_id);

                    String property_name = null;
                    String entity_kind = null;

                    if(request_for.trim().equalsIgnoreCase("post")){
                        property_name = "postid";
                        entity_kind = "PostLikes";

                    }else if(request_for.trim().equalsIgnoreCase("comment")){
                        property_name = "commentid";
                        entity_kind = "CommentLikes";
                    }
                    else{
                        out.println(DiscussionBoardUtils.INVALID_ARGS);
                        return;
                    }

                    Query.Filter id_filter = new Query.FilterPredicate(property_name, Query.FilterOperator.EQUAL, id);
                    Query.Filter email_filter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email);
                    Query.CompositeFilter email_id_filter = Query.CompositeFilterOperator.and(id_filter,email_filter);
                    Query q = new Query(entity_kind).setFilter(email_id_filter);

                    PreparedQuery pq = datastore.prepare(q);
                    List<Entity> likes_list = pq.asList(FetchOptions.Builder.withDefaults());
                    int likes_list_size = likes_list.size();

                    if(likes_list_size == 0 && request_type.trim().equalsIgnoreCase("like")) {
                        Entity temp = new Entity(entity_kind);
                        temp.setProperty("email", email);
                        temp.setProperty(property_name, id);
                        temp.setProperty("course_id", obj.get(0));
                        datastore.put(temp);
                        out.println(DiscussionBoardUtils.SUCCESS);
                    }
                    else if(likes_list_size == 1 && request_type.trim().equalsIgnoreCase("dislike")){
                        Entity temp = likes_list.get(0);
                        datastore.delete(temp.getKey());
                        out.println(DiscussionBoardUtils.SUCCESS);
                    }else if(likes_list_size == 1 && request_type.trim().equalsIgnoreCase("like")){
                        out.println(DiscussionBoardUtils.DUPLICATE_ENTRY);
                    }
                    else if(likes_list_size == 0 && request_type.trim().equalsIgnoreCase("dislike")){
                        out.println(DiscussionBoardUtils.DUPLICATE_ENTRY);
                    }
                    else{
                        out.println(DiscussionBoardUtils.DATABASE_ERROR);
                    }

                } catch (NumberFormatException e) {
                    out.println(DiscussionBoardUtils.POST_NOT_FOUND);
                    return;
                }
                break;
        }
        return;
    }


    @RequestMapping(value = "discussion_changeusername.htm", method = RequestMethod.GET)
    public void changeUsername(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String instructor_email = request.getParameter("i_email");
        String course = request.getParameter("course");
        String section = request.getParameter("section");
        String username = request.getParameter("username");



        if (instructor_email == null || course == null || username == null  || section == null
                || instructor_email.trim().isEmpty() || course.trim().isEmpty() || section.trim().isEmpty() ||username.trim().isEmpty()) {
            out.println(DiscussionBoardUtils.EMPTY_PARAMETERS);
            return;
        }

        if(username.trim().length() < 3){
            out.println(DiscussionBoardUtils.INVALID_LENGTH);
            return;
        }

        Key forumkey = DiscussionBoardUtils.getForumKey(instructor_email.trim(), course.trim(),section.trim());
        if (forumkey == null) {
            out.println(DiscussionBoardUtils.BOARD_NOT_FOUND);
            return;
        }

        User active_user = GenUtils.getActiveUser();
        if (active_user == null) {
            out.println(DiscussionBoardUtils.NOT_LOGGED_IN);
            return;
        }

        String email = active_user.getEmail();
        int enrollment_status = DiscussionBoardUtils.isEnrolled(forumkey, email);
        switch (enrollment_status) {
            case DiscussionBoardUtils.NOT_ENROLLED:
                out.println(DiscussionBoardUtils.NOT_ENROLLED);
                return;
            case DiscussionBoardUtils.DUPLICATE_ENTRY:
                out.println(DiscussionBoardUtils.DATABASE_ERROR);
                return;
            case DiscussionBoardUtils.ENROLLED:
                int status = DiscussionBoardUtils.updateUsername(forumkey,email,username);
                out.println(status);
                return;
        }
        return;
    }











    @RequestMapping(value = "populate_discussionboard.htm", method = RequestMethod.GET)
    public void populateDiscussionBoard(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();



        String instructor_email = request.getParameter("i_email");
        String course = request.getParameter("course");
        String section = request.getParameter("section");

        if (instructor_email == null || course == null || section == null || section.trim().length() == 0 || course.trim().length() == 0 || instructor_email.trim().length() == 0) {
            out.println(DiscussionBoardUtils.EMPTY_PARAMETERS);
            return;
        }

        Key forumkey = DiscussionBoardUtils.getForumKey(instructor_email.trim(), course.trim(), section.trim());
        if (forumkey == null) {
            out.println(DiscussionBoardUtils.BOARD_NOT_FOUND);
            return;
        }

        User active_user = GenUtils.getActiveUser();
        if (active_user == null) {
            out.println(DiscussionBoardUtils.NOT_LOGGED_IN);
            return;
        }

        String email = active_user.getEmail();
        int enrollment_status = DiscussionBoardUtils.isEnrolled(forumkey, email);
        switch (enrollment_status) {
            case DiscussionBoardUtils.NOT_ENROLLED:
                out.println(DiscussionBoardUtils.NOT_ENROLLED);
                break;
            case DiscussionBoardUtils.DUPLICATE_ENTRY:
                out.println(DiscussionBoardUtils.DATABASE_ERROR);
                break;
            case DiscussionBoardUtils.ENROLLED:

                try {
                    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                    Entity forum = datastore.get(forumkey);

                    Query.Filter forumkey_filter = new Query.FilterPredicate("forum_key", Query.FilterOperator.EQUAL, forumkey);
                    Query q = new Query("Enrollment").setFilter(forumkey_filter);
                    PreparedQuery pq = datastore.prepare(q);

                    /*Retrieve enrollment list*/
                    List<Entity> enrollment_list = pq.asList(FetchOptions.Builder.withDefaults());
                    int enrollment_list_size = enrollment_list.size();


                    ArrayList<PostData> post_array_list = new ArrayList<>();

                    /*Retrieve data about the creator of the discussion group*/
                    EnrollmentData instructor_data = GenUtils.getEnrolledUser(instructor_email,course,forumkey);

                    /*Retrieve data about the user accessing the discussion board*/
                    EnrollmentData active_user_enrollment_data = GenUtils.getEnrolledUser(email,course,forumkey);

                    /*Retrieve Posts*/
                    PostData postData;
                    Query.Filter forumid_filter = new Query.FilterPredicate("forum_id", Query.FilterOperator.EQUAL, forumkey.getId());
                    q = new Query("Post").setFilter(forumid_filter);
                    q.addSort("timestamp", Query.SortDirection.DESCENDING);
                    pq = datastore.prepare(q);

                    List<Entity> post_list = pq.asList(FetchOptions.Builder.withDefaults());
                    int post_list_size = post_list.size();
                    int comment_list_size = 0;
                    for (int i = 0; i < post_list_size; i++) {
                        Entity post = post_list.get(i);
                        String post_email = (String) post.getProperty("email");
                        String post_title = (String) post.getProperty("title");
                        String post_content = (String) post.getProperty("content");
                        long post_id =  post.getKey().getId();

                        /*Retrieve details about the user that made this post*/
                        EnrollmentData post_user = GenUtils.getEnrolledUser(post_email,course,forumkey);

                        /*Retrieve the number of "likes" this post has.*/
                        Query.Filter postid_filter = new Query.FilterPredicate("postid", Query.FilterOperator.EQUAL, post_id);
                        q = new Query("PostLikes").setFilter(postid_filter);
                        pq = datastore.prepare(q);
                        int post_likes = pq.countEntities(FetchOptions.Builder.withDefaults());

                        /*Retrieve Comments*/
                        ArrayList<CommentData> comment_array_list = new ArrayList<>();
                        q = new Query("Comment");
                        q.addSort("timestamp", Query.SortDirection.ASCENDING);
                        pq = datastore.prepare(q);
                        List<Entity> comment_list = pq.asList(FetchOptions.Builder.withDefaults());
                        int c_list_size = comment_list.size();
                        for (int j = 0; j < c_list_size; j++) {
                            long comment_post_id = (long) comment_list.get(j).getProperty("postkey");
                            if (comment_post_id == post_id) {
                                comment_list_size = comment_list_size + 1;
                                String comment_email = (String) comment_list.get(j).getProperty("email");
                                long comment_id = comment_list.get(j).getKey().getId();
                                String comment_content = (String) comment_list.get(j).getProperty("content");

                                /*Retrieve details about the user that made this comment*/
                                EnrollmentData comment_user = GenUtils.getEnrolledUser(comment_email,course,forumkey);

                                /*Retrieve the number of "likes" this comment has.*/
                                Query.Filter commentid_filter = new Query.FilterPredicate("commentid", Query.FilterOperator.EQUAL, comment_id);
                                q = new Query("CommentLikes").setFilter(commentid_filter);
                                pq = datastore.prepare(q);
                                int comment_likes = pq.countEntities(FetchOptions.Builder.withDefaults());


                                CommentData temp_comment = new CommentData(comment_id, comment_user, comment_content, comment_likes);
                                temp_comment.determinePermissions(active_user_enrollment_data);
                                comment_array_list.add(temp_comment);
                            }
                        }


                        PostData temp_post = new PostData(post_id, post_user, post_title, post_content, post_likes, comment_array_list);
                        temp_post.determinePermissions(active_user_enrollment_data);
                        post_array_list.add(temp_post);
                    }


                    //Key groupID, String email, String course, String username, String access, ArrayList<PostData> postData
                    DiscussionBoardData discussionBoardData = new DiscussionBoardData(forumkey, enrollment_list_size,
                            post_list_size, comment_list_size,instructor_data, post_array_list,active_user_enrollment_data);

                    out.println(discussionBoardData.generateJSON());
                    System.out.println(discussionBoardData.generateJSON());


                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
                break;


        }


    }
}
