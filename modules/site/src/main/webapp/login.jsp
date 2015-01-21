<%
    Long userId = (Long)session.getAttribute("user_id");
    userId = (userId == null ? -1 : userId);

    User user = null;

    if (userId == -1) {
%>
<%@include file="openid.jsp"%>
<%
    } else {
        user = ((SitePersistence)getServletConfig().getServletContext().getAttribute("persistence")).getUser(userId);
    }
%>