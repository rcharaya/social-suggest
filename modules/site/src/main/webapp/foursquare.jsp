<%@ page import="com.yahoo.hack.infra.config.Credentials" %>
<%@ page import="java.net.URLConnection" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="com.google.gson.JsonParser" %>
<%@ page import="com.google.gson.JsonObject" %>
<%@include file="login.jsp" %>
<%
    Boolean oAuthStart = (Boolean) session.getAttribute("foursquare_oauth_start");
    if (oAuthStart == null || !oAuthStart) {
        session.setAttribute("foursquare_oauth_start", true);
        response.sendRedirect("https://foursquare.com/oauth2/authenticate?client_id=" + Credentials.getProperty(Credentials.FOURSQUARE_APP_ID) + "&response_type=code&redirect_uri=http://localhost:8080/site/foursquare.jsp");
    } else {
        session.removeAttribute("foursquare_oauth_start");

        String code = request.getParameter("code");

        String nextUrl = "https://foursquare.com/oauth2/access_token?client_id=" + Credentials.getProperty(Credentials.FOURSQUARE_APP_ID)+ "&client_secret=" + Credentials.getProperty(Credentials.FOURSQUARE_APP_SECRET) + "&grant_type=authorization_code&redirect_uri=http://localhost:8080/site/foursquare.jsp&code=" + code;

        URL url = new URL(nextUrl);
        URLConnection conn = url.openConnection();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream is = conn.getInputStream();
        byte[] buff = new byte[1024];
        int count;
        while ((count = is.read(buff)) > 0) {
            bos.write(buff, 0, count);
        }
        is.close();
        bos.close();

        JsonObject root = new JsonParser().parse(bos.toString()).getAsJsonObject();
        String accessToken = root.get("access_token").getAsString();

        if (accessToken != null) {
            user.setFoursquareAccessToken(accessToken);
            user = ((SitePersistence) getServletConfig().getServletContext().getAttribute("persistence")).mergeUser(user);
        }

        response.sendRedirect("/site/");
    }

%>