/**
 * nav_bar_transactions.js
 * Purpose: This script handles the interations involving the navigation bar.
 * @author Karl Jean-Brice
 */
$(document).ready(function () {

    check_user_status();


    //$(location).attr('href');
    //pure javascript
    //var pathname = window.location.pathname;
    // to show it in an alert window
    //alert(location.protocol + "//" + location.host);

    function load_forum_list() {
        var forum_area = $('#load_forum_area');
        var hostname = window.location.host;
        forum_area.html("<li ><a class='list-size' href='javascript:void(0)'>Searching...</a></li>");

        var $url = "/getforumlist.htm";
        $.ajax({
            method: 'get',
            url: $url,
            dataType: 'json',
            success: function (course_list) {
                console.log("COURSE LIST:Success");
                var JSON_course_list = course_list;
                console.log(JSON_course_list);
                var source = $('#discussion-list-template').html();
                console.log(source);
                var forum_list_template = Handlebars.compile(source);
                console.log(forum_list_template);
                var list_data = forum_list_template(JSON_course_list);

                forum_area.html(list_data);
                console.log(list_data);
            },
            error: function () {
                forum_area.html("<li><a  class='list-size' href='javascript:void(0)'>None Available</a></li>");
                console.log("Loading Course list: Aw, It didn't connect to the servlet :(");
            }

        });
    }

    $('body').on('click', '.scroll-nav-home', function (e) {
        var linkHref = $(this).attr('href');
        e.preventDefault();
        $('html, body').animate({
            scrollTop: $(linkHref).offset().top
        },1000);
    });


    $('body').on('click mouseover', '#nav_forum', function () {
        load_forum_list();
    });


    $('body').on('click', 'li[data-list-email][data-course][data-section]', function () {
        //data-list_name="{{instructorEmail}}" data-course
        var i_email = $(this).attr('data-list-email');
        var course = $(this).attr('data-course');
        var section = $(this).attr('data-section');

        if(!i_email || i_email.trim().length === 0 || !course || course.trim().length === 0 || !section || section.trim().length === 0){
            return;
        }


        var form_iemail = $('#form-iemail');
        var form_course = $('#form-course');
        var form_section = $('#form-section');

        var link_form = $('#link-form');

        form_iemail.attr("value",i_email.trim());
        form_course.attr("value",course.trim());
        form_section.attr("value",section.trim());
        var url = location.protocol + "//" + location.host + "/Discussionboard.htm";
        link_form.attr("action",url);
        link_form.submit();
    });

    function check_user_status(){
        var home_area = $('#home-area');
        if(home_area.length === 0){
            return;
        }

        var $url = "/loginstatus.htm";
        $.ajax({
            method: 'get',
            url: $url,
            dataType: 'json',
            success: function (login_status) {
                console.log("Load Login Data:Success");
                var JSON_login_status = login_status;
                console.log(JSON_login_status);
                var source = $('#home-page-template').html();
                console.log(source);
                var home_page_template = Handlebars.compile(source);
                console.log(home_page_template);
                var login_status_data = home_page_template(JSON_login_status);

                home_area.html(login_status_data);
                console.log(login_status_data);
                var forum_area = $('#load_forum_area');
                var defaultClosingTime = 500;
                var options = {
                    'data-alignment': 'right'
                };
                var elem = new Foundation.DropdownMenu($('#home-area'),{disableHover:false, closeOnClick:true, alignment:"right"});

            },
            error: function () {
                console.log("Loading Login Data: Aw, It didn't connect to the servlet :(");
            }

        });


    }






});


