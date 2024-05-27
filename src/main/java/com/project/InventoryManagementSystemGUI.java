package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class InventoryManagementSystemGUI extends JFrame {
    private final InventoryManagementSystem inventorySystem;
    private JTextField productNameField;
    private JTextField quantityField;
    private JComboBox<String> unitComboBox;
    private JTextArea inventoryArea;

    public InventoryManagementSystemGUI(InventoryManagementSystem inventorySystem) {
        this.inventorySystem = inventorySystem;
        initializeUI();
        displayInventory();
    }

    private void initializeUI() {
        setTitle("Inventory Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        inputPanel.add(new JLabel("Product Name:"), gbc);
        gbc.gridy++;
        inputPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridy++;
        inputPanel.add(new JLabel("Unit:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        productNameField = new JTextField();
        inputPanel.add(productNameField, gbc);
        gbc.gridy++;
        quantityField = new JTextField();
        inputPanel.add(quantityField, gbc);
        gbc.gridy++;
        unitComboBox = new JComboBox<>(new String[] {"kg", "liters", "grams", "units"});
        inputPanel.add(unitComboBox, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JButton addButton = createAddButton();
        inputPanel.add(addButton, gbc);

        gbc.gridy++;
        JButton removeButton = createRemoveButton();
        inputPanel.add(removeButton, gbc);

        gbc.gridy++;
        JButton updateButton = createUpdateButton();
        inputPanel.add(updateButton, gbc);

        getContentPane().add(inputPanel, BorderLayout.NORTH);

        inventoryArea = new JTextArea();
        inventoryArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(inventoryArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private JButton createAddButton() {
        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String productName = productNameField.getText();
                    double quantity = Double.parseDouble(quantityField.getText());
                    String unit = (String) unitComboBox.getSelectedItem();
                    inventorySystem.addProduct(productName, quantity, unit);
                    displayInventory();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid quantity value", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return addButton;
    }

    private JButton createRemoveButton() {
        JButton removeButton = new JButton("Remove Product");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productName = productNameField.getText();
                inventorySystem.removeProduct(productName);
                displayInventory();
            }
        });
        return removeButton;
    }

    private JButton createUpdateButton() {
        JButton updateButton = new JButton("Update Quantity");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String productName = productNameField.getText();
                    double quantity = Double.parseDouble(quantityField.getText());
                    String unit = (String) unitComboBox.getSelectedItem();
                    inventorySystem.updateProductQuantity(productName, quantity, unit);
                    displayInventory();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid quantity value", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return updateButton;
    }

    private void displayInventory() {
        Map<String, Product> inventory = inventorySystem.getInventory();
        StringBuilder inventoryText = new StringBuilder("Inventory:\n");
        for (Product product : inventory.values()) {
            inventoryText.append("Name: ").append(product.getName())
                    .append(", Quantity: ").append(product.getQuantity())
                    .append(", Unit: ").append(product.getUnit()).append("\n");
        }
        inventoryArea.setText(inventoryText.toString());
    }
}
