import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Customer extends User implements Serializable {
    private ArrayList<String> pastPurchase; //edit made, if the code is made under the assumption this is a Products class please let me know
    private ArrayList<Integer> purchaseCount;
    private ArrayList<Integer> cartQuantityList;

    public void setShoppingCart(ArrayList<Products> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public ArrayList<String> getShoppingCartChanges() {
        return shoppingCartChanges;
    }

    public void initializeToShoppingCart(Products products, int quantity) {    //Initializing
        shoppingCart.add(products);
        cartQuantityList.add(quantity);
    }

    public void addToShoppingCart(Products products, int quantity) {    //Within the market (I'm so sorry this had to happen :(((((( )
        products.setInShoppingCart(products.getInShoppingCart() + quantity);
        shoppingCart.add(products);
        cartQuantityList.add(quantity);
    }

    public ArrayList<Integer> getCartQuantityList() {
        return cartQuantityList;
    }

    public void setCartQuantityList(ArrayList<Integer> cartQuantityList) {
        this.cartQuantityList = cartQuantityList;
    }

    public void setShoppingCartChanges(ArrayList<String> shoppingCartChanges) {
        this.shoppingCartChanges = shoppingCartChanges;
    }

    private ArrayList<Products> shoppingCart;
    private ArrayList<String> shoppingCartChanges;

    public Customer(String email, String name, String password) {
        super(email, name, password);
        pastPurchase = new ArrayList<>();
        purchaseCount = new ArrayList<>();
        shoppingCart = new ArrayList<>();
        shoppingCartChanges = new ArrayList<>();
        cartQuantityList = new ArrayList<>();

    }

    public ArrayList<Products> getShoppingCart() {
        return shoppingCart;
    }

    public void removeFromShoppingCart(Products products) {
        for (int i = 0; i < shoppingCart.size(); i++) {
            if (products.equals(shoppingCart.get(i))) {
                shoppingCart.remove(shoppingCart.get(i));
                cartQuantityList.remove(i);
            }
        }
    }

    public ArrayList<String> getPastPurchase() {
        return pastPurchase;
    }

    public void setPastPurchase(ArrayList<String> pastPurchase) {
        this.pastPurchase = pastPurchase;
    }

    public ArrayList<Integer> getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(ArrayList<Integer> purchaseCount) {
        this.purchaseCount = purchaseCount;
    }

    public void addProducts(String products, int purchases) {
        pastPurchase.add(products);
        purchaseCount.add(purchases);
    }

    public void shoppingCartChangeHelper(String productname, int errorCode) {
        StringBuilder errorMessage = new StringBuilder();
        switch (errorCode) {
            case 1:    //out of stock
                errorMessage.append(String.format("The item %s is out of stock!", productname));
                break;
            case 2:    //not found on database
                errorMessage.append(String.format("The item %s is not found in our database.", productname));
                break;
            default:   //This should NOT be the case
                errorMessage.append(String.format("Unknown error!"));
                break;
        }
        errorMessage.append(String.format("\nItem %s has been removed from your shopping cart.", productname));
        shoppingCartChanges.add(errorMessage.toString());
    }
}
