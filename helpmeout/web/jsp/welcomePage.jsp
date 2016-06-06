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
                        <c:when test="${param.viewtopic != null}">
                            <c:forEach items="${topics}" var="topic">
                                <c:if test="${topic.topicid == param.viewtopic}">
                                    <c:forEach items="${categories}" var="category">
                                        <c:if test="${category.categoryid == topic.categoryid}">
                                            <h3>
                                                <c:out value="${category.title}"/>
                                            </h3>
                                        </c:if>
                                    </c:forEach>

                                    <c:set var="count" value="0" scope="page" /> 
                                    <c:forEach items="${comments}" var="comment">
                                        <c:if test="${comment.topicid == topic.topicid}">
                                            <p><b><c:out value="${comment.username}"/>:</b>
                                                <c:out value="${comment.editDate}"/></p>
                                                <c:choose>
                                                    <c:when test="${count % 5 == 0}">
                                                    <div id="commentN1"><c:out value="${comment.text}"/></div>
                                                </c:when>
                                                <c:when test="${count % 5 == 1}">
                                                    <div id="commentN2"><c:out value="${comment.text}"/></div>
                                                </c:when>
                                                <c:when test="${count % 5 == 2}">
                                                    <div id="commentN3"><c:out value="${comment.text}"/></div>
                                                </c:when>
                                                <c:when test="${count % 5 == 3}">
                                                    <div id="commentN4"><c:out value="${comment.text}"/></div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div id="commentN5"><c:out value="${comment.text}"/></div>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                        <c:set var="count" value="${count + 1}" scope="page"/>
                                    </c:forEach>
                                                <%--  <c:if test="${param.loggedIn}"> --%>
                                                <% if (loggedIn) {%>
                                        <form action="NewCommentServlet">
                                            <h2>Neuer Beitrag</h2>
                                            <textarea name="text" rows="10" cols="110"></textarea>
                                            <input type="submit" value="posten" />
                                        </form>   
                                        <% }%>
                                  <%--  </c:if>--%>
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
                        <% if (loggedIn) {%>
                        <a href="NewTopicPageServlet">
                            <input type="button" value="+ Neues Thema hinzufügen" />
                        </a>
                        <% }%>
                    </div>
                </div>
        </form>
    </body>
</html>
