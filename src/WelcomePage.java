import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/*
 * The main page of this software
 */
public class WelcomePage extends JFrame{
    private JFrame mainFrame = this;

    private JPanel mainPanel;

    private JLabel welcomeLabel;
    
    private JButton continueButton;
    private JButton administratorButton;

    private Customer customerData;
    
    private File tempFile;
    private File customerDataFile;

    private FileInputStream fi;
    private ObjectInputStream in;
    private FileInputStream fiTempFile;
    private ObjectInputStream inTempFile;

    public WelcomePage(){
        mainFrame.setSize(1280, 720);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setTitle("Welcome!");

        GridBagLayout mainLayout = new GridBagLayout();
        mainLayout.columnWidths = new int[] {0};
		mainLayout.rowHeights = new int[] {0, 150, 0};
		mainLayout.columnWeights = new double[]{0.0};
		mainLayout.rowWeights = new double[]{0.0, 0.0, 0.0};

        mainPanel = new JPanel(mainLayout);

        welcomeLabel = new JLabel("Welcome to CMC Pharmacy");
        welcomeLabel.setFont(new Font("Arial Narrow", Font.PLAIN, 48));
        GridBagConstraints gbc_welcomeLabel = new GridBagConstraints();
        gbc_welcomeLabel.gridx = 0;
        gbc_welcomeLabel.gridy = 1;
        mainPanel.add(welcomeLabel, gbc_welcomeLabel);

        continueButton = new JButton("Click to Continue");
        continueButton.addActionListener(new continueToCustomerDataPage());          
        GridBagConstraints gbc_continueButton = new GridBagConstraints();
        gbc_continueButton.gridx = 0;
        gbc_continueButton.gridy = 2;
        mainPanel.add(continueButton, gbc_continueButton);

        administratorButton = new JButton("Admin");
        administratorButton.setOpaque(true);
        administratorButton.setBackground(new Color(238, 238, 238));
        administratorButton.setForeground(new Color(238, 238, 238));
        administratorButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        administratorButton.setMargin(new Insets(20, 20, 20, 20));
        administratorButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                administratorButton.setBackground(Color.WHITE);
                administratorButton.setForeground(Color.BLACK);
                administratorButton.setBorder(new LineBorder(Color.BLACK));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                administratorButton.setBackground(new Color(238, 238, 238));
                administratorButton.setForeground(new Color(238, 238, 238));
                administratorButton.setBorder(new EmptyBorder(0, 0, 0, 0));
            }
        });
        administratorButton.addActionListener(new continueToAdministratorPage());
        GridBagConstraints gbc_administratorButton = new GridBagConstraints();
        gbc_administratorButton.gridx = 0;
        gbc_administratorButton.gridy = 3;
        gbc_administratorButton.insets = new Insets(30, 0, 0, 0);
        mainPanel.add(administratorButton, gbc_administratorButton);

        mainFrame.add(mainPanel);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        WelcomePage frame = new WelcomePage();
        frame.setVisible(true);
    }

    /*
     * If the user is a customer, show the customer information dialog
     */
    private class continueToCustomerDataPage implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean isDataEntered;
            Customer inputtedData = new Customer();

            CustomerInformationDialog customerInformationDialog = new CustomerInformationDialog(mainFrame);

            try {
                customerInformationDialog.setVisible(true);

                tempFile = new File("src/File_Data/temp.bin");

                fiTempFile = new FileInputStream(tempFile);
                inTempFile = new ObjectInputStream(fiTempFile);

                isDataEntered = inTempFile.readBoolean();
                inputtedData = (Customer) inTempFile.readObject();

                fiTempFile.close();
                inTempFile.close();
                
                if(isDataEntered == true){
                    try{
                        customerDataFile = new File("src/File_Data/Customer_Data.bin");
                        fi = new FileInputStream(customerDataFile);
                        in = new ObjectInputStream(fi);

                        customerData = new Customer();

                        while(true){
                            customerData = (Customer)in.readObject();

                            if(customerData.getCustomerName() == inputtedData.getCustomerName())
                                break;
                        }
                    }
                    catch(EOFException e2){
                        MedicineListPage medicineListPage = new MedicineListPage(customerData);                

                        medicineListPage.setVisible(true);
                        
                        dispose();

                        fi.close();
                        in.close();
                        tempFile.delete();
                    }
                    catch(ClassNotFoundException e2){
                        JOptionPane.showMessageDialog(null, "Error! Please call the employee!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            catch (IOException | ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    /*
     * If the user is an admin, show the admin page
     */
    private class continueToAdministratorPage implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            AdministrationLoginDialog loginDialog = new AdministrationLoginDialog(mainFrame);
            loginDialog.setVisible(true);
        }
    }
}
