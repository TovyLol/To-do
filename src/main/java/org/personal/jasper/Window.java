package org.personal.jasper;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Window extends JFrame {
    private static final String TODO_DIR = System.getProperty("user.home") + File.separator + ".todo_lists";
    private JPanel categoryPanel;
    private List<Category> categories;
    private JButton addCategoryButton;

    public Window() {
        setTitle("To-Do List Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        categories = new ArrayList<>();
        loadCategories();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Panel to hold categories
        categoryPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JScrollPane scrollPane = new JScrollPane(categoryPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // Button to add new categories
        addCategoryButton = new JButton("Add New Category");
        addCategoryButton.addActionListener(e -> createNewCategory());
        add(addCategoryButton, BorderLayout.SOUTH);

        updateCategoryPanel();
    }

    private void loadCategories() {
        File dir = new File(TODO_DIR);
        if (!dir.exists()) dir.mkdirs();

        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                categories.add(new Category(file));
            }
        }
    }

    private void updateCategoryPanel() {
        categoryPanel.removeAll();
        for (Category category : categories) {
            categoryPanel.add(category.getPanel());
        }
        categoryPanel.revalidate();
        categoryPanel.repaint();
    }

    private void createNewCategory() {
        String categoryName = JOptionPane.showInputDialog(this, "Enter Category Name:");

        if (categoryName != null && !categoryName.trim().isEmpty()) {
            File newCategoryFile = new File(TODO_DIR, categoryName.trim() + ".txt");
            try {
                if (newCategoryFile.createNewFile()) {
                    Category newCategory = new Category(newCategoryFile);
                    categories.add(newCategory);
                    updateCategoryPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Category already exists!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Window().setVisible(true));
    }
}
