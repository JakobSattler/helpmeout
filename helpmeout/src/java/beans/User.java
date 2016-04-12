/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import database.DBAccess;
import database.DBAccess.UserAlreadyExistsException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

/**
 *
 * @author Jakob
 */
public class User {
    String username;
    String passwordHash;
    LocalDate registerDate;
    
    public static void main(String[] args) throws NoSuchAlgorithmException, Exception {
        User.create("jakob", "01081997");
    }
    /**
     * Creates a new user and saves it to the database
     * 
     * @param username username of the new user
     * @param password password of the new user in plain text
     * @return 
     * @throws NoSuchAlgorithmException
     * @throws database.DBAccess.UserAlreadyExistsException
     * @throws Exception 
     */
    public static User create(String username, String password) throws NoSuchAlgorithmException, UserAlreadyExistsException, Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
        }
        String resultStr = sb.toString();
        
        User user = new User(username, resultStr, LocalDate.now());
        DBAccess dba = DBAccess.getInstance();
        dba.createUser(user);
        return user;
    }
    
    public User(){
        
    }
    
    public User(String username, String password, LocalDate registerDate){
        this.username = username;
        this.passwordHash = password;
        this.registerDate = registerDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return passwordHash;
    }

    public void setPassword(String password) {
        this.passwordHash = password;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

    
    
    
}
