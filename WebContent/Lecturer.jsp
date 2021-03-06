<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*, models.*"%>
<!DOCTYPE html>
<html>
<head>

<%@ include file="includes/logoutscripts.html"%>


<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lecturer Page</title>
<style>
#header {
	background-color: #ffaa00;
	color: white;
	text-align: center;
	padding: 25px;
	font-size: 30px;
}

#nav {
	line-height: 30px;
	background-color: #eeeeee;
	height: 700px;
	width: 260px;
	float: left;
	padding: 9px;
}

#section {
	width: 70%;
	float: left;
	padding: 1px;
}

p {
	font-family: "Arial", cursive, sans-serif;
	font-size: 17px;
	margin: 0px 0px;
}

.btnclass {
	width: 47%;
	background-color: #4CAF50;
	color: white;
	padding: 12px 20px;
	margin: 8px 0;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

#startExam {
	text-align: center;
	margin-left: 20ox;
}


</style>
</head>

<body>
	<jsp:directive.include file="includes/LogoutButton.jsp" />
	<jsp:include page="includes/chat.jsp" />
	
	<div id="header">
		<h1>Lecturer</h1>
	</div>

	<%
		Lecturer lecturer = (Lecturer) request.getAttribute("lecturer");
	%>
	<div id="nav">
		<form action="Lecturer" method="post">

			<br>
			<p>EMail: ${lecturer.getMail() }</p>
			<p>ლექტორი: ${lecturer.getFirstName()} ${lecturer.getLastName() }</p>
			<!-- aq unda iyos Exams.jsp-s gamozaxeba-->

		</form>
		<form action="ModifyExam" method="get">
			<br /> <input  name="newExam" class="btnclass" type="submit" value="New Exam" >
		</form>
		
	</div>

	<jsp:include page="includes/GenericExamsView.jsp"></jsp:include>
	
</body>
</html>