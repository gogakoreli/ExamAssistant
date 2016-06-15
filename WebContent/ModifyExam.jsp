<%@page import="servlets.ModifyExamServlet"%>
<%@ page import="models.Exam"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="includes/logoutscripts.html" %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<head>
<title>Create Exam</title>

<style>
body {
	background-color: #f2f2f2;
	margin: 20px;
	font-family: "Arial", cursive, sans-serif;
	font-size: 18px;
	text-align: center;
}

div {
	text-align: left;
	heigth: 280px;
	width: 470px;
	border: 3px solid #ffaa00;
	border-radius: 5px;
	background-color: white;
	padding: 25px;
	margin: auto;
}

input[type=submit], input[type=button] {
	width: 48%;
	background-color: #4CAF50;
	color: white;
	padding: 12px 20px;
	margin: 4px 0;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}


.disabledfield {
	cursor: default;
	border: none;
	background: white;
}

.visibletextbox {
	background: #FCF5D8;
	border: 1px;
}


</style>
<script>
    
	function hasClass(elem, klass) {
		return (" " + elem.className + " ").indexOf(" " + klass + " ") > -1;
	}
	function EnibleEditing() {

		var x = document.getElementsByClassName("disabledfield");
		var arrayLength = x.length;
		for (var i = 0; i < arrayLength; i++) {

			if (hasClass(x[i], 'lecturer')) {
				//alert("I am an alert box!");
				x[i].removeAttribute("disabled");
				if (!hasClass(x[i], 'visibletextbox')) {
					x[i].className += " visibletextbox";
				}
			}
		}
	}
</script>


</head>
</head>
<body>
	<%
	if(ModifyExamServlet.checkNewExam(request)){
		request.getSession().setAttribute("newExam", "Create New Exam");
	}
	 Exam exam = (Exam) request.getAttribute("exam");

	%>
	<div>
	<h1> გამოცდა </h1>
	<br />
	<form action="ModifyExam" method="post">
		დასახელება:  <input type="text" id="fname" name="examName" class="disabledfield lecturer"
		 value = ${exam.getName() } disabled>
		 <br /> <br /> 
		 <input type="checkbox" name="examStatus"  value="openBook" 
		  class="disabledfield lecturer" checked="checked" disabled/> ღია წიგნი
		 <input type="checkbox" name="examStatus"  value="openBook" 
		  class="disabledfield lecturer" checked="checked" disabled/> ღია რვეული
	
		<br /> <br /> 
		<input type="radio" name="examType"  value="Final"  
		class="disabledfield lecturer" checked="checked" disabled/> ფინალური <br> 
		<input type="radio" name="examType"  value="Midterm" 
		class="disabledfield lecturer"  disabled/> შუალედური   <br>
		<input type="radio" name="examType"  value="Quizz"  class="disabledfield lecturer" disabled/> ქვიზი 
		  <br /> <br /> 
			
		ქველექტორი:<input type="text" name="subLecMail"  /> <br /> <br /> 
		
		დაწყების დრო: <input type="text" id="fname" name="startTime" class="disabledfield lecturer"
		 value = ${exam.getStartTime() } disabled> <br /> <br />
		ხანგრძლივობა: 
		<input type="text" id="fname" name="examDuration" class="disabledfield lecturer"
		 value = ${exam.getDuration() } disabled>  <br /> <br /> 
		ვარიანტი: <input type="text" id="fname" name="dateCreated" class="disabledfield lecturer"
		 value = ${exam.getNumVariants() } disabled> 
		
		 <br />  <br />
		 
		<input class="start" onclick="EnibleEditing()" type="button"
               value="გამოცდის განახლება">
		<input type="submit" name="saveExam" value="გამოცდის შენახვა" />
</form>
</div>

</body>
</html>
