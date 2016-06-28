<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.*, models.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	Exam exam = (Exam) request.getAttribute("exam");
	Student student = (Student) request.getAttribute("student");
	ArrayList<ExamMaterial> materials = (ArrayList<ExamMaterial>) request.getAttribute("materials");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=exam.getName()%></title>
</head>
<body>

	<%
		for (int i = 0; i < materials.size(); i++) {
	%>
	<a href="Download?fileLocation=<%=materials.get(i).getLocation()%>"><%=i+1%> : <%=materials.get(i).getMaterial()%></a>
	<br>
	<%
		}
	%>
</body>
</html>