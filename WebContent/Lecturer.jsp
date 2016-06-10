<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*, models.*, servlets.ModifyExamServlet"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lecturer Page</title>
<style>
body {
	background-color: #f2f2f2;
	text-align: center;
}

div {
	heigth: 300px;
	width: 500px;
	text-align: center;
	border: 3px solid #ffaa00;
	border-radius: 5px;
	background-color: white;
	padding: 25px;
	margin: auto;
}

input.start {
	background-color: #4CAF50;
	border: none;
	color: white;
	padding: 18px 40px;
	text-decoration: none;
	margin: 8px 2px;
	cursor: pointer;
}

p {
	font-family: "Arial";
	font-size: 20px;
	text-align: left;
	margin: 13px 90px;
	font-weight: light;
}

p.title {
	font-size: 27px;
	text-align: center;
	margin: 14px 90px;
	font-weight: 600;
}

#startExam{
	text-align: center;
	margin-left: 20ox;
}
</style>
</head>

<body>
	<jsp:include page="includes/LogoutButton.jsp"></jsp:include>
	
	<h1>This is lecturer page</h1>
	<%
		Lecturer lecturer = (Lecturer) request.getAttribute("lecturer");
	%>
	<form action="Lecturer" method="post">
		<div id="startExam">
			<br>
			<p>ლექტორი : ${lecturer.getFirstName()} ${lecturer.getLastName() }</p>
			<!-- aq unda iyos Exams.jsp-s gamozaxeba-->
			<br /> <br />
		</div>
	</form>
		<form action="ModifyExam" method="get">
		<br /> 
    	<input type="submit" name="newExam" value="Create New Exam" />
	</form>
	
	<jsp:include page="includes/GenericExamsView.jsp"></jsp:include>
</body>
</html>