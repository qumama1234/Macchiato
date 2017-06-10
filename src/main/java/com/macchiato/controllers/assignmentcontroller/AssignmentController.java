package com.macchiato.controllers.assignmentcontroller;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.hackerrank.api.client.ApiException;
import com.hackerrank.api.hackerrank.api.CheckerApi;
import com.hackerrank.api.hackerrank.model.Result;
import com.hackerrank.api.hackerrank.model.Submission;
import com.macchiato.beans.QuestionBean;
import com.macchiato.beans.QuestionInfoBean;
import com.macchiato.beans.QuestionListBean;
import com.macchiato.beans.QuestionInfoListBean;
import com.macchiato.utility.GenUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

/**
 * Created by Raymond on 4/20/2017.
 */

@Controller
/**
 * This controller will be used to map our urls to the frontend so we can activate our javascript
 * functions
 */
public class AssignmentController {

    /**
     * This method will be used to populate the question page with info
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "PopulateQues.htm", method = RequestMethod.GET)
    public void populateQuesRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        System.out.println(request.getSession().getAttribute("assignmentKey"));
        PrintWriter out = response.getWriter();
        User active_user = GenUtils.getActiveUser();
        String student_email = active_user.getEmail();
        if (student_email == null) {
            System.out.println("active_user is null");
        } else {
            QuestionListBean newList = findQuestions(request.getSession().getAttribute("assignmentKey").toString());
            System.out.println(newList.generateJSON());
            out.println(newList.generateJSON());
        }
    }

    /**
     * This method will used to help populate the student responses ot the question page
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "PopulateQuesInfo.htm", method = RequestMethod.GET)
    public void populateQuesInfoRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        User active_user = GenUtils.getActiveUser();
        String student_email = active_user.getEmail();
        if (student_email == null) {
            System.out.println("active_user is null");
        } else {
            QuestionInfoListBean newList = findQuestionsInfo(request.getSession().getAttribute("assignmentKey").toString(), student_email);
            System.out.println(newList.generateJSON());
            out.println(newList.generateJSON());
        }
    }

    /**
     * This method will be used to compile student code
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "Compile.htm", method = RequestMethod.POST)
    public void compileCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text");
        PrintWriter out = response.getWriter();
        String text = request.getParameter("text");
        String num = request.getParameter("num");
        String submitted = request.getParameter("submitted");
        System.out.println("submitted is" + submitted);
        System.out.println("Num is " + num);
        String solution = "";
        QuestionListBean newList = findQuestions(request.getSession().getAttribute("assignmentKey").toString());
        QuestionBean q = null;
        for (QuestionBean question : newList.getProblems()) {
            System.out.println("Question key right now is" + question.getQuestionKey());
            if (question.getQuestionKey().equals(num)) {
                solution = question.getSolution();
                q = question;
            }
        }
        if(submitted.equals("no")){
            updateQuestion(q.getQuestionKey(),text);
        }

        // System.out.println(text);
        String apiKey = "hackerrank|2458825-1355|a7001ed51bce45bd9f6cc1e4bf499ef05d8d4495";
        String source = text;
        Integer lang = new Integer(3);
        String testcases = "[\"" + StringEscapeUtils.escapeJava(solution) + "\"]";
        String format = "JSON";
        String callbackUrl = "";
        String wait = "true";

        try {
            CheckerApi checkerApi = new CheckerApi();
            Submission response1 = checkerApi.submission(apiKey, source, lang, testcases, format, callbackUrl, wait);
            Result answer = response1.getResult();
            System.out.println(answer);

            List<String> stdout = response1.getResult().getStdout();
            //   System.out.println(answer.getCompilemessage());
            String finmessage = answer.getCompilemessage();
            //   System.out.println("Compiler message is " + finmessage);
            //   System.out.println("User entered text is " + text);
            if (finmessage.equals("")) {
                if(submitted.equals("no")){
                    out.println(stdout.get(0).trim());
                }
                else{
                    System.out.println(stdout.get(0).trim());
                    System.out.println(solution);
                    if(stdout.get(0).trim().equals(solution)){
                        if(submitted.equals("yes")){
                            boolean checkPoint = updatePoint(q.getQuestionKey());
                            System.out.println("CheckPoint is " + checkPoint);
                            if(checkPoint == true){
                                out.print("correct");
                            }
                            else{
                                out.print("cheated");
                            }
                        }
                    }
                    else{
                        out.print("wrong");
                    }
                }

            } else {
                out.println(finmessage);
            }

        } catch (Exception e) {
            System.out.printf("ApiException caught: %s\n", e.getMessage());
        }
    }

    @RequestMapping(value = "SubmitSol.htm", method = RequestMethod.GET)
    public void submitSolution(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text");
        PrintWriter out = response.getWriter();
        String questionKey = request.getParameter("questionKey");
        updateComplete(questionKey);
        out.print("cheated");

    }


    /**
     * Helper method to store dummy data for testing purposes
     */
    public void useDummyData() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity exam1 = new Entity("assignment");
        exam1.setProperty("crsCode", "1234");
        exam1.setProperty("assignmentKey", "turtle");
        datastore.put(exam1);
        Entity question1 = new Entity("question");
        question1.setProperty("problem", "Initialize an integer i with the value 1");
        question1.setProperty("solution", "int i = 1;");
        question1.setProperty("studentans", "");
        question1.setProperty("assignmentKey", "turtle");
        question1.setProperty("questionnum", "1");
        datastore.put(question1);
        Entity question2 = new Entity("question");
        question2.setProperty("problem", "Initialize an integer i with the value 2");
        question2.setProperty("solution", "int i = 2;");
        question2.setProperty("studentans", "");
        question2.setProperty("assignmentKey", "turtle");
        question2.setProperty("questionnum", "2");
        datastore.put(question2);
    }

    /**
     * This method will be used to find Questions in the datastore
     *
     * @param assignmentKey
     * @return QuestionListBean
     */
    public QuestionListBean findQuestions(String assignmentKey) {
        QuestionListBean newList = new QuestionListBean();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter assignment_filter = new Query.FilterPredicate("assignmentKey", Query.FilterOperator.EQUAL, assignmentKey);
        Query q = new Query("Question").setFilter(assignment_filter).addSort("questionId", Query.SortDirection.ASCENDING);
        PreparedQuery pq = datastore.prepare(q);
        System.out.println("pq as a list is" + pq.asList(FetchOptions.Builder.withDefaults()));
        for (Entity e : pq.asList(FetchOptions.Builder.withDefaults())) {
            QuestionBean question = new QuestionBean((String) e.getProperty("problem"), (String) e.getProperty("solution"), (String) e.getProperty("questionId"), (String) e.getProperty("student_answer"));
            question.setAssignmentKey(assignmentKey);
            question.setQuestionKey((String) e.getProperty("questionKey"));
            question.setTeacherAnswer((String) e.getProperty("teacherAnswer"));
            System.out.println("findQuestions key is" + question.getQuestionKey());
            newList.getProblems().add(question);
        }
        return newList;
    }

    /**
     * This method will be used to find QuestionsInfo in the datastore if we dont have it we need to create it
     *
     * @param assignmentKey
     * @return QuestionListBean
     */
    public QuestionInfoListBean findQuestionsInfo(String assignmentKey, String email_address) {
        int i = 1;
        QuestionInfoListBean newList = new QuestionInfoListBean();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter assignment_filter = new Query.FilterPredicate("assignmentKey", Query.FilterOperator.EQUAL, assignmentKey);
        Query q = new Query("QuestionInfo").setFilter(assignment_filter).addSort("questionId", Query.SortDirection.ASCENDING);
        PreparedQuery pq = datastore.prepare(q);
        System.out.println("pq as a list is" + pq.asList(FetchOptions.Builder.withDefaults()));
        QuestionListBean list = findQuestions(assignmentKey);
            boolean check = false;
            boolean check1 = false;
            for(QuestionBean b: list.getProblems()){
                for(Entity e: pq.asList(FetchOptions.Builder.withDefaults())){
                    if(b.getQuestionKey().equals(e.getProperty("question_key"))){
                        QuestionInfoBean quesInfo = new QuestionInfoBean((String)e.getProperty("point"), email_address, b.getQuestionKey(), assignmentKey, (String)e.getProperty("complete"));
                        quesInfo.setQuestionId((String)e.getProperty("questionId"));
                        newList.getQuestionInfo().add(quesInfo);
                       check = true;
                       break;
                    }
                }
                for(Entity e: pq.asList(FetchOptions.Builder.withDefaults())){
                    if(Integer.parseInt((String)e.getProperty("questionId")) >= i){
                        i = Integer.parseInt((String)e.getProperty("questionId"));
                        check1 = true;
                    }
                }
                if(check1 == true){
                    i = i + 1;
                }
                if(check == false){
                    Entity questionInfo = new Entity("QuestionInfo");
                    questionInfo.setProperty("point", "0");
                    questionInfo.setProperty("email_address", email_address);
                    questionInfo.setProperty("question_key", b.getQuestionKey());
                    questionInfo.setProperty("assignmentKey", assignmentKey);
                    questionInfo.setProperty("complete", "0");
                    questionInfo.setProperty("questionId",Integer.toString(i));
                    datastore.put(questionInfo);
                    QuestionInfoBean quesInfo = new QuestionInfoBean("0", email_address, b.getQuestionKey(), assignmentKey, "0");
                    quesInfo.setQuestionId(Integer.toString(i));
                    newList.getQuestionInfo().add(quesInfo);
                }
        }
        return newList;
    }

        /**
         * Will use datastore to update student responses
         * @param questionKey
         * @param student_ans
         */

    public void updateQuestion(String questionKey, String student_ans) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter question_filter = new Query.FilterPredicate("questionKey", Query.FilterOperator.EQUAL, questionKey);
        Query q = new Query("Question").setFilter(question_filter);
        PreparedQuery pq = datastore.prepare(q);
        System.out.println("pq as a list is" + pq.asList(FetchOptions.Builder.withDefaults()));
        for (Entity e : pq.asList(FetchOptions.Builder.withDefaults())) {
            e.setProperty("student_answer", student_ans);
            datastore.put(e);
        }
    }

    /**
     * Will use datastore to update student point to that question
     * @param questionKey
     */
    public boolean updatePoint(String questionKey){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter question_info_filter = new Query.FilterPredicate("question_key",Query.FilterOperator.EQUAL, questionKey);
        Query q = new Query("QuestionInfo").setFilter(question_info_filter);
        PreparedQuery pq = datastore.prepare(q);
        for(Entity e: pq.asList(FetchOptions.Builder.withDefaults())){
            if(e.getProperty("complete").equals("0")){
                e.setProperty("point","1");
                e.setProperty("complete","1");
                datastore.put(e);
                return true;
            }

        }
        return false;
    }

    public void updateComplete(String questionKey){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter question_info_filter = new Query.FilterPredicate("question_key",Query.FilterOperator.EQUAL, questionKey);
        Query q = new Query("QuestionInfo").setFilter(question_info_filter);
        PreparedQuery pq = datastore.prepare(q);
        for(Entity e: pq.asList(FetchOptions.Builder.withDefaults())){
            e.setProperty("complete","1");
            datastore.put(e);
        }
    }
}

