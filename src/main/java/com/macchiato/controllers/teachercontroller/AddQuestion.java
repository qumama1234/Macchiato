package com.macchiato.controllers.teachercontroller;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.macchiato.beans.QuestionBean;
import com.macchiato.utility.GenUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Xiangbin on 5/5/2017.
 * Edited by Raymond
 */
@Controller
public class AddQuestion {
    /**
     *The function will use  request to get the assignment key and get
     * all the information from the front end(inputed by user)
     * and add question in to this assignment
     * @param  request  The servlet container to get the data from front end
     * @param  response  The servlet container to sent to the data from front end
     */
    //this function add the new course to the database
    @RequestMapping(value="addQuestion.htm", method = RequestMethod.POST)
    public void addQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int i = 1;
        User active_user = GenUtils.getActiveUser();
        String instructor_email =active_user.getEmail();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        if (instructor_email == null ) {
            System.out.println("active_user is null");
        }
        else{
            String assignmentKey=request.getParameter("assignmentKey");
            System.out.println("Is this working " + assignmentKey);
            String problem=request.getParameter("problem");
            String solution=request.getParameter("solution");
            String teacherAnswer=request.getParameter("teacherAnswer");
            QuestionBean newQuestion=new QuestionBean();
            newQuestion.setProblem(problem);
            newQuestion.setSolution(solution);
            newQuestion.setTeacherAnswer(teacherAnswer);


            Query q = new Query("Question");
            PreparedQuery pq = datastore.prepare(q);
            System.out.println("pq as a list is" + pq.asList(FetchOptions.Builder.withDefaults()));
            int size = pq.asList(FetchOptions.Builder.withDefaults()).size();
            if(size != 0){
                i = 0;
                for (Entity e : pq.asList(FetchOptions.Builder.withDefaults())) {
                    if(Integer.parseInt((String)e.getProperty("questionId")) >= i && e.getProperty("assignmentKey").equals(assignmentKey)){
                        i = Integer.parseInt((String)e.getProperty("questionId"));
                        System.out.println("I is " + i);
                    }
                }
                i = i + 1;
                System.out.println(i);
            }


            Entity user = new Entity("Question");

            user.setProperty("problem",problem);
            user.setProperty("solution", solution);
            user.setProperty("assignmentKey",assignmentKey);
            user.setProperty("teacherAnswer",teacherAnswer);
            user.setProperty("questionId",Integer.toString(i));
            user.setProperty("student_answer","");
            datastore.put(user);
            user.setProperty("questionKey",user.getKey().toString());
            datastore.put(user);
            System.out.println("this is new key for this question: "+user.getKey().toString());
            System.out.print("From add: "+newQuestion.generateJSON());
            //out.println(newClass.generateJSON());
        }
    }


}
