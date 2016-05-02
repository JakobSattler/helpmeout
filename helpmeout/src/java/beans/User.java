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
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Random;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 *
 * @author Jakob
 */
public class User {

    private String username;
    private String email;
    private String password;
    private String salt;
    private LocalDate registerDate;

    public static void main(String[] args) throws NoSuchAlgorithmException, Exception {
        DBAccess dba = DBAccess.getInstance();
        System.out.println(dba.isLoginCorrect("jakob", "blabla"));
        System.out.println(dba.isLoginCorrect("jfkalsdö", "blabla"));
        System.out.println(dba.isLoginCorrect("jakob", "jklö"));
    }

    /**
     * Returns the hash value of the password argument
     * 
     * @param password password of the new user in plain text
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public static String createPasswordHash(String password) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : result) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
    
    

    public User() {

    }

    public User(String username, String email, String password,
            String passwordSalt, LocalDate registerDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.registerDate = registerDate;
        this.salt = passwordSalt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", password=" + password + ", salt=" + salt + ", registerDate=" + registerDate + '}';
    }

}
