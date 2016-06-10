<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Start Exam</title>
<%@ include file="includes/logoutscripts.html" %>
</head>

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
</style>
<body>
 <jsp:directive.include file="includes/LogoutButton.jsp"/>
	<form action="StudentServlet" method="post">
		<div>
			<p class="title">Exam information</p>
			<br /> <br />
			<p>Student: Ana Rakviashvili</p>
			<p>Exam: OOP</p>
			<p>Duration: 2h</p>
			<br /> <br /> <input class="start" type="submit" value="Start Exam">
		</div>
	</form>
</body>

</html>