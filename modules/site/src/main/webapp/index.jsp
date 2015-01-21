<%
    String action = request.getParameter("action");
    action = (action == null ? "" : action.trim().toLowerCase());

    if (action.length() > 0) {

%>
<a href="/site/">Login Again</a>
<%
} else {
%>
<%@include file="login.jsp" %>
<%
    if (user != null) {
%>
<%@include file="link_account.jsp" %>
<br />
<a href="logout.jsp">Logout</a>
<%
        }
    }
%>