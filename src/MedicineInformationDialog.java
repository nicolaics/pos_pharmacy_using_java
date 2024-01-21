import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/*
 * The dialog to show the detailed information of a medicine
 */
public class MedicineInformationDialog <T extends Medicine> extends JDialog {
    private JPanel mainPanel;
    private JPanel buttonsPanel;

    private JScrollPane descriptionScrollPanel;

    private JLabel medicinePicture;
    private JLabel medicineNameLabel;
    private JLabel medicineNameFromDataLabel;
    private JLabel stockLabel;
    private JLabel stockFromDataLabel;
    private JLabel priceLabel;
    private JLabel priceFromDataLabel;
    private JLabel expiredDateLabel;
    private JLabel expiredDateFromDataLabel;
    private JLabel typeLabel;
    private JLabel typeFromDataLabel;
    private JLabel needPrescriptionLabel;
    private JLabel needPrescriptionFromDataLabel;
    private JLabel medicineDescriptionLabel;
    private JTextArea medicineDescriptionFromDataTextArea;
    private JLabel howToConsumeLabel;
    private JLabel howToConsumeFromDataLabel;
    private JLabel amountLabel;
    private JSpinner amountSpinner;
    private JLabel medicineType;
    private JLabel subtotalLabel;
    private JLabel subtotalPriceLabel;

    private JButton addToCartButton;
    private JButton cancelButton;

    private String medicineName;

    private ImageIcon medicineIcon;
    private Image medicineImage;
    
    private double subtotal;
    private int amountBought;
    private int maxPurchaseAmount;

    private T medicine;

    private File medicineBoughtFile;
    private FileOutputStream fo;
    private ObjectOutputStream out;
    private MyObjectOutputStream myOut;

    public MedicineInformationDialog(JFrame frame, boolean needPrescription, T temp){
        super(frame, true);

        setTitle("Medicine Information");
        setSize(900, 700);
        setLayout(new BorderLayout());

        String fileName = "src/image/";
    
        medicine = temp;

        medicineName = medicine.getMedicineName();

        if(needPrescription == true){
            needPrescriptionFromDataLabel = new JLabel("YES");
            fileName += "with-prescription/" + medicineName.toLowerCase() + ".png";
        }
        else{
            needPrescriptionFromDataLabel = new JLabel("NO");
            fileName += "without-prescription/" + medicineName.toLowerCase() + ".png";
        }


        medicineIcon = new ImageIcon(fileName);
        medicineImage = medicineIcon.getImage();
        Image newSize = medicineImage.getScaledInstance(350, 350,  java.awt.Image.SCALE_SMOOTH); // resize the image
        medicineIcon = new ImageIcon(newSize);
        
        // add the wallpaper to the frame
        medicinePicture = new JLabel(medicineIcon);
        add(medicinePicture, BorderLayout.WEST);
        
        GridBagLayout mainLayout = new GridBagLayout();
        mainLayout.columnWidths = new int[]{0, 0, 0, 0};
        mainLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        mainLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        mainLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};


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

            medicineNameFromDataLabel = new JLabel(medicineName);
            medicineNameFromDataLabel.setFont(descriptionFont);
            GridBagConstraints medicineNameFromDataLabel_gbc = new GridBagConstraints();
            medicineNameFromDataLabel_gbc.gridx = 1;
            medicineNameFromDataLabel_gbc.gridy = 0;
            medicineNameFromDataLabel_gbc.gridwidth = 2;
            medicineNameFromDataLabel_gbc.fill = GridBagConstraints.HORIZONTAL;
            medicineNameFromDataLabel_gbc.anchor = GridBagConstraints.WEST;
            medicineNameFromDataLabel_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(medicineNameFromDataLabel, medicineNameFromDataLabel_gbc);
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

            stockFromDataLabel = new JLabel(String.format("%,.0f", medicine.getStock()));
            stockFromDataLabel.setFont(descriptionFont);
            GridBagConstraints stockFromDataLabel_gbc = new GridBagConstraints();
            stockFromDataLabel_gbc.gridx = 1;
            stockFromDataLabel_gbc.gridy = 1;
            stockFromDataLabel_gbc.gridwidth = 2;
            stockFromDataLabel_gbc.fill = GridBagConstraints.HORIZONTAL;
            stockFromDataLabel_gbc.anchor = GridBagConstraints.WEST;
            stockFromDataLabel_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(stockFromDataLabel, stockFromDataLabel_gbc);
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

            priceFromDataLabel = new JLabel(String.format("%,.0f", medicine.getPrice()));
            priceFromDataLabel.setFont(descriptionFont);
            GridBagConstraints priceFromDataLabel_gbc = new GridBagConstraints();
            priceFromDataLabel_gbc.gridx = 1;
            priceFromDataLabel_gbc.gridy = 2;
            priceFromDataLabel_gbc.gridwidth = 2;
            priceFromDataLabel_gbc.fill = GridBagConstraints.HORIZONTAL;
            priceFromDataLabel_gbc.anchor = GridBagConstraints.WEST;
            priceFromDataLabel_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(priceFromDataLabel, priceFromDataLabel_gbc);
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

            expiredDateFromDataLabel = new JLabel(medicine.getExpiredDate());
            expiredDateFromDataLabel.setFont(descriptionFont);
            GridBagConstraints expiredDateFromDataLabel_gbc = new GridBagConstraints();
            expiredDateFromDataLabel_gbc.gridx = 1;
            expiredDateFromDataLabel_gbc.gridy = 3;
            expiredDateFromDataLabel_gbc.gridwidth = 2;
            expiredDateFromDataLabel_gbc.fill = GridBagConstraints.HORIZONTAL;
            expiredDateFromDataLabel_gbc.anchor = GridBagConstraints.WEST;
            expiredDateFromDataLabel_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(expiredDateFromDataLabel, expiredDateFromDataLabel_gbc);
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

            typeFromDataLabel = new JLabel(medicine.getType());
            typeFromDataLabel.setFont(descriptionFont);
            GridBagConstraints typeFromDataLabel_gbc = new GridBagConstraints();
            typeFromDataLabel_gbc.gridx = 1;
            typeFromDataLabel_gbc.gridy = 4;
            typeFromDataLabel_gbc.gridwidth = 2;
            typeFromDataLabel_gbc.fill = GridBagConstraints.HORIZONTAL;
            typeFromDataLabel_gbc.anchor = GridBagConstraints.WEST;
            typeFromDataLabel_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(typeFromDataLabel, typeFromDataLabel_gbc);
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

            needPrescriptionFromDataLabel.setFont(new Font("Arial Narrow", Font.BOLD, 16));
            needPrescriptionFromDataLabel.setForeground(Color.RED);
            GridBagConstraints needPrescriptionFromDataLabel_gbc = new GridBagConstraints();
            needPrescriptionFromDataLabel_gbc.gridx = 1;
            needPrescriptionFromDataLabel_gbc.gridy = 5;
            needPrescriptionFromDataLabel_gbc.gridwidth = 2;
            needPrescriptionFromDataLabel_gbc.fill = GridBagConstraints.HORIZONTAL;
            needPrescriptionFromDataLabel_gbc.anchor = GridBagConstraints.WEST;
            needPrescriptionFromDataLabel_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(needPrescriptionFromDataLabel, needPrescriptionFromDataLabel_gbc);
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

            medicineDescriptionFromDataTextArea = new JTextArea(medicine.getDescription());
            medicineDescriptionFromDataTextArea.setLineWrap(true);
            medicineDescriptionFromDataTextArea.setEditable(false);
            medicineDescriptionFromDataTextArea.setFont(descriptionFont);
            medicineDescriptionFromDataTextArea.setBorder(new EmptyBorder(0,0,0,0));
            medicineDescriptionFromDataTextArea.setBackground(new Color(238, 238, 238));

            descriptionScrollPanel = new JScrollPane(medicineDescriptionFromDataTextArea);
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

            howToConsumeFromDataLabel = new JLabel(medicine.getHowToConsume());
            howToConsumeFromDataLabel.setFont(descriptionFont);
            GridBagConstraints howToConsumeFromDataLabel_gbc = new GridBagConstraints();
            howToConsumeFromDataLabel_gbc.gridx = 1;
            howToConsumeFromDataLabel_gbc.gridy = 7;
            howToConsumeFromDataLabel_gbc.gridwidth = 2;
            howToConsumeFromDataLabel_gbc.fill = GridBagConstraints.HORIZONTAL;
            howToConsumeFromDataLabel_gbc.anchor = GridBagConstraints.WEST;
            howToConsumeFromDataLabel_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(howToConsumeFromDataLabel, howToConsumeFromDataLabel_gbc);
        }
        {
            subtotalLabel = new JLabel("Subtotal:");
            subtotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            subtotalLabel.setFont(titleFont);
            GridBagConstraints subtotalLabel_gbc = new GridBagConstraints();
            subtotalLabel_gbc.gridx = 0;
            subtotalLabel_gbc.gridy = 9;
            subtotalLabel_gbc.anchor = GridBagConstraints.EAST;
            subtotalLabel_gbc.insets = new Insets(10, 5, 10, 10);
            mainPanel.add(subtotalLabel, subtotalLabel_gbc);
            
            subtotalPriceLabel = new JLabel("0");
            subtotalPriceLabel.setFont(descriptionFont);
            GridBagConstraints subtotalPriceLabel_gbc = new GridBagConstraints();
            subtotalPriceLabel_gbc.gridx = 1;
            subtotalPriceLabel_gbc.gridy = 9;
            subtotalPriceLabel_gbc.gridwidth = 2;
            subtotalPriceLabel_gbc.fill = GridBagConstraints.HORIZONTAL;
            subtotalPriceLabel_gbc.anchor = GridBagConstraints.WEST;
            subtotalPriceLabel_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(subtotalPriceLabel, subtotalPriceLabel_gbc);
        }
        {
            amountLabel = new JLabel("Amount:");
            amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            amountLabel.setFont(titleFont);
            GridBagConstraints amountLabel_gbc = new GridBagConstraints();
            amountLabel_gbc.gridx = 0;
            amountLabel_gbc.gridy = 8;
            amountLabel_gbc.anchor = GridBagConstraints.EAST;
            amountLabel_gbc.insets = new Insets(10, 5, 10, 10);
            mainPanel.add(amountLabel, amountLabel_gbc);
            
            maxPurchaseAmount = medicine.getMaxPurchaseAmount();

            amountSpinner = new JSpinner();
            amountSpinner.setModel(new SpinnerNumberModel(0, 0, maxPurchaseAmount, 1));
            amountSpinner.setFont(descriptionFont);
            amountSpinner.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    amountBought = (Integer) amountSpinner.getValue();
                    subtotal = medicine.calculateSubtotal(medicine.getPrice(), amountBought);
                    subtotalPriceLabel.setText(String.format("%,.0f", subtotal));
                }
                
            });
            GridBagConstraints amountSpinner_gbc = new GridBagConstraints();
            amountSpinner_gbc.gridx = 1;
            amountSpinner_gbc.gridy = 8;
            amountSpinner_gbc.anchor = GridBagConstraints.WEST;
            amountSpinner_gbc.fill = GridBagConstraints.BOTH;
            amountSpinner_gbc.insets = new Insets(10, 0, 10, 10);
            mainPanel.add(amountSpinner, amountSpinner_gbc);

            if(medicine.getType().equals("Syrup") || medicine.getType().equals("Liquid") || medicine.getType().equals("Eye Drop")){
                medicineType = new JLabel("Bottle");    
            }
            else{
                medicineType = new JLabel(medicine.getType());
            }
            medicineType.setFont(descriptionFont);
            GridBagConstraints medicineType_gbc = new GridBagConstraints();
            medicineType_gbc.gridx = 2;
            medicineType_gbc.gridy = 8;
            medicineType_gbc.insets = new Insets(10, 0, 10, 20);
            mainPanel.add(medicineType, medicineType_gbc);
        }

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        fileName = "src/File_Data/Invoice_1_Customer.bin";
        medicineBoughtFile = new File(fileName);

        addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(new AddToCart());

        buttonsPanel.add(addToCartButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonsPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    // public static void main(String[] args) {
    //     MedicineInformationDialog<MedicineWithoutPrescription> frame = new MedicineInformationDialog<>();
    //     frame.setVisible(true);
    // }

    /*
     * If the user decided to add to cart the selected medicine,
     * get the data selected by the user
     */
    private class AddToCart implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(amountBought > 0){
                medicine.setSubtotal(subtotal);
                medicine.setAmountBought(amountBought);
        
                try {
                    fo = new FileOutputStream(medicineBoughtFile, true);

                    if(medicineBoughtFile.length() == 0){
                        out = new ObjectOutputStream(fo);
                        out.writeObject(medicine);
                        out.close();
                    }
                    else{
                        myOut = new MyObjectOutputStream(fo);
                        myOut.writeObject(medicine);
                        myOut.close();
                    }

                    fo.close();

                    JOptionPane.showMessageDialog(null, "Added to Cart!", "", JOptionPane.INFORMATION_MESSAGE);
                }
                catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                dispose();
            }
            else{
                JOptionPane.showMessageDialog(null, "Amount is 0! Item is not added to cart!", "", JOptionPane.INFORMATION_MESSAGE);
            }
        }

    }
}