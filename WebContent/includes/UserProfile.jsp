<%@page import="data_managers.AccountManager"%>
<%@ page import="java.util.*, models.*, helper.*"%>
<%
	AccountManager accountManager = AccountManager.getAccountManager(session);
	EAUser user = accountManager.getCurrentUser(session);
%>
<style>

div.profile {
  	border: 3px solid #ffaa00;
	border-radius: 12px;
}

</style>


<div class = "profile" >
	<img src="Display?image=<%=user.getImage()%>">
	
	<h2 class="font-semibold mgbt-xs-5"><%=user.getFirstName() + " " + user.getLastName()%></h2>
	<h4> <%=user.getRole() %> </h4>
	<h4> <%=user.getMail() %> </h4>
		
</div>