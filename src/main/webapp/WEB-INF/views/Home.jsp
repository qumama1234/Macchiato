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
                    <li><img src="images/Macchiato.png" alt="Mock Image" height="32" width="32"></li>
                    <li><a href="javascript:void(0);" class="scroll-nav logo-name">Macchiato</a></li>

                </ul>
            </div>
            <div class="top-bar-right">
                <ul id = "home-area" class="menu">
                    <li style="visibility: hidden;"><a href="javascript:void(0)" class="project-btn-styles scroll-nav">Invalid</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>
<!-- END TOP BAR -->



<section id="landing-section">
    <div id="landing-text-area">
        <div class="row">
            <div class="center-text small-12 medium-12 large-12 columns">
                <span id="landing-text-top"><b>Macchiato</b></span>
                <span id="landing-text-bottom">Make simple Java assignments and collaborate with your students, for free.</span>
                <br>
                <ul class=" landing-padding landing-btn-area menu">
                    <li><a href="#summary-section" class="btn-styles li-padding scroll-nav-home" id="learn-more">Learn more</a></li>
                </ul>
            </div>
        </div>
    </div>
</section>

<section id="summary-section" class="background-color-lightgray">
    <div class="summary-area-padding">
        <div class="row">
            <div class="center-text small-12 medium-12 large-12 columns h1-title-padding">
                <h1><span class = "h1-title">What is Macchiato?</span></h1>
            </div>
        </div>
        <div class="row">
            <div class="center-text small-12 medium-12 large-12 columns">
                <p><span class="summary-des">Macchiato is a web-application that provides a platform for instructors to create and assign simple Java problems to their students.
                This website also includes a discussion board, where students and instructors can work together to discuss assignments or anything related to the course. We hope you enjoy your stay!</span></p>
            </div>
        </div>
    </div>
</section>

<script id= "home-page-template" type="text/x-handle-template">
    {{#if User}}
    <li style="visibility: hidden;"><a href="javascript:void(0)" class="project-btn-styles scroll-nav" data-open="change_post_modal">Invalid</a></li>
    <li>
        <a id="current-user" href="javascript:void(0)" data-username="{{User.email}}" class="scroll-nav welcome-name ">Welcome, {{User.email}}</a>
    </li>
    <li><a href="/enter.htm" class="scroll-nav link">Enter</a></li>
    <li>
        <a href="/login.htm?access=-1" class="scroll-nav link">Logout</a>
    </li>
    {{else}}
    <li><a href="/login.htm?access=0" class="project-btn-styles scroll-nav" id="create-new-post" data-open="change_post_modal">&nbsp;&nbsp;Student Login&nbsp;&nbsp;</a></li>
    <li class="extra-padding-left"><a href="/login.htm?access=1" class="project-btn-styles scroll-nav" id="change-user-name" data-open="change_username_modal">Instructor Login</a></li>
    {{/if}}
</script>

<script src="https://code.jquery.com/jquery-3.1.1.min.js" type="text/javascript"></script>
<script src="libs/handlebars-v4.0.5.js" type="text/javascript"></script>
<script src="js/nav_bar_transactions.js" type="text/javascript"></script>
<script src="js/vendor/jquery.js"></script>
<script src="js/vendor/what-input.js"></script>
<script src="js/vendor/foundation.js"></script>
<script src="js/app.js"></script>
</body>


</html>
