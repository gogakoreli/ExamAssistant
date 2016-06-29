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

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<script>
$(document).ready(function(){
    $("#but").click(function(){
        $("#div1").fadeToggle();
    });
});
</script>

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
	

<style>

input[type=text], select {
    width: 80%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
}

input[type=submit] {
    width: 25%;
    background-color: green;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

#but {
    width: 100%;
    background-color: #4CAF50; 
    color: white;
	padding: 10px 0px;
	margin: 0px 0;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	position: left;
}

input[type=submit]:hover {
    background-color: #45a049;
}

#div1 {
	position: bottom;
    display:none;
    border-radius: 5px;
    background-color: #f2f2f2; 
    border: 3px solid green;
    padding: 20px;
    margin:auto; 
    margin-top:170px;
    width: 550px;
}
</style>



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
					<th>Notification</th>
					
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
					<td>  <button id = "but"> Notification </button>
					</td>
					
				</tr>
				<%
					}
					}
				%>
			</tbody>


		</table>
		
		<script type='text/javascript' src="includes/js/list.min.js">
		
		</script>
	
</div>

<div id = "div1">

		<label for="lname"> Notification </label> <br /> <input type="text"
			id="lname" name="lastname"> <br /> 
			<label for="country"> Variant </label> <br /> 
			<select id="country" name="country">
			<option>I</option>
			<option>II</option>
			<option>Both</option>

		</select> <input type="submit" value="Submit">
    
</div>



</body>
</html>