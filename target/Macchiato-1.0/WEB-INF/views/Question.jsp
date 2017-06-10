<%--
  Created by IntelliJ IDEA.
  User: Raymond
  Date: 4/6/2017
  Time: 3:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Macchiato</title>
    <link rel="stylesheet" href="css/app.css">
    <link rel="stylesheet" href="icons/foundation-icons/foundation-icons/foundation-icons.css"/>
    <link rel="stylesheet" href="css/assignment.css">
    <link href="https://fonts.googleapis.com/css?family=Cormorant" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">


</head>

<body>
<!-- TOP BAR -->
<div class="top-bar-container " data-sticky-container>
    <div class="sticky" data-sticky data-options="stickTo:top; marginTop:0; stickyOn:small;" data-check-every="0">
        <div class="top-bar">
            <div class="top-bar-left">
                <ul class="menu">
                    <li><img src="images/Macchiato.png" alt="Mock Image" height="32" width="32"></li>
                    <li><a href="javascript:void(0);" class="scroll-nav logo-name">Macchiato</a></li>

                </ul>
            </div>
            <div class="top-bar-right">
                <ul id = "home-area" class="dropdown menu">
                    <li style="visibility: hidden;"><a href="javascript:void(0)" class="project-btn-styles scroll-nav">Invalid</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>

<div class="top_bar_container" style="padding:1.5%;" >
    <div class="top_bar">
        <div class="top-bar-right">
            <ul class="dropdown menu" data-dropdown-menu>
                <li>
                    <a id = "nav_questions"  href="javascript:void(0)" class="scroll-nav link-highlight">Questions</a>
                    <ul id = "load_question_area" class="menu forum-links submenu-test">

                    </ul>

                </li>
            </ul>
        </div>
    </div>
</div>
<!-- END TOP BAR -->


<!-- MODAL AREA -->
<%--<div class="student_bar">--%>
    <%--<span class="ques_list">--%>
        <%--<img src="images/barline.png" width="50" height="50" class="quesbtn">--%>
	<%--<div class="question-content">--%>
    <%--<a href="#">Question 1</a>--%>
    <%--<a href="#">Question 2</a>--%>
    <%--<a href="#">Question 3</a>--%>
  <%--</div>--%>
    <%--</span>--%>
<%--</div>--%>
<div class="ques_box">
    <div class="ques_num">QUESTION ERROR</div>
    <div class="the_ques">QUESTION ERROR.</div>
    <button class="sol_box">VIEW SOLUTION</button>
    <div id="dialog" style="display: none"; title="Solution">
        <p id = "solution" >This is the default dialog which is useful for displaying information. The dialog window can be moved, resized and closed with the 'x' icon.</p>
    </div>
    <div id="dialog1" style="display: none"; title="Submit Code">
        <p id = "code" >This is the default dialog which is useful for displaying information. The dialog window can be moved, resized and closed with the 'x' icon.</p>
    </div>
    <div id="dialog2" style="display: none"; title="Compiler Output">
        <p id = "output" >This is the default dialog which is useful for displaying information. The dialog window can be moved, resized and closed with the 'x' icon.</p>
    </div>
    <div id="dialog3" style="display: none"; title="Submission Output">
        <p id = "output1" >This is the default dialog which is useful for displaying information. The dialog window can be moved, resized and closed with the 'x' icon.</p>
    </div>

    <textarea name="Text1" id="myText" cols="40" rows="5"></textarea>
    <button class="compile_box">COMPILE AND SAVE</button>
    <button class="sub_box">SUBMIT</button>
    <div class = "prev_next_box">
    <span class = "prev_space"><button class="prev_box">PREV</button>
</span>
        <span class = "next_space"><button class="next_box">NEXT</button>
</span>
    </div>

</div>


<!-- END MODAL AREA -->


<section class="row">
    <div class="small-4 medium-4 large-4 columns">

    </div>
    <div class="small-4 medium-4 large-4 columns">

    </div>
    <div class="small-4 medium-4 large-4 columns">

    </div>

</section>

<script id= "question-list-template" type="text/x-handle-template">

    {{#each Questions}}
    <li class="list_file" data-id="{{id}}">
        <a href="javascript:void(0)">{{id}}</a>
    </li>
    {{else}}
    {{/each}}

</script>

<script id= "home-page-template" type="text/x-handle-template">
    {{#if User}}
    <li style="visibility: hidden;"><a href="javascript:void(0)" class="project-btn-styles scroll-nav">Invalid</a></li>
    <li>
        <a id="current-user" href="javascript:void(0)" data-username="{{User.email}}" class="scroll-nav welcome-name ">Hi, {{User.email}}</a>
    </li>
    <li><a href="/{{User.home}}" class="scroll-nav link-highlight">Home</a></li>
    <li >
        <a id = "nav_forum"  href="javascript:void(0)" class="scroll-nav link">Forum</a>
        <ul id = "load_forum_area" class="menu forum-links submenu-test">

        </ul>
        <form id="link-form" action=" " method="post" style="display: none;">
            <input name = "i_email" id = "form-iemail" type = "text" style="display:none;">
            <input name = "course" id = "form-course" type = "text" style="display:none;">
            <input name = "section" id = "form-section" type = "text" style="display:none;">
        </form>
    </li>
    <li>
        <a href="/CourseInfo.htm" class="scroll-nav link">Course Info</a>
    </li>
    <li>
        <a href="/login.htm?access=-1" class="scroll-nav link">Logout</a>
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


<script id="discussion-list-template" type="text/x-handle-template">

    {{#each Courses}}
    <li style="white-space: nowrap;" class="list_file" data-list-email="{{instructorEmail}}" data-course="{{course}}" data-section="{{section}}">
        <a class ="list-size" href="javascript:void(0)">{{course}}: {{section}}</a>
    </li>
    {{else}}
    <li><a class ="list-size" href="javascript:void(0)">None Available</a></li>
    {{/each}}

</script>


<script src="https://code.jquery.com/jquery-3.1.1.min.js" type="text/javascript"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="libs/handlebars-v4.0.5.js" type="text/javascript"></script>
<script src="js/nav_bar_transactions.js" type="text/javascript"></script>
<script src="js/vendor/jquery.js"></script>
<script src="js/vendor/what-input.js"></script>
<script src="js/vendor/foundation.js"></script>
<script src="js/app.js"></script>
<script src="js/question_transactions.js"></script>
</body>


</html>

<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
