package org.personal.jasper;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Category {
    private File file;
    private JPanel panel;
    private JList<String> taskList;
    private DefaultListModel<String> model;

    public Category(File file) {
        this.file = file;
        initPanel();
    }

    private void initPanel() {
        panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(file.getName().replace(".txt", "")));
        panel.setBackground(new Color(200, 200, 255));

        model = new DefaultListModel<>();
        taskList = new JList<>(model);
        loadTasks();

        // Add buttons to add and remove tasks
        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> addTaskDialog());
        JButton removeButton = new JButton("Remove Task");
        removeButton.addActionListener(e -> removeTask());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        panel.add(new JScrollPane(taskList), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                model.addElement(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addTaskDialog() {
        String newTask = JOptionPane.showInputDialog(panel, "Enter a new task:");

        if (newTask != null && !newTask.trim().isEmpty()) {
            addTask(newTask.trim());
        }
    }

    private void addTask(String task) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(task);
            writer.newLine();
            model.addElement(task);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String taskToRemove = model.get(selectedIndex);
            model.remove(selectedIndex);

            // Re-write the file without the removed task
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                List<String> lines = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.equals(taskToRemove)) {
                        lines.add(line);
                    }
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (String task : lines) {
                        writer.write(task);
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
