/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.User;
import database.DBAccess;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
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
            getServletContext().setAttribute("categories", dba.getAllCategories());
            getServletContext().setAttribute("topics", dba.getAllTopics());
            getServletContext().setAttribute("comments", dba.getAllComments());
            System.out.println(request.getSession().getAttribute("loggedIn"));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(WelcomePageServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
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
        request.getRequestDispatcher("jsp/welcomePage.jsp").forward(request, response);
        
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
        processRequest(request, response);
        try {
            DBAccess dba = DBAccess.getInstance();
            if (request != null) {
                if (request.getAttribute("login") != null && (boolean) request.getAttribute("login")) {
                    request.setAttribute("login", false);
                    request.getRequestDispatcher("jsp/welcomePage.jsp").forward(request, response);
                } else {
                    try {
                        String username = request.getParameter("username");
                        String password = request.getParameter("password");

                        if (username != null && password != null) {
                            if (dba.isLoginCorrect(username, password)) {
                                User user = dba.getUserByUsername(username);
                                String sessionID = user.getPassword() + user.getSalt();
                                request.getSession().setAttribute("user", user);
                                
                                request.getSession().setAttribute("sessionID", sessionID);
                                Cookie cookie = new Cookie("sessionID",
                                        user.getPassword() + user.getSalt());
                                response.addCookie(cookie);
                                response.sendRedirect("WelcomePageServlet");
                            } else {
                                request.setAttribute("loginError", "Benutzername oder "
                                        + "Passwort falsch!");
                                request.getRequestDispatcher("jsp/welcomePage.jsp").forward(request, response);
                            }
                        } else if (request.getParameter("logout") != null
                                && !request.getParameter("logout").equals("")) {
                            System.out.println("logout");
                            Cookie[] cookies = request.getCookies();
                            for (Cookie cookie : cookies) {
                                if (cookie.getName().equals("sessionID")) {
                                    cookie.setMaxAge(0);
                                    response.addCookie(cookie);
                                }
                            }
                            response.sendRedirect("WelcomePageServlet");
                        } else {
                            request.getRequestDispatcher("jsp/welcomePage.jsp").forward(request, response);
                        }
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(WelcomePageServlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(WelcomePageServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(WelcomePageServlet.class.getName()).log(Level.SEVERE, null, ex);
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

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config); //To change body of generated methods, choose Tools | Templates.
    }

    
}
