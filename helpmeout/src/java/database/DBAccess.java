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

    //class implemented as singletone
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
            String email = rs.getString("email");
            String password = rs.getString("password");
            String salt = rs.getString("salt");
            LocalDate registerDate = rs.getDate("registerdate").toLocalDate();
            userList.add(new User(username, email, password, salt, registerDate));
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
     * Returns a user from the database
     *
     * @return
     * @throws Exception
     */
    public User getUserByUsername(String username) throws Exception {
        Connection conn = connPool.getConnection();
        User user = null;

        PreparedStatement getUserByUsernameStmt = getUserByUsernameStmts.get(conn);
        if (getUserByUsernameStmt == null) {
            getUserByUsernameStmt = conn.prepareStatement(getAllUsersSqlString);
            getUserByUsernameStmts.put(conn, getUserByUsernameStmt);
        }

        ResultSet rs = getUserByUsernameStmt.executeQuery();
        while (rs.next()) {
            String name = rs.getString("username");
            String email = rs.getString("email");
            String password = rs.getString("password");
            String salt = rs.getString("salt");
            LocalDate registerDate = rs.getDate("registerdate").toLocalDate();
            user = new User(name, email, password, salt, registerDate);
        }
        connPool.releaseConnection(conn);
        return user;
    }

    //Create user
    private final HashMap<Connection, PreparedStatement> createUserStmts
            = new HashMap<>();
    private final String createUserSqlString = "INSERT INTO user "
            + "(username, email, password, salt, registerdate) "
            + "VALUES (?, ?, ?, ?, ?)";

    /**
     * Saves a user to the database
     *
     * @param user
     * @throws database.DBAccess.UserAlreadyExistsException
     * @throws Exception
     */
    public void createUser(User user) throws UserAlreadyExistsException, Exception {
        if (!getUserByUsername(user.getUsername()).equals(user.getUsername())) {
            Connection conn = connPool.getConnection();

            PreparedStatement createUserStmt = createUserStmts.get(conn);
            if (createUserStmt == null) {
                createUserStmt = conn.prepareStatement(createUserSqlString);
                createUserStmts.put(conn, createUserStmt);
            }

            createUserStmt.setString(1, user.getUsername());
            createUserStmt.setString(2, user.getEmail());
            createUserStmt.setString(3, user.getPassword());
            createUserStmt.setString(4, user.getSalt());
            createUserStmt.setDate(5, Date.valueOf(user.getRegisterDate()));
            createUserStmt.executeUpdate();

            connPool.releaseConnection(conn);
        } else {
            throw new UserAlreadyExistsException("User already exists!");
        }
    }

    //Check if password is correct
    private final HashMap<Connection, PreparedStatement> getPasswordByUserStmts
            = new HashMap<>();
    private final String getPasswordByUserSqlString = "SELECT password, salt FROM user "
            + "WHERE username =  ? "
            + "LIMIT 0 , 30";

    /**
     * Returns true if the password for a user is correct, false if not
     * 
     * @param username
     * @param password md5-hash of the users password
     * @return
     * @throws Exception 
     */
    public boolean isPasswordCorrect(String username, String password) throws Exception {
        Connection conn = connPool.getConnection();
        Boolean passwordIsCorrect = false;

        PreparedStatement getPasswordByUserStmt = getPasswordByUserStmts.get(conn);
        if (getPasswordByUserStmt == null) {
            getPasswordByUserStmt = conn.prepareStatement(getPasswordByUserSqlString);
            getPasswordByUserStmts.put(conn, getPasswordByUserStmt);
        }
        getPasswordByUserStmt.setString(1, username);

        ResultSet rs = getPasswordByUserStmt.executeQuery();
        while (rs.next()) {
            String salt = rs.getString("salt");
            String passwordHash = rs.getString("password");
            if (User.createPasswordHash(password + salt).equals(passwordHash)) {
                passwordIsCorrect = true;
            }
        }
        connPool.releaseConnection(conn);
        return passwordIsCorrect;
    }

    //Create comment
    private final HashMap<Connection, PreparedStatement> createTopicStmts
            = new HashMap<>();
    private final String createTopicSqlString = "INSERT INTO topic "
            + "(categoryid, username, title, createdate) "
            + "VALUES (?, ?, ?, ?)";

    /**
     * Saves a topic to the database
     *
     * @param categoryid ID of the category the topic belongs to
     * @param username Name of the user which creates the topic
     * @param title Title of the topic
     * @param createDate Date when the topic is created
     * @throws Exception
     */
    public void createTopic(int categoryid, String username, String title,
            LocalDate createDate) throws Exception {
        Connection conn = connPool.getConnection();
        PreparedStatement createTopicStmt = createTopicStmts.get(conn);
        if (createTopicStmt == null) {
            createTopicStmt = conn.prepareStatement(createTopicSqlString);
            createTopicStmts.put(conn, createTopicStmt);
        }

        createTopicStmt.setInt(1, categoryid);
        createTopicStmt.setString(2, username);
        createTopicStmt.setString(3, title);
        createTopicStmt.setDate(4, Date.valueOf(createDate));
        createTopicStmt.executeUpdate();

        connPool.releaseConnection(conn);
    }
    //ConnectionPool
    private DBConnectionPool connPool;

    private DBAccess() throws ClassNotFoundException {
        connPool = DBConnectionPool.getInstance();
    }

    public static void main(String[] args) {
        try {
            DBAccess dba = DBAccess.getInstance();
            dba.createTopic(1, "jakob", "hallo", LocalDate.now());
            for (User u : dba.getAllUsers()) {
                System.out.println(u);
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public class UserAlreadyExistsException extends Exception {

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
