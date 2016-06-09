<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<head>
<title>Create Exam</title>
<script language="javascript">  

</script>
</head>
</head>
<body>
	<form action="${pageContext.request.contextPath}/ModifyExamServlet" method="post">
    Exam Name: <input type="text" name="examName" /><br>
	<input type="checkbox" name="examType"  value="openBook"  /> Open Book<br>
	Add Sub Lecturer: <input type="text" name="subLecMail" /><br>
	Add Materials: <input type="text" name="materials" /><br>
	Start Time: <input type="text" name="startTime" /><br>
	Exam Duration: <input type="text" name="examDuration" /><br>
	Date Created: <input type="text" name="dateCreated" /><br>
	
<input type="submit" value="Submit" />
</form>


</body>
</html>

