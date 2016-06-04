<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*, servlets.LoginServlet"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Log In</title>
<style>
body {
	background-color: #f2f2f2;
	text-align: center;
}

input[type=submit] {
	width: 45%;
	background-color: #ffaa00;
	color: white;
	padding: 12px 20px;
	margin: 8px 0;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

input {
	width: 45%;
	padding: 12px 20xp;
	margin: 8px 4px;
	display: inline-block;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
}

div {
	heigth: 280px;
	width: 470px;
	text-align: center;
	border: 3px solid #ffaa00;
	border-radius: 5px;
	background-color: white;
	padding: 25px;
}

p {
	font-family: "Comic Sans MS", cursive, sans-serif;
	font-size: 18px;
	margin: 0px 0px;
}
</style>
<body>
	<form action="LoginServlet" method="post">
		<div>
			<img src="freeuni.GIF" style="width: 180px; height: 130px;"> <br />
			<br />
			<p>Email:</p>
			<input type="text" name="username"> <br /> <br />
			<p>Password:</p>
			<input type="text" name="password"> <br /> <br /> <input
				type="submit" value="Log In"> <br /> <br />
		</div>
	</form>
</head>
</body>
</html>