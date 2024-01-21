import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/*
 * This page is exclusively for the Supervisor Administrator
 * There are some features that are only available if you are
 * the supervisor, or we can say the owner
 */
public class SupervisorAdministrationPage extends JFrame {
    private JFrame thisFrame = this;

    private JMenuBar menuBar;

    private JMenu fileMenu;
    private JMenuItem addMenuItem;
    private JMenuItem viewMedicineListMenuItem;
    private JMenuItem initializeDataMenuItem;

    private JMenu salesMenu;
    private JMenuItem viewDailySalesMenuItem;
    private JMenuItem viewCustomerListMenuItem;

    private JMenu accountMenu;
    private JMenuItem addAdministratorMenuItem;
    private JMenuItem removeAdministratorMenuItem;
    private JMenuItem changePasswordMenuItem;
    private JMenuItem logoutMenuItem;

    private JPanel searchPanel;
    private JScrollPane medicineListScrollPane;

    private JLabel searchLabel;
    private JTextField searchTextField;
    
    private JButton searchButton;

    private JLabel initializingLabel;

    private JProgressBar progressBar;

    private LinkedList<Medicine> medicineLinkedList;
    private LinkedList<String> medicineNameLinkedList;
    private LinkedList<String> searchResultLinkedList;

    private DefaultListModel<String> medicineListModel;
    private JList<String> medicineList;
    
    private File medicineDataFile;
    private FileOutputStream fo;
    private ObjectOutputStream out;
    private FileInputStream fi;
    private ObjectInputStream in;

    private InitializeMedicineData initializeWorker;
    private ViewAllMedicine viewMedicineWorker;

    private Admin admin;

    public SupervisorAdministrationPage(Admin temp){
        setSize(1280, 720);
        setTitle("SUPERVISOR Administration Page");
        setLayout(new BorderLayout(10, 0));

        // save the Admin from previous page to admin
        // so we can use it in this class
        admin = temp;

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // create a new font style
        Font menuFont = new Font("Dialog", Font.PLAIN, 12);
        
        // initializing the File menu bar
        {
            fileMenu = new JMenu("File");
            fileMenu.setFont(menuFont);
            menuBar.add(fileMenu);

            viewMedicineListMenuItem = new JMenuItem("View All Medicines");
            viewMedicineListMenuItem.setFont(menuFont);
            viewMedicineListMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewMedicineWorker = new ViewAllMedicine();

                    viewMedicineWorker.execute();
                }
            });
            fileMenu.add(viewMedicineListMenuItem);

            fileMenu.addSeparator();

            addMenuItem = new JMenuItem("Add Medicine");
            addMenuItem.setFont(menuFont);
            // add medicine
            addMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    AddMedicineDialog addMedicineDialog = new AddMedicineDialog(thisFrame);

                    addMedicineDialog.setVisible(true);
                }

            });
            fileMenu.add(addMenuItem);

            fileMenu.addSeparator();

            // resets the medicine list and the Medicine_List.bin file
            initializeDataMenuItem = new JMenuItem("Initialize");
            initializeDataMenuItem.setFont(menuFont);
            medicineLinkedList = new LinkedList<Medicine>();
            initializeDataMenuItem.addActionListener(new initializeMedicineList());
            fileMenu.add(initializeDataMenuItem);
        }
        // initializing the Sales menu bar
        {
            salesMenu = new JMenu("Sales");
            salesMenu.setFont(menuFont);
            menuBar.add(salesMenu);

            viewDailySalesMenuItem = new JMenuItem("View Daily Sales");
            viewDailySalesMenuItem.setFont(menuFont);
            viewDailySalesMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    ViewDailySalesPage viewDailySalesPage = new ViewDailySalesPage(thisFrame);
                    viewDailySalesPage.setVisible(true);
                }
            });
            salesMenu.add(viewDailySalesMenuItem);

            viewCustomerListMenuItem = new JMenuItem("View Customer List");
            viewCustomerListMenuItem.setFont(menuFont);
            
            salesMenu.add(viewCustomerListMenuItem);
        }
        // initializing the Account menu bar
        {
            accountMenu = new JMenu("Account");
            accountMenu.setFont(menuFont);
            menuBar.add(accountMenu);

            addAdministratorMenuItem = new JMenuItem("Add New Administrator");
            addAdministratorMenuItem.setFont(menuFont);
            addAdministratorMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    AddAdminDialog adminDialog = new AddAdminDialog(thisFrame);
                    adminDialog.setVisible(true);
                }
            });
            accountMenu.add(addAdministratorMenuItem);

            removeAdministratorMenuItem = new JMenuItem("Remove Existing Administrator");
            removeAdministratorMenuItem.setFont(menuFont);
            removeAdministratorMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    RemoveAdminDialog removeAdminDialog = new RemoveAdminDialog(thisFrame);

                    removeAdminDialog.setVisible(true);
                }
            });
            accountMenu.add(removeAdministratorMenuItem);

            accountMenu.addSeparator();

            changePasswordMenuItem = new JMenuItem("Change Password");
            changePasswordMenuItem.setFont(menuFont);
            changePasswordMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog(thisFrame, admin);

                    changePasswordDialog.setVisible(true);
                }
            });
            accountMenu.add(changePasswordMenuItem);

            accountMenu.addSeparator();

            logoutMenuItem = new JMenuItem("Logout");
            logoutMenuItem.setFont(menuFont);
            logoutMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    WelcomePage welcomePage = new WelcomePage();
                    
                    dispose();
                    welcomePage.setVisible(true);
                }
            });
            accountMenu.add(logoutMenuItem);
        }

        // to display all of the medicine list
        medicineNameLinkedList = new LinkedList<String>();
        medicineListModel = new DefaultListModel<String>();
        medicineList = new JList<String>();
        medicineList.setFont(new Font("Arial Narrow", Font.PLAIN, 18));
        medicineList.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if(e.getClickCount() == 2){ // if the list is double-clicked
                   selectMedicine();
                }
            }
        });
        medicineListScrollPane = new JScrollPane();
        add(medicineListScrollPane, BorderLayout.CENTER);

        initializingLabel = new JLabel();
        initializingLabel.setFont(new Font(initializingLabel.getFont().getName(), Font.PLAIN, initializingLabel.getFont().getSize()));
        initializingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        progressBar = new JProgressBar(0, 100);
        add(progressBar, BorderLayout.SOUTH);

        GridBagLayout searchLayout = new GridBagLayout();
        searchLayout.columnWidths = new int[] {0, 0, 0};
		searchLayout.rowHeights = new int[] {0, 0};
		searchLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		searchLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};

        searchPanel = new JPanel(searchLayout);

        searchLabel = new JLabel("Search Keyword:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints searchLabel_gbc = new GridBagConstraints();
        searchLabel_gbc.gridx = 0;
        searchLabel_gbc.gridy = 0;
        searchLabel_gbc.insets = new Insets(10, 20, 10, 10);
        searchPanel.add(searchLabel, searchLabel_gbc);

        searchTextField = new JTextField();
        searchTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        GridBagConstraints searchTextField_gbc = new GridBagConstraints();
        searchTextField_gbc.gridx = 1;
        searchTextField_gbc.gridy = 0;
        searchTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
        searchTextField_gbc.insets = new Insets(10, 0, 10, 10);
        searchPanel.add(searchTextField, searchTextField_gbc);

        // add icon for the search button
        ImageIcon searchIcon = new ImageIcon("src/image/search_icon.png");
        Image searchImage = searchIcon.getImage();
        Image newSize = searchImage.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH); // resize the image
        searchIcon = new ImageIcon(newSize);

        searchButton = new JButton("Search", searchIcon);
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.addActionListener(new SearchMedicine());
        GridBagConstraints searchButton_gbc = new GridBagConstraints();
        searchButton_gbc.gridx = 2;
        searchButton_gbc.gridy = 0;
        searchButton_gbc.fill = GridBagConstraints.HORIZONTAL;
        searchButton_gbc.insets = new Insets(10, 0, 10, 20);
        searchPanel.add(searchButton, searchButton_gbc);

        add(searchPanel, BorderLayout.NORTH);

        // the file for keeping the list of the medicines
        medicineDataFile = new File("src/File_Data/Medicine_List.bin");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                new SupervisorAdministrationPage(null);     
            }
        });
    }

    /*
     * The class to initialize or reset the medicine list
     * from Medicine_List.bin file to the GUI
     */
    private class initializeMedicineList implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            // ask for user's confirmation
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            // if the user confirms to initialize all of the data
            if(confirm == 0){
                // reset the list
                medicineLinkedList.removeAll(medicineLinkedList);
                
                initializeWorker = new InitializeMedicineData();

                // start the swingworker
                initializeWorker.execute();
            }
        }
    }

    /*
     * The class to initialize reset the data of the medicine,
     * by replacing the old Medicine_List.bin file with a new one.
     * This method will collect the initialized medicine data
     * in MedicineWithPrescription class and MedicineWithoutPresccription class
     */
    private class InitializeMedicineData extends SwingWorker<Void, Integer>{
        /* Initialize the data in the background */
        @Override
        protected Void doInBackground() throws Exception {
            int progressCounter = 0;

            medicineNameLinkedList.clear();

            for(String medicineString : MedicineWithPrescription.withPrescriptionRawData){
                String []split = medicineString.split(" : ");
                
                MedicineWithPrescription medicineWithPrescription = new MedicineWithPrescription();

                medicineWithPrescription.setMedicineName(split[0]);
                medicineWithPrescription.setStock(Double.parseDouble(split[1]));
                medicineWithPrescription.setPrice(Double.parseDouble(split[2]));
                medicineWithPrescription.setExpiredDate(split[3]);
                medicineWithPrescription.setType(split[4]);
                medicineWithPrescription.setDescription(split[5]);
                medicineWithPrescription.setHowToConsume(split[6]);
                medicineWithPrescription.setAmountBought(0);
                medicineWithPrescription.setSubtotal(0);

                if(medicineWithPrescription.getType().equals("Tablet")){
                    medicineWithPrescription.setMaxPurchaseAmount(30);
                }
                else if(medicineWithPrescription.getType().equals("Syrup") || medicineWithPrescription.getType().equals("Eye Drop")){
                    medicineWithPrescription.setMaxPurchaseAmount(2);
                }
                else{
                    medicineWithPrescription.setMaxPurchaseAmount(3);
                }

                medicineLinkedList.add(medicineWithPrescription);
                medicineNameLinkedList.add(medicineWithPrescription.getMedicineName());

                progressCounter++;

                publish(medicineLinkedList.size());
                publish(progressCounter);
            }

            for(String medicineString : MedicineWithoutPrescription.withoutPrescriptionRawData){
                String []split = medicineString.split(" : ");

                MedicineWithoutPrescription medicineWithoutPrescription = new MedicineWithoutPrescription();

                medicineWithoutPrescription.setMedicineName(split[0]);
                medicineWithoutPrescription.setStock(Double.parseDouble(split[1]));
                medicineWithoutPrescription.setPrice(Double.parseDouble(split[2]));
                medicineWithoutPrescription.setExpiredDate(split[3]);
                medicineWithoutPrescription.setType(split[4]);
                medicineWithoutPrescription.setDescription(split[5]);
                medicineWithoutPrescription.setHowToConsume(split[6]);
                medicineWithoutPrescription.setMaxPurchaseAmount(5);
                medicineWithoutPrescription.setAmountBought(0);
                medicineWithoutPrescription.setSubtotal(0);

                medicineLinkedList.add(medicineWithoutPrescription);
                medicineNameLinkedList.add(medicineWithoutPrescription.getMedicineName());

                progressCounter++;
                publish(medicineLinkedList.size());
                publish(progressCounter);
            }
    
            try {
                fo = new FileOutputStream(medicineDataFile, false);
                out = new ObjectOutputStream(fo);

                // wrtie the resetted medicine list to file
                out.writeObject(medicineLinkedList);

                out.close();
                fo.close();
            }
            catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            return null;
        }

        /* Update the GUI */
        @Override
        protected void process(List<Integer> chunks) {
            Integer medicineNumber = chunks.get(chunks.size() - 2);
            Integer progressValue = chunks.get(chunks.size() - 1);

            progressBar.setStringPainted(true);
            progressBar.setValue((progressValue * 100)/medicineNumber);  
        }

        /* After the swingworker is done call showMedicine method */
        @Override
        protected void done() {
            showMedicine(medicineNameLinkedList, "Initializing is Done!");
        }
    }

    /* 
     * The class to show all of the medicines in the GUI,
     * when the user click View All Medicine in the Menu bar
     */
    private class ViewAllMedicine extends SwingWorker<Void, Void>{
        /* Get the medicines data from the file in the background */
        @Override
        protected Void doInBackground() throws Exception {
            int i;
            
            medicineNameLinkedList.clear();

            try {
                fi = new FileInputStream(medicineDataFile);
                in = new ObjectInputStream(fi);
                
                medicineLinkedList = (LinkedList<Medicine>) in.readObject();

                in.close();
                fi.close();
            }
            catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "File Error! Contact Administrator!", "", JOptionPane.ERROR_MESSAGE);
            }

            for(i = 0; i < medicineLinkedList.size(); i++){
                medicineNameLinkedList.add(medicineLinkedList.get(i).getMedicineName());
            }

            return null;
        }

        /* After the swingworker is done call showMedicine method */
        @Override
        protected void done() {
            showMedicine(medicineNameLinkedList, "All Medicines are Displayed!");
        }
    }

    /* 
     * The method to display the selected medicine's detialed information in a new dialog
     */
    private void selectMedicine(){
        String selectedMedicineName;
        int index;
        boolean needPrescription = false;
        
        // get the selected medicine from the list
        selectedMedicineName = medicineList.getSelectedValue();

        // iterate through the list, to get the medicine data
        for(index = 0; index < medicineLinkedList.size(); index++){
            if(selectedMedicineName.equals(medicineLinkedList.get(index).getMedicineName()) == true){
                try{
                    // try to cast it to MedicineWithPrescription, if it succeds, then the medicine requires a prescription
                    MedicineWithPrescription temp = (MedicineWithPrescription) medicineLinkedList.get(index);
                    needPrescription = true;
                }
                catch(ClassCastException e1){
                    // if it fails to be casted to MedicineWithPrescription
                    needPrescription = false;
                }
                break;
            }
        }

        // create and call the editMedicineDialog
        EditMedicineDialog editMedicineDialog = new EditMedicineDialog(thisFrame, needPrescription, medicineLinkedList, index);

        editMedicineDialog.setVisible(true);
    }

    /*
     * showMedicine method is to show the medicine list in the GUI
     */
    private void showMedicine(LinkedList<String> data, String text){
        // resets all of the list model, list, and scroll pane
        // and re-adds the newly ones
        medicineListModel.clear();
        Collections.sort(data);
        medicineListModel.addAll(data);

        medicineList.removeAll();
        medicineList.setModel(medicineListModel);
        
        remove(medicineListScrollPane);
        medicineListScrollPane.setViewportView(null);
        medicineListScrollPane.setViewportView(medicineList);
        add(medicineListScrollPane, BorderLayout.CENTER);

        initializingLabel.setText(text);

        // change the progress bar into a label
        remove(progressBar);
        add(initializingLabel, BorderLayout.SOUTH);

        // refresh the GUI
        revalidate();
        repaint();
    }

    /*
     * The class to search the medicine from the user entered input
     */
    private class SearchMedicine implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            // if the textfield is not empty
            if(searchTextField.getText().isEmpty() == false){
                int i;
                
                searchResultLinkedList = new LinkedList<String>();
                
                // iterate through the medicine list, if found add the medicine to the searchResultLinkedList
                for(i = 0; i < medicineLinkedList.size(); i++){
                    if(medicineLinkedList.get(i).getMedicineName().toLowerCase().contains(searchTextField.getText())){
                        searchResultLinkedList.add(medicineLinkedList.get(i).getMedicineName());
                    }
                }

                // show in the GUI
                showMedicine(searchResultLinkedList, "Search Finished!");
            }
            // if the text field is empty, just display all of the medicines
            else{
                showMedicine(medicineNameLinkedList, "");
            }
        }
    }

}
