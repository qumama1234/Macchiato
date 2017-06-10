package com.macchiato.utility;

import com.google.appengine.api.datastore.*;
import com.macchiato.beans.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Xiangbin on 4/26/2017.
 */
public class TeachersUtils {


    /**
     * helping to find all the course that created by this teacher
     * when you inputs a email it will return all the Course bean
     * list Created by this teacher
     * @param  email  the email from the instructor
     * @return ArrayList<CourseBean> course bean array list will be all the course owned by this person
     */
    public static ArrayList<CourseBean> isOwned(String email) {
        String CrsName;
        String CrsCode;
        String Crsdis;
        String section;
        ArrayList<CourseBean> classList = new ArrayList<CourseBean>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();


        Query.Filter email_filter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email);
        Query q = new Query("Course").setFilter(email_filter);
        PreparedQuery pq = datastore.prepare(q);
        int numberOfClass = pq.asList(FetchOptions.Builder.withDefaults()).size();
        System.out.println("number of course;" + numberOfClass);
        if (numberOfClass == 0) {
            System.out.println("There is no class that you own");
        } else {
            for (Entity result : pq.asIterable()) {
                email = (String) result.getProperty("email");
                CrsName = (String) result.getProperty("name");
                CrsCode = (String) result.getProperty("course_code");
                Crsdis = (String) result.getProperty("description");
                section=(String) result.getProperty("section");
                CourseBean newBean = new CourseBean();
                newBean.setInstrEmail(email);
                newBean.setCrsName(CrsName);
                newBean.setCrsCode(CrsCode);
                newBean.setDescription(Crsdis);
                newBean.setSection(section);
                classList.add(newBean);
            }
        }


        System.out.println("number of class in the list:" + classList.size());
        return classList;
    }


    /**
     * this function helps generate  CourseBean list to a List of json type of string
     * @param  courseList  list of assignment that owned by the teacher
     * @return outputString course bean array list will be all the course owned by this person
     */
    public static String CourseListJson(ArrayList<CourseBean> courseList) {
        String outputString = "[";
        if (courseList.size() <= 0) {
            return "[]";
        }
        for (int i = 0; i < courseList.size(); i++) {
            if (i == courseList.size() - 1) {
                outputString += courseList.get(i).generateJSON() + "]";
            } else {
                outputString += courseList.get(i).generateJSON() + ",";
            }
        }
        return outputString;
    }

    //this function will help user to find all the assignment from this course
    /**
     * this function helps generate  CourseBean list to a List of json type of string
     * @param  course_code  code of course that we needs to find
     * @return AssignmentList the list of assignment under that course
     */
    public static ArrayList<AssignmentBean> findAllAssigmentBean(String course_code) {
        String assignmentName;
        String dueData;
        String key;
        Date newDate;
        ArrayList<AssignmentBean> AssignmentList = new ArrayList<AssignmentBean>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter crsCode_filter = new Query.FilterPredicate("course_code", Query.FilterOperator.EQUAL, course_code);
        Query q = new Query("Assignment").setFilter(crsCode_filter);
        PreparedQuery pq = datastore.prepare(q);
        int numberOfAssignment = pq.asList(FetchOptions.Builder.withDefaults()).size();
        if (numberOfAssignment == 0) {
            System.out.println("There is no assignment that you own");
        } else {
            for (Entity result : pq.asIterable()) {
                course_code = (String) result.getProperty("course_code");
                assignmentName = (String) result.getProperty("assignmentName");
                dueData=(String) result.getProperty("duedate");
                newDate=dataGenerate(dueData);
                //Date rightnow=new Date();
                //System.out.println(rightnow+" vs "+newDate);
                key = result.getKey().toString();
                AssignmentBean newBean = new AssignmentBean();

                if (Passed(newDate)) {
                        newBean.setEnd("1");
                        result.setProperty("end","1");
                        datastore.put(result);
                        System.out.print("yes");
                    }else{
                        result.setProperty("end","0");
                        newBean.setEnd("0");
                        datastore.put(result);
                    }

                newBean.setCrsCode(course_code);
                newBean.setAissignmentKey(key);
                newBean.setAissignmentName(assignmentName);
                newBean.setDuedata(newDate);
                AssignmentList.add(newBean);
            }
        }
        System.out.println("number of  assignment in the list:" + AssignmentList.size());
        Collections.sort(AssignmentList);
        return AssignmentList;
    }


    /**
     * this function  helps generate  assignmentBean list to a List of json type of string
     * @param  assignmentList list of assignment bean under this course
     * @return outputString Json that content the information from list of assignment bean
     */
    public static String AssignmentListJson(ArrayList<AssignmentBean> assignmentList) {
        String outputString = "[";
        if (assignmentList.size() <= 0) {
            return "[]";
        }
        for (int i = 0; i < assignmentList.size(); i++) {
            if (i == assignmentList.size() - 1) {
                outputString += assignmentList.get(i).generateJSON() + "]";
            } else {
                outputString += assignmentList.get(i).generateJSON() + ",";
            }
        }
        return outputString;
    }


    /**
     * this function will function all the question bean from one assignment
     * @param  assignmentKey  key of assignment
     * @return questionList List of question bean under this assignment key
     */
    public static ArrayList<QuestionBean> findAllQuestionBean(String assignmentKey) {
        String problem;
        String solution;
        String id;
        String questionkey;
        String teacherAns;
        System.out.println("Load all the Question :"+assignmentKey);
        ArrayList<QuestionBean> questionList = new ArrayList<QuestionBean>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter assignmentKey_filter = new Query.FilterPredicate("assignmentKey", Query.FilterOperator.EQUAL, assignmentKey);
        Query q = new Query("Question").setFilter(assignmentKey_filter);
        PreparedQuery pq = datastore.prepare(q);
        int numberOfQuestion = pq.asList(FetchOptions.Builder.withDefaults()).size();
        if (numberOfQuestion == 0) {
            System.out.println("There is no assignment that you own");
        } else {
            for (Entity result : pq.asIterable()) {
                problem = (String) result.getProperty("problem");
                solution = (String) result.getProperty("solution");
                questionkey=(String) result.getProperty("questionKey");
                teacherAns=(String) result.getProperty("teacherAnswer");
                id = result.getKey().toString();
                QuestionBean newBean = new QuestionBean();
                newBean.setProblem(problem);
                newBean.setSolution(solution);
                newBean.setAssignmentKey(assignmentKey);
                newBean.setQuestionKey(questionkey);
                newBean.setTeacherAnswer(teacherAns);
                newBean.setId(id);
                questionList.add(newBean);
            }
        }
        System.out.println("number of  assignment in the list:" + questionList.size());
        return questionList;
    }




    /**
     * this function  helps generate  QuestionBean list to a List of json type of string
     * @param  QuestionList  list of question bean
     * @return outputString Json object has all the information from that list of question bean
     */
    public static String QuestionListJson(ArrayList<QuestionBean> QuestionList) {
        String outputString = "[";
        if (QuestionList.size() <= 0) {
            return "[]";
        }
        for (int i = 0; i < QuestionList.size(); i++) {
            if (i == QuestionList.size() - 1) {
                outputString += QuestionList.get(i).generateJSON() + "]";
            } else {
                outputString += QuestionList.get(i).generateJSON() + ",";
            }
        }
        return outputString;
    }

    /**
     * this function will function all the question bean from one assignment
     * @param  assignmentKey  the assignment key
     * @return studentForEachGrade List of student grades bean that has each students grades from this assignment
     */
    public static ArrayList<StudentGradeBean> findAllStudentGradeBean(String assignmentKey) {
        String email;
        double point;

        System.out.println("Load all the Question :"+assignmentKey);
        ArrayList<StudentGradeBean> gradeList = new ArrayList<StudentGradeBean>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter assignmentKey_filter = new Query.FilterPredicate("assignmentKey", Query.FilterOperator.EQUAL, assignmentKey);
        Query q = new Query("QuestionInfo").setFilter(assignmentKey_filter);
        PreparedQuery pq = datastore.prepare(q);
        int numberOfQuestion = pq.asList(FetchOptions.Builder.withDefaults()).size();
        if (numberOfQuestion == 0) {
            System.out.println("There is no assignment that you own");
        } else {
            for (Entity result : pq.asIterable()) {
                email = (String) result.getProperty("email_address");
                point =  Double.parseDouble((String) result.getProperty("point"));

                StudentGradeBean newBean = new StudentGradeBean();
                newBean.setEmail(email);
                newBean.setPoint(point);
                newBean.setTotal(numberOfQuestion);
                newBean.setAssignmentKey(assignmentKey);
                gradeList.add(newBean);
            }
        }
        System.out.println("number of  grade bean in the list:" + gradeList.size());


        return studentForEachGrade(gradeList);
    }


    /**
     * this function  helps generate  QuestionBean list to a List of json type of string
     * @param  GradeList  list of student grade bean
     * @return outputString Json object has all the information from in put list of Student Grades bean
     */
    public static String StudentGradesListJson(ArrayList<StudentGradeBean> GradeList) {
        String outputString = "[";
        if (GradeList.size() <= 0) {
            return "[]";
        }
        for (int i = 0; i < GradeList.size(); i++) {
            if (i == GradeList.size() - 1) {
                outputString += GradeList.get(i).generateJSON() + "]";
            } else {
                outputString += GradeList.get(i).generateJSON() + ",";
            }
        }
        return outputString;
    }




    /**
     * this function will find the email of each student
     * @param  GradeList  list of student grade bean
     * @return newList each students grades from one assignment
     */
    public static  ArrayList<StudentGradeBean> studentForEachGrade(ArrayList<StudentGradeBean> GradeList){
        ArrayList<String> allEmail=AllEmails(GradeList);
        ArrayList<StudentGradeBean> newList=new ArrayList<StudentGradeBean>();
        for(int i=0;i<allEmail.size();i++){
            StudentGradeBean newBean=new StudentGradeBean();
            newBean.setEmail(allEmail.get(i));
            int total=0;
            for(int g=0;g<GradeList.size();g++){
                if(GradeList.get(g).getEmail().equals(allEmail.get(i))){
                    newBean.point= newBean.point+GradeList.get(g).getPoint();
                    newBean.total++;
                }
            }
            newList.add(newBean);
        }
        return newList;
    }

    /**
     * find all the student from the database
     * @param  GradeList  list of student grade bean
     * @return allemail the email of each student from that list of Student Grade bean
     */
    public static  ArrayList<String> AllEmails(ArrayList<StudentGradeBean> GradeList){
        ArrayList<String> allemail=new ArrayList<String>();
        boolean findit;
        for(int i=0;i<GradeList.size();i++){
            findit=false;
            for(int e=0;e<allemail.size();e++){
                if(allemail.get(e).equals(GradeList.get(i).getEmail())){
                    findit=true;
                }
            }
            if(findit==false){
                allemail.add(GradeList.get(i).getEmail());
            }
        }
        System.out.print("Number of Students"+allemail.size());
        return allemail;
    }


    /**
     * this function will function all the question bean from one assignment
     * @param  assignmentKey  the assigment key that you searcher
     * @return questionForEachGrade the email of each student from that list of Student Grade bean
     */
    public static ArrayList<QuestionGradeBean> findAllQuestionGradeBean(String assignmentKey) {
        double point;
        String id;
        System.out.println("Load all the Question :"+assignmentKey);
        ArrayList<QuestionGradeBean> gradeList = new ArrayList<QuestionGradeBean>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter assignmentKey_filter = new Query.FilterPredicate("assignmentKey", Query.FilterOperator.EQUAL, assignmentKey);
        Query q = new Query("QuestionInfo").setFilter(assignmentKey_filter);
        PreparedQuery pq = datastore.prepare(q);
        int numberOfQuestion = pq.asList(FetchOptions.Builder.withDefaults()).size();
        if (numberOfQuestion == 0) {
            System.out.println("There is no assignment that you own");
        } else {
            for (Entity result : pq.asIterable()) {
                point =  Double.parseDouble((String) result.getProperty("point"));
                id=(String) result.getProperty("questionId");
                QuestionGradeBean newBean = new QuestionGradeBean();
                newBean.setPoint(point);
                newBean.setTotal(numberOfQuestion);
                newBean.setAssignmentKey(assignmentKey);
                newBean.setId(id);
                newBean.setAssignmentKey(assignmentKey);
                gradeList.add(newBean);
            }
        }
        System.out.println("number of  grade bean in the list:" + gradeList.size());


        return questionForEachGrade(gradeList);
    }


    /**
     * this function  helps generate  QuestionGradeBean list to a List of json type of string
     * @param  GradeList  list of the question grade bean
     * @return outputString Json object has all the information from list of the question grade bean
     */
    public static String QuestionGradesListJson(ArrayList<QuestionGradeBean> GradeList) {
        String outputString = "[";
        if (GradeList.size() <= 0) {
            return "[]";
        }
        for (int i = 0; i < GradeList.size(); i++) {
            if (i == GradeList.size() - 1) {
                outputString += GradeList.get(i).generateJSON() + "]";
            } else {
                outputString += GradeList.get(i).generateJSON() + ",";
            }
        }
        return outputString;
    }


    /**
     * fitter each student from the database,this function
     * helps generate  QuestionGradeBean list to a List of json type of string
     * @param  GradeList  list of the question grade bean
     * @return outputString Json object has all the information from list of the question grade bean
     */
    public static  ArrayList<QuestionGradeBean> questionForEachGrade(ArrayList<QuestionGradeBean> GradeList){
        ArrayList<String> allQuestion=AllQuestions(GradeList);
        ArrayList<QuestionGradeBean> newList=new ArrayList<QuestionGradeBean>();
        for(int i=0;i<allQuestion.size();i++){
            QuestionGradeBean newBean=new QuestionGradeBean();
            newBean.setQuestionKey(allQuestion.get(i));
            int total=0;
            for(int g=0;g<GradeList.size();g++){
                if(GradeList.get(g).getQuestionKey().equals(allQuestion.get(i))){
                    newBean.point= newBean.point+GradeList.get(g).getPoint();
                    newBean.total++;
                    newBean.setAssignmentKey(GradeList.get(g).getAssignmentKey());
                }
            }
            newList.add(newBean);
        }
        return newList;
    }

    /**
     * find all the student from the database
     * helps generate  QuestionGradeBean list to a List of json type of string
     * @param  GradeList  list of the QuestionGradeBean
     * @return outputString Json object has all the information from list of the question grade bean
     */
    public static  ArrayList<String> AllQuestions(ArrayList<QuestionGradeBean> GradeList){
        ArrayList<String> allQuestionkey=new ArrayList<String>();
        boolean findit;
        for(int i=0;i<GradeList.size();i++){
            findit=false;
            for(int e=0;e<allQuestionkey.size();e++){
                if(allQuestionkey.get(e).equals(GradeList.get(i).getQuestionKey())){
                    findit=true;
                }
            }
            if(findit==false){
                allQuestionkey.add(GradeList.get(i).getQuestionKey());
            }
        }
        return allQuestionkey;
    }


    /**
     * this function will remove all the non-digit char in the string a
     * @param  a  string
     * @return return that string with only all the number
     */
    public static String numberkeeper(String a){
        String c= a.replaceAll("[^\\d.]", "");
        return c;
    }


    /**
     *  this function will change a string formed date to a int array with year,month,day in side
     * @param  b  string
     * @return get a date format that to the date
     */
    public static Date dataGenerate(String b){
        Date newDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            newDate= sdf.parse(b);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    /**
     *  this function will help the check this date is passed or not
     * @param  due  the due Date
     * @return  boolean return true if the date passed false otherwise
     */
    public static  boolean  Passed(Date due){
        Date now=new Date();
        if(due.getYear()>now.getYear()){
            return false;
        }
        else if(due.getYear()<now.getYear()){
            return true;
        }else {
        if (due.getMonth() > now.getMonth()) {
                return false;
            }
            else if(due.getMonth() < now.getMonth()){
            return true;
            }
            else {
                if (due.getDate() > now.getDate()) {
                      return false;
                  }  else {
            return true;
        }
        }
        }
    }

}
