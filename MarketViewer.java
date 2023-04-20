import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MarketViewer implements Runnable {

    //Welcome
    JLabel welcomeTitle;
    JButton login;
    JButton createAccount;

    //Main Menu
    JLabel optionsTitle;
    JButton account;
    JButton marketOptions;
    JButton quit;

    //Account Menu
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
    JLabel marketTitle;
    JButton viewListings;
    JButton productSearch;
    JButton priceSort;
    JButton quantitySort;
    JButton viewHistory;
    JButton viewCart;
    JButton backToMainFromMarket;

    //Listings Menu
    JLabel listingsHeader;
    JComboBox productsDropdown;
    JButton viewProduct;
    JButton backToMarketFromListings;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MarketViewer());
    }

    public void run() {

    //WELCOME MENU SETUP
            //Create Objects for screen
        JFrame welcomeWindow = new JFrame("Farmer's Market Welcome Menu");
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
        welcomeWindow.setSize(400, 200);
        welcomeWindow.setLocationRelativeTo(null);
        welcomeWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        welcomeWindow.setVisible(true);


    //MAIN MENU SETUP
            //Create Objects for screen
        JFrame mainWindow = new JFrame("Farmer's Market Main Menu");
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
        mainWindow.setSize(400, 200);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainWindow.setVisible(false);


    //ACCOUNT MENU SETUP
        JFrame accountWindow = new JFrame("Farmer's Market Account Menu");
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
        accountWindow.setSize(700, 300);
        accountWindow.setLocationRelativeTo(null);
        accountWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        accountWindow.setVisible(false);



    //PRODUCTS MENU SETUP
        JFrame productsWindow = new JFrame("Farmer's Market Products Menu");
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
        productsWindow.setSize(700, 300);
        productsWindow.setLocationRelativeTo(null);
        productsWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        productsWindow.setVisible(false);


        Products[] dummyList = {new Products("Corn", 1, 10, "Its corn!", 0, "CornMart", 0)
                , new Products("Apple", 2, 20, "Grow on trees", 0, "AppleMart", 0)   };

        String[] dummyListStr = new String[dummyList.length];
        for(int i = 0; i < dummyList.length; i++) {
            dummyListStr[i] = dummyList[i].getName() + " from " + dummyList[i].getStoreName() + " ($" + dummyList[i].getPrice() + ")";
        }
    //LISTINGS MENU SETUP
        JFrame listingsWindow = new JFrame("Farmer's Market Listings Menu");
        Container listingsContent = listingsWindow.getContentPane();
        listingsContent.setLayout(new BorderLayout());
            //Create Objects
        listingsHeader = new JLabel("Product Listing:");
        listingsHeader.setFont(new Font("Serif", Font.PLAIN, 24));
        productsDropdown = new JComboBox(dummyListStr);
        viewProduct = new JButton("View This Product");
        backToMarketFromListings = new JButton();
            //Paneling for organization
        JPanel listingsHeaderPnl = new JPanel();
        listingsHeaderPnl.add(listingsHeader);
        JPanel listingsPnl = new JPanel();
        listingsPnl.add(productsDropdown);
        listingsPnl.add(viewProduct);
        listingsContent.add(listingsPnl, BorderLayout.CENTER);
        listingsContent.add(listingsHeaderPnl, BorderLayout.NORTH);
            //General Config
        listingsWindow.setSize(900, 300);
        listingsWindow.setLocationRelativeTo(null);
        listingsWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        listingsWindow.setVisible(false);



    //ACTION LISTENERS


        //WELCOME WINDOW
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO implement logging in with JOptionPane
                welcomeWindow.setVisible(false);
                mainWindow.setVisible(true);
            }
        });
        createAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO implement creating an account with JOptionPane
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
                //TODO: Quitting? Close all windows?
            }
        });
        //TODO: What to do if red X button is pushed?

    }
}
