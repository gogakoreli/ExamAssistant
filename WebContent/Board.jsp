<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>  
<%@ page import="helper.AccountManager"%>    
<%@ page import="helper.ContextStartupListener"%>    
<%@ page import="models.EAUser"%>    


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Exam Board</title>
<style>
#header {
    background-color:black;
    color:white;
    text-align:center;
    padding:5px;
}
#nav {
    line-height:30px;
    background-color:#eeeeee;
    height:300px;
    width:280px;
    float:left;
    padding:9px;
}
#section {
    width:350px;
    float:left;
    padding:10px;
}

</style>
</head>
<body>

<div id="header">
<h1>Exam Board</h1>
</div>

<div id="nav">
<%
	ServletContext ctx = request.getServletContext();
	AccountManager manager = (AccountManager) ctx.getAttribute(ContextStartupListener.ACCOUNT_MANEGER_ATTRIBUTE_NAME);
	EAUser user = manager.getCurrentUser(request.getSession());
	out.println("<p> EMail: " + user.getMail()+ "</p>");
	out.println("<p> User: " + user.getFirstName()+ " " + user.getLastName()+ "</p>");
%>

<input type="submit" value="Exam List">
<br /> <br />
<input type="submit" value="Create Exam">

</div>

<div id="section">

</div>
</body>
</html>