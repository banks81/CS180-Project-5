import java.util.*;
import java.io.*;
import java.util.Scanner;

public class Store implements Serializable{
    public String name;
    public ArrayList<Products> goods;
    private ArrayList<Customer> customers;
    private double revenue;
    private int sales;

    private String sellerName;
    private String sellerEmail;

    public Store(String name, String sellerName, String sellerEmail) {  //edit; sellerName and sellerEmail as a parameter.
        this.name = name;
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
        goods = new ArrayList<>();
        customers = new ArrayList<>();
        revenue = 0.00;
        sales = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Products> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<Products> goods) {
        this.goods = goods;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public void addGoods(Products products) {
        getGoods().add(products);
    }

    public String toMarketString() {
        String store = "Store Name: " + getName() + "\nProducts: \n";
        for (int i = 0; i < goods.size(); i++) {
            store = store + goods.get(i).toString() + "\n";
        }
        return store;
        //to print in the market
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
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

    /*
     * View store code
     */
    /*
    public void viewStore(Scanner scan) {
        int choice;
        do {
            System.out.println("Booth: " + name);
            System.out.println("1. View products");
            System.out.println("2. View sales");
            System.out.println("3. Add product");
            System.out.println("4. Edit product");
            System.out.println("5. Remove product");
            System.out.println("6. Import product csv file");
            System.out.println("7. Export product csv file");
            System.out.println("8. Go back");
            choice = checkChoice(scan, 8);
            if (choice == 1) {
                int count = 0;

                for (Products good : goods) {
                    count++;
                    System.out.println(good.toString());
                }
                if (count == 0) {
                    System.out.println("No products found!");
                }
            } else if (choice == 2) {
                // TODO view the history of sales
            } else if (choice == 3) {
                System.out.println("Enter the name of the product: ");
                String productName = scan.nextLine();
                System.out.println("Enter the description of the product: ");
                String productDescription = scan.nextLine();
                System.out.println("Enter the price of the product: ");
                double price = checkDouble(scan);
                System.out.println("Enter the quantity of the product: ");
                int quantity = checkInt(scan);
                addGoods(new Products(productName, price, quantity, productDescription, 0, name, 0));
                System.out.println("Product successfully added!");
            } else if (choice == 4) {
                int count = 0;
                if (!goods.isEmpty()) {
                    System.out.println("Which product would you like to edit?");
                    for (Products good : goods) {
                        count++;
                        System.out.println(count + ". " + good.getName());
                    }
                    choice = checkChoice(scan, goods.size());
                    getGoods().get(choice - 1).editProduct(scan);
                    System.out.println("Product successfully edited!");
                } else {
                    System.out.println("No products available to edit!");
                }
            } else if (choice == 5) {
                int count = 0;
                if (!goods.isEmpty()) {
                    System.out.println("Which product would you like to remove?");
                    for (Products good : goods) {
                        count++;
                        System.out.println(count + ". " + good.getName());
                    }
                    choice = checkChoice(scan, goods.size());
                    getGoods().remove(getGoods().get(choice - 1));
                    System.out.println("Product successfully removed!");
                } else {
                    System.out.println("No products available to remove!");
                }
            } else if (choice == 6) {
                System.out.println("Enter the name of the file: ");
                String fileName = scan.nextLine();
                try {
                    importProducts(fileName);
                } catch (FileNotFoundException e) {
                    System.out.println("File not found!");
                }
            } else if (choice == 7) {
                System.out.println("Enter the name of the file: ");
                String fileName = scan.nextLine();
                exportProducts(fileName);
            } else if (choice == 8) {
                break;
            }
        } while (true);

    }
     */

    public void editStore(Scanner scan) {
        System.out.println("Enter the name of the booth: ");
        String productName = scan.nextLine();
        setName(productName);
    }

    public void importProducts(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (true) {
                String eachLine = bufferedReader.readLine();
                if (eachLine == null) {
                    break;
                }
                try {
                    String[] lineBreak = eachLine.split(",");
                    //Assuming the csv file is in name, price, quantity, description format
                    addGoods(new Products(lineBreak[0], Double.parseDouble(lineBreak[1]),
                            Integer.parseInt(lineBreak[2]), lineBreak[3], 0, getName(), 0)); //edit; store name added to the parameter & field of Products
                } catch (
                        Exception e) {                                                         //another edit made to initialize sale as int
                    System.out.println("Wrong file format!");                                   //better when we work with initialization from file IO
                    break;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportProducts(String fileName) {
        File file = new File(fileName);
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bfw = new BufferedWriter(fw);
            for (Products good : goods) {
                bfw.write(good.getName() + "," + good.getPrice() + "," + good.getQuantity() + "," + good.getDescription() +
                        "," + good.getSales() + "," + good.getStoreName() + "\n");
            }
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
