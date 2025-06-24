import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class NoteTaker extends JFrame implements ActionListener {

    JTextArea textArea;
    JMenuBar menuBar;
    JMenu fileMenu, formatMenu;
    JMenuItem newItem, openItem, saveItem, exitItem;
    JLabel statusLabel;
    JCheckBoxMenuItem darkMode;

    JComboBox<String> fontSizeBox;
    JComboBox<String> fontBox;

    public NoteTaker() {
        setTitle("Java Note-Taking App");
        setSize(700, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Text Area ---
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // --- Status Bar ---
        statusLabel = new JLabel("Characters: 0");
        add(statusLabel, BorderLayout.SOUTH);

        textArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                statusLabel.setText("Characters: " + textArea.getText().length());
            }
        });

        // --- Menu Bar ---
        menuBar = new JMenuBar();

        // --- File Menu ---
        fileMenu = new JMenu("File");
        newItem = new JMenuItem("New");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // --- Dark Mode Toggle ---
        darkMode = new JCheckBoxMenuItem("Dark Mode");
        darkMode.addItemListener(e -> {
            if (darkMode.isSelected()) {
                textArea.setBackground(Color.DARK_GRAY);
                textArea.setForeground(Color.WHITE);
                statusLabel.setForeground(Color.WHITE);
                getContentPane().setBackground(Color.DARK_GRAY);
            } else {
                textArea.setBackground(Color.WHITE);
                textArea.setForeground(Color.BLACK);
                statusLabel.setForeground(Color.BLACK);
                getContentPane().setBackground(null);
            }
        });
        fileMenu.addSeparator();
        fileMenu.add(darkMode);

        // --- Format Menu ---
        formatMenu = new JMenu("Format");

        // Font size
        String[] sizes = {"12", "16", "20", "24", "28"};
        fontSizeBox = new JComboBox<>(sizes);
        fontSizeBox.setSelectedItem("16");

        // Font family
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox = new JComboBox<>(fonts);
        fontBox.setSelectedItem("Arial");

        fontSizeBox.addActionListener(e -> updateFont());
        fontBox.addActionListener(e -> updateFont());

        formatMenu.add(new JLabel("Font Family:"));
        formatMenu.add(fontBox);
        formatMenu.add(new JLabel("Font Size:"));
        formatMenu.add(fontSizeBox);

        // --- Add Menus ---
        menuBar.add(fileMenu);
        menuBar.add(formatMenu);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    private void updateFont() {
        String fontName = (String) fontBox.getSelectedItem();
        int fontSize = Integer.parseInt((String) fontSizeBox.getSelectedItem());
        textArea.setFont(new Font(fontName, Font.PLAIN, fontSize));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newItem) {
            textArea.setText("");
        } else if (e.getSource() == openItem) {
            openFile();
        } else if (e.getSource() == saveItem) {
            saveFile();
        } else if (e.getSource() == exitItem) {
            System.exit(0);
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.read(reader, null);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error opening file!");
            }
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                textArea.write(writer);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file!");
            }
        }
    }

    public static void main(String[] args) {
        new NoteTaker();
    }
}
