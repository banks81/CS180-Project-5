
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import javax.swing.JOptionPane;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class marketClientGUI implements Runnable {
    //Welcome
    JFrame welcomeWindow;
    JLabel welcomeTitle;
    JButton login;
    JButton createAccount;

    //Main Menu
    JFrame mainWindow;
    JLabel optionsTitle;
    JButton account;
    JButton viewMarketBtn;
    JButton quit;

    //Account Menu
    JFrame accountWindow;
    JLabel accountHeader;
    JTextField nameField;
    JTextField emailField;
    JTextField passwordField;
    JButton nameEnter;
    JButton emailEnter;
    JButton passwordEnter;
    JButton deleteAccount;
    JButton backToMainFromAccount;

    //Products Menu
    JFrame productsWindow;
    JLabel marketTitle;
    JButton viewListings;
    JButton productSearch;
    JButton priceSort;
    JButton quantitySort;
    JButton viewHistory;
    JButton viewCart;
    JButton backToMainFromMarket;

    //Sellers Menu
    JFrame sellerWindow;
    JLabel sellerTitle;
    JButton viewBooths;
    JButton addBooth;
    JButton editBooth;
    JButton removeBooth;
    JButton backToMainFromSeller;


    //Booth Menu
    JFrame boothWindow;
    JLabel boothTitle;
    JButton viewProducts;
    JButton viewSales;
    JButton addProduct;
    JButton editProduct;
    JButton removeProduct;
    JButton importProduct;
    JButton exportProduct;
    JButton backToSellerFromBooth;


    //Listings Menu
    JFrame listingsWindow;
    JLabel listingsHeader;
    JComboBox productsDropdown;
    JButton viewProduct;
    JButton backToMarketFromListings;
    private ArrayList<Products> productsList;
    private ArrayList<Products> shoppingCart;
    private ArrayList<Products> pastPurchases;
    private String[] productsArray = new String[0];

    //Client stuff
    public static Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    int userType; //customer = 1; seller = 0



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new marketClientGUI());

    }

    public void endProgram() {
        //Close all windows
        mainWindow.setVisible(false);
        welcomeWindow.setVisible(false);
        accountWindow.setVisible(false);
        productsWindow.setVisible(false);
        sellerWindow.setVisible(false);
        listingsWindow.setVisible(false);

        mainWindow.dispose();
        welcomeWindow.dispose();
        accountWindow.dispose();
        productsWindow.dispose();
        sellerWindow.dispose();
        listingsWindow.dispose();

        try {
            oos.close();
            ois.close();
            writer.close();
            reader.close();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void run() {

        //Client Connection
        try {
            socket = new Socket("localhost", 4242);
            System.out.println("got past socket");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("reader");
            writer = new PrintWriter(socket.getOutputStream());
            System.out.println("writer");
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("reached end of socket stuff");
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("ois");


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Classnotfoundexception");
        }

        //WELCOME MENU SETUP
        //Create Objects for screen
        welcomeWindow = new JFrame("Farmer's Market Welcome Menu");
        Container welcomeContent = welcomeWindow.getContentPane();
        welcomeTitle = new JLabel("Welcome!");
        welcomeTitle.setFont(new Font("Serif", Font.PLAIN, 30));
        login = new JButton("Login");
        createAccount = new JButton("Create Account");
        welcomeContent.setLayout(new BorderLayout());
        //Header
        JPanel welcomeTitlePanel = new JPanel();
        welcomeTitlePanel.add(welcomeTitle);
        welcomeContent.add(welcomeTitlePanel, BorderLayout.NORTH);
        //Buttons
        JPanel welcomeButtonPanel = new JPanel();
        welcomeButtonPanel.add(createAccount, BorderLayout.CENTER);
        welcomeButtonPanel.add(login, BorderLayout.CENTER);
        welcomeContent.add(welcomeButtonPanel, BorderLayout.CENTER);
        //General Config
        welcomeWindow.setSize(500, 300);
        welcomeWindow.setLocationRelativeTo(null);
        welcomeWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        welcomeWindow.setVisible(true);


        //MAIN MENU SETUP
        //Create Objects for screen
        mainWindow = new JFrame("Farmer's Market Main Menu");
        Container mainContent = mainWindow.getContentPane();
        mainContent.setLayout(new BorderLayout());
        optionsTitle = new JLabel("Options:");
        optionsTitle.setFont(new Font("Serif", Font.PLAIN, 30));
        account = new JButton("View or Edit Account");
        viewMarketBtn = new JButton("View Farmer's Market");
        quit = new JButton("Quit Program");
        mainContent.add(quit, BorderLayout.SOUTH);
        //Header
        JPanel mainTitlePanel = new JPanel();
        mainTitlePanel.add(optionsTitle);
        mainContent.add(mainTitlePanel, BorderLayout.NORTH);
        //Buttons
        JPanel mainButtonPanel = new JPanel();
        mainButtonPanel.add(account, BorderLayout.CENTER);
        mainButtonPanel.add(viewMarketBtn, BorderLayout.CENTER);
        mainContent.add(mainButtonPanel, BorderLayout.CENTER);
        //General Config
        mainWindow.setSize(500, 300);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        mainWindow.setVisible(false);


        //ACCOUNT MENU SETUP
        accountWindow = new JFrame("Farmer's Market Account Menu");
        Container accountContent = accountWindow.getContentPane();
        accountContent.setLayout(new BorderLayout());
        //Create objects for screen
        accountHeader = new JLabel("Account Information");
        accountHeader.setFont(new Font("Serif", Font.PLAIN, 24));
        nameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JTextField();
        nameEnter = new JButton("Enter");
        nameEnter.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));
        emailEnter = new JButton("Enter");
        passwordEnter = new JButton("Enter");
        deleteAccount = new JButton("Delete Account");
        backToMainFromAccount = new JButton("Back");
        accountContent.add(backToMainFromAccount, BorderLayout.SOUTH);
        //Paneling for organization
        JPanel accountHeaderPnl = new JPanel();
        accountHeaderPnl.add(accountHeader);
        JPanel accountInfoPnl = new JPanel(new GridLayout(3, 2, 10, 15));
        accountInfoPnl.add(nameField);
        accountInfoPnl.add(nameEnter);
        accountInfoPnl.add(emailField);
        accountInfoPnl.add(emailEnter);
        accountInfoPnl.add(passwordField);
        accountInfoPnl.add(passwordEnter);
        accountWindow.add(accountInfoPnl, BorderLayout.CENTER);
        accountWindow.add(accountHeaderPnl, BorderLayout.NORTH);
        //General Config
        accountWindow.setSize(500, 300);
        accountWindow.setLocationRelativeTo(null);
        accountWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        accountWindow.setVisible(false);



        //PRODUCTS MENU SETUP
        productsWindow = new JFrame("Farmer's Market Products Menu");
        Container productsContent = productsWindow.getContentPane();
        productsContent.setLayout(new BorderLayout());
        //Create Objects
        marketTitle = new JLabel("The Farmer's Market");
        marketTitle.setFont(new Font("Serif", Font.PLAIN, 24));
        viewListings = new JButton("View Product Listings");
        productSearch = new JButton("Search Products");
        priceSort = new JButton("Sort Products by Price");
        quantitySort = new JButton("Sort Products by Quantity");
        viewHistory = new JButton("View Purchase History");
        viewCart = new JButton("View Shopping Cart");
        backToMainFromMarket = new JButton("Back");
        //Paneling for organization
        JPanel marketHeaderPnl = new JPanel();
        marketHeaderPnl.add(marketTitle);
        JPanel marketButtonsPnl = new JPanel(new GridLayout(3, 2, 10, 15));
        marketButtonsPnl.add(viewListings);
        marketButtonsPnl.add(productSearch);
        marketButtonsPnl.add(priceSort);
        marketButtonsPnl.add(quantitySort);
        marketButtonsPnl.add(viewHistory);
        marketButtonsPnl.add(viewCart);
        productsWindow.add(marketButtonsPnl, BorderLayout.CENTER);
        productsWindow.add(marketHeaderPnl, BorderLayout.NORTH);
        productsWindow.add(backToMainFromMarket, BorderLayout.SOUTH);
        //General Config
        productsWindow.setSize(500, 300);
        productsWindow.setLocationRelativeTo(null);
        productsWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        productsWindow.setVisible(false);

        //SELLER MENU SETUP
        sellerWindow = new JFrame("Farmer's Market Seller Menu");
        Container sellerContent = sellerWindow.getContentPane();
        sellerContent.setLayout(new BorderLayout());
        JPanel sellerHeaderPnl = new JPanel();
        JPanel sellerBtnPnl = new JPanel(new GridLayout(2, 2, 10, 15));
        //Create Objects
        sellerTitle = new JLabel("Seller Option");
        viewBooths = new JButton("View your Booths");
        addBooth = new JButton("Add a Booth");
        editBooth = new JButton("Edit a Booth");
        removeBooth = new JButton("Remove a Booth");
        backToMainFromSeller = new JButton("Back");
        //Paneling for organization
        sellerHeaderPnl.add(sellerTitle);
        sellerContent.add(sellerHeaderPnl, BorderLayout.NORTH);
        sellerBtnPnl.add(viewBooths);
        sellerBtnPnl.add(addBooth);
        sellerBtnPnl.add(editBooth);
        sellerBtnPnl.add(removeBooth);
        sellerContent.add(sellerBtnPnl, BorderLayout.CENTER);
        sellerContent.add(backToMainFromSeller, BorderLayout.SOUTH);
        //General Config
        sellerWindow.setSize(500, 300);
        sellerWindow.setLocationRelativeTo(null);
        sellerWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        sellerWindow.setVisible(false);



        //BOOTH MENU SETUP
        boothWindow = new JFrame("Booth View");
        Container boothContent = boothWindow.getContentPane();
        boothContent.setLayout(new BorderLayout());
        JPanel boothHeaderPnl = new JPanel();
        JPanel boothBtnPnl = new JPanel(new GridLayout(4, 2, 10, 15));
        //Create Objects
        boothTitle = new JLabel("Set this later");
        viewProducts = new JButton("View Products");
        viewSales = new JButton("View Sales");
        addProduct = new JButton("Add a Product");
        editProduct = new JButton("Edit a Product");
        removeProduct = new JButton("Remove a Product");
        importProduct = new JButton("Import a Product");
        exportProduct = new JButton("Export a Product");
        backToSellerFromBooth = new JButton("Back");
        //Paneling for organization
        boothHeaderPnl.add(boothTitle);
        boothContent.add(boothHeaderPnl, BorderLayout.NORTH);
        boothBtnPnl.add(viewProducts);
        boothBtnPnl.add(viewSales);
        boothBtnPnl.add(addProduct);
        boothBtnPnl.add(editProduct);
        boothBtnPnl.add(removeProduct);
        boothBtnPnl.add(importProduct);
        boothBtnPnl.add(exportProduct);
        boothContent.add(boothBtnPnl, BorderLayout.CENTER);
        boothContent.add(backToSellerFromBooth, BorderLayout.SOUTH);
        //General Config
        boothWindow.setSize(500, 300);
        boothWindow.setLocationRelativeTo(null);
        boothWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        boothWindow.setVisible(false);


        //LISTINGS MENU SETUP
        listingsWindow = new JFrame("Farmer's Market Listings Menu");
        Container listingsContent = listingsWindow.getContentPane();
        listingsContent.setLayout(new BorderLayout());
        //Create Objects
        listingsHeader = new JLabel("Product Listing:");
        listingsHeader.setFont(new Font("Serif", Font.PLAIN, 24));
        productsDropdown = new JComboBox<>(productsArray);

        viewProduct = new JButton("View This Product");
        backToMarketFromListings = new JButton("Back");
        //Paneling for organization
        JPanel listingsHeaderPnl = new JPanel();
        listingsHeaderPnl.add(listingsHeader);
        JPanel listingsPnl = new JPanel();
        listingsPnl.add(productsDropdown);
        listingsPnl.add(viewProduct);
        listingsContent.add(listingsPnl, BorderLayout.CENTER);
        listingsContent.add(listingsHeaderPnl, BorderLayout.NORTH);
        listingsContent.add(backToMarketFromListings, BorderLayout.SOUTH);
        //General Config
        listingsWindow.setSize(500, 300);
        listingsWindow.setLocationRelativeTo(null);
        listingsWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        listingsWindow.setVisible(false);



        //ACTION LISTENERS

        //WELCOME WINDOW
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                do {
                    String email = JOptionPane.showInputDialog("Enter your email: ");
                    String password = JOptionPane.showInputDialog("Enter your password: ");
                    writer.println("EXISTING USER");
                    writer.flush();
                    writer.println(email);
                    writer.flush();
                    writer.println(password);
                    writer.flush();
                    String keepGoing = null;
                    try {
                        keepGoing = reader.readLine();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (!keepGoing.equals("NO USER")) {
                        if (keepGoing.equals("CUSTOMER")) {
                            userType = 1;
                        } else {
                            userType = 0;
                        }
                        welcomeWindow.setVisible(false);
                        mainWindow.setVisible(true);
                        break;
                    } else {
                        String [] errorDialogButtons = {"Try again", "Quit"};
                        int seller = JOptionPane.showOptionDialog(null, "No user was found with these credentials", "Error",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, errorDialogButtons, errorDialogButtons[0]);
                        if (seller != 0) {
                            writer.println("NO");
                            writer.flush();
                            endProgram();
                            break;
                        }
                    }
                } while (true);
            }
        });
        createAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                writer.println("NEW USER");
                writer.flush();
                //need to write 'type' to the server
                //type = 1, customer
                //type = 0, seller
                //TODO implement creating an account with JOptionPane
                String [] dialogButtons = {"Seller", "Customer"};
                int type = JOptionPane.showOptionDialog(null, "Welcome new user!\nAre you a seller or customer?", "Create Account",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, dialogButtons, dialogButtons[0]);
                writer.println(type);
                writer.flush();
                userType = type;

                String name = JOptionPane.showInputDialog("Welcome new user! \nEnter your name: ");

                writer.println(name);
                writer.flush();

                String success = null;
                int emailFail = -1;
                try {
                    do {
                        String email = JOptionPane.showInputDialog("Welcome new user! \nEnter your email: ");
                        writer.println(email);
                        writer.flush();
                        success = reader.readLine();
                        if (success.equals("ERROR")) {
                            String [] errorDialogButtons = {"Try again", "Quit"};
                            emailFail = JOptionPane.showOptionDialog(null, "This email is already in use.", "Error",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, errorDialogButtons, errorDialogButtons[0]);
                            System.out.println(emailFail);
                            if(emailFail == 1) {
                                writer.println("NO"); //would you like to re enter email? no
                                writer.flush();
                                endProgram();
                                break;
                            } else if (emailFail == 0) {
                                writer.println("CONTINUE");
                                writer.flush();
                            }
                            //TODO fix if user clicks the x

                        } else {
                            break;
                        }

                    } while (true);
                    if (emailFail != 1) {
                        String password = JOptionPane.showInputDialog("Welcome new user! \nEnter your password: ");
                        writer.println(password);
                        writer.flush();
                    } else {
                        writer.println("no password, user quit");
                        writer.flush();
                    }


                } catch (Exception e1) {
                    e1.printStackTrace();
                }


                welcomeWindow.setVisible(false);
                mainWindow.setVisible(true);
            }
        });

        //MAIN WINDOW
        viewMarketBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                writer.println("2");
                writer.flush();

                if (userType == 0) {
                    sellerWindow.setVisible(true);
                } else {
                    productsWindow.setVisible(true);
                }
                mainWindow.setVisible(false);
            }
        });
        account.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: fill in the text fields with updated information
                writer.println("1");
                writer.flush();
                try {
                    String name = reader.readLine();
                    String email = reader.readLine();
                    String password = reader.readLine();
                    nameField.setText(name);
                    emailField.setText(email);
                    passwordField.setText(password);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    //maybe have an error that goes back to the previous page or something
                }
                accountWindow.setVisible(true);
                mainWindow.setVisible(false);
            }
        });

        //ACCOUNT WINDOW
        nameEnter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: update server with new name info
                writer.println("1");
                writer.flush();
                System.out.println("sent 1 to server");
                String name = nameField.getText();
                writer.println(name);
                writer.flush();
                System.out.println("sent " + name + "to server");

                JOptionPane.showMessageDialog(null, "Your name has been changed to " + name, "Change Success!", JOptionPane.INFORMATION_MESSAGE);
                accountWindow.setVisible(false);
                mainWindow.setVisible(true);
            }
        });
        emailEnter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: update server with new email info
                writer.println("2");
                writer.flush();

                String emailPaneAnswer = emailField.getText();
                do {
                    writer.println(emailPaneAnswer);
                    writer.flush();
                    String emailSuccess = null;
                    try {
                        emailSuccess = reader.readLine();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    if (emailSuccess.equals("SUCCESS")) {
                        break;
                    }
                    else {
                        String [] errorDialogButtons = {"Try again", "Cancel Change"};
                        int emailFail = JOptionPane.showOptionDialog(null, "This email is already in use.", "Error",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, errorDialogButtons, errorDialogButtons[0]);
                        if (emailFail == 0) {
                            writer.println("TRY AGAIN");
                            writer.flush();
                            emailPaneAnswer = JOptionPane.showInputDialog("Enter a different email:");
                        } else { //User chooses cancel
                            writer.println("NO");
                            writer.flush();
                            break;
                        }
                    }
                } while (true);

                JOptionPane.showMessageDialog(null, "Your email has been changed to " + emailPaneAnswer, "Change Success!", JOptionPane.INFORMATION_MESSAGE);
                accountWindow.setVisible(false);
                mainWindow.setVisible(true);
            }
        });
        passwordEnter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: update server with new password info
                writer.println("3");
                writer.flush();
                String password = passwordField.getText();
                writer.println(password);
                writer.flush();
                JOptionPane.showMessageDialog(null, "Your password has been changed to " + password, "Change Success!", JOptionPane.INFORMATION_MESSAGE);
                accountWindow.setVisible(false);
                mainWindow.setVisible(true);
            }
        });


        //MARKET WINDOW (productsWindow)
        viewListings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                writer.println("1");
                writer.flush();

                //Reset information
                productsList = new ArrayList<>();
                productsList.clear();
                listingsPnl.remove(productsDropdown);
                listingsPnl.remove(viewProduct);
                productsDropdown = new JComboBox();
                listingsPnl.add(productsDropdown);
                listingsPnl.add(viewProduct);

                //View the listings
                do {
                    try {
                        Products newProduct = (Products) ois.readObject();
                        productsList.add(newProduct);
                        System.out.println(newProduct.getName() + " received");
                    } catch (Exception e1) {
                        break;
                    }
                } while (true);

                for(int i = 0; i < productsList.size(); i++) {
                    System.out.println(productsList.get(i));
                }

                for(int i = 0; i < productsList.size(); i++) {
                    System.out.println(productsList.get(i).getName() + " is in dropdown");
                    productsDropdown.addItem(productsList.get(i).getName() + " from " + productsList.get(i).getStoreName() + " ($" + productsList.get(i).getPrice() + ")");
                }

                listingsWindow.setVisible(true);
                productsWindow.setVisible(false);
            }

        });

        viewProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: optionpane popup with info about selected product, buttons: BUY and ADD TO SHOPPING CART and CANCEL
                int selectedProductIndex = productsDropdown.getSelectedIndex();
                writer.println(selectedProductIndex);
                writer.flush();
                String [] productDialogButtons = {"Purchase Now", "Add One to Cart", "Cancel"};
                System.out.println(productsList.get(selectedProductIndex).getQuantity());
                int productAction = JOptionPane.showOptionDialog(null, productsList.get(selectedProductIndex).toString(), "Product Description",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, productDialogButtons, productDialogButtons[0]);
                writer.println(productAction);
                writer.flush();

                if (productAction == 0) {
                    int quantity = Integer.parseInt(JOptionPane.showInputDialog("How many units would you like?"));
                    writer.println(quantity);
                    writer.flush();
                    try {
                        String buySuccess = reader.readLine();
                        if (buySuccess.equals("ERROR")) {
                            JOptionPane.showMessageDialog(null, "There was an issue with your purchase.", "Product Purchase", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Your purchase was successful!", "Product Purchase", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else if (productAction == 1) {
                    if (productsList.get(selectedProductIndex).getQuantity() >= 1) {
                        JOptionPane.showMessageDialog(null, "Product has been added to cart!", "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Product is out of stock!", "Shopping Cart", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Come again!", "Order Cancel", JOptionPane.INFORMATION_MESSAGE);
                }

                listingsWindow.setVisible(false);
                productsWindow.setVisible(true);
            }
        });

        viewCart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO Pull shopping cart information and display it in an option pane
                writer.println("6");
                writer.flush();
                String shoppingCartString = "";
                ArrayList<Products> shoppingTemp = new ArrayList<>();
                do {
                    try {
                        Products newProduct = (Products) ois.readObject();
                        shoppingTemp.add(newProduct);
                        System.out.println(newProduct.getName() + " received");
                    } catch (Exception e1) {
                        break;
                    }
                } while (true);

                if (!shoppingTemp.isEmpty()) {
                    for (int i = 0; i < shoppingTemp.size(); i++) {
                        int j = i + 1;
                        shoppingCartString += j + ". " + shoppingTemp.get(i).getName() + " from " +
                                shoppingTemp.get(i).getStoreName() + ". $" + shoppingTemp.get(i).getPrice() + "\n"
                                + "Quantity available: " + shoppingTemp.get(i).getQuantity() + "\n";
                    }
                }
                String[] cartDialogue = new String[3];
                /**
                 * 1. purchase cart
                 * 2. remove an item
                 * 3. return to market menu
                 */
                cartDialogue[0] = "Purchase entire cart";
                cartDialogue[1] = "Remove an item";
                cartDialogue[2] = "Return to market menu";
                if (!shoppingTemp.isEmpty()) {
                    int cartChoice = JOptionPane.showOptionDialog(null, shoppingCartString, "Error",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                            cartDialogue, cartDialogue[0]);
                    switch (cartChoice) {
                        case 0:
                            writer.println("1");
                            writer.flush(); //sends purchase cart to server
                            String success;
                            ArrayList<String> failuresArr = new ArrayList<>();
                            do {
                                try {
                                    success = reader.readLine();
                                    if (success.equals("SUCCESS")) {
                                        break;
                                    } else {
                                        failuresArr.add(success);
                                    }
                                } catch (IOException ex) {
                                    break;
                                }
                                System.out.println(success);
                            } while (true);
                            System.out.println("IM OUT");
                            if (failuresArr.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Your cart has been successfully purchased!",
                                        "Cart Confirmation", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                String failuresString = "";
                                for (int i = 0; i < failuresArr.size(); i++) {
                                    int j = i + 1;
                                    failuresString += j + ". " + failuresArr.get(i);
                                }
                                JOptionPane.showMessageDialog(null, "These items could not be purchased:" +
                                        "\n" + failuresString, "Cart Error", JOptionPane.ERROR_MESSAGE);
                            }
                            break;

                        case 1: // REMOVE AN ITEM
                            writer.println("2"); //sends remove an item to server
                            writer.flush();
                            String[] removeItemArr = new String[shoppingTemp.size()];
                            for (int i = 0; i < shoppingTemp.size(); i++) {
                                int j = i + 1;
                                removeItemArr[i] = j + ". " + shoppingTemp.get(i).getName() + " from " + shoppingTemp.get(i).getStoreName()
                                        + "($" + shoppingTemp.get(i).getPrice() + ")";
                            }
                            String removeChoice = (String) JOptionPane.showInputDialog(null, "Which item would you like to remove?"
                                    , "Remove Item", JOptionPane.PLAIN_MESSAGE, null, removeItemArr, null);
                            if (removeChoice != null) {
                                try {
                                    int indexNo = Integer.parseInt(removeChoice.substring(0, 1));
                                    if (indexNo <= removeChoice.length() && indexNo > 0) {
                                        writer.println(indexNo);
                                        writer.flush();
                                        System.out.println("printed index to server");
                                        JOptionPane.showMessageDialog(null, "Your cart was updated!", "Cart Update",
                                                JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        writer.println("CANCEL REMOVE");
                                        writer.flush();
                                        System.out.println("printed cancel remove to server");
                                        JOptionPane.showMessageDialog(null, "Nothing was changed!", "Cart Update",
                                                JOptionPane.INFORMATION_MESSAGE);
                                    }

                                } catch (Exception e1) {
                                    JOptionPane.showMessageDialog(null, "Error", "Error", JOptionPane.ERROR_MESSAGE);
                                    writer.println("-1");
                                    writer.flush();
                                    System.out.println("printed -1 in exception catch to server");
                                    e1.printStackTrace();
                                    JOptionPane.showMessageDialog(null, "Nothing was changed!", "Cart Update",
                                            JOptionPane.INFORMATION_MESSAGE);
                                }
                            } else {
                                writer.println("-1");
                                writer.flush();
                                System.out.println("removeChoice was null");
                                JOptionPane.showMessageDialog(null, "Nothing was changed!", "Cart Update",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }

                            break;
                        case 2:
                            writer.println("3");
                            writer.flush();
                            break;
                        default:
                            writer.println("3");
                            writer.flush();
                            break;


                    }
                } else {
                    JOptionPane.showMessageDialog(null, "There is nothing in your cart!", "Cart Error", JOptionPane.ERROR_MESSAGE);
                }


            }
        });

        viewHistory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                writer.println("5");
                writer.flush();
                ArrayList<String> pastPurchases = new ArrayList<>();
                do {
                    try {
                        String end = reader.readLine();
                        if (!end.equals("END")) {
                            pastPurchases.add(end);
                        } else {
                            break;
                        }
                    } catch (Exception e1) {
                        break;
                    }
                } while (true);

                if (!pastPurchases.isEmpty()) {
                    String pastPurchaseString = "";
                    for (int i = 0; i < pastPurchases.size(); i++) {
                        int j = i + 1;
                        pastPurchaseString += j + ". " + pastPurchases.get(i) + "\n";
                    }
                    JOptionPane.showMessageDialog(null, pastPurchaseString, "Past Purchases", JOptionPane.INFORMATION_MESSAGE);
                }


            }
        });
        productSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: pull product list, show text input optionPane, then show info in different pane, loop this until they no longer want to search
                writer.println("2");
                writer.flush();
                String keyword = JOptionPane.showInputDialog("What would you like to search for?");
                writer.println(keyword);
                writer.flush();

                try {
                    ArrayList<String> searchResults = new ArrayList<>();
                    String firstMessage = reader.readLine();
                    System.out.println(firstMessage);
                    if (!firstMessage.equals("NO SEARCH RESULTS")) { //Results for this search exist
                        searchResults.add(firstMessage);
                        firstMessage = reader.readLine();
                        while (!firstMessage.equals("END")) {
                            searchResults.add(firstMessage);
                            firstMessage = reader.readLine();
                        }
                        String[] productChoices = searchResults.toString().substring(1, searchResults.toString().length() - 1).split(", ");
                        for (int i = 0; i < productChoices.length; i++) {
                            int j = i + 1;
                            String tempProduct = productChoices[i];
                            productChoices[i] = j + ". " + tempProduct;
                        }
                        String searchIndex = (String) JOptionPane.showInputDialog(null, "Which product would you like to view?",
                                "Product List", JOptionPane.QUESTION_MESSAGE, null, productChoices, productChoices[0]);

                        int searchIndexInt;

                        if (searchIndex.equals("null")) { //user hit "cancel" button
                            searchIndexInt = searchResults.size() + 1;
                        } else {
                            try {
                                searchIndexInt = Integer.parseInt(searchIndex.substring(0, 1));
                            } catch (Exception e1) {
                                searchIndexInt = searchResults.size() + 1; //if there's a numberFormatException it just cancels
                                System.out.println("Error with selection of search product");
                            }
                        }
                        writer.println(searchIndexInt); //sends search index to server
                        writer.flush();

                        //What to do with product?
                        //do {
                        if (searchIndexInt <= searchResults.size()) {
                            Products product = (Products) ois.readObject();
                            //GUI
                            //  display the product and ask what to do about it
                            String [] searchDialogButtons = {"Purchase Now", "Add One to Cart", "Cancel"};
                            int productAction = JOptionPane.showOptionDialog(null, product.toString(), "Product Description",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, searchDialogButtons, searchDialogButtons[0]);
                            System.out.println(productAction);
                            /**
                             * 1. add to shopping cart
                             * 2. buy now
                             * 3. go back
                             */
                            switch (productAction) {
                                case 0: //purchase now
                                    System.out.println("case 0: purchase now");
                                    writer.println("2"); //this is what the number is in the server
                                    writer.flush();
                                    System.out.println("sent 2 to the server");
                                    int quantity = -1;
                                    do {
                                        try {
                                            quantity = Integer.parseInt(JOptionPane.showInputDialog(null,
                                                    "How many would you like to purchase?", "Order Form",
                                                    JOptionPane.QUESTION_MESSAGE));
                                            System.out.println(quantity);
                                            if (quantity > 0) {
                                                System.out.println(quantity);
                                                break;
                                            }
                                        } catch (Exception e1) {
                                            JOptionPane.showMessageDialog(null, "Please enter a valid number!",
                                                    "Quantity Error", JOptionPane.ERROR_MESSAGE);

                                        }
                                    } while (true);
                                    writer.println(quantity);
                                    writer.flush();
                                    String purchaseSuccess = reader.readLine();
                                    if (purchaseSuccess.equals("SUCCESS")) {
                                        JOptionPane.showMessageDialog(null, "Your product has been purchased!",
                                                "Product Purchase", JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Please try again with a new number!",
                                                "Out of Stock", JOptionPane.ERROR_MESSAGE);
                                    }

                                    break;
                                case 1: //add one to cart
                                    writer.println("1"); //this is the number in the server for add to cart
                                    writer.flush();
                                    System.out.println("sent 1 to the server, adding one to the cart");
                                    String success = reader.readLine();
                                    if (success.equals("SUCCESS")) {
                                        JOptionPane.showMessageDialog(null, "Added to cart!",
                                                "Search System", JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Could not add to cart," +
                                                " out of stock!", "Out of Stock Error", JOptionPane.ERROR_MESSAGE);
                                    }

                                default: //cancel
                                    writer.write("3");
                                    writer.flush();
                                    break;

                            }


                        }
                        writer.println("DO NOT SEARCH AGAIN"); //I'm trying not to edit the server too much
                        writer.flush(); //if we wanted them to be able to search again, we would print "YES" to the server and add a loop
                        //but I didn't want to mess with that right now

                        //} while (true); this loop seemed unnecessary


                    } else { //There are no results for this search
                        JOptionPane.showMessageDialog(null, "Sorry, your search yielded no results.", "Error", JOptionPane.ERROR_MESSAGE);
                        writer.println("DO NOT SEARCH AGAIN");
                        writer.flush();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }



            }
        });
        priceSort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: pull product list, sort it, return product list OR call pre-defined sort method
                writer.println("3");
                writer.flush();
                productsList = new ArrayList<>();
                productsList.clear();
                listingsPnl.remove(productsDropdown);
                listingsPnl.remove(viewProduct);
                productsDropdown = new JComboBox();
                listingsPnl.add(productsDropdown);
                listingsPnl.add(viewProduct);

                //View the listings
                do {
                    try {
                        Products newProduct = (Products) ois.readObject();
                        productsList.add(newProduct);
                        System.out.println(newProduct.getName() + " received");
                    } catch (Exception e1) {
                        break;
                    }
                } while (true);

                for (int i = 0; i < productsList.size(); i++) {
                    System.out.println(productsList.get(i));
                }

                for (int i = 0; i < productsList.size(); i++) {
                    System.out.println(productsList.get(i).getName() + " is in dropdown");
                    productsDropdown.addItem(productsList.get(i).getName() + " from " + productsList.get(i).getStoreName() + " ($" + productsList.get(i).getPrice() + ")");
                }

                listingsWindow.setVisible(true);
                productsWindow.setVisible(false);

                JOptionPane.showMessageDialog(null, "Products have been sorted from lowest to highest price!\n" +
                        "Check the listings to view the new order.", "Product Sorting", JOptionPane.INFORMATION_MESSAGE);
            }

        });
        quantitySort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: pull product list, sort it, return product list OR call pre-defined sort method
                writer.println("4");
                writer.flush();
                productsList = new ArrayList<>();
                productsList.clear();
                listingsPnl.remove(productsDropdown);
                listingsPnl.remove(viewProduct);
                productsDropdown = new JComboBox();
                listingsPnl.add(productsDropdown);
                listingsPnl.add(viewProduct);

                //View the listings
                do {
                    try {
                        Products newProduct = (Products) ois.readObject();
                        productsList.add(newProduct);
                        System.out.println(newProduct.getName() + " received");
                    } catch (Exception e1) {
                        break;
                    }
                } while (true);

                for (int i = 0; i < productsList.size(); i++) {
                    System.out.println(productsList.get(i));
                }

                for (int i = 0; i < productsList.size(); i++) {
                    System.out.println(productsList.get(i).getName() + " is in dropdown");
                    productsDropdown.addItem(productsList.get(i).getName() + " from " + productsList.get(i).getStoreName() + " ($" + productsList.get(i).getPrice() + ")");
                }

                listingsWindow.setVisible(true);
                productsWindow.setVisible(false);

                JOptionPane.showMessageDialog(null, "Products have been sorted from lowest to highest availability!\n" +
                        "Check the listings to view the new order.", "Product Sorting", JOptionPane.INFORMATION_MESSAGE);

            }
        });

        //SELLER WINDOW
        viewBooths.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                writer.println("1"); //view booths
                writer.flush();
                ArrayList<String> storeNames = new ArrayList<>();
                String[] storeNamesArr;
                int searchIndexInt = 0;
                try {
                    do {
                        String storeName = reader.readLine();
                        if (storeName.equals("END")) {
                            break;
                        }
                        System.out.println(storeName);
                        storeNames.add(storeName);
                    } while (true);
                    storeNamesArr = new String[storeNames.size()];
                    if (storeNames.isEmpty() || storeNames == null) {
                        return;
                    }

                    for (int i = 0; i < storeNames.size(); i++) {
                        int j = i + 1;
                        String tempStoreName = storeNames.get(i);
                        storeNamesArr[i] = j + ". " + tempStoreName;
                    }
                    String searchIndex = (String) JOptionPane.showInputDialog(null, "Which booth would you like to view?",
                            "Store List", JOptionPane.QUESTION_MESSAGE, null, storeNamesArr, storeNamesArr[0]);

                    try {
                        searchIndexInt = Integer.parseInt(searchIndex.substring(0, 1)) - 1;
                    }   catch (NumberFormatException e1 ) {
                        searchIndexInt = -1;
                    }
                    System.out.println(searchIndexInt);
                    System.out.println(searchIndex);
                    writer.println(searchIndexInt);
                    writer.flush();

                } catch (Exception e4) {
                    e4.printStackTrace();
                }

                boothHeaderPnl.remove(boothTitle);
                boothTitle = new JLabel(storeNames.get(searchIndexInt));
                boothHeaderPnl.add(boothTitle);

                sellerWindow.setVisible(false);
                boothWindow.setVisible(true);
            }
        });
        
        viewProducts.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                writer.println("1"); //view products
                writer.flush();
                System.out.println("viewing products");
                ArrayList<Products> sellerProductList = new ArrayList<>();
                String[] sellerProducts;
                do {
                    try {
                        Products newProduct = (Products) ois.readObject();
                        sellerProductList.add(newProduct);
                        System.out.println(newProduct.getName() + " received");
                    } catch (Exception e1) {
                        break;
                    }
                } while (true);
                if (sellerProductList == null || sellerProductList.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You do not have any products to view!",
                            "View Products", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for(int i = 0; i < sellerProductList.size(); i++) {
                    System.out.println(sellerProductList.get(i));
                }
                sellerProducts = new String[sellerProductList.size()];
                for (int i = 0; i < sellerProductList.size(); i++   ) {
                    int j = i + 1;
                    sellerProducts[i] = j + ". " + sellerProductList.get(i).getName();
                }
                String productToView = (String) JOptionPane.showInputDialog(null, "Which product would you like to view?",
                        "Product List", JOptionPane.QUESTION_MESSAGE, null, sellerProducts, sellerProducts[0]);
                int productToViewInt;
                try {
                    productToViewInt = Integer.parseInt(productToView.substring(0, 1)) - 1;
                } catch (NumberFormatException e1) {
                    productToViewInt = -1;
                }
                if (productToViewInt != -1) {
                    JOptionPane.showMessageDialog(null, sellerProductList.get(productToViewInt).toString(),
                            "Product Info", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

        //BACK BUTTONS
        backToMarketFromListings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                productsDropdown.removeAllItems();
                for (int i = 0; i < productsList.size(); i++) {
                    productsList.remove(0);
                }
                writer.println("BACK");
                writer.flush();
                listingsWindow.setVisible(false);
                productsWindow.setVisible(true);
            }
        });
        backToMainFromAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                writer.println("5");
                writer.flush();
                accountWindow.setVisible(false);
                mainWindow.setVisible(true);
            }
        });
        backToMainFromMarket.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                writer.println("7");
                productsWindow.setVisible(false);
                mainWindow.setVisible(true);
            }
        });
        backToMainFromSeller.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //writer.println("7");
                sellerWindow.setVisible(false);
                mainWindow.setVisible(true);
            }
        });
        backToSellerFromBooth.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //writer.println("7");
                boothWindow.setVisible(false);
                sellerWindow.setVisible(true);
            }
        });

        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (userType == 1) {
                    writer.println("3");
                    writer.flush();
                }
                endProgram();
            }
        });


        //TODO: What to do if red X button is pushed?
        //Solution: window listeners
        //Idea 1: disable the red X for all windows, so the only way to close the program is the quit button
        //TODO: How to handle information when each of these is closed?
        mainWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("CLOSED");
                mainWindow.setVisible(true);
                JOptionPane.showMessageDialog(null, "You must quit the program using the button below", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        welcomeWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("CLOSED");
                JOptionPane.showMessageDialog(null, "See you next time!", "Bye!", JOptionPane.INFORMATION_MESSAGE);
                endProgram();
            }
        });
        accountWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("CLOSED");
                accountWindow.setVisible(true);
                JOptionPane.showMessageDialog(null, "You must quit the program using the Quit Program button on the previous screen", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        productsWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("CLOSED");
                productsWindow.setVisible(true);
                JOptionPane.showMessageDialog(null, "You must quit the program using the Quit Program button on the previous screen", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        listingsWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("CLOSED");
                listingsWindow.setVisible(true);
                JOptionPane.showMessageDialog(null, "You must quit the program using the Quit Program button on the previous screen", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        sellerWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("CLOSED");
                sellerWindow.setVisible(true);
                JOptionPane.showMessageDialog(null, "You must quit the program using the Quit Program button on the previous screen", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        boothWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("CLOSED");
                boothWindow.setVisible(true);
                JOptionPane.showMessageDialog(null, "You must quit the program using the Quit Program button on the previous screen", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }
}
