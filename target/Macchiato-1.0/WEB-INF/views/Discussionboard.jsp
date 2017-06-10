<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!doctype html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Macchiato</title>
    <link rel="icon" href="images/favicon.png" type="image/png"/>
    <link rel="stylesheet" href="css/app.css">
    <link rel="stylesheet" href="icons/foundation-icons/foundation-icons/foundation-icons.css"/>
    <link href="https://fonts.googleapis.com/css?family=Cormorant" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
</head>

<body id="body-style">
<!-- TOP BAR -->
<div class="top-bar-container " data-sticky-container>
    <div class="sticky" data-sticky data-options="stickTo:top; marginTop:0; stickyOn:small;" data-check-every="0">
        <div class="top-bar">
            <div class="top-bar-left">
                <ul class="menu">
                    <li><img src="images/Macchiato.png" alt="JTE Image" height="32" width="32"></li>
                    <li><a href="#" class="scroll-nav logo-name">Macchiato</a></li>

                </ul>
            </div>
            <div class="top-bar-right">
                <ul id ="home-area" class="dropdown menu">

                </ul>
            </div>
        </div>
    </div>
</div>

<!-- END TOP BAR -->


<!-- MODAL AREA -->
<div id="change_username_modal" class="reveal"  data-reveal data-close-on-esc="false" data-close-on-click = "false" data-animation-in="slide-in-down fast" data-animation-out="slide-out-up fast">
    <a id="close-username-modal" class="close-button" data-close>&#215;</a>
    <div class = "modal-heading-form">
        <h3 class ="modal-heading-text">Change Your Username</h3>
    </div>
    <label><span class = "label-style-modal">Username (3 characters minimum)</span>
        <input id="txt-username" type="text" maxlength="30" placeholder="Type your username here...">
    </label>

    <span id="err-changeusername" class="lbl-error" style="opacity: 0;"></span>

    <div id = "btn-username-submit" class="menu modal-area btn-username-submit">
        <span class="modal-btn-full">Submit Request</span>
    </div>


    <p class="text-center"><a href="#"></a></p>

</div>


<div id="create_post_modal" class="reveal" data-reveal data-animation-in="slide-in-down fast" data-animation-out="slide-out-up fast">
    <a id = "close-createpost" class="close-button" data-close>&#215;</a>
    <div class = "modal-heading-form">
        <h3 class = "modal-heading-text">New Post</h3>
    </div>
    <label><span class = "label-style-modal">Title</span>
        <input id = "post-title" type="text" placeholder="Type your post title here...">
    </label>

    <label><span class = "label-style-modal">Content</span>
        <textarea id = "post-feed" class="height-transition comment_input"
                  placeholder="Write your post content here..."></textarea>
    </label>

    <span id="err-createpost" class="lbl-error" style="opacity: 0;"></span>

    <div id="post-submit" class="menu modal-area btn-post-submit">
        <span class="modal-btn-full">Create Post</span>
    </div>


    <p class="text-center"><a href="#"></a></p>

</div>


<div id="edit_post_modal" class="reveal" data-reveal data-animation-in="slide-in-down fast" data-animation-out="slide-out-up fast">
    <a id = "close-editpost" class="close-button" data-close>&#215;</a>
    <div class = "modal-heading-form">
        <h3 class = "modal-heading-text">Modify Post</h3>
    </div>
    <label><span class = "label-style-modal">Title</span>
        <input id="edit-title" type="text" placeholder="Type your post title here...">
    </label>

    <label><span class = "label-style-modal">New Content</span>
        <textarea id="edit-post-feed" class="height-transition comment_input"
                  placeholder="Write your post content here..."></textarea>
    </label>

    <span id="err-editpost" class="lbl-error" style="opacity:0;"></span>
    <span id="val-editpost" data-postid="" style="display:none;"></span>


    <div id="editpost-submit" class="menu modal-area btn-post-submit">
        <span class="modal-btn-full">Confirm Changes</span>
    </div>


    <p class="text-center"><a href="#"></a></p>

</div>


<div id="edit_comment_modal" class="reveal" data-reveal data-animation-in="slide-in-down fast" data-animation-out="slide-out-up fast">
    <a id = "close-editcomment" class="close-button" data-close>&#215;</a>
    <div class = "modal-heading-form">
        <h3 class = "modal-heading-text">Modify Comment</h3>
    </div>

    <label><span class = "label-style-modal">New Content</span>
        <textarea id="edit-comment-feed" class="height-transition comment_input"
                  placeholder="Write your comment content here..."></textarea>
    </label>

    <span id="err-editcomment" class="lbl-error" style="opacity:0;"></span>
    <span id="val-editcomment" data-commentid="" style="display:none;"></span>

    <div id = "editcomment-submit" class="menu modal-area btn-post-submit">
        <span class="modal-btn-full">Confirm Changes</span>
    </div>


    <p class="text-center"><a href="#"></a></p>

</div>


<div id="delete_post_modal" class="reveal" data-reveal data-animation-in="slide-in-down fast" data-animation-out="slide-out-up fast">
    <a id="close-deletepost" class="close-button" data-close>&#215;</a>
    <div class = "modal-heading-form">
        <h3 class = "modal-heading-text">Delete Confirmation</h3>
    </div>

    <div class = "text-center">
        <span class = "notification-style-modal">Are you sure you want to delete this post?</span>
    </div>

    <span id="err-deletepost" class="lbl-error"></span>
    <span id="val-deletepost" data-postid="" style="display:none;"></span>

    <div id = "deletepost-submit" class="menu modal-area btn-deletepost-submit">
        <span class="modal-btn-full">Delete Post</span>
    </div>


    <p class="text-center"><a href="#"></a></p>

</div>


<div id="delete_comment_modal" class="reveal" data-reveal data-animation-in="slide-in-down fast" data-animation-out="slide-out-up fast">
    <a id="close-deletecomment" class="close-button" data-close>&#215;</a>
    <div class = "modal-heading-form">
        <h3 class = "modal-heading-text">Delete Confirmation</h3>
    </div>

    <div class = "text-center">
        <span class = "notification-style-modal">Are you sure you want to delete this comment?</span>
    </div>

    <span id="err-deletecomment" class="lbl-error"></span>
    <span id="val-deletecomment" data-commentid="" style="display:none;"></span>

    <div id ="deletecomment-submit" class="menu modal-area btn-deletepost-submit">
        <span class="modal-btn-full">Delete Comment</span>
    </div>


    <p class="text-center"><a href="#"></a></p>

</div>




<!-- END MODAL AREA -->



<!-- COURSE INFORMATION SECTION -->
<section id="forum-stats-section">
    <div class="top-bar-container">
        <div class="top-bar">
            <div class="top-bar-center">
                <ul class="menu">
                    <li><a  href="javascript:void(0)" id = "navbar-instructor"  class="scroll-nav"></a></li>
                    <li><a  href="javascript:void(0)" id = "navbar-course" ></a></li>
                    <li><a  href="javascript:void(0)" id = "navbar-enrolled" ></a></li>
                    <li><a  href="javascript:void(0)" id = "navbar-posts" ></a></li>
                    <li><a  href="javascript:void(0)" id = "navbar-comments" ></a></li>

                </ul>
            </div>
        </div>
    </div>
</section>
<!-- END COURSE INFORMATION SECTION -->


<section class="row">
    <!-- DISCUSSION AREA -->
    <div id="extra-section" class="small-1 medium-1 large-1 columns">
        <span id ="i_email" data-iemail="${i_email}" style="display:none;"></span>
        <span id ="course" data-course="${course}" style="display:none;" ></span>
        <span id ="section" data-section="${section}" style="display:none;" ></span>
        <span id ="u_set" data-uset="" style="display:none;" ></span>
    </div>

    <div id="discussion-section" class="small-10 medium-10 large-10 columns">
        <div class="post-area">
            <div class="wrapper">
                <div class="middle_box">
                    <div class="clear"></div>
                    <div class="feed_div">
                        <div id="post_area_template" class="post-area-template">

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="stats-section" class="small-1 medium-1 large-1 columns">

    </div>

</section>

<script id= "home-page-template" type="text/x-handle-template">
    {{#if User}}
    <li style="visibility: hidden;"><a href="javascript:void(0)" class="project-btn-styles scroll-nav">Invalid</a></li>
    <li>
        <a id="current-user" href="javascript:void(0)" data-username="{{User.email}}" class="scroll-nav welcome-name ">Hi, {{User.email}}</a>
    </li>
    <li><a href="/{{User.home}}" class="scroll-nav link">Home</a></li>
    <li >
        <a id = "nav_forum"  href="javascript:void(0)" class="scroll-nav link-highlight">Forum</a>
        <ul id = "load_forum_area" class="menu forum-links submenu-test">

        </ul>
        <form id="link-form" action=" " method="post" style="display: none;">
            <input name = "i_email" id = "form-iemail" type = "text" style="display:none;">
            <input name = "course" id = "form-course" type = "text" style="display:none;">
            <input name = "section" id = "form-section" type = "text" style="display:none;">
        </form>
    </li>
    <li>
        <a href="/login.htm?access=-1" class="scroll-nav link">Logout</a>
    </li>
    <li>
        <a href="javascript:void(0)"  class="project-btn-styles scroll-nav" id="create-new-post" data-open="create_post_modal">Create New Post</a>
    </li>
    <li class="extra-padding-left">
        <a href="javascript:void(0)" class="project-btn-styles scroll-nav" id="change-user-name" data-open="change_username_modal">Change User Name</a>
    </li>
    {{else}}
    <li>
        <a href="javascript:void(0)" data-username="{{User.email}}" class="scroll-nav welcome-name ">You're not logged in!</a>
    </li>
    <li>
        <a href="/Home.htm" class="scroll-nav link">Go to login portal</a>
    </li>
    {{/if}}
</script>


<script id= "discussion-list-template" type="text/x-handle-template">

    {{#each Courses}}
    <li style="white-space: nowrap;" class="list_file" data-list-email="{{instructorEmail}}" data-course="{{course}}" data-section="{{section}}">
        <a class ="list-size" href="javascript:void(0)">{{course}}: {{section}}</a>
    </li>
    {{else}}
    <li><a class ="list-size" href="javascript:void(0)" >None Available</a></li>
    {{/each}}

</script>


<script id = "discussion-area-template" type = "text/x-handle-template">

    {{#each Posts}}
    <div class="feed_box clearfix" data-postid="{{postID}}">
        <div class="feed_left">
            <p><img class="userimg" src="images/user_icon.gif"></p>
            <p data-access="{{pAccess}}">{{pUsername}}</p>
        </div>
        <div class="feed_right">
            <span data-postid="{{postID}}" class="h2-title">{{postTitle}}</span>
            <span class="h2-title small-padding fi-comments"></span>
            <span class="comment-count">{{pCommentCount}}</span>
            <span class= "h2-title small-padding fi-arrow-up arrow-up"></span>


            <span data-postid="{{postID}}" class="block-style post_refresh">
                {{postContent}}
            </span>
            <p class="likebox"><i class="fi-like"></i><span class="like-count">&nbsp;{{pLikes}}</span>&nbsp;<span
                    class="darker-gray">·</span>
                <a class="link_btn like_post_btn" data-post-userid="{{pEnrollmentID}}" data-postid="{{postID}}" href="javascript:void(0)">Like</a>&nbsp;<span
                        class="darker-gray" >·</span>
                <a class="link_btn dislike_post_btn" data-post-userid="{{pEnrollmentID}}" data-postid="{{postID}}" href="javascript:void(0)" >Unlike</a>&nbsp;

                <span class="darker-gray" style="visibility: hidden;"  data-edit-permission="{{pEditPermission}}">·</span>

                <a class="link_btn edit_post_btn" data-edit-permission="{{pEditPermission}}" data-post-userid="{{pEnrollmentID}}" data-postid="{{postID}}" href="javascript:void(0)" data-open="edit_post_modal" style="visibility: hidden;">Edit</a>&nbsp;

                <span class="darker-gray" style="visibility: hidden;"  data-delete-permission="{{pDeletePermission}}">·</span>

                <a  class="link_btn delete_post_btn" style="visibility: hidden;" data-delete-permission="{{pDeletePermission}}" data-post-userid="{{pEnrollmentID}}" data-postid="{{postID}}" href="javascript:void(0)" data-open="delete_post_modal">Delete</a>
            </p>

            {{#each Comments}}
            <div class="comment_div">
                <div class=" comment_feed_left">
                    <p><img class="userimg" src="images/user_icon.gif"></p>
                    <p data-access="{{cAccess}}">{{cUsername}}</p>
                </div>
                <div class="comment_background comment_ele text-left">
                    <div class = "comment_content">
                        <span data-commentid="{{commentID}}" class="comment_refresh">{{commentContent}}<br></span>

                        <p class="likebox"><i class="fi-like"></i><span class="like-count">&nbsp;{{cLikes}}</span>&nbsp;
                            <span class="darker-gray">·</span>
                            <a class="link_btn like_comment_btn"
                               data-comment-userid="{{cEnrollmentID}}" data-commentid="{{commentID}}" href="javascript:void(0)">Like</a>&nbsp;
                            <span class="darker-gray">·</span>
                            <a class="link_btn dislike_comment_btn"
                               data-comment-userid="{{cEnrollmentID}}" data-commentid="{{commentID}}" href="javascript:void(0)">Unlike</a>&nbsp;

                            <span class="darker-gray" style="visibility: hidden;" data-edit-permission="{{cEditPermission}}" >·</span>

                            <a class="link_btn edit_comment_btn" data-open="edit_comment_modal"
                               style="visibility: hidden;" data-comment-userid="{{cEnrollmentID}}" data-commentid="{{commentID}}" data-edit-permission="{{cEditPermission}}" href="javascript:void(0)">Edit</a>&nbsp;

                            <span class="darker-gray" style="visibility: hidden;"  data-delete-permission="{{cDeletePermission}}">·</span>

                            <a class="link_btn delete_comment_btn" data-open="delete_comment_modal"
                               style="visibility: hidden;"  data-delete-permission="{{cDeletePermission}}" data-comment-userid="{{cEnrollmentID}}" data-commentid="{{commentID}}"  href="javascript:void(0)" >Delete</a>
                        </p>
                    </div>
                </div>

            </div>
            {{/each}}


            <form id="commentform_{{postID}}" class="comment-submit" action="javascript:void(0);"
                  method="get">
                <input name="action" value="comment" type="hidden">
                <input name="post_id" value="{{postID}}" type="hidden">
                <div class="comment_submit_section">
                    <span class="lbl-error" name="err_comment"><br></span>
                    <div class="comment_textarea">
                                                        <textarea class="height-transition comment_input"
                                                                  name="comment_content" id="comment_{{postID}}"
                                                                  placeholder="Write your comment here"></textarea>
                    </div>

                    <div class="comment_add extra-padding-bottom">
                        <ul class="menu">
                            <li class="comment-submit"><span class="comment-btn">Submit</span></li>
                            <li class="comment-cancel"> &nbsp;&nbsp;<span class="comment-btn">Cancel</span></li>
                        </ul>

                    </div>
                </div>
            </form>
        </div>

    </div>
    {{else}}
    <div id="discussion-text-area">
    <div class="row">
        <div class="center-text small-12 medium-12 large-12 columns">
            <span id="discussion-text-top">No Content to display</span>
        </div>
    </div>
    </div>
    {{/each}}

</script>
<!-- END DISCUSSION AREA -->





<script src="https://code.jquery.com/jquery-3.1.1.min.js" type="text/javascript"></script>
<script src="libs/handlebars-v4.0.5.js" type="text/javascript"></script>
<script src="js/discussion_board_transactions.js" type="text/javascript"></script>
<script src="js/nav_bar_transactions.js" type="text/javascript"></script>
<script src="js/vendor/jquery.js"></script>
<script src="js/vendor/what-input.js"></script>
<script src="js/vendor/foundation.js"></script>
<script src="js/app.js"></script>
</body>


</html>
