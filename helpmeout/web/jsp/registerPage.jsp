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
            function validate(form) {
                var passwordError = document.getElementById("error");
                alert("validate");

                if (form.username.value === "" || form.username.value === null
                        || form.password.value === "" || form.password.value === null
                        || form.password2.value === "" || form.password.value === null
                        || form.email.value === "" || form.email.value === null) {
                    passwordError.textContent = "Es müssen alle Felder ausgefüllt sein!";
                    return false;
                } else if (form.password.value !== form.password2.value) {
                    passwordError.textContent = "Passwörter stimmen nicht überein!";
                    return false;
                }

                return true;
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
                <form method="POST" action="RegisterPageServlet" 
                      onsubmit="return validate(this)">
                    <table border="0">
                        <tr>
                            <td>benutzername:</td>
                            <td><input type="text" name="username"/></td>
                            <td id="userError"></td>
                        </tr>
                        <tr>
                            <td>email:</td>
                            <td><input type="text" name="email"/></td>
                        </tr>
                        <tr>
                            <td>passwort:</td>
                            <td><input type="password" name="password"/></td>
                        </tr>
                        <tr>
                            <td>passwort bestätigen:</td>
                            <td><input type="password" name="password2"/></td>
                        </tr>
                        <tr>
                            <td><input type="submit" value="registrieren"/></td>
                            <td><a href="WelcomePageServlet"><input type="button" value="abbrechen" /></a></td>
                        </tr>

                        </tbody>
                    </table>
                    <label class="error" id="error">
                        <%=request.getAttribute("error") != null ? 
                                request.getAttribute("error") : ""%>
                    </label>
                </form>
            </div>
        </div>
    </body>
</html>
