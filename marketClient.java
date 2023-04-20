import javax.swing.*;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
import java.util.ArrayList;
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
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());


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
                                System.out.println("what choice?");
                                int decision = scan.nextInt();
                                scan.nextLine();
                                writer.println(decision);
                                writer.flush();
                                switch (decision) {
                                    case 1:
                                        ArrayList<Products> productsList = new ArrayList();
                                        while (reader.readLine().equals("END")) {
                                            productsList.add((Products) ois.readObject());
                                        }
                                        //GUI
                                        //  show the list of products and make a selection
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
                                            System.out.println("What would you like to do? 1. 2. 3");
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
                                        break;


                                    case 2: //searchProducts
                                        do {
                                            //GUI
                                            //  asks for a keyword they want to scan for
                                            ArrayList<String> searchResults = new ArrayList<>();
                                            System.out.println("what would you like to search for?");
                                            String keyword = scan.nextLine();
                                            writer.write(keyword);
                                            writer.flush();
                                            String firstMessage = reader.readLine();
                                            if (!firstMessage.equals("NO SEARCH RESULTS")) {
                                                searchResults.add(firstMessage);
                                                while (!reader.readLine().equals("END")) {
                                                    searchResults.add(reader.readLine());
                                                }
                                                //GUI
                                                //  shows all search results names in numbered order
                                                //  gets the reader to pick one
                                                System.out.println("Which product would you like to view?");
                                                int searchIndex = scan.nextInt();
                                                scan.nextLine();
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
                                                        writer.write(placeholderGUI);
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
                                                                    writer.write(quantity);
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
                                                break;
                                            } else {
                                                writer.println("NO");
                                                writer.flush();
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
                                    case 5: //view your purchase history
                                        ArrayList<String> purchaseHistory = new ArrayList<>();
                                        while (!reader.readLine().equals("END")) {
                                            purchaseHistory.add(reader.readLine());
                                        }
                                        //GUI
                                        //  show purchase history in a pop-up
                                        if (!purchaseHistory.isEmpty()) {
                                            //display GUI of purchaseHistory
                                        } else {
                                            //display You have no past purchases!
                                        }
                                        break;
                                        
                                    case 6: //view your shopping cart
                                        ArrayList<Products> productListTemp = new ArrayList<>();
                                        do {
                                            try {
                                                Object o = ois.readObject();
                                                String exitLine = (String) o;
                                                if (exitLine.equals("END")) {
                                                    break;
                                                } else {
                                                    productListTemp.add((Products) o);
                                                }
                                            } catch(Exception e) {
                                                break;
                                            }
                                        } while(true);
                                        //GUI for What would you like to do?
                                        //  1. Purchase cart
                                        //  2. Remove an item
                                        //  3. Return to Market Menu
                                         //items that couldn't be purchased
                                        int choice3 = scan.nextInt();   //I'm sorry I keep declaring new variables
                                        scan.nextLine();              //I'm just worried I'll fuck up the loops if I don't
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
                                        break;
                                        
                                    case 7: //go back to the main menu
                                    case 8: //quit



                                }

                            } while (marketPlace);


                    }

                } while (mainMenu);

            } else {
                //SELLER
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}

/*
