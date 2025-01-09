package org.personal.jasper;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;

public class Window extends JFrame {
    private static final String TODO_FILE = System.getProperty("user.home") + File.separator + "todo_list.txt";
    private DefaultListModel<String> model;
    private JList<String> list;

    public Window() {
        ImageIcon applicationicon = new ImageIcon("C:\\Github Repo\\To-do with autostartup\\src\\icon.png");
        setTitle("To-Do List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(applicationicon.getImage());

        Dimension beeldGrote = Toolkit.getDefaultToolkit().getScreenSize();
        int breedte = beeldGrote.width / 2;
        int hoogte = beeldGrote.height / 2;
        setSize(breedte, hoogte);
        setLocationRelativeTo(null);

        initComponents();
        getList();
    }

    private void initComponents() {
        JPanel mainPaneel = new JPanel();
        mainPaneel.setLayout(new BorderLayout());
        mainPaneel.setBackground(new Color(240, 240, 240));

        JLabel titelhouder = new JLabel("My To-Do List", SwingConstants.CENTER);
        titelhouder.setFont(new Font("Arial", Font.BOLD, 24));
        titelhouder.setForeground(Color.black);
        titelhouder.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPaneel.add(titelhouder, BorderLayout.NORTH);

        model = new DefaultListModel<>();
        list = new JList<>(model);
        list.setFont(new Font("Arial", Font.PLAIN, 16));
        list.setBackground(new Color(255, 255, 255));
        list.setSelectionBackground(Color.lightGray);
        list.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

        JScrollPane taakvak = new JScrollPane(list);
        taakvak.setBorder(BorderFactory.createTitledBorder("Tasks"));
        taakvak.setBackground(Color.WHITE);
        mainPaneel.add(taakvak, BorderLayout.CENTER);

        JPanel textvak = new JPanel();
        textvak.setLayout(new BorderLayout(10, 10));
        textvak.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textvak.setBackground(new Color(240, 240, 240));

        JTextField textveld = new JTextField();
        textveld.setFont(new Font("Arial", Font.PLAIN, 16));
        textveld.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

        JButton toevoegbutton = new JButton("Add Task");
        toevoegbutton.setFont(new Font("Arial", Font.BOLD, 14));
        toevoegbutton.setBackground(new Color(50, 150, 50));
        toevoegbutton.setForeground(Color.BLACK);
        toevoegbutton.setFocusPainted(false);
        toevoegbutton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JButton deletebutton = new JButton("Remove Task");
        deletebutton.setFont(new Font("Arial", Font.BOLD, 14));
        deletebutton.setBackground(new Color(200, 50, 50));
        deletebutton.setForeground(Color.BLACK);
        deletebutton.setFocusPainted(false);
        deletebutton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JPanel knopfield = new JPanel();
        knopfield.setLayout(new GridLayout(1, 2, 10, 10));
        knopfield.add(toevoegbutton);
        knopfield.add(deletebutton);
        knopfield.setBackground(new Color(240, 240, 240));

        textvak.add(textveld, BorderLayout.CENTER);
        textvak.add(knopfield, BorderLayout.EAST);

        mainPaneel.add(textvak, BorderLayout.SOUTH);

        toevoegbutton.addActionListener(e -> {
            String item = textveld.getText().trim();
            if (!item.isEmpty()) {
                model.addElement((model.size() + 1) + ". " + item);
                textveld.setText("");
                writeList();
            }
        });

        deletebutton.addActionListener(e -> {
            int selectedIndex = list.getSelectedIndex();
            if (selectedIndex != -1) {
                model.remove(selectedIndex);
                maaklijstBeter();
                writeList();
            }
        });

        add(mainPaneel);
    }

    private void getList() {
        try {
            Path storagefilepath = Paths.get(TODO_FILE);
            if (Files.exists(storagefilepath)) {
                Files.lines(storagefilepath).forEach(model::addElement);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading to-do list: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void writeList() {
        try (BufferedWriter schrijfmijntaken = new BufferedWriter(new FileWriter(TODO_FILE))) {
            for (int i = 0; i < model.size(); i++) {
                schrijfmijntaken.write(model.getElementAt(i));
                schrijfmijntaken.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to-do list: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void maaklijstBeter() {
        for (int i = 0; i < model.size(); i++) {
            String item = model.getElementAt(i);
            int indexofpunt = item.indexOf(".");
            if (indexofpunt != -1) {
                item = (i + 1) + item.substring(indexofpunt);
                model.set(i, item);
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
