<%@ page import="com.yahoo.hack.infra.config.Credentials" %>
<%@ page import="java.net.URLConnection" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@include file="login.jsp" %>
<%
    Boolean oAuthStart = (Boolean) session.getAttribute("facebook_oauth_start");
    if (oAuthStart == null || !oAuthStart) {
        session.setAttribute("facebook_oauth_start", true);
        response.sendRedirect("https://graph.facebook.com/oauth/authorize?client_id=" + Credentials.getProperty(Credentials.FACEBOOK_APP_ID) + "&display=page&redirect_uri=http://localhost:8080/site/facebook.jsp&scope=offline_access,read_stream,publish_stream");
    } else {
        session.removeAttribute("facebook_oauth_start");

        String code = request.getParameter("code");

        String nextUrl = "https://graph.facebook.com/oauth/access_token?client_id=" +
                Credentials.getProperty(Credentials.FACEBOOK_APP_ID) + "&redirect_uri=" +
                "http://localhost:8080/site/facebook.jsp" + "&client_secret=" + Credentials.getProperty(Credentials.FACEBOOK_APP_SECRET) + "&code=" + code;
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

        String[] tmp = bos.toString().split("&");
        String accessToken = null;
        for (String t : tmp) {
            if (!t.startsWith("access_token=")) {
                continue;
            }
            accessToken = t.substring("access_token=".length());
        }

        if (accessToken != null) {
            user.setFacebookAccessToken(accessToken);
            user = ((SitePersistence) getServletConfig().getServletContext().getAttribute("persistence")).mergeUser(user);
        }

        response.sendRedirect("/site/");
    }

%>