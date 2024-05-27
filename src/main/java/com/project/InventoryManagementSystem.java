package com.project;



import javax.swing.*;
import java.util.Map;

public class InventoryManagementSystem {
    private final MongoDBUtil dbUtil;

    public InventoryManagementSystem(MongoDBUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    public void addProduct(String productName, double quantity, String unit) {
        dbUtil.addProduct(productName, quantity, unit);
    }

    public void updateProductQuantity(String productName, double quantity, String unit) {
        dbUtil.updateProductQuantity(productName, quantity, unit);
    }

    public void removeProduct(String productName) {
        dbUtil.removeProduct(productName);
    }

    public Map<String, Product> getInventory() {
        return dbUtil.getInventory();
    }

    public static void main(String[] args) {
        String uri = "mongodb://localhost:27017"; // replace with your MongoDB URI
        String dbName = "inventoryDB";
        String collectionName = "products";
        MongoDBUtil dbUtil = new MongoDBUtil(uri, dbName, collectionName);

        InventoryManagementSystem inventorySystem = new InventoryManagementSystem(dbUtil);
        SwingUtilities.invokeLater(() -> new InventoryManagementSystemGUI(inventorySystem).setVisible(true));
    }
}
