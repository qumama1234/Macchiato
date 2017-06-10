/**
 * Created by Xiangbin on 5/14/2017.
 */
$.noConflict();
$(document).ready(function () {
load_StudentGrade_item("Load the Page");
});


//this function will bring json file and load course list to JSP file on teacher page
function load_StudentGrade_item(type){
    console.log("Type:" + type);

    $.ajax({
        method: 'get',
        url: "/LoadStudentGrade.htm?assignmentKey="+localStorage.getItem("assignmentKey"),
        dataType: 'json',

        success: function (grade_table) {
            var item_area=$('#item_area');
            console.log("Get Grades :Success");
            JSON_list_items = grade_table;
            var list_data="";
            $.each(JSON_list_items, function (i, item) {
                list_data += '<tr><td>' + item.email + '</td><td>'+item.point+ '</td></tr>';
            });
            item_area.html(list_data);
        },
        error: function () {
            console.log("Loading the grading table: Aw, It didn't connect to the servlet :(");
        }
    });
}

//this function will bring json file and load course list to JSP file on teacher page
function load_QuestionGrading_item(type){
    console.log("Type:" + type);

    $.ajax({
        method: 'get',
        url: "/LoadQuestionGrade.htm?assignmentKey="+localStorage.getItem("assignmentKey"),
        dataType: 'json',

        success: function (grade_table) {
            var item_area=$('#item2_area');
            console.log("Get Question Grades :Success");
            JSON_list_items = grade_table;
            var list_data="";
            $.each(JSON_list_items, function (i, item) {
                list_data += '<tr><td>' + item.id + '</td><td>'+item.point+ '</td></tr>';
            });
            item_area.html(list_data);
        },
        error: function () {
            console.log("Loading the grading table: Aw, It didn't connect to the servlet :(");
        }
    });
}


