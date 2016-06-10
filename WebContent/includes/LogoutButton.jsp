<%@page import="helper.LogManager"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
	
	<%  
	
	LogManager.logInfoMessage("Trying to redirect !!");
	response.sendRedirect("/Login"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style>
#logout {
	margin-top: 5px;
	margin-right: 5px;
	position: absolute;
	top: 0;
	right: 0;
}
</style>
<meta name="viewport" content="width=device-width, initial-scale=2">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</head>
<body>
	<a id="logout" href="Logout" class="btn btn-info btn-lg"> <span
		class="glyphicon glyphicon-log-out"> </span> Log out
	</a>
</body>
</html>