import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/*
 * The page to view the daily sales
 */
public class ViewDailySalesPage extends JDialog{
    private JInternalFrame internalFrame;

    private JScrollPane transactionScrollPane;
    private JPanel totalSalesPanel;
    private JPanel searchDatePanel;

    private int year;
    private int month;
    private int day;

    private String dateSelected = "";
    private String fileName;

    private JLabel searchDateLabel;
    private JTextField searchDateTextField;
    private JButton searchDateButton;

    private JLabel totalSalesLabel;
    private JLabel amountOfTotalSalesLabel;

    private File invoiceFile;
    private FileInputStream fi;
    private ObjectInputStream in;

    private Invoice<Medicine> dailyInvoice;
    private Invoice<Medicine> selectedInvoice;
    private Vector<Invoice<Medicine>> dailyInvoiceVector;

    private Vector<String> transactionVector;
    private JList<String> transactionList;
    private DefaultListModel<String> transactionListModel;

    private double totalSales;

    public ViewDailySalesPage(JFrame mainFrame){
        super(mainFrame, true);

        setTitle("Daily Sales");
        setSize(1280, 720);
        setLayout(new BorderLayout());

        GridBagLayout searchLayout = new GridBagLayout();
        searchLayout.columnWidths = new int[] {0, 0, 0};
		searchLayout.rowHeights = new int[] {0, 0};
		searchLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		searchLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};

        searchDatePanel = new JPanel(searchLayout);
        add(searchDatePanel, BorderLayout.NORTH);

        searchDateLabel = new JLabel("Enter Date (DD-MM-YYYY):");
        searchDateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints searchDateLabel_gbc = new GridBagConstraints();
        searchDateLabel_gbc.gridx = 0;
        searchDateLabel_gbc.gridy = 0;
        searchDateLabel_gbc.insets = new Insets(10, 20, 10, 10);
        searchDatePanel.add(searchDateLabel, searchDateLabel_gbc);

        searchDateTextField = new JTextField("DD-MM-YYYY");
        searchDateTextField.setFont(new Font("Arial", Font.ITALIC, 12));
        searchDateTextField.setForeground(Color.GRAY);
        // add a FocusListener, to remove the watermark/guide for the user
        searchDateTextField.addFocusListener(new FocusListener(){
            // if the user's keyboard cursor is in the text field
            @Override
            public void focusGained(FocusEvent e) {
                if(searchDateTextField.getText().equals("DD-MM-YYYY") == true){ // check if the field is filled or not
                    searchDateTextField.setText("");
                    searchDateTextField.setFont(new Font("Arial", Font.PLAIN, 14));
                    searchDateTextField.setForeground(Color.BLACK);
                }
            }

            // if the user's keyboard cursor is not in the text field
            @Override
            public void focusLost(FocusEvent e){
                if(searchDateTextField.getText().equals("") == true){ // check if the field is filled or not
                    searchDateTextField.setText("DD-MM-YYYY");
                    searchDateTextField.setFont(new Font("Arial", Font.ITALIC, 12));
                    searchDateTextField.setForeground(Color.GRAY);
                }
            }
        });

        GridBagConstraints searchDateTextField_gbc = new GridBagConstraints();
        searchDateTextField_gbc.gridx = 1;
        searchDateTextField_gbc.gridy = 0;
        searchDateTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
        searchDateTextField_gbc.insets = new Insets(10, 0, 10, 10);
        searchDatePanel.add(searchDateTextField, searchDateTextField_gbc);

        ImageIcon searchIcon = new ImageIcon("src/image/search_icon.png");
        Image searchImage = searchIcon.getImage();
        Image newSize = searchImage.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH); // resize the image
        searchIcon = new ImageIcon(newSize);

        searchDateButton = new JButton("Search", searchIcon);
        searchDateButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchDateButton.addActionListener(new GetDate());
        GridBagConstraints searchDateButton_gbc = new GridBagConstraints();
        searchDateButton_gbc.gridx = 2;
        searchDateButton_gbc.gridy = 0;
        searchDateButton_gbc.fill = GridBagConstraints.HORIZONTAL;
        searchDateButton_gbc.insets = new Insets(10, 0, 10, 20);
        searchDatePanel.add(searchDateButton, searchDateButton_gbc);
        
        dailyInvoiceVector = new Vector<Invoice<Medicine>>();
        transactionVector = new Vector<String>();
        transactionListModel = new DefaultListModel<String>();
        transactionList = new JList<String>();
        transactionList.setFont(new Font("Arial", Font.PLAIN, 14));
        transactionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if(e.getClickCount() == 2){
                    selectInvoice();
                }
            }
        });

        transactionScrollPane = new JScrollPane(transactionList);

        internalFrame = new JInternalFrame();
        internalFrame.setLayout(new BorderLayout());
        internalFrame.setVisible(true);
        internalFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        internalFrame.add(transactionScrollPane, BorderLayout.CENTER);
        add(internalFrame, BorderLayout.CENTER);
        
        totalSalesPanel = new JPanel(new FlowLayout());
        add(totalSalesPanel, BorderLayout.SOUTH);

        totalSalesLabel = new JLabel();
        totalSalesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalSalesPanel.add(totalSalesLabel);

        amountOfTotalSalesLabel = new JLabel();
        amountOfTotalSalesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalSalesPanel.add(amountOfTotalSalesLabel);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        ViewDailySalesPage frame = new ViewDailySalesPage(null);

        frame.setVisible(true);
    }

    /*
     * The method to get all of the invoices per day
     * from the file
     */
    private void getInvoices(){
        String transactionString;

        fileName = "src/File_Data/Daily_Invoice_"
                    + dateSelected + ".bin";

        invoiceFile = new File(fileName);
        
        dailyInvoiceVector.clear();
        transactionVector.clear();

        totalSales = 0;

        try {
            fi = new FileInputStream(invoiceFile);
            in = new ObjectInputStream(fi);

            while(true){
                dailyInvoice = (Invoice<Medicine>) in.readObject();
                dailyInvoiceVector.add(dailyInvoice);

                transactionString = dailyInvoice.getTransactionTime() + " / " + dailyInvoice.getCustomerInfo().getCustomerName();
                transactionVector.add(transactionString);

                totalSales += dailyInvoice.getTotalPrice();
            }
        }
        catch(EOFException e){
            try {
                fi.close();
                in.close();

                showInvoice();
            }
            catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "File Closing Error! Contact Administrator!", "", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "File doesn't exists! Check the date inputted again or contact administrator!", "", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * The class to get the date typed by the user
     */
    private class GetDate implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(searchDateTextField.getText().isEmpty() == true ||
                searchDateTextField.getText().equals("DD-MM-YYYY") == true){
                JOptionPane.showMessageDialog(null, "Date is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                String split[] = searchDateTextField.getText().split("-");

                try{
                    day = Integer.parseInt(split[0]);
                    month = Integer.parseInt(split[1]);
                    year = Integer.parseInt(split[2]);

                    dateSelected = year + String.format("-%02d-%02d", month, day);

                    getInvoices();
                }
                catch(NumberFormatException e1){
                    JOptionPane.showMessageDialog(null, "Date is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /*
     * The method to show the invoices in the GUI
     */
    private void showInvoice(){
        transactionListModel.clear();
        transactionListModel.addAll(transactionVector);

        transactionList.removeAll();
        transactionList.setModel(transactionListModel);

        transactionScrollPane.removeAll();
        transactionScrollPane.add(transactionList);

        internalFrame.setTitle("Daily Sales of " + dateSelected);

        internalFrame.revalidate();
        internalFrame.repaint();

        totalSalesLabel.setText("Total Sales of " + dateSelected + ":");
        amountOfTotalSalesLabel.setText(String.format("%,.0f", totalSales));

        revalidate();
        repaint();
    }

    /*
     * This method is to open a new dialog
     * for the details of the invoice of each
     * customer
     */
    private void selectInvoice(){
        selectedInvoice = new Invoice<Medicine>();

        String selectedInvoiceString = transactionList.getSelectedValue();
        String[] split = selectedInvoiceString.split(" / ");

        int i;

        for(i = 0; i < dailyInvoiceVector.size(); i++){
            String customerName = dailyInvoiceVector.get(i).getCustomerInfo().getCustomerName();

            if(split[0].equals(dailyInvoiceVector.get(i).getTransactionTime()) == true &&
                split[1].equals(customerName) == true){
                selectedInvoice = dailyInvoiceVector.get(i);
                break;
            }
        }

        String title = "Invoice of " + selectedInvoice.getCustomerInfo().getCustomerName();
    
        ViewInvoiceDialog viewInvoiceDialog = new ViewInvoiceDialog(null, title, selectedInvoice);
        viewInvoiceDialog.setVisible(true);
    }
}
