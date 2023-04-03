import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CategoryValidatorGUI extends JFrame implements ActionListener {
    private JLabel sizeLabel;
    private JTextField sizeField;
    private JButton submitButton;
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JTable table2;
    private DefaultTableModel model2;
    private JScrollPane scrollPane2;
    private JButton validateButton;

    public CategoryValidatorGUI() {
        setTitle("Category Validator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        sizeLabel = new JLabel("Size:");
        sizeField = new JTextField(5);
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        inputPanel.add(sizeLabel);
        inputPanel.add(sizeField);
        inputPanel.add(submitButton);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        model = new DefaultTableModel();
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        validateButton = new JButton("Validate");
        validateButton.addActionListener(this);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(validateButton, BorderLayout.SOUTH);
        tablePanel.setVisible(true);




        // Tensor panel
        JPanel tensorPanel = new JPanel(new BorderLayout());
        model2 = new DefaultTableModel();
        table2 = new JTable(model2);
        scrollPane2 = new JScrollPane(table2);
        validateButton = new JButton("Validate");
        validateButton.addActionListener(this);
        tensorPanel.add(scrollPane2, BorderLayout.CENTER);
//        tensorPanel.add(validateButton, BorderLayout.SOUTH);
        tensorPanel.setVisible(true);

//        // Output panel
//        JPanel outputPanel = new JPanel(new FlowLayout());
//        JLabel outputLabel = new JLabel("Output:");
//        JTextArea outputArea = new JTextArea(3, 30);
//        outputPanel.add(outputLabel);
//        outputPanel.add(outputArea);

        // Add panels to frame
        add(inputPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(tensorPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("gg");
        if (e.getSource() == submitButton) {
            System.out.println("f");
            int size = Integer.parseInt(sizeField.getText())+1;
            model.setColumnCount(size);
            model.setRowCount(size);
            table.setEnabled(true);
            table.setVisible(true);
            scrollPane.setVisible(true);
            model2.setColumnCount(size);
            model2.setRowCount(size);
            table2.setEnabled(true);
            table2.setVisible(true);
            scrollPane2.setVisible(true);
            validateButton.setVisible(true);
            submitButton.setEnabled(false);
            System.out.println("validated");
        } else if (e.getSource() == validateButton) {
            System.out.println("fd");
            String[][] data = new String[model.getRowCount()][model.getColumnCount()];
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    data[i][j] = (String) model.getValueAt(i, j);
                }
            }
            // Pass the data to the backend for validation
            int isValid = validateCategory(data);
            if (isValid ==0) {
                JOptionPane.showMessageDialog(this, "Valid monoidal category!");
            } else if(isValid ==1) {
                JOptionPane.showMessageDialog(this, "the tensor table is invalid");
            } else if (isValid ==2) {
                JOptionPane.showMessageDialog(this, "Invalid category");
            } else if (isValid ==3) {
                JOptionPane.showMessageDialog(this, "It is NOT a valid monoidal category");
            }else if (isValid ==4){
                JOptionPane.showMessageDialog(this, "It failed the associativity test");
            }else if (isValid ==5) {
                JOptionPane.showMessageDialog(this, "It failed the check2 test");
            }else if (isValid ==6) {
                JOptionPane.showMessageDialog(this, "It failed the domain test");
            }else if (isValid ==7) {
                JOptionPane.showMessageDialog(this, "It failed the codomain test");
            }else if (isValid ==8) {
                JOptionPane.showMessageDialog(this, "It failed the identity test");
            }else if (isValid ==9) {
                JOptionPane.showMessageDialog(this, "It failed the unique identity test");
            }
            validateButton.setEnabled(false);
            table.setEnabled(false);
        }
    }

    private int validateCategory(String[][] data) {
        return Main.validateCategory(data);
    }

}
