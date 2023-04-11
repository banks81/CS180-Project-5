import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.*;

public class Market {
    public static final String WELCOME = "Welcome to the CS 180 Farmer's Market!";
    public static final String LOGIN = "Are you an existing user?";
    public static final String ENTERVALID = "Please enter a valid option!";
    public static final String YESNO = "1. Yes\n2. No";
    public static final String ENTERLOGIN = "Please enter your email address.";
    public static final String NOUSER = "No user found with given credentials!";
    public static final String TRYAGAIN = "Would you like to try again?";
    public static final String ENTERPSWD = "Please enter your password.";
    public static final String EMAILEXISTS = "A user already exists with this email!";

    public static final String BYE = "Sorry to see you go! Happy trails!";
    public static final String OPTIONS = "Please choose your option:";
    public static final String SELLORCUST = "Would you like to join as a\n1. Customer\n2. Farmer";
    public static final String ENTERNAME = "Please enter your name";
    public static final String CHANGENAME = "What would you like to change your name to?";
    public static final String CHANGEEMAIL = "What would you like to change your email to?";
    public static final String CHANGEPASSWORD = "What would you like to change your password to?";
    public static final String CHANGED = "Changed!";
    public static final String SUREDELETE = "Are you sure you want to delete your account?";
    public static final String DELETING = "Deleting your account...";
    public static final String SUREQUIT = "Are you sure you want to quit?";


    //seller
    public static final String SELLEROPTIONS = "1. Manage Booths\n2. View sales";
    public static final String STOREOPTION = "Which booth would you like to access?";
    public static final String STORESPEC = "1. Create products\n2. Modify existing products";
    public static final String NEWOPTIONS = "Would you like to import csv file?";
    public static final String FILEIMPORT = "Please enter the name of file:";

    //customer
    public static final String MAINMENU = "Main Menu\n1. View/edit your account\n2. View farmer's market\n3. Quit";
    public static final String CUSTACCOUNTCHOICES = "1. Edit your name\n2. Edit your email\n" +
            "3. Edit your password\n4. Delete your account\n5. Return to main menu";
    public static final String CUSTINFO = "Customer Info:";
    public static final String MARKETHEADER = "~*~ Farmer's Market Main Menu ~*~";
    public static final String SELECTPRODUCT = "Select which product you'd like to view!";
    public static final String VIEWOVERALL = "1. View overall farmer's market listings";
    public static final String SEARCHSPECIFIC = "2. Search for specific products";
    public static final String SORTPRICE = "3. Sort the products by price, lowest to highest";
    public static final String SORTQUANTITY = "4. Sort the products by quantity available, lowest to highest";
    public static final String VIEWHISTORY = "5. View your purchase history";
    public static final String VIEWSHOPPINGCART = "6. View your shopping cart";
    public static final String GOTOMAINMENU = "Go back to main menu";
    public static final String QUIT = "Quit";
    //for viewing/purchasing items
    public static final String ADDTOCART = "1. Add to cart";

    public static final String PURCHASENOW = "2. Purchase now";

    public static final String GOBACKTOPRODUCTS = "3. Go back to products list";
    public static final String ADDEDTOSHOP = "Added to shopping cart!";
    public static final String REMOVEDSHOP = "Removed from shopping cart!";
    public static final String PURCHASED = "Purchased!";


    //shopping cart menu
    public static final String SHOPPINGCARTHEADER = "~*~ Shopping Cart ~*~";
    public static final String INSHOPPINGCART = "Items in your shopping cart:";
    public static final String EDITCART = "1. Edit/delete items";
    public static final String PURCHASECART = "2. Purchase all";
    public static final String GOTOMARKETMENU = "3. Go back to the market";

    public static boolean doesEmailExist(ArrayList<User> customersList, ArrayList<User> sellersList, String email) {
        boolean emailExists = false;
        for (int i = 0; i < sellersList.size(); i++) {
            if (email.equals(sellersList.get(i).getEmail())) {
                emailExists = true;
            }
        }
        for (int i = 0; i < customersList.size(); i++) {
            if (email.equals(customersList.get(i).getEmail())) {
                emailExists = true;
            }
        }
        return emailExists;
    }

    public static ArrayList<User> customersList = new ArrayList<>();
    public static ArrayList<User> sellersList = new ArrayList<>();
    public static ArrayList<Store> storesList = new ArrayList<>(); //arrayList of stores in the marketplace
    public static ArrayList<Products> productsList = new ArrayList<>();
    public static ArrayList<String> customerTempCart = new ArrayList<>();
    

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int choice = -1; //scanner choice
        boolean invalidChoice = false; //the boolean I'll use for do-while loops to check invalid input
        boolean stayInMenu = true;
        boolean emailExists = false; //does an email already exist for this user
        boolean seller; //if true, user is a seller, if false, user is a customer
        boolean stayInMarketMenu = true;
        boolean stayInProductMenu = true; //boolean for the smaller end-menus after market menu

        User currentUser = null; //the current user of the program

        //TODO here call the method that parses through the files and gets all the info
        //Necessary arrays to have:
        // 1. arrayList of products
        //
        //for buyers and sellers and etc

        readFile();
        System.out.println(WELCOME);

        choice = checkChoice(LOGIN + "\n" + YESNO, scan, 2);

        boolean matches = false;
        if (choice == 1) { //sign in
            do {
                System.out.println(ENTERLOGIN);
                String email = scan.nextLine();
                System.out.println(ENTERPSWD);
                String password = scan.nextLine();
                for (int i = 0; i < customersList.size(); i++) {
                    if (email.equals(customersList.get(i).getEmail()) &&
                            password.equals(customersList.get(i).getPassword())) {
                        currentUser = customersList.get(i);
                        matches = true;
                    }
                }
                if (!matches) {
                    for (int i = 0; i < sellersList.size(); i++) {
                        if (email.equals(sellersList.get(i).getEmail()) &&
                                password.equals(sellersList.get(i).getPassword())) {
                            currentUser = sellersList.get(i);
                            matches = true;
                        }
                    }
                }

                if (matches) {
                    System.out.println("Connecting you to the farmer's market!");
                } else {
                    System.out.println(NOUSER);
                    choice = checkChoice(TRYAGAIN + '\n' + YESNO, scan, 2);
                    if (choice == 1) {
                        matches = false;
                    } else {
                        System.out.println(BYE);
                        writeFile();
                        return;
                    }
                }

            } while (!matches);


        } else if (choice == 2) { //Create an account
            choice = checkChoice(SELLORCUST, scan, 2);
            if (choice == 1) { //user is a customer
                seller = false;
            } else {
                seller = true; //user is a seller
            }
            System.out.println(ENTERNAME);
            String name = scan.nextLine();
            System.out.println(ENTERLOGIN);
            String email = scan.nextLine();
            System.out.println(ENTERPSWD);
            String password = scan.nextLine();
            do {
                if (emailExists) {
                    System.out.println(ENTERLOGIN);
                    email = scan.nextLine();
                    emailExists = false;
                }

                emailExists = doesEmailExist(customersList, sellersList, email);
                if (emailExists) {
                    System.out.println(EMAILEXISTS);
                    choice = checkChoice("Would you like to enter a different email?\n"
                            + YESNO, scan, 2);
                    if (choice == 2) {
                        System.out.println(BYE);
                        writeFile();
                        return;
                    }
                }
            } while (emailExists);

            if (!seller) { //customer
                currentUser = new Customer(email, name, password);
                customersList.add(currentUser);
            } else { //seller
                currentUser = new Seller(email, name, password);
                sellersList.add(currentUser);
            }
        }
        //NOW CODE FOR RUNNING MARKETPLACE, ETC
        do { //the loop that keeps the user going back to the main menu
            choice = checkChoice(MAINMENU, scan, 3);
            if (choice == 1) { //view/edit your account
                do { //loop that keeps them in the view/edit account
                    System.out.println(CUSTINFO);
                    System.out.println("Name: " + currentUser.getName() +
                            "\nEmail: " + currentUser.getEmail() +
                            "\nPassword: " + currentUser.getPassword());
                    choice = checkChoice(CUSTACCOUNTCHOICES, scan, 5);
                    switch (choice) {
                        case 1 -> { //edit your name
                            System.out.println(CHANGENAME);
                            String name = scan.nextLine();
                            currentUser.setName(name);
                            System.out.println(CHANGED);
                        }
                        case 2 -> { //edit your email
                            System.out.println(CHANGEEMAIL);
                            String email = scan.nextLine();
                            emailExists = doesEmailExist(customersList, sellersList, email);
                            if (!emailExists) {
                                currentUser.setEmail(email);
                                System.out.println(CHANGED);
                            } else {
                                System.out.println(EMAILEXISTS);
                                choice = checkChoice(TRYAGAIN + "\n" + YESNO, scan, 2);
                            }
                        }
                        case 3 -> { //edit your password
                            System.out.println(CHANGEPASSWORD);
                            String password = scan.nextLine();
                            currentUser.setPassword(password);
                            System.out.println(CHANGED);
                        }
                        case 4 -> { //delete your account
                            choice = checkChoice(SUREDELETE + "\n" + YESNO, scan, 2  );
                            if (choice == 1) {
                                System.out.println(BYE);
                                System.out.println("We hope you join us again soon!");
                                if (currentUser instanceof Customer) {
                                    customersList.remove(currentUser);
                                }
                                if (currentUser instanceof Seller) {
                                    sellersList.remove(currentUser);
                                }
                                writeFile();
                                return;
                            } else {
                                System.out.println("Ok!");
                            }
                        }
                        case 5 -> //go back to main menu
                                stayInMenu = false;
                    }

                } while (stayInMenu);
                stayInMenu = true;

            } else if (choice == 2) { //Farmer's Market Main Menu
                //PRINT MARKETPLACE
                if (currentUser instanceof Customer) {

                    do { //the loop that keeps the user in the market menu
                        choice = checkChoice(MARKETHEADER + "\n" +
                                VIEWOVERALL + "\n" +
                                SEARCHSPECIFIC + "\n" +
                                SORTPRICE + "\n" +
                                SORTQUANTITY + "\n" +
                                VIEWHISTORY + "\n" +
                                VIEWSHOPPINGCART + "\n" +
                                "7. " + GOTOMAINMENU + "\n" +
                                "8. " + QUIT, scan, 8);

                        switch (choice) { //for mainMarketMenu choices
                            case 1 -> {
                                int productSelection = -1;//VIEW OVERALL LISTINGS
                                do {
                                    String productSelectionString = SELECTPRODUCT + "\n";
                                    for (int i = 0; i < productsList.size(); i++) { //prints out all products
                                        productSelectionString = productSelectionString + String.valueOf(i + 1)
                                                + ". " + productsList.get(i).toString() + '\n';
                                    }
                                    productSelectionString = productSelectionString + String.valueOf(productsList.size() + 1)
                                            + ". Go back to market menu\n";
                                    productSelectionString = productSelectionString + String.valueOf(productsList.size() + 2)
                                            + ". Quit";
                                    productSelection = checkChoice(productSelectionString, scan, productsList.size() + 2);

                                    if (productSelection != productsList.size() + 1 && productSelection != productsList.size() + 2) {
                                        //basically if the choice is to view one of the products
                                        System.out.println(productsList.get(productSelection - 1).toString()); //prints product

                                        choice = checkChoice("Would you like to...\n1. Add one to cart\n"
                                             + PURCHASENOW + "\n" + GOBACKTOPRODUCTS, scan, 3);

                                        switch (choice) {
                                            case 1 -> { //shopping cart requires quantity of item in the cart :/ I'll initialize it to be one for now
                                                ((Customer) currentUser).addToShoppingCart(productsList.get(productSelection - 1), 1);
                                                System.out.println(ADDEDTOSHOP);
                                                System.out.println("Returning to product listings...");
                                                stayInProductMenu = true;
                                                break;
                                            }
                                            case 2 -> {
                                                int purchaseQuantity = 5000;
                                                if (productsList.get(productSelection - 1).getQuantity() == 0) {
                                                    System.out.println("Sorry! This item is out of stock.");
                                                } else {
                                                    do {
                                                        purchaseQuantity = checkInt(scan, "How many would you like to purchase?");

                                                        if (productsList.get(productSelection - 1).getQuantity() < purchaseQuantity) {
                                                            invalidChoice = true;      //checks that purchaseQuantity isn't greater than product quantity
                                                            System.out.println("There aren't that many products available! \n" +
                                                                    "Please enter a new number.");
                                                        } else if (purchaseQuantity == 0) {
                                                            invalidChoice = false;
                                                            System.out.println("Not purchasing any right now.\nReturning to product listings...");
                                                        } else {
                                                            invalidChoice = false;
                                                            System.out.println("Purchasing " + purchaseQuantity + "...");
                                                            productsList.get(productSelection - 1).setQuantity(productsList.get(productSelection - 1).getQuantity()
                                                                    - purchaseQuantity);
                                                            ((Customer) currentUser).addProducts(productsList.get(productSelection - 1).getName(),
                                                                    purchaseQuantity);

                                                        }

                                                    } while (invalidChoice);
                                                }
                                            }
                                            case 3 -> {
                                                stayInProductMenu = true;
                                                break;
                                            }
                                        }

                                    } else {
                                        if (productSelection == productsList.size() + 1) {    //go back to market menu
                                            stayInMarketMenu = true;
                                            stayInProductMenu = false;
                                        } else if (productSelection == productsList.size() + 2) { //quit
                                            choice = checkChoice(SUREQUIT + "\n" + YESNO, scan, 2);

                                            if (choice == 1) {
                                                System.out.println(BYE);
                                                writeFile();
                                                return;
                                            } else {
                                                System.out.println("Ok!");
                                                stayInProductMenu = true;
                                                stayInMarketMenu = true;
                                            }
                                        }
                                    }
                                } while (stayInProductMenu);


                            }
                            case 2 -> {                   //SEARCH FOR PRODUCTS
                                int stayInSearchMenu = -1;
                                ArrayList<Products> foundProducts = new ArrayList<>();
                                int foundProductCounter = 0;
                                int userSelection;

                                do { //Loop to continue making searches
                                    int timesThroughSearch = 0;
                                    foundProducts = new ArrayList<>();
                                    foundProductCounter = 0;
                                    System.out.println("What product are you looking for?");
                                    String productSearch = scan.nextLine();

                                    //Loop to look for searched keyword in every product description, name, and store name
                                    System.out.println("Search Results:");
                                    for (int i = 0; i < productsList.size(); i++) {
                                        if (productsList.get(i).getName().contains(productSearch) ||
                                                productsList.get(i).getStoreName().contains(productSearch) ||
                                                productsList.get(i).getDescription().contains(productSearch)) {
                                            foundProductCounter++;
                                            foundProducts.add(productsList.get(i));
                                            System.out.println(foundProductCounter + ". " + productsList.get(i).getName());
                                        }
                                    }
                                    if (foundProducts.isEmpty()) {
                                        System.out.println("Sorry, your search yielded no results");
                                    } else {
                                        //Ask the user to make a selection
                                        userSelection = -1;
                                        
                                        userSelection = checkChoice("Select which product you'd like to view!", scan,
                                                    foundProducts.size());
                                        
                                        //Show Selected product
                                        System.out.println(foundProducts.get(userSelection - 1));
                                        choice = checkChoice("Would you like to...\n1. Add one to cart\n" +
                                                PURCHASENOW + "\n" + GOBACKTOPRODUCTS, scan, 3);
                                        switch (choice) {
                                            case 1 -> { //same here
                                                ((Customer) currentUser).addToShoppingCart(foundProducts.get(userSelection - 1), 1);
                                                System.out.println(ADDEDTOSHOP);
                                                stayInProductMenu = true;
                                                break;
                                            }
                                            case 2 -> {
                                                int purchaseQuantity = 5000;
                                                purchaseQuantity = checkInt(scan, "How many would you like to purchase?",
                                                        foundProducts.get(userSelection - 1).getQuantity(),
                                                        "There aren't that many products available!\n" +
                                                                "Please enter a new number.");
                                                if (purchaseQuantity == 0) {
                                                    System.out.println("Cannot purchase 0 items! Returning to product menu.");
                                                } else {
                                                    System.out.println("Purchasing " + purchaseQuantity + "...");
                                                    productsList.get(userSelection - 1).setQuantity(foundProducts.get(userSelection - 1).getQuantity()
                                                            - purchaseQuantity);
                                                    ((Customer) currentUser).addProducts(foundProducts.get(userSelection - 1).getName(),
                                                            purchaseQuantity);
                                                }
                                            }
                                        }

                                        //Ask if user wants to search again, if so stay in loop
                                    }
                                    stayInSearchMenu = -1;
                                    stayInSearchMenu = checkChoice("Would you like to make another search?\n" +
                                                YESNO, scan, 2);
                                    timesThroughSearch++;
                                } while (stayInSearchMenu == 1);


                            }
                            case 3 -> { //sort the products by price, lowest to highest
                                ArrayList<Products> tempArr = new ArrayList<>();
                                double min;
                                int minInd;

                                while (!productsList.isEmpty()) { //Empties out the entire list, sorting each removed element into the temp array
                                    min = productsList.get(0).getPrice();
                                    minInd = 0;

                                    //For loop through all products - find minimum price, then add that to the sorted
                                    //array and remove it from the original array
                                    for (int i = 0; i < productsList.size(); i++) {
                                        if (productsList.get(i).getPrice() < min) {
                                            min = productsList.get(i).getPrice();
                                            minInd = i;
                                        }
                                    }
                                    tempArr.add(productsList.get(minInd));
                                    productsList.remove(minInd);
                                }
                                productsList = new ArrayList<>(tempArr); //Set the original array equal to the sorted temp array
                                System.out.println("Products have been sorted from lowest to highest price!\n" +
                                        "Select option 1 to view sorted products.");

                            }
                            case 4 -> { //sort the products by quantity available, lowest to highest
                                ArrayList<Products> tempArr = new ArrayList<>();
                                double min;
                                int minInd;
                                while (!productsList.isEmpty()) { //Empties out the entire list, sorting each removed element into the temp array
                                    min = productsList.get(0).getQuantity();
                                    minInd = 0;

                                    //For loop through all products - find minimum quantity, then add that to the sorted
                                    //array and remove it from the original array
                                    for (int i = 0; i < productsList.size(); i++) {
                                        if (productsList.get(i).getQuantity() < min) {
                                            min = productsList.get(i).getQuantity();
                                            minInd = i;
                                        }
                                    }
                                    tempArr.add(productsList.get(minInd));
                                    productsList.remove(minInd);
                                }
                                productsList = new ArrayList<>(tempArr); //Set the original array equal to the sorted temp array
                                System.out.println("Products have been sorted from lowest to highest quantity available!");

                            }
                            case 5 -> { //view your purchase history
                                ArrayList<String> tempStringArr = ((Customer) currentUser).getPastPurchase();

                                if (tempStringArr.isEmpty())
                                    System.out.println("You have no previous purchases.");
                                else {
                                    System.out.println("Previous purchases:");
                                    for (int i = 0; i < tempStringArr.size(); i++) {
                                        System.out.println((i + 1) + ". " + tempStringArr.get(i));
                                    }
                                }
                                System.out.print("\nReturning to market menu...\n");

                            }
                            case 6 -> { //view your shopping cart
                                //Retrieve the private shopping cart list through a temp list
                                if (!((Customer) currentUser).getShoppingCartChanges().isEmpty()) {
                                    for (int i = 0; i < ((Customer) currentUser).getShoppingCartChanges().size(); i++) {
                                        System.out.println(((Customer) currentUser).getShoppingCartChanges().get(i));
                                    }
                                }

                                ArrayList<Products> tempProductsArr = ((Customer) currentUser).getShoppingCart();
                                //Print out the list
                                if (tempProductsArr.isEmpty()) {
                                    System.out.println("You have nothing in your shopping cart.");
                                    stayInMarketMenu = true;
                                } else {
                                    System.out.println("Shopping cart:");
                                    for (int i = 0; i < tempProductsArr.size(); i++) {
                                        System.out.println((i + 1) + ". " + tempProductsArr.get(i).getName() +
                                                ", " + tempProductsArr.get(i).getDescription() + "\n    Price: " +
                                                tempProductsArr.get(i).getPrice());
                                    }
                                    //Get input on what action the user would like to take
                                    int userSelection = 0;
                                    userSelection = checkChoice("What would you like to do?\n" +
                                            "1. Purchase cart\n" +
                                            "2. Remove an item\n" +
                                            "3. Return to Market Menu", scan, 3);

                                    if (userSelection == 1) { //PURCHASE CART
                                        for (int i = 0; i < ((Customer) currentUser).getShoppingCart().size(); i++) {
                                            if (((Customer) currentUser).getShoppingCart().get(i).getQuantity() > 1) {

                                                ((Customer) currentUser).getShoppingCart().get(i).setQuantity(
                                                        ((Customer) currentUser).getShoppingCart().get(i).getQuantity() - 1); //lowers quantity by 1
                                                ((Customer) currentUser).addProducts(((Customer) currentUser).getShoppingCart()
                                                        .get(i).getName(), 1);

                                                //this lowers the product's quantity by 1 and adds it to the customer's
                                                //list of past purchased items
                                            } else {
                                                System.out.println("Could not purchase one" + tempProductsArr.get(i).getName()
                                                        + ", out of stock!");

                                            }
                                        }
                                        for (int i = 0; i < tempProductsArr.size(); i++) {
                                            ((Customer) currentUser).removeFromShoppingCart(tempProductsArr.get(i));
                                        }


                                    } else if (userSelection == 2) { //REMOVE AN ITEM FROM CART
                                        //Get input on which item to remove
                                        userSelection = 0;
                                        userSelection = checkChoice("Which item would you like to remove?", 
                                                scan, tempProductsArr.size());
                                        //Remove inputted item
                                        ((Customer) currentUser).removeFromShoppingCart(tempProductsArr.get(userSelection - 1));

                                    } else { //BACK TO MARKET MENU
                                        stayInMarketMenu = true;
                                    }
                                }
                            }
                            case 7 -> { //go back to main menu
                                stayInMarketMenu = false;
                            }
                            case 8 -> { //quit
                                stayInMarketMenu = false;
                                stayInMenu = false;
                                System.out.println(BYE);
                                writeFile();
                                return;
                            }
                        } //end of switch statement


                    } while (stayInMarketMenu);


                } else if (currentUser instanceof Seller) {
                    ((Seller) currentUser).sellerMainMenu(scan);
                } else {
                    System.out.println("User was not initialized");
                }


            } else if (choice == 3) {
                System.out.println(BYE);
                writeFile();
                return;
            }
        } while (stayInMenu);
        writeFile();
    }

    public static void readFile() {
        File users = new File("UsersList.txt");
        try {
            FileReader fileReader = new FileReader(users);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            boolean customerOverFlowed = true;
            String infoType = null;
            String lineNext = "";
            while (true) {
                if (customerOverFlowed) {   //if true; current cursor at --------
                    lineNext = bufferedReader.readLine();
                    customerOverFlowed = false;
                } else {    //if false; current cursor at customer information
                    lineNext = infoType;
                }
                if (lineNext == null) {
                    break;
                }
                String[] firstLine = lineNext.split(",");
                if (firstLine[0].equals("Customer")) {
                    Customer customerCurrent = new Customer(firstLine[1], firstLine[2], firstLine[3]);
                    infoType = bufferedReader.readLine();
                    if (infoType.equals("PAST PURCHASE")) {
                        while (true) {
                            String productsList = bufferedReader.readLine();
                            if (productsList.equals("--------")) {
                                break;
                            }
                            String[] pastPurchase = productsList.split(",");
                            customerCurrent.addProducts(pastPurchase[0], Integer.parseInt(pastPurchase[1]));
                        }
                        infoType = bufferedReader.readLine();
                        if (infoType == null) {
                            break;
                        }
                    }
                    if (infoType.equals("SHOPPING CART")) {
                        StringBuilder customerCartBuild = new StringBuilder();
                        while (true) {
                            String shoppingList = bufferedReader.readLine();
                            if (shoppingList.equals("--------")) {
                                customerOverFlowed = true;
                                break;
                            }
                            customerCartBuild.append(shoppingList);
                            customerCartBuild.append("\n");
                        }
                        customerTempCart.add(customerCartBuild.toString());
                    } else {
                        if (infoType.equals("--------")) {
                            customerOverFlowed = true;
                        } else {
                            customerOverFlowed = false;
                        }
                        customerTempCart.add(null);
                    }
                    customersList.add(customerCurrent);
                } else {
                    Seller sellerCurrent = new Seller(firstLine[1], firstLine[2], firstLine[3]);
                    sellersList.add(sellerCurrent);
                    infoType = bufferedReader.readLine();
                    customerOverFlowed = true;
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist!");
        } catch (IOException e) {
            System.out.println("Invalid file format!");
        }
        File storeList = new File("StoreList.txt");
        try {
            FileReader fileReader = new FileReader(storeList);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            boolean isAtDashline = true;
            String lineNext = "";
            //each store
            while (true) {
                if (isAtDashline) {
                    lineNext = bufferedReader.readLine();    //cursor must be at -------- before this statement
                }
                isAtDashline = true;
                if (lineNext == null || lineNext.equals("\n")) {
                    break;
                }
                Seller currentSeller = null;
                Store currentStore = null;

                String storeName = lineNext;
                String[] sellerDesc = bufferedReader.readLine().split(",");
                String sellerName = sellerDesc[1];
                String sellerEmail = sellerDesc[0]; //solid until here
                for (User seller : sellersList) {
                    if (seller.getName().equals(sellerName) && seller.getEmail().equals(sellerEmail)) {
                        currentSeller = (Seller) seller;
                        break;
                    }
                }
                currentStore = new Store(storeName, sellerName, sellerEmail);
                currentStore.setRevenue(Double.parseDouble(bufferedReader.readLine()));
                currentStore.setSales(Integer.parseInt(bufferedReader.readLine()));

                lineNext = bufferedReader.readLine();
                if (lineNext.equals("CUSTOMERLIST")) {
                    ArrayList<Customer> customerList = currentStore.getCustomers();
                    //customerList
                    while (true) {
                        lineNext = bufferedReader.readLine();
                        if (lineNext.equals("--------")) {
                            break;
                        } else {
                            String[] customerDetails = lineNext.split(",");
                            for (User customerpotential : customersList) {
                                if (customerpotential.getName().equals(customerDetails[0]) && customerpotential.getEmail().equals(customerDetails[1])) {
                                    customerList.add((Customer) customerpotential);
                                    break;
                                }
                            }
                        }
                    }
                    currentStore.setCustomers(customerList);
                    lineNext = bufferedReader.readLine();
                    isAtDashline = false;
                }
                if (lineNext == null) {
                    storesList.add(currentStore);
                    currentSeller.addStore(currentStore);
                    break;
                }
                if (lineNext.equals("PRODUCTSLIST")) {
                    //productList
                    while (true) {
                        lineNext = bufferedReader.readLine();
                        if (lineNext.equals("--------")) {
                            isAtDashline = true;
                            break;
                        }
                        String[] productDetails = lineNext.split(";;"); //Now splits if there is ;;
                        Products currentProduct = new Products(productDetails[0], Double.parseDouble(productDetails[1]), Integer.parseInt(productDetails[2]),
                                productDetails[3], Integer.parseInt(productDetails[4]), storeName, Integer.parseInt(productDetails[5]));
                        currentStore.addGoods(currentProduct);
                        productsList.add(currentProduct);
                    }
                }
                storesList.add(currentStore);
                currentSeller.addStore(currentStore);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist!");
        } catch (IOException e) {
            System.out.println("Invalid file format!");
        }

        for (int i = 0; i < customersList.size(); i++) {
            Customer currentCustomer = (Customer) customersList.get(i);
            if (customerTempCart.get(i) != null) {
                String[] customerCartContents = customerTempCart.get(i).split("\n");
                for (String customerCartContent : customerCartContents) {
                    boolean foundItem = false;
                    boolean inStock = false;
                    String[] itemDesc = customerCartContent.split(",");
                    String itemName = itemDesc[0];
                    String storeName = itemDesc[1];
                    int quantity = Integer.parseInt(itemDesc[2]);
                    for (Products productInQuestion : productsList) {
                        if (productInQuestion.getName().equals(itemName) && productInQuestion.getStoreName().equals(storeName)) {
                            inStock = productInQuestion.getQuantity() > 0;
                            foundItem = true;
                        }
                        if (inStock && foundItem) {
                            currentCustomer.addToShoppingCart(productInQuestion, quantity);
                            break;
                        } else if (foundItem && !inStock) {
                            currentCustomer.shoppingCartChangeHelper(itemName, 1);
                            break;
                        }
                    }
                    if (!foundItem) {
                        currentCustomer.shoppingCartChangeHelper(itemName, 2);
                    }
                }
            }
        }
    }

    public static void writeFile() {
        File users = new File("UsersList.txt");
        try {
            FileWriter fileWriter = new FileWriter(users, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            for (User customerUser : customersList) {
                Customer customer = (Customer) customerUser;
                printWriter.println("Customer," + customer.getEmail() + "," + customer.getName() + "," + customer.getPassword());
                if (!customer.getPastPurchase().isEmpty()) {
                    printWriter.println("PAST PURCHASE");
                    for (int i = 0; i < customer.getPastPurchase().size(); i++) {
                        printWriter.println(customer.getPastPurchase().get(i) + "," + customer.getPurchaseCount().get(i));
                    }
                    printWriter.println("--------");
                }
                if (!customer.getShoppingCart().isEmpty()) {
                    printWriter.println("SHOPPING CART");
                    for (int j = 0; j < customer.getShoppingCart().size(); j++) {
                        printWriter.println(customer.getShoppingCart().get(j).getName() + "," +
                                customer.getShoppingCart().get(j).getStoreName() + "," +
                                customer.getCartQuantityList().get(j));
                    }
                    printWriter.println("--------");
                }
                if (customer.getShoppingCart().isEmpty() && customer.getPastPurchase().isEmpty()) {
                    printWriter.println("--------");
                }
                if (!customer.getShoppingCartChanges().isEmpty()) {
                    for (String errorMessages : customer.getShoppingCartChanges()) {
                    }
                }
            }
            for (User seller : sellersList) {
                printWriter.println("Seller," + seller.getEmail() + "," + seller.getName() + "," + seller.getPassword());
                printWriter.println("--------");
            }
            printWriter.close();
        } catch (Exception e) {
            int i = 0;
        }

        File storeFile = new File("StoreList.txt");
        try {
            FileWriter fileWriter = new FileWriter(storeFile, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            for (Store store : storesList) {
                printWriter.println(store.getName());
                printWriter.println(store.getSellerEmail() + "," + store.getSellerName());
                printWriter.println(store.getRevenue());
                printWriter.println(store.getSales());
                if (!store.getCustomers().isEmpty()) {
                    printWriter.println("CUSTOMERLIST");
                    for (Customer customer : store.getCustomers()) {
                        printWriter.println(customer.getName() + "," + customer.getEmail());
                    }
                    printWriter.println("--------");
                }
                if (!store.getGoods().isEmpty()) {
                    printWriter.println("PRODUCTSLIST");
                    for (Products product : store.getGoods()) {
                        printWriter.printf("%s;;%.2f;;%d;;%s;;%d;;%d\n", product.getName(), product.getPrice(),
                                product.getQuantity(), product.getDescription(), product.getSales(),
                                product.getInShoppingCart());
                    }
                    printWriter.println("--------");
                }
                if (store.getCustomers().isEmpty() && store.getGoods().isEmpty()) {
                    printWriter.println("--------");
                }
            }
            printWriter.close();
        } catch (Exception e) {
            System.out.println("Program terminated.");
        }
    }
    public static int checkChoice(String dialogue, Scanner scan, int lastNum) {
        int choice;
        do {
            System.out.println(dialogue);
            try {
                choice = Integer.parseInt(scan.nextLine());
                if (choice > 0 && choice < lastNum + 1) {
                    return choice;
                } else {
                    System.out.println(ENTERVALID);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid option!");
            }
        } while (true);
    }
    public static int checkInt(Scanner scan, String dialogue) {
        int choice;
        do {
            System.out.println(dialogue);
            try {
                choice = Integer.parseInt(scan.nextLine());
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid option!");
            }
        } while (true);
    }
    public static int checkInt(Scanner scan, String dialogue, int maxNumber, String error) {
        int choice;
        do {
            System.out.println(dialogue);
            try {
                choice = Integer.parseInt(scan.nextLine());
                if (choice <= maxNumber && choice >= 0) {
                    return choice;
                } else {
                    System.out.println(error);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        } while (true);
    }

}
