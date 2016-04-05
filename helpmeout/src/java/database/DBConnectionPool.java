/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;

/**
 *
 * @author Jakob
 */
public class DBConnectionPool implements DBConfig{

    private LinkedList<Connection> connections = new LinkedList<>();
    private final int MAX_CONNECTIONS = 150;
    private int num_connections = 0;

    private static DBConnectionPool theInstance = null;
    
    public static DBConnectionPool getInstance() throws ClassNotFoundException{
        if(theInstance == null){
            theInstance = new DBConnectionPool();
        }
        return theInstance;
    }
    
    public DBConnectionPool() throws ClassNotFoundException {
        Class.forName(DB_DRIVER);
    }
    
    public Connection getConnection() throws Exception{
        if(connections.isEmpty()){
            if(num_connections >= MAX_CONNECTIONS){
                throw new Exception("Maximum number of users reached - try again later");
            }
            Connection connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWD);
            num_connections++;
            return connection;
        }
        return connections.poll();
    }
    
    public void releaseConnection(Connection connection){
        connections.offer(connection);
    }
}
