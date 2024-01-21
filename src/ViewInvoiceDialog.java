import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewInvoiceDialog extends JDialog {
    private JPanel customerInfoPanel;

    private JScrollPane customerInfoScrollPane;
    
    private JLabel customerNameLabel;
    private JTextField customerNameTextField;
    private JLabel phoneNumberLabel;
    private JTextField phoneNumberTextField;
    private JLabel addressLabel;
    private JTextArea addressTextArea;
    private JLabel pickedUpMethodLabel;
    private JTextField pickedUpMethodTextField;
    private JLabel extraNoteLabel;
    private JTextField extraNoteTextField;
    private JLabel transactionTimeLabel;
    private JTextField transactionTimeTextField;
    private JLabel totalPriceLabel;
    private JTextField totalPriceTextField;

    private JTable medicineBoughtTable;

    private Invoice<Medicine> invoiceData;

    private String[][] rowData;
    
    public ViewInvoiceDialog(JFrame frame, String invoiceTitle, Invoice<Medicine> selectedInvoice) {
        super(frame, true);

        invoiceData = selectedInvoice;

        setTitle(invoiceTitle);
        setSize(600, 900);
        setLayout(new BorderLayout());
        
        GridBagLayout customerInfoLayout = new GridBagLayout();
        customerInfoLayout.columnWidths = new int[] {0, 0};
		customerInfoLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0};
		customerInfoLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		customerInfoLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};

        customerInfoPanel = new JPanel(customerInfoLayout);

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 14);

        customerNameLabel = new JLabel("Customer Name:");
        customerNameLabel.setFont(labelFont);
        GridBagConstraints customerNameLabel_gbc = new GridBagConstraints();
        customerNameLabel_gbc.gridx = 0;
        customerNameLabel_gbc.gridy = 0;
        customerNameLabel_gbc.anchor = GridBagConstraints.EAST;
        customerNameLabel_gbc.insets = new Insets(10, 15, 5, 10);
        customerInfoPanel.add(customerNameLabel, customerNameLabel_gbc);
        
        customerNameTextField = new JTextField(invoiceData.getCustomerInfo().getCustomerName());
        customerNameTextField.setEditable(false);
        customerNameTextField.setFont(textFieldFont);
        GridBagConstraints customerNameTextField_gbc = new GridBagConstraints();
        customerNameTextField_gbc.gridx = 1;
        customerNameTextField_gbc.gridy = 0;
        customerNameTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
        customerNameTextField_gbc.insets = new Insets(10, 0, 5, 15);
        customerInfoPanel.add(customerNameTextField, customerNameTextField_gbc);

        phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setFont(labelFont);
        GridBagConstraints phoneNumberLabel_gbc = new GridBagConstraints();
        phoneNumberLabel_gbc.gridx = 0;
        phoneNumberLabel_gbc.gridy = 1;
        phoneNumberLabel_gbc.anchor = GridBagConstraints.EAST;
        phoneNumberLabel_gbc.insets = new Insets(10, 15, 5, 10);
        customerInfoPanel.add(phoneNumberLabel, phoneNumberLabel_gbc);

        phoneNumberTextField = new JTextField(invoiceData.getCustomerInfo().getPhoneNumber());
        phoneNumberTextField.setEditable(false);
        phoneNumberTextField.setFont(textFieldFont);
        GridBagConstraints phoneNumberTextField_gbc = new GridBagConstraints();
        phoneNumberTextField_gbc.gridx = 1;
        phoneNumberTextField_gbc.gridy = 1;
        phoneNumberTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
        phoneNumberTextField_gbc.insets = new Insets(10, 0, 5, 15);
        customerInfoPanel.add(phoneNumberTextField, phoneNumberTextField_gbc);

        addressLabel = new JLabel("Address:");
        addressLabel.setFont(labelFont);
        GridBagConstraints addressLabel_gbc = new GridBagConstraints();
        addressLabel_gbc.gridx = 0;
        addressLabel_gbc.gridy = 2;
        addressLabel_gbc.anchor = GridBagConstraints.EAST;
        addressLabel_gbc.insets = new Insets(10, 15, 5, 10);
        customerInfoPanel.add(addressLabel, addressLabel_gbc);

        addressTextArea = new JTextArea(invoiceData.getCustomerInfo().getAddress());
        addressTextArea.setEditable(false);
        addressTextArea.setFont(textFieldFont);
        GridBagConstraints addressTextArea_gbc = new GridBagConstraints();
        addressTextArea_gbc.gridx = 1;
        addressTextArea_gbc.gridy = 2;
        addressTextArea_gbc.fill = GridBagConstraints.BOTH;
        addressTextArea_gbc.insets = new Insets(10, 0, 5, 15);
        customerInfoPanel.add(addressTextArea, addressTextArea_gbc);

        pickedUpMethodLabel = new JLabel("Picked Up Method:");
        pickedUpMethodLabel.setFont(labelFont);
        GridBagConstraints pickedUpMethodLabel_gbc = new GridBagConstraints();
        pickedUpMethodLabel_gbc.gridx = 0;
        pickedUpMethodLabel_gbc.gridy = 3;
        pickedUpMethodLabel_gbc.anchor = GridBagConstraints.EAST;
        pickedUpMethodLabel_gbc.insets = new Insets(10, 15, 5, 10);
        customerInfoPanel.add(pickedUpMethodLabel, pickedUpMethodLabel_gbc);

        pickedUpMethodTextField = new JTextField(invoiceData.getReceiveMedicineMethod());
        pickedUpMethodTextField.setEditable(false);
        pickedUpMethodTextField.setFont(textFieldFont);
        GridBagConstraints pickedUpMethodTextField_gbc = new GridBagConstraints();
        pickedUpMethodTextField_gbc.gridx = 1;
        pickedUpMethodTextField_gbc.gridy = 3;
        pickedUpMethodTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
        pickedUpMethodTextField_gbc.insets = new Insets(10, 0, 5, 15);
        customerInfoPanel.add(pickedUpMethodTextField, pickedUpMethodTextField_gbc);

        extraNoteLabel = new JLabel("Extra Note:");
        extraNoteLabel.setFont(labelFont);
        GridBagConstraints extraNoteLabel_gbc = new GridBagConstraints();
        extraNoteLabel_gbc.gridx = 0;
        extraNoteLabel_gbc.gridy = 4;
        extraNoteLabel_gbc.anchor = GridBagConstraints.EAST;
        extraNoteLabel_gbc.insets = new Insets(10, 15, 5, 10);
        customerInfoPanel.add(extraNoteLabel, extraNoteLabel_gbc);

        extraNoteTextField = new JTextField(invoiceData.getExtraNote());
        extraNoteTextField.setEditable(false);
        extraNoteTextField.setFont(textFieldFont);
        GridBagConstraints extraNoteTextField_gbc = new GridBagConstraints();
        extraNoteTextField_gbc.gridx = 1;
        extraNoteTextField_gbc.gridy = 4;
        extraNoteTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
        extraNoteTextField_gbc.insets = new Insets(10, 0, 5, 15);
        customerInfoPanel.add(extraNoteTextField, extraNoteTextField_gbc);

        transactionTimeLabel = new JLabel("Transaction Time:");
        transactionTimeLabel.setFont(labelFont);
        GridBagConstraints transactionTimeLabel_gbc = new GridBagConstraints();
        transactionTimeLabel_gbc.gridx = 0;
        transactionTimeLabel_gbc.gridy = 5;
        transactionTimeLabel_gbc.anchor = GridBagConstraints.EAST;
        transactionTimeLabel_gbc.insets = new Insets(10, 15, 5, 10);
        customerInfoPanel.add(transactionTimeLabel, transactionTimeLabel_gbc);

        transactionTimeTextField = new JTextField(invoiceData.getTransactionTime());
        transactionTimeTextField.setEditable(false);
        transactionTimeTextField.setFont(textFieldFont);
        GridBagConstraints transactionTimeTextField_gbc = new GridBagConstraints();
        transactionTimeTextField_gbc.gridx = 1;
        transactionTimeTextField_gbc.gridy = 5;
        transactionTimeTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
        transactionTimeTextField_gbc.insets = new Insets(10, 0, 5, 15);
        customerInfoPanel.add(transactionTimeTextField, transactionTimeTextField_gbc);

        totalPriceLabel = new JLabel("Total Price:");
        totalPriceLabel.setFont(labelFont);
        GridBagConstraints totalPriceLabel_gbc = new GridBagConstraints();
        totalPriceLabel_gbc.gridx = 0;
        totalPriceLabel_gbc.gridy = 6;
        totalPriceLabel_gbc.anchor = GridBagConstraints.EAST;
        totalPriceLabel_gbc.insets = new Insets(10, 15, 15, 10);
        customerInfoPanel.add(totalPriceLabel, totalPriceLabel_gbc);

        totalPriceTextField = new JTextField(String.format("%,.0f", invoiceData.getTotalPrice()));
        totalPriceTextField.setEditable(false);
        totalPriceTextField.setFont(textFieldFont);
        GridBagConstraints totalPriceTextField_gbc = new GridBagConstraints();
        totalPriceTextField_gbc.gridx = 1;
        totalPriceTextField_gbc.gridy = 6;
        totalPriceTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
        totalPriceTextField_gbc.insets = new Insets(10, 0, 15, 15);
        customerInfoPanel.add(totalPriceTextField, totalPriceTextField_gbc);

        add(customerInfoPanel, BorderLayout.NORTH);

        String[] columnName = {"Medicine Name", "Amount Bought", "Type", "Price Each", "Subtotal"};

        showMedicineBought();

        DefaultTableModel medicineBoughtTableModel = new DefaultTableModel(rowData, columnName){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
        medicineBoughtTable = new JTable(medicineBoughtTableModel);
        medicineBoughtTable.setRowSelectionAllowed(false);
        medicineBoughtTable.setFont(new Font("Arial", Font.PLAIN, 12));
        medicineBoughtTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        customerInfoScrollPane = new JScrollPane(medicineBoughtTable);
        add(customerInfoScrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        Invoice<Medicine> temp = new Invoice<>();
        temp.setCustomerInfo(new Customer());

        ViewInvoiceDialog viewInvoiceDialog = new ViewInvoiceDialog(null, "", temp);

        viewInvoiceDialog.setVisible(true);
    }

    /*
     * This method is to show the medicines in the table
     */
    public void showMedicineBought(){
        int numberOfMedicinesBought = invoiceData.getMedicineBought().size();
        int i;

        rowData = new String[numberOfMedicinesBought][5];

        for(i = 0; i < numberOfMedicinesBought; i++){
            rowData[i][0] = invoiceData.getMedicineBought().get(i).getMedicineName();
            rowData[i][1] = String.format("%,d", invoiceData.getMedicineBought().get(i).getAmountBought());
            rowData[i][2] = invoiceData.getMedicineBought().get(i).getType();
            rowData[i][3] = String.format("%,.0f", invoiceData.getMedicineBought().get(i).getPrice());
            rowData[i][4] = String.format("%,.0f", invoiceData.getMedicineBought().get(i).getSubtotal());
        }
    }
}
