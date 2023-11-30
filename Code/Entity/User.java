package Code.Entity;

import java.util.ArrayList;

import Code.Control.DatabaseController;
import Code.Strategy.Strategy;

public abstract class User {
    private static String email;
    private String password;
    private static String firstName;
    private static String lastName;
    private static ArrayList<Purchase> purchases;
    protected Strategy strategy;

    public User(String fName, String lName, String e, String p) {
        email = e;
        password = p;
        firstName = fName;
        lastName = lName;
        purchases = DatabaseController.getPurchases(e);
    }

    public abstract void performStrategy();

    public void setStrategy(Strategy s) {
        strategy = s;
    }

    public static void removePurchase(Purchase purchase) {
        purchases.remove(purchase);
    }

    public static ArrayList<Purchase> getPurchases() {
        return purchases;
    }

    public static void addPurchase(Purchase p) {
        purchases.add(p);
    }

    public static void clearPurchases() {
        purchases.clear();
    }

    public static String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static String getLastName() {
        return lastName;
    }
    
    public void setEmail(String email) {
        User.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
