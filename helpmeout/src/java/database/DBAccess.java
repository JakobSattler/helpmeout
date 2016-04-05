/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import beans.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Jakob
 */
public class DBAccess {

    //class implemented ass singletone
    private static DBAccess theInstance = null;
    
    //Get all users
    private final HashMap<Connection, PreparedStatement> getAllUsersStmts
            = new HashMap<>();
    private final String getAllUsersSqlString = "SELECT * FROM user";
    public LinkedList<User> getAllUsers() throws Exception {
        LinkedList<User> userList = new LinkedList<>();
        Connection conn = connPool.getConnection();

        PreparedStatement getAllUsersStmt = getAllUsersStmts.get(conn);
        if(getAllUsersStmt == null){
            getAllUsersStmt = conn.prepareStatement(getAllUsersSqlString);
        }
        
        ResultSet rs = getAllUsersStmt.executeQuery();
        while(rs.next()){
            String username = rs.getString("username");
            String password = rs.getString("password");
            LocalDate registerDate = rs.getDate("registerdate").toLocalDate();
            userList.add(new User(username, password, registerDate));
        }
        connPool.releaseConnection(conn);
        return userList;
    }
    
    public static DBAccess getInstance() throws ClassNotFoundException {
        if (theInstance == null) {
            theInstance = new DBAccess();
        }
        return theInstance;
    }

    //ConnectionPool
    private DBConnectionPool connPool;

    private DBAccess() throws ClassNotFoundException {
        connPool = DBConnectionPool.getInstance();
    }

    public static void main(String[] args) {
        try {
            DBAccess dba = DBAccess.getInstance();
            LinkedList<User> users = dba.getAllUsers();
            for (User user : users) {
                System.out.println(user);
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

}
