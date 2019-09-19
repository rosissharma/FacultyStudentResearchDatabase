import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import java.util.Arrays;
import java.util.List;
import java.nio.charset.*;

public class MainTestFunction {

    //Holds the user type of the logged in user
    private String userType;

    //Logic tracking variables
    int actionCode = 0;
    int subActionCode = 0;

    //Reads in user input
    Scanner scan = new Scanner(System.in);

    //Buisiness layer
    ResearchInfo ri = new ResearchInfo();

    /**
     * Constructor, sets default action code
     */
    MainTestFunction() {
        actionCode = 0;
    }

    /**
     * Prints out people to the user
     *
     * @param arrayTable 2D array of users
     */
    public void printPersons(ArrayList<ArrayList<String>> arrayTable) {
        //Check if array is empty
        if (arrayTable == null) {
            System.out.println("No People Found");
        } else {
            for (int i = 1; i <= arrayTable.size(); i++) {
                printPerson(arrayTable.get(i - 1));
            }
        }
    }

    /**
     * Prints out the informations of a person
     *
     * @param arrayTable Array of an user
     */
    public void printPerson(ArrayList<String> arrayTable) {
        //Check if array is empty
        if (arrayTable.isEmpty()) {
            System.out.println("The ArrayList is Empty!");
        } else {
            System.out.println("Name:                " + arrayTable.get(1));
            System.out.println("Phone Number:        " + arrayTable.get(2));
            System.out.println("Email:               " + arrayTable.get(3));
            System.out.println("Website:             " + arrayTable.get(4));
            System.out.println("Research Interests:  " + arrayTable.get(5));
            if (arrayTable.get(0).equals("Professor")) {
                System.out.println("College:             " + arrayTable.get(6));
            } else if (arrayTable.get(0).equals("Student")) {
                System.out.println("Major:               " + arrayTable.get(6));
                System.out.println("Minor:               " + arrayTable.get(7));
            }
            System.out.println();//Line break
        }
    }

    /**
     * Prints out the projects from an array
     *
     * @param arrayTable Array of projects
     */
    public void printProjects(ArrayList<ArrayList<String>> arrayTable) {
        if (arrayTable == null) {
            System.out.println("No Projects Found");
        } else {
            for (int i = 1; i <= arrayTable.size(); i++) {
                System.out.println("\nProject ID:            " + arrayTable.get(i - 1).get(0));
                System.out.println("Project Title:         " + arrayTable.get(i - 1).get(1));
                System.out.println("Project Description:   " + arrayTable.get(i - 1).get(2));
            }
        }
    }

    /**
     * List of options for the user to choose
     */
    public void logicHandler() {
        System.out.println("\n");
        System.out.println("1. Show all users");
        System.out.println("2. Search for a user");
        System.out.println("3. Update your profile");
        System.out.println("4. List your projects");
        System.out.println("5. List all projects");
        System.out.println("6. Add youself to a project");
        System.out.println("7. Remove yourself from a project");
        System.out.println("8. Search for a project");
        System.out.println("9. Exit");
        try {
            switch (scan.nextInt()) {
                case 1:
                    printPersons(ri.listAll());
                    break;
                case 2:
                    System.out.println("What would you like to search for:");
                    scan.nextLine();
                    printPersons(ri.searchPeople(scan.nextLine()));
                    break;
                case 3:
                    System.out.println("Your current profile information");
                    printPerson(ri.getPersonalInfo());
                    scan.nextLine();
                    subActionCode = 0;
                    while (subActionCode == 0) {
                        subLogicHandler();
                    }
                    break;
                case 4:
                    printProjects(ri.listMyProjects());
                    break;
                case 5:
                    printProjects(ri.listAllProjects());
                    break;
                case 6:
                    ArrayList<ArrayList<String>> list = ri.listNotMyProjects();
                    if (list.size() != 0) {
                        printProjects(list);
                        System.out.println("\nEnter Project ID of project you'd like to be added to:");
                        scan.nextLine();
                        try {
                            ri.addToProject(scan.nextInt());
                        } catch (Exception e) {
                            System.out.println("Invalid project");
                            scan.nextLine();
                        }
                    } else {
                        System.out.println("No projects found");
                    }
                    break;
                case 7:
                    ArrayList<ArrayList<String>> list2 = ri.listMyProjects();
                    if (list2.size() != 0) {
                        printProjects(list2);
                        System.out.println("\nEnter Project ID of project you'd like to be removed from:");
                        scan.nextLine();
                        try {
                            ri.removeFromProject(scan.nextInt());
                        } catch (Exception e) {
                            System.out.println("Invalid project");
                        }
                    } else {
                        System.out.println("No projects found");
                    }
                    break;
                case 8:
                    System.out.println("What would you like to search for:");
                    scan.nextLine();
                    printProjects(ri.searchProjects(scan.nextLine()));
                    break;
                case 9:
                    System.out.println("Goodbye");
                    actionCode = 1;
                    break;
                default:
                    System.out.println("Invalid Input");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Must enter an integer");
            scan.nextLine();//Clear Scanner
        }
    }

    /**
     * Method that allows the user to update profile
     */
    public void subLogicHandler() {
        System.out.println("\nSelect which attribute you would like to change");
        System.out.println("1. Email");
        System.out.println("2. Website");
        System.out.println("3. Phone Number");
        System.out.println("4. Research Interests");
        if (userType.equals("Student")) {
            System.out.println("5. Major");
            System.out.println("6. Minor");
        } else if (userType.equals("Professor")) {
            System.out.println("7. College");
        }
        System.out.println("8. Save");
        System.out.println("9. Cancel");
        try {
            switch (scan.nextInt()) {
                case 1:
                    System.out.println("Enter your new email:");
                    scan.nextLine();
                    ri.setEmail(scan.nextLine());
                    break;
                case 2:
                    System.out.println("Enter your new website:");
                    scan.nextLine();
                    ri.setWebsite(scan.nextLine());
                    break;
                case 3:
                    System.out.println("Enter your new phone number:");
                    scan.nextLine();
                    ri.setPhoneNumber(scan.nextLine());
                    break;
                case 4:
                    System.out.println("Enter your new Research Interests:");
                    scan.nextLine();
                    ri.setResearchInterests(scan.nextLine());
                    break;
                case 5:
                    System.out.println("Enter your new Major");
                    scan.nextLine();
                    ri.setMajor(scan.nextLine());
                    break;
                case 6:
                    System.out.println("Enter your new Minor");
                    scan.nextLine();
                    ri.setMinor(scan.nextLine());
                    break;
                case 7:
                    System.out.println("Enter your new college");
                    scan.nextLine();
                    ri.setCollege(scan.nextLine());
                    break;
                case 8:
                    ri.put();
                    subActionCode = 1;
                    System.out.println("Saved");
                    break;
                case 9:
                    ri.fetch();
                    subActionCode = 1;
                    System.out.println("Exiting without saving");
                    break;
                default:
                    System.out.println("Invalid Input");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Must enter an integer");
            scan.nextLine();
        }
    }

    /**
     * Method to hash the passsword
     *
     * @param passwordToHash The password that will be hashed
     * @return hashedPassword The password that has been hashed
     */
    public String hashPassword(String passwordToHash) {
        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    /**
     * Login page that prompts the user for username and password
     */
    public void login() {
        Scanner reader = new Scanner(System.in);
        while (true) {
            System.out.println("Username:");
            String username = reader.nextLine();
            System.out.println("Password:");
            String password = reader.nextLine();
            String temp = ri.login(username, hashPassword(password));
            if (temp != null) {
                userType = temp;
                System.out.println("Welcome to the Professor and Student research network");
                break;
            } else {
                System.out.println("Invalid username or password");
            }
        }
    }

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {

        MainTestFunction mtf = new MainTestFunction();

        System.out.println("Welcome to CMD Test Program Nightly Build");

        //Ask for user to login
        mtf.login();

        //Loop main tests until user chooses to exit
        while (mtf.actionCode == 0) {
            mtf.logicHandler();
        }
    }
}
