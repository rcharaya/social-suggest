<h1>Link accounts</h1>
<ul>
    <li>
        <%
            if (user.getFacebookAccessToken() == null) {
        %>
        <a href="facebook.jsp">Link Facebook</a>
        <%
            } else {
        %>
        Facebook [<a href="revoke.jsp?account=facebook">Revoke Access</a>]
        <%
            }
        %>
    </li>
    <li>
        <%
            if (user.getTwitterAccessToken() == null) {
        %>
        <a href="twitter.jsp">Link Twitter</a>
        <%
            } else {
        %>
        Twitter [<a href="revoke.jsp?account=twitter">Revoke Access</a>]
        <%
            }
        %>
    </li>
    <li>
        <%
            if (user.getFoursquareAccessToken() == null) {
        %>
        <a href="foursquare.jsp">FourSquare</a>
        <%
            } else {
        %>
        FourSquare [<a href="revoke.jsp?account=foursquare">Revoke Access</a>]
        <%
            }
        %>
    </li>
</ul>
