<%-- 
    Document   : registerPage
    Created on : 12.04.2016, 08:40:22
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
            function validate() {
                var form = document.forms["registerForm"];
                if (form.password.value === form.password2.value) {
                    alert("true");
                    return true;
                }
                alert(document.getElementById("passwordError"));
                document.getElementById("passwordError").innerHTML = "Passwörter stimmen nicht überein!";
                return false;
            }
        </script>
        <title>Registrieren</title>
    </head>
    <body>
        <div id="container">
            <jsp:include page="/css/cssmenu/index.html"></jsp:include>
            <div id="head">
                <h1>Help Me out! - Regestrierung</h1>
            </div>
            <div id="main">
                <form method="POST" action="WelcomePageServlet">
                    <table border="0">
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
                            <td>passwort:</td>
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
                            <td><input type="submit" value="registrieren" onsubmit="return validate()"/></td>
                            <td><input type="submit" value="abbrechen" /></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
    </body>
</html>
