import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/*
 * The dialog to request the information of the customer
 */
public class CustomerInformationDialog extends JDialog{
    private JPanel mainPanel;
    private JPanel buttonsPanel;
    
    private JLabel instructionLabel;
    private JLabel nameLabel;
    private JLabel phoneNumberLabel;
    
    private JTextField nameTextField;
    private JTextField phoneNumberTextField;

    private JButton okButton;
    private JButton cancelButton;

    private File tempFile;
    private File customerDataFile;

    private FileInputStream fi;
    private ObjectInputStream in;
    private FileOutputStream fo;
    private ObjectOutputStream out;
    private MyObjectOutputStream myOut;

    private FileOutputStream foTempFile;
    private ObjectOutputStream outTempFile;

    public CustomerInformationDialog(JFrame frame){
        super(frame, true);

        setTitle("Customer's Information");
        setSize(500, 250);
        setLayout(new BorderLayout());

        GridBagLayout mainLayout = new GridBagLayout();
		mainLayout.columnWidths = new int[] {10, 20};
		mainLayout.rowHeights = new int[] {50, 0, 0};
		mainLayout.columnWeights = new double[]{0.0, 1.0};
		mainLayout.rowWeights = new double[]{0.0, 0.0, 0.0};

        mainPanel = new JPanel(mainLayout);

        instructionLabel = new JLabel("Please input your data first!");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints instruction_gbc = new GridBagConstraints();
        instruction_gbc.gridx = 0;
        instruction_gbc.gridy = 0;
        instruction_gbc.gridwidth = 2;
        instruction_gbc.fill = GridBagConstraints.BOTH;
        instruction_gbc.anchor = GridBagConstraints.CENTER;
        instruction_gbc.insets = new Insets(0, 20, 0, 20);
        mainPanel.add(instructionLabel, instruction_gbc);

        nameLabel = new JLabel("Name:");
        GridBagConstraints nameLabel_gbc = new GridBagConstraints();
        nameLabel_gbc.gridx = 0;
        nameLabel_gbc.gridy = 1;
        nameLabel_gbc.anchor = GridBagConstraints.EAST;
        nameLabel_gbc.insets = new Insets(0, 20, 15, 10);
        mainPanel.add(nameLabel, nameLabel_gbc);

        nameTextField = new JTextField();
        nameTextField.setColumns(20);
        GridBagConstraints nameTextField_gbc = new GridBagConstraints();
        nameTextField_gbc.gridx = 1;
        nameTextField_gbc.gridy = 1;
        nameTextField_gbc.fill = GridBagConstraints.BOTH;
        nameTextField_gbc.insets = new Insets(0, 0, 15, 20);
        mainPanel.add(nameTextField, nameTextField_gbc);

        phoneNumberLabel = new JLabel("Phone Number: ");
        GridBagConstraints phoneNumberLabel_gbc = new GridBagConstraints();
        phoneNumberLabel_gbc.gridx = 0;
        phoneNumberLabel_gbc.gridy = 2;
        phoneNumberLabel_gbc.anchor = GridBagConstraints.EAST;
        phoneNumberLabel_gbc.insets = new Insets(0, 20, 10, 10);
        mainPanel.add(phoneNumberLabel, phoneNumberLabel_gbc);

        phoneNumberTextField = new JTextField();
        phoneNumberTextField.setColumns(20);
        GridBagConstraints phoneNumberTextField_gbc = new GridBagConstraints();
        phoneNumberTextField_gbc.gridx = 1;
        phoneNumberTextField_gbc.gridy = 2;
        phoneNumberTextField_gbc.fill = GridBagConstraints.BOTH;
        phoneNumberTextField_gbc.insets = new Insets(0, 0, 10, 20);
        mainPanel.add(phoneNumberTextField, phoneNumberTextField_gbc);

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        tempFile = new File("src/File_Data/temp.bin");
        customerDataFile = new File("src/File_Data/Customer_Data.bin");

        try{
            foTempFile = new FileOutputStream(tempFile, false);
            outTempFile = new ObjectOutputStream(foTempFile);

            // write false to tell that the user pressed cancel or x button
            outTempFile.writeBoolean(false);
            outTempFile.writeObject(new Customer()); // set the customer ID to be read first as 0

            outTempFile.close();
            foTempFile.close();
        }
        catch(IOException e2){
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        okButton = new JButton("Ok");
        okButton.addActionListener(new getCustomerData());
        buttonsPanel.add(okButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        });
        buttonsPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    // public static void main(String[] args) {
    //     CustomerInformationDialog dialog = new CustomerInformationDialog();

    //     dialog.setVisible(true);
    // }

    /*
     * The class to check the inputted customer data
     * Check if the customer has exist in the database already
     * or not. If not yet, add the data to the database
     */
    private class getCustomerData implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            
            Customer customerData = new Customer();
            Customer checkCustomer = new Customer();
            
            boolean isDuplicate = false;

            String errorMsg = "";

            try {
                if((nameTextField.getText().isEmpty() == false) &&
                        (phoneNumberTextField.getText().isEmpty() == false)){
                    customerData.setCustomerName(nameTextField.getText().toUpperCase());
                        
                    if(phoneNumberTextField.getText().length() != 11){
                        errorMsg = "Wrong telephone number format! Please type numbers only!";
                        throw new MyException(errorMsg);
                    }

                    double tempNumber = Double.parseDouble(phoneNumberTextField.getText());

                    customerData.setPhoneNumber(phoneNumberTextField.getText());

                    foTempFile = new FileOutputStream(tempFile, false);
                    outTempFile = new ObjectOutputStream(foTempFile);
                    
                    // check if the customer pressed ok button
                    // and not cancel or exit button
                    outTempFile.writeBoolean(true);
                    outTempFile.writeObject(customerData);

                    outTempFile.close();
                    foTempFile.close();
                }
                else{
                    errorMsg = "You have not inputted your data yet!";
                    throw new MyException(errorMsg);
                }

                if(customerDataFile.length() == 0){
                    fo = new FileOutputStream(customerDataFile, true);
                    out = new ObjectOutputStream(fo);

                    out.writeObject(customerData);
                        
                    out.close();
                    fo.close();

                    dispose();
                }
                else{
                    try{
                        fi = new FileInputStream(customerDataFile);
                        in = new ObjectInputStream(fi);

                        while(true){
                            try {
                                checkCustomer = (Customer)in.readObject();
                                

                                if(checkCustomer.getCustomerName().equals(customerData.getCustomerName()) == true &&
                                    checkCustomer.getPhoneNumber().equals(customerData.getPhoneNumber()) == true){
                                        isDuplicate = true;
                                        break;
                                }
                            }
                            catch (ClassNotFoundException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        }

                        fi.close();
                        in.close();
                    }
                    catch(EOFException e2){
                        // TODO Auto-generated catch block
                        fi.close();
                        in.close();
                    }
                    catch(IOException e2){
        
                    }
                    finally{
                        if(isDuplicate == false){
                            fo = new FileOutputStream(customerDataFile, true);
                            myOut = new MyObjectOutputStream(fo);

                            myOut.writeObject(customerData);
                            
                            myOut.close();
                            fo.close();
                        }

                        dispose();
                    }
                }
            }
            catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            catch (NumberFormatException e1){
                JOptionPane.showMessageDialog(null, "Wrong telephone number format! Please input numbers only!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            catch (MyException e1) {
                JOptionPane.showMessageDialog(null, errorMsg, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

