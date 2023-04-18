import javax.swing.*;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
import java.util.Scanner;
//GUI is set to pink for pattern recognition
public class marketClient {
    public static void main(String[] args) throws IOException{
        //declaring variables
        boolean isCustomer = false; //false if a seller, true if a customer
        boolean mainMenu = false;
        String placeholderGUI = ""; //placeholder for where I need GUI input
        Scanner scan = new Scanner(System.in);

        try {
            Socket socket = new Socket("localhost", 4242);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            //PUT IN GUI FOR LOGIN/WHATEVER
            //send either "EXISTING USER" or "NEW USER"
            boolean existingUser = false;
            //here, use GUI to figure out if it's an existing user or they want to create an account
            boolean foundUser = false;
            if (existingUser) { //user is logging in
                writer.println("EXISTING USER");
                writer.flush();
                do {
                    String email = "email";       //placeholder
                    String password = "password"; //placeholder
                    //GUI for entering email
                    //GUI for entering password
                    writer.println(email);
                    writer.flush();
                    writer.println(password);
                    writer.flush();
                    String checkCust = reader.readLine();
                    if (checkCust.equals("CUSTOMER")) {
                        isCustomer = true;
                        foundUser = true;
                    } else if (checkCust.equals("SELLER")) {
                        isCustomer = false;
                        foundUser = true;
                    } else if (checkCust.equals("NOUSER")) {
                        foundUser = false;
                        //GUI for re-entering the username and password
                        System.out.println("No user found! Would you like to try again?");
                        if (placeholderGUI.equalsIgnoreCase("Don't re-enter password")) {
                            writer.println("NO");
                            writer.flush();
                        }
                    }
                } while (!foundUser);

            } else {
                writer.write("NEW USER");
                writer.flush();
                //if it's a customer, print 1, if not, print 2
                System.out.println("would you like to join as a customer (1) or seller (2)");
                placeholderGUI = scan.nextLine();
                if (Integer.parseInt(placeholderGUI) == 1) {
                    writer.println("1");
                    writer.flush();
                    isCustomer = true;
                } else {
                    writer.println("2");
                    writer.flush();
                    isCustomer = false;
                }
                System.out.println("Enter a name"); //use GUI for this all instead
                placeholderGUI = scan.nextLine();
                writer.println(placeholderGUI);
                writer.flush();
                System.out.println("Enter an email");
                placeholderGUI = scan.nextLine();
                writer.println(placeholderGUI);
                writer.flush();
                System.out.println("Enter a password");
                placeholderGUI = scan.nextLine();
                writer.println(placeholderGUI);
                writer.flush();
                boolean emailExists;
                if (reader.readLine().equals("EMAIL SUCCESS")) { //TODO make this section work better
                    emailExists = false;
                } else { //the user needs to enter a new email
                    emailExists = true;
                    do {
                        System.out.println("Enter a new email!");
                        placeholderGUI = scan.nextLine();
                        writer.write(placeholderGUI);
                        writer.flush();

                        if (placeholderGUI.equals("NO")) { //this is if the user cancels or chooses not to try again
                            writer.println("NO");
                            writer.flush();
                            return;
                        }
                    } while (emailExists);
                }



            }
            //enter mail menu

            //CUSTOMER


            if (isCustomer) {
                mainMenu = true;
                /**
                 * 1. view / edit your acc
                 * 2. view farmer's market <<holy shit
                 * 3. quit
                 * **/
                do {
                    //display GUI for main menu: gets info for what they want to do in the main menu
                    int choice = scan.nextInt(); //choice is recieved from GUI
                    scan.nextLine();
                    writer.println(choice); //sends choice to server so they know which one
                    switch (choice) {
                        case 1: //view / edit account
                            boolean editAcc = true;
                            do {
                                String name = reader.readLine();
                                String email = reader.readLine();
                                String password = reader.readLine();
                                //GUI
                                //  get info on if the person is changing 1. name, 2. email, or 3. password
                                //  send 1, 2, or 3 to server


                            } while (editAcc);
                    }

                } while (mainMenu);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

/*
