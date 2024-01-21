import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/*
 * The dialog to show the log in page for the
 * administrator
 */
public class AdministrationLoginDialog extends JDialog {
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel buttonsPanel;
    
    private JLabel instructionLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    
    private JTextField usernameTextField;
    private JPasswordField passwordField;

    private JButton okButton;
    private JButton cancelButton;

    private File credentialsFile;

    private FileInputStream fi;
    private ObjectInputStream in;
    
    public AdministrationLoginDialog(JFrame frame){
        super(frame, true);

        mainFrame = frame;

        setTitle("Administrator Login");
        setSize(500, 250);
        setLayout(new BorderLayout());

        GridBagLayout mainLayout = new GridBagLayout();
		mainLayout.columnWidths = new int[] {10, 20};
		mainLayout.rowHeights = new int[] {50, 0, 0};
		mainLayout.columnWeights = new double[]{0.0, 1.0};
		mainLayout.rowWeights = new double[]{0.0, 0.0, 0.0};

        mainPanel = new JPanel(mainLayout);

        instructionLabel = new JLabel("Please input your credentials!");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints instruction_gbc = new GridBagConstraints();
        instruction_gbc.gridx = 0;
        instruction_gbc.gridy = 0;
        instruction_gbc.gridwidth = 2;
        instruction_gbc.fill = GridBagConstraints.BOTH;
        instruction_gbc.anchor = GridBagConstraints.CENTER;
        instruction_gbc.insets = new Insets(0, 20, 0, 20);
        mainPanel.add(instructionLabel, instruction_gbc);

        usernameLabel = new JLabel("Username:");
        GridBagConstraints usernameLabel_gbc = new GridBagConstraints();
        usernameLabel_gbc.gridx = 0;
        usernameLabel_gbc.gridy = 1;
        usernameLabel_gbc.anchor = GridBagConstraints.EAST;
        usernameLabel_gbc.insets = new Insets(0, 20, 15, 10);
        mainPanel.add(usernameLabel, usernameLabel_gbc);

        passwordLabel = new JLabel("Password:");;
        GridBagConstraints passwordLabel_gbc = new GridBagConstraints();
        passwordLabel_gbc.gridx = 0;
        passwordLabel_gbc.gridy = 2;
        passwordLabel_gbc.anchor = GridBagConstraints.EAST;
        passwordLabel_gbc.insets = new Insets(0, 20, 10, 10);
        mainPanel.add(passwordLabel, passwordLabel_gbc);

        usernameTextField = new JTextField();
        usernameTextField.setColumns(20);
        GridBagConstraints usernameTextField_gbc = new GridBagConstraints();
        usernameTextField_gbc.gridx = 1;
        usernameTextField_gbc.gridy = 1;
        usernameTextField_gbc.fill = GridBagConstraints.BOTH;
        usernameTextField_gbc.insets = new Insets(0, 0, 15, 20);
        mainPanel.add(usernameTextField, usernameTextField_gbc);

        passwordField = new JPasswordField();
        passwordField.setColumns(20);
        GridBagConstraints passwordField_gbc = new GridBagConstraints();
        passwordField_gbc.gridx = 1;
        passwordField_gbc.gridy = 2;
        passwordField_gbc.fill = GridBagConstraints.BOTH;
        passwordField_gbc.insets = new Insets(0, 0, 10, 20);
        mainPanel.add(passwordField, passwordField_gbc);

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        okButton = new JButton("Ok");
        okButton.addActionListener(new getAdminCredentials());
        buttonsPanel.add(okButton);
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

    /*
     * The class to check whether the admin credentials inputted
     * exist and match with the one in the file
     */
    private class getAdminCredentials implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(usernameTextField.getText().isEmpty() == true){
                JOptionPane.showMessageDialog(null,  "Username and Password do not match!", "Error!", JOptionPane.ERROR_MESSAGE);
            }
            else{
                String username;
                String password;
                boolean isError = false;
                Admin adminCredentials = new Admin();

                username = usernameTextField.getText();

                credentialsFile = new File("src/File_Data/Credentials.bin");

                try {
                    fi = new FileInputStream(credentialsFile);
                    in = new ObjectInputStream(fi);

                    while(true){
                        adminCredentials = (Admin)in.readObject();

                        if(username.equals(adminCredentials.getUsername())){
                            break;
                        }
                    }

                    fi.close();
                    in.close();
                }
                catch (IOException | ClassNotFoundException e1){
                    try {
                        fi.close();
                        in.close();
                        isError = true;
                        JOptionPane.showMessageDialog(null,  "Username does not exists!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (IOException e2) {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                    }
                }

                if(isError == false){
                    password = new String(passwordField.getPassword());
                    
                    if(adminCredentials.getPassword().equals(password)){
                        if(adminCredentials.getUsername().equals("supervisor") == true){
                            JOptionPane.showMessageDialog(null, "Login Success!", "", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                            mainFrame.dispose();
                            
                            SupervisorAdministrationPage supervisorAdminPage = new SupervisorAdministrationPage(adminCredentials);
                            supervisorAdminPage.setVisible(true);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Login Success!", "", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                            mainFrame.dispose();
                            
                            AdministrationPage adminPage = new AdministrationPage(adminCredentials);
                            adminPage.setVisible(true);
                        }
                    }
                }
            }
            
        }

    }

    // public static void main(String[] args) {
    //     AdministrationLoginDialog loginDialog = new AdministrationLoginDialog();
    //     loginDialog.setVisible(true);
    // }
}
