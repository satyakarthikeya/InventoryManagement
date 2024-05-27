package com.project;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

public class MongoDBUtil {
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public MongoDBUtil(String uri, String dbName, String collectionName) {
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase(dbName);
        collection = database.getCollection(collectionName);
    }

    public void addProduct(String productName, double quantity, String unit) {
        Document product = collection.find(eq("name", productName)).first();
        if (product != null) {
            if (product.getString("unit").equals(unit)) {
                double newQuantity = product.getDouble("quantity") + quantity;
                collection.updateOne(eq("name", productName), new Document("$set", new Document("quantity", newQuantity)));
            } else {
                System.out.println("Unit mismatch for " + productName + ". Expected " + product.getString("unit") + " but got " + unit);
            }
        } else {
            Document newProduct = new Document("name", productName)
                    .append("quantity", quantity)
                    .append("unit", unit);
            collection.insertOne(newProduct);
        }
    }

    public void updateProductQuantity(String productName, double quantity, String unit) {
        Document product = collection.find(eq("name", productName)).first();
        if (product != null) {
            if (product.getString("unit").equals(unit)) {
                collection.updateOne(eq("name", productName), new Document("$set", new Document("quantity", quantity)));
            } else {
                collection.updateOne(eq("name", productName), new Document("$set", new Document("quantity", quantity).append("unit", unit)));
                System.out.println("Updated unit for " + productName + " to " + unit);
            }
        }
    }

    public void removeProduct(String productName) {
        collection.deleteOne(eq("name", productName));
    }

    public Map<String, Product> getInventory() {
        Map<String, Product> inventory = new HashMap<>();
        for (Document doc : collection.find()) {
            String name = doc.getString("name");
            double quantity = doc.getDouble("quantity");
            String unit = doc.getString("unit");
            inventory.put(name, new Product(name, quantity, unit));
        }
        return inventory;
    }

    public void close() {
        mongoClient.close();
    }
}
