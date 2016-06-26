<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="data_managers.AccountManager"%>
<%@ page import="listeners.ContextStartupListener"%>
<%@ page import="models.EAUser"%>
<%@ page import="models.Exam"%>
<%@ page import="java.util.ArrayList"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


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
<body>

<div id="section">

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