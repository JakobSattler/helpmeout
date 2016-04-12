/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import beans.User;
import java.sql.Connection;
import java.sql.Date;
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

    public static DBAccess getInstance() throws ClassNotFoundException {
        if (theInstance == null) {
            theInstance = new DBAccess();
        }
        return theInstance;
    }

    //Get all users
    private final HashMap<Connection, PreparedStatement> getAllUsersStmts
            = new HashMap<>();
    private final String getAllUsersSqlString = "SELECT * FROM user";

    /**
     * Returns all users from the database
     *
     * @return
     * @throws Exception
     */
    public LinkedList<User> getAllUsers() throws Exception {
        LinkedList<User> userList = new LinkedList<>();
        Connection conn = connPool.getConnection();

        PreparedStatement getAllUsersStmt = getAllUsersStmts.get(conn);
        if (getAllUsersStmt == null) {
            getAllUsersStmt = conn.prepareStatement(getAllUsersSqlString);
            getAllUsersStmts.put(conn, getAllUsersStmt);
        }

        ResultSet rs = getAllUsersStmt.executeQuery();
        while (rs.next()) {
            String username = rs.getString("username");
            String password = rs.getString("password");
            LocalDate registerDate = rs.getDate("registerdate").toLocalDate();
            userList.add(new User(username, password, registerDate));
        }
        connPool.releaseConnection(conn);
        return userList;
    }

    //Get user by username
    private final HashMap<Connection, PreparedStatement> getUserByUsernameStmts
            = new HashMap<>();
    private final String getUserByUsernameSqlString = "SELECT * FROM user "
            + "WHERE username = ?";

    /**
     * Returns all users from the database
     *
     * @return
     * @throws Exception
     */
    public User getUserByUsername(String username) throws Exception {
        Connection conn = connPool.getConnection();
        User user = null;

        PreparedStatement getUserByUsername = getUserByUsernameStmts.get(conn);
        if (getUserByUsername == null) {
            getUserByUsername = conn.prepareStatement(getAllUsersSqlString);
            getUserByUsernameStmts.put(conn, getUserByUsername);
        }

        ResultSet rs = getUserByUsername.executeQuery();
        while (rs.next()) {
            String name = rs.getString("username");
            String password = rs.getString("password");
            LocalDate registerDate = rs.getDate("registerdate").toLocalDate();
            user = new User(name, password, registerDate);
        }
        connPool.releaseConnection(conn);
        return user;
    }

    //Create user
    //Get all users
    private final HashMap<Connection, PreparedStatement> createUserStmts
            = new HashMap<>();
    private final String createUserSqlString = "INSERT INTO user "
            + "(username, password, registerdate) "
            + "VALUES (?, ?, ?)";

    public void createUser(User user) throws UserAlreadyExistsException, Exception {
        if (getUserByUsername(user.getUsername()) == null) {
            Connection conn = connPool.getConnection();

            PreparedStatement createUserStmt = createUserStmts.get(conn);
            if (createUserStmt == null) {
                createUserStmt = conn.prepareStatement(createUserSqlString);
                createUserStmts.put(conn, createUserStmt);
            }

            createUserStmt.setString(1, user.getUsername());
            createUserStmt.setString(2, user.getPassword());
            createUserStmt.setDate(3, Date.valueOf(user.getRegisterDate()));
            createUserStmt.executeUpdate();

            connPool.releaseConnection(conn);
        }
        else{
            throw new UserAlreadyExistsException("User already exists!");
        }
    }

    //ConnectionPool
    private DBConnectionPool connPool;

    private DBAccess() throws ClassNotFoundException {
        connPool = DBConnectionPool.getInstance();
    }

    public static void main(String[] args) {
        try {
            DBAccess dba = DBAccess.getInstance();
            dba.createUser(new User("jakobe", "asdf", LocalDate.now()));
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    private class UserAlreadyExistsException extends Exception{

        public UserAlreadyExistsException() {
        }

        public UserAlreadyExistsException(String message) {
            super(message);
        }

        public UserAlreadyExistsException(String message, Throwable cause) {
            super(message, cause);
        }

        public UserAlreadyExistsException(Throwable cause) {
            super(cause);
        }

        public UserAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
        
        
    }
}
