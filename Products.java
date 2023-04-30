import java.io.Serializable;
import java.util.Scanner;

public class Products implements Serializable {
    private String name;
    private double price;
    private int quantity;
    private String description;
    private int sales;
    private String storeName; //edit; store name added as a parameter to allow backtracking if necessary
    private int inShoppingCart;

    public Products(String name, double price, int quantity, String description, int sales, String storeName, int inShoppingCart) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.storeName = storeName;
        this.sales = sales; //allow sales to be inputted directly
        this.inShoppingCart = inShoppingCart;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getInShoppingCart() {
        return inShoppingCart;
    }

    public void setInShoppingCart(int inShoppingCart) {
        this.inShoppingCart = inShoppingCart;
    }

    public String toString() {
        return String.format("Product Name: " + getName() + ", Store name: " + getStoreName() + "" +
                "\n              Product Description: " + getDescription()
                + "\n              Quantity Available: " + getQuantity() + ", Price: " + getPrice());
    }

    public int checkChoice(Scanner scan, int lastNum) {
        int choice;
        do {
            try {
                choice = Integer.parseInt(scan.nextLine());
                if (choice > 0 && choice < lastNum + 1) {
                    return choice;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid option!");
            }
        } while (true);
    }

    public int checkInt(Scanner scan) {
        int choice;
        do {
            try {
                choice = Integer.parseInt(scan.nextLine());
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid option!");
            }
        } while (true);
    }

    public double checkDouble(Scanner scan) {
        double choice;
        do {
            try {
                choice = Double.parseDouble(scan.nextLine());
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid option!");
            }
        } while (true);
    }

    public void editProduct(Scanner scan) {
        int choice;
        System.out.println("Edit name?\n1. Yes\n2. No");
        choice = checkChoice(scan, 2);
        if (choice == 1) {
            System.out.println("Enter the name of the product: ");
            String productName = scan.nextLine();
            setName(productName);
        }
        System.out.println("Edit description?\n1. Yes\n2. No");
        choice = checkChoice(scan, 2);
        if (choice == 1) {
            System.out.println("Enter the description of the product: ");
            String productDescription = scan.nextLine();
            setDescription(productDescription);
        }
        System.out.println("Edit price?\n1. Yes\n2. No");
        choice = checkChoice(scan, 2);
        if (choice == 1) {
            System.out.println("Enter the price of the product: ");
            double price = checkDouble(scan);
            setPrice(price);
        }
        System.out.println("Edit quantity?\n1. Yes\n2. No");
        choice = checkChoice(scan, 2);
        if (choice == 1) {
            System.out.println("Enter the quantity of the product: ");
            int quantity = checkInt(scan);
            setQuantity(quantity);
        }
    }


}
