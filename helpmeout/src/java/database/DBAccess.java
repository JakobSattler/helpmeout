/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import beans.Category;
import beans.Comment;
import beans.Topic;
import beans.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.codec.binary.Base64;

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

    /**
     * Returns the hash value of the password argument
     *
     * @param password password of the new user in plain text
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String createPasswordHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : result) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    /**
     * Returns a new random salt
     *
     * @return
     */
    public String createPasswordSalt() {
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        String passwordSalt = Base64.encodeBase64String(salt);
        return passwordSalt;
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

    //Get all categories
    private final HashMap<Connection, PreparedStatement> getAllCategoriesStmts
            = new HashMap<>();
    private final String getAllCategoriesSqlString = "SELECT * FROM category;";

    /**
     * Returns all categories from the database
     *
     * @return
     */
    public LinkedList<Category> getAllCategories() throws SQLException, Exception {
        LinkedList<Category> categoryList = new LinkedList<>();
        Connection conn = connPool.getConnection();

        PreparedStatement getAllCategoriesStmt = getAllCategoriesStmts.get(conn);
        if (getAllCategoriesStmt == null) {
            getAllCategoriesStmt = conn.prepareStatement(getAllCategoriesSqlString);
            getAllCategoriesStmts.put(conn, getAllCategoriesStmt);
        }

        ResultSet rs = getAllCategoriesStmt.executeQuery();
        while (rs.next()) {
            int id = Integer.parseInt(rs.getString("categoryid"));
            String title = rs.getString("title");
            categoryList.add(new Category(id, title));
        }
        connPool.releaseConnection(conn);
        return categoryList;
    }

    //Get all topics
    private final HashMap<Connection, PreparedStatement> getAllTopicsStmts
            = new HashMap<>();
    private final String getAllTopicsSqlString = "SELECT * FROM topic;";

    /**
     * Returns all topics from the database
     *
     * @return
     */
    public LinkedList<Topic> getAllTopics() throws SQLException, Exception {
        LinkedList<Topic> topicList = new LinkedList<>();
        Connection conn = connPool.getConnection();

        PreparedStatement getAllTopicsStmt = getAllTopicsStmts.get(conn);
        if (getAllTopicsStmt == null) {
            getAllTopicsStmt = conn.prepareStatement(getAllTopicsSqlString);
            getAllTopicsStmts.put(conn, getAllTopicsStmt);
        }

        ResultSet rs = getAllTopicsStmt.executeQuery();
        while (rs.next()) {
            int id = Integer.parseInt(rs.getString("topicid"));
            int catId = Integer.parseInt(rs.getString("categoryid"));
            String title = rs.getString("title");
            String username = rs.getString("username");
            LocalDate createDate = LocalDate.parse(rs.getString("createDate"));
            topicList.add(new Topic(id, catId, username, title, createDate));
        }
        connPool.releaseConnection(conn);
        return topicList;
    }

    //Get all comments
    private final HashMap<Connection, PreparedStatement> getAllCommentsStmts
            = new HashMap<>();
    private final String getAllCommentsSqlString = "SELECT * FROM comment;";

    /**
     * Returns all comments from the database
     *
     * @return
     */
    public LinkedList<Comment> getAllComments() throws SQLException, Exception {
        LinkedList<Comment> commentList = new LinkedList<>();
        Connection conn = connPool.getConnection();

        PreparedStatement getAllCommentsStmt = getAllCommentsStmts.get(conn);
        if (getAllCommentsStmt == null) {
            getAllCommentsStmt = conn.prepareStatement(getAllCommentsSqlString);
            getAllCommentsStmts.put(conn, getAllCommentsStmt);
        }

        ResultSet rs = getAllCommentsStmt.executeQuery();
        while (rs.next()) {
            int id = Integer.parseInt(rs.getString("commentid"));
            int topicId = Integer.parseInt(rs.getString("topicid"));
            String text = rs.getString("text");
            String username = rs.getString("username");
            LocalDate editDate = LocalDate.parse(rs.getString("editdate"));
            commentList.add(new Comment(id, topicId, username, text, editDate));
        }
        connPool.releaseConnection(conn);
        return commentList;
    }

    //Get category
    private final HashMap<Connection, PreparedStatement> getCategoryStmts
            = new HashMap<>();
    private final String getCategorySqlString = "SELECT * FROM category WHERE categoryid = ?;";

    /**
     * Returns category from the database
     *
     * @return
     */
    public Category getCategory(int catID) throws SQLException, Exception {
        Category cat = null;
        Connection conn = connPool.getConnection();

        PreparedStatement getCategoryStmt = getCategoryStmts.get(conn);
        if (getCategoryStmt == null) {
            getCategoryStmt = conn.prepareStatement(getCategorySqlString);
            getCategoryStmts.put(conn, getCategoryStmt);
        }
        getCategoryStmt.setInt(1, catID);
        ResultSet rs = getCategoryStmt.executeQuery();
        while (rs.next()) {
            int id = Integer.parseInt(rs.getString("categoryid"));
            String title = rs.getString("title");
            cat = new Category(id, title);
        }
        connPool.releaseConnection(conn);
        return cat;
    }

    //Get topics form category
    private final HashMap<Connection, PreparedStatement> getTopicsByCategoryStmts
            = new HashMap<>();
    private final String getTopicsByCategoryString = "SELECT * FROM topic WHERE categoryid = ?;";

    /**
     * Returns all categories from the database
     *
     * @return
     */
    public LinkedList<Topic> getTopicsByCategory(int category) throws SQLException, Exception {
        LinkedList<Topic> topicList = new LinkedList<>();
        Connection conn = connPool.getConnection();

        PreparedStatement getTopicsByCategoryStmt = getTopicsByCategoryStmts.get(conn);
        if (getTopicsByCategoryStmt == null) {
            getTopicsByCategoryStmt = conn.prepareStatement(getTopicsByCategoryString);
            getTopicsByCategoryStmts.put(conn, getTopicsByCategoryStmt);
        }
        getTopicsByCategoryStmt.setInt(1, category);
        ResultSet rs = getTopicsByCategoryStmt.executeQuery();
        while (rs.next()) {
            int id = Integer.parseInt(rs.getString("topicid"));
            String username = rs.getString("username");
            String title = rs.getString("title");
            LocalDate createDate = rs.getDate("createdate").toLocalDate();
            topicList.add(new Topic(id, category, username, title, createDate));
        }
        connPool.releaseConnection(conn);
        return topicList;
    }

    //Get topic
    private final HashMap<Connection, PreparedStatement> getTopicStmts
            = new HashMap<>();
    private final String getTopicSqlString = "SELECT * FROM topic WHERE topicid = ?;";

    /**
     * Returns category from the database
     *
     * @return
     */
    public Topic getTopic(int topID) throws SQLException, Exception {
        Topic top = null;
        Connection conn = connPool.getConnection();

        PreparedStatement getTopicStmt = getTopicStmts.get(conn);
        if (getTopicStmt == null) {
            getTopicStmt = conn.prepareStatement(getTopicSqlString);
            getTopicStmts.put(conn, getTopicStmt);
        }
        getTopicStmt.setInt(1, topID);
        ResultSet rs = getTopicStmt.executeQuery();
        while (rs.next()) {
            int categoryid = Integer.parseInt(rs.getString("categoryid"));
            String username = rs.getString("username");
            String title = rs.getString("title");
            LocalDate createDate = rs.getDate("createdate").toLocalDate();
            top = new Topic(topID, categoryid, username, title, createDate);
        }
        connPool.releaseConnection(conn);
        return top;
    }

    //Get topics form category
    private final HashMap<Connection, PreparedStatement> getCommentsByTopicStmts
            = new HashMap<>();
    private final String getCommentsByTopicString = "SELECT * FROM topic WHERE categoryid = ?;";

    /**
     * Returns all categories from the database
     *
     * @return
     */
    public LinkedList<Comment> getCommentsByTopic(int topic) throws SQLException, Exception {
        LinkedList<Comment> commentList = new LinkedList<>();
        Connection conn = connPool.getConnection();

        PreparedStatement getCommentsByTopicStmt = getCommentsByTopicStmts.get(conn);
        if (getCommentsByTopicStmt == null) {
            getCommentsByTopicStmt = conn.prepareStatement(getCommentsByTopicString);
            getCommentsByTopicStmts.put(conn, getCommentsByTopicStmt);
        }
        getCommentsByTopicStmt.setInt(1, topic);
        ResultSet rs = getCommentsByTopicStmt.executeQuery();
        while (rs.next()) {
            int id = Integer.parseInt(rs.getString("commentid"));
            String username = rs.getString("username");
            String text = rs.getString("text");
            LocalDate editdate = rs.getDate("editdate").toLocalDate();
            commentList.add(new Comment(id, topic, username, text, editdate));
        }
        connPool.releaseConnection(conn);
        return commentList;
    }

    //Get user by username
    private final HashMap<Connection, PreparedStatement> getUserByUsernameStmts
            = new HashMap<>();
    private final String getUserByUsernameSqlString = "SELECT * FROM user "
            + "WHERE username = ?";

    /**
     * Returns a user from the database
     *
     * @param username
     * @return
     * @throws Exception
     */
    public User getUserByUsername(String username) throws Exception {
        Connection conn = connPool.getConnection();
        User user = null;

        PreparedStatement getUserByUsernameStmt = getUserByUsernameStmts.get(conn);
        if (getUserByUsernameStmt == null) {
            getUserByUsernameStmt = conn.prepareStatement(getUserByUsernameSqlString);
            getUserByUsernameStmts.put(conn, getUserByUsernameStmt);
        }
        getUserByUsernameStmt.setString(1, username);

        ResultSet rs = getUserByUsernameStmt.executeQuery();
        while (rs.next()) {
            String name = rs.getString("username");
            String email = rs.getString("email");
            String password = rs.getString("password");
            String salt = rs.getString("salt");
            LocalDate registerDate = rs.getDate("registerdate").toLocalDate();
            user = new User(name, email, password, salt, registerDate);
            System.out.println(user);
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
     * @param username
     * @param password password in plain text
     * @param email
     * @param role possible roles: admin, user
     * @throws database.DBAccess.UserAlreadyExistsException
     * @throws Exception
     */
    public void createUser(String username, String password,
            String email, String role) throws UserAlreadyExistsException, Exception {
        System.out.println(getUserByUsername(username) == null);
        if (getUserByUsername(username) == null) {
            Connection conn = connPool.getConnection();

            PreparedStatement createUserStmt = createUserStmts.get(conn);
            if (createUserStmt == null) {
                createUserStmt = conn.prepareStatement(createUserSqlString);
                createUserStmts.put(conn, createUserStmt);
            }
            String passwordSalt = createPasswordSalt();
            String passwordHash = createPasswordHash(password + passwordSalt);

            createUserStmt.setString(1, username.toLowerCase());
            createUserStmt.setString(2, email.toLowerCase());
            createUserStmt.setString(3, passwordHash);
            createUserStmt.setString(4, passwordSalt);
            createUserStmt.setDate(5, Date.valueOf(LocalDate.now()));
            createUserStmt.executeUpdate();

            String[] permissions;
            switch (role.toLowerCase()) {
                case "admin":
                    permissions = new String[]{"CREATE_TOPIC", "CREATE_COMMENT",
                        "CREATE_CATEGORY"};
                    createUserPermissions(username, permissions);
                    break;
                case "user":
                    permissions = new String[]{"CREATE_TOPIC", "CREATE_COMMENT"};
                    createUserPermissions(username, permissions);
                    break;
            }
            connPool.releaseConnection(conn);
        } else {
            throw new UserAlreadyExistsException("User already exists!");
        }
    }

    //Create userpermission
    private final HashMap<Connection, PreparedStatement> createUserpermissionStmts
            = new HashMap<>();
    private final String createUserPermissionSqlString = "INSERT INTO userpermission "
            + "VALUES (?, ?)";

    /**
     * Creates permission for a user<br>
     * <br>
     * <b>Permissions:</b><br>
     * CREATE_TOPIC, CREATE_COMMENT, CREATE_CATEGORY
     *
     * @param username
     * @param permissions
     */
    public void createUserPermissions(String username, String... permissions) throws Exception {
        Connection conn = connPool.getConnection();
        PreparedStatement createUserpermissionStmt = createUserpermissionStmts.get(conn);
        if (createUserpermissionStmt == null) {
            createUserpermissionStmt = conn.prepareStatement(createUserPermissionSqlString);
            getPasswordByUserStmts.put(conn, createUserpermissionStmt);
        }
        for (String permission : permissions) {
            createUserpermissionStmt.setString(1, username.toLowerCase());
            createUserpermissionStmt.setString(2, permission.toLowerCase());
            createUserpermissionStmt.executeUpdate();
        }
        connPool.releaseConnection(conn);
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
    public boolean isLoginCorrect(String username, String password) throws Exception {
        Connection conn = connPool.getConnection();
        Boolean loginIsCorrect = false;

        PreparedStatement getPasswordByUserStmt = getPasswordByUserStmts.get(conn);
        if (getPasswordByUserStmt == null) {
            getPasswordByUserStmt = conn.prepareStatement(getPasswordByUserSqlString);
            getPasswordByUserStmts.put(conn, getPasswordByUserStmt);
        }
        getPasswordByUserStmt.setString(1, username.toLowerCase());

        ResultSet rs = getPasswordByUserStmt.executeQuery();
        while (rs.next()) {
            String salt = rs.getString("salt");
            String passwordHash = rs.getString("password");
            if (User.createPasswordHash(password + salt).equals(passwordHash)) {
                loginIsCorrect = true;
            }
        }
        connPool.releaseConnection(conn);
        return loginIsCorrect;
    }

    //Create topic
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
            String commentText, LocalDate createDate) throws Exception {
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

    //Create comment
    private final HashMap<Connection, PreparedStatement> createCommentStmts
            = new HashMap<>();
    private final String createCommentSqlString = "INSERT INTO comment "
            + "(topicid, username, text, editdate) "
            + "VALUES (?, ?, ?, ?)";

    public void createComment(int topicid, String username,
            String text, LocalDate editDate) throws SQLException, Exception {
        Connection conn = connPool.getConnection();
        PreparedStatement createTopicStmt = createCommentStmts.get(conn);
        if (createTopicStmt == null) {
            createTopicStmt = conn.prepareStatement(createCommentSqlString);
            createCommentStmts.put(conn, createTopicStmt);
        }

        createTopicStmt.setInt(1, topicid);
        createTopicStmt.setString(2, username);
        createTopicStmt.setString(3, text);
        createTopicStmt.setDate(4, Date.valueOf(editDate));
        createTopicStmt.executeUpdate();

        connPool.releaseConnection(conn);
    }
    //ConnectionPool
    private DBConnectionPool connPool;

    public boolean isUserLoggedIn(HttpServletRequest request) {
        boolean loggedIn = false;
        Cookie[] cookies = request.getCookies();
        Cookie sessionIDCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionID")) {
                    sessionIDCookie = cookie;
                }
            }
        }
        String sessionID = (String) request.getSession().getAttribute("sessionID");
        if (sessionIDCookie != null && sessionID != null) {
            loggedIn = sessionIDCookie.getValue().equals(sessionID);
        }
        return loggedIn;
    }

    //Create comment
    private final HashMap<Connection, PreparedStatement> getUserpermissionByUserAndPermissionStmts
            = new HashMap<>();
    private final String getUserpermissionByUserAndPermissionSqlString
            = "SELECT permissiondesc "
            + "FROM userpermission "
            + "WHERE username=? AND permissiondesc=?";

    /**
     * Checks if a user has a permission<br>
     * <br>
     * <b>Permissions:</b><br>
     * CREATE_TOPIC, CREATE_COMMENT, CREATE_CATEGORY
     *
     * @param user
     * @param permission
     * @return
     * @throws java.lang.Exception
     */
    public boolean hasUserPermission(User user, String permission) throws Exception {
        Connection conn = connPool.getConnection();
        PreparedStatement getUserpermissionByUserAndPermissionStmt
                = getUserpermissionByUserAndPermissionStmts.get(conn);
        if (getUserpermissionByUserAndPermissionStmt == null) {
            getUserpermissionByUserAndPermissionStmt
                    = conn.prepareStatement(getUserpermissionByUserAndPermissionSqlString);
            getUserpermissionByUserAndPermissionStmts.put(conn, getUserpermissionByUserAndPermissionStmt);
        }

        getUserpermissionByUserAndPermissionStmt.setString(1, user.getUsername());
        getUserpermissionByUserAndPermissionStmt.setString(2, permission);
        ResultSet rs = getUserpermissionByUserAndPermissionStmt.executeQuery();

        connPool.releaseConnection(conn);
        return rs.next();
    }

    private DBAccess() throws ClassNotFoundException {
        connPool = DBConnectionPool.getInstance();
    }

    public static void main(String[] args) {
        try {
            DBAccess dba = DBAccess.getInstance();
            
            
            
            User admin = new User("admin", "", "", "", LocalDate.now());
            User user = new User("user", "", "", "", LocalDate.now());
            System.out.println(dba.hasUserPermission(user, "CREATE_TOPIC"));
            System.out.println(dba.hasUserPermission(user, "CREATE_COMMENT"));
            System.out.println(dba.hasUserPermission(user, "CREATE_CATEGORY"));
            System.out.println(dba.hasUserPermission(admin, "CREATE_CATEGORY"));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
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
