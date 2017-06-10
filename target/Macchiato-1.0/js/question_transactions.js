/**
 * Created by Raymond on 4/7/2017.
 */

/**
 * This javascript will handle all the actions a user will perform on the question webpage
 */

$.noConflict();
$(document).ready(function () {

    var JSON_list_items;
    var JSON_list_items1;
    var $url = "/PopulateQues.htm";
    var i = 0;
    var $url1 = "/PopulateQuesInfo.htm";
    var submitted = "no";

    generateQuestionList($url, $url1);


    /**
     * Allows the user to move onto the next question
     */
    $('body').on('click','.next_box', function(e) {

        clearText();
        nextQues();
    });

    /**
     * Allows the user to move onto the prev question
     */
    $('body').on('click','.prev_box', function(e) {

        clearText();
        if (i == 0){
            i = 2;
        }
        else{
            i--;
        }
        $('.ques_num').text(JSON_list_items.Questions[i].id);
        $('.the_ques').text(JSON_list_items.Questions[i].problem);
        $('#myText').val(JSON_list_items.Questions[i].studentanswer);
        localStorage.setItem("questionNum",JSON_list_items.Questions[i].questionKey);
    });

    /**
     * Allows the user to see the solution
     */
    $('body').on('click','.sol_box', function(e){

        $('#solution').text(JSON_list_items.Questions[i].teacherAnswer);
        $('#dialog').dialog();
        var $url5 = "/SubmitSol.htm?" + "&questionKey=" + JSON_list_items.Questions[i].questionKey;
        $.ajax({
            method: 'get',
            url: $url5,
            dataType: 'text',
            success: function (text_field){
                if(text_field != ""){
                    if(text_field == "cheated"){
                        $('#output1').html("Since, You Viewed The Solution, No Credit Will Be Assigned.");
                        $('#dialog3').dialog({height:'auto',width:'auto'});
                    }
                }

            },
            error: function () {
                console.log("Solution Failure: Aw, It didn't connect to the servlet :(");
            }
        });

    });

    /**
     * Allows the user to check their code that they wrote to see if it has errors or it is fine
     */
    $('body').on('click','.compile_box', function(e){

        $(".sub_box").show();

        $('#output').val('');
        var text = $('#myText').val();
        var num = localStorage.getItem("questionNum");
        //text = text.replace(/\r?\n/g, '<br />');
        submitted = "no";
        var $url3 = "/Compile.htm?" + "&text=" + text + "&num=" + num + "&submitted=" + submitted;

        $.ajax({
            method: 'post',
            url: $url3,
            dataType: 'text',
            success: function (text_field){
                if(text_field != ""){
                    var new_text = text_field.replace(/<br\s?\/?>/g,"\n");
                    $('#output').html(new_text);
                    $('#dialog2').dialog({height:'auto',width:'auto'});


                    $.ajax({
                        method: 'get',
                        url: $url,
                        dataType: 'json',
                        success: function (question_table) {
                            console.log("Get Questions :Success");
                            JSON_list_items = question_table;
                            $('.ques_num').text(JSON_list_items.Questions[i].id);
                            $('.the_ques').text(JSON_list_items.Questions[i].problem);

                            $.ajax({
                                method: 'get',
                                url: $url1,
                                dataType: 'json',
                                success: function (questionInfo_table) {
                                    console.log("Get QuestionsInfo :Success");
                                    JSON_list_items1 = questionInfo_table;
                                    $('#myText').val(JSON_list_items.Questions[i].studentanswer);
                                    localStorage.setItem("questionNum",JSON_list_items.Questions[i].questionKey);
                                },
                                error: function () {
                                    console.log("Get QuestionsInfo Failure: Aw, It didn't connect to the servlet :(");
                                }
                            });

                        },
                        error: function () {
                            console.log("Get Questions Failure: Aw, It didn't connect to the servlet :(");
                        }
                    });


                }
                else{
                    $('#output').html("Could Not Compile Your Code!");
                    $('#dialog2').dialog({height:'auto',width:'auto'});
                }

            },
            error: function () {
                console.log("Compile Failure: Aw, It didn't connect to the servlet :(");
            }
        });

    });

    /**
     * Allows the user to submit their code  so that they can get a point value associated with each
     * question
     */
    $('body').on('click','.sub_box', function(e){

        $('#code').text( $('#myText').val());
        $( "#dialog1" ).dialog({
            resizable: false,
            height: "auto",
            width: 400,
            modal: true,
            buttons: {
                "Yes": function() {
                    $( this ).dialog( "close" );
                    var text = $('#myText').val();
                    var num = localStorage.getItem("questionNum");
                    submitted = "yes";
                    //text = text.replace(/\r?\n/g, '<br />');
                    var $url4 = "/Compile.htm?" + "&text=" + text + "&num=" + num + "&submitted=" + submitted;
                    $.ajax({
                        method: 'post',
                        url: $url4,
                        dataType: 'text',
                        success: function (text_field){
                            if(text_field != ""){
                                if(text_field == "correct"){
                                    $('#output1').html("Congratulations You Got It Correct!");
                                    $('#dialog3').dialog({height:'auto',width:'auto'});
                                }
                                else if(text_field == "wrong"){
                                    $('#output1').html("Sorry Try Again.");
                                    $('#dialog3').dialog({height:'auto',width:'auto'});
                                }
                                else if(text_field == "cheated"){
                                    $('#output1').html("Since, You Viewed The Solution Or Got It Correct, No Credit Will Be Assigned.");
                                    $('#dialog3').dialog({height:'auto',width:'auto'});
                                }

                            }
                            else{
                                $('#output1').html("Could Not Submit Your Code!");
                                $('#dialog3').dialog({height:'auto',width:'auto'});
                            }

                        },
                        error: function () {
                            console.log("Compile Failure: Aw, It didn't connect to the servlet :(");
                        }
                    });

                  //  clearText();
                    nextQues();
                },
                No: function() {
                    $( this ).dialog( "close" );
                }
            }
        });
    });

    /**
     * Helper function to populate the web page by modifying the elements in the jsp
     */
    function nextQues(){
        if (i == JSON_list_items.Questions.length - 1){
            i = 0;
        }
        else {
            i++;
        }
        $('.ques_num').text(JSON_list_items.Questions[i].id);
        $('.the_ques').text(JSON_list_items.Questions[i].problem);
        $('#myText').val(JSON_list_items.Questions[i].studentanswer);
        localStorage.setItem("questionNum",JSON_list_items.Questions[i].questionKey);
    }

    /**
     * Helper function to clear what a user has written in a text area
     */
    function clearText(){

        $('#myText').val('');
    }

    /**
     * Function used to load questions on the dropdown
     * @param question_list
     */
    function load_question_list(question_list){
        var question_area = $('#load_question_area');
        var hostname = window.location.host;
        question_area.html("<li><a href='javascript:void(0)'>Searching...</a></li>");
        var source = $('#question-list-template').html();
        var question_list_template = Handlebars.compile(source);
        var list_data = question_list_template(question_list);

        question_area.html(list_data);
    }

    $('body').on('click mouseover', '#nav_questions', function () {
        console.log("HOVERING");
        load_question_list(JSON_list_items);
    });

    $('body').on('click', 'li[data-id]', function () {
        var questionId = $(this).attr('data-id');
        var updated_questionId = questionId.replace('Question', '');
        var updated_numI = Number(updated_questionId) - 1;
        i = updated_numI;

       generateQuestionList($url, $url1);

    });

    function generateQuestionList($url, $url1){
        $(".sub_box").hide();

        $.ajax({
            method: 'get',
            url: $url,
            dataType: 'json',
            success: function (question_table) {
                console.log("Get Questions :Success");
                JSON_list_items = question_table;
                $('.ques_num').text(JSON_list_items.Questions[i].id);
                $('.the_ques').text(JSON_list_items.Questions[i].problem);

                $.ajax({
                    method: 'get',
                    url: $url1,
                    dataType: 'json',
                    success: function (questionInfo_table) {
                        console.log("Get QuestionsInfo :Success");
                        JSON_list_items1 = questionInfo_table;
                        $('#myText').val(JSON_list_items.Questions[i].studentanswer);
                        localStorage.setItem("questionNum",JSON_list_items.Questions[i].questionKey);
                    },
                    error: function () {
                        console.log("Get QuestionsInfo Failure: Aw, It didn't connect to the servlet :(");
                    }
                });

            },
            error: function () {
                console.log("Get Questions Failure: Aw, It didn't connect to the servlet :(");
            }
        });

    }

});

