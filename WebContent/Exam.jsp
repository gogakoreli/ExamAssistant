<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.*, models.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	Exam exam = (Exam) request.getAttribute("exam");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=exam.getName()%></title>
</head>
<body>
	<!-- <a href="Download?fileName=" +<%=exam.getExamID()%>> </a>  -->
</body>
</html>