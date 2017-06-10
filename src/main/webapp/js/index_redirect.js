/**
 * index_redirect.js
 * Purpose: This script redirects the index page to the Home page.
 * @author Karl Jean-Brice
 */
$(document).ready(function () {
    redirect_home();

    function redirect_home() {
        var home_form = $('#home-form');

        var url = "/Home.htm";
        home_form.attr("action", url);
        home_form.submit();
    }
});

