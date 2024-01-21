import javax.swing.*;
import javax.swing.border.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;

/*
 * The page to view the shopping cart
 */
public class ShoppingCartPage <T extends Medicine> extends JFrame{ 
    private JPanel mainPanel;
    private JPanel mainSplitPane;
    private JPanel shoppingCartPanel;
    private JPanel medicineBoughtPanel;
    private JPanel totalPricePanel;
    private JPanel bottomButtonsPanel;

    private JInternalFrame customerDataPanel;

    private JScrollPane shoppingCartScrollPane;
    
    private JLabel nameLabel;
    private JLabel phoneNumberLabel;
    private JLabel addressLabel;
    private JLabel receiveMedicineLabel;
    private JLabel extraNoteLabel;
    private JLabel shoppingCartLabel;
    private JLabel totalPriceLabel;
    private JLabel totalPriceAmountLabel;
    private JLabel numberOfMedicinesBoughtLabel;
    private JLabel numberOfMedicinesBoughtAmountLabel;

    private JTextField nameTextField;
    private JTextField phoneNumberTextField;
    private JTextField extraNoteTextField;

    private JTextArea addressTextArea;

    private JRadioButton waitAtStoreRadioButton;
    private JRadioButton deliveryRadioButton;
    private ButtonGroup buttonGroup;

    private JButton paymentButton;
    private JButton homeButton;

    private JCheckBox[] medicineBoughtCheckBox;

    private LinkedList<Medicine> medicineBoughtList;
    private LinkedList<Medicine> finalMedicineBoughtList;
    private T medicineBoughtData;

    private File medicineBoughtFile;
    private FileInputStream fi;
    private ObjectInputStream in;

    private Customer customerData;

    private Invoice<Medicine> dailyCustomerInvoice;

    private double subtotal;
    private double totalPrice = 0;

    private int numberOfMedicinesBought;
    private int finalNumberOfMedicinesBought;

    private boolean needPrescription;

    public ShoppingCartPage(Customer temp){
        setTitle("Shopping Cart");
        setSize(1280, 720);
        
        medicineBoughtList = new LinkedList<Medicine>();

        customerData = temp;

        mainPanel = new JPanel(new BorderLayout());
        
        {            
            getMedicineBoughtData();

            shoppingCartScrollPane = new JScrollPane(medicineBoughtPanel);
            
            shoppingCartPanel = new JPanel(new BorderLayout());
            shoppingCartPanel.add(shoppingCartScrollPane, BorderLayout.CENTER);

            shoppingCartLabel = new JLabel("Shopping Cart");
            shoppingCartLabel.setFont(new Font("Arial Narrow", Font.BOLD, 20));
            shoppingCartLabel.setHorizontalAlignment(SwingConstants.CENTER);
            shoppingCartLabel.setVerticalAlignment(SwingConstants.CENTER);
            shoppingCartPanel.add(shoppingCartLabel, BorderLayout.NORTH);

            totalPricePanel = new JPanel(new GridLayout(2, 2, 10, 0));

            totalPriceLabel = new JLabel("Total Price:");
            totalPriceLabel.setFont(new Font("Arial Narrow", Font.BOLD, 16));
            totalPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            totalPricePanel.add(totalPriceLabel);

            totalPriceAmountLabel = new JLabel(String.format("%,.0f", totalPrice));
            totalPriceAmountLabel.setHorizontalAlignment(SwingConstants.LEFT);
            totalPriceAmountLabel.setFont(new Font("Arial Narrow", Font.PLAIN, 16));
            totalPricePanel.add(totalPriceAmountLabel);

            numberOfMedicinesBoughtLabel = new JLabel("Total Number of Medicines Bought:");
            numberOfMedicinesBoughtLabel.setFont(new Font("Arial Narrow", Font.BOLD, 16));
            numberOfMedicinesBoughtLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            totalPricePanel.add(numberOfMedicinesBoughtLabel);

            numberOfMedicinesBoughtAmountLabel = new JLabel("" + numberOfMedicinesBought);
            numberOfMedicinesBoughtAmountLabel.setHorizontalAlignment(SwingConstants.LEFT);
            numberOfMedicinesBoughtAmountLabel.setFont(new Font("Arial Narrow", Font.PLAIN, 16));
            totalPricePanel.add(numberOfMedicinesBoughtAmountLabel);
            
            shoppingCartPanel.add(totalPricePanel, BorderLayout.SOUTH);
        }

        {
            customerDataPanel = new JInternalFrame("Customer Data");
            customerDataPanel.setVisible(true);
            customerDataPanel.setClosable(false);
            customerDataPanel.setMaximizable(false);
            customerDataPanel.setResizable(false);
            
            GridBagLayout insideCustomerDataPanelLayout = new GridBagLayout();
		    insideCustomerDataPanelLayout.columnWidths = new int[] {0, 0, 0, 0};
		    insideCustomerDataPanelLayout.rowHeights = new int[] {0, 0, 30, 0, 0};
		    insideCustomerDataPanelLayout.columnWeights = new double[]{0.5, 1.0, 0.0, Double.MIN_VALUE};
		    insideCustomerDataPanelLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
            customerDataPanel.setLayout(insideCustomerDataPanelLayout);
            {
                nameLabel = new JLabel("Name:");
                nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
                GridBagConstraints nameLabel_gbc = new GridBagConstraints();
                nameLabel_gbc.gridx = 0;
                nameLabel_gbc.gridy = 0;
                nameLabel_gbc.anchor = GridBagConstraints.EAST;
                nameLabel_gbc.insets = new Insets(15, 10, 10, 0);
                customerDataPanel.add(nameLabel, nameLabel_gbc);

                nameTextField = new JTextField(customerData.getCustomerName());
                nameTextField.setEditable(false);
                nameTextField.setFont(new Font("Arial", Font.PLAIN, 14));
                GridBagConstraints nameTextField_gbc = new GridBagConstraints();
                nameTextField_gbc.gridx = 1;
                nameTextField_gbc.gridy = 0;
                nameTextField_gbc.gridwidth = 2;
                nameTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
                nameTextField_gbc.insets = new Insets(15, 10, 10, 20);
                customerDataPanel.add(nameTextField, nameTextField_gbc);

                phoneNumberLabel = new JLabel("Phone Number:");
                phoneNumberLabel.setFont(new Font("Arial", Font.BOLD, 14));
                GridBagConstraints phoneNumberLabel_gbc = new GridBagConstraints();
                phoneNumberLabel_gbc.gridx = 0;
                phoneNumberLabel_gbc.gridy = 1;
                phoneNumberLabel_gbc.anchor = GridBagConstraints.EAST;
                phoneNumberLabel_gbc.insets = new Insets(10, 10, 10, 0);
                customerDataPanel.add(phoneNumberLabel, phoneNumberLabel_gbc);

                phoneNumberTextField = new JTextField(customerData.getPhoneNumber());
                phoneNumberTextField.setEditable(false);
                phoneNumberTextField.setFont(new Font("Arial", Font.PLAIN, 14));
                GridBagConstraints phoneNumberTextField_gbc = new GridBagConstraints();
                phoneNumberTextField_gbc.gridx = 1;
                phoneNumberTextField_gbc.gridy = 1;
                phoneNumberTextField_gbc.gridwidth = 2;
                phoneNumberTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
                phoneNumberTextField_gbc.insets = new Insets(10, 10, 10, 20);
                customerDataPanel.add(phoneNumberTextField, phoneNumberTextField_gbc);

                addressLabel = new JLabel("Address:");
                addressLabel.setFont(new Font("Arial", Font.BOLD, 14));
                GridBagConstraints addressLabel_gbc = new GridBagConstraints();
                addressLabel_gbc.gridx = 0;
                addressLabel_gbc.gridy = 2;
                addressLabel_gbc.anchor = GridBagConstraints.EAST;
                addressLabel_gbc.insets = new Insets(10, 10, 10, 0);
                customerDataPanel.add(addressLabel, addressLabel_gbc);

                addressTextArea = new JTextArea();
                addressTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
                addressTextArea.setLineWrap(true);
                GridBagConstraints addressTextArea_gbc = new GridBagConstraints();
                addressTextArea_gbc.gridx = 1;
                addressTextArea_gbc.gridy = 2;
                addressTextArea_gbc.gridwidth = 2;
                addressTextArea_gbc.fill = GridBagConstraints.BOTH;
                addressTextArea_gbc.insets = new Insets(10, 10, 10, 20);
                customerDataPanel.add(addressTextArea, addressTextArea_gbc);

                receiveMedicineLabel = new JLabel("<html><p style=\"text-align:right\">How do you want to<br>receive your medicine?</p></html>");
                receiveMedicineLabel.setFont(new Font("Arial", Font.BOLD, 14));
                GridBagConstraints receiveMedicineLabel_gbc = new GridBagConstraints();
                receiveMedicineLabel_gbc.gridx = 0;
                receiveMedicineLabel_gbc.gridy = 3;
                receiveMedicineLabel_gbc.anchor = GridBagConstraints.EAST;
                receiveMedicineLabel_gbc.insets = new Insets(10, 0, 10, 0);
                customerDataPanel.add(receiveMedicineLabel, receiveMedicineLabel_gbc);

                buttonGroup = new ButtonGroup();
                
                waitAtStoreRadioButton = new JRadioButton("Wait At the Store");
                waitAtStoreRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));
                GridBagConstraints waitAtStoreRadioButton_gbc = new GridBagConstraints();
                waitAtStoreRadioButton_gbc.gridx = 1;
                waitAtStoreRadioButton_gbc.gridy = 3;
                waitAtStoreRadioButton_gbc.anchor = GridBagConstraints.WEST;
                waitAtStoreRadioButton_gbc.insets = new Insets(10, 10, 10, 0);
                customerDataPanel.add(waitAtStoreRadioButton, waitAtStoreRadioButton_gbc);
                buttonGroup.add(waitAtStoreRadioButton);

                deliveryRadioButton = new JRadioButton("By Delivery to the Address Above");
                deliveryRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));
                GridBagConstraints deliveryRadioButton_gbc = new GridBagConstraints();
                deliveryRadioButton_gbc.gridx = 2;
                deliveryRadioButton_gbc.gridy = 3;
                deliveryRadioButton_gbc.anchor = GridBagConstraints.WEST;
                deliveryRadioButton_gbc.insets = new Insets(10, 0, 15, 20);
                customerDataPanel.add(deliveryRadioButton, deliveryRadioButton_gbc);
                buttonGroup.add(deliveryRadioButton);

                extraNoteLabel = new JLabel("Extra Note:");
                extraNoteLabel.setFont(new Font("Arial", Font.BOLD, 14));
                GridBagConstraints extraNoteLabel_gbc = new GridBagConstraints();
                extraNoteLabel_gbc.gridx = 0;
                extraNoteLabel_gbc.gridy = 4;
                extraNoteLabel_gbc.anchor = GridBagConstraints.EAST;
                extraNoteLabel_gbc.insets = new Insets(10, 10, 10, 0);
                customerDataPanel.add(extraNoteLabel, extraNoteLabel_gbc);

                extraNoteTextField = new JTextField();
                extraNoteTextField.setFont(new Font("Arial", Font.PLAIN, 14));
                GridBagConstraints extraNoteTextField_gbc = new GridBagConstraints();
                extraNoteTextField_gbc.gridx = 1;
                extraNoteTextField_gbc.gridy = 4;
                extraNoteTextField_gbc.gridwidth = 2;
                extraNoteTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
                extraNoteTextField_gbc.insets = new Insets(10, 10, 10, 20);
                customerDataPanel.add(extraNoteTextField, extraNoteTextField_gbc);
            }
        }
        
        mainSplitPane = new JPanel(new GridLayout(1, 2, 20, 10));
        mainSplitPane.add(shoppingCartPanel);
        mainSplitPane.add(customerDataPanel);
        mainSplitPane.setBorder(new EmptyBorder(new Insets(0, 0, 0, 10)));
        mainPanel.add(mainSplitPane, BorderLayout.CENTER);

        bottomButtonsPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(bottomButtonsPanel, BorderLayout.SOUTH);

        homeButton = new JButton("Back to Home");
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MedicineListPage medicineListPage = new MedicineListPage(temp);
                medicineListPage.setVisible(true);
                dispose();
            }
        });
        bottomButtonsPanel.add(homeButton);

        paymentButton = new JButton("Proceed to Payment");
        paymentButton.addActionListener(new ProceedToPayment());
        bottomButtonsPanel.add(paymentButton);

        add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Customer customerData1 = new Customer();

        ShoppingCartPage frame = new ShoppingCartPage(customerData1);
        frame.setVisible(true);
    }

    /*
     * The method to get the medicine bought by the customer from the file
     * and display it in the GUI
     */
    private void getMedicineBoughtData(){
        int i;
        int amountBought;
        String medicineName;
        String checkboxString;
        
        medicineBoughtFile = new File("src/File_Data/Invoice_1_Customer.bin");

        try {
            fi = new FileInputStream(medicineBoughtFile);
            in = new ObjectInputStream(fi);
            
            while(true){
                medicineBoughtData = (T) in.readObject();
                medicineBoughtList.add(medicineBoughtData);
            }
        }
        catch (IOException | ClassNotFoundException e) {
            try {
                in.close();
                fi.close();
            }
            catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        numberOfMedicinesBought = medicineBoughtList.size();
        finalNumberOfMedicinesBought = numberOfMedicinesBought;

        try{
            for(i = 0; i < numberOfMedicinesBought; i++){
                MedicineWithPrescription temp = (MedicineWithPrescription) medicineBoughtList.get(i);
                needPrescription = true;
            }
        }
        catch(ClassCastException e1){
            needPrescription = false;
        }

        GridBagLayout medicineBoughtLayout = new GridBagLayout();
		medicineBoughtLayout.columnWidths = new int[]{485, 0};
		medicineBoughtLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};

        int[] rowHeights = new int[numberOfMedicinesBought + 1];
        double[] rowWeights = new double[numberOfMedicinesBought + 1];

        for(i = 0; i < (numberOfMedicinesBought + 1); i++){
            rowHeights[i] = 0;
            rowWeights[i] = 0.0;
        }
        
        rowWeights[i - 1] = Double.MIN_VALUE;

        medicineBoughtLayout.rowHeights = rowHeights;
		medicineBoughtLayout.rowWeights = rowWeights;

        medicineBoughtPanel = new JPanel(medicineBoughtLayout);
        medicineBoughtCheckBox = new JCheckBox[numberOfMedicinesBought];

        for(i = 0; i < numberOfMedicinesBought; i++){
            int index = i;
            medicineName = medicineBoughtList.get(i).getMedicineName();
            amountBought = medicineBoughtList.get(i).getAmountBought();
            subtotal = medicineBoughtList.get(i).getSubtotal();
            totalPrice += subtotal;

            checkboxString = String.format("<html>Medicine Name: %s<br>Amount Bought: %d<br>Subtotal: %,.0f</html>", medicineName, amountBought, subtotal);
            medicineBoughtCheckBox[i] = new JCheckBox(checkboxString, true);
            medicineBoughtCheckBox[i].setFont(new Font("Arial Narrow", Font.PLAIN, 18));
            medicineBoughtCheckBox[i].setBorder(new LineBorder(Color.BLACK, 1));
            medicineBoughtCheckBox[i].setBorderPainted(true);
            medicineBoughtCheckBox[i].addItemListener(new ItemListener(){
                @Override
                public void itemStateChanged(ItemEvent e) {
                    // unselect
                    if(e.getStateChange() == 2){
                        totalPrice -= medicineBoughtList.get(index).getSubtotal();
                        totalPriceAmountLabel.setText(String.format("%,.0f", totalPrice));
                        
                        finalNumberOfMedicinesBought--;
                        numberOfMedicinesBoughtAmountLabel.setText("" + finalNumberOfMedicinesBought);
                    }
                    // select
                    else if(e.getStateChange() == 1){
                        totalPrice += medicineBoughtList.get(index).getSubtotal();
                        totalPriceAmountLabel.setText(String.format("%,.0f", totalPrice));

                        finalNumberOfMedicinesBought++;
                        numberOfMedicinesBoughtAmountLabel.setText("" + finalNumberOfMedicinesBought);
                    }
                }
            });
            
            GridBagConstraints medicineBoughtCheckBox_gbc = new GridBagConstraints();
            medicineBoughtCheckBox_gbc.gridx = 0;
            medicineBoughtCheckBox_gbc.gridy = i;
            medicineBoughtCheckBox_gbc.anchor = GridBagConstraints.WEST;
            medicineBoughtCheckBox_gbc.fill = GridBagConstraints.BOTH;
            medicineBoughtCheckBox_gbc.insets = new Insets(10, 10, 5, 0);
            medicineBoughtPanel.add(medicineBoughtCheckBox[i], medicineBoughtCheckBox_gbc);
        }
    }

    /*
     * The class to proceed to payment dialog,
     * after the user has checked its shopping list,
     * and create an invoice
     */
    private class ProceedToPayment implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String errorMsg = "";
            String errorMsgTitle = "";
            
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");  
            LocalDateTime presentTime = LocalDateTime.now();

            int i;

            finalMedicineBoughtList = new LinkedList<Medicine>();

            for(i = 0; i < numberOfMedicinesBought; i++){
                if(medicineBoughtCheckBox[i].isSelected() == true){
                    finalMedicineBoughtList.add(medicineBoughtList.get(i));
                }
            }

            dailyCustomerInvoice = new Invoice<Medicine>();

            try{
                if(addressTextArea.getText().isEmpty() == true){
                    errorMsg = "You haven't inputted your address yet!";
                    errorMsgTitle = "Please Input Your Address";
                    throw new MyException("Address not Inputted");
                }

                if(waitAtStoreRadioButton.isSelected()){
                    dailyCustomerInvoice.setReceiveMedicineMethod("Wait At the Store");
                }
                else if(deliveryRadioButton.isSelected()){
                    dailyCustomerInvoice.setReceiveMedicineMethod("Delivery");
                }
                else{
                    errorMsg = "You haven't selected the method on how to receive your medicine";
                    errorMsgTitle = "Please Select How to Receive Your Medicine";
                    throw new MyException("Radio Button not Selected");
                }

                if(extraNoteTextField.getText().isEmpty() == true){
                    dailyCustomerInvoice.setExtraNote("-");
                }
                else{
                    dailyCustomerInvoice.setExtraNote(extraNoteTextField.getText());
                }
                
                customerData.setAddress(addressTextArea.getText());

                dailyCustomerInvoice.setCustomerInfo(customerData);
                dailyCustomerInvoice.setMedicineBought(finalMedicineBoughtList);
                dailyCustomerInvoice.setTotalPrice(totalPrice);
                dailyCustomerInvoice.setNeedPrescription(needPrescription);
                dailyCustomerInvoice.setTransactionTime(timeFormat.format(presentTime));

                PaymentDialog paymentDialog = new PaymentDialog(dailyCustomerInvoice, needPrescription);

                paymentDialog.setVisible(true);

                dispose();
            }
            catch(MyException e1){
                JOptionPane.showMessageDialog(null, errorMsg,
                                                errorMsgTitle, JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
