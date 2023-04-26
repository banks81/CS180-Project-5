
package Proj4;

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


    //Listings Menu
    JFrame listingsWindow;
    JLabel listingsHeader;
    JComboBox productsDropdown;
    JButton viewProduct;
    JButton backToMarketFromListings;
    private ArrayList<Products> productsList;
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
        //Create Objects
        sellerTitle = new JLabel("Seller Option");
        //Paneling for organization
        sellerHeaderPnl.add(sellerTitle);
        sellerContent.add(sellerHeaderPnl);
        //General Config
        sellerWindow.setSize(500, 300);
        sellerWindow.setLocationRelativeTo(null);
        sellerWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        sellerWindow.setVisible(false);


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
                //TODO implement logging in with JOptionPane
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
                productsList = new ArrayList<>();
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
                int productAction = JOptionPane.showOptionDialog(null, productsList.get(selectedProductIndex).getDescription(), "Product Description",
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
                    JOptionPane.showMessageDialog(null, "Product has been added to cart!", "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Come again!", "Order Cancel", JOptionPane.INFORMATION_MESSAGE);
                }

                listingsWindow.setVisible(false);
                productsWindow.setVisible(true);
            }
        });

        viewCart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: Pull shopping cart information and display it in an option pane
                writer.println("6");
                writer.flush();


            }
        });
        viewHistory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: Pull purchase history information and display it in an option pane
            writer.println("5");
            writer.flush();


            }
        });
        productSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: pull product list, show text input optionPane, then show info in different pane, loop this until they no longer want to search
            writer.println("2");
            writer.flush();

            }
        });
        priceSort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: pull product list, sort it, return product list OR call pre-defined sort method
            writer.println("3");
            writer.flush();

            }
        });
        quantitySort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: pull product list, sort it, return product list OR call pre-defined sort method
            writer.println("4");
            writer.flush();

            }
        });

        //LISTINGS WINDOW


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

    }
}

