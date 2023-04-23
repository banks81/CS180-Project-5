import javax.swing.*;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//GUI is set to pink for pattern recognition
public class marketClient {
    public static void main(String[] args) throws IOException{
        //declaring variables
        boolean isCustomer = false; //false if a seller, true if a customer

        String placeholderGUI = ""; //placeholder for where I need GUI input
        Scanner scan = new Scanner(System.in);

        try {
            Socket socket = new Socket("localhost", 4242);
            System.out.println("got past socket");
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("reader");
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            System.out.println("writer");
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("reached end of socket stuff");
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("ois");


            //PUT IN GUI FOR LOGIN/WHATEVER
            //send either "EXISTING USER" or "NEW USER"
            boolean existingUser = false;
            //here, use GUI to figure out if it's an existing user or they want to create an account
            System.out.println("are you an existing user?");
            if (scan.nextLine().equalsIgnoreCase("yes")) {
                existingUser = true;
            } else {
                existingUser = false;
            }

            boolean userExists = false;
            if (existingUser) { //user is logging in
                writer.println("EXISTING USER");
                writer.flush();
                do {
                    System.out.println("enter your email");
                    String email = scan.nextLine();       //placeholder
                    System.out.println("enter your password");
                    String password = scan.nextLine(); //placeholder
                    //GUI for entering email
                    //GUI for entering password
                    writer.println(email);
                    writer.flush();
                    System.out.printf("printed %s to server\n", email);
                    writer.println(password);
                    writer.flush();
                    System.out.printf("printed %s to server\n", password);

                    String checkCust = reader.readLine();

                    if (checkCust.equals("CUSTOMER")) {
                        isCustomer = true;
                        userExists = true;
                    } else if (checkCust.equals("SELLER")) {
                        isCustomer = false;
                        userExists = true;
                    } else if (checkCust.equals("NO USER")) {
                        userExists = false;
                        //GUI for re-entering the username and password
                        System.out.println("No user found! Would you like to try again?");
                        placeholderGUI = scan.nextLine();
                        if (placeholderGUI.equalsIgnoreCase("no")) { //no user was found
                            writer.println("NO");
                            writer.flush();
                            System.out.println("printed NO to server");
                            //close all
                            return;
                        } else {
                            writer.println("YES");
                            writer.flush();
                            System.out.println("printed YES to server");
                        }
                    }
                } while (!userExists);

            } else {
                writer.println("NEW USER"); //this writes to line 302 of the server
                writer.flush();
                System.out.println("new user");
                //if it's a customer, print 1, if not, print 2
                System.out.println("would you like to join as a customer (1) or seller (2)");
                int type = scan.nextInt();
                scan.nextLine();
                if (type == 1) {
                    writer.println("1");
                    writer.flush();
                    isCustomer = true;
                } else {
                    writer.println("2");
                    writer.flush();
                    isCustomer = false;
                }
                System.out.println("Enter a name"); //use GUI for this all instead
                String name = scan.nextLine();
                writer.println(name);
                writer.flush();
                System.out.println("Enter an email");
                String email = scan.nextLine();
                writer.println(email);
                writer.flush();

                boolean emailExists = false;
                do {
                    String successYesNo = reader.readLine(); //if "ERROR" the email exists
                    if (successYesNo.equals("ERROR")) { //the user needs to enter a new email
                        emailExists = true;

                        System.out.println("Enter a new email!");
                        email = scan.nextLine(); //GUI SET EMAIL TO
                        //  "DO NOT RE-ENTER EMAIL" if user says not to enter email
                        writer.println(email);
                        writer.flush();

                        if (email.equals("DO NOT RE-ENTER EMAIL")) { //this is if the user cancels or chooses not to try again
                            oos.close();
                            ois.close();
                            reader.close();
                            writer.close();
                            return;
                        }
                    } else {
                        emailExists = false;
                    }
                } while (emailExists);
                System.out.println("Enter a password");
                String password = scan.nextLine();
                writer.println(password);
                writer.flush();



            }
            System.out.println("Login success!");

            //CUSTOMER
            if (isCustomer) {
                boolean mainMenu = false;
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
                    writer.flush();
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
                                //  send 4 to server if removing/deleting account
                                //  send 5 to exit this page
                                int decision = scan.nextInt(); //whatever the GUI puts out
                                scan.nextLine();
                                writer.println(decision);
                                writer.flush();
                                switch (decision) {
                                    case 1:
                                        String newName = scan.nextLine();
                                        writer.println(newName);
                                        writer.flush();
                                        break;
                                    case 2:
                                        do {
                                            email = scan.nextLine();
                                            writer.println(email);
                                            writer.flush();
                                            if (reader.readLine().equals("ERROR")) { //this means the email does exist
                                                //use GUI for this instead of print statements
                                                System.out.println("A user with that email already exists! Would you like to try again?");
                                                placeholderGUI = scan.nextLine();
                                                if (placeholderGUI.equals("no")) {
                                                    writer.println("NO");
                                                    writer.flush();
                                                    break;
                                                } else {
                                                    writer.println("CONTINUE");
                                                    writer.flush();
                                                }
                                            } else { //this is if it's a success and the email was changed
                                                System.out.println("Email was successfully changed!"); //GUI
                                                break;
                                            }
                                        } while (true);
                                        break;
                                    case 3:
                                        //use GUI for this input
                                        System.out.println("What would you like to change your password to?");
                                        String changedPassword = scan.nextLine(); //whatever the user wants to change it to
                                        writer.println(changedPassword);
                                        writer.flush();
                                        break;
                                    case 4:
                                        //I don't think anything is needed here as is but
                                        //should we have a way for the person to confirm before they delete their account?
                                        //I'm not sure how to do that without messing up the flow of the market/client
                                        break;
                                    case 5:
                                        editAcc = false;
                                }


                            } while (editAcc);
                            break;
                        case 2: //view farmer's market
                            /**
                             * 1. view overall farmer's market listings
                             * 2. search for specific products
                             * 3. sort the products by price, lowest to highest
                             * 4. sort the products by quantity available, lowest to hightest
                             * 5. view your purchase history
                             * 6. view your shopping cart
                             * 7. go back to main menu
                             * 8. quit
                             * **/
                            boolean marketPlace = true;
                            do {
                                //Use GUI to tell what the user wants to do
                                System.out.println("Refer to: case 2: //view farmer's market");
                                int decision = scan.nextInt();
                                scan.nextLine();
                                writer.println(decision);
                                writer.flush();
                                switch (decision) {
                                    case 1:
                                        ArrayList<Products> productsList = new ArrayList();
                                        do {
                                            try {
                                                Products newProduct = (Products) ois.readObject();
                                                productsList.add(newProduct);
                                                System.out.println(newProduct.getName() + " received");
                                            } catch (Exception e) {
                                                break;
                                            }
                                        } while (true);
                                        //GUI
                                        //  show the list of products and make a selection
                                        if (!productsList.isEmpty()) {
                                            System.out.println("which product would you like to look at?");
                                            int marketChoice = scan.nextInt();
                                            scan.nextLine();
                                            writer.println(marketChoice);
                                            if (marketChoice <= productsList.size()) {
                                                Products productOfChoice = productsList.get(marketChoice - 1);
                                                /**
                                                 * 1. add one to cart
                                                 * 2. purchase now
                                                 * 3. go back to products list
                                                 * **/
                                                System.out.println("1. add one to cart//2. purchase now//3. go back to products list");
                                                placeholderGUI = scan.nextLine(); //you may need to create another variable, I'm not sure
                                                writer.println(placeholderGUI);  //for now I'm just using placeholderGUI for the choice
                                                writer.flush();
                                                switch (Integer.parseInt(placeholderGUI)) {
                                                    case 1: //add to shopping cart
                                                        break; //the server doesn't have it reading or writing anything
                                                    case 2: //purchase now
                                                        //GUI
                                                        //  asks 'how many would you like to purchase?'
                                                        //  have an option to buy 0 or cancel
                                                        //  if cancel, send 0 to server
                                                        System.out.println("How many would you like to purchase?");
                                                        int quantity = scan.nextInt();
                                                        scan.nextLine();
                                                        writer.println(quantity);
                                                        writer.flush();
                                                        String boughtConfirmation = reader.readLine();
                                                        if (boughtConfirmation.equals("DID NOT PURCHASE")) { //this means they entered 0 or cancelled
                                                            //GUI
                                                            //  pop-up that says something along the lines of "ok! your order was cancelled"
                                                            System.out.println("your order was cancelled!");
                                                        } else if (boughtConfirmation.equals("SUCCESS")) { //this means product was bought
                                                            //GUI
                                                            //  some message about your product was bought successfully
                                                        } else if (boughtConfirmation.equals("ERROR")) { //this means they tried to buy
                                                            //more than allowed amount
                                                            //GUI
                                                            //  some message like "there aren't that many of this product available!"
                                                        }
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            } else if (marketChoice == productsList.size() + 2) {
                                                return;
                                            }
                                        } else {
                                            System.out.println("No products available!");
                                        }
                                        break;
                                    case 2: //searchProducts
                                        do {
                                            //GUI
                                            //  asks for a keyword they want to scan for
                                            ArrayList<String> searchResults = new ArrayList<>();
                                            System.out.println("what would you like to search for?");
                                            String keyword = scan.nextLine();
                                            writer.println(keyword);
                                            writer.flush();
                                            String firstMessage = reader.readLine();
                                            if (!firstMessage.equals("NO SEARCH RESULTS")) {
                                                searchResults.add(firstMessage);
                                                firstMessage = reader.readLine();
                                                while (!firstMessage.equals("END")) {
                                                    searchResults.add(firstMessage);
                                                    firstMessage = reader.readLine();
                                                }
                                                System.out.println(searchResults.toString());
                                                //GUI
                                                //  shows all search results names in numbered order
                                                //  gets the reader to pick one
                                                System.out.println("Which product would you like to view?");
                                                int searchIndex = scan.nextInt();
                                                scan.nextLine();
                                                writer.println(searchIndex);
                                                writer.flush();
                                                do {
                                                    if (searchIndex <= searchResults.size()) {
                                                        Products product = (Products) ois.readObject();
                                                        //GUI
                                                        //  display the product and ask what to do about it
                                                        /**
                                                         * 1. add to shopping cart
                                                         * 2. buy now
                                                         * 3. go back
                                                         */
                                                        System.out.println("Would you like to \n" +
                                                                "1. add to shopping cart\n" +
                                                                "2. purchase product\n" +
                                                                "3. go back");
                                                        placeholderGUI = String.valueOf(scan.nextInt());
                                                        scan.nextLine();
                                                        writer.println(placeholderGUI);
                                                        writer.flush();
                                                        switch (Integer.parseInt(placeholderGUI)) {
                                                            case 1: //add to shopping cart
                                                                //adds to shopping cart
                                                                //GUI success message
                                                                break;
                                                            case 2:
                                                                do {
                                                                    //GUI
                                                                    //  asks how many they want to purchase
                                                                    System.out.println("how many would you like to purchase?");
                                                                    int quantity = scan.nextInt();
                                                                    scan.nextLine();
                                                                    writer.println(quantity);
                                                                    writer.flush();
                                                                    if (reader.readLine().equals("SUCCESS")) {
                                                                        //GUI
                                                                        //the object has been successfully purchased
                                                                        break;
                                                                    } else {
                                                                        System.out.println("Not enough product in stock! Please try again");
                                                                        //use GUI for that
                                                                    }
                                                                } while (true);
                                                            default:
                                                                break;
                                                        }
                                                        break;
                                                    } else { //this handles if the searchindex is greater than the
                                                        //number of products
                                                        //TODO idk how to handle the loops here
                                                    }

                                                } while (true);
                                            } else { //this means there are no search results
                                                //GUI for no search results
                                            }
                                            //now ask if they want to search again
                                            // TODO ask minyhoung why the server
                                            //  has it pass as "yes" if they want to exit the loop
                                            //  right now it's coded backwards
                                            //GUI for would you like to search again?
                                            System.out.println("search again? 1. yes 2. no");
                                            int choice2 = scan.nextInt();
                                            scan.nextLine();
                                            if (choice2 == 1) {
                                                writer.println("YES");
                                                writer.flush();
                                            } else {
                                                writer.println("NO");
                                                writer.flush();
                                                break;
                                            }
                                        } while (true);
                                    case 3: //sort by price lowest to highest
                                        //no info is passed from server to client or vice versa
                                        //GUI
                                        //  says it's been sorted from lowest to highest
                                        break;
                                    case 4: //sort by quantity available, lowest to highest
                                        //no info is passed from server to client or vice versa
                                        //GUI
                                        //  says it's been
                                        break;
                                    case 5: //view your purchase history
                                        ArrayList<String> purchaseHistory = new ArrayList<>();
                                        String pastPurchase = reader.readLine();
                                        while (!pastPurchase.equals("END")) {
                                            purchaseHistory.add(pastPurchase);
                                            pastPurchase = reader.readLine();
                                        }
                                        //GUI
                                        //  show purchase history in a pop-up
                                        if (!purchaseHistory.isEmpty()) {
                                            System.out.println(purchaseHistory.toString());
                                            //display GUI of purchaseHistory
                                        } else {
                                            //display You have no past purchases!
                                        }
                                        break;

                                    case 6: //view your shopping cart
                                        ArrayList<Products> productListTemp = new ArrayList<>();
                                        do {
                                            try {
                                                Products products = (Products) ois.readObject();
                                                productListTemp.add(products);
                                                System.out.println("Received " + products.getName());
                                            } catch(Exception e) {
                                                break;
                                            }
                                        } while(true);
                                        //GUI for What would you like to do?
                                        //  1. Purchase cart
                                        //  2. Remove an item
                                        //  3. Return to Market Menu
                                        //items that couldn't be purchased
                                        if (!productListTemp.isEmpty()) {
                                            int choice3 = scan.nextInt();   //I'm sorry I keep declaring new variables
                                            scan.nextLine();              //I'm just worried I'll fuck up the loops if I don't
                                            writer.println(choice3);
                                            writer.flush();
                                            switch (choice3) {
                                                case 1:
                                                    StringBuilder notPurchasedItems = new StringBuilder();
                                                    do {
                                                        String potentialFailure = reader.readLine();
                                                        if (potentialFailure.equals("SUCCESS")) {
                                                            break;
                                                        } else {
                                                            notPurchasedItems.append(potentialFailure);
                                                            notPurchasedItems.append(", ");
                                                        }
                                                    } while (true);

                                                    if (!notPurchasedItems.isEmpty()) {
                                                        notPurchasedItems.delete(notPurchasedItems.length() - 2,
                                                                notPurchasedItems.length());
                                                        String errorMessage = String.format("The items %s is out of stock.",
                                                                notPurchasedItems.toString());
                                                        JOptionPane.showMessageDialog(null, errorMessage, "title", JOptionPane.ERROR_MESSAGE);
                                                        //GUI
                                                        //  minyhoung coded this JOptionPane but you don't need to use it
                                                        //  pop-ups for each item that was not able to be purchased
                                                    }
                                                    //GUI
                                                    //  message about "thanks for shopping! Returning to *whatever menu*
                                                    break;
                                                case 2: //remove an item
                                                    //GUI
                                                    //  use it to make selections, etc
                                                    System.out.println("Which item would you like to remove?");
                                                    choice3 = scan.nextInt();
                                                    scan.nextLine();
                                                    writer.println(choice3);
                                                    writer.flush();
                                                    reader.readLine(); //this reads the success line from server
                                                    break;
                                                default: //go back to market menu
                                                    break;
                                            }
                                        } else {
                                            //something with empty GUI
                                        }
                                        break;
                                    case 7: //go back to the main menu
                                        marketPlace = false;
                                        break;
                                    case 8: //quit
                                        marketPlace = false;
                                        mainMenu = false;
                                        //GUI
                                        //  a pop-up that says goodbye
                                        //  i would like to add something in here
                                        //  for both the server and client
                                        //  that checks if they actually want to quit
                                        //  but I'm not editing server right now so I'm not doing that yet
                                        return; //this ends the program
                                }

                            } while (marketPlace);
                            break;
                        case 3: //quit
                            socket.close();
                            //GUI
                            //  a pop-up that says goodbye
                            //  i would like to add something in here
                            //  for both the server and client
                            //  that checks if they actually want to quit
                            //  but I'm not editing server right now so I'm not doing that yet
                            mainMenu = false;
                            return; //ends program

                    }

                } while (mainMenu);

            } else { //SELLER
                boolean mainMenu = true;
                int menuChoice;
                do {
                    /**
                     * Main Menu
                     * 1. View/edit your account
                     * 2. View farmer's market
                     * 3. Quit
                     * **/
                    //GUI
                    //  for main menu
                    System.out.println("What would you like to do?");
                    menuChoice = scan.nextInt();
                    scan.nextLine();
                    writer.println(menuChoice);
                    writer.flush();
                    switch (menuChoice) {
                        case 1: //view/edit your account
                            boolean editAcc = true;
                            do {
                                String name = reader.readLine();
                                String email = reader.readLine();
                                String password = reader.readLine();
                                //GUI
                                //  1. edit your name
                                //  2. edit your email
                                //  3. edit your password
                                //  4. delete your account
                                //  5. return to main menu
                                System.out.println("What would you like to do?");
                                menuChoice = scan.nextInt();
                                scan.nextLine();
                                writer.println(menuChoice);
                                writer.flush();
                                switch (menuChoice) {
                                    case 1: //edit your name
                                        //GUI input
                                        System.out.println("What would you like to change your name to?");
                                        name = scan.nextLine();
                                        writer.println(name);
                                        writer.flush();
                                        reader.readLine(); //reads "SUCCESS" from server
                                        break;
                                    case 2: //edit your email
                                        do {
                                            //GUI input
                                            System.out.println("what would you like to change your email to?");
                                            email = scan.nextLine();
                                            writer.println(email);
                                            writer.flush();
                                            String success = reader.readLine();
                                            if (success.equals("FAILED")) {
                                                //GUI for That email is already in use! Please try again
                                            } else {
                                                break;
                                            }
                                        } while (true);
                                        break;
                                    case 3:
                                        //GUI input
                                        System.out.println("what would you like to change your password to?");
                                        //maybe have a way to check it's not the same as the current password but idk
                                        password = scan.nextLine();
                                        writer.println(password);
                                        writer.flush();
                                        break;
                                    case 4:
                                        writer.println(4);
                                        writer.flush();
                                        socket.close();
                                        return;
                                    default:
                                        editAcc = false;
                                        break;
                                }
                            } while (editAcc);
                        case 2: //view farmer's market
                            boolean marketMenu = true;
                            //GUI
                            //  1. View booths
                            //  2. Add booth
                            //  3. Edit booth
                            //  4. Remove booth
                            //  5. Go back
                            do {
                                System.out.println("what would you like to do?");
                                menuChoice = scan.nextInt();
                                scan.nextLine();
                                writer.println(menuChoice);
                                writer.flush();

                                switch (menuChoice) {
                                    case 1: //view booths
                                        do {
                                            String storeName = reader.readLine();
                                            if (storeName.equals("END")) {
                                                break;
                                            }
                                            System.out.println(storeName);
                                        } while (true);

                                        System.out.println("Select booth");

                                        menuChoice = scan.nextInt();
                                        scan.nextLine();
                                        writer.println(menuChoice);
                                        writer.flush();

                                        //GUI
                                        //  1. View products
                                        //  2. View sales
                                        //  3. Add product
                                        //  4. Edit product
                                        //  5. Remove product
                                        //  6. Import product csv file
                                        //  7. Export product csv file
                                        //  8. Go back

                                        boolean boothMenu = true;
                                        do {
                                            System.out.println("What would you like to do?");
                                            menuChoice = scan.nextInt();
                                            scan.nextLine();
                                            writer.println(menuChoice);
                                            writer.flush();
                                            switch (menuChoice) {
                                                case 1:
                                                    do {
                                                        try {
                                                            Object o = ois.readObject();
                                                            System.out.println(((Products) o).getName());
                                                        } catch (Exception e) {
                                                            break;
                                                        }
                                                    } while (true);
                                                    break;
                                                case 2:
                                                    System.out.printf("The sales is %s.\n", reader.readLine());
                                                    break;
                                                case 3:
                                                    do {
                                                        System.out.println("Enter product name (0 to stop)");
                                                        String productName = scan.nextLine();
                                                        if (productName.equals("0")) {
                                                            writer.println("END OF PRODUCT");
                                                            writer.flush();
                                                            break;
                                                        } else {
                                                            writer.println(productName);
                                                            writer.flush();
                                                        }
                                                    } while (true);
                                                    break;
                                                case 4: //edit products
                                                    ArrayList<Products> productsList = new ArrayList<>();
                                                    do {
                                                        try {
                                                            Products products = (Products) ois.readObject();
                                                            productsList.add(products);
                                                            System.out.println(products.getName());
                                                        } catch (Exception e) {
                                                            break;
                                                        }
                                                    } while (true);
                                                    if (!productsList.isEmpty()) {
                                                        System.out.println("Select product to edit");
                                                        writer.println(scan.nextLine());
                                                        writer.flush();
                                                        System.out.println("would you like to change name?");
                                                        if (scan.nextLine().equals("yes")) {
                                                            oos.writeBoolean(true);
                                                            oos.flush();
                                                            System.out.println("Enter new name:");
                                                            writer.println(scan.nextLine());
                                                            writer.flush();
                                                        } else {
                                                            oos.writeBoolean(false);
                                                            oos.flush();
                                                        }
                                                        System.out.println("would you like to change description?");
                                                        if (scan.nextLine().equals("yes")) {
                                                            oos.writeBoolean(true);
                                                            oos.flush();
                                                            System.out.println("Enter new description:");
                                                            writer.println(scan.nextLine());
                                                            writer.flush();
                                                        } else {
                                                            oos.writeBoolean(false);
                                                            oos.flush();
                                                        }
                                                        System.out.println("would you like to change price?");
                                                        if (scan.nextLine().equals("yes")) {
                                                            oos.writeBoolean(true);
                                                            oos.flush();
                                                            System.out.println("Enter new price:");
                                                            writer.println(scan.nextLine());
                                                            writer.flush();
                                                        } else {
                                                            oos.writeBoolean(false);
                                                            oos.flush();
                                                        }
                                                        System.out.println("would you like to change quantity?");
                                                        if (scan.nextLine().equals("yes")) {
                                                            oos.writeBoolean(true);
                                                            oos.flush();
                                                            System.out.println("Enter new quantity:");
                                                            writer.println(scan.nextLine());
                                                            writer.flush();
                                                        } else {
                                                            oos.writeBoolean(false);
                                                            oos.flush();
                                                        }
                                                    } else {
                                                        System.out.println("No products in the chosen store");
                                                    }
                                                    break;
                                                case 5: //removing product
                                                    productsList = new ArrayList<>();
                                                    do {
                                                        try {
                                                            Products products = (Products) ois.readObject();
                                                            productsList.add(products);
                                                            System.out.println(products.getName());
                                                        } catch (Exception e) {
                                                            break;
                                                        }
                                                    } while (true);
                                                    if (!productsList.isEmpty()) {
                                                        System.out.println("Which product would you like to remove?");
                                                        menuChoice = scan.nextInt();
                                                        scan.nextLine();
                                                        writer.println(menuChoice);
                                                        writer.flush();
                                                    } else {
                                                        System.out.println("No products in the chosen store");
                                                    }
                                                    break;
                                                case 6:
                                                    System.out.println("Enter csv file name");
                                                    String fileName = scan.nextLine();
                                                    if (!fileName.contains("csv")) {
                                                        fileName = fileName + ".csv";
                                                    }
                                                    Seller currentSeller = (Seller) ois.readObject();
                                                    Store currentStore = (Store) ois.readObject();
                                                    try {
                                                        currentStore.assignProduct(fileName);
                                                        for (Store stores : currentSeller.getStore()) {
                                                            if (stores.getName().equals(currentStore.getName())) {
                                                                currentSeller.getStore().set(currentSeller.getStore().
                                                                        indexOf(stores), currentStore);
                                                            }
                                                        }
                                                    } catch (Exception e) {}
                                                    oos.writeObject(currentSeller);
                                                    oos.flush();
                                                    oos.writeObject(currentStore);
                                                    oos.flush();
                                                    break;
                                                case 7:
                                                    currentStore = (Store) ois.readObject();
                                                    fileName = scan.nextLine();
                                                    currentStore.writeProductFile(fileName);
                                                    break;
                                                case 8:
                                                    boothMenu = false;
                                                    break;
                                            }
                                        } while (boothMenu);
                                        break;
                                    case 2:
                                        System.out.println("Enter booth name");
                                        String boothname = scan.nextLine();
                                        writer.println(boothname);
                                        writer.flush();
                                        break;
                                    case 3:
                                        String response = reader.readLine();
                                        if (response.equals("EMPTY")) {
                                            System.out.println("You have no booth to edit.");
                                        } else {
                                            do {
                                                String storeName = reader.readLine();
                                                if (storeName.equals("END")) {
                                                    break;
                                                } else {
                                                    System.out.println(storeName);
                                                }
                                            } while (true);
                                            System.out.println("What store would you like to edit?");
                                            menuChoice = scan.nextInt();
                                            scan.nextLine();
                                            writer.println(menuChoice);
                                            writer.flush();
                                            System.out.println("Enter the name of the booth: ");
                                            String productName = scan.nextLine();
                                            writer.println(productName);
                                            writer.flush();
                                            break;
                                        }
                                    case 4:
                                        response = reader.readLine();
                                        if (response.equals("EMPTY")) {
                                            System.out.println("You have no booth to remove.");
                                        } else {
                                            do {
                                                String storeName = reader.readLine();
                                                if (storeName.equals("END")) {
                                                    break;
                                                } else {
                                                    System.out.println(storeName);
                                                }
                                            } while (true);
                                            System.out.println("Which booth do you want to remove?");
                                            writer.println(scan.nextLine());
                                            writer.flush();
                                        }
                                        break;
                                    case 5:
                                        marketMenu = false;
                                        break;
                                }
                            } while (marketMenu);
                            break;
                        case 3:
                            mainMenu = false;
                            break;
                    }
                } while (mainMenu);


            }


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Classnotfoundexception");
        }

    }
}
