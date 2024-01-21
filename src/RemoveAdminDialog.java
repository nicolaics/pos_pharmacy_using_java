import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;

/*
 * The dialog to remove an administrator
 */
public class RemoveAdminDialog extends JDialog {
    private JPanel buttonsPanel;

    private JScrollPane adminScrollPane;
    
    private JLabel instructionLabel;
    
    private LinkedList<Admin> adminLinkedList;
    private LinkedList<String> usernameLinkedList;
    private DefaultListModel<String> adminListModel;
    private JList<String> adminList;

    private JButton closeButton;

    private File credentialsFile;
    private FileInputStream fi;
    private ObjectInputStream in;
    private FileOutputStream fo;
    private ObjectOutputStream out;
    
    private int confirm;

    private String usernameSelected;

    public RemoveAdminDialog(JFrame frame){
        super(frame, true);

        setTitle("Remove Administrator");
        setSize(500, 250);
        setLayout(new BorderLayout());

        instructionLabel = new JLabel("Select the Administrator's Username That You Want To Remove");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(instructionLabel, BorderLayout.NORTH);

        adminLinkedList = new LinkedList<Admin>();
        usernameLinkedList = new LinkedList<String>();
        getAdminData();

        adminListModel = new DefaultListModel<String>();
        adminListModel.addAll(usernameLinkedList);

        adminList = new JList<String>(adminListModel);
        adminList.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if(e.getClickCount() == 2){
                   remomveConfirmation();
                }
            }
        });

        adminScrollPane = new JScrollPane(adminList);
        add(adminScrollPane, BorderLayout.CENTER);

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonsPanel.add(closeButton);

        add(buttonsPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    /*
     * The method to get the administrator data from the file
     */
    private void getAdminData(){
        credentialsFile = new File("src/File_Data/Credentials.bin");

        adminLinkedList.clear();
        usernameLinkedList.clear();

        try {
            fi = new FileInputStream(credentialsFile);
            in = new ObjectInputStream(fi);

            while(true){
                Admin tempAdmin = new Admin();

                tempAdmin = (Admin) in.readObject();
                adminLinkedList.add(tempAdmin);

                if(tempAdmin.getUsername().equals("supervisor") == false){
                    usernameLinkedList.add(tempAdmin.getUsername());
                }
            }
        }
        catch(EOFException e){
            try {
                fi.close();
                in.close();
            }
            catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * The method to remove the selected administrator
     * and updates the file
     */
    private void remomveConfirmation(){
        int i;

        usernameSelected = adminList.getSelectedValue();

        confirm = JOptionPane.showConfirmDialog(null, "Confirm delete \"" + usernameSelected + "\"?", "Confirm", JOptionPane.OK_CANCEL_OPTION);

        if(confirm == 0){
            for(i = 0; i < usernameLinkedList.size(); i++){
                if(usernameLinkedList.get(i).equals(usernameSelected)){
                    usernameLinkedList.remove(i);
                    break;
                }
            }

            adminListModel.clear();
            adminList.removeAll();
            adminScrollPane.removeAll();

            adminListModel.addAll(usernameLinkedList);
            adminList.setModel(adminListModel);
            adminScrollPane.add(adminList);

            revalidate();
            repaint();

            for(i = 0; i < adminLinkedList.size(); i++){
                if(adminLinkedList.get(i).getUsername().equals(usernameSelected)){
                    adminLinkedList.remove(i);
                    break;
                }
            }

            try {
                fo = new FileOutputStream(credentialsFile, false);
                out = new ObjectOutputStream(fo);

                for(i = 0; i < adminLinkedList.size(); i++){
                    out.writeObject(adminLinkedList.get(i));
                }

                out.close();
                fo.close();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        RemoveAdminDialog loginDialog = new RemoveAdminDialog(null);
        loginDialog.setVisible(true);
    }
}
