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
	<form action="ModifyExam" method="post">
    Exam Name: <input type="text" name="examName" /><br>
	<input type="checkbox" name="examStatus"  value="openBook"  /> Open Book<br>
	<input type="radio" name="examType"  value="Final" checked="checked" /> Final<br>
	<input type="radio" name="examType"  value="Midterm" /> Midterm <br>
	<input type="radio" name="examType"  value="Quizz" /> Quizz <br>
	Add Sub Lecturer: <input type="text" name="subLecMail" /><br>
	Start Time: <input type="text" name="startTime" /><br>
	Exam Duration: <input type="text" name="examDuration" /><br>
	Date Created: <input type="text" name="dateCreated" /><br>
	
	
<input type="submit" name="saveExam" value="Save Exam" />
</form>
<!--This is a comment. Comments are not displayed in the browser
	<form action="ModifyExam" method="post" enctype="multipart/form-data">
    <input type="file" name="file" multiple="true" />
    <input type="submit" name="saveFile" value="Save File"/> <br>
    </form>
-->

</body>
</html>

