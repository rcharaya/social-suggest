<%@ page import="org.expressme.openid.OpenIdManager" %>
<%@ page import="org.expressme.openid.Endpoint" %>
<%@ page import="org.expressme.openid.Association" %>
<%@ page import="org.expressme.openid.Authentication" %>
<%@ page import="com.yahoo.hack.site.SitePersistence" %>
<%@ page import="com.yahoo.hack.infra.model.User" %>
<%
    String loginUrl = (String) session.getAttribute("openid_url");

    OpenIdManager manager = new OpenIdManager();
    manager.setReturnTo("http://localhost:8080/site/");
    manager.setRealm("http://localhost:8080");


    if (loginUrl == null) {
        Endpoint endpoint = manager.lookupEndpoint("Yahoo");
        Association association = manager.lookupAssociation(endpoint);

        String url = manager.getAuthenticationUrl(endpoint, association);
        session.setAttribute("openid_raw_mac_key", association.getRawMacKey());

        session.setAttribute("openid_url", url);
        response.sendRedirect(url);
    } else {
        session.removeAttribute("openid_url");

        byte[] rawMacKey = (byte[]) session.getAttribute("openid_raw_mac_key");
        session.removeAttribute("openid_raw_mac_key");

        Authentication auth = null;
        try {
            auth = manager.getAuthentication(request, rawMacKey);
        } catch (Exception e) {
            return;
        }

        SitePersistence persistence = (SitePersistence) getServletConfig().getServletContext().getAttribute("persistence");
        user = persistence.storeUser(auth.getIdentity());

        session.setAttribute("user_id", user.getId());
        response.sendRedirect("/site/");
    }


%>