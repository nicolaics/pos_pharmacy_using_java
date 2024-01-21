import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Collections;

/*
 * The page to show all of the medicines to the user
 */
public class MedicineListPage extends JFrame{
    private JFrame thisFrame = this;
    
    private JPanel mainPanel;
    private JPanel medicineListPanel;
    private JPanel bottomButtonsPanel;
    private JPanel headerPanel;
    private JPanel searchPanel;
    private JPanel customerInfoPanel;

    private JScrollPane medicineListScrollPane;

    private JMenuBar menuBar;

    private JMenu sortMenu;
    private JMenuItem sortAlphabetMenuItem;
    private JMenuItem sortPrescriptionMenuItem;

    private JMenu filterMenu;
    private JMenuItem filterByPrescriptionNeededMenuItem;
    private JMenuItem filterByPrescriptionNotNeededMenuItem;
    
    private JMenu filterBySymptoms;
    private JMenuItem coughMenuItem;
    private JMenuItem fluMenuItem;
    private JMenuItem feverMenuItem;
    private JMenuItem headacheMenuItem;
    private JMenuItem stomachacheMenuItem;

    private JLabel searchLabel;
    private JLabel customerNameLabel;
    private JLabel customerPhoneNumberLabel;
    private JLabel medicineListLabel;

    private JButton viewShoppingCartButton;
    private JButton homeButton;
    private JButton searchButton;

    private JTextField searchTextField;

    private LinkedList<Medicine> medicineClassDataList;

    private LinkedList<String> medicineNameList;
    private LinkedList<String> medicineNameSearchResultList;
    private LinkedList<String> withPrescriptionNameList;
    private LinkedList<String> withoutPrescriptionNameList;
    private LinkedList<String> filterMedicineNameList;

    private DefaultListModel<String> medicineListModel;

    private JList<String> medicineList;
    
    private File medicineDataFile;
    private FileInputStream fi;
    private ObjectInputStream in;

    private Customer customerData;

    public MedicineListPage(Customer temp){
        setSize(1280, 720);
        
        withoutPrescriptionNameList = new LinkedList<String>();
        withPrescriptionNameList = new LinkedList<String>();
        medicineClassDataList = new LinkedList<Medicine>();

        customerData = temp;
        
        // initialize the menu bar
        {
            menuBar = new JMenuBar();
            setJMenuBar(menuBar);

            sortMenu = new JMenu("Sort");
            
            sortAlphabetMenuItem = new JMenuItem("Sort Alphabetically");
            sortAlphabetMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    Collections.sort(medicineNameList);
                    medicineListModel.clear();
                    medicineListModel.addAll(medicineNameList);
                }
            });
            sortMenu.add(sortAlphabetMenuItem);

            sortPrescriptionMenuItem = new JMenuItem("Sort By Prescription Not Needed First");
            sortPrescriptionMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    Collections.sort(withPrescriptionNameList);
                    Collections.sort(withoutPrescriptionNameList);

                    medicineListModel.clear();
                    medicineListModel.addAll(withoutPrescriptionNameList);
                    medicineListModel.addAll(withPrescriptionNameList);
                }
            });
            sortMenu.add(sortPrescriptionMenuItem);
            menuBar.add(sortMenu);
        }
        {
            filterMenu = new JMenu("Filter");
            
            filterByPrescriptionNeededMenuItem = new JMenuItem("Filter By Prescription Needed");
            filterByPrescriptionNeededMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    divideByPrescription();
                    
                    medicineListModel = new DefaultListModel<String>();
                    medicineListModel.addAll(withPrescriptionNameList);

                    medicineList.removeAll();
                    medicineList.setModel(medicineListModel);
                    
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            });
            filterMenu.add(filterByPrescriptionNeededMenuItem);

            filterByPrescriptionNotNeededMenuItem = new JMenuItem("Filter By Prescription Not Needed");
            filterByPrescriptionNotNeededMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    divideByPrescription();
                    
                    medicineListModel = new DefaultListModel<String>();
                    medicineListModel.addAll(withoutPrescriptionNameList);

                    medicineList.removeAll();
                    medicineList.setModel(medicineListModel);
                    
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            });
            filterMenu.add(filterByPrescriptionNotNeededMenuItem);
            
            filterMenu.addSeparator();
            
            filterBySymptoms = new JMenu("Filter By Symptoms");
            
            coughMenuItem = new JMenuItem("Cough");
            coughMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    int i;

                    filterMedicineNameList = new LinkedList<String>();

                    for(i = 0; i < medicineClassDataList.size(); i++){
                        String filterMedicineNameString = medicineClassDataList.get(i).getDescription().toUpperCase();

                        if(filterMedicineNameString.contains("COUGH") == true){
                            filterMedicineNameList.add(medicineClassDataList.get(i).getMedicineName());
                        }
                    }

                    medicineListModel = new DefaultListModel<String>();
                    medicineListModel.addAll(filterMedicineNameList);

                    medicineList.removeAll();
                    medicineList.setModel(medicineListModel);
                    
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            });
            filterBySymptoms.add(coughMenuItem);

            fluMenuItem = new JMenuItem("Flu");
            fluMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    int i;

                    filterMedicineNameList = new LinkedList<String>();

                    for(i = 0; i < medicineClassDataList.size(); i++){
                        String filterMedicineNameString = medicineClassDataList.get(i).getDescription().toUpperCase();

                        if(filterMedicineNameString.contains("FLU") == true || filterMedicineNameString.contains("COLD") == true){
                            filterMedicineNameList.add(medicineClassDataList.get(i).getMedicineName());
                        }
                    }

                    medicineListModel = new DefaultListModel<String>();
                    medicineListModel.addAll(filterMedicineNameList);

                    medicineList.removeAll();
                    medicineList.setModel(medicineListModel);
                    
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            });
            filterBySymptoms.add(fluMenuItem);

            feverMenuItem = new JMenuItem("Fever");
            feverMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    int i;

                    filterMedicineNameList = new LinkedList<String>();

                    for(i = 0; i < medicineClassDataList.size(); i++){
                        String filterMedicineNameString = medicineClassDataList.get(i).getDescription().toUpperCase();

                        if(filterMedicineNameString.contains("FEVER") == true){
                            filterMedicineNameList.add(medicineClassDataList.get(i).getMedicineName());
                        }
                    }

                    medicineListModel = new DefaultListModel<String>();
                    medicineListModel.addAll(filterMedicineNameList);

                    medicineList.removeAll();
                    medicineList.setModel(medicineListModel);
                    
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            });
            filterBySymptoms.add(feverMenuItem);

            headacheMenuItem = new JMenuItem("Headache");
            headacheMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    int i;

                    filterMedicineNameList = new LinkedList<String>();

                    for(i = 0; i < medicineClassDataList.size(); i++){
                        String filterMedicineNameString = medicineClassDataList.get(i).getDescription().toUpperCase();

                        if(filterMedicineNameString.contains("HEADACHE") == true){
                            filterMedicineNameList.add(medicineClassDataList.get(i).getMedicineName());
                        }
                    }

                    medicineListModel = new DefaultListModel<String>();
                    medicineListModel.addAll(filterMedicineNameList);

                    medicineList.removeAll();
                    medicineList.setModel(medicineListModel);
                    
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            });
            filterBySymptoms.add(headacheMenuItem);

            stomachacheMenuItem = new JMenuItem("Stomach-ache");
            stomachacheMenuItem.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    int i;

                    filterMedicineNameList = new LinkedList<String>();

                    for(i = 0; i < medicineClassDataList.size(); i++){
                        String filterMedicineNameString = medicineClassDataList.get(i).getDescription().toUpperCase();

                        if(filterMedicineNameString.contains("STOMACH") == true){
                            filterMedicineNameList.add(medicineClassDataList.get(i).getMedicineName());
                        }
                    }

                    medicineListModel = new DefaultListModel<String>();
                    medicineListModel.addAll(filterMedicineNameList);

                    medicineList.removeAll();
                    medicineList.setModel(medicineListModel);
                    
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            });
            filterBySymptoms.add(stomachacheMenuItem);
            
            filterMenu.add(filterBySymptoms);
            menuBar.add(filterMenu);

        }

        mainPanel = new JPanel(new BorderLayout());

        // initialize the panel for customer info & search panel
        {
            headerPanel = new JPanel(new GridLayout(1, 2));

            searchPanel = new JPanel(new FlowLayout());

            searchLabel = new JLabel("Search: ");
            searchPanel.add(searchLabel);

            searchTextField = new JTextField();
            searchTextField.setColumns(40);
            searchTextField.setHorizontalAlignment(SwingConstants.LEFT);
            searchPanel.add(searchTextField);
            
            ImageIcon searchIcon = new ImageIcon("src/image/search_icon.png");
            Image searchImage = searchIcon.getImage();
            Image newSize = searchImage.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH); // resize the image
            searchIcon = new ImageIcon(newSize);

            searchButton = new JButton("Search", searchIcon);
            searchButton.setHorizontalAlignment(SwingConstants.LEFT);
            searchButton.addActionListener(new SearchMedicine());
            searchPanel.add(searchButton);

            customerInfoPanel = new JPanel(new GridLayout(2, 1));

            customerNameLabel = new JLabel(customerData.getCustomerName());
            customerNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            customerPhoneNumberLabel = new JLabel("" + customerData.getPhoneNumber());
            customerPhoneNumberLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            customerInfoPanel.add(customerNameLabel);
            customerInfoPanel.add(customerPhoneNumberLabel);
            
            headerPanel.add(searchPanel);
            headerPanel.add(customerInfoPanel);

            mainPanel.add(headerPanel, BorderLayout.NORTH);
        }
        
        // Initialize the medicine list
        {
            getMedicineData();
            divideByPrescription();

            medicineList = new JList<String>(medicineListModel);
            medicineList.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if(e.getClickCount() == 2){
                       selectMedicine();
                    }
                }
            });
            medicineList.setFont(new Font("Arial Narrow", Font.PLAIN, 18));
            medicineListScrollPane = new JScrollPane(medicineList);

            medicineListPanel = new JPanel(new BorderLayout());
            medicineListPanel.add(medicineListScrollPane, BorderLayout.CENTER);

            medicineListLabel = new JLabel("Medicine List");
            medicineListLabel.setFont(new Font("Arial Narrow", Font.BOLD, 20));
            medicineListLabel.setHorizontalAlignment(SwingConstants.CENTER);
            medicineListLabel.setVerticalAlignment(SwingConstants.CENTER);
            medicineListPanel.add(medicineListLabel, BorderLayout.NORTH);

            mainPanel.add(medicineListPanel, BorderLayout.CENTER);
        }

        bottomButtonsPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(bottomButtonsPanel, BorderLayout.SOUTH);

        homeButton = new JButton("Back to Home");
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WelcomePage welcomePage = new WelcomePage();
                welcomePage.setVisible(true);
                dispose();
            }
        });
        bottomButtonsPanel.add(homeButton);

        viewShoppingCartButton = new JButton("View Shopping Cart");
        viewShoppingCartButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ShoppingCartPage<Medicine> shoppingCartPage = new ShoppingCartPage<Medicine>(customerData);

                shoppingCartPage.setVisible(true);
                dispose();
            }
        });
        bottomButtonsPanel.add(viewShoppingCartButton);

        add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        MedicineListPage frame = new MedicineListPage(new Customer());
        frame.setVisible(true);
    }

    /*
     * The method to get all of the medicines from the file
     */
    private void getMedicineData(){
        medicineDataFile = new File("src/File_Data/Medicine_List.bin");

        try {
            fi = new FileInputStream(medicineDataFile);
            in = new ObjectInputStream(fi);
            
            medicineClassDataList = (LinkedList<Medicine>) in.readObject();

            in.close();
            fi.close();
        }
        catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int i;

        medicineNameList = new LinkedList<String>();

        for(i = 0; i < medicineClassDataList.size(); i++){
            medicineNameList.add(medicineClassDataList.get(i).getMedicineName());
        }

        medicineListModel = new DefaultListModel<String>();
        medicineListModel.addAll(medicineNameList);
    }

    /*
     * The method to display the selected medicine information page
     * */
    private void selectMedicine(){
        String selectedMedicineName;
        int i;
        boolean needPrescription = true;
        MedicineInformationDialog medicineInformationDialog;

        MedicineWithoutPrescription withoutPrescription = new MedicineWithoutPrescription();
        MedicineWithPrescription withPrescription = new MedicineWithPrescription();
        
        selectedMedicineName = medicineList.getSelectedValue();

        for(i = 0; i < withoutPrescriptionNameList.size(); i++){
            if(selectedMedicineName.equals(withoutPrescriptionNameList.get(i)) == true){
                needPrescription = false;
                break;
            }
        }

        if(needPrescription == false){
            for(i = 0; i < medicineClassDataList.size(); i++){
                if(selectedMedicineName.equals(medicineClassDataList.get(i).getMedicineName())){
                    withoutPrescription = (MedicineWithoutPrescription) medicineClassDataList.get(i);
                    break;
                }
            }

            medicineInformationDialog = new MedicineInformationDialog<MedicineWithoutPrescription>(thisFrame, needPrescription, withoutPrescription);
            medicineInformationDialog.setVisible(true);
        }
        else{
            for(i = 0; i < medicineClassDataList.size(); i++){
                if(selectedMedicineName.equals(medicineClassDataList.get(i).getMedicineName())){
                    withPrescription = (MedicineWithPrescription) medicineClassDataList.get(i);
                    break;
                }
            }
            medicineInformationDialog = new MedicineInformationDialog<MedicineWithPrescription>(thisFrame, needPrescription, withPrescription);
            medicineInformationDialog.setVisible(true);
        }
    }

    /*
     * The method to classify the medicines into
     * medicine that needs prescription and not
     */
    private void divideByPrescription(){
        int i;

        withoutPrescriptionNameList.clear();
        withPrescriptionNameList.clear();

        for (i = 0; i < medicineClassDataList.size(); i++){
            try{
                MedicineWithoutPrescription withoutPrescription = new MedicineWithoutPrescription();

                withoutPrescription = (MedicineWithoutPrescription) medicineClassDataList.get(i);

                withoutPrescriptionNameList.add(withoutPrescription.getMedicineName());
            }
            catch (ClassCastException e1) {
                MedicineWithPrescription withPrescription = new MedicineWithPrescription();

                withPrescription = (MedicineWithPrescription) medicineClassDataList.get(i);
                withPrescriptionNameList.add(withPrescription.getMedicineName());
            }
        }
    }

    /*
     * The class if the user search for a medicine using the provided box
     */
    private class SearchMedicine implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(searchTextField.getText().isEmpty() == false) {
                String searchString = searchTextField.getText().toUpperCase();
                String temp;

                int i;

                medicineNameSearchResultList = new LinkedList<String>();

                for (i = 0; i < medicineNameList.size(); i++) {
                    temp = medicineNameList.get(i).toUpperCase();

                    if (temp.contains(searchString)) {
                        medicineNameSearchResultList.add(medicineNameList.get(i));
                    }
                }

                medicineListModel = new DefaultListModel<String>();
                medicineListModel.addAll(medicineNameSearchResultList);

                medicineList.removeAll();
                medicineList.setModel(medicineListModel);
                
                mainPanel.revalidate();
                mainPanel.repaint();
            }
            else{
                medicineListModel = new DefaultListModel<String>();
                medicineListModel.addAll(medicineNameList);

                medicineList.removeAll();
                medicineList.setModel(medicineListModel);

                mainPanel.revalidate();
                mainPanel.repaint();
            }
        }
    }

    
}
