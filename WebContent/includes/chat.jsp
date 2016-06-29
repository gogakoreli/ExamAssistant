
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="models.Student"%>
<%@page import="models.Lecturer"%>
<%@page import="models.EAUser"%>
<%@page import="data_managers.AccountManager"%>
<%
	AccountManager accManager = AccountManager.getAccountManager(request.getSession());
	EAUser user = accManager.getCurrentUser(request.getSession());
%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Title of the document</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js">
	
</script>
<script type="text/javascript">
	function open_chatbox() {

		/* only student supports this */
		if(student)
			getChatDiv(0, 'შეკითხვა მასწავლებელთან.');
	
		$('#chatnow').hide();
		$('#chat').fadeOut(400);
		$('#chatBox').fadeIn(600);
	}

	function closeChat(id) {
		if (student) {
			$('#chatBox').fadeOut(400);
			$('#chat').fadeIn(500);
			$('#chatnow').show();
		} else {
			var chatdiv = getChatDiv(id, '');
			chatdiv.style.display = 'none';
			//chatdiv.parentNode.removeChild(chatdiv);
		}
	}
	
	function openChatBox(id){
		var chdiv = document.getElementById('chatBox');
		chdiv.style.display = 'inline';
		
		var chatdivid = 'chat' + id;
		var chatdiv = document.getElementById(chatdivid);
		if (chatdiv)
			chatdiv.style.display = 'inherit';
		//chdiv.setAttribute("display", "incline");
	}
	
	/* before we leave this page */
	window.onbeforeunload = function (e) {
		sessionStorage.clear();
		sessionStorage.chatObj = document.getElementById('beforechat').innerHTML;
		$('*[id*=messageswith]').each(function() {
			current=$(this);
			var currentId = current.attr('id');
			//console.log("saveing" + currentId);
			//console.log("saveing value " + current.val());
			sessionStorage.setItem(currentId, current.val());
			sessionStorage.currentId = current.val();
		});
	}
	
	function updatechatinterface(){
		if (sessionStorage.chatObj){
			document.getElementById('beforechat').innerHTML = sessionStorage.chatObj;
			$('*[id*=messageswith]').each(function() {
				current=$(this);
				//console.log(current);
				var currentId = $(current).attr('id');
				//console.log("getting old" + currentId);
				//console.log("old =" + sessionStorage.getItem(currentId));
				if (sessionStorage.getItem(currentId))
					current.val(sessionStorage.getItem(currentId));
			});
		}
	}
		
		
</script>


<script type="text/javascript">
	var student =<%if (user instanceof Lecturer)
				out.append('0');
			else
				out.append('1');%>;
	var webSocket = new WebSocket(
			"ws://localhost:8080/ExamAssistant/ChatroomServerEndpoint");

	webSocket.onmessage = function processMessage(message) {

		//console.log(message);
		var json = JSON.parse(message.data);

		var msgFromId = json.fromId;
		var msgFromName = json.name;
		var msgText = json.message;

		if (student)
			msgFromId = 0;
		else
			openChatBox(msgFromId);
		console
				.log("called!  " + msgFromId + " " + msgFromName + " "
						+ msgText);
		
		var chatdivid = getChatDiv(msgFromId, msgFromName);

		var msgtxtarea = document.getElementById('messageswith' + msgFromId);
		msgtxtarea.value += msgFromName + ": " + msgText + "\n";
		msgtxtarea.set
	}

	function getChatDiv(msgFromId, msgFromName) {
		var chatdivid = 'chat' + msgFromId;
		var chatdiv = document.getElementById(chatdivid);
		if (chatdiv === null) {
			addChatWith(msgFromId, msgFromName);
			chatdiv = document.getElementById(chatdivid);
		}

		return chatdiv;
	}

	//adds chat window 
	function addChatWith(msgFromId, msgFromName) {
		console.log("adding chat");
		var chatdivid = 'chat' + msgFromId;
		var div = document.createElement('div');
		div.setAttribute("id", chatdivid);

		div.className = "chat";
		div.innerHTML = '<div class="chat-title" id="divchattitle' + msgFromId + '"> <p>'
				+ msgFromName
				+ '</p> <div onclick="closeChat('
				+ msgFromId
				+ ')" class="chatclosebtn">X</div> </div> <div class="dd" id="divmsgflow' + msgFromId + '"> <textarea id="messageswith' + msgFromId + '" style="color: green; border: 1px solid #ffaa00;  resize: none; font-size: 16px;" ' +
  'readonly="readonly" rows="13" cols="45" ></textarea></div> <div class="chatfooter"> <textarea class="message-input" placeholder="Type message..." id="messagetext'
				+ msgFromId
				+ '" onkeypress="return textfieldKeyPressed(event,'
				+ msgFromId + ')" > </textarea> </div>';

		document.getElementById('chatsys').appendChild(div);
		//sendJsonToServer(msgFromId,'',true);
	}

	function sendMessage(sendTo) {

		console.log("sending message !  ");
		var messagetext = document.getElementById('messagetext' + sendTo);
		sendJsonToServer(sendTo, messagetext.value, false);
		var msgtxtarea = document.getElementById('messageswith' + sendTo);
		msgtxtarea.value += 'You: ' + messagetext.value + "\n";
		messagetext.value = "";
	}

	function sendJsonToServer(sendTo, message, isUpdate) {
		webSocket.send(JSON.stringify({
			fromId : sendTo,
			message : message,
			updateChatWindowText : false
		}));
	}

	function textfieldKeyPressed(e, id) {
		if (e.keyCode == 13) {
			sendMessage(id);
			return false;
		}
	}
</script>


<style>
.chat {
	width: 300px;
	height: 350px;
	overflow: hidden;
	background: rgba(0, 0, 0, .5);
	display: inline-block;
	margin-right: 15px;
	margin-bottom: 5px;
}

.dd {
	flex-direction: column;
	display: flex;
}

input[type=button] {
	width: 100%;
	background-color: #248A52;
	color: white;
	padding: 12px 20px;
	margin: 8px 0;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

body {
	background-color: #f2f2f2;
	font-family: 'Open Sans', sans-serif;
	font-size: 12px;
}

.message-input {
	width: 100%;
}

.chat-title {
	flex: 0 1 45px;
	position: relative;
	background: rgba(0, 0, 0, 0.2);
	color: #fff;
	text-transform: capitalize;
	text-align: left;
	padding-left: 20px;
	padding-top: 5px;
	padding-bottom: 5px;
	text-align: left;
	cursor: pointer;
}

.chatsysclass {
	bottom: 0px;
	position: fixed;
	right: 0px;
}

.chatfooter {
	
}

.chat-title p{
    padding: 0;
    font-family: "Arial", cursive, sans-serif;
    font-size: 17px;
    margin: 0px 0px;
}
</style>


<style>
#chatnow {
	bottom: 0px;
	position: fixed;
	right: 0px;
	width: 200px;
	height: auto;
	padding: 10px;
	background: #088A68;
	color: #EDEDED;
	text-align: center;
	font-family: Cambria;
	font-size: 20px;
	bottom: 0px;
	right: 15px;
	cursor: pointer;
}

#chatBox {
	bottom: 0px;
	position: fixed;
	right: 0px;
	display: none;
	width: 300px;
	height: 353px;
	padding: 10px;
	/* background: #EDEDED; */
	color: #FF7700;
	text-align: center;
	font-family: Cambria;
	font-size: 20px;
	bottom: 0px;
	right: 15px;
	cursor: pointer;
}

.chatclosebtn {
	position: absolute;
	width: 30px;
	height: 30px;
	text-align: center;
	/* background: rgba(0, 0, 0, 0.8); */
	color: #FF7700;
	font-family: Cambria;
	right: 0px;
	top: 0px;
}

.visiblechat{
	display: inline;
}

</style>
</head>

<body>

	<% if (user instanceof Student) out.print("<div id=\"chatnow\" onClick=\"open_chatbox();\">Chat Now</div>"); %>
	

	<div id="beforechat">
	<div id="chatBox" >

		<div id='chatsys' class='chatsysclass'>
		</div>
		<br> <br>

	</div>
	<script>
		updatechatinterface();
	</script>
	</div>
		<% //if (user instanceof Lecturer) out.print("<script>open_chatbox();</script>"); %>
</body>

</html>