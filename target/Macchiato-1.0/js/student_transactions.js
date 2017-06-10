/**
 * Created by li on 4/5/2017.
 */

$(document).ready(function () {
    var currCrsList;
    load_student("/LoadStudent.htm");
    // Loads in the student information.
    function load_student($url) {

        $.ajax({
            method: 'get',
            url: $url,
            dataType: 'json',
            success: function (stud_user) {
                console.log("Get Email:Success");
                var JSON_stud_user = stud_user;
                $('#stud_name').text(JSON_stud_user.Student[0].email);
                $('#class_title').text(JSON_stud_user.Student[2].name);
                var section = "Section: ";
                section += JSON_stud_user.Student[2].section;
                if(section != "Section: "){
                    $('#class_section').text(section);
                }

                currCrsList = JSON_stud_user.Student[1]
                load_course_list(currCrsList);
                var assignList = JSON_stud_user.Student[3];
                load_assignment_list(assignList);
                var enroll_status = JSON_stud_user.Enroll.status;
                if(enroll_status == 1){
                    $('#notification_title').text("Success!");
                    $('#notification_txt').text("You have successfully enrolled!");
                    $('#notification_modal').foundation('open');
                }else if(enroll_status == 2){
                    $('#notification_title').text("Failure");
                    $('#notification_txt').text("You are already enrolled in this course.");
                    $('#notification_modal').foundation('open');
                }else if(enroll_status == 3){
                    $('#notification_title').text("Failure");
                    $('#notification_txt').text("Sorry! This course does not exist.");
                    $('#notification_modal').foundation('open');
                }
                console.log(enroll_status);
            },
            error: function () {
                console.log("Email Failure: Aw, It didn't connect to the servlet :(");
            }
        });
        console.log("Load Student is done");
    }


    /*
     Loads the list of courses the Student is enrolled in
    * */
    function load_course_list(crs_list){
        var assignment_area = $('#load_assignment_area');
        var hostname = window.location.host;
        assignment_area.html("<li><a href='javascript:void(0)'>Searching...</a></li>");
        var source = $('#course-list-template').html();
        var course_list_template = Handlebars.compile(source);
        var list_data = course_list_template(crs_list);

        assignment_area.html(list_data);
    }

    /*
    Loads the assignments for that course
     */
    function load_assignment_list(assignment_list){
        var assignment_area = $('#assignment_area');
        var source = $('#assignment-list-template').html();
        var assignment_list_template = Handlebars.compile(source);
        var list_data = assignment_list_template(assignment_list);
        assignment_area.html(list_data);
    }

    /*
     Course List pops up on mouse over
     */
    $('body').on('click mouseover', '#nav_assignments', function () {
        load_course_list(currCrsList);
    });

    /*
     Gives the Student Controller information on what course to load in when clicked on
     */
    $('body').on('click', 'li[data-crsName][data-crsCode]', function () {
        var crsName = $(this).attr('data-crsName');
        var crsCode = $(this).attr('data-crsCode');

        var form_crsName = $('#form-crsName');
        var form_crsCode = $('#form-crsCode');

        var link_form = $('#link-form-student');

        form_crsName.attr("value",crsName.trim());
        form_crsCode.attr("value",crsCode.trim());

        var test = link_form.serializeArray();
        var url = "/LoadStudent.htm?crsName=" + crsCode;
        load_student(url);
    });


    /*
     Brings up a modal for the user to input the course code they want to enroll in
     */
    $('body').on('click', '#enroll_submit', function () {
        var crsCode = $('#enroll_crs_code').val().trim();
        if(crsCode){
            console.log(crsCode);
            console.log("Being Clicked");
            var url = "/LoadStudent.htm?enroll=" + crsCode;
            console.log("Hiding");
            $('#enroll_modal').foundation('close');
            $('#no_crs_code').text("");
            $('#enroll_crs_code').val("");
            load_student(url);
        }else{
            $('#no_crs_code').text("No Course Code");
        }

    });



});


