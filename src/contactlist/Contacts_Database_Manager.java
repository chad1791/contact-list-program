/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contactlist;
import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLDataException;
import java.util.Properties;

/**Name:Edgar A. Chan
 * Date: 12/1/2023
 * Description: Handles the executed queries in Contacts list
 *
 * @author edgar
 */
public class Contacts_Database_Manager {
    
    // Database connection parameters
    String url;
    String user;
    String password;
    Connection conn;
    
    String mainQuery = "UPDATE contacts SET ";
    int updates_length = 1;
    
    public Contacts_Database_Manager(String url_connection, String user, String password){
        this.url = url_connection;
        this.user = user;
        this.password = password;
    }
    
    // Method to test the database connection
    public boolean testDbConnection(){
        try{
            conn = DriverManager.getConnection(this.url, this.user, this.password);
            
            if(conn != null)
            {                
                //System.out.println("The connection to the database was successful");                          // Connection successful, was turn into comment to not be redundant
                return true;
            }
            else 
            {
                //System.out.println("We could not establish a connection to the database at this time.");      // Unable to establish a connection, was turn into comment to not be redundant
                return false;
            }          
        }
        catch(SQLException ex){
            // Exception handling for connection failure
            System.out.println("The following error occurred while trying to connect to the database: ");
            System.out.println(ex.toString());
            return false;
        }
    }
    
    // Method to add a new contact to the database
    public void addContact(String first, String last, String nick, String phone, String email, String company, String dob){
        
        try {
            int deleted = 0;
            
            // Prepare SQL statement for inserting a new contact        
            PreparedStatement statement = conn.prepareStatement("INSERT INTO contacts (firstname, lastname, nickname, phonenumber, email, company, birthdate,deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, first);
            statement.setString(2, last);
            statement.setString(3, nick);
            statement.setString(4, phone);
            statement.setString(5, email);
            statement.setString(6, company);
            statement.setString(7, dob);
            statement.setInt(8, deleted);
            
            // Execute the insert statement
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) 
            {
                System.out.println("Contact added successfully.");
            } else 
            {
                System.out.println("Error adding contact.");
            }
        }
        catch(SQLException ex){
            // Exception handling for SQL errors
            System.out.println("The following error occurred while trying to connect to the database: ");
            System.out.println(ex.toString());
        }
    }
    
    // Method to delete a contact from the database, please note in the database in the delete 1 appears showing the roow has been deleted
    public boolean deleteContact(String phonenumber){
        try {
            
            // Prepare SQL statement for updating the 'deleted' flag for a contact
            String query = "UPDATE contacts SET deleted=? WHERE phonenumber=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, "1");
            statement.setString(2, phonenumber);
            
            // Execute the update statement
            if(statement.executeUpdate() == 1)
            {
                return true;
            }
            else 
            {
                return false;
            }            
        }
        catch (SQLException ex){
            // Exception handling for SQL errors
            System.out.println("The following error occurred while trying to connect to the database: ");
            System.out.println(ex.toString());
            return false;
        }
    }
    
    // Method to update a contact in the database
    public void updateContact(String phonenumber, Properties updates){
        
        try {        
            updates_length = 1;

            // Construct the dynamic part of the SQL update query
            updates.forEach((key, value)-> {                
                mainQuery += key.toString() + "=?";
                updates_length++;

                if(updates_length <= updates.size())
                {
                    mainQuery += ",";
                }
            }
            );
            
            updates_length = 1;
            
            // Complete the SQL update query
            mainQuery += " WHERE phonenumber=?"; 
            PreparedStatement statement = conn.prepareStatement(mainQuery);            
            updates.forEach((key, value)->{
                try {
                    statement.setString(updates_length, value.toString()); 
                    updates_length++;
                } 
                catch(SQLException ex){
                    System.out.println("The following error occurred while trying to connect to the database: ");
                    System.out.println(ex.toString());
                }                
            }
            );
            
            // Execute the update statement            
            statement.setString(updates.size()+1, phonenumber);
            if(statement.executeUpdate()==1){
                System.out.println("Contact with phone number ["+phonenumber+"] was successfully updated!");
            }   
            else {
                System.out.println("Unable to update contact!");
            }
        }
        catch(SQLException ex){
            // Exception handling for SQL errors
            System.out.println("The following error occurred while trying to connect to the database: ");
            System.out.println(ex.toString());
        }
    }
    
    // Method to lookup a contact in the database
    public Properties lookupContact(String phone){
        
        Properties lookup = new Properties();
        
        try {
            // Prepare SQL statement for selecting a contact by phone number
            String query = "SELECT * FROM contacts WHERE phonenumber LIKE ? AND deleted = '0'";         //Deleted was added to not displayed any deleted rows indicated by 1
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, phone);
            
            ResultSet result = statement.executeQuery();
            while (result.next()) {                
                lookup.setProperty("firstname", result.getString("firstName"));
                lookup.setProperty("lastname", result.getString("lastname"));
                lookup.setProperty("phonenumber", result.getString("phonenumber"));
                lookup.setProperty("email", result.getString("email"));
                lookup.setProperty("company", result.getString("company"));
                System.out.println();
            }
            
            return lookup;
        }
        catch(SQLException ex){
            // Exception handling for SQL errors
            System.out.println("The following error occurred while trying to connect to the database: ");
            System.out.println(ex.toString());
            return lookup;
        }
    }
}
