<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html >
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
	margin: 12px 4px;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
}

div {
	text-align: center;
	width: 470px;
	border: 3px solid #ffaa00;
	border-radius: 5px;
	background-color: white;
	padding: 25px;
	margin: auto;
}

p {
	font-family: "Comic Sans MS", cursive, sans-serif;
	font-size: 18px;
	margin: 0px 0px;
}

p.error {
	font-size: 15px;
	color: red;
}
</style>
<body>
	<form action="Login" method="post">
		<div>
			<img src="freeuni.GIF" style="width: 180px; height: 130px;"> <br />
			<%
				String errorString = (String) request.getAttribute("errorString");
				if (errorString != null) {
					out.print("<p class = \"error\">" + errorString + "</p>");
				}
			%>
			<br />
			<p>Email:</p>
			<input type="text" name="username"> <br /> <br />
			<p>Password:</p>
			<input type="password" name="password"> <br /> <br /> <input
				type="submit" value="Log In"> <br /> <br />
		</div>
	</form>
</head>
</body>
</html>