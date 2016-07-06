<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*, models.*"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Student Page</title>


<%@ include file="includes/logoutscripts.html" %>
<jsp:include page="includes/chat.jsp" />

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
	width: 250px;
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

</style>

<style>
body {
	background-color: #f2f2f2;
}


#sudentmaindiv div {
	heigth: 300px;
	width: 500px;
	text-align: center;
	border: 3px solid #ffaa00;
	border-radius: 5px;
	background-color: white;
	padding: 25px;
	margin: auto;
	margin-top: 20px;
}

#sudentmaindiv  input.start {
	background-color: #4CAF50;
	border: none;
	color: white;
	padding: 18px 40px;
	text-decoration: none;
	margin: 8px 2px;
	cursor: pointer;
}

#sudentmaindiv  p {
	font-family: "Arial";
	font-size: 20px;
	text-align: left;
	margin: 13px 90px;
	font-weight: light;
}

#sudentmaindiv  p.title {
	font-size: 27px;
	text-align: center;
	margin: 14px 90px;
	font-weight: 600;
}

#sudentmaindiv  #startExam {
	text-align: center;
}

</style>

</head>

<jsp:directive.include file="includes/LogoutButton.jsp"/>
    

<body>    
    <div id="header">
		<h1>Student</h1>

	</div>
	
	<div id="nav">
		<br />
		
			<%
				Student student = (Student) request.getAttribute("student");
				Exam exam = (Exam) request.getAttribute("exam");
				request.getSession().setAttribute("exam", exam);
			%>

		<img src="Display?image=<%=student.getImage()%>">

		<h2 class="font-semibold mgbt-xs-5"><%=student.getFirstName() + " " + student.getLastName()%></h2>
		<h4>
			<%=student.getRole()%>
		</h4>
		<h4>
			EMail:
			<%=student.getMail()%>
		</h4>

	</div>
		
	<div id="sudentmaindiv">    
		<form action="Student" method="post">
		<div id="startExam" class="divclass">
			<p class="title">${exam.getType() } გამოცდა</p>
			<br>
			<p>სტუდენტი : ${student.getFirstName()} ${student.getLastName() }</p>
			<p>გამოცდა : ${exam.getName() }</p>
			<p>ხანგრძლივობა : ${exam.getDuration()} წუთი</p>
			<br /> <br /> <input class="start" type="submit"
				value="გამოცდის დაწყება">
		</div>
		
	</form>
			</div>
	
	
</body>   

</body>
</html>