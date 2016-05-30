<%-- 
    Document   : welcomePage
    Created on : 05.04.2016, 09:00:50
    Author     : Julia
--%>

<%@page import="database.DBAccess"%>
<%@page import="beans.*"%>
<%@page import="java.util.LinkedList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    </head>
    <body>
        <%!
            private boolean loggedIn;
            private DBAccess dba;
            private User user;
            private String pageID = "overview";
            private LinkedList<Category> categories = new LinkedList<>();
            private Category category;
            private LinkedList<Topic> topics = new LinkedList<>();
            private Topic topic;
            private LinkedList<Comment> comments = new LinkedList<>();
        %>
        <%
            loggedIn = (boolean) session.getAttribute("loggedIn");
            dba = DBAccess.getInstance();
            user = (User) session.getAttribute("user");
        %>
        <jsp:include page="/css/cssmenu/index.html"></jsp:include>
            <form action="WelcomePageServlet" method="POST">
            <% if (session.getAttribute("loggedIn") != null) {
                    if ((boolean) session.getAttribute("loggedIn")) {
                        loggedIn = true;
                    }
                }
                if (request.getParameter("topic") != null) {
                    topic = dba.getTopic(Integer.parseInt(request.getParameter("topic")));
                    category = dba.getCategory(topic.getCategoryid());
                    pageID = "topic";
                }
                if (request.getParameter("category") != null) {
                    category = dba.getCategory(Integer.parseInt(request.getParameter("category")));
                    pageID = "category";
                }
                if (request.getParameter("pageID") != null) {
                    pageID = request.getParameter("pageID");
                }%>
            <div id="container">
                <div id="head">
                    <h1>Help Me out!</h1>
                </div>
                <div id="main">
                    <c:choose>
                        <c:when test="${param.viewcategory != null}">
                            <c:forEach items="${categories}" var="category">
                                <c:if test="${category.categoryid == param.viewcategory}">
                                    <a href="WelcomePageServlet?viewcategory=<c:out value="${category.categoryid}"/>">
                                        <h3>
                                            <c:out value="${category.title}"/>
                                        </h3>
                                    </a>
                                    <c:forEach items="${topics}" var="topic">
                                        <c:if test="${topic.categoryid == category.categoryid}">
                                            <a href="WelcomePageServlet?viewtopic=<c:out value="${topic.topicid}"/>">
                                                <p>
                                                    <c:out value="${topic.title}"/>
                                                </p>
                                            </a>
                                        </c:if>     
                                    </c:forEach>
                                </c:if>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${categories}" var="category">
                                <a href="WelcomePageServlet?viewcategory=<c:out value="${category.categoryid}"/>">
                                    <h3>
                                        <c:out value="${category.title}"/>
                                    </h3>
                                </a>
                                <c:forEach items="${topics}" var="topic">
                                    <c:if test="${topic.categoryid == category.categoryid}">
                                        <a href="WelcomePageServlet?viewtopic=<c:out value="${topic.topicid}"/>">
                                            <p><c:out value="${topic.title}"/></p>
                                        </a>
                                    </c:if>
                                </c:forEach>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    <%--
                    <%
                        if (pageID.equals(
                                "category")) {%>
                    <h3><%=category.getTitle()%></h3>
                    <% topics = dba.getTopicsByCategory(category.getCategoryid());
                        for (Topic top : topics) {
                            if (top.getCategoryid() == category.getCategoryid()) {%>
                    <h4><%=top.getTitle()%></h4>
                    <%}
                        }%>
                    <%} else if (pageID.equals(
                            "topic")) {%>
                    <h3><%=category.getTitle()%> / <%=topic.getTitle()%></h3>
                    <% int i = 0;
                        comments = dba.getCommentsByTopic(topic.getTopicid());
                        for (Comment com : comments) {
                            if (com.getTopicid() == topic.getTopicid()) {
                                i++;
                                if (i % 5 == 0) {%>
                    <div id="commentN1"><%=com.getText()%></div>
                    <% } else if (i % 5 == 1) {%>
                    <div id="commentN2"><%=com.getText()%></div>
                    <% } else if (i % 5 == 2) {%>
                    <div id="commentN3"><%=com.getText()%></div>
                    <% } else if (i % 5 == 3) {%>
                    <div id="commentN4"><%=com.getText()%></div>
                    <% } else {%>
                    <div id="commentN5"><%=com.getText()%></div>
                    <%}%>    
                    <%  }
                        } %>
                    <% }
                        if (loggedIn) {%>
                    <a href="TopicPageServlet">
                        <input type="button" value="+ Neues Thema hinzufügen" />
                    </a>
                    <% } %>
                    --%>
                </div>
                <div id="side">
                    <div id="login">
                        <% if (loggedIn) {%>
                        <h2>Willkommen <%=user.getUsername()%></h2>
                        <input type="hidden" value="logout" name="logout" id="logout"/>
                        <input type="submit" value="Abmelden" />
                        <% } else {%>
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
                                    <td><input type="submit" value="Anmelden" /></td>
                                    <td><a href="RegisterPageServlet"><input type="button" value="Registrieren"/></a></td>
                                </tr>
                            </tbody>
                        </table>
                        <c:if test="${loginError != null}">
                            <label class="error"><c:out value="${loginError}"/></label>
                        </c:if>
                        <% }%>
                    </div>
                    <div id="news">
                        <h2>Aktuelles</h2>
                        <p>News</p>
                    </div>
                </div>
        </form>
    </body>
</html>
