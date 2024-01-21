import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

/*
 * The dialog to request the payment type from the user,
 * whether it is cash or credit card
 */
public class PaymentDialog extends JFrame{
    private JFrame thisFrame = this;

    private JLabel totalAmountLabel;
    private JLabel payWithLabel;

    private JTextField amountTextField;

    private JButton creditCardButton;
    private JButton cashButton;
    private JButton cancelButton;

    private File invoiceFile;
    private FileOutputStream fo;
    private ObjectOutputStream out;
    private MyObjectOutputStream myOut;

    private Invoice<Medicine> dailyCustomerInvoice;

    private boolean needPrescription;

    public PaymentDialog(Invoice<Medicine> invoice, boolean prescription){
        setTitle("Payment Dialog");
        setSize(500, 240);
        
        dailyCustomerInvoice = invoice;
        needPrescription = prescription;

        GridBagLayout mainLayout = new GridBagLayout();
        mainLayout.columnWidths = new int[] {250, 250};
		mainLayout.rowHeights = new int[] {20, 50, 50, 30};
		mainLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		mainLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};

        setLayout(mainLayout);

        totalAmountLabel = new JLabel("Total:");
        totalAmountLabel.setFont(new Font("Arial Narrow", Font.PLAIN, 18));
        GridBagConstraints totalAmountLabel_gbc = new GridBagConstraints();
        totalAmountLabel_gbc.gridx = 0;
        totalAmountLabel_gbc.gridy = 0;
        totalAmountLabel_gbc.anchor = GridBagConstraints.EAST;
        add(totalAmountLabel, totalAmountLabel_gbc);

        amountTextField = new JTextField(String.format("%,.0f", dailyCustomerInvoice.getTotalPrice()));
        amountTextField.setEditable(false);
        amountTextField.setFont(new Font("Arial Narrow", Font.BOLD, 18));
        GridBagConstraints amountTextField_gbc = new GridBagConstraints();
        amountTextField_gbc.gridx = 1;
        amountTextField_gbc.gridy = 0;
        amountTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
        amountTextField_gbc.anchor = GridBagConstraints.WEST;
        amountTextField_gbc.insets = new Insets(0, 5, 0, 20);
        add(amountTextField, amountTextField_gbc);
        
        payWithLabel = new JLabel("Pay With:");
        payWithLabel.setFont(new Font("Arial Narrow", Font.PLAIN, 18));
        payWithLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints payWithLabel_gbc = new GridBagConstraints();
        payWithLabel_gbc.gridx = 0;
        payWithLabel_gbc.gridy = 1;
        payWithLabel_gbc.gridwidth = 2;
        payWithLabel_gbc.fill = GridBagConstraints.BOTH;
        payWithLabel_gbc.anchor = GridBagConstraints.CENTER;
        add(payWithLabel, payWithLabel_gbc);

        creditCardButton = new JButton("Credit Card");
        creditCardButton.setFont(new Font("Arial Narrow", Font.BOLD, 18));
        creditCardButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                writeSalesDataToFile();
                
                PrintingDialog printingDialog = new PrintingDialog(thisFrame, needPrescription, dailyCustomerInvoice);

                printingDialog.setVisible(true);

                dispose();
            }
        });
        GridBagConstraints creditCardButton_gbc = new GridBagConstraints();
        creditCardButton_gbc.gridx = 0;
        creditCardButton_gbc.gridy = 2;
        creditCardButton_gbc.fill = GridBagConstraints.BOTH;
        add(creditCardButton, creditCardButton_gbc);

        cashButton = new JButton("Cash");
        cashButton.setFont(new Font("Arial Narrow", Font.BOLD, 18));
        cashButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                writeSalesDataToFile();
                
                PrintingDialog printingDialog = new PrintingDialog(thisFrame, needPrescription, dailyCustomerInvoice);

                printingDialog.setVisible(true);

                dispose();
            }
        });
        GridBagConstraints cashButton_gbc = new GridBagConstraints();
        cashButton_gbc.gridx = 1;
        cashButton_gbc.gridy = 2;
        cashButton_gbc.fill = GridBagConstraints.BOTH;
        add(cashButton, cashButton_gbc);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ShoppingCartPage<Medicine> shoppingCartPage = new ShoppingCartPage<Medicine>(dailyCustomerInvoice.getCustomerInfo());
                
                shoppingCartPage.setVisible(true);
                dispose();
            }
        });
        GridBagConstraints cancelButton_gbc = new GridBagConstraints();
        cancelButton_gbc.gridx = 0;
        cancelButton_gbc.gridy = 3;
        cancelButton_gbc.gridwidth = 2;
        cancelButton_gbc.fill = GridBagConstraints.BOTH;
        cancelButton_gbc.insets = new Insets(10, 0, 0, 0);
        add(cancelButton, cancelButton_gbc);
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    // public static void main(String[] args) {
    //     PaymentDialog paymentDialog = new PaymentDialog(new Invoice<Medicine> temp1, false);

    //     paymentDialog.setVisible(true);
    // }

    /*
     * The method to write the sales or the invoice
     * of the customer to the data
     */
    private void writeSalesDataToFile(){ 
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime today = LocalDateTime.now();
        
        String todayDate = dateFormat.format(today);

        String fileName = "src/File_Data/Daily_Invoice_"
                                + todayDate + ".bin";
        invoiceFile = new File(fileName);

        try {
            fo = new FileOutputStream(invoiceFile, true);

            if (invoiceFile.length() == 0) {
                out = new ObjectOutputStream(fo);
                out.writeObject(dailyCustomerInvoice);

                out.close();
                fo.close();
            }
            else{
                myOut = new MyObjectOutputStream(fo);
                myOut.writeObject(dailyCustomerInvoice);

                myOut.close();
                fo.close();
            }
        }
        catch (IOException e1){
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

}
