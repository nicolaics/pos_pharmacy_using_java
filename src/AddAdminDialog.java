import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/*
 * The dialog to add a new administrator
 */
public class AddAdminDialog extends JDialog {
    private JPanel mainPanel;
    private JPanel buttonsPanel;
    
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel confirmPasswordLabel;
    
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private JButton addButton;
    private JButton cancelButton;

    private File credentialsFile;
    private FileInputStream fi;
    private ObjectInputStream in;
    private FileOutputStream fo;
    private MyObjectOutputStream myOut;
    
    public AddAdminDialog(JFrame frame){
        super(frame, true);

        setTitle("Add New Administrator");
        setSize(500, 250);
        setLayout(new BorderLayout());

        GridBagLayout mainLayout = new GridBagLayout();
		mainLayout.columnWidths = new int[] {10, 20};
		mainLayout.rowHeights = new int[] {0, 0, 0};
		mainLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		mainLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};

        mainPanel = new JPanel(mainLayout);

        usernameLabel = new JLabel("Username:");
        GridBagConstraints usernameLabel_gbc = new GridBagConstraints();
        usernameLabel_gbc.gridx = 0;
        usernameLabel_gbc.gridy = 0;
        usernameLabel_gbc.anchor = GridBagConstraints.EAST;
        usernameLabel_gbc.insets = new Insets(0, 20, 15, 10);
        mainPanel.add(usernameLabel, usernameLabel_gbc);

        passwordLabel = new JLabel("Password:");
        GridBagConstraints passwordLabel_gbc = new GridBagConstraints();
        passwordLabel_gbc.gridx = 0;
        passwordLabel_gbc.gridy = 1;
        passwordLabel_gbc.anchor = GridBagConstraints.EAST;
        passwordLabel_gbc.insets = new Insets(0, 20, 10, 10);
        mainPanel.add(passwordLabel, passwordLabel_gbc);

        confirmPasswordLabel = new JLabel("Confirm Password:");
        GridBagConstraints confirmPasswordLabel_gbc = new GridBagConstraints();
        confirmPasswordLabel_gbc.gridx = 0;
        confirmPasswordLabel_gbc.gridy = 2;
        confirmPasswordLabel_gbc.anchor = GridBagConstraints.EAST;
        confirmPasswordLabel_gbc.insets = new Insets(0, 20, 10, 10);
        mainPanel.add(confirmPasswordLabel, confirmPasswordLabel_gbc);

        usernameTextField = new JTextField();
        usernameTextField.setColumns(20);
        GridBagConstraints usernameTextField_gbc = new GridBagConstraints();
        usernameTextField_gbc.gridx = 1;
        usernameTextField_gbc.gridy = 0;
        usernameTextField_gbc.fill = GridBagConstraints.BOTH;
        usernameTextField_gbc.insets = new Insets(0, 0, 15, 20);
        mainPanel.add(usernameTextField, usernameTextField_gbc);

        passwordField = new JPasswordField();
        passwordField.setColumns(20);
        GridBagConstraints passwordField_gbc = new GridBagConstraints();
        passwordField_gbc.gridx = 1;
        passwordField_gbc.gridy = 1;
        passwordField_gbc.fill = GridBagConstraints.BOTH;
        passwordField_gbc.insets = new Insets(0, 0, 10, 20);
        mainPanel.add(passwordField, passwordField_gbc);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setColumns(20);
        GridBagConstraints confirmPasswordField_gbc = new GridBagConstraints();
        confirmPasswordField_gbc.gridx = 1;
        confirmPasswordField_gbc.gridy = 2;
        confirmPasswordField_gbc.fill = GridBagConstraints.BOTH;
        confirmPasswordField_gbc.insets = new Insets(0, 0, 10, 20);
        mainPanel.add(confirmPasswordField, confirmPasswordField_gbc);

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        addButton = new JButton("Add Administrator");
        addButton.addActionListener(new getAdminCredentials());
        buttonsPanel.add(addButton);
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
     * The class to get all of the administrator's credentials
     * from the text field inputted by the user and also from the file.
     * And check whether the username and password is valid
     */
    private class getAdminCredentials implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            String errorMsg = "";
            String username;
            String password;
            String confirmPassword;

            Admin adminCredentials = new Admin();
            Admin newAdmin = new Admin();

            try{
                if(usernameTextField.getText().isEmpty() == true){
                    errorMsg = "Username field cannot be empty!";
                    throw new MyException(errorMsg);
                }

                password = new String(passwordField.getPassword());
                confirmPassword = new String(confirmPasswordField.getPassword());

                if(password.isBlank() == true){
                    errorMsg = "Password field cannot be empty!";
                    throw new MyException(errorMsg);
                }

                if(password.equals(confirmPassword) == false){
                    errorMsg = "Password do not match!";
                    throw new MyException(errorMsg);
                }

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

                    errorMsg = "The username is not valid!";
                    JOptionPane.showMessageDialog(null,  errorMsg, "Error!", JOptionPane.ERROR_MESSAGE);
                }
                catch (IOException | ClassNotFoundException e1){
                    try {
                        fi.close();
                        in.close();
                        
                        newAdmin.setUsername(username);
                        newAdmin.setPassword(confirmPassword);

                        fo = new FileOutputStream(credentialsFile, true);
                        myOut = new MyObjectOutputStream(fo);

                        myOut.writeObject(newAdmin);

                        myOut.close();
                        fo.close();

                        JOptionPane.showMessageDialog(null, "Admin Successfully Added!", "Success!", JOptionPane.INFORMATION_MESSAGE);

                        dispose();
                    }
                    catch (IOException e2) {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                    }
                }               
            }
            catch(MyException e1){
                JOptionPane.showMessageDialog(null,  errorMsg, "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        AddAdminDialog addAdminDialog = new AddAdminDialog(null);
        addAdminDialog.setVisible(true);
    }
}
