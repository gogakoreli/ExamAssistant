<%@page import="data_managers.AccountManager"%>
<%@ page import="java.util.*, models.*, helper.*"%>
<%
	AccountManager accountManager = AccountManager.getAccountManager(session);
	EAUser user = accountManager.getCurrentUser(session);
%>


<div class="panel-body">
	<div class="text-center vd_info-parent">
		<image src="Display?image=<%=user.getImage()%>">
	</div>

	<h2 class="font-semibold mgbt-xs-5"><%=user.getFirstName() + " " + user.getLastName()%></h2>
	<h4>Student/Lecturer/ExamBoard</h4>
	<p>MACS/ESM/NONE</p>
</div>