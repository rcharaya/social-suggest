<%@include file="login.jsp"%>
<%
    String account = request.getParameter("account");
    if ("facebook".equalsIgnoreCase(account)) {
        user.setFacebookAccessToken(null);
        user = ((SitePersistence)getServletConfig().getServletContext().getAttribute("persistence")).mergeUser(user);
    } else if ("twitter".equalsIgnoreCase(account)) {
        user.setTwitterAccessToken(null);
        user.setTwitterAccessTokenSecret(null);
        user = ((SitePersistence)getServletConfig().getServletContext().getAttribute("persistence")).mergeUser(user);
    } else if ("foursquare".equalsIgnoreCase(account)) {
        user.setFoursquareAccessToken(null);
        user = ((SitePersistence)getServletConfig().getServletContext().getAttribute("persistence")).mergeUser(user);
    }
    response.sendRedirect("/site/");
%>