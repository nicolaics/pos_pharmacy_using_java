import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;

/*
 * The final dialog that says printing the receipt
 * and automatically closes this dialog
 */
public class PrintingDialog extends JDialog{
    private JLabel printingLabel;

    private JButton exitButton;

    private String closingString;
    
    private File medicineListFile;
    private FileInputStream fi;
    private ObjectInputStream in;
    private FileOutputStream fo;
    private ObjectOutputStream out;

    private LinkedList<Medicine> medicineList;
    private Invoice<Medicine> customerInvoice;

    private UpdateMedicineFileClass updateMedicineFileWorker;

    public PrintingDialog(JFrame mainFrame, boolean needPrescription, Invoice<Medicine> invoice){
        super(mainFrame, true);

        setTitle("Printing");
        setSize(500, 300);

        customerInvoice = invoice;

        updateMedicineFileWorker = new UpdateMedicineFileClass();
        updateMedicineFileWorker.execute();

        GridBagLayout mainLayout = new GridBagLayout();
        mainLayout.columnWidths = new int[] {500};
		mainLayout.rowHeights = new int[] {200, 30};
		mainLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		mainLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};

        setLayout(new GridBagLayout());

        if(needPrescription == false){
            closingString = "<html><center>NOW PRINTING YOUR RECEIPT..."
                            + "<br>Please show the receipt to the employee!"
                            + "<br>THANK YOU!</center></html>";
        }
        else{
            closingString = "<html><center>NOW PRINTING YOUR RECEIPT..."
                            + "<br>Please show the receipt to the employee!"
                            + "<br>Please prepare the prescription as well!"
                            + "<br>THANK YOU!</center></html>";
        }
        printingLabel = new JLabel(closingString);
        printingLabel.setFont(new Font("Arial Narrow", Font.BOLD, 24));
        GridBagConstraints printingLabel_gbc = new GridBagConstraints();
        printingLabel_gbc.gridx = 0;
        printingLabel_gbc.gridy = 0;
        printingLabel_gbc.anchor = GridBagConstraints.CENTER;
        printingLabel_gbc.fill = GridBagConstraints.BOTH;
        printingLabel_gbc.insets = new Insets(0, 0, 10, 0);
        add(printingLabel, printingLabel_gbc);

        exitButton = new JButton("<html><center>EXIT" +
                                "<br>(Auto close in 5 seconds)</center></html>");
        exitButton.setFont(new Font("Arial Narrow", Font.PLAIN, 16));
        exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        exitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                WelcomePage welcomePage = new WelcomePage();
                
                dispose();
                welcomePage.setVisible(true);
            }

        });
        GridBagConstraints exitButton_gbc = new GridBagConstraints();
        exitButton_gbc.gridx = 0;
        exitButton_gbc.gridy = 1;
        exitButton_gbc.anchor = GridBagConstraints.CENTER;
        exitButton_gbc.insets = new Insets(10, 0, 0, 0);
        add(exitButton, exitButton_gbc);
        
        File deleteFile = new File("src/File_Data/Invoice_1_Customer.bin");
        deleteFile.delete();

        Timer timer = new Timer(5000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                WelcomePage welcomePage = new WelcomePage();
                welcomePage.setVisible(true);

                dispose();
            }
        });

        timer.setRepeats(false);
        timer.start();
    }

    /*
     * Update the Medicine_List.bin file, by subtracting the stock with
     * the stock that the user has bought
     */
    private class UpdateMedicineFileClass extends SwingWorker<Void, Void>{

        @Override
        protected Void doInBackground() throws Exception {
            medicineListFile = new File("src/File_Data/Medicine_List.bin");
            
            try{
                fi = new FileInputStream(medicineListFile);
                in = new ObjectInputStream(fi);

                while(true){
                    medicineList = (LinkedList<Medicine>) in.readObject();
                }
            }
            catch(ClassNotFoundException e1){

            }
            catch(IOException e1){
                fi.close();
                in.close();
            }

            int medicineBoughtCounter;
            int medicineFromFileCounter;
            double newStock;
            String medicineBoughtString;

            for(medicineBoughtCounter = 0; medicineBoughtCounter < customerInvoice.getMedicineBought().size(); medicineBoughtCounter++){
                medicineBoughtString = customerInvoice.getMedicineBought().get(medicineBoughtCounter).getMedicineName();

                for(medicineFromFileCounter = 0; medicineFromFileCounter < medicineList.size(); medicineFromFileCounter++){
                    if(medicineBoughtString.equals(medicineList.get(medicineFromFileCounter).getMedicineName())){
                        newStock = medicineList.get(medicineFromFileCounter).getStock() - customerInvoice.getMedicineBought().get(medicineBoughtCounter).getAmountBought();
                        medicineList.get(medicineFromFileCounter).setStock(newStock);
                    }
                }
            }

            try{
                fo = new FileOutputStream(medicineListFile, false);
                out = new ObjectOutputStream(fo);

                out.writeObject(medicineList);

                fo.close();
                out.close();
            }
            catch(IOException e1){

            }

            return null;
        }
    }

    public static void main(String[] args) {
        PrintingDialog printingDialog = new PrintingDialog(null, false, new Invoice<Medicine>());

        printingDialog.setVisible(true);
    }
}
