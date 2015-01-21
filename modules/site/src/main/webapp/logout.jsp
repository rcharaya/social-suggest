<%
    session.removeAttribute("user_id");
    response.sendRedirect("/site/?action=loggedout");
%>