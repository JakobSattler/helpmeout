<%-- 
    Document   : newCategoryPage
    Created on : 25.04.2016, 09:23:25
    Author     : Julia
--%>

<%@page import="java.util.LinkedList"%>
<%@page import="beans.Category"%>
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
            function validate(form) {
                var error = document.getElementById("error");

                if (form.title.value === "" || form.title.value === null) {
                    error.textContent = "Es müssen alle Felder ausgefüllt sein!";
                    return false;
                }
                return true;
            }
        </script>
        <title>Neues Thema</title>
    </head>
    <body>
         <jsp:include page="/css/cssmenu/index_1.jsp"></jsp:include>
        <%! LinkedList<Category> categories = new LinkedList<>();%>
        <div id="container">
            <div id="head">
                <h1>Neue Kategorie hinzufügen</h1>
            </div>
            <form action="NewCategoryServlet" method="POST" onsubmit="return validate(this)">
                <div id="main">
                    <% if (application.getAttribute("loggedIn") != null) {
                            
                        }%>
                    <table border="0">
                        <tbody>
                            <tr>
                                <td>Kategorie:</td>
                                <td><input type="text" name="title" value="" /></td>
                            </tr>
                            <tr>
                                <td><a href="WelcomePageServlet"><input type="button" value="abbrechen" /></a></td>
                                <td><input type="submit" value="hinzufügen" /></td>
                            </tr>
                        </tbody>
                    </table>
                    <input type="submit" value="Kategorie speichern und neues Thema hinzufügen" />
                    <label class="error" id="error">
                        <%=request.getAttribute("error") != null
                                ? request.getAttribute("error") : ""%>
                    </label>
                </div>
            </form>
        </div>
    </body>
</html>
