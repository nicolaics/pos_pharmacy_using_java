import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;

/*
 * The dialog to change the Username of an admin
 */
public class ChangeUsernameDialog extends JDialog {
    private JPanel mainPanel;
    private JPanel buttonsPanel;
    
    private JLabel oldUsernameLabel;
    private JLabel newUsernameLabel;
    
    private JTextField oldUsernameTextField;
    private JTextField newUsernameTextField;

    private JButton doneButton;
    private JButton cancelButton;

    private File credentialsFile;
    private FileInputStream fi;
    private ObjectInputStream in;
    private FileOutputStream fo;
    private ObjectOutputStream out;

    private LinkedList<Admin> adminLinkedList;

    private Admin admin;

    public ChangeUsernameDialog(JFrame frame, Admin temp){
        super(frame, true);

        admin = temp;
        getAdminCredentialsFromFile();

        setTitle("Change Username for \"" + temp.getUsername() + "\"");
        setSize(500, 250);
        setLayout(new BorderLayout());

        GridBagLayout mainLayout = new GridBagLayout();
		mainLayout.columnWidths = new int[] {10, 20};
		mainLayout.rowHeights = new int[] {0, 0};
		mainLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		mainLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};

        mainPanel = new JPanel(mainLayout);

        oldUsernameLabel = new JLabel("Old Username:");
        GridBagConstraints oldUsernameLabel_gbc = new GridBagConstraints();
        oldUsernameLabel_gbc.gridx = 0;
        oldUsernameLabel_gbc.gridy = 0;
        oldUsernameLabel_gbc.anchor = GridBagConstraints.EAST;
        oldUsernameLabel_gbc.insets = new Insets(0, 20, 15, 10);
        mainPanel.add(oldUsernameLabel, oldUsernameLabel_gbc);

        newUsernameLabel = new JLabel("New Username:");
        GridBagConstraints newUsernameLabel_gbc = new GridBagConstraints();
        newUsernameLabel_gbc.gridx = 0;
        newUsernameLabel_gbc.gridy = 1;
        newUsernameLabel_gbc.anchor = GridBagConstraints.EAST;
        newUsernameLabel_gbc.insets = new Insets(0, 20, 10, 10);
        mainPanel.add(newUsernameLabel, newUsernameLabel_gbc);

        oldUsernameTextField = new JTextField(admin.getUsername());
        oldUsernameTextField.setEditable(false);
        oldUsernameTextField.setColumns(20);
        GridBagConstraints oldUsernameField_gbc = new GridBagConstraints();
        oldUsernameField_gbc.gridx = 1;
        oldUsernameField_gbc.gridy = 0;
        oldUsernameField_gbc.fill = GridBagConstraints.BOTH;
        oldUsernameField_gbc.insets = new Insets(0, 0, 15, 20);
        mainPanel.add(oldUsernameTextField, oldUsernameField_gbc);

        newUsernameTextField = new JTextField();
        newUsernameTextField.setColumns(20);
        GridBagConstraints newUsernameField_gbc = new GridBagConstraints();
        newUsernameField_gbc.gridx = 1;
        newUsernameField_gbc.gridy = 1;
        newUsernameField_gbc.fill = GridBagConstraints.BOTH;
        newUsernameField_gbc.insets = new Insets(0, 0, 10, 20);
        mainPanel.add(newUsernameTextField, newUsernameField_gbc);

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        doneButton = new JButton("Done");
        doneButton.addActionListener(new ChangeUsername());
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
     * The class that change the Username if the user has pressed done.
     */
    private class ChangeUsername implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int index;
            String errorMsg = "";

            try{
                if(newUsernameTextField.getText().isEmpty() == true){
                    errorMsg = "New username field cannot be empty!";
                    throw new MyException(errorMsg);
                }

                // the loop to check whether the new username has existed or not
                for(index = 0; index < adminLinkedList.size(); index++){
                    if(adminLinkedList.get(index).getUsername().equals(newUsernameTextField.getText()) == true){
                        errorMsg = "Username has existed already!";
                        throw new MyException(errorMsg);
                    }
                }

                // change the username into the new one
                for(index = 0; index < adminLinkedList.size(); index++){
                    if(adminLinkedList.get(index).getUsername().equals(admin.getUsername()) == true){
                        adminLinkedList.get(index).setUsername(newUsernameTextField.getText());
                        break;
                    }
                }
        
                writeToFile(); // call writeToFile method
                JOptionPane.showMessageDialog(null, "Username Changed Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            catch(MyException e1){
                JOptionPane.showMessageDialog(null,  errorMsg, "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /*
     * writeToFile method is to write to the file
     * the new Username of an admin
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
        ChangeUsernameDialog ChangeUsernameDialog = new ChangeUsernameDialog(null, null);
        ChangeUsernameDialog.setVisible(true);
    }
}
