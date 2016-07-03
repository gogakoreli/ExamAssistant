<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Log In</title>
	<script>
		function removeStorage(){
			sessionStorage.clear();
			console.log("cleared");
		}
		removeStorage();
	</script>
<style>

body { 

	background-color: #f2f2f2;
	text-align: center;
}

input[type=submit] {
	width: 65%;
	height: 45px;
 	background-color: #ffaa00; 
	color: white;
	padding: 12px 20px;
	margin: 20px 0;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-family: "Comic Sans MS", cursive, sans-serif;
	font-size: 18px;
}

input:focus {
	box-shadow: inset 0 -5px 45px rgba(100, 100, 100, 0.4), 0 1px 1px
		rgba(255, 255, 255, 0.2);
}

input {
	width: 60%;
	margin-bottom: 10px;
	background: rgba(0, 0, 0, 0.04);
	outline: none;
	padding: 10px;
	font-size: 14px;
 	color: #fff; 
 	text-shadow: red;
	border: 1px solid rgba(0, 0, 0, 0.3);
	border-radius: 4px;
	box-shadow: inset 0 -5px 45px rgba(100, 100, 100, 0.2), 0 1px 1px
		rgba(255, 255, 255, 0.2);
 	-webkit-transition: box-shadow .5s ease; 
 	-moz-transition: box-shadow .5s ease; 
	-o-transition: box-shadow .5s ease; 
 	-ms-transition: box-shadow .5s ease; 
 	transition: box-shadow .5s ease; 
}


div {
	text-align: center;
	width: 420px;
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
			<img src="freeuni.GIF" style="width: 200px; height: 157px;"> <br />
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