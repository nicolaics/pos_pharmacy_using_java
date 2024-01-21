import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/*
 * The dialog to add medicine
 */
public class AddMedicineDialog extends JDialog {
    private JPanel mainPanel;
    private JPanel buttonsPanel;

    private JScrollPane descriptionScrollPanel;

    private JLabel medicinePicture;
    private JLabel medicineNameLabel;
    private JTextField medicineNameTextField;
    private JLabel stockLabel;
    private JTextField stockTextField;
    private JLabel priceLabel;
    private JTextField priceTextField;
    private JLabel expiredDateLabel;
    private JTextField expiredDateTextField;
    private JLabel typeLabel;
    private JTextField typeTextField;
    private JLabel needPrescriptionLabel;
    private JComboBox<String> needPrescriptionComboBox;
    private JLabel medicineDescriptionLabel;
    private JTextArea medicineDescriptionTextArea;
    private JLabel howToConsumeLabel;
    private JTextField howToConsumeTextField;
    private JLabel maxPurchaseAmountLabel;
    private JTextField maxPurchaseAmountTextField;
    private JComboBox<String> medicineType;

    private JButton okButton;
    private JButton cancelButton;

    private File medicineFile;
    private FileInputStream fi;
    private ObjectInputStream in;
    private FileOutputStream fo;
    private ObjectOutputStream out;

    public AddMedicineDialog(JFrame frame){
        super(frame, true);

        setTitle("Add Medicine");
        setSize(900, 700);
        setLayout(new BorderLayout());
        
        GridBagLayout mainLayout = new GridBagLayout();
        mainLayout.columnWidths = new int[]{0, 0, 0, 0};
        mainLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        mainLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        mainLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};

        String medicinePictureStr = "<html><center>To add pictures:" +
                                    "<br>1. Find the picture in 1x1 ratio" +
                                    "<br>2. Name the file the same as the medicine name" +
                                    "<br>you typed and put it in the folder as appropriate</center></html>";

        medicinePicture = new JLabel(medicinePictureStr);
        medicinePicture.setBorder(new EmptyBorder(0, 10, 0, 0));
        medicinePicture.setFont(new Font("Arial Narrow", Font.BOLD, 14));
        add(medicinePicture, BorderLayout.WEST);

        mainPanel = new JPanel(mainLayout);
        
        Font descriptionFont = new Font("Arial Narrow", Font.PLAIN, 16);
        Font titleFont = new Font("Arial Narrow", Font.BOLD, 16);
        {
            medicineNameLabel = new JLabel("Medicine Name:");
            medicineNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            medicineNameLabel.setFont(titleFont);
            GridBagConstraints medicineNameLabel_gbc = new GridBagConstraints();
            medicineNameLabel_gbc.gridx = 0;
            medicineNameLabel_gbc.gridy = 0;
            medicineNameLabel_gbc.anchor = GridBagConstraints.EAST;
            medicineNameLabel_gbc.insets = new Insets(10, 5, 10, 10);
            mainPanel.add(medicineNameLabel, medicineNameLabel_gbc);

            medicineNameTextField = new JTextField();
            medicineNameTextField.setFont(descriptionFont);
            GridBagConstraints medicineNameTextField_gbc = new GridBagConstraints();
            medicineNameTextField_gbc.gridx = 1;
            medicineNameTextField_gbc.gridy = 0;
            medicineNameTextField_gbc.gridwidth = 2;
            medicineNameTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
            medicineNameTextField_gbc.anchor = GridBagConstraints.WEST;
            medicineNameTextField_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(medicineNameTextField, medicineNameTextField_gbc);
        }
        {
            stockLabel = new JLabel("Stock Available:");
            stockLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            stockLabel.setFont(titleFont);
            GridBagConstraints stockLabel_gbc = new GridBagConstraints();
            stockLabel_gbc.gridx = 0;
            stockLabel_gbc.gridy = 1;
            stockLabel_gbc.anchor = GridBagConstraints.EAST;
            stockLabel_gbc.insets = new Insets(10, 5, 10, 10);
            mainPanel.add(stockLabel, stockLabel_gbc);

            stockTextField = new JTextField();
            stockTextField.setFont(descriptionFont);
            GridBagConstraints stockTextField_gbc = new GridBagConstraints();
            stockTextField_gbc.gridx = 1;
            stockTextField_gbc.gridy = 1;
            stockTextField_gbc.gridwidth = 2;
            stockTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
            stockTextField_gbc.anchor = GridBagConstraints.WEST;
            stockTextField_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(stockTextField, stockTextField_gbc);
        }
        {
            priceLabel = new JLabel("Price:");
            priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            priceLabel.setFont(titleFont);
            GridBagConstraints priceLabel_gbc = new GridBagConstraints();
            priceLabel_gbc.gridx = 0;
            priceLabel_gbc.gridy = 2;
            priceLabel_gbc.anchor = GridBagConstraints.EAST;
            priceLabel_gbc.insets = new Insets(10, 5, 10, 10);
            mainPanel.add(priceLabel, priceLabel_gbc);

            priceTextField = new JTextField();
            priceTextField.setFont(descriptionFont);
            GridBagConstraints priceTextField_gbc = new GridBagConstraints();
            priceTextField_gbc.gridx = 1;
            priceTextField_gbc.gridy = 2;
            priceTextField_gbc.gridwidth = 2;
            priceTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
            priceTextField_gbc.anchor = GridBagConstraints.WEST;
            priceTextField_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(priceTextField, priceTextField_gbc);
        }
        {
            expiredDateLabel = new JLabel("Nearest Expired Date:");
            expiredDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            expiredDateLabel.setFont(titleFont);
            GridBagConstraints expiredDateLabel_gbc = new GridBagConstraints();
            expiredDateLabel_gbc.gridx = 0;
            expiredDateLabel_gbc.gridy = 3;
            expiredDateLabel_gbc.anchor = GridBagConstraints.EAST;
            expiredDateLabel_gbc.insets = new Insets(10, 5, 10, 10);
            mainPanel.add(expiredDateLabel, expiredDateLabel_gbc);

            expiredDateTextField = new JTextField();
            expiredDateTextField.setFont(descriptionFont);
            GridBagConstraints expiredDateTextField_gbc = new GridBagConstraints();
            expiredDateTextField_gbc.gridx = 1;
            expiredDateTextField_gbc.gridy = 3;
            expiredDateTextField_gbc.gridwidth = 2;
            expiredDateTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
            expiredDateTextField_gbc.anchor = GridBagConstraints.WEST;
            expiredDateTextField_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(expiredDateTextField, expiredDateTextField_gbc);
        }
        {
            typeLabel = new JLabel("Medicine Type:");
            typeLabel.setFont(titleFont);
            typeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            GridBagConstraints typeLabel_gbc = new GridBagConstraints();
            typeLabel_gbc.gridx = 0;
            typeLabel_gbc.gridy = 4;
            typeLabel_gbc.anchor = GridBagConstraints.EAST;
            typeLabel_gbc.insets = new Insets(10, 5, 10, 10);
            mainPanel.add(typeLabel, typeLabel_gbc);

            typeTextField = new JTextField();
            typeTextField.setFont(descriptionFont);
            GridBagConstraints typeTextField_gbc = new GridBagConstraints();
            typeTextField_gbc.gridx = 1;
            typeTextField_gbc.gridy = 4;
            typeTextField_gbc.gridwidth = 2;
            typeTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
            typeTextField_gbc.anchor = GridBagConstraints.WEST;
            typeTextField_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(typeTextField, typeTextField_gbc);
        }
        {
            needPrescriptionLabel = new JLabel("Need Prescription:");
            needPrescriptionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            needPrescriptionLabel.setFont(titleFont);
            GridBagConstraints needPrescriptionLabel_gbc = new GridBagConstraints();
            needPrescriptionLabel_gbc.gridx = 0;
            needPrescriptionLabel_gbc.gridy = 5;
            needPrescriptionLabel_gbc.anchor = GridBagConstraints.EAST;
            needPrescriptionLabel_gbc.insets = new Insets(10, 5, 10, 10);
            mainPanel.add(needPrescriptionLabel, needPrescriptionLabel_gbc);

            DefaultComboBoxModel<String> needPrescriptionComboBoxModel = new DefaultComboBoxModel<String>();
            needPrescriptionComboBoxModel.addElement("");
            needPrescriptionComboBoxModel.addElement("YES");
            needPrescriptionComboBoxModel.addElement("NO");

            needPrescriptionComboBox = new JComboBox<String>();
            needPrescriptionComboBox.setModel(needPrescriptionComboBoxModel);
            needPrescriptionComboBox.setFont(new Font("Arial Narrow", Font.BOLD, 16));
            needPrescriptionComboBox.setForeground(Color.RED);
            needPrescriptionComboBox.setEditable(false);
            GridBagConstraints needPrescriptionComboBox_gbc = new GridBagConstraints();
            needPrescriptionComboBox_gbc.gridx = 1;
            needPrescriptionComboBox_gbc.gridy = 5;
            needPrescriptionComboBox_gbc.gridwidth = 2;
            needPrescriptionComboBox_gbc.fill = GridBagConstraints.HORIZONTAL;
            needPrescriptionComboBox_gbc.anchor = GridBagConstraints.WEST;
            needPrescriptionComboBox_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(needPrescriptionComboBox, needPrescriptionComboBox_gbc);
        }
        {
            medicineDescriptionLabel = new JLabel("Description:");
            medicineDescriptionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            medicineDescriptionLabel.setFont(titleFont);
            GridBagConstraints medicineDescriptionLabel_gbc = new GridBagConstraints();
            medicineDescriptionLabel_gbc.gridx = 0;
            medicineDescriptionLabel_gbc.gridy = 6;
            medicineDescriptionLabel_gbc.insets = new Insets(10, 5, 10, 10);
            medicineDescriptionLabel_gbc.anchor = GridBagConstraints.EAST;
            mainPanel.add(medicineDescriptionLabel, medicineDescriptionLabel_gbc);

            medicineDescriptionTextArea = new JTextArea();
            medicineDescriptionTextArea.setLineWrap(true);
            medicineDescriptionTextArea.setEditable(true);
            medicineDescriptionTextArea.setFont(descriptionFont);

            descriptionScrollPanel = new JScrollPane(medicineDescriptionTextArea);
            GridBagConstraints descriptionScrollPanel_gbc = new GridBagConstraints();
            descriptionScrollPanel_gbc.gridx = 1;
            descriptionScrollPanel_gbc.gridy = 6;
            descriptionScrollPanel_gbc.gridwidth = 2;
            descriptionScrollPanel_gbc.fill = GridBagConstraints.BOTH;
            descriptionScrollPanel_gbc.anchor = GridBagConstraints.WEST;
            descriptionScrollPanel_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(descriptionScrollPanel, descriptionScrollPanel_gbc);
        }
        {
            howToConsumeLabel = new JLabel("How to Consume:");
            howToConsumeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            howToConsumeLabel.setFont(titleFont);
            GridBagConstraints howToConsumeLabel_gbc = new GridBagConstraints();
            howToConsumeLabel_gbc.gridx = 0;
            howToConsumeLabel_gbc.gridy = 7;
            howToConsumeLabel_gbc.anchor = GridBagConstraints.EAST;
            howToConsumeLabel_gbc.insets = new Insets(10, 5, 10, 10);
            mainPanel.add(howToConsumeLabel, howToConsumeLabel_gbc);

            howToConsumeTextField = new JTextField();
            howToConsumeTextField.setFont(descriptionFont);
            GridBagConstraints howToConsumeTextField_gbc = new GridBagConstraints();
            howToConsumeTextField_gbc.gridx = 1;
            howToConsumeTextField_gbc.gridy = 7;
            howToConsumeTextField_gbc.gridwidth = 2;
            howToConsumeTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
            howToConsumeTextField_gbc.anchor = GridBagConstraints.WEST;
            howToConsumeTextField_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(howToConsumeTextField, howToConsumeTextField_gbc);
        }
        {
            maxPurchaseAmountLabel = new JLabel("Set Max. Purchase Amount:");
            maxPurchaseAmountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            maxPurchaseAmountLabel.setFont(titleFont);
            GridBagConstraints maxPurchaseAmountLabel_gbc = new GridBagConstraints();
            maxPurchaseAmountLabel_gbc.gridx = 0;
            maxPurchaseAmountLabel_gbc.gridy = 8;
            maxPurchaseAmountLabel_gbc.anchor = GridBagConstraints.EAST;
            maxPurchaseAmountLabel_gbc.insets = new Insets(10, 5, 10, 10);
            mainPanel.add(maxPurchaseAmountLabel, maxPurchaseAmountLabel_gbc);
            
            maxPurchaseAmountTextField = new JTextField();
            maxPurchaseAmountTextField.setFont(descriptionFont);
            GridBagConstraints setMaxPurchaseAmount_gbc = new GridBagConstraints();
            setMaxPurchaseAmount_gbc.gridx = 1;
            setMaxPurchaseAmount_gbc.gridy = 8;
            setMaxPurchaseAmount_gbc.anchor = GridBagConstraints.WEST;
            setMaxPurchaseAmount_gbc.fill = GridBagConstraints.BOTH;
            setMaxPurchaseAmount_gbc.insets = new Insets(10, 0, 10, 10);
            mainPanel.add(maxPurchaseAmountTextField, setMaxPurchaseAmount_gbc);


            DefaultComboBoxModel<String> typeComboBoxModel = new DefaultComboBoxModel<String>();
            typeComboBoxModel.addElement("");
            typeComboBoxModel.addElement("Cream");
            typeComboBoxModel.addElement("Envelope");
            typeComboBoxModel.addElement("Eye Drop");
            typeComboBoxModel.addElement("Liquid");
            typeComboBoxModel.addElement("Sachet");
            typeComboBoxModel.addElement("Strip");
            typeComboBoxModel.addElement("Syrup");
            typeComboBoxModel.addElement("Tablet");

            medicineType = new JComboBox<String>(typeComboBoxModel);
            medicineType.setEditable(true);
            medicineType.setFont(descriptionFont);
            GridBagConstraints medicineType_gbc = new GridBagConstraints();
            medicineType_gbc.gridx = 2;
            medicineType_gbc.gridy = 8;
            medicineType_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(medicineType, medicineType_gbc);
        }
        {
            buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            okButton = new JButton("Ok");
            okButton.addActionListener(new AddMedicine());
            buttonsPanel.add(okButton);

            cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            buttonsPanel.add(cancelButton);
        }

        String fileName = "src/File_Data/Medicine_List.bin";
        
        medicineFile = new File(fileName);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    // public static void main(String[] args) {
    //     AddMedicineDialog frame = new AddMedicineDialog();
    //     frame.setVisible(true);
    // }

    /*
     * The class that does the job if the user adds a medicine
     * This class will check all the conditions for the user
     * to add the medicine, and write to the file the new medicine
     */
    private class AddMedicine implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            double price;
            double stock;
            int maxPurchaseAmount;
            int i;

            String errorMsg = "Invalid Inputted Data";

            LinkedList<Medicine> medicineLinkedList = new LinkedList<Medicine>();

            if(medicineNameTextField.getText().isEmpty() == false ||
                priceTextField.getText().isEmpty() == false ||
                stockTextField.getText().isEmpty() == false ||
                expiredDateTextField.getText().isEmpty() == false ||
                needPrescriptionComboBox.getSelectedIndex() != 0||
                medicineDescriptionTextArea.getText().isEmpty() == false ||
                howToConsumeTextField.getText().isEmpty() == false ||
                maxPurchaseAmountTextField.getText().isEmpty() == false ||
                medicineType.getSelectedIndex() != 0)
            {
                try{
                    price = Double.parseDouble(priceTextField.getText());
                    stock = Double.parseDouble(stockTextField.getText());
                    maxPurchaseAmount = Integer.parseInt(maxPurchaseAmountTextField.getText());

                    if(checkExpiredDate() == false){
                        errorMsg = "Invalid date";
                        throw new MyException(errorMsg);
                    }

                    if(maxPurchaseAmount <= 0){
                        throw new MyException(errorMsg);
                    }

                    fi = new FileInputStream(medicineFile);
                    in = new ObjectInputStream(fi);

                    medicineLinkedList = (LinkedList<Medicine>) in.readObject();

                    fi.close();
                    in.close();
                    
                    for(i = 0; i < medicineLinkedList.size(); i++){
                        if(medicineNameTextField.getText().equals(medicineLinkedList.get(i).getMedicineName())){
                            errorMsg = "Duplicated Medicine Name";
                            throw new MyException(errorMsg);
                        }
                    }

                    if(needPrescriptionComboBox.getSelectedIndex() == 1){
                        MedicineWithPrescription medicineWithPrescription = new MedicineWithPrescription();
    
                        medicineWithPrescription.setMedicineName(medicineNameTextField.getText());
                        medicineWithPrescription.setPrice(price);
                        medicineWithPrescription.setStock(stock);
                        medicineWithPrescription.setExpiredDate(expiredDateTextField.getText());
                        medicineWithPrescription.setDescription(medicineDescriptionTextArea.getText());
                        medicineWithPrescription.setHowToConsume(howToConsumeTextField.getText());
                        medicineWithPrescription.setMaxPurchaseAmount(maxPurchaseAmount);
                        medicineWithPrescription.setType(medicineType.getSelectedItem().toString());
                        
                        medicineLinkedList.add(medicineWithPrescription);
                    }
                    else if(needPrescriptionComboBox.getSelectedIndex() == 2){
                        MedicineWithoutPrescription medicineWithoutPrescription = new MedicineWithoutPrescription();
    
                        medicineWithoutPrescription.setMedicineName(medicineNameTextField.getText());
                        medicineWithoutPrescription.setPrice(price);
                        medicineWithoutPrescription.setStock(stock);
                        medicineWithoutPrescription.setExpiredDate(expiredDateTextField.getText());
                        medicineWithoutPrescription.setDescription(medicineDescriptionTextArea.getText());
                        medicineWithoutPrescription.setHowToConsume(howToConsumeTextField.getText());
                        medicineWithoutPrescription.setMaxPurchaseAmount(maxPurchaseAmount);
                        medicineWithoutPrescription.setType(medicineType.getSelectedItem().toString());
                        
                        medicineLinkedList.add(medicineWithoutPrescription);
                    }

                    fo = new FileOutputStream(medicineFile, false);
                    out = new ObjectOutputStream(fo);

                    out.writeObject(medicineLinkedList);

                    out.close();
                    fo.close();
                }
                catch(NumberFormatException | MyException e2){
                    JOptionPane.showMessageDialog(null, errorMsg,
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                }
                catch(ClassNotFoundException | IOException e2){
                    JOptionPane.showMessageDialog(null, "File Error",
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                }

                dispose();
            }
            else{
                try {
                    throw new MyException("Invalid");
                }
                catch (MyException e1) {
                    JOptionPane.showMessageDialog(null, "Invalid Inputted Data",
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /*
     * The method to check the inputted expired date by
     * the user is valid or not. The pre-requisite is that
     * the expired date should be at least at the following year
     */
    private boolean checkExpiredDate(){
        boolean flag;

        Calendar today = Calendar.getInstance();

        int year = today.get(Calendar.YEAR);

        String split[] = expiredDateTextField.getText().split("-");

        try{
            int inputtedDay = Integer.parseInt(split[0]);
            int inputtedMonth = Integer.parseInt(split[1]);
            int inputtedYear = Integer.parseInt(split[2]);

            if(inputtedYear > year){
                if(inputtedMonth == 1 || inputtedMonth == 3 || inputtedMonth == 5 || inputtedMonth == 7 || inputtedMonth == 8 ||
                inputtedMonth == 10 || inputtedMonth == 12){
                    if(inputtedDay > 31){
                        throw new MyException("Invalid date");
                    }
                }
                else if(inputtedMonth == 4 || inputtedMonth == 6 || inputtedMonth == 9 || inputtedMonth == 11){
                    if(inputtedDay > 30){
                        throw new MyException("Invalid date");
                    }
                }
                else if(inputtedMonth == 2){
                    // check for leap year
                    if (inputtedYear % 4 == 0){

                        // if the year is century
                        if (inputtedYear % 100 == 0){
                  
                            // if year is divided by 400
                            // then it is a leap year
                            if(inputtedYear % 400 == 0){
                                if(inputtedDay > 29){
                                    throw new MyException("Invalid date");
                                }
                            }
                            else{
                                if(inputtedDay > 28){
                                    throw new MyException("Invalid date");
                                }
                            }
                        }
                        else{
                            if(inputtedDay > 29){
                                throw new MyException("Invalid date");
                            }
                        }
                    }
                    else{
                        if(inputtedDay > 28){
                            throw new MyException("Invalid date");
                        }
                    }
                }
                else{
                    throw new MyException("Invalid date");
                }
            }
            else{
                throw new MyException("Invalid date");
            }

            flag = true;
        }
        catch(NumberFormatException | MyException e1){
            flag = false;
        }

        return flag;
    }
}