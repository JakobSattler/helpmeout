/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.time.LocalDate;

/**
 *
 * @author Jakob
 */
public class User {
    String username;
    String password;
    LocalDate registerDate;
    
    public User(){
        
    }
    
    public User(String username, String password, LocalDate registerdate){
        this.username = username;
        this.password = password;
        this.registerDate = registerdate;
    }

    public String getBenutzername() {
        return username;
    }

    public void setBenutzername(String username) {
        this.username = username;
    }

    public String getPasswort() {
        return password;
    }

    public void setPasswort(String password) {
        this.password = password;
    }

    public LocalDate getErstelldatum() {
        return registerDate;
    }

    public void setErstelldatum(LocalDate registerdate) {
        this.registerDate = registerdate;
    }
    
    
}
