<%-- 
    Document   : myPage
    Created on : 19.04.2016, 08:16:39
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
        <title>Mein Konto</title>
    </head>
    <body>
        <jsp:include page="/css/cssmenu/index.html"></jsp:include>
    <div id="container">
        <div id="head"> 
            <h1>Mein Konto</h1>
        </div> 
        <div id="main">
            <table border="0">
                <tbody>
                    <tr>
                        <td>benutzername:</td>
                        <td><input type="text" name="username" value="" /></td>
                        <td id="userError"></td>
                    </tr>
                    <tr>
                        <td>email:</td>
                        <td><input type="text" name="email" value="" /></td>
                    </tr>
                    <tr>
                        <td>altes passwort:</td>
                        <td><input type="password" name="passwordOld" value="" /></td>
                    </tr>
                    <tr>
                        <td>neues passwort:</td>
                        <td><input type="password" name="password" value="" /></td>
                    </tr>
                    <tr>
                        <td>passwort bestätigen:</td>
                        <td><input type="password" name="password2" value="" /></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="hidden" name="passwordError"/></td>
                    </tr>
                    <tr>
                        <td><input type="submit" value="speichern" onsubmit="return validate()"/></td>
                        <td><a href="WelcomePageServlet"><input type="submit" value="abbrechen" /></a></td>
                    </tr>
                </tbody>
            </table>

        </div>
    </div>
</body>
</html>
