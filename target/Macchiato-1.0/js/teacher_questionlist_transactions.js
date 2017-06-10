/**
 * Created by Xiangbin on 5/4/2017.
 */
$(document).ready(function () {
    load_question_item("Load the Question");
    // this function will control the button in the teacher home page to create a new assignment
    $('body').on('click', '#add_question_submit', function (e) {
        var problem = $('#question_problem_text').val().trim();
        var solution = $('#question_answer_text').val().trim();
        var teacherAnswer=$('#teacher_answer_text').val().trim();
        console.log("ADD ASSIGNEMTN");
        var url = "/addQuestion.htm?problem=" +problem + "&solution=" + solution+"&assignmentKey=" + localStorage.getItem("assignmentKey")+"&teacherAnswer="+teacherAnswer;

        $.ajax({
            method: 'post',
            url: url,
            dataType: 'text',
            success: function (h) {
                if(problem.length==0||solution.length==0){
                    alert("Can not create question with problem and solution empty")
                    console.log("ADD QUESTION ERROR");
                }
                else{
                    clear_form_data();
                    load_question_item('ADD_QUESTION');
                    $('#close_add_question_modal').click();
                    console.log("ADD QUESTION: SUCCESS");
                }
            },
            error: function () {
                console.log("Add Item Failure: Aw, It didn't connect to the servlet :(");
            }
        });
    });

    //this function will submit all the information to front end when you click #question_edit_submit button
    $('body').on('click', '#question_edit_submit', function (e) {
        var problem = $('#change_question_name_text').val().trim();
        var solution = $('#change_question_answer_text').val().trim();
        var teacher_ans=$('#change_teacher_answer_text').val().trim();
        console.log("Edit Question");
        console.log(teacher_ans);
        var url = "/editQuestion.htm?problem=" +problem + "&solution=" + solution+"&questionKey=" + localStorage.getItem("questionKey")+"&teacherAnswer="+teacher_ans;

        $.ajax({
            method: 'post',
            url: url,
            dataType: 'text',
            success: function (h) {
                if(problem.length==0||solution.length==0){
                    alert("Can not create question with problem and solution empty")
                    console.log("Edit QUESTION ERROR");
                }
                else{
                    clear_form_data();
                    load_question_item('ADD_QUESTION');
                    $('#close_Edit_modal').click();
                    console.log("Edit QUESTION: SUCCESS");
                }
            },
            error: function () {
                console.log("Add Item Failure: Aw, It didn't connect to the servlet :(");
            }
        });
    });
});

// clean all the data
function clear_form_data(){
    $('#question_problem_text').val("");
    $('#question_answer_text').val("");
    $('#teacher_answer_text').val("");
    console.log("Clear all data");
}

//this function will bring json file and load course list to JSP file on teacher page
function load_question_item(type){
    console.log("Type:" + type);

    $.ajax({
        method: 'get',
        url: "/LoadQuestion.htm?assignmentKey="+localStorage.getItem("assignmentKey"),
        dataType: 'json',

        success: function (question_table) {
            var a;
            var item_area=$('#item_area');
            console.log("Get Question :Success");
            JSON_list_items = question_table;
            var list_data="";
            $.each(JSON_list_items, function (i, item) {
                a=1+i;
                list_data += '<tr><td>' +a + '</td><td>'+ '<button onclick="delete_helper('+'\''+item.questionKey+'\''+')" type="button" class="btn btn-link" >delete</button>'+'</td><td>' +
                    '<button onclick="Question_helper('+'\''+item.questionKey+'\''+')" type="button" class="btn btn-link" data-toggle="modal" data-target="#edit_modal">Edit</button>'+'</td><td>'+item.problem+ '</td></tr>';
            });
            item_area.html(list_data);
        },
        error: function () {
            console.log("Loading the course table: Aw, It didn't connect to the servlet :(");
        }
    });
}

// when you delete you question it will sent the question key to back
// end and delete it on the database and reload the page
function delete_helper(questionKey){
    localStorage.setItem("questionKey",questionKey);
    console.log("Set new value to local Storage "+questionKey);
    var url="/deleteQuestion.htm?questionKey="+questionKey;
    console.log(url);
    $.ajax({
        method: 'post',
        url: url,
        dataType: 'text',
        success: function (h) {
            if(localStorage.getItem("questionKey")==0){
                console.log("ADD QUESTION ERROR");
            }
            else{
                clear_form_data();
                load_question_item('delete_QUESTION');
                console.log("Question Delete: SUCCESS");
            }
        },
        error: function () {
            console.log("Add Item Failure: Aw, It didn't connect to the servlet :(");
        }
    });
}

// when you edit you question it will sent the question key to back
// end and sent back all the information to Let you load the old infomation of this question to the page
function Question_helper(questionKey){
    localStorage.setItem("questionKey",questionKey);
    console.log("Set new value to local Storage "+questionKey+" ,And junmp to grading page");
    var url = "/findQuestion.htm?questionKey=" +questionKey;
    $.ajax({
        method: 'get',
        url: url,
        dataType: 'json',
        success: function (question) {
            $('#change_question_name_text').val(question.problem);
            $('#change_question_answer_text').val(question.solution);
            $("#change_teacher_answer_text").val(question.teacherAnswer);
            console.log(question.teacherAnswer);
            console.log("Load question success");
        },
        error: function () {
            console.log("Loading the desertion: Aw, It didn't connect to the servlet :(");
        }
    });
    console.log(localStorage.getItem(questionKey));
}




