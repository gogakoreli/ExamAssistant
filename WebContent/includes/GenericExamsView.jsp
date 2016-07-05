<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="data_managers.AccountManager"%>
<%@ page import="listeners.ContextStartupListener"%>
<%@ page import="models.EAUser"%>
<%@ page import="models.Exam"%>
<%@ page import="models.Lecturer"%>
<%@ page import="java.util.ArrayList"%>

<!DOCTYPE html >
<html>
<head>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<script>
	$(document).ready(function() {
		$("button").click(function() {
			$("#div1").fadeToggle();
		});
	});
</script>


<script type="text/javascript">
	var webSocket = new WebSocket(
			"ws://localhost:8080/ExamAssistant/NotificationServerEndpoint");
 
	webSocket.onmessage = function processMessage(message) {
		console.log("Received message: "+message);
		var gson = message.data;
		messageTextArea.value += gson + "\n";
	}

	
	function sendMessage() {
		console.log("Sending message: "+messageText.value);
		var messagetext = document.getElementById('messagetext');
		//sendJsonToServer(messagetext, 2, null);
		webSocket.send(messageText.value);
		messageText.value = "";
	}

	
// 	function sendJsonToServer(message, examId, variants) {
// 		webSocket.send(JSON.stringify({
// 			message : message,
// 			examId: examId,
// 			variants: variants
// 		}));
// 	}

</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<!-- new designed boxes   -->
<script type="text/javascript"
	src="http://code.jquery.com/jquery-git.js"></script>
<script type="text/javascript"
	src="http://mottie.github.com/tablesorter/js/jquery.tablesorter.js"></script>
<script type="text/javascript"
	src="http://mottie.github.com/tablesorter/js/jquery.tablesorter.widgets.js"></script>
<link rel="stylesheet" type="text/css"
	href="http://fiddle.jshell.net/css/normalize.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.blue.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.dark.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.default.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.dropbox.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.green.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.grey.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.ice.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.black-ice.css">
	

<style>


input[type=text], select {
    width: 80%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
}

input[type=submit] {
    width: 47%;
    background-color: green;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

#but {
    width: 100%;
    background-color: #4CAF50; 
    color: white;
	padding: 10px 0px;
	margin: 0px 0;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	position: left;
}

#but:hover {
    background-color: #45a049;
}

#but1 {
    width: 25px;
    font-family: tahoma; 
    
}

input[type=submit]:hover {
    background-color: #45a049;
}

#div1 {
	position: relative;
 	z-index:1200;
    display:none;
    border-radius: 5px;
    background-color: #f2f2f2; 
    border: 3px solid green;
    padding: 20px;
    margin:auto; 
    margin-top:160px;
    width: 550px;
}
</style>



<style type="text/css">
/* This class is added to the span wrapping the
   header text using the "onRenderHeader" option */
.roundedCorners {
	border: #777 1px solid;
	padding: 0 10px;
	border-radius: 1em;
	-moz-border-radius: 1em;
	-webkit-border-radius: 1em;
}
</style>



</head>
<body>

	<%
		ArrayList<Exam> exams = (ArrayList<Exam>) request.getAttribute("exams");
		EAUser user = (EAUser) request.getAttribute("lecturer");

		if (exams != null) {
	%>

	<div id="section" >
		
		<table class="tablesorter">
			<tr>
			<thead>
				<tr>
					<th>ExamId</th>
					<th>Name</th>
					<th>Type</th>
					<th>StartTime</th>
					<th>Duration</th>
					<th>Resource</th>
					<th>Variants</th>
					<th>Status</th>
					<%
						if (user instanceof Lecturer) {
					%>
					<th>Notification</th>
					<%
						}
					%>
				</tr>
			</thead>
			<tbody>

				<%
					for (int i = 0; i < exams.size(); i++) {
							Exam ex = exams.get(i);
				%>

				<tr>
					<td><%=ex.getExamID()%></td>
					<td><a href='ModifyExam?examID=<%=ex.getExamID()
					
					
					%>'> <%=ex.getName()%>
					</a></td>
					<td><%=ex.getType()%></td>
					<td><%=ex.getStartTime()%></td>
					<td><%=ex.getDuration()%></td>
					<td><%=ex.getResourceType()%></td>
					<td><%=ex.getNumVariants()%></td>
					<td><%=ex.getStatus()%></td>
					<%
						if (user instanceof Lecturer) {
							String name = "but"+i;
							request.setAttribute(name, i);
					%>
					<td>
						<button id="but">Notification</button>
					</td>
					<%
						}
					%>

				</tr>
				<%
					}
					}
				%>
			</tbody>


		</table>
		
		<script type='text/javascript' src="includes/js/list.min.js">
		
		</script>
	  </div>


	<div id="div1">

		<div style="text-align: right;">
			<button id="but2"> X </button>
			<br />
		</div>


		<label  > Notification </label> <br /> 
		<input type="text"  id="messageText"> <br />   <br /> 
			<label> Variant </label>
		<br />

		<%
			//int ex = (int) request.getAttribute("but0");
			String name = (String) request.getParameter("name");
		%>


		<input type="checkbox" name="option3" value="All"> All <br> <input
			type="text"  name="lastname" value="I"> <br /> 
			
	
	     <input type="submit" value="Send" onclick="sendMessage();" />
			

	</div>
</body>
</html>