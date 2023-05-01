# CS180-Project-5


## How to Compile and Run:

To compile our project, ensure you have all necessary files: marketServer.java, marketClientGUI.java, User.java, Customer.java, Seller.java, Products.java, Store.java, StoreList.txt, UsersList.txt. To run the server, open and run marketServer.java. It contains the main method that will run the server, and it is critical to run the server first. To run the client, open and run marketClientGUI.java. This contains the main method that will display the GUI to the user and allow communication to the server. When you run the client and successfully connect, you should see a window with the word "Welcome!" at the top with two buttons below. You will be able to test all functionality through this GUI.


## Submission Details

Alexander Neff - Submitted Report on Brightspace. 
Aaron Banks - Submitted Vocareum workspace.
NAME - Submitted Presentation on Brightspace.


## Class Descriptions: 

### marketServer

The marketServer class contains the main method which creates a new server thread when a new connection with a client is established. Within the run method of the server thread, the program communicates with the client to first verify provided account data with existing data read from the UserList.txt file. The data, or an error message if applicable, is sent back to the client. It should be noted that all data is stored server-side and is sent to the client when appropriate. Once the server knows if the active client is a customer or a seller, it handles requests from the user and any necessary data appropriately.


### marketClientGUI

The marketClientGUI class displays the client GUI to the user. First, the user is prompted to log in or create an account. The information is sent to the server, and this class updates the GUI corresponding to the response from the server. Once logged in, the user is able to view/edit their account regardless of their instance as a Customer or Seller. While both types of users are able to view the market, their options once they choose to view it are different. In order to exit the program, the user has to select "quit program" on this main menu. Every other page contains a back button to return to the previous page and eventually the main menu. Once selecting to view the market if the user is a customer, they are able to view the products available, search for a product, sort the products by price, sort the products by quantity, view their purchase history, and view their shopping cart. Upon viewing a product, the customer is able to purchase now or add to their shopping cart. If the user is a seller and chooses to view the market, they are able to view booths, add a booth, edit a booth, and remove a booth. Upon viewing booths, the user is able to view the products, add a product, edit a product, remove a product, import a product csv file, export a product csv file, and view sales. 




// DELETE BELOW WHEN DONE


### Market

The Market class' main functionality has to do with its main method. This serves as the main method for the entire program. The main method controls how the user interfaces with the program through the console. It first directs the User to either log in or create an account. The Market class includes methods to validate that the input email is valid and that, if the user claims they have an existing account, that the email/password combination they input matches a pair within the .txt files of user information. After signing in, it presents the user with a menu of options. This allows them to choose to view/edit their account (name, email, password, or delete their account) whether they are a Seller or Customer. However, beyond this point, if a User is and instance of a Seller, their menu functionality is passed to the Seller and Store classes through methods called in the Market class. If the user is an instance of a Customer, they are handled within the Market class. The Customer can choose to view the Farmer's Market Menu, which is where many of the Option three - Market requirements are implemented. The Customer can view all available products, search for products by keyword, sort them by quantity or price, view their purchase history, or quit. This is also where the Shopping cart selection functionality is coded - this Farmer's Market Menu allows the user to choose to view their shopping cart. If the Customer selects “View market listings” then the Market class allows them to choose which product they would like to purchase now or add to their cart. It should be noted that all functionality of the Shopping cart, although it is called within the Market class main method, is contained within the Customer class.

The Market class does not extend and is not extended by any other class, but it does call methods from the Seller, Customer, and Products classes.

The Market class was tested in TestOne.java, TestTwo.java, and TestThree.java. TestOne creates a new user as a customer then quits the program. TestTwo uses an existing customer to view products, add a product to the cart, purchase a product, view the cart, view past purchases, purchase items in the cart, and then quits the program. TestThree tests the input validation of our program. It tests the creation of a new seller, a new store, a new product, and exportation of a product csv file.

// DELETE ABOVE WHEN DONE


### User

The User class serves as a superclass to the Customer and Seller classes. This was to eliminate the repetition of code between the two subclasses, because both Sellers and Customers must have names, emails, and passwords within our implementation. The User class contains all of this information for any instance of a Customer or Seller.



### Seller

The Seller class extends the User class, which contains the basic information of any Seller’s name, email, and password. The Seller class contains more specific information about the Seller, namely an ArrayList of type Store that contains all the booths that the Seller has created. It also contains the functionality to add, remove, and edit existing booths under that Seller’s name. Similar to how the Market class contains the functionality for the menus printed to the console for the Customer, the Seller class contains the logic that hosts the menus and print outs for a Seller navigating the program, specifically within the SellerMainMenu() function (which is called from the Market class). This implements the rest of the required functionality specified under the Option Three – Seller requirement, because it allows the Seller to view their current booths (which are Store objects) and their sales. Lastly, the Seller class implements the File I/O selection described in Option Three of the handout, which states that users must be able to import and export files containing Product information. The user can choose to do this through the menus printed in SellerMainMenu(), but the actual functionality of the File I/O is implemented in the AssignProduct() method of the Store class. It is worth noting that the Seller class also passes some of the implementation for the Seller’s menu to the Store class (see Store class description).


### Customer

The Customer class extends the User class, and it contains all of the information that must be associated with an instance of a User that is a Customer, that is not already stored within the User class. This includes a String ArrayList of the names of products past purchased, an Integer ArrayList of the number of products past purchased, and another Integer ArrayList of the number of items in the cart for each Product in the cart. This class contains all necessary getters and setters for this information, along with the methods called from the Market class to manipulate the information when a purchase or cart addition is made. The Customer class handles the Shopping Cart selection from Option three of the handout. The card can be added to, removed from, or purchased all at once. The shopping cart is also updated between sign ins, for example if the Seller signs in and increases the quantity, the shopping cart will reflect that. This is enabled by the method shoppingCartChangeHelper(). 


### Products

The Products class does not have a superclass/subclass relationship with any of the other files, however it is instantiated and called upon often in the Market, Store, and Customer classes. It contains all information necessary to identify a Product within its fields: name, price, quantity available, description, number of sales, and the name of the store it was from. It includes all the necessary getters and setters for this information. The Product class also has a toString method which makes it easier to print out all the relevant information about any given product from a different class. Lastly, it implements the required functionality of a Seller being able to edit a product in one of their Stores with the editProduct() method. This method is called from the Store class and allows a Seller to change the name, description, price, or quantity of the Product. 


### Store

The Store class does not extend and is not extended by any other class, but it is instantiated in the form of an ArrayList within the Seller class and its methods are called from there – it is very intertwined with the code of the Seller class in this way. The Store class fields contain all information that is required to be associated with any given booth at the Farmer’s Market: name of the seller, email of the seller, the store’s name, and an ArrayList of Product objects representing the items available in the booth. The class includes all necessary getters and setters for these fields. This class also hosts one of the menus for the Seller to navigate through, namely in the viewStore() method, which is called from the Seller class. The viewStore() method allows a Seller to edit, remove, or add products to this booth. As mentioned in the Seller class description above, the Store method implements the selection of File I/O through its method assignProduct().


