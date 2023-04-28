import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class marketServer implements Runnable {
    public static ArrayList<User> customersList = new ArrayList<>();
    public static ArrayList<User> sellersList = new ArrayList<>();
    public static ArrayList<Store> storesList = new ArrayList<>(); //arrayList of stores in the marketplace
    public static ArrayList<Products> productsList = new ArrayList<>();
    public static ArrayList<String> customerTempCart = new ArrayList<>();
    Socket socket;
    public marketServer(Socket socket){
        this.socket = socket;
    }
    public static Products productString(String strings) {
        String[] stringsplit = strings.split(";;");
        return(new Products(stringsplit[0], Double.parseDouble(stringsplit[4]), Integer.parseInt(stringsplit[3]), stringsplit[1], 0, stringsplit[2],0 ));
    }
    public static void readFile() {
        File users = new File("UsersList.txt");
        try {
            FileReader fileReader = new FileReader(users);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            boolean customerOverFlowed = true;
            String infoType = null;
            String lineNext = "";
            customersList.clear();
            sellersList.clear();
            storesList.clear();
            productsList.clear();
            System.out.println(productsList.toString());
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
                        if (inStock) {
                            currentCustomer.addToShoppingCart(productInQuestion, quantity);
                            break;
                        } else {
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
        productsList.clear();
    }
    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(4242);
        System.out.println("Waiting for connection on 4242");
        while (true){
            Socket socket = serverSocket.accept();
            marketServer server = new marketServer(socket);
            new Thread(server).start();
        }
    }
    public void run() {
        try {
            System.out.println("accepted");
            System.out.println("accepted");
            String searchKeyword;
            StringBuilder searchResult = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("bufferedreader");
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            System.out.println("writer");
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("ois");
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            System.out.println("got to end of streams");

            readFile();
            User[] currentUser = new User[1];
            if (reader.readLine().equals("EXISTING USER")) {
                System.out.println("Existing user");
                boolean userExists = false;
                do {
                    String email = reader.readLine();
                    String password = reader.readLine();
                    for (User users : customersList) {
                        if (users.getEmail().equals(email) && users.getPassword().equals(password)) {
                            writer.println("CUSTOMER");
                            writer.flush();
                            currentUser[0] = (Customer) users;
                            userExists = true;
                            break;
                        }
                    }
                    if (!userExists) {
                        for (User users : sellersList) {
                            if (users.getEmail().equals(email) && users.getPassword().equals(password)) {
                                writer.println("SELLER");
                                writer.flush();
                                currentUser[0]  = (Seller) users;
                                userExists = true;
                                break;
                            }
                        }
                    }
                    if (!userExists) { //no user was found
                        writer.println("NO USER");
                        writer.flush();
                        if (reader.readLine().equals("NO")) {
                            //serverSocket.close();
                            writeFile();
                            return;
                        } else {
                            System.out.println("try again!");
                        }
                    }
                } while (!userExists);
            } else {  //CREATE ACCOUNT
                //during 'else' statement it reads 'NEW USER'
                System.out.println("new user");
                int type = Integer.parseInt(reader.readLine());
                String name = reader.readLine();
                System.out.println("Name: " + name);
                boolean doesEmailExist = false;
                String checkContinue = null;
                String email = null;
                do {
                    email = reader.readLine();
                    System.out.println("email: " + email);
                    doesEmailExist = doesEmailExist(email);
                    if (doesEmailExist) {
                        writer.println("ERROR");
                        writer.flush();
                        System.out.println("sent 'ERROR' to server");

                        checkContinue = reader.readLine();

                        if (checkContinue.equals("NO")) {
                            writer.close();
                            reader.close();
                            ois.close();
                            oos.close();
                            //serverSocket.close();
                            writeFile();
                            return;
                        } else if (checkContinue.equals("CONTINUE")) {
                            System.out.println("continue");
                            continue;
                        }
                    } else {
                        writer.println("SUCCESS");
                        writer.flush();
                    }
                } while (doesEmailExist);

                String password = reader.readLine();
                if (type == 1) {
                    currentUser[0] = new Customer(email, name, password);
                    customersList.add((Customer) currentUser[0]);
                } else {
                    currentUser[0] = new Seller(email, name, password);
                    sellersList.add((Seller) currentUser[0]);
                }
            }
            //Enter main menu

            //CUSTOMER
            if (currentUser[0].getClass() == Customer.class) {
                Customer current = (Customer) currentUser[0];
                boolean mainmenu = true;
                /**
                 * 1. view / edit your acc
                 * 2. view farmer's market <<holy shit
                 * 3. quit
                 * **/
                do {
                    switch (Integer.parseInt(reader.readLine())) {
                        case 1:    //view/edit your acc
                            boolean editAcc = true;
                            do {
                                writer.println(current.getName());
                                writer.flush();
                                writer.println(current.getEmail());
                                writer.flush();
                                writer.println(current.getPassword());
                                writer.flush();
                                switch (Integer.parseInt(reader.readLine())) {
                                    case 1:
                                        String name = reader.readLine();
                                        current.setName(name);
                                        System.out.println("set name to " + name);
                                        editAcc = false;
                                        break;
                                    case 2:
                                        do {
                                            String email = reader.readLine();
                                            if (doesEmailExist(email)) {
                                                writer.println("ERROR");
                                                writer.flush();
                                                if (reader.readLine().equals("NO")) {
                                                    break;
                                                }
                                            } else {
                                                current.setEmail(email);
                                                writer.println("SUCCESS");
                                                writer.flush();
                                                break;
                                            }
                                        } while (true);
                                        editAcc = false;
                                        break;
                                    case 3:
                                        String password = reader.readLine();
                                        current.setPassword(password);
                                        editAcc = false;
                                        break;
                                    case 4:
                                        customersList.remove(current);
                                        writeFile();
                                        socket.close();
                                        break;
                                    case 5:
                                        editAcc = false;
                                }
                            } while (editAcc);
                            break;
                        //expects:
                        /**
                         * 1. Integer for choice prompt from 1~5
                         * 1-1 :: change name
                         *        String of name
                         * 1-2 :: change email
                         *        String of email
                         *        passes 2 :: if user decides not to continue, NO
                         * 1-3 :: change password
                         *        String of password
                         * everything else does not require additional prompts
                         * **/
                        //passes:
                        /**
                         * 1. string formatted "%s,%s,%s" for name, email, password of current user
                         * 2. expects 1-2:: if email exists, ERROR
                         *                  if email does not exist, SUCCESS
                         * **/
                        case 2:
                            boolean marketPlace = true;
                            //view farmers market
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
                            do {
                                int menuChoice = Integer.parseInt(reader.readLine());
                                System.out.println("Option chosen: " + menuChoice);
                                switch (menuChoice) {
                                    case 1: // VIEW PRODUCTS
                                        readFile();
                                        for (Products product : productsList) {
                                            oos.writeObject(product);
                                            oos.flush();
                                            System.out.println(product.getName());
                                        }
                                        oos.writeObject("END");
                                        oos.flush();

                                        int marketChoice;
                                        String listingAction = reader.readLine();
                                        if (listingAction.equals("BACK")) {
                                            continue;
                                        }
                                        marketChoice = Integer.parseInt(listingAction);
                                        readFile();
                                        if (marketChoice <= productsList.size()) {
                                            System.out.println("product chosen: " + productsList.get(marketChoice).getName());
                                            Products productOfChoice = productsList.get(marketChoice);  //They already have name, storename, description, quantity and price
                                            /**
                                             * 1. add one to cart
                                             * 2. purchase now
                                             * 3. go back to products lists
                                             * **/
                                            marketChoice = Integer.parseInt(reader.readLine());
                                            System.out.println("Option chosen: " + marketChoice);
                                            switch (marketChoice) {
                                                case 1:
                                                    System.out.println("add one to cart");
                                                    if (productOfChoice.getQuantity() >= 1) {
                                                        current.addToShoppingCart(productOfChoice, 1);
                                                        productOfChoice.setInShoppingCart(productOfChoice.getInShoppingCart() + 1);
                                                        System.out.println(productOfChoice.getName() + " transaction finished");
                                                    } else {
                                                        System.out.println("quantity was less than 1, could not add to cart");

                                                    }
                                                    break;
                                                case 0:
                                                    System.out.println("purchase now");
                                                    int quantity = Integer.parseInt(reader.readLine());
                                                    if (productOfChoice.getQuantity() > quantity) {
                                                        productOfChoice.setQuantity(productOfChoice.getQuantity() - quantity);
                                                        productOfChoice.setSales(productOfChoice.getSales() + quantity);
                                                        current.addProducts(productOfChoice.getName(), quantity);
                                                        writer.println("SUCCESS");
                                                        writer.flush();
                                                    } else {
                                                        writer.println("ERROR");
                                                        writer.flush();
                                                    }
                                                    System.out.println(productOfChoice.getName() + " transaction finished");
                                                    break;
                                                default:
                                                    System.out.println(productOfChoice.getName() + " transaction finished");
                                                    break;
                                            }
                                        } else if (marketChoice == productsList.size() + 2) {
                                            //serverSocket.close();
                                            writeFile();
                                            return;
                                        }
                                        break;
                                    //expects:
                                    /**
                                     * 1. integer from n ~ no. of products + 2
                                     *  1-1 :: if index from n ~ no. of products
                                     *      - integer from 1~3
                                     *      1-1-2 :: if customer desires to purchase it now
                                     *          - integer for quantity
                                     * **/
                                    //passes:
                                    /**
                                     * 1. n products using ObjectOutputStream
                                     * 2. END for end of products
                                     * 3. expects 1-1-2 :: SUCCESS or ERROR depending on the quantity of the product
                                     * **/
                                    case 2:    //searchProducts
                                        do {
                                            String keyword = reader.readLine();
                                            ArrayList<Products> searchResults = new ArrayList<>();
                                            readFile();
                                            for (Products product : productsList) {
                                                if (product.getName().contains(keyword) ||
                                                        product.getDescription().contains(keyword) ||
                                                        product.getStoreName().contains(keyword)) {
                                                    searchResults.add(product);
                                                }
                                            }
                                            if (!searchResults.isEmpty()) {
                                                for (Products foundProduct : searchResults) {
                                                    writer.println(foundProduct.getName());
                                                    writer.flush();
                                                    System.out.println(foundProduct.getName());
                                                }
                                                writer.println("END");
                                                writer.flush();
                                                int searchIndex = Integer.parseInt(reader.readLine());
                                                System.out.println("Product " + searchIndex);
                                                do {
                                                    if (searchIndex <= searchResults.size()) {
                                                        Products product = searchResults.get(searchIndex - 1);
                                                        oos.writeObject(product);
                                                        oos.flush();
                                                        switch (Integer.parseInt(reader.readLine())) {
                                                            case 1:
                                                                if (product.getQuantity() >= 1) {
                                                                    current.addToShoppingCart(product, 1);
                                                                    product.setInShoppingCart(product.getInShoppingCart() + 1);
                                                                    writer.println("SUCCESS");
                                                                    writer.flush();
                                                                } else {
                                                                    writer.println("FAILED");
                                                                    writer.flush();
                                                                }
                                                                break;
                                                            case 2:
                                                                do {
                                                                    int quantity = Integer.parseInt(reader.readLine());
                                                                    if (product.getQuantity() >= quantity) {
                                                                        product.setQuantity(product.getQuantity() - quantity);
                                                                        writer.println("SUCCESS");  //is in stock
                                                                        writer.flush();
                                                                        break;
                                                                    } else {
                                                                        writer.println("FAILED"); //out of stock, assuming that the user does not get to back out
                                                                        writer.flush();
                                                                        break;
                                                                    }
                                                                } while (true);
                                                            default:
                                                                break;
                                                        }
                                                        break;
                                                    } //no else statement, hope market GUI prompts the user for invalid stuff
                                                } while (true);
                                            } else {
                                                writer.println("NO SEARCH RESULTS");
                                                writer.flush();
                                            }
                                            if (!reader.readLine().equals("YES")) {
                                                break;
                                            }
                                        } while (true);
                                        break;
                                    //expects:
                                    /**
                                     * 1. String keyword for search prompt
                                     *
                                     * Passes 1-1 :: should the search result exist
                                     * 2. integer of index for product the user desires to get details of
                                     * 3. integer of choice among 1~3
                                     *  3-2 :: should the customer decide to purchase right now
                                     *      - integer for quantity of purchase
                                     * **/
                                    //passes:
                                    /**
                                     * 1-1 :: search results exist:
                                     *  - n product names in String!
                                     *  - END for end of products
                                     *      1-1-1 :: user chooses a product
                                     *          - Products of given integer using ObjectOutputStream
                                     *          expects 3-2 :: user purchases a product
                                     *          - SUCCESS if product is in stock
                                     *          - FAILED if product is not in stock
                                     * 1-2 :: search result == null:
                                     *  - NO SEARCH RESULTS passed
                                     * **/
                                    case 3: // SORT BY PRICE
                                        ArrayList<Products> tempArr = new ArrayList<>();
                                        double min;
                                        int minInd;

                                        readFile();
                                        while (!productsList.isEmpty()) { //Empties out the entire list, sorting each removed element into the temp array
                                            min = productsList.get(0).getPrice();
                                            minInd = 0;

                                            //For loop through all products - find minimum price, then add that to the sorted
                                            //array and remove it from the original array
                                            //readFile();
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
                                        for (Products product : productsList) {
                                            oos.writeObject(product);
                                            oos.flush();
                                            System.out.println(product.getName());
                                        }
                                        oos.writeObject("END");
                                        oos.flush();

                                        listingAction = reader.readLine();
                                        if (listingAction.equals("BACK")) {
                                            continue;
                                        }
                                        marketChoice = Integer.parseInt(listingAction);
                                        readFile();
                                        if (marketChoice <= productsList.size()) {
                                            System.out.println("product chosen: " + productsList.get(marketChoice).getName());
                                            Products productOfChoice = productsList.get(marketChoice);  //They already have name, storename, description, quantity and price
                                            /**
                                             * 1. add one to cart
                                             * 2. purchase now
                                             * 3. go back to products lists
                                             * **/
                                            marketChoice = Integer.parseInt(reader.readLine());
                                            System.out.println("Option chosen: " + marketChoice);
                                            switch (marketChoice) {
                                                case 1:
                                                    System.out.println("add one to cart");
                                                    if (productOfChoice.getQuantity() >= 1) {
                                                        current.addToShoppingCart(productOfChoice, 1);
                                                        productOfChoice.setInShoppingCart(productOfChoice.getInShoppingCart() + 1);
                                                        System.out.println(productOfChoice.getName() + " transaction finished");
                                                    } else {
                                                        System.out.println("quantity was less than 1, could not add to cart");

                                                    }
                                                    break;
                                                case 0:
                                                    System.out.println("purchase now");
                                                    int quantity = Integer.parseInt(reader.readLine());
                                                    if (productOfChoice.getQuantity() > quantity) {
                                                        productOfChoice.setQuantity(productOfChoice.getQuantity() - quantity);
                                                        productOfChoice.setSales(productOfChoice.getSales() + quantity);
                                                        current.addProducts(productOfChoice.getName(), quantity);
                                                        writer.println("SUCCESS");
                                                        writer.flush();
                                                    } else {
                                                        writer.println("ERROR");
                                                        writer.flush();
                                                    }
                                                    System.out.println(productOfChoice.getName() + " transaction finished");
                                                    break;
                                                default:
                                                    System.out.println(productOfChoice.getName() + " transaction finished");
                                                    break;
                                            }
                                        } else if (marketChoice == productsList.size() + 2) {
                                            //serverSocket.close();
                                            writeFile();
                                            return;
                                        }
                                        break;
                                    case 4: // SORT BY QUANTITY
                                        tempArr = new ArrayList<>();
                                        readFile();
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
                                        for (Products product : productsList) {
                                            oos.writeObject(product);
                                            oos.flush();
                                            System.out.println(product.getName());
                                        }
                                        oos.writeObject("END");
                                        oos.flush();

                                        listingAction = reader.readLine();
                                        if (listingAction.equals("BACK")) {
                                            continue;
                                        }
                                        marketChoice = Integer.parseInt(listingAction);
                                        readFile();
                                        if (marketChoice <= productsList.size()) {
                                            System.out.println("product chosen: " + productsList.get(marketChoice).getName());
                                            Products productOfChoice = productsList.get(marketChoice);  //They already have name, storename, description, quantity and price
                                            /**
                                             * 1. add one to cart
                                             * 2. purchase now
                                             * 3. go back to products lists
                                             * **/
                                            marketChoice = Integer.parseInt(reader.readLine());
                                            System.out.println("Option chosen: " + marketChoice);
                                            switch (marketChoice) {
                                                case 1:
                                                    System.out.println("add one to cart");
                                                    if (productOfChoice.getQuantity() >= 1) {
                                                        current.addToShoppingCart(productOfChoice, 1);
                                                        productOfChoice.setInShoppingCart(productOfChoice.getInShoppingCart() + 1);
                                                        System.out.println(productOfChoice.getName() + " transaction finished");
                                                    } else {
                                                        System.out.println("quantity was less than 1, could not add to cart");

                                                    }
                                                    break;
                                                case 0:
                                                    System.out.println("purchase now");
                                                    int quantity = Integer.parseInt(reader.readLine());
                                                    if (productOfChoice.getQuantity() > quantity) {
                                                        productOfChoice.setQuantity(productOfChoice.getQuantity() - quantity);
                                                        productOfChoice.setSales(productOfChoice.getSales() + quantity);
                                                        current.addProducts(productOfChoice.getName(), quantity);
                                                        writer.println("SUCCESS");
                                                        writer.flush();
                                                    } else {
                                                        writer.println("ERROR");
                                                        writer.flush();
                                                    }
                                                    System.out.println(productOfChoice.getName() + " transaction finished");
                                                    break;
                                                default:
                                                    System.out.println(productOfChoice.getName() + " transaction finished");
                                                    break;
                                            }
                                        } else if (marketChoice == productsList.size() + 2) {
                                            //serverSocket.close();
                                            writeFile();
                                            return;
                                        }
                                        break;
                                    case 5:
                                        for (String pastPurchases : current.getPastPurchase()) {
                                            writer.println(pastPurchases.split(",")[0]);
                                            writer.flush();
                                            System.out.println(pastPurchases);
                                        }
                                        writer.println("END");
                                        writer.flush();
                                        break;
                                    //passes:
                                    /**
                                     * 1. n String of products' name
                                     * 2. END for end of products
                                     * **/
                                    case 6:
                                        for (Products cartItem : current.getShoppingCart()) {
                                            oos.writeObject(cartItem);
                                            oos.flush();
                                            System.out.println(cartItem.getName());
                                        }
                                        oos.writeObject("END");
                                        oos.flush();
                                        //passes:
                                        /**
                                         * 1. n Products using ObjectOutputStream
                                         * 2. String END
                                         * so I kinda thought about this
                                         *
                                         * in client side:
                                         * ArrayList<Products> productListTemp = new ArrayList<>();
                                         * do {
                                         *     try {
                                         *         Object o = ois.readObject();
                                         *         String exitLine = (String) o;
                                         *         if (existLine.equals("END") {
                                         *             break;
                                         *         } else {
                                         *             productListTemp.add((Products) o);
                                         *         }
                                         *     } catch(Exception e) {
                                         *         break;
                                         *     }
                                         * } while(true);
                                         * **/

                                        /**
                                         * What would you like to do?
                                         * 1. Purchase cart
                                         * 2. Remove an item
                                         * 3. Return to Market Menu
                                         * **/
                                        if (!current.getShoppingCart().isEmpty()) {
                                            switch (Integer.parseInt(reader.readLine())) {
                                                case 1:
                                                    for (Products cartItem : current.getShoppingCart()) {
                                                        if (cartItem.getQuantity() > 1) {
                                                            cartItem.setQuantity(cartItem.getQuantity() - 1);
                                                            cartItem.setInShoppingCart(cartItem.getInShoppingCart() - 1);
                                                            cartItem.setSales(cartItem.getSales() + 1);

                                                            current.removeFromShoppingCart(cartItem);
                                                            current.addProducts(cartItem.getName(), 1);
                                                        } else {
                                                            writer.println(cartItem.getName());
                                                            writer.flush();
                                                        }
                                                    }
                                                    writer.println("SUCCESS");
                                                    writer.flush();
                                                    break;
                                                //passes:
                                                /**
                                                 * 1. if some products are out of stock, this passes n Strings of products' name
                                                 * 2. SUCCESS
                                                 *
                                                 * StringBuilder failedItems = new StringBuilder();
                                                 * do {
                                                 *     String potentialFailure = reader.readLine();
                                                 *     if (potentialFailure.equals("SUCCESS")) {
                                                 *         break;
                                                 *     } else {
                                                 *         failedItems.append(potentialFailure);
                                                 *         failedItems.append(", ");
                                                 *     }
                                                 * } while(true);
                                                 * ...
                                                 * if (!failedItems.isempty()) {
                                                 *     failedItems.delete(failedItems.length() - 2, failedItems.length());
                                                 *     String errorMessage = String.format("The items %s is out of stock.", failedItems.toString());
                                                 *     JOptionPane.showMessageDialog(null, errorMessage, "title", JOptionPane.ERROR_MESSAGE);
                                                 * }
                                                 * **/
                                                case 2:
                                                    try {
                                                        int indexNo = Integer.parseInt(reader.readLine()) - 1;
                                                        System.out.println("recieved " + indexNo);
                                                        if (indexNo != -1) {
                                                            System.out.println(current.getShoppingCart().get(indexNo));
                                                            current.removeFromShoppingCart(current.getShoppingCart().get(indexNo));
                                                        }
                                                    } catch (Exception e) {
                                                        System.out.println("caught an exception");
                                                        break;
                                                    }
                                                    break;
                                                //expects:
                                                /**
                                                 * 1. integer index for deleting product
                                                 * **/
                                                //passes:
                                                /**
                                                 * 1. SUCCESS
                                                 * **/
                                                default:
                                                    break;
                                            }
                                            break;
                                        }
                                        break;
                                    case 7:
                                        marketPlace = false;
                                        break;
                                    case 8:
                                        marketPlace = false;
                                        mainmenu = false;
                                        //serverSocket.close();
                                        break;
                                }
                            } while (marketPlace);
                            break;
                        case 3:
                            //serverSocket.close();
                            mainmenu = false;
                            break;
                    }
                } while (mainmenu);
            } else if (currentUser[0].getClass() == Seller.class) {
                Seller current = (Seller) currentUser[0];
                boolean mainmenu = true;
                do {
                    /**
                     * Main Menu
                     * 1. View/edit your account
                     * 2. View farmer's market
                     * 3. Quit
                     * **/
                    switch (Integer.parseInt(reader.readLine())) {
                        case 1 : //1. View/edit your account
                            System.out.println("Option 1: View/edit your account");
                            boolean editAcc = true;
                            do {
                                writer.println(current.getName());
                                writer.flush();
                                writer.println(current.getEmail());
                                writer.flush();
                                writer.println(current.getPassword());
                                writer.flush();
                                /**
                                 * 1. Edit your name
                                 * 2. Edit your email
                                 * 3. Edit your password
                                 * 4. Delete your account
                                 * 5. Return to main menu
                                 * **/
                                switch (Integer.parseInt(reader.readLine())) {
                                    case 1 :
                                        current.setName(reader.readLine());
                                        writer.println("SUCCESS");
                                        writer.flush();
                                        break;
                                    case 2 :
                                        do {
                                            String newEmail = reader.readLine();
                                            if (doesEmailExist(newEmail)) {
                                                writer.println("FAILED");
                                                writer.flush();
                                            } else {
                                                current.setEmail(newEmail);
                                                writer.println("SUCCESS");
                                                writer.flush();
                                                break;
                                            }
                                        } while(true);
                                        break;
                                    case 3 :
                                        current.setPassword(reader.readLine());
                                        writer.println("SUCCESS");
                                        writer.flush();
                                        break;
                                    case 4 :
                                        sellersList.remove((User) current);
                                        //serverSocket.close();
                                        editAcc = false;
                                        mainmenu = false;
                                        break;
                                    case 5 :
                                        editAcc = false;
                                        break;
                                }
                            } while (editAcc);
                        case 2 :    //view farmer's market
                            /**
                             * 1. View booths
                             * 2. Add booth
                             * 3. Edit booth
                             * 4. Remove booth
                             * 5. Go back
                             * **/
                            boolean marketMenu = true;
                            do {
                                switch (Integer.parseInt(reader.readLine())) {
                                    case 1: //1. View booths
                                        for (Store storeInList : current.getStore()) {
                                            writer.println(storeInList.getName());
                                            writer.flush();
                                        }
                                        writer.println("END");
                                        writer.flush();
                                        Store currentStore = current.getStore().get(Integer.parseInt(reader.readLine()) - 1);
                                        /**
                                         * 1. View products
                                         * 2. View sales
                                         * 3. Add product
                                         * 4. Edit product
                                         * 5. Remove product
                                         * 6. Import product csv file
                                         * 7. Export product csv file
                                         * 8. Go back
                                         * **/
                                        boolean boothmenu = true;
                                        do {
                                            switch (Integer.parseInt(reader.readLine())) {
                                                case 1 :    //1. View products
                                                    for (Products productInStore : currentStore.getGoods()) {
                                                        oos.writeObject(productInStore);
                                                        oos.flush();
                                                    }
                                                    oos.writeObject("END OF PRODUCTS");
                                                    oos.flush();
                                                    break;
                                                case 2 :    //2. View sales
                                                    writer.println(String.format("%d", currentStore.getSales()));
                                                    writer.flush();
                                                    break;
                                                case 3 :    //3. Add product
                                                    boolean productImport = true;
                                                    while (productImport) {
                                                        if (reader.readLine().equals("END OF PRODUCT")) {
                                                            productImport = false;
                                                        } else {    //product in %s;;%s;;%s;;%d;;%.2f format
                                                            String newProduct = reader.readLine();
                                                            currentStore.addGoods(new Products(newProduct, 0,
                                                                    0, "Description", 0,
                                                                    currentStore.getName(), 0));
                                                        }
                                                    }
                                                    break;
                                                case 4 :    //4. Edit product
                                                    for (Products products : currentStore.getGoods()) {
                                                        oos.writeObject(products);
                                                        oos.flush();
                                                    }
                                                    oos.writeObject("END");
                                                    oos.flush();
                                                    if (!currentStore.getGoods().isEmpty()) {
                                                        Integer index = Integer.parseInt(reader.readLine()) - 1;
                                                        Products editingProduct = currentStore.getGoods().get(index);
                                                        if (ois.readBoolean()) {    //if changing name
                                                            editingProduct.setName(reader.readLine());
                                                        }
                                                        if (ois.readBoolean()) {    //if changing description
                                                            editingProduct.setDescription(reader.readLine());
                                                        }
                                                        if (ois.readBoolean()) {    //if changing price
                                                            editingProduct.setPrice(Double.parseDouble(reader.readLine()));
                                                        }
                                                        if (ois.readBoolean()) {    //if changing quantity
                                                            editingProduct.setQuantity(Integer.parseInt(reader.readLine()));
                                                        }
                                                        currentStore.getGoods().set(index, editingProduct);
                                                    }
                                                    break;
                                                //expects:
                                                /**
                                                 * 1. integer value of index for product desired to edit(from 1~n)
                                                 * 2. total of 4 boolean values, potentially followed by valid* data
                                                 *      input validation expected from the client side!
                                                 * **/
                                                //passes:
                                                /**
                                                 * 1. n values of product names
                                                 * 2. END
                                                 * **/
                                                case 5 :    //5. Remove product
                                                    for (Products products : currentStore.getGoods()) {
                                                        oos.writeObject(products);
                                                        oos.flush();
                                                    }
                                                    oos.writeObject("END");
                                                    oos.flush();
                                                    if (!currentStore.getGoods().isEmpty()) {
                                                        Integer index = Integer.parseInt(reader.readLine()) - 1;
                                                        currentStore.getGoods().remove(index);
                                                    }
                                                    break;
                                                //expects:
                                                /**
                                                 * integer value of index for product desired to delete(from 1~n)
                                                 * **/
                                                //passes:
                                                /**
                                                 * 1. n values of product names
                                                 * 2. END
                                                 * **/
                                                case 6 :    //6. Import product csv file
                                                    //boolean success = true;
                                                    oos.writeObject(current);
                                                    oos.flush();
                                                    oos.writeObject(currentStore);
                                                    oos.flush();
                                                    try {
                                                        current = (Seller) ois.readObject();
                                                        currentStore = (Store) ois.readObject();
                                                    } catch (Exception e) {}    //??
                                                    break;
                                                //expects:
                                                /**
                                                 * 1. n Products using ObjectInputStream
                                                 * 2. END prompt from the client
                                                 * **/
                                                //passes:
                                                /**
                                                 * if all products file is imported successfully : SUCCESS
                                                 * else : ERROR
                                                 * **/
                                                case 7 :    //7. Export product csv file
                                                    oos.writeObject(currentStore);
                                                    oos.flush();
                                                    break;
                                                //expects
                                                /**
                                                 * desired file name. Are we expecting csv ending? if not...
                                                 * **/
                                                //passes
                                                /**
                                                 * if file written successful : SUCCESS
                                                 * else : ERROR
                                                 * **/
                                                case 8 :    //8. Go back
                                                    boothmenu = false;
                                                    break;
                                            }
                                        } while(boothmenu);
                                    case 2: //2. Add booth
                                        String storeName = reader.readLine();
                                        current.addStore(new Store(storeName, current.getName(), current.getEmail()));
                                        break;
                                    //expects: a store name(String)
                                    case 3: //3. Edit booth
                                        if (current.getStore().isEmpty()) {
                                            writer.println("EMPTY");
                                            writer.flush();
                                        } else {
                                            writer.println("NOT EMPTY");
                                            writer.flush();
                                            for (Store store : current.getStore()) {
                                                writer.println(store.getName());
                                                writer.flush();
                                                System.out.println(store.getName());
                                            }
                                            writer.println("END");
                                            writer.flush();
                                            int index = Integer.parseInt(reader.readLine()) - 1;
                                            current.getStore().get(index).setName(reader.readLine());
                                        }
                                        break;
                                    //expects:
                                    /**
                                     * 1. integer index of store
                                     * 2. String of new name for store
                                     * **/
                                    //passes:
                                    /**
                                     * 1. EMPTY if no store is within seller's directory, NOT EMPTY if else
                                     * ----below are for if NOT EMPTY is passed----
                                     * 2. n String of storeNames
                                     * 3. END for end of stores
                                     * **/
                                    case 4: //4. Remove booth
                                        if (!current.getStore().isEmpty()) {
                                            writer.println("NOT EMPTY");
                                            writer.flush();
                                            for (Store store : current.getStore()) {
                                                writer.println(store.getName());
                                            }
                                            oos.writeObject("END");
                                            oos.flush();
                                            int index = Integer.parseInt(reader.readLine()) - 1;
                                            ArrayList<Store> newList = current.getStore();
                                            newList.remove(index);
                                            current.setStore(newList);
                                        } else {
                                            writer.println("EMPTY");
                                            writer.flush();
                                        }
                                        break;
                                    //expects:
                                    /**
                                     * 1. integer index of store
                                     * **/
                                    //passes:
                                    /**
                                     * 1. EMPTY if no store is within seller's directory, NOT EMPTY if else
                                     * ----below are for if NOT EMPTY is passed----
                                     * 2. n String of storeNames
                                     * 3. END for end of stores
                                     * **/
                                    case 5: //5. Go back
                                        marketMenu = false;
                                        break;
                                }
                            } while(marketMenu);
                            break;
                        case 3:
                            mainmenu = false;
                            break;
                    }
                } while(mainmenu);
            }

            //serverSocket.close();
            ois.close();
            oos.close();
            reader.close();
            writer.close();
            writeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean doesEmailExist(String email) {
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
}
