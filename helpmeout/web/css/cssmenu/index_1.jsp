<%@page import="beans.Topic"%>
<%@page import="java.util.LinkedList"%>
<%@page import="beans.Category"%>
<%@page import="database.DBAccess"%>
<!--<html lang="en">
    <head>
   <meta charset='utf-8'>
   <meta http-equiv="X-UA-Compatible" content="IE=edge">
   <meta name="viewport" content="width=device-width, initial-scale=1">
   <link rel="stylesheet" href="styles.css">
   <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
   <script src="script.js"></script>
   <title>CSS MenuMaker</title>
</head>
<body>-->

<div>
    <%! private DBAccess dba;
        private LinkedList<Category> categories = new LinkedList<>();
        private LinkedList<Topic> topics = new LinkedList<>();
    %>
    <%  dba = DBAccess.getInstance();
        categories = dba.getAllCategories();
        topics = dba.getAllTopics();%>
    <ul id="menu-bar">
        <li><a href='WelcomePageServlet?pageID=overview'><span>Startseite</span></a></li>
            <% for (Category cat : categories) {%>
        <li><a href='WelcomePageServlet?viewcategory=<%=cat.getCategoryid()%>'><span><%=cat.getTitle()%> </span></a>
            <ul>
                <% for (Topic top : topics) {
                if (top.getCategoryid() == cat.getCategoryid()) {%>
                <li><a href='WelcomePageServlet?viewtopic=<%=top.getTopicid()%>'><span><%=top.getTitle()%></span></a></li>
                    <% }
               } %>
            </ul>
        </li>
        <%}%>

        <!--<ul>
           <li><a><span></span></a></li>
        </ul>-->
        <li><a href='MyPageServlet'><span>Mein Konto</span></a></li>
    </ul>
</div>

<!--</body>
</html> -->
