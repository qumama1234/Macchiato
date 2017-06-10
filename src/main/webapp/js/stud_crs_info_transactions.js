/**
 * Created by li on 5/12/2017.
 */

$(document).ready(function () {
    var currCrsList;
    load_info("/LoadStudent.htm");

    /*
    Uses ajax to fill in the information for the student
     */
    function load_info($url) {
        $.ajax({
            method: 'get',
            url: $url,
            dataType: 'json',
            success: function (crs_info) {
                console.log("Get Info:Success");
                var JSON_crs_info = crs_info;
                $('#stud_name').text(JSON_crs_info.Student[0].email);
                $('#class_title').text(JSON_crs_info.Student[2].name);
                $('#crs_desc').text(JSON_crs_info.Student[2].description);
                var section = "Section: ";
                section += JSON_crs_info.Student[2].section;
                if(section != "Section: "){
                    $('#class_section').text(section);
                }
                currCrsList = JSON_crs_info.Student[1]
                load_course_list(currCrsList);
                var enroll_status = JSON_crs_info.Enroll.status;
                if(enroll_status == 1){
                    $('#notification_title').text("Success!");
                    $('#notification_txt').text("You have successfully enrolled!");
                    $('#notification_modal').foundation('open');
                }else if(enroll_status == 2){
                    $('#notification_title').text("Failure");
                    $('#notification_txt').text("You have already enrolled in this course.");
                    $('#notification_modal').foundation('open');
                }else if(enroll_status == 3){
                    $('#notification_title').text("Failure");
                    $('#notification_txt').text("Sorry! This course does not exist.");
                    $('#notification_modal').foundation('open');
                }
            },
            error: function () {
                console.log("Get Info Failure: Aw, It didn't connect to the servlet :(");
            }
        });
        console.log("Load Course Info is done");
    }

    /*
     Loads the list of courses the Student is enrolled in
     * */
    function load_course_list(crs_list){
        var assignment_area = $('#load_assignment_area');
        var hostname = window.location.host;
        assignment_area.html("<li><a href='javascript:void(0)'>Searching...</a></li>");
        console.log(crs_list);
        var source = $('#course-list-template').html();
        console.log(source);
        var course_list_template = Handlebars.compile(source);
        console.log(course_list_template);
        var list_data = course_list_template(crs_list);

        assignment_area.html(list_data);
        console.log(list_data);
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

        var link_form = $('#link-form');

        form_crsName.attr("value",crsName.trim());
        form_crsCode.attr("value",crsCode.trim());

        var test = link_form.serializeArray();
        console.log(crsName);
        console.log(crsCode);
        var url = "/LoadStudent.htm?crsName=" + crsCode;
        console.log(url);
        load_info(url);

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
            load_info(url);
        }else{
            $('#no_crs_code').text("No Course Code");
        }

    });


});