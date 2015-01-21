<%@ page import="com.yahoo.hack.infra.config.Credentials" %>
<%@ page import="twitter4j.Twitter" %>
<%@ page import="twitter4j.TwitterFactory" %>
<%@ page import="twitter4j.auth.AccessToken" %>
<%@ page import="twitter4j.auth.RequestToken" %>
<%@include file="login.jsp" %>
<%
    session.setAttribute("twitter_oauth_start", false);

    Boolean oAuthStart = (Boolean) session.getAttribute("twitter_oauth_start");

    Twitter twitter = new TwitterFactory().getInstance();
    twitter.setOAuthConsumer(Credentials.getProperty(Credentials.TWITTER_APP_ID), Credentials.getProperty(Credentials.TWITTER_APP_SECRET));

    String pin = request.getParameter("pin");
    pin = (pin == null ? "" : pin.trim());

    RequestToken requestToken = null;

    requestToken = (RequestToken)session.getAttribute("twitter_request_token");

    if (pin.length() == 0) {
        //session.setAttribute("twitter_oauth_start", true);
        requestToken = twitter.getOAuthRequestToken();//"http://localhost:8080/site/twitter.jsp");
        session.setAttribute("twitter_request_token", requestToken);

%>
<a href="<%=requestToken.getAuthorizationURL()%>" target="_blank">Authorize App</a>

<br/> <br/>

<form method="POST" action="twitter.jsp">
    <input type="text" name="pin"/>
    <input type="submit" value="Update Pin"/>
</form>
<%
    } else {
        session.removeAttribute("twitter_request_token");
        AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, pin);
        user.setTwitterAccessToken(accessToken.getToken());
        user.setTwitterAccessTokenSecret(accessToken.getTokenSecret());

        user = ((SitePersistence) getServletConfig().getServletContext().getAttribute("persistence")).mergeUser(user);
        response.sendRedirect("/site/");
    }

%>