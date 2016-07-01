<%@page import="helper.SecurityChecker"%>
<%@page import="com.mysql.jdbc.Security"%>
<%@page import="helper.LogManager"%>
	<%  
	SecurityChecker sChecker = new SecurityChecker(request, null);
	if (!sChecker.CheckPermissions()){
		sChecker.redirectToValidPage(response);
	}
	//LogManager.logInfoMessage("requested uri" + request.getRequestURI());
	//LogManager.logInfoMessage("requested url" + request.getRequestURL());
	//LogManager.logInfoMessage("requested referer" + request.getHeader("Referer"));
	//response.sendRedirect("/Login"); 
	%>
	
	<script>
		function removeStorage(){
			sessionStorage.clear();
			console.log("cleared");
		}
		
		//yvavilovani kitri da gasagebi 
	</script>

	<a id="logout" href="Logout" class="btn btn-info btn-lg"> <span
		class="glyphicon glyphicon-log-out"> </span> Log out
	</a>
