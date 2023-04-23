
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;

public class MarketViewer implements Runnable {

    //Welcome
    JFrame welcomeWindow;
    JLabel welcomeTitle;
    JButton login;
    JButton createAccount;

    //Main Menu
    JFrame mainWindow;
    JLabel optionsTitle;
    JButton account;
    JButton marketOptions;
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

    //Listings Menu
    JFrame listingsWindow;
    JLabel listingsHeader;
    JComboBox productsDropdown;
    JButton viewProduct;
    JButton backToMarketFromListings;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MarketViewer());
    }

    public void endProgram() {
        //Close all windows
        mainWindow.dispose();
        welcomeWindow.dispose();
        accountWindow.dispose();
        productsWindow.dispose();
        listingsWindow.dispose();
    }

    public void run() {

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
        marketOptions = new JButton("View Farmer's Market");
        quit = new JButton("Quit Program");
        mainContent.add(quit, BorderLayout.SOUTH);
            //Header
        JPanel mainTitlePanel = new JPanel();
        mainTitlePanel.add(optionsTitle);
        mainContent.add(mainTitlePanel, BorderLayout.NORTH);
            //Buttons
        JPanel mainButtonPanel = new JPanel();
        mainButtonPanel.add(account, BorderLayout.CENTER);
        mainButtonPanel.add(marketOptions, BorderLayout.CENTER);
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


        Products[] dummyList = {new Products("Corn", 1, 10, "Its corn!", 0, "CornMart", 0)
                , new Products("Apple", 2, 20, "Grow on trees", 0, "AppleMart", 0)   };

        String[] dummyListStr = new String[dummyList.length];
        for(int i = 0; i < dummyList.length; i++) {
            dummyListStr[i] = dummyList[i].getName() + " from " + dummyList[i].getStoreName() + " ($" + dummyList[i].getPrice() + ")";
        }
    //LISTINGS MENU SETUP
        listingsWindow = new JFrame("Farmer's Market Listings Menu");
        Container listingsContent = listingsWindow.getContentPane();
        listingsContent.setLayout(new BorderLayout());
            //Create Objects
        listingsHeader = new JLabel("Product Listing:");
        listingsHeader.setFont(new Font("Serif", Font.PLAIN, 24));
        productsDropdown = new JComboBox(dummyListStr);
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
                String username = JOptionPane.showInputDialog("Enter your username: ");
                String password = JOptionPane.showInputDialog("Enter your password: ");

                welcomeWindow.setVisible(false);
                mainWindow.setVisible(true);
            }
        });
        createAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO implement creating an account with JOptionPane
                String [] dialogButtons = {"Seller", "Customer"};
                int seller = JOptionPane.showOptionDialog(null, "Welcome new user!\nAre you a seller or customer?", "Create Account",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, dialogButtons, dialogButtons[0]);
                String name = JOptionPane.showInputDialog("Welcome new user! \nEnter your name: ");
                String email = JOptionPane.showInputDialog("Welcome new user! \nEnter your email: ");
                String password = JOptionPane.showInputDialog("Welcome new user! \nEnter your password: ");
                welcomeWindow.setVisible(false);
                mainWindow.setVisible(true);
            }
        });

        //MAIN WINDOW
        marketOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                productsWindow.setVisible(true);
                mainWindow.setVisible(false);
            }
        });
        account.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: fill in the text fields with updated information
                accountWindow.setVisible(true);
                mainWindow.setVisible(false);
            }
        });

        //ACCOUNT WINDOW
        nameEnter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: update server with new name info
            }
        });
        emailEnter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: update server with new email info
            }
        });
        passwordEnter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: update server with new password info
            }
        });


        //MARKET WINDOW (productsWindow)
        viewListings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listingsWindow.setVisible(true);
                productsWindow.setVisible(false);
            }
        });
        viewCart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: Pull shopping cart information and display it in an option pane
            }
        });
        viewHistory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: Pull purchase history information and display it in an option pane
            }
        });
        productSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: pull product list, show text input optionPane, then show info in different pane, loop this until they no longer want to search
            }
        });
        priceSort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: pull product list, sort it, return product list OR call pre-defined sort method
            }
        });
        quantitySort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: pull product list, sort it, return product list OR call pre-defined sort method
            }
        });

        //LISTINGS WINDOW
        viewProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: optionpane popup with info about selected product, buttons: BUY and ADD TO SHOPPING CART and CANCEL
            }
        });

        //BACK BUTTONS
        //BACK BUTTONS
        backToMarketFromListings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listingsWindow.setVisible(false);
                productsWindow.setVisible(true);
            }
        });
        backToMainFromAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                accountWindow.setVisible(false);
                mainWindow.setVisible(true);
            }
        });
        backToMainFromMarket.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                productsWindow.setVisible(false);
                mainWindow.setVisible(true);
            }
        });
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
