<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%
	String url = "/views/home/Main.jsf?uid=".concat(request.getParameter("uid"));
    ViewHelper.userId = request.getParameter("uid");
%>

<%@page import="com.afb.portal.presentation.tools.ViewHelper"%>

<html>
	<body>
		<jsp:forward page="<%=url %>"></jsp:forward>
	</body>
</html>