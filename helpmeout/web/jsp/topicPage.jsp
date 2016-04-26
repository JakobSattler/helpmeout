<%-- 
    Document   : topicPage
    Created on : 25.04.2016, 09:23:25
    Author     : Julia
--%>

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
        <title>Neues Thema</title>
    </head>
    <body>
        <jsp:include page="/css/cssmenu/index.html"></jsp:include>
        <div id="container">
            <div id="head">
                <h1>Neues Thema hinzufügen</h1>
            </div>
            <form action="TopicPageServlet" method="POST">
                <div id="main">
                    <table border="0">
                        <tbody>
                            <tr>
                                <td>Kategorie:</td>
                                <td ><select name="category" style="width:100%;">
                                        <option></option>
                                    </select></td>
                            </tr>
                            <tr>
                                <td>Titel:</td>
                                <td><input type="text" name="title" value="" style="width:100%;"/></td>
                            </tr>
                            <tr>
                                <td>Text:</td>
                                <td><textarea name="text" rows="10" cols="30" style="width:100%;">
                                    </textarea></td>
                            </tr>
                            <tr>
                                <td><input type="submit" value="Hinzufügen" /></td>
                                <td><input type="submit" value="Abbrechen" /></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </form>
        </div>
    </body>
</html>
