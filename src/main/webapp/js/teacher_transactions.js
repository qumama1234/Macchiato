/**
 * Created by Xiangbin on 4/20/2017.
 */

//this page is all the JS function to support teacher home page
$(document).ready(function () {

    var JSON_list_items;
    var $url = "/LoadCourse.htm";
    load_course_item("LOAD");


    // this function will control the button in the teacher home page to create a new class
    $('body').on('click', '#add_course_submit', function (e) {
        var course_name = $('#course_name_text').val().trim();
        var course_dis = $('#course_dis_text').val().trim();
        var section = $('#course_section_text').val().trim();
        var i_email = $('#i_email').attr('data-iemail');

        console.log("123");
        console.log(course_dis);
        var url = "/instructor_addcourse.htm?&i_email=" + i_email + "&course_name=" + course_name + "&section=" + section + "&description=" + course_dis;


        $.ajax({
            method: 'post',
            url: url,
            dataType: 'text',
            success: function (h) {
                if(course_name.length==0||course_dis.length==0){
                    alert("Can not create course with course name or course description is empty")
                    console.log("ADD COURSE ERROR");
                }
                else{
                    $('#close_add_modal').click();
                    clear_form_data();
                    load_course_item('ADD_COURSE');
                    console.log("ADD ITEM: SUCCESS");
                }
            },
            error: function () {
                console.log("Add Item Failure: Aw, It didn't connect to the servlet :(");
            }
        });
    });

    //this function will bring json file and load course list to JSP file on teacher page
    function load_course_item(type){
        console.log("Type:" + type);

        $.ajax({
            method: 'get',
            url: "/LoadCourse.htm",
            dataType: 'json',

            success: function (course_table) {
                var item_area=$('#item_area');
                console.log("Get Courses :Success");
                JSON_list_items = course_table;
                var list_data="";
                $.each(JSON_list_items, function (i, item) {
                    list_data += '<tr><td>' + item.name + '</td><td>' + item.section + '</td><td>'+
                        '<button onclick="edit_course_helper('+'\''+item.course_code+'\''+')" type="button" class="btn btn-link" data-toggle="modal" data-target="#info_modal">Edit</button>'+ '</td><td>'+
                        '<button onclick="assignment_course_helper('+'\''+item.course_code+'\''+')" type="button" class="btn btn-link"  >Assigments</button>'+'</td><td>' +
                        item.course_code + '</td></tr>';
                });
                item_area.html(list_data);
            },
            error: function () {
                console.log("Loading the course table: Aw, It didn't connect to the servlet :(");
            }
        });
    }

    //this function will help clean some data in teacher home page
    function clear_form_data() {
        var item_area=$('#item_area');
        console.log("Clear all data");
        item_area.val("");
        $('#change_course_dis_text').val("");
        $('#course_name_text').val("");
        $('#course_dis_text').val("");

    }

    //this function will change the description of the course
    $('body').on('click', '#info_course_submit', function (e) {
        var course_code = localStorage.getItem("course_code");
        var course_dis = $('#change_course_dis_text').val().trim();
        console.log(course_dis);
        var url = "/editCourse.htm?course_code=" +course_code + "&description=" + course_dis ;

        $.ajax({
            method: 'post',
            url: url,
            dataType: 'text',
            success: function (h) {
                if(course_dis.length==0){
                    alert("Can not create course,because description is empty")
                    console.log("Change Description ERROR");
                }
                else{
                    $('#close_information_modal').click();
                    clear_form_data();
                    console.log(course_code);
                    console.log("Change Description Successes");
                }
            },
            error: function () {
                console.log("Add Item Failure: Aw, It didn't connect to the servlet :(");
            }
        });
    });
});


//function will help put course code to local Storage of HTML5 and load desertion from the database
function edit_course_helper(course_code){
    localStorage.setItem("course_code",course_code);
    console.log("Set new value to local Storage "+course_code);
    var url = "/findDes.htm?course_code=" +course_code;
    $.ajax({
        method: 'get',
        url: url,
        dataType: 'json',
        success: function (course) {
            $('#change_course_dis_text').val(course.description);
            console.log("Load description success");
        },
        error: function () {
            console.log("Loading the desertion: Aw, It didn't connect to the servlet :(");
        }
    });
}
// when you click assignment it will keep the assignment key on local storage, and locate your page to assignment page
function assignment_course_helper(course_code){
    localStorage.setItem("course_code",course_code);
    console.log("Set new value to local Storage "+course_code+" ,And junmp to assignment page");
    var url="TeacherAssignmentPage.htm"
    location.href =url;
    console.log(localStorage.getItem("course_code"));
}


