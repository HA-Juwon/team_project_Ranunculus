$(document).ready(function(){
    $("#contents").click(function(){
        $("#popup").fadeIn();
    });
    $("#popup").click(function(){
        $("#popup").fadeOut();
    });
});