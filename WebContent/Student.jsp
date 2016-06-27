<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*, models.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Student Page</title>


<%@ include file="includes/logoutscripts.html" %>
<%@ include file="includes/Chat.html" %>

<style>
body {
	background-color: #f2f2f2;
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

#startExam {
	text-align: center;
}

</style>

</head>

<jsp:directive.include file="includes/LogoutButton.jsp"/>
    
<jsp:directive.include file="includes/UserProfile.jsp"/>

<body>    
    
    <p> </p>
	<%
		Student student = (Student) request.getAttribute("student");
		Exam exam = (Exam) request.getAttribute("exam");
		request.getSession().setAttribute("exam", exam);
	%>
	<form action="Student" method="post">
		<div id="startExam">
			<p class="title">${exam.getType() } გამოცდა</p>
			<br>
			<p>სტუდენტი : ${student.getFirstName()} ${student.getLastName() }</p>
			<p>გამოცდა : ${exam.getName() }</p>
			<p>ხანგრძლივობა : ${exam.getDuration()} წუთი</p>
			<br /> <br /> <input class="start" type="submit"
				value="გამოცდის დაწყება">
		</div>
	</form>
</body>
</html>