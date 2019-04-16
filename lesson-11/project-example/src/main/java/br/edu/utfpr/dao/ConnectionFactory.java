package br.edu.utfpr.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author akira
 */
public class ConnectionFactory {
    private static final String URL = "jdbc:derby:memory:database;create=true";
    
    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException("Erro na Conexão: ", ex);
        }
    }
    
    
    public static void closeConnection(Connection con){
        try {
            if(con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            System.err.println("Erro : "+ ex);
        }
    }
    
    
    public static void closeConnection(Connection con, PreparedStatement stmt){
        try {
            if(stmt != null) {
                stmt.close();
            }
        } catch (SQLException ex) {
            System.err.println("Erro : "+ ex);
        }
        closeConnection(con);

    }
    
    
    public static void closeConnection(Connection con, PreparedStatement stmt, ResultSet rs){
        try {
            if(rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            System.err.println("Erro : "+ ex);
        }
        closeConnection(con, stmt);
    }
    
}