
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;" charset="UTF-8">

<script type="text/javascript">
	var webSocket = new WebSocket(
			"ws://localhost:8080/ExamAssistant/ChatroomServerEndpoint");
	
	webSocket.onmessage = function processMessage(message) {
		var gson = message.data;
		messageTextArea.value += gson + "\n";
	}

	function sendMessage() {
		webSocket.send(messageText.value);
		messageText.value = "";
	}
</script>

<style>
.chat {
	width: 80%;
	height: 300px;
	overflow: hidden;
	background: rgba(0, 0, 0, .5);
	border-radius: 20px;
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
	text-transform: uppercase;
	text-align: left;
	padding: 10px 10px 10px 50px;
}



</style>


</head>

<body>
	<div class="chat">
		<div class="chat-title">
			<p>leqtoris saxeli </p>
		</div>
		<div class = "dd"> 
		<textarea style="color:green; border: 1px solid #ffaa00;" 
		id="messageTextArea" readonly="readonly" rows="10" cols="45"></textarea>
		</div>
		<textarea class="message-input" id="messageText"
			placeholder="Type message..."> </textarea>
		<input type="button"  value="Send"  onclick="sendMessage();" />
		</div>
		
</body>

</html>

