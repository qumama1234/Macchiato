package com.macchiato.controllers.teachercontroller;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.macchiato.utility.GenUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Xiangbin on 5/5/2017.
 * this function delete the question from the database
 */
@Controller
public class DeleteQuestion {
    /**
     *The function will use  request to get the Question key
     *  and And find it in the question database
     *  delete this course from the database
     * @param  request  The servlet container to get the data from front end
     * @param  response  The servlet container to sent to the data from front end
     */
    @RequestMapping(value="/deleteQuestion.htm", method = RequestMethod.POST)
    public void addQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User active_user = GenUtils.getActiveUser();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();


        if (active_user.getEmail() == null ) {
            System.out.println("active_user is null");
        }
        else{
            String questionKey=request.getParameter("questionKey");
            System.out.println("Delete the Question :"+questionKey);
            Query.Filter questionKey_filter = new Query.FilterPredicate("questionKey", Query.FilterOperator.EQUAL, questionKey);
            Query q = new Query("Question").setFilter(questionKey_filter);
            PreparedQuery pq = datastore.prepare(q);
            int numberOfQuestion = pq.asList(FetchOptions.Builder.withDefaults()).size();
            System.out.print("NUmber of quESITON I FOUND "+numberOfQuestion);
            if (numberOfQuestion !=1) {
                System.out.println("There is no question to be found");
            } else {
                for (Entity result : pq.asIterable()) {
                    datastore.delete(result.getKey());
                }
            }
        }
    }



}
