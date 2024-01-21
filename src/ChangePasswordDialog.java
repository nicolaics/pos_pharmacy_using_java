import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;

/*
 * The dialog to change the password of an admin
 */
public class ChangePasswordDialog extends JDialog {
    private JPanel mainPanel;
    private JPanel buttonsPanel;
    
    private JLabel oldPasswordLabel;
    private JLabel newPasswordLabel;
    private JLabel confirmPasswordLabel;
    
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    private JButton doneButton;
    private JButton cancelButton;

    private File credentialsFile;
    private FileInputStream fi;
    private ObjectInputStream in;
    private FileOutputStream fo;
    private ObjectOutputStream out;

    private LinkedList<Admin> adminLinkedList;

    private Admin admin;

    public ChangePasswordDialog(JFrame frame, Admin temp){
        super(frame, true);

        admin = temp;
        getAdminCredentialsFromFile();

        setTitle("Change Password for \"" + temp.getUsername() + "\"");
        setSize(500, 250);
        setLayout(new BorderLayout());

        GridBagLayout mainLayout = new GridBagLayout();
		mainLayout.columnWidths = new int[] {10, 20};
		mainLayout.rowHeights = new int[] {0, 0, 0};
		mainLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		mainLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};

        mainPanel = new JPanel(mainLayout);

        oldPasswordLabel = new JLabel("Old Password:");
        GridBagConstraints oldPasswordLabel_gbc = new GridBagConstraints();
        oldPasswordLabel_gbc.gridx = 0;
        oldPasswordLabel_gbc.gridy = 0;
        oldPasswordLabel_gbc.anchor = GridBagConstraints.EAST;
        oldPasswordLabel_gbc.insets = new Insets(0, 20, 15, 10);
        mainPanel.add(oldPasswordLabel, oldPasswordLabel_gbc);

        newPasswordLabel = new JLabel("New Password:");
        GridBagConstraints newPasswordLabel_gbc = new GridBagConstraints();
        newPasswordLabel_gbc.gridx = 0;
        newPasswordLabel_gbc.gridy = 1;
        newPasswordLabel_gbc.anchor = GridBagConstraints.EAST;
        newPasswordLabel_gbc.insets = new Insets(0, 20, 10, 10);
        mainPanel.add(newPasswordLabel, newPasswordLabel_gbc);

        confirmPasswordLabel = new JLabel("Confirm Password:");
        GridBagConstraints confirmPasswordLabel_gbc = new GridBagConstraints();
        confirmPasswordLabel_gbc.gridx = 0;
        confirmPasswordLabel_gbc.gridy = 2;
        confirmPasswordLabel_gbc.anchor = GridBagConstraints.EAST;
        confirmPasswordLabel_gbc.insets = new Insets(0, 20, 10, 10);
        mainPanel.add(confirmPasswordLabel, confirmPasswordLabel_gbc);

        oldPasswordField = new JPasswordField();
        oldPasswordField.setColumns(20);
        GridBagConstraints oldPasswordField_gbc = new GridBagConstraints();
        oldPasswordField_gbc.gridx = 1;
        oldPasswordField_gbc.gridy = 0;
        oldPasswordField_gbc.fill = GridBagConstraints.BOTH;
        oldPasswordField_gbc.insets = new Insets(0, 0, 15, 20);
        mainPanel.add(oldPasswordField, oldPasswordField_gbc);

        newPasswordField = new JPasswordField();
        newPasswordField.setColumns(20);
        GridBagConstraints newPasswordField_gbc = new GridBagConstraints();
        newPasswordField_gbc.gridx = 1;
        newPasswordField_gbc.gridy = 1;
        newPasswordField_gbc.fill = GridBagConstraints.BOTH;
        newPasswordField_gbc.insets = new Insets(0, 0, 10, 20);
        mainPanel.add(newPasswordField, newPasswordField_gbc);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setColumns(20);
        GridBagConstraints confirmPasswordField_gbc = new GridBagConstraints();
        confirmPasswordField_gbc.gridx = 1;
        confirmPasswordField_gbc.gridy = 2;
        confirmPasswordField_gbc.fill = GridBagConstraints.BOTH;
        confirmPasswordField_gbc.insets = new Insets(0, 0, 10, 20);
        mainPanel.add(confirmPasswordField, confirmPasswordField_gbc);

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        doneButton = new JButton("Done");
        doneButton.addActionListener(new ChangePassword());
        buttonsPanel.add(doneButton);
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
     * The method to get all of the administrator's credentials
     * from the file
     */
    private void getAdminCredentialsFromFile(){
        Admin adminCredentials = new Admin();

        credentialsFile = new File("src/File_Data/Credentials.bin");

        adminLinkedList = new LinkedList<Admin>();

        try{
            fi = new FileInputStream(credentialsFile);
            in = new ObjectInputStream(fi);

            while(true){
                adminCredentials = (Admin)in.readObject();
                adminLinkedList.add(adminCredentials);
           }
        }
        catch(EOFException e1){
            try {
                fi.close();
                in.close();
            }
            catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
        }
        catch (IOException | ClassNotFoundException e1){
        }
    }

    /*
     * The class that change the password if the user has pressed done.
     */
    private class ChangePassword implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int index;
            String errorMsg = "";

            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            try{
                if(newPassword.isBlank() == true){
                    errorMsg = "Password field cannot be empty!";
                    throw new MyException(errorMsg);
                }

                for(index = 0; index < adminLinkedList.size(); index++){
                    if(adminLinkedList.get(index).getUsername().equals(admin.getUsername()) == true){
                        break;
                    }
                }

                if(adminLinkedList.get(index).getPassword().equals(oldPassword) == false){
                    errorMsg = "The old password is wrong!";
                    throw new MyException(errorMsg);
                }
                else{
                    if(newPassword.equals(confirmPassword) == true){
                        adminLinkedList.get(index).setPassword(confirmPassword);
                        writeToFile(); // call writeToFile method
                        JOptionPane.showMessageDialog(null, "Password Changed Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                    else{
                        errorMsg = "New password do not match!";
                        throw new MyException(errorMsg);
                    }
                }
            }
            catch(MyException e1){
                JOptionPane.showMessageDialog(null,  errorMsg, "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /*
     * writeToFile method is to write to the file
     * the new password of an admin
     */
    private void writeToFile(){
        int i;

        try {
            fo = new FileOutputStream(credentialsFile, false);
            out = new ObjectOutputStream(fo);

            for(i = 0; i < adminLinkedList.size(); i++){
                out.writeObject(adminLinkedList.get(i));
            }

            fo.close();
            out.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ChangePasswordDialog ChangePasswordDialog = new ChangePasswordDialog(null, null);
        ChangePasswordDialog.setVisible(true);
    }
}
