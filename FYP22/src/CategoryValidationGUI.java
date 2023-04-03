//import java.awt.BorderLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.JTextField;
//import javax.swing.table.DefaultTableModel;
//
//public class CategoryValidationGUI extends JFrame implements ActionListener {
//
//    private static final long serialVersionUID = 1L;
//    private JTextField sizeField;
//    private JTable table;
//    private JButton validateButton;
//    private JLabel statusLabel;
//    private CategoryValidator validator;
//
//    public CategoryValidationGUI() {
//        super("Monoidal Category Validation");
//        validator = new CategoryValidator();
//
//        JPanel topPanel = new JPanel();
//        topPanel.add(new JLabel("Size of Table: "));
//        sizeField = new JTextField(10);
//        topPanel.add(sizeField);
//        validateButton = new JButton("Validate");
//        validateButton.addActionListener(this);
//        topPanel.add(validateButton);
//        add(topPanel, BorderLayout.NORTH);
//
//        table = new JTable();
//        table.setModel(new DefaultTableModel());
//        add(new JScrollPane(table), BorderLayout.CENTER);
//
//        JPanel bottomPanel = new JPanel();
//        statusLabel = new JLabel("");
//        bottomPanel.add(statusLabel);
//        add(bottomPanel, BorderLayout.SOUTH);
//
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(600, 400);
//        setVisible(true);
//    }
//
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == validateButton) {
//            int size = Integer.parseInt(sizeField.getText());
//            DefaultTableModel model = (DefaultTableModel) table.getModel();
//            model.setRowCount(size);
//            model.setColumnCount(size);
//            for (int i = 0; i < size; i++) {
//                for (int j = 0; j < size; j++) {
//                    model.setValueAt("", i, j);
//                }
//            }
//            String[][] data = new String[size][size];
//            for (int i = 0; i < size; i++) {
//                for (int j = 0; j < size; j++) {
//                    data[i][j] = (String) model.getValueAt(i, j);
//                }
//            }
//            String validationMessage = validator.validateTable(data);
//            statusLabel.setText(validationMessage);
//        }
//    }
//
//    public static void main(String[] args) {
//        new CategoryValidationGUI();
//    }
//}
