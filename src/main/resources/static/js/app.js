var stompClient = null;
var alias = null;
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    alias = $("#alias").val();
    $("#login-content").hide();
    $("#options").show();
    console.log("Alias is: "+alias);
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/topic/answers', function (greeting) {
            showAnswer(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/topic/quiz', function (data){
            console.log("det här kördes");
            getQuestion(JSON.parse(data.body).content);
        })
        stompClient.send("/app/connect",{}, "connected");

    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val(), 'message': $("#message").val()}));}
function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}
function showAnswer(message) {
    $("#otherPlayers").append("<tr><td>" + message + "</td></tr>");
}
function getQuestion(data) {
    console.log(data);
    var obj = JSON.parse(data);
    $("#question").html(obj.question);
    $("th#options1").html(obj.text1);
    $("th#options2").html(obj.text2);
    $("th#options3").html(obj.text3);
    $("th#options4").html(obj.text4);
}
function markAnswer(elem){
    $( "table#options th").css("background-color","cornflowerblue");
    elem.css("background-color","black");
    var sendVar = elem.attr("value");
    stompClient.send("/app/answer", {}, JSON.stringify({'optionSelected': sendVar, 'playerAlias': alias}));
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "table#options th" ).click(function() { markAnswer($(this)); });

});

