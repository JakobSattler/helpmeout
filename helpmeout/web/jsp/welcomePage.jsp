<%-- 
    Document   : welcomePage
    Created on : 05.04.2016, 09:00:50
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
        <script type="text/javascript">
            $(document).load($(window).bind("resize", changeDivOrder));

            function changeDivOrder(e) {
                if ($(window).width() < 650)
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
        <jsp:include page="/css/cssmenu/index.html"></jsp:include>
        <div id="container">
            <div id="head">
                <h1>Help Me out!</h1>
            </div>
            <div id="main">
                <h3>Mathe</h3>
                <p>Geometrie</p>
                <p>Terme</p>
                <h3>Deutsch</h3>
                <p>Lyrik</p>
                <p>Texte</p>
            </div>
            <div id="side">
                <div id="login">
                    <h2>Login</h2>
                    <table border="0">
                        <tbody>
                            <tr>
                                <td>benutzername:</td>
                                <td><input type="text" name="username" value="" /></td>
                            </tr>
                            <tr>
                                <td>passwort:</td>
                                <td><input type="text" name="password" value="" /></td>
                            </tr>
                            <tr>
                                <td><input type="submit" value="registrieren" /></td>
                                <td><input type="submit" value="anmelden" /></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div id="news">
                    <h2>Aktuelles</h2>
                    <p>News</p>
                </div>
            </div>
        </div>
    </body>
</html>
