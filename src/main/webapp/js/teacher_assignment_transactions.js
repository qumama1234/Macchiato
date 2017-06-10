/**
 * Created by Xiangbin on 5/4/2017.
 */
$.noConflict();
$(document).ready(function () {

    load_assignment_item("Load the Assignment");
    // this function will control the button in the teacher home page to create a new assignment
    $('body').on('click', '#add_assignment_submit', function (e) {
        var assignment_name = $('#assignment_name_text').val().trim();
        var assignment_due = $('#assignment_due').val().trim();
        console.log(assignment_due);
        console.log("ADD ASSIGNEMTN");
        var url = "/addAssignment.htm?assignmentName=" +assignment_name + "&duedate=" + assignment_due+"&course_code=" + localStorage.getItem("course_code");

        if(assignment_name.length==0){
            alert("Can not create course with course name or course description is empty")
            console.log("ADD COURSE ERROR");
        }else {
            $.ajax({
                method: 'post',
                url: url,
                dataType: 'text',
                success: function (h) {
                        clear_form_data();
                        load_assignment_item('ADD_ASSIGNMENT');
                        $('#close_add_assignment').click();
                        console.log("ADD ITEM: SUCCESS");
                },
                error: function () {
                    console.log("Add Item Failure: Aw, It didn't connect to the servlet :(");
                }
            });
        }
    });

});
function clear_form_data(){
    $('#assignment_name_text').val("");
    $('#assignment_due').val("");
    console.log("Clear all data");
}

//this function will bring json file and load course list to JSP file on teacher page
function load_assignment_item(type){
    console.log("Type:" + type);

    $.ajax({
        method: 'get',
        url: "/LoadAssignment.htm?course_code="+localStorage.getItem("course_code"),
        dataType: 'json',

        success: function (assignment_table) {
            var item_area=$('#item_area');
            console.log("Get Assignment :Success");
            JSON_list_items = assignment_table;
            var list_data="";
            $.each(JSON_list_items, function (i, item) {
                list_data += '<tr><td>' + item.assignmentName + '</td><td>'+ '<button onclick="assignment_helper('+'\''+item.assignmentKey+'\''+')" type="button" class="btn btn-link" >Questions</button>'+'</td><td>' +
                    '<button onclick="assignment_Grade_helper('+'\''+item.assignmentKey+'\''+')" type="button" class="btn btn-link" >Grades</button>'+'</td><td>'+item.duedate+'</td><td>'+item.endString+ '</td></tr>';
            });
            item_area.html(list_data);
        },
        error: function () {
            console.log("Loading the course table: Aw, It didn't connect to the servlet :(");
        }
    });
}

function assignment_helper(assignmentkey){
    localStorage.setItem("assignmentKey",assignmentkey);
    console.log("Set new value to local Storage "+assignmentkey+" ,And junmp to question page");
    var url="TeacherQuestionPage.htm"
    location.href =url;
    console.log(localStorage.getItem("assignmentKey"));
}

function assignment_Grade_helper(assignmentkey){
    localStorage.setItem("assignmentKey",assignmentkey);
    console.log("Set new value to local Storage "+assignmentkey+" ,And junmp to grading page");
    var url="TeacherGradingPage.htm"
    location.href =url;
    console.log(localStorage.getItem("assignmentKey"));
}
