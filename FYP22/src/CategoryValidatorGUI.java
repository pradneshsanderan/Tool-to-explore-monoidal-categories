import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.EventObject;

public class CategoryValidatorGUI extends JFrame implements ActionListener {


    private JPanel mainPanel;
    private JLabel sizeLabel;
    private JTextField sizeField;
    private JButton submitButton;
    private JTable table1, table2;
    private JScrollPane scrollPane1, scrollPane2;
    private JLabel resultLabel;
    public CategoryValidatorGUI() {
        setTitle("Monoidal Category Validator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        sizeLabel = new JLabel("Enter the size of the tables:");
        sizeField = new JTextField(10);
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);

        mainPanel.add(sizeLabel);
        mainPanel.add(sizeField);
        mainPanel.add(submitButton);

        add(mainPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            int size = Integer.parseInt(sizeField.getText()) +1;
            String[] columnNames = new String[size];
            for (int i = 0; i < size; i++) {
                columnNames[i] = " ";
            }
            Object[][] data1 = new Object[size][size];
            Object[][] data2 = new Object[size][size];
//            Arrays.fill(data1, columnNames);
//            Arrays.fill(data2, columnNames);

            table1 = new JTable(data1, columnNames);
            table2 = new JTable(data2, columnNames);
            table1.setTransferHandler(null);
            table2.setTransferHandler(null);

            scrollPane1 = new JScrollPane(table1);
            scrollPane2 = new JScrollPane(table2);

            mainPanel.remove(sizeLabel);
            mainPanel.remove(sizeField);
            mainPanel.remove(submitButton);
            mainPanel.add(scrollPane1);
            mainPanel.add(scrollPane2);

            JButton validateButton = new JButton("Validate");
            validateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Get the data from the tables
                    String[][] data1 = new String[size][size];
                    String[][] data2 = new String[size][size];
                    for (int row = 0; row < size; row++) {
                        for (int col = 0; col < size; col++) {
                            data1[row][col] = (String) table1.getModel().getValueAt(row, col);
                            data2[row][col] = (String) table2.getModel().getValueAt(row, col);
                        }
                    }

                    // Send the data to the backend for validation
                    int isValid = validateCategory(data1, data2);

                    // Display the validation result
                    if (isValid ==0) {
                        resultLabel.setText("Valid monoidal category!");
                    } else if(isValid ==1) {
                        resultLabel.setText( "the tensor table is invalid");
                    } else if (isValid ==2) {
                        resultLabel.setText( "Invalid category");
                    } else if (isValid ==3) {
                        resultLabel.setText( "It is NOT a valid monoidal category");
                    }else if (isValid ==4){
                        resultLabel.setText( "It failed the associativity test");
                    }else if (isValid ==5) {
                        resultLabel.setText( "It failed the check2 test");
                    }else if (isValid ==6) {
                        resultLabel.setText( "It failed the domain test");
                    }else if (isValid ==7) {
                        resultLabel.setText( "It failed the codomain test");
                    }else if (isValid ==8) {
                        resultLabel.setText( "It failed the identity test");
                    }else if (isValid ==9) {
                        resultLabel.setText("It failed the unique identity test");
                    }
                }
            });

            resultLabel = new JLabel(" ");

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(validateButton);
            buttonPanel.add(resultLabel);

            mainPanel.add(buttonPanel);

            revalidate();
            repaint();
        }
    }


    private int validateCategory(String[][] data,String[][] data2) {
        return Main.validateCategory(data,data2);

    }

}
//class CustomCellEditor extends DefaultCellEditor {
//    public CustomCellEditor() {
//        super(new JTextField());
//    }
//    public boolean isCellEditable(EventObject e) {
//        if (e instanceof MouseEvent) {
//            int clickCount;
//            int modifiers;
//            if (e.getSource() instanceof JTable) {
//                JTable table = (JTable) e.getSource();
//                clickCount = table.getClickCount();
//                modifiers = table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).getModifiers();
//                if (clickCount == 2 && (modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
//                    int row = table.getSelectedRow();
//                    int col = table.getSelectedColumn();
//                    Object value = table.getValueAt(row, col);
//                    return (value == null || value.toString().isEmpty());
//                }
//            }
//        }
//        return super.isCellEditable(e);
//    }
//}

