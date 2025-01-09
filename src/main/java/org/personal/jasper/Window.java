package org.personal.jasper;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;

public class Window extends JFrame {
    private static final String TODO_FILE = System.getProperty("user.home") + File.separator + "todo_list.txt";
    private DefaultListModel<String> todoListModel;
    private JList<String> todoList;

    public Window() {
        ImageIcon img = new ImageIcon("C:\\Github Repo\\To-do with autostartup\\src\\icon.png");
        setTitle("To-Do List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(img.getImage());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width / 2;
        int height = screenSize.height / 2;
        setSize(width, height);
        setLocationRelativeTo(null);

        initComponents();
        loadTodoList();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        JLabel titleLabel = new JLabel("My To-Do List", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.black);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        todoListModel = new DefaultListModel<>();
        todoList = new JList<>(todoListModel);
        todoList.setFont(new Font("Arial", Font.PLAIN, 16));
        todoList.setBackground(new Color(255, 255, 255));
        todoList.setSelectionBackground(Color.lightGray);
        todoList.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

        JScrollPane scrollPane = new JScrollPane(todoList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tasks"));
        scrollPane.setBackground(Color.WHITE);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(new Color(240, 240, 240));

        JTextField inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

        JButton addButton = new JButton("Add Task");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(50, 150, 50));
        addButton.setForeground(Color.BLACK);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JButton removeButton = new JButton("Remove Task");
        removeButton.setFont(new Font("Arial", Font.BOLD, 14));
        removeButton.setBackground(new Color(200, 50, 50));
        removeButton.setForeground(Color.BLACK);
        removeButton.setFocusPainted(false);
        removeButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.setBackground(new Color(240, 240, 240));

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            String item = inputField.getText().trim();
            if (!item.isEmpty()) {
                todoListModel.addElement((todoListModel.size() + 1) + ". " + item);
                inputField.setText("");
                saveTodoList();
            }
        });

        removeButton.addActionListener(e -> {
            int selectedIndex = todoList.getSelectedIndex();
            if (selectedIndex != -1) {
                todoListModel.remove(selectedIndex);
                renumberTodoList();
                saveTodoList();
            }
        });

        add(mainPanel);
    }

    private void loadTodoList() {
        try {
            Path filePath = Paths.get(TODO_FILE);
            if (Files.exists(filePath)) {
                Files.lines(filePath).forEach(todoListModel::addElement);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading to-do list: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveTodoList() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TODO_FILE))) {
            for (int i = 0; i < todoListModel.size(); i++) {
                writer.write(todoListModel.getElementAt(i));
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to-do list: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void renumberTodoList() {
        for (int i = 0; i < todoListModel.size(); i++) {
            String item = todoListModel.getElementAt(i);
            int indexOfDot = item.indexOf(".");
            if (indexOfDot != -1) {
                item = (i + 1) + item.substring(indexOfDot);
                todoListModel.set(i, item);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window window = new Window();
            window.setVisible(true);
        });
    }
}
