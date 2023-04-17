import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class marketServer {
    public static ArrayList<User> customersList = new ArrayList<>();
    public static ArrayList<User> sellersList = new ArrayList<>();
    public static ArrayList<Store> storesList = new ArrayList<>(); //arrayList of stores in the marketplace
    public static ArrayList<Products> productsList = new ArrayList<>();
    public static ArrayList<String> customerTempCart = new ArrayList<>();
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
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            Socket socket = serverSocket.accept();
            String searchKeyword;
            StringBuilder searchResult = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            readFile();
            User[] currentUser = new User[1];
            if (reader.readLine().equals("EXISTING USER")) {
                boolean userExists = false;
                do {
                    String email = reader.readLine();
                    String password = reader.readLine();
                    for (User users : customersList) {
                        if (users.getEmail().equals(email) && users.getPassword().equals(password)) {
                            writer.println("CUSTOMER");
                            currentUser[0] = (Customer) users;
                            userExists = true;
                            break;
                        }
                    }
                    if (!userExists) {
                        for (User users : sellersList) {
                            if (users.getEmail().equals(email) && users.getPassword().equals(password)) {
                                writer.println("SELLER");
                                currentUser[0]  = (Seller) users;
                                userExists = true;
                                break;
                            }
                        }
                    }
                    if (!userExists) {
                        if (reader.readLine().equals("NO")) {
                            serverSocket.close();
                            return;
                        }
                    }
                } while (!userExists);
            } else {
                int type = Integer.parseInt(reader.readLine());
                String name = reader.readLine();
                String email = reader.readLine();
                String password = reader.readLine();
                while (doesEmailExist(email)) {
                    if (reader.readLine().equals("NO")) {
                        serverSocket.close();
                        return;
                    }
                    email = reader.readLine();
                }
                if (type == 1) {
                    currentUser[0] = new Customer(email, name, password);
                } else {
                    currentUser[0] = new Seller(email, name, password);
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
                                writer.println(String.format("%s,%s,%s", current.getName(), current.getEmail(), current.getPassword()));
                                switch (Integer.parseInt(reader.readLine())) {
                                    case 1:
                                        current.setName(reader.readLine());
                                        break;
                                    case 2:
                                        do {
                                            String email = reader.readLine();
                                            if (doesEmailExist(email)) {
                                                writer.println("ERROR");
                                                if (reader.readLine().equals("NO")) {
                                                    break;
                                                }
                                            } else {
                                                current.setEmail(email);
                                                writer.println("SUCCESS");
                                                break;
                                            }
                                        } while (true);
                                        break;
                                    case 3:
                                        current.setPassword(reader.readLine());
                                        break;
                                    case 4:
                                        customersList.remove(current);
                                        break;
                                    case 5:
                                        editAcc = false;
                                }
                            } while (editAcc);
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
                                switch (Integer.parseInt(reader.readLine())) {
                                    case 1:
                                        for (Products product : productsList) {
                                            writer.println(String.format("%s;;%s;;%s;;%d;;%.2f", product.getName(),
                                                    product.getStoreName(), product.getDescription(), product.getQuantity(),
                                                    product.getPrice()));
                                        }
                                        writer.println("END OF PRODUCTS");
                                        int marketChoice = Integer.parseInt(reader.readLine()); //assuming it will be given from 1, not index zero
                                        if (marketChoice <= productsList.size()) {
                                            Products productOfChoice = productsList.get(marketChoice - 1);  //They already have name, storename, description, quantity and price
                                            /**
                                             * 1. add one to cart
                                             * 2. purchase now
                                             * 3. go back to products list
                                             * **/
                                            switch (Integer.parseInt(reader.readLine())) {
                                                case 1:
                                                    current.addToShoppingCart(productOfChoice, 1);
                                                    productOfChoice.setInShoppingCart(productOfChoice.getInShoppingCart() + 1);
                                                    break;
                                                case 2:
                                                    int quantity = Integer.parseInt(reader.readLine());
                                                    productOfChoice.setQuantity(productOfChoice.getQuantity() - quantity);
                                                    productOfChoice.setSales(productOfChoice.getSales() + quantity);
                                                    break;
                                                default:
                                                    break;
                                            }
                                        } else if (marketChoice == productsList.size() + 2) {
                                            serverSocket.close();
                                            return;
                                        }
                                        break;
                                    case 2:    //searchProducts
                                        do {
                                            String keyword = reader.readLine();
                                            ArrayList<Products> searchResults = new ArrayList<>();
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
                                                }
                                                writer.println("END OF PRODUCTS");
                                                int searchIndex = Integer.parseInt(reader.readLine());
                                                do {
                                                    if (searchIndex <= searchResults.size()) {
                                                        Products product = searchResults.get(searchIndex - 1);
                                                        writer.println(String.format("%s;;%s;;%s;;%d;;%.2f", product.getName(),
                                                                product.getStoreName(), product.getDescription(), product.getQuantity(),
                                                                product.getPrice()));
                                                        switch (Integer.parseInt(reader.readLine())) {
                                                            case 1:
                                                                current.addToShoppingCart(product, 1);
                                                                product.setInShoppingCart(product.getInShoppingCart() + 1);
                                                                break;
                                                            case 2:
                                                                do {
                                                                    int quantity = Integer.parseInt(reader.readLine());
                                                                    if (product.getQuantity() >= quantity) {
                                                                        product.setQuantity(product.getQuantity() - quantity);
                                                                        writer.println("SUCCESS");  //is in stock
                                                                        break;
                                                                    } else {
                                                                        writer.println("FAILURE"); //out of stock, assuming that the user does not get to back out
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
                                            }
                                            if (!reader.readLine().equals("YES")) {
                                                break;
                                            }
                                        } while (true);
                                        break;
                                    case 3:
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
                                        break;
                                    case 4:
                                        tempArr = new ArrayList<>();
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
                                        break;
                                    case 5:
                                        for (String pastPurchases : current.getPastPurchase()) {
                                            writer.println(pastPurchases.split(",")[0]);
                                        }
                                        writer.println("END OF PRODUCTS");
                                        break;
                                    case 6:
                                        for (Products cartItem : current.getShoppingCart()) {
                                            writer.println(String.format("%s;;%s;;%.2f", cartItem.getName(),
                                                    cartItem.getDescription(), cartItem.getPrice()));
                                        }
                                        writer.println("END OF PRODUCTS");
                                        /**
                                         * What would you like to do?
                                         * 1. Purchase cart
                                         * 2. Remove an item
                                         * 3. Return to Market Menu
                                         * **/
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
                                                        current.shoppingCartChangeHelper(cartItem.getName(), 1);
                                                        //TODO: Discuss how we will deal with multiple error messages with GUI
                                                    }
                                                }
                                                writer.println("SUCCESS");
                                                break;
                                            case 2:
                                                do {
                                                    int indexNo = Integer.parseInt(reader.readLine());
                                                    if (indexNo <= current.getShoppingCart().size()) {
                                                        current.removeFromShoppingCart(current.getShoppingCart().get(indexNo));
                                                        writer.println("SUCCESS");
                                                        break;
                                                    } else {
                                                        writer.println("ERROR");    //TODO: PROMPT USER TO ENTER NO. AGAIN
                                                    }
                                                } while (true);
                                                break;
                                            default:
                                                break;
                                        }
                                        break;
                                    case 7:
                                        marketPlace = false;
                                        break;
                                    case 8:
                                        marketPlace = false;
                                        mainmenu = false;
                                        serverSocket.close();
                                        break;
                                }
                            } while (marketPlace);
                        case 3:
                            serverSocket.close();
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
                            boolean editAcc = true;
                            do {
                                writer.println(String.format("%s,%s,%s", current.getName(), current.getEmail(),
                                        current.getPassword()));
                                /**
                                 * 1. Edit your name
                                 * 2. Edit your email
                                 * 3. Edit your password
                                 * 4. Delete your account
                                 * 5. Return to main menu
                                 * **/
                                switch
                            } while (editAcc);
                    }

                } while(mainmenu);
            }


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
