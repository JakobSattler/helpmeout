/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.User;
import database.DBAccess;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Julia
 */
@WebServlet(name = "WelcomePageServlet", urlPatterns = {"/WelcomePageServlet"})
public class WelcomePageServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            DBAccess dba = DBAccess.getInstance();
            request.getSession().setAttribute("loggedIn", dba.isUserLoggedIn(request));
            request.getRequestDispatcher("jsp/welcomePage.jsp").forward(request, response);
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(WelcomePageServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request != null) {
            if (request.getAttribute("login") != null && (boolean) request.getAttribute("login")) {
                request.setAttribute("login", false);
                request.getRequestDispatcher("jsp/welcomePage.jsp").forward(request, response);
            } else {
                try {
                    String username = request.getParameter("username");
                    String password = request.getParameter("password");
                    DBAccess dba = DBAccess.getInstance();

                    if (dba.isLoginCorrect(username, password)) {
                        User user = dba.getUserByUsername(username);
                        String sessionID = user.getPassword() + user.getSalt();
                        request.getSession().setAttribute("sessionID", sessionID);
                        Cookie cookie = new Cookie("sessionID",
                                user.getPassword() + user.getSalt());
                        response.addCookie(cookie);
                        request.setAttribute("login", true);
                        response.sendRedirect("WelcomePageServlet");
                    } else {
                        request.setAttribute("loginError", "Username oder "
                                + "Passwort falsch!");
                        request.getRequestDispatcher("jsp/welcomePage.jsp").forward(request, response);
                    }
                    // TODO: User prüfen (gibt es Username? ist passwort zu username richtig?

                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(WelcomePageServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(WelcomePageServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
