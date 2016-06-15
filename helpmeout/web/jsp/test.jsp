<%-- 
    Document   : welcomePage
    Created on : 05.04.2016, 09:00:50
    Author     : Julia
--%>

<%@page import="beans.*"%>
<%@page import="java.util.LinkedList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset='utf-8'>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="css/welcomeStyle.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="css/cssmenu/styles.css">
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <script src="css/cssmenu/script.js"></script>
        <script type="text/javascript">
            $(document).load($(window).bind("resize", changeDivOrder));

            function changeDivOrder(e) {
                if ($(window).width() < 660)
                {
                    $("#main").remove().insertAfter($("#login"));
                } else {
                    $("#main").remove().insertBefore($("#side"));
                }
            }
        </script>
        <title>Help Me Out</title>
        <%-- --%>
    </head>
    <body>
        <%! boolean login = false;
            String pageID = "overview";
            LinkedList<Category> categories = new LinkedList<>();
            Category category;
            LinkedList<Topic> topics = new LinkedList<>();
            Topic topic;
            LinkedList<Comment> comments = new LinkedList<>();%>
        <jsp:include page="/css/cssmenu/index_1.jsp"></jsp:include>
            <form action="WelcomePageServlet" method="POST">
            <% if (session.getAttribute("loggedIn") != null) {
                    if ((boolean) session.getAttribute("loggedIn")) {
                        login = true;
                    }
                }%>
            <div id="container">
                <div id="head">
                    <h1>Help Me out!</h1>
                </div>
                <div id="main">
                    <% if (pageID.equals("overview")) {
                            if (login) { %>
                    <a href="TopicPageServlet"><input type="button" value="+ neues Thema hinzufügen" /></a>
                        <% }%>
                        <% for (Category cat : categories) {%>
                    <h3><%=cat.getTitle()%></h3>
                    <% for (Topic top : topics) {
                            if (top.getCategoryid() == cat.getCategoryid()) {%>
                    <p><%=top.getTitle()%></p>
                    <%}
                        }%>
                    <% }
                    } else if (pageID.equals("category")) {%>
                    <h3><%=category.getTitle()%></h3>
                    <% for (Topic top : topics) {
                            if (top.getCategoryid() == category.getCategoryid()) {%>
                    <h4>top.getTitle()</h4>
                    <%}
                        }%>
                    <%} else if (pageID.equals("topic")) {%>
                    <h3><%=category.getTitle()%> / <%=topic.getTitle()%></h3>
                    <% int i = 1;
                        String id = "";
                        for (Comment com : comments) {
                            if (com.getTopicid() == topic.getTopicid()) {
                                if (i % 5 == 0) {%>
                    <div id = "commentN5">
                        <% } else if (i % 4 == 0) {%>
                        <div id = "commentN4">
                            <%} else if (i % 3 == 0) { %>
                            <div id = "commentN3">
                                <%  } else if (i % 2 == 0) {%>
                                <div id = "commentN2">
                                    <%  } else {%>
                                    <div id = "commentN1">
                                        <% }
                                            i++; %>
                                    </div>
                                    <% } %>
                                    <% } %>
                                    <% }%>
                                    <%}%>
                                </div>
                                <div id="side">
                                    <div id="login">
                                        <% if (login

                                            
                                            

                                            
                                                ) { %>
                                        <h2>Willkommen BENUTZER</h2>
                                        <input type="submit" value="abmelden" />
                                        <input type="hidden" value="logout" id="logout"/>
                                        <% } else { %>
                                        <h2>Login</h2>
                                        <table border="0">
                                            <tbody>
                                                <tr>
                                                    <td>Benutzername:</td>
                                                    <td><input type="text" name="username" value="" id="username"/></td>
                                                </tr>
                                                <tr>
                                                    <td>Passwort:</td>
                                                    <td><input type="password" name="password" value="" id="password"/></td>
                                                </tr>
                                                <tr>
                                                    <td><input type="submit" value="anmelden" /></td>
                                                    <td><a href="RegisterPageServlet"><input type="button" value="registrieren"/></a></td>
                                                </tr>
                                                <tr>
                                                    <% if (request.getAttribute("loginError") != null) {%>
                                                    <td><%=request.getAttribute("loginError")%></td>
                                                    <% } %>
                                                </tr>
                                            </tbody>
                                        </table>
                                        <% }%>
                                    </div>
                                    <div id="news">
                                        <h2>Aktuelles</h2>
                                        <p>News</p>
                                    </div>
                                </div>
                            </div>
                            </form>
                            </body>
                            </html>

