<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%> 
<%@ page import="helper.AccountManager"%>    
<%@ page import="helper.ContextStartupListener"%>    
<%@ page import="models.EAUser"%>    
<%@ page import="models.Exam"%>    
<%@ page import="java.util.ArrayList"%>    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Exam Board</title>
<style>
#header {
    background-color: #ffaa00;
    color:white;
    text-align:center;
    padding:5px;
}
#nav {
    line-height:30px;
    background-color:#eeeeee;
    height:700px;
    width:250px;
    float:left;
    padding:9px;
}
#section {
    width:70%;
    float:left;
    padding:1px;
}

p {
 font-family: "Arial", cursive, sans-serif;
 font-size: 17px;
 margin: 0px 0px;
}

input[type=submit] {
	width: 47%;
	background-color: #ffaa00;
	color: white;
	padding: 12px 20px;
	margin: 8px 0;
	border: none;
	border-radius: 4px;
	cursor: pointer;	
}

table {
    border-collapse: collapse;
    width: 100%;
    padding:20px;
}

th, td {
    text-align: left;
    padding: 1px;
}

tr:nth-child(even){background-color: #f2f2f2}

th {
    background-color: #4CAF50;
    color: white;
}

</style>
</head>
<body>

	<jsp:include page="includes/LogoutButton.jsp"></jsp:include>

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

<p> EMail: <%= user.getMail() %> </p>
<p> User: <%=user.getFirstName()%>  <%= user.getLastName() %> </p>
<form action="Board" method="post">

<br /> 
<input type="submit" name = "but" value="Exam List">
<br /> <br />
<input type="submit" name = "but" value="Create Exam">
</form>
</div>

<jsp:include page="includes/GenericExamsView.jsp"></jsp:include>

<!-- 
<div id="section">
<%
  ArrayList<Exam> exams = (ArrayList<Exam>) request.getAttribute("exams");
  if (exams != null) {
	  %>
	  <table>
			<tr>
				<th> ExamId</th>
    <th> Name</th>
    <th> Type</th>
    <th> StartTime </th>
    <th> Duration</th>
    <th> Resource</th>
    <th> Variants</th>
    <th> Status</th>
  </tr> <%
   for (int i = 0; i < exams.size(); i++) {
    	Exam ex = exams.get(i);
    	
   %>
   
			<tr>
				<td><%=ex.getExamID()%></td>
				<td><%=ex.getName()%></td>
				<td><%=ex.getType()%></td>
				<td><%=ex.getStartTime()%></td>
				<td><%=ex.getDuration()%></td>
				<td><%=ex.getResourceType()%></td>
				<td><%=ex.getNumVariants()%></td>
				<td><%=ex.getStatus()%></td>
			</tr>
			<%
				}
				}
			%>
		</table>
	</div>  -->
</body>

</html>