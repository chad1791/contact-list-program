///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
// */
//package contactlist;
//
///**
// *
// * @author edgar
// */
//public class ContactList {
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        // TODO code application logic here
//    }
//    
//}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package contactlist;
import java.util.Properties;
import java.util.Scanner;

/**Name:Edgar A. Chan
 * Date: 12/1/2023
 * Description:The Main Program that allow the user enter, modify and view information in the database contactbook.
 *
 * @author edgar
 */
public class ContactList {
    public static void main(String[] args) {
        
        // Database connection parameters
        String url_conn = "jdbc:mysql://localhost:3306/contactbook";
        String user = "root";
        String password = "";
        
        // Creating an instance of Contacts_Database_Manager for database operations
        Contacts_Database_Manager cdm = new Contacts_Database_Manager(url_conn, user, password);
        
        // Testing database connection
        boolean connection_successful = cdm.testDbConnection();
        if(connection_successful){
            System.out.println("Successfully connected to the database!");
        }
        else {
          System.out.println("Unable to connect to the database!");
        }
        
        // Setting up a scanner for user input  
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("***********************************************************");
            System.out.println("What would you like to do?");
            System.out.println("***********************************************************");
            System.out.println("1. Add a new contact");
            System.out.println("2. Look up a person's information");
            System.out.println("3. Change a person's information");
            System.out.println("4. Delete a person's information");
            System.out.println("5. Quit");
            System.out.println("***********************************************************");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            System.out.println("***********************************************************");
            scanner.nextLine();
            switch (choice) {
                case 1:
                    //Allows the user to add row with their information
                    System.out.print("Enter first name: ");
                    String fn = scanner.nextLine();
                    System.out.print("Enter last name: ");
                    String ln = scanner.nextLine();
                    System.out.print("Enter nick name: ");
                    String nm = scanner.nextLine();
                    System.out.print("Enter phone number: ");
                    String cell = scanner.nextLine();
                    System.out.print("Enter email address: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter company name: ");
                    String company = scanner.nextLine();
                    System.out.print("Enter last date of birth: ");
                    String dob = scanner.nextLine();
                    
                    cdm.addContact(fn, ln, nm, cell, email, company, dob);
                break;
                case 2:
                    //Allows the user to look up information in the database
                    System.out.print("Enter phone number: ");
                    String searchTerm = scanner.nextLine();
                    
                    Properties lookup = new Properties();
                    lookup = cdm.lookupContact(searchTerm);
                 
                    if(!lookup.isEmpty()){
                        System.out.println("Contact found! See details below: ");
                        lookup.forEach((key, value)-> {
                            System.out.println(key + ": " + value);
                        });  
                    }
                    else {
                        System.out.println("Contact could not be found!");
                    }                    
                break;
                case 3:
                    //Allows the user to edit  information in the database
                    String cont_update = "yes";
                    Properties updates = new Properties();
                                        
                    System.out.println("Enter Phone number of the contact you want to update:");
                    String phonenumber = scanner.nextLine();
                    
                    System.out.println("You can update the following contact details: \nfirstname \nlastname \nnickname \nphonenumber \nemail \ncompany \nbirthdate");
                        
                    while(cont_update.equals("yes")){
                        System.out.print("Enter the update property you want to update: ");
                        String updatePorpery = scanner.nextLine();
                        System.out.print("Enter the new value: ");
                        String updateValue = scanner.nextLine();
                        
                        updates.setProperty(updatePorpery, updateValue);
                        
                        System.out.println("Press [yes] to add another update property or [no] to update the already added properties.");
                        cont_update = scanner.nextLine();
                    }               
                     
                    cdm.updateContact(phonenumber, updates);
                    
                break;
                case 4:
                    //Allows the user to delete information in the database indicated by 1
                    System.out.print("Enter Phone number of the contact you want to delete: ");
                    String phone = scanner.nextLine();
                    
                    boolean deleted = cdm.deleteContact(phone);
                    if(deleted){
                        System.out.println("Contact was successfully deleted!");
                    }
                    else {
                        System.out.println("Contact was could not be deleted!");
                    }
                break;
                case 5:
                    //Ends the program
                    System.out.println("Goodbye!");
                return;
                default:
                    //Handles invalid choices
                    System.out.println("Invalid choice.");
                break;
            }
        }             
    }    
}
