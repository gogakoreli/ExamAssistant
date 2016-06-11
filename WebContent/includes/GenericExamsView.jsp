<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="helper.AccountManager"%>
<%@ page import="helper.ContextStartupListener"%>
<%@ page import="models.EAUser"%>
<%@ page import="models.Exam"%>
<%@ page import="java.util.ArrayList"%>




<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

 <!--  <style>
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

 -->


<!-- new designed boxes   -->
<script type="text/javascript"
	src="http://code.jquery.com/jquery-git.js"></script>
<script type="text/javascript"
	src="http://mottie.github.com/tablesorter/js/jquery.tablesorter.js"></script>
<script type="text/javascript"
	src="http://mottie.github.com/tablesorter/js/jquery.tablesorter.widgets.js"></script>
<link rel="stylesheet" type="text/css"
	href="http://fiddle.jshell.net/css/normalize.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.blue.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.dark.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.default.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.dropbox.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.green.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.grey.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.ice.css">
<link rel="stylesheet" type="text/css"
	href="http://mottie.github.com/tablesorter/css/theme.black-ice.css">



<style type="text/css">
/* This class is added to the span wrapping the
   header text using the "onRenderHeader" option */
.roundedCorners {
	border: #777 1px solid;
	padding: 0 10px;
	border-radius: 1em;
	-moz-border-radius: 1em;
	-webkit-border-radius: 1em;
}
</style>





<!-- end of new desgined boxes  -->




</head>
<div id="section">
	<body>

		<%
			ArrayList<Exam> exams = (ArrayList<Exam>) request.getAttribute("exams");
			if (exams != null) {
		%>
		<table class="tablesorter">
			<tr>
			<thead>
				<tr>
					<th>ExamId</th>
					<th>Name</th>
					<th>Type</th>
					<th>StartTime</th>
					<th>Duration</th>
					<th>Resource</th>
					<th>Variants</th>
					<th>Status</th>
				</tr>
			</thead>
			<tbody>

				<%
					for (int i = 0; i < exams.size(); i++) {
							Exam ex = exams.get(i);
				%>

				<tr>
					<td><%=ex.getExamID()%></td>
					<td><a href='ModifyExam?examID=<%=ex.getExamID()
					
					
					%>'> <%=ex.getName()%>
					</a></td>
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
			</tbody>


		</table>


		<script type='text/javascript' src="includes/js/list.min.js">
			//<![CDATA[

		</script>
</div>

</body>
</html>