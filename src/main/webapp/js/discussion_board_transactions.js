/**
 * discussion_board_transactions.js
 * Purpose: This script is responsible for handling front-end interaction for the discussion board.
 * Calls back-end functions using AJAX to populate the page.
 * @author Karl Jean-Brice
 */
$(document).ready(function () {

    var SUCCESS = "0";
    var ENROLLED = "1";
    var NOT_ENROLLED = "2";
    var NOT_LOGGED_IN = "3";
    var DUPLICATE_STUDENT = "4";
    var EMPTY_PARAMETERS = "5";
    var BOARD_NOT_FOUND = "6";
    var DATABASE_ERROR = "7";
    var POST_NOT_FOUND = "8";
    var NO_ACCESS = "9";
    var INVALID_ARGS = "10";
    var ALREADY_ACTIVE = "11";
    var INVALID_LENGTH = "12";

    load_discussionpage();


    /**
     * Requests data from a backend function to populate the discussion page with post and comment data.
     * @author Karl Jean-Brice
     */
    function load_discussionpage() {
        var email_area = $('#i_email').attr('data-iemail');
        var course_area = $('#course').attr('data-course');
        var section = $('#section').attr('data-section');



        var navbar_instructor = $('#navbar-instructor');
        var navbar_course = $('#navbar-course');
        var navbar_enrolled = $('#navbar-enrolled');
        var navbar_comments = $('#navbar-comments');
        var navbar_posts = $('#navbar-posts');

        var post_comment_area = $('#post_area_template');
        var $url = "/populate_discussionboard.htm?i_email=" + email_area + "&course=" + course_area + "&section=" + section ;

        $.ajax({
            method: 'get',
            url: $url,
            dataType: 'json',
            success: function (discussion_data) {
                console.log("POPULATE:Success");
                var JSON_discussion_data = discussion_data;
                console.log(JSON_discussion_data);
                var source = $('#discussion-area-template').html();
                var discussion_area_template = Handlebars.compile(source);
                console.log("T:" + source);
                var disc_data = discussion_area_template(JSON_discussion_data);
                console.log("E:" + disc_data);

                post_comment_area.html(disc_data);
                navbar_instructor.text("Instructor: " + JSON_discussion_data.username);
                navbar_course.text("Course: " + JSON_discussion_data.course);
                navbar_enrolled.text("Enrolled: " + JSON_discussion_data.totalEnrollment);
                navbar_comments.text("Comments: " + JSON_discussion_data.totalComments);
                navbar_posts.text("Posts: " + JSON_discussion_data.totalPosts);


                var u_set = $('#u_set');
                u_set.attr("data-uset",JSON_discussion_data.uset);
                console.log("u_set:" + u_set.attr("data-uset"));
                if(JSON_discussion_data.uset === "0"){
                    $('#change-user-name').click();
                }

                var edit_fields = $("[data-edit-permission=edit]");
                console.log(edit_fields.length);
                edit_fields.removeAttr('data-edit-permission');
                edit_fields.removeAttr('style');

                var delete_fields = $("[data-delete-permission=delete]");
                console.log(delete_fields.length);
                delete_fields.removeAttr('data-delete-permission');
                delete_fields.removeAttr('style');

                var instructor_color = $("[data-access=INSTRUCTOR]");
                instructor_color.removeAttr('data-access');
                instructor_color.addClass('i-color');

                var student_color = $("[data-access=STUDENT]");
                student_color.removeAttr('data-access');



                if(JSON_discussion_data.currentUsername){
                    $('#current-user').text("Hi, " + JSON_discussion_data.currentUsername);
                    $('#current-user').attr("data-username",JSON_discussion_data.currentUsername);
                    $('#txt-username').val(JSON_discussion_data.currentUsername);
                }

            },
            error: function () {
                alert("Loading the discussion page failed");
            }

        });
    }


    /**
     * Populates the username dialog box with user's current username.
     * @author Karl Jean-Brice
     */
    $('body').on('click', '#change-user-name', function () {

        var err_label = $('#err-changeusername');
        var current_user =  $('#current-user').attr("data-username");
        $('#txt-username').val(current_user);
        err_label.css("opacity","0");

        var u_set = $('#u_set');
        if(u_set.attr('data-uset') === "1"){
            $('#close-username-modal').css("visibility","visible");
        }
        else{
            $('#close-username-modal').css("visibility","hidden");
        }



    });

    /**
     * Sends data to a backend function to allow a user to change their username.
     * @author Karl Jean-Brice
     */
    $('body').on('click', '#btn-username-submit', function () {
        var i_email = $('#i_email').attr('data-iemail');
        var course = $('#course').attr('data-course');
        var section = $('#section').attr('data-section');
        var username = $('#txt-username').val().trim();
        var err_label = $('#err-changeusername');

        var url = "/discussion_changeusername.htm?&i_email=" + i_email + "&course=" + course + "&section=" + section + "&username=" + username;
        err_label.fadeTo(0,0);
        $.ajax({
            method: 'get',
            url: url,
            dataType: 'text',
            success: function (edit_status) {
                console.log("Username Change:Success");
                console.log("Username Change: " + edit_status);
                switch(edit_status.trim()){
                    case SUCCESS:
                        load_discussionpage();
                        $('#close-username-modal').click();
                        console.log("It got here");
                        break;
                    case ENROLLED:
                        break;
                    case NOT_ENROLLED:
                        err_label.text("You must first be enrolled in this course before you can make changes to your username.");
                        err_label.fadeTo(1000,1);
                        break;
                    case NOT_LOGGED_IN:
                        err_label.text("You must login and enroll in this course before you can modify your username.");
                        err_label.fadeTo(1000,1);
                        break;
                    case DUPLICATE_STUDENT:
                        err_label.text("There is a problem with our database. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case EMPTY_PARAMETERS:
                        err_label.text("Your username cannot be empty.");
                        err_label.fadeTo(1000,1);
                        break;
                    case BOARD_NOT_FOUND:
                        err_label.text("We're having an issue locating the discussion board for this course. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case DATABASE_ERROR:
                        err_label.text("There is a problem with our database. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case ALREADY_ACTIVE:
                        err_label.text("This username is already taken on this forum.");
                        err_label.fadeTo(1000,1);
                        break;
                    case INVALID_LENGTH:
                        err_label.text("Your username must have at least 3 characters.");
                        err_label.fadeTo(1000,1);
                        console.log("Invalid_Length:It got here");
                        break;
                    case POST_NOT_FOUND:
                        err_label.text("This post has already been deleted or doesn't exist. Please refresh your page.");
                        err_label.fadeTo(1000,1);
                        break;
                }
            },
            error: function () {
                console.log("Post:Delete: Aw, It didn't connect to the servlet :(");
            }

        });

    });


    /**
     * Clears out the label used to display error messages when posting.
     * @author Karl Jean-Brice
     */
    $('body').on('click', '#create-new-post', function () {

        var err_label = $('#err-createpost');
        err_label.css("opacity","0");
    });



    /**
     * Sends data to a backend function to allow a user to post on the current page.
     * @author Karl Jean-Brice
     */
    $('body').on('click', '#post-submit', function () {
        var post_content = $('#post-feed').val().trim();
        var post_title = $('#post-title').val().trim();
        var err_label = $('#err-createpost');
        //var post_div = '#post_area_template';
        err_label.fadeTo(0,0);
        if (post_content.length === 0 && post_title.length === 0) {
            err_label.text("You must add a title and content before you can post.");
            err_label.fadeTo(1000,1);
        }
        else if (post_content.length === 0){
            err_label.text("You must add content before you can post.");
            err_label.fadeTo(1000,1);
        }
        else if (post_title.length === 0){
            err_label.text("You must add a title before you can post.");
            err_label.fadeTo(1000,1);
        }
        else {
            var i_email = $('#i_email').attr('data-iemail');
            var course = $('#course').attr('data-course');
            var section = $('#section').attr('data-section');
            var url = "/discussion_addpost.htm?&i_email=" + i_email + "&course=" + course + "&title=" + post_title + "&content=" + post_content + "&section=" + section;

            $.ajax({
                method: 'get',
                url: url,
                dataType: 'text',
                success: function (post_status) {
                    console.log("Post-Success");
                    console.log("Post-Response: " + post_status);
                    load_discussionpage();
                    $('#post-feed').val("");
                    $('#post-title').val("");
                    $('#close-createpost').click();

                },
                error: function () {
                    console.log("Post-Error: Aw, It didn't connect to the servlet :(");
                }

            });

        }

    });



    /**
     * Populates the edit post dialog box with the requested title and content information.
     * @author Karl Jean-Brice
     */
    $('body').on('click', 'a.link_btn.edit_post_btn', function () {

        var post_id = $(this).attr('data-postid');
        var post_title = $('span.h2-title[data-postid=' + post_id +']' ).text().trim();
        var post_content = $( 'span.post_refresh[data-postid=' + post_id +']' ).text().trim();
        var err_label = $('#err-editpost');

        $('#edit-title').val(post_title);
        $('#edit-post-feed').val(post_content);
        $('#val-editpost').attr('data-postid',post_id);
        err_label.css("opacity","0");
    });


    /**
     * Sends data to a backend function to allow a user to edit a post on the current page.
     * @author Karl Jean-Brice
     */
    $('body').on('click', '#editpost-submit', function () {
        var i_email = $('#i_email').attr('data-iemail');
        var course = $('#course').attr('data-course');
        var section = $('#section').attr('data-section');
        var post_id = $('#val-editpost').attr('data-postid');
        var post_title = $('#edit-title').val().trim();
        var post_content =$('#edit-post-feed').val().trim();
        var err_label = $('#err-editpost');

        var url = "/discussion_editpost.htm?&i_email=" + i_email + "&course=" + course + "&section=" + section + "&post_id=" + post_id + "&title=" + post_title + "&content=" + post_content;
        err_label.fadeTo(0,0);
        $.ajax({
            method: 'get',
            url: url,
            dataType: 'text',
            success: function (edit_status) {
                console.log("Post:Edit-Success");
                console.log("Post:Edit-Response: " + edit_status);
                switch(edit_status.trim()){
                    case SUCCESS:
                        load_discussionpage();
                        $('#close-editpost').click();
                        break;
                    case ENROLLED:
                        break;
                    case NOT_ENROLLED:
                        err_label.text("You must first be enrolled in this course before you can edit a post on this page.");
                        err_label.fadeTo(1000,1);
                        break;
                    case NOT_LOGGED_IN:
                        err_label.text("You must login and enroll in this course before you can edit a post on this page.");
                        err_label.fadeTo(1000,1);
                        break;
                    case DUPLICATE_STUDENT:
                        err_label.text("There is a problem with our database. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case EMPTY_PARAMETERS:
                        err_label.text("Both fields are required to edit this post.");
                        err_label.fadeTo(1000,1);
                        break;
                    case BOARD_NOT_FOUND:
                        err_label.text("We're having an issue locating the discussion board for this course. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case DATABASE_ERROR:
                        err_label.text("There is a problem with our database. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case POST_NOT_FOUND:
                        err_label.text("This post has already been deleted or doesn't exist. Please refresh your page.");
                        err_label.fadeTo(1000,1);
                        break;
                }
            },
            error: function () {
                console.log("Post:Delete: Aw, It didn't connect to the servlet :(");
            }

        });

    });




    /**
     * Populates the edit comment dialog box with the requested content information.
     * @author Karl Jean-Brice
     */
    $('body').on('click', 'a.link_btn.edit_comment_btn', function () {

        var comment_id = $(this).attr('data-commentid');
        var comment_content = $( 'span.comment_refresh[data-commentid=' + comment_id +']' ).text().trim();
        var err_label = $('#err-editcomment');

        $('#edit-comment-feed').val(comment_content);
        $('#val-editcomment').attr('data-commentid',comment_id);
        err_label.css("opacity","0");
    });



    /**
     * Sends data to a backend function to allow a user to edit a comment on the current page.
     * @author Karl Jean-Brice
     */
    $('body').on('click', '#editcomment-submit', function () {
        var i_email = $('#i_email').attr('data-iemail');
        var course = $('#course').attr('data-course');
        var section = $('#section').attr('data-section');
        var comment_id = $('#val-editcomment').attr('data-commentid');
        var comment_content =$('#edit-comment-feed').val().trim();
        var err_label = $('#err-editcomment');

        var url = "/discussion_editcomment.htm?&i_email=" + i_email + "&course=" + course  + "&section=" + section + "&comment_id=" + comment_id + "&content=" + comment_content;
        err_label.fadeTo(0,0);
        $.ajax({
            method: 'get',
            url: url,
            dataType: 'text',
            success: function (edit_status) {
                console.log("Comment:Edit-Success");
                console.log("Comment:Edit-Response: " + edit_status);
                switch(edit_status.trim()){
                    case SUCCESS:
                        load_discussionpage();
                        $('#close-editcomment').click();
                        break;
                    case ENROLLED:
                        break;
                    case NOT_ENROLLED:
                        err_label.text("You must first be enrolled in this course before you can edit a comment on this page.");
                        err_label.fadeTo(1000,1);
                        break;
                    case NOT_LOGGED_IN:
                        err_label.text("You must login and enroll in this course before you can edit a comment on this page.");
                        err_label.fadeTo(1000,1);
                        break;
                    case DUPLICATE_STUDENT:
                        err_label.text("There is a problem with our database. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case EMPTY_PARAMETERS:
                        err_label.text("You must add content to edit this comment.");
                        err_label.fadeTo(1000,1);
                        break;
                    case BOARD_NOT_FOUND:
                        err_label.text("We're having an issue locating the discussion board for this course. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case DATABASE_ERROR:
                        err_label.text("There is a problem with our database. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case POST_NOT_FOUND:
                        err_label.text("This comment has already been deleted or doesn't exist. Please refresh your page.");
                        err_label.fadeTo(1000,1);
                        break;
                }
            },
            error: function () {
                console.log("Post:Delete: Aw, It didn't connect to the servlet :(");
            }

        });

    });




    /**
     * Sends data to a backend function to allow a user to submit a comment on the current page.
     * @author Karl Jean-Brice
     */
    $('body').on('click', 'li.comment-submit', function () {
        $( this ).closest( 'form.comment-submit' ).submit();
    });
    $('body').on('submit', 'form.comment-submit', function (e) {


        e.preventDefault();
        var comment_form = $(this);
        var post_id = (comment_form.find('input[name=post_id]')).val();
        var content = (comment_form.find('textarea[name=comment_content]')).val().trim();
        var err_label = comment_form.find('span[name=err_comment]');
        var i_email = $('#i_email').attr('data-iemail');
        var course = $('#course').attr('data-course');
        var section = $('#section').attr('data-section');
        //var control = '#post_area_template';

        err_label.fadeTo(0,0);
        if (content.length === 0) {
            err_label.text("You must add content before you can submit this comment!");
            err_label.fadeTo(1000,1);
        } else {
            err_label.text("");
            var url = "/discussion_addcomment.htm?&i_email=" + i_email + "&course=" + course + "&section=" + section +"&post_id=" + post_id + "&content=" + content;

            $.ajax({
                method: 'get',
                url: url,
                dataType: 'text',
                success: function (comment_status) {
                    console.log("Comment-Success");
                    console.log("Comment-Response: " + comment_status);

                    switch(comment_status.trim()){
                        case SUCCESS:
                            load_discussionpage();
                            (comment_form.find('input[name=comment]')).val("");
                            break;
                        case ENROLLED:
                            break;
                        case NOT_ENROLLED:
                            err_label.text("You must first be enrolled in this course before you can comment on this page.");
                            err_label.fadeTo(1000,1);
                            break;
                        case NOT_LOGGED_IN:
                            err_label.text("You must login and enroll in this course before you can comment on this page.");
                            err_label.fadeTo(1000,1);
                            break;
                        case DUPLICATE_STUDENT:
                            err_label.text("There is a problem with our database. Please try again later.");
                            err_label.fadeTo(1000,1);
                            break;
                        case EMPTY_PARAMETERS:
                            err_label.text("You must add content before you can submit this comment.");
                            err_label.fadeTo(1000,1);
                            break;
                        case BOARD_NOT_FOUND:
                            err_label.text("We're having an issue locating the discussion board for this course. Please try again later.");
                            err_label.fadeTo(1000,1);
                            break;
                        case DATABASE_ERROR:
                            err_label.text("There is a problem with our database. Please try again later.");
                            err_label.fadeTo(1000,1);
                            break;
                        case POST_NOT_FOUND:
                            err_label.text("This post has been removed. Please refresh your page.");
                            err_label.fadeTo(1000,1);
                            break;
                        case NO_ACCESS:
                            err_label.text("You don't have the access rights to delete this post.");
                            err_label.fadeTo(1000,1);
                            break;

                    }
                },
                error: function () {
                    console.log("Comment-Error: Aw, It didn't connect to the servlet :(");
                }

            });
        }


    });


    /**
     * Sends data to a backend function to allow a user to delete a post on the current page.
     * @author Karl Jean-Brice
     */
    $('body').on('click', 'a.link_btn.delete_post_btn', function () {

        var post_id = $(this).attr("data-postid");
        var err_label = $('#err-deletepost');

        $('#val-deletepost').attr('data-postid',post_id);
        err_label.text("");

    });
    $('body').on('click', '#deletepost-submit', function () {

        var i_email = $('#i_email').attr('data-iemail');
        var course = $('#course').attr('data-course');
        var section = $('#section').attr('data-section');
        var post_id = $('#val-deletepost').attr('data-postid');
        var err_label = $('#err-deletepost');

        var url = "/discussion_deletepost.htm?&i_email=" + i_email + "&course=" + course + "&section=" + section + "&post_id=" + post_id;
        err_label.fadeTo(0,0);
        $.ajax({
            method: 'get',
            url: url,
            dataType: 'text',
            success: function (delete_status) {
                console.log("Post:Delete-Success");
                console.log("Post:Delete-Response: " + delete_status);
                switch(delete_status.trim()){
                    case SUCCESS:
                        load_discussionpage();
                        $('#close-deletepost').click();
                        break;
                    case ENROLLED:
                        break;
                    case NOT_ENROLLED:
                        err_label.text("You must first be enrolled in this course before you can delete a post on this page.");
                        err_label.fadeTo(1000,1);
                        break;
                    case NOT_LOGGED_IN:
                        err_label.text("You must login and enroll in this course before you can delete a post on this page.");
                        err_label.fadeTo(1000,1);
                        break;
                    case DUPLICATE_STUDENT:
                        err_label.text("There is a problem with our database. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case EMPTY_PARAMETERS:
                        err_label.text("Unable to delete to this post. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case BOARD_NOT_FOUND:
                        err_label.text("We're having an issue locating the discussion board for this course. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case DATABASE_ERROR:
                        err_label.text("There is a problem with our database. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case POST_NOT_FOUND:
                        err_label.text("This post has already been deleted or doesn't exist. Please refresh your page.");
                        err_label.fadeTo(1000,1);
                        break;
                }
            },
            error: function () {
                console.log("Post:Delete: Aw, It didn't connect to the servlet :(");
            }

        });


    });


    /**
     * Sends data to a backend function to allow a user to delete a comment on the current page.
     * @author Karl Jean-Brice
     */
    $('body').on('click', 'a.link_btn.delete_comment_btn', function () {
        var err_label = $('#err-deletecomment');
        var comment_id = $(this).attr("data-commentid");

        $('#val-deletecomment').attr('data-commentid',comment_id);
        err_label.text("");
    });
    $('body').on('click', '#deletecomment-submit', function () {

        var i_email = $('#i_email').attr('data-iemail');
        var course = $('#course').attr('data-course');
        var section = $('#section').attr('data-section');
        var comment_id = $('#val-deletecomment').attr('data-commentid');
        var err_label = $('#err-deletecomment');

        var url = "/discussion_deletecomment.htm?&i_email=" + i_email + "&course=" + course + "&section=" + section + "&comment_id=" + comment_id;
        err_label.fadeTo(0,0);
        $.ajax({
            method: 'get',
            url: url,
            dataType: 'text',
            success: function (delete_status) {
                console.log("Comment:Delete-Success");
                console.log("Comment:Delete-Response: " + delete_status);
                switch(delete_status.trim()){
                    case SUCCESS:
                        load_discussionpage();
                        $('#close-deletecomment').click();
                        break;
                    case ENROLLED:
                        break;
                    case NOT_ENROLLED:
                        err_label.text("You must first be enrolled in this course before you can delete a comment on this page.");
                        err_label.fadeTo(1000,1);
                        break;
                    case NOT_LOGGED_IN:
                        err_label.text("You must login and enroll in this course before you can delete a comment on this page.");
                        err_label.fadeTo(1000,1);
                        break;
                    case DUPLICATE_STUDENT:
                        err_label.text("There is a problem with our database. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case EMPTY_PARAMETERS:
                        err_label.text("Unable to delete to this comment. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case BOARD_NOT_FOUND:
                        err_label.text("We're having an issue locating the discussion board for this course. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case DATABASE_ERROR:
                        err_label.text("There is a problem with our database. Please try again later.");
                        err_label.fadeTo(1000,1);
                        break;
                    case POST_NOT_FOUND:
                        err_label.text("This comment has already been deleted or doesn't exist. Please refresh your page.");
                        err_label.fadeTo(1000,1);
                        break;
                }
            },
            error: function () {
                console.log("Post:Delete: Aw, It didn't connect to the servlet :(");
            }

        });


    });


    /**
     * Sends data to a backend function to allow the user to up vote a post.
     * @author Karl Jean-Brice
     */
    $('body').on('click', 'a.link_btn.like_post_btn', function () {

        var i_email = $('#i_email').attr('data-iemail');
        var course = $('#course').attr('data-course');
        var section = $('#section').attr('data-section');
        var transaction_type = "like";
        var for_type = "post"
        var post_id = $(this).attr("data-postid");

        var url = "/discussion_voterequest.htm?&i_email=" + i_email + "&course=" + course + "&section=" + section + "&id=" + post_id + "&type=" + transaction_type + "&for=" + for_type;

        $.ajax({
            method: 'get',
            url: url,
            dataType: 'text',
            success: function (post_status) {
                console.log("Post:Like-Success");
                console.log("Post:Like-Response: " + post_status);
                switch(post_status.trim()){
                    case SUCCESS:
                        load_discussionpage();
                        break;
                    case ENROLLED:
                        break;
                    case NOT_ENROLLED:
                        break;
                    case NOT_LOGGED_IN:
                        break;
                    case DUPLICATE_STUDENT:
                        break;
                    case EMPTY_PARAMETERS:
                        break;
                    case BOARD_NOT_FOUND:
                        break;
                    case DATABASE_ERROR:
                        break;
                    case POST_NOT_FOUND:
                        break;
                }
            },
            error: function () {
                console.log("Post:Like: Aw, It didn't connect to the servlet :(");
            }

        });
    });


    /**
     * Sends data to a backend function to allow the user to down vote a post.
     * @author Karl Jean-Brice
     */
    $('body').on('click', 'a.link_btn.dislike_post_btn', function () {

        var i_email = $('#i_email').attr('data-iemail');
        var course = $('#course').attr('data-course');
        var section = $('#section').attr('data-section');
        var transaction_type = "dislike";
        var for_type = "post"
        var post_id = $(this).attr("data-postid");

        var url = "/discussion_voterequest.htm?&i_email=" + i_email + "&course=" + course + "&section=" + section + "&id=" + post_id + "&type=" + transaction_type + "&for=" + for_type;

        $.ajax({
            method: 'get',
            url: url,
            dataType: 'text',
            success: function (post_status) {
                console.log("Post:Dislike-Success");
                console.log("Post:Dislike-Response: " + post_status);
                switch(post_status.trim()){
                    case SUCCESS:
                        load_discussionpage();
                        break;
                    case ENROLLED:
                        break;
                    case NOT_ENROLLED:
                        break;
                    case NOT_LOGGED_IN:
                        break;
                    case DUPLICATE_STUDENT:
                        break;
                    case EMPTY_PARAMETERS:
                        break;
                    case BOARD_NOT_FOUND:
                        break;
                    case DATABASE_ERROR:
                        break;
                    case POST_NOT_FOUND:
                        break;
                }
            },
            error: function () {
                console.log("Post:Dislike: Aw, It didn't connect to the servlet :(");
            }

        });
    });


    /**
     * Sends data to a backend function to allow the user to up vote a comment.
     * @author Karl Jean-Brice
     */
    $('body').on('click', 'a.link_btn.like_comment_btn', function () {

        var i_email = $('#i_email').attr('data-iemail');
        var course = $('#course').attr('data-course');
        var section = $('#section').attr('data-section');
        var transaction_type = "like";
        var for_type = "comment"
        var post_id = $(this).attr("data-commentid");

        var url = "/discussion_voterequest.htm?&i_email=" + i_email + "&course=" + course + "&section=" + section + "&id=" + post_id + "&type=" + transaction_type + "&for=" + for_type;

        $.ajax({
            method: 'get',
            url: url,
            dataType: 'text',
            success: function (comment_status) {
                console.log("Comment:Like-Success");
                console.log("Comment:Like-Response: " + comment_status);
                switch(comment_status.trim()){
                    case SUCCESS:
                        load_discussionpage();
                        break;
                    case ENROLLED:
                        break;
                    case NOT_ENROLLED:
                        break;
                    case NOT_LOGGED_IN:
                        break;
                    case DUPLICATE_STUDENT:
                        break;
                    case EMPTY_PARAMETERS:
                        break;
                    case BOARD_NOT_FOUND:
                        break;
                    case DATABASE_ERROR:
                        break;
                    case POST_NOT_FOUND:
                        break;
                }
            },
            error: function () {
                console.log("Comment:Like: Aw, It didn't connect to the servlet :(");
            }

        });
    });



    /**
     * Sends data to a backend function to allow the user to down vote a comment.
     * @author Karl Jean-Brice
     */
    $('body').on('click', 'a.link_btn.dislike_comment_btn', function () {

        var i_email = $('#i_email').attr('data-iemail');
        var course = $('#course').attr('data-course');
        var section = $('#section').attr('data-section');
        var transaction_type = "dislike";
        var for_type = "comment"
        var post_id = $(this).attr("data-commentid");

        /*
         String instructor_email = request.getParameter("i_email");
         String course = request.getParameter("course");
         String request_for = request.getParameter("for");
         String request_type = request.getParameter("type");
         String request_id = request.getParameter("id");
         */

        var url = "/discussion_voterequest.htm?&i_email=" + i_email + "&course=" + course + "&section=" + section + "&id=" + post_id + "&type=" + transaction_type + "&for=" + for_type;

        $.ajax({
            method: 'get',
            url: url,
            dataType: 'text',
            success: function (comment_status) {
                console.log("Comment:Dislike-Success");
                console.log("Comment:Dislike-Response: " + comment_status);
                switch(comment_status.trim()){
                    case SUCCESS:
                        load_discussionpage();
                        break;
                    case ENROLLED:
                        break;
                    case NOT_ENROLLED:
                        break;
                    case NOT_LOGGED_IN:
                        break;
                    case DUPLICATE_STUDENT:
                        break;
                    case EMPTY_PARAMETERS:
                        break;
                    case BOARD_NOT_FOUND:
                        break;
                    case DATABASE_ERROR:
                        break;
                    case POST_NOT_FOUND:
                        break;
                }
            },
            error: function () {
                console.log("Comment:Dislike: Aw, It didn't connect to the servlet :(");
            }

        });
    });




});