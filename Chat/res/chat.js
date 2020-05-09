var endPointURL = "ws://" + window.location.host;
var name = null;
var id = null;
var chatClient = null;
var chatTime = null;
var textMuted = null;
var span = null;
var lineTag = null;
var breakTag = null;
var messageBubble = null;

function connect(){
//    if(window.location.pathname.includes("form")){
//        window.location.pathname = window.location.pathname.replace("form","chat");
//    }
    name = localStorage.name;
    id = localStorage.id;
    document.getElementById("heading").innerHTML = id.toUpperCase();
    chatClient = new WebSocket(endPointURL);
    var t = "@&@ID"+id;
    setTimeout(
     function() {
     chatClient.send(t);
     if(localStorage.fileName){
        var fileName = localStorage.fileName;
        localStorage.removeItem("fileName");
        var message = "@&@"+fileName;
        var date = new Date();
        var options = {
            weekday: "long", year: "numeric", month: "short",
            day: "numeric", hour: "2-digit", minute: "2-digit"
        };
        var dateString = date.toLocaleTimeString("en-us", options);
        var jsonObj = {"name" : name, "id" : id, "message" : message, "date" : dateString};
        chatClient.send(JSON.stringify(jsonObj));
        alert("File Sent");
     }
     }, 1000);
    chatClient.onmessage = function (event) {
        var jsonObj = JSON.parse(event.data);
        var name = jsonObj.name;
        var message = jsonObj.message;
        var date = jsonObj.date;
        messageBubble = document.createElement('div');
        messageBubble.className = "row message-bubble";
        chatTime = document.createElement('span');
        chatTime.className = "chat-time";
        chatTime.innerHTML = date;
        textMuted = document.createElement('span');
        textMuted.className = "text-muted";
        textMuted.innerHTML = name;
        span = document.createElement('span');
        if(message.startsWith("@&@")){
            var a = document.createElement('a');
            var link = message.substring(3);
            a.setAttribute('href',"uploads/"+link);
            a.setAttribute('download',link);
            a.innerHTML = link;
            span.appendChild(a);
        }else{
            span.innerHTML = message;
        }
        breakTag = document.createElement('br');
        lineTag = document.createElement('hr');
        messageBubble.appendChild(chatTime);
        messageBubble.appendChild(textMuted);
        messageBubble.appendChild(breakTag);
        messageBubble.appendChild(breakTag);
        messageBubble.appendChild(span);
        document.getElementById('history').appendChild(messageBubble);
        document.getElementById('history').appendChild(lineTag);
        document.getElementById('history').scrollTop = document.getElementById('history').scrollHeight;
    };
}

function disconnect(){
    chatClient.close();
}

function sendMessage(){
    var inputElement = document.getElementById("message");
    var message = inputElement.value.trim();
    if (message !== "") {
        inputElement.value = "";
        messageBubble = document.createElement('div');
        messageBubble.className = "row message-bubble";
        chatTime = document.createElement('span');
        chatTime.className = "chat-time";
        var date = new Date();
        var options = {
            weekday: "long", year: "numeric", month: "short",
            day: "numeric", hour: "2-digit", minute: "2-digit"
        };
        var dateString = date.toLocaleTimeString("en-us", options);
        chatTime.innerHTML = dateString;
        textMuted = document.createElement('span');
        textMuted.className = "text-muted";
        textMuted.innerHTML = name;
        span = document.createElement('span');
        span.innerHTML = message;
        breakTag = document.createElement('br');
        lineTag = document.createElement('hr');
        messageBubble.appendChild(chatTime);
        messageBubble.appendChild(textMuted);
        messageBubble.appendChild(breakTag);
        messageBubble.appendChild(breakTag);
        messageBubble.appendChild(span);
        document.getElementById('history').appendChild(messageBubble);
        document.getElementById('history').appendChild(lineTag);
        document.getElementById('history').scrollTop = document.getElementById('history').scrollHeight;
        var jsonObj = {"name" : name, "id" : id, "message" : message, "date" : dateString};
        chatClient.send(JSON.stringify(jsonObj));
    }
    inputElement.focus();
}

function getFileData(myFile){
        var file = myFile.files[0];
        var filename = file.name;
        localStorage.fileName = filename;
        chatClient.close();
        myFile.form.submit();
}