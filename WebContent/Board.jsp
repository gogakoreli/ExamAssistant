<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%> 
<%@ page import="data_managers.AccountManager"%>    
<%@ page import="listeners.ContextStartupListener"%>    
<%@ page import="models.EAUser"%>    
<%@ page import="models.Exam"%>    
<%@ page import="java.util.ArrayList"%>    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Exam Board</title>

<%@ include file="includes/logoutscripts.html"%>

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
</head>
<body>

	<jsp:directive.include file="includes/LogoutButton.jsp" />

	<div id="header">

		<h1>Exam Board</h1>

	</div>

	<div id="nav">
		<br />
		<%
			ServletContext ctx = request.getServletContext();
			AccountManager manager = (AccountManager) ctx.getAttribute(ContextStartupListener.ACCOUNT_MANEGER_ATTRIBUTE_NAME);
			EAUser user = manager.getCurrentUser(request.getSession());
		%>

		<p>
			EMail:
			<%=user.getMail()%>
		</p>
		<p>
			User:
			<%=user.getFirstName()%>
			<%=user.getLastName()%>
		</p>

		<form action="Board" method="post">
			<br /> <input type="submit" name="but" value="Exam List">

		</form>
	</div>
	<jsp:include page="includes/GenericExamsView.jsp"></jsp:include>
</body>
</html>