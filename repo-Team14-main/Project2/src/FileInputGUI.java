import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * The GUI used to input files. 
 * Contains options to input files by text or file system selection. 
 * Allows for multiple files to be input to the program. 
 * @author Bilal Osman
 */
public class FileInputGUI extends JFrame {
    private JTextField filenameTextField;
    private String auditName;
    private ArrayList<String> filenames = new ArrayList<String>();


    /**
     * Constructor for the GUI. Creates a JFrame containing the title, instructions, 
     * and file selection options. Adds listeners to the buttons and defines their 
     * functions.
     */
    public FileInputGUI() {
        // Create a JFrame with a title
        super("File Input GUI");
        this.setPreferredSize(new Dimension(400, 300));

        this.setResizable(false);

        // Create a panel to hold the text field and button
        JPanel panel = new JPanel();
        GridBagConstraints grid = new GridBagConstraints();

        // Create a label to display instructions to the user
        JLabel welcomeLabel = new JLabel("Welcome! Please enter file(s) to start the voting software\n");
        JLabel instructionsLabel = new JLabel("Select input method and then run simulation by clicking 'Run Election'\n");

        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(10, 0, 0, 0);
        panel.add(welcomeLabel, grid);
        grid.gridy = 1;
        grid.insets = new Insets(0, 0, 10, 0);
        panel.add(instructionsLabel, grid);

        // Create a button to initiate file processing
        JButton processButton1 = new JButton("Add File by Text");
        JButton processButton2 = new JButton("Add File by Computer");
        JButton processButton3 = new JButton("Run Election");

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        buttonPanel.add(processButton1);
        buttonPanel.add(processButton2);
        buttonPanel.add(processButton3);

        grid.gridx = 0;
        grid.gridy = 2;
        grid.insets = new Insets(0, 0, 10, 0);
        panel.add(buttonPanel, grid);

        JPanel newElementsPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add File");
        addButton.setEnabled(false);
        //addButton.addActionListener((ActionListener) this);
        JTextField textField = new JTextField(20);
        textField.setEditable(true);
        newElementsPanel.add(addButton);
        newElementsPanel.add(textField);
        newElementsPanel.setVisible(false);

        // Add newElementsPanel to main panel
        grid.gridx = 0;
        grid.gridy = 3;
        grid.insets = new Insets(10, 0, 0, 0);
        panel.add(newElementsPanel, grid);

        // Add an action listener to the button
        processButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.getFrameForComponent(null).dispose();
                if (e.getSource() == processButton1) {
                    newElementsPanel.setVisible(true);
                    addButton.setEnabled(true);
                }
            }
        });

        processButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == processButton2) {
                    JOptionPane.getFrameForComponent(null).dispose();
                    if(newElementsPanel.isVisible()){
                        newElementsPanel.setVisible(false);
                        addButton.setEnabled(false);
                    }

                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setMultiSelectionEnabled(true);
                    fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv", "CSV"));

                    int result = fileChooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        if (selectedFile != null && selectedFile.isFile() && selectedFile.getName().endsWith(".csv")) {
                            filenames.add(selectedFile.getAbsolutePath());
                            JOptionPane.showMessageDialog(null, "File " + selectedFile + " processed successfully!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Please select a valid CSV file!");
                        }
                    } else if (result == JFileChooser.CANCEL_OPTION) {
                        JOptionPane.showMessageDialog(null, "File selection canceled.");
                    } else {
                        JOptionPane.showMessageDialog(null, "An error occurred while selecting the file.");
                    }
                }
            }
        });

        processButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == processButton3) {
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel);

                    JOptionPane.getFrameForComponent(null).dispose();
                    if(newElementsPanel.isVisible()){
                        newElementsPanel.setVisible(false);
                        addButton.setEnabled(false);
                    }

                    if(filenames.size() == 0){
                        JOptionPane.showMessageDialog(null, "There are no files. Please enter a file\n");
                        return;
                    }
                    String inputDate = JOptionPane.showInputDialog("Enter date (yyyyMMdd format):");
                    LocalDate currentDate;

                    try {
                        currentDate = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
                    } catch (DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid date input. Using today's date.");
                        currentDate = LocalDate.now();
                    }

                    auditName = "audit_" + currentDate.toString();
                    JOptionPane.showMessageDialog(null, "audit name is " + auditName);

                    String[] stringArray = filenames.toArray(new String[filenames.size()]);
                    FileParser parse = new FileParser(stringArray);
                    parse.setAuditName(auditName);

                    JOptionPane.getFrameForComponent(null).dispose();
                    JOptionPane.showMessageDialog(null, "Running Election....");
                    try {
                        Thread.sleep(5000); // wait for 10 seconds
                    } catch (InterruptedException es) {
                        es.printStackTrace();
                    }

                    // simulates election based on data from file
                    try {
                        parse.buildElection();
                    } catch (IOException ex) { //file error handling
                        JOptionPane.showMessageDialog(frame, "File error. Please try again later..\n");
                        System.exit(0);
                    }
                    JOptionPane.getFrameForComponent(null).dispose();
                    JOptionPane.showMessageDialog(frame, "Outcome:\n\n" + parse.getGUIresults() + "\nElection results are also now in '" + auditName + "'\n");
                    System.exit(0);
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.getFrameForComponent(null).dispose();
                String filename = textField.getText();

                if(filename.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please Input a File!");
                } else if(!filename.endsWith(".csv")) {
                    JOptionPane.showMessageDialog(null, "Please Input a csv file!");
                } else {
                    JOptionPane.showMessageDialog(null, "File " + filename + " processed successfully!");
                    filenames.add(filename);
                    newElementsPanel.setVisible(false);
                    addButton.setEnabled(false);
                }
            }

        });

        // Add the panel to the frame
        setContentPane(panel);

        //exits the code
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        // opens GUI
        JFrame jframe = new FileInputGUI();
        // Set the size of the frame and make it visible
        jframe.setVisible(true);
        jframe.setSize(500, 550);

    }
}
