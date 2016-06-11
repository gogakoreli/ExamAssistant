<%@page import="servlets.ModifyExamServlet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="includes/logoutscripts.html" %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<head>
<title>Create Exam</title>

<style>

body {
	background-color: #f2f2f2;
	margin: 20px;
	font-family: "Arial", cursive, sans-serif;
	font-size: 18px;
	text-align: center;
}

div {
	text-align: left;
	heigth: 280px;
	width: 470px;
	border: 3px solid #ffaa00;
	border-radius: 5px;
	background-color: white;
	padding: 25px;
	margin: auto;
}


input[type=submit] {
	width: 40%;
	background-color: #4CAF50;
	color: white;
	padding: 12px 20px;
	margin: 4px 0;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

input[type="text"] {
	width: 100%;
}

</style>

<script language="javascript">  

</script>
</head>
</head>
<body>
	<%
	if(ModifyExamServlet.checkNewExam(request)){
		request.getSession().setAttribute("newExam", "Create New Exam");
	}
	%>
	<div>
	<form action="ModifyExam" method="post">
		Exam Name: <input type="text" name="examName" /> <br /> <br />
		 <input type="checkbox" name="examStatus" value="openBook" /> Open Book
		<br /> <br /> 
		<input type="radio" name="examType" value="Final"
			checked="checked" /> Final<br> <input type="radio" name="examType"
			value="Midterm" /> Midterm <br> <input type="radio" name="examType"
			value="Quizz" /> Quizz <br /> <br /> 
		Add Sub Lecturer:<input type="text" name="subLecMail" /> <br /> <br /> 
		Start Time: <input type="text" name="startTime" /> <br /> <br />
		Exam Duration: <input type="text" name="examDuration" /> <br /> <br /> 
		Date Created: <input type="text" name="dateCreated" />
		
		 <br />  <br />
		<input type="submit" name="saveExam" value="Save Exam" />
</form>
</div>
<!--This is a comment. Comments are not displayed in the browser
	<form action="ModifyExam" method="post" enctype="multipart/form-data">
    <input type="file" name="file" multiple="true" />
    <input type="submit" name="saveFile" value="Save File"/> <br>
    </form>
-->

</body>
</html>

