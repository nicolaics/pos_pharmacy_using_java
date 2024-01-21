import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/*
 * The dialog for the administrator to edit or remove a medicine
 */
public class EditMedicineDialog extends JDialog {
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
    private JLabel maxPurchaseAmount;
    private JTextField maxPurchaseAmountTextField;
    private JComboBox<String> medicineType;

    private JButton okButton;
    private JButton cancelButton;
    private JButton deleteButton;

    private String medicineName;

    private ImageIcon medicineIcon;
    private Image medicineImage;

    private LinkedList<Medicine> medicineLinkedList;
    private int index;

    private File medicineFile;
    private FileOutputStream fo;
    private ObjectOutputStream out;

    public EditMedicineDialog(JFrame frame, boolean needPrescription, LinkedList<Medicine> temp, int tempIndex){
        super(frame, true);

        setTitle("Edit Medicine Information");
        setSize(900, 700);
        setLayout(new BorderLayout());

        String fileName = "image/";
    
        medicineLinkedList = temp;
        index = tempIndex;

        medicineName = medicineLinkedList.get(index).getMedicineName();

        DefaultComboBoxModel<String> needPrescriptionComboBoxModel = new DefaultComboBoxModel<String>();
        needPrescriptionComboBoxModel.addElement("YES");
        needPrescriptionComboBoxModel.addElement("NO");

        needPrescriptionComboBox = new JComboBox<String>(needPrescriptionComboBoxModel);

        // get the pictures from the file
        if(needPrescription == true){
            needPrescriptionComboBox.setSelectedIndex(0);
            fileName += "with-prescription/" + medicineName.toLowerCase() + ".png";
        }
        else{
            needPrescriptionComboBox.setSelectedIndex(1);
            fileName += "without-prescription/" + medicineName.toLowerCase() + ".png";
        }
        
        medicineIcon = new ImageIcon(fileName);
        medicineImage = medicineIcon.getImage();
        Image newSize = medicineImage.getScaledInstance(350, 350,  java.awt.Image.SCALE_SMOOTH); // resize the image
        medicineIcon = new ImageIcon(newSize);

        medicinePicture = new JLabel(medicineIcon);
        medicinePicture.setToolTipText("To change the picture, read the readme section");
        add(medicinePicture, BorderLayout.WEST);
        
        GridBagLayout mainLayout = new GridBagLayout();
        mainLayout.columnWidths = new int[]{0, 0, 0, 0};
        mainLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        mainLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        mainLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};


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

            medicineNameTextField = new JTextField(medicineName);
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

            stockTextField = new JTextField(String.format("%,.0f", medicineLinkedList.get(index).getStock()));
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

            priceTextField = new JTextField(String.format("%,.0f", medicineLinkedList.get(index).getPrice()));
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

            expiredDateTextField = new JTextField(medicineLinkedList.get(index).getExpiredDate());
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

            typeTextField = new JTextField(medicineLinkedList.get(index).getType());
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

            needPrescriptionComboBox.setEditable(false);
            needPrescriptionComboBox.setFont(new Font("Arial Narrow", Font.BOLD, 16));
            needPrescriptionComboBox.setForeground(Color.RED);
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

            medicineDescriptionTextArea = new JTextArea(medicineLinkedList.get(index).getDescription());
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

            howToConsumeTextField = new JTextField(medicineLinkedList.get(index).getHowToConsume());
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
            maxPurchaseAmount = new JLabel("Set Max. Purchase Amount:");
            maxPurchaseAmount.setHorizontalAlignment(SwingConstants.RIGHT);
            maxPurchaseAmount.setFont(titleFont);
            GridBagConstraints maxPurchaseAmount_gbc = new GridBagConstraints();
            maxPurchaseAmount_gbc.gridx = 0;
            maxPurchaseAmount_gbc.gridy = 8;
            maxPurchaseAmount_gbc.anchor = GridBagConstraints.EAST;
            maxPurchaseAmount_gbc.insets = new Insets(10, 5, 10, 10);
            mainPanel.add(maxPurchaseAmount, maxPurchaseAmount_gbc);
            
            maxPurchaseAmountTextField = new JTextField("" + medicineLinkedList.get(index).getMaxPurchaseAmount());
            maxPurchaseAmountTextField.setFont(descriptionFont);
            GridBagConstraints setMaxPurchaseAmount_gbc = new GridBagConstraints();
            setMaxPurchaseAmount_gbc.gridx = 1;
            setMaxPurchaseAmount_gbc.gridy = 8;
            setMaxPurchaseAmount_gbc.anchor = GridBagConstraints.WEST;
            setMaxPurchaseAmount_gbc.fill = GridBagConstraints.BOTH;
            setMaxPurchaseAmount_gbc.insets = new Insets(10, 0, 10, 10);
            mainPanel.add(maxPurchaseAmountTextField, setMaxPurchaseAmount_gbc);

            DefaultComboBoxModel<String> typeComboBoxModel = new DefaultComboBoxModel<String>();
            typeComboBoxModel.addElement("Cream");
            typeComboBoxModel.addElement("Envelope");
            typeComboBoxModel.addElement("Eye Drop");
            typeComboBoxModel.addElement("Liquid");
            typeComboBoxModel.addElement("Sachet");
            typeComboBoxModel.addElement("Strip");
            typeComboBoxModel.addElement("Syrup");
            typeComboBoxModel.addElement("Tablet");

            medicineType = new JComboBox<String>(typeComboBoxModel);
            medicineType.setSelectedItem(new String(medicineLinkedList.get(index).getType()));
            medicineType.setEditable(true);
            medicineType.setFont(descriptionFont);
            GridBagConstraints medicineType_gbc = new GridBagConstraints();
            medicineType_gbc.gridx = 2;
            medicineType_gbc.gridy = 8;
            medicineType_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(medicineType, medicineType_gbc);
        }

        fileName = "src/File_Data/Medicine_List.bin";
        
        medicineFile = new File(fileName);
        {
            buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            okButton = new JButton("Ok");
            okButton.addActionListener(new EditMedicine());
            buttonsPanel.add(okButton);

            deleteButton = new JButton("Delete Medicine");
            deleteButton.addActionListener(new RemoveMedicine());
            buttonsPanel.add(deleteButton);

            cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            buttonsPanel.add(cancelButton);
        }

        add(mainPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    // public static void main(String[] args) {
    //     EditMedicineDialog<MedicineWithoutPrescription> frame = new EditMedicineDialog<>();
    //     frame.setVisible(true);
    // }

    /*
     * The class that checks the editted fields from the user
     * and edits the medicine as well as updates the
     * database or the file
     */
    private class EditMedicine implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            double price;
            double stock;
            int maxPurchaseAmount;
            int i;

            String combinePrice;
            String combineStock;
            String errorMsg = "Invalid Inputted Data";

            if(medicineNameTextField.getText().isEmpty() == false ||
                priceTextField.getText().isEmpty() == false ||
                stockTextField.getText().isEmpty() == false ||
                expiredDateTextField.getText().isEmpty() == false ||
                medicineDescriptionTextArea.getText().isEmpty() == false ||
                howToConsumeTextField.getText().isEmpty() == false ||
                maxPurchaseAmountTextField.getText().isEmpty() == false ||
                medicineType.getSelectedIndex() != 0)
            {
                try{
                    if(priceTextField.getText().contains(",")){
                        String split[] = priceTextField.getText().split(",");
        
                        combinePrice = split[0] + split[1];
                    }
                    else{
                        combinePrice = priceTextField.getText();
                    }
        
                    if(stockTextField.getText().contains(",")){
                        String split[] = priceTextField.getText().split(",");
        
                        combineStock = split[0] + split[1];
                    }
                    else{
                        combineStock = stockTextField.getText();
                    }

                    price = Double.parseDouble(combinePrice);
                    stock = Double.parseDouble(combineStock);
                    maxPurchaseAmount = Integer.parseInt(maxPurchaseAmountTextField.getText());

                    if(checkExpiredDate() == false){
                        errorMsg = "Invalid date";
                        throw new MyException(errorMsg);
                    }

                    if(maxPurchaseAmount <= 0){
                        throw new MyException(errorMsg);
                    }

                    for(i = 0; i < medicineLinkedList.size(); i++){
                        if(medicineNameTextField.getText().equals(medicineLinkedList.get(i).getMedicineName())){
                            errorMsg = "Duplicated Medicine Name";
                            throw new MyException(errorMsg);
                        }
                    }

                    fo = new FileOutputStream(medicineFile, false);
                    out = new ObjectOutputStream(fo);

                    medicineLinkedList.get(index).setMedicineName(medicineNameTextField.getText());
                    medicineLinkedList.get(index).setPrice(price);
                    medicineLinkedList.get(index).setStock(stock);
                    medicineLinkedList.get(index).setExpiredDate(expiredDateTextField.getText());
                    medicineLinkedList.get(index).setDescription(medicineDescriptionTextArea.getText());
                    medicineLinkedList.get(index).setHowToConsume(howToConsumeTextField.getText());
                    medicineLinkedList.get(index).setMaxPurchaseAmount(maxPurchaseAmount);
                    medicineLinkedList.get(index).setType(medicineType.getSelectedItem().toString());

                    out.writeObject(medicineLinkedList);

                    fo.close();
                    out.close();
                }
                catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Problem with File! Contact Administrator!",
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                }
                catch(NumberFormatException | MyException e2){
                    JOptionPane.showMessageDialog(null, errorMsg,
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            dispose();
        }
    }

    /*
     * The class to remove the selected medicine
     * and updates the file or database
     */
    private class RemoveMedicine implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            medicineLinkedList.remove(index);
            
            try {
                fo = new FileOutputStream(medicineFile, false);
                out = new ObjectOutputStream(fo);

                out.writeObject(medicineLinkedList);

                fo.close();
                out.close();

            }
            catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "Problem with File! Contact Administrator!",
                                                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            dispose();
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