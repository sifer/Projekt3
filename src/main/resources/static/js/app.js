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
            console.log("omg this worked");
            showAnswer(JSON.parse(greeting.body).content);
        });
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

