import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Collections;
import java.util.Vector;

public class ViewCustomerListPage extends JFrame{
    private JFrame mainFrame = this;

    private JPanel searchPanel;

    private JLabel searchLabel;
    private JTextField searchTextField;

    private JButton searchButton;

    private JScrollPane customerNameScrollPane;

    private Vector<Customer> customerClassVector;
    private Vector<String> customerNameVector;
    private Vector<String> searchResultVector;

    private DefaultListModel<String> customerNameModel;
    private JList<String> customerNameList;

    private File file;
    private FileInputStream fi;
    private ObjectInputStream in;

    private String selectedCustomer;
    private String searchKeyword;

    public ViewCustomerListPage(){
        setSize(1280, 720);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("View All Customers");
        setAlwaysOnTop(true);
        setLayout(new BorderLayout());

        /* SEARCH PANEL */
        {
            GridBagLayout searchLayout = new GridBagLayout();
            searchLayout.columnWidths = new int[] {0, 0, 0};
            searchLayout.rowHeights = new int[] {0, 0};
            searchLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
            searchLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};

            searchPanel = new JPanel();
            searchPanel.setLayout(searchLayout);

            searchLabel = new JLabel("Search Customer Name:");
            GridBagConstraints searchLabel_gbc = new GridBagConstraints();
            searchLabel_gbc.gridx = 0;
            searchLabel_gbc.gridy = 0;
            searchLabel_gbc.insets = new Insets(10, 20, 10, 10);
            searchPanel.add(searchLabel, searchLabel_gbc);

            searchTextField = new JTextField();
            GridBagConstraints searchTextField_gbc = new GridBagConstraints();
            searchTextField_gbc.gridx = 1;
            searchTextField_gbc.gridy = 0;
            searchTextField_gbc.fill = GridBagConstraints.HORIZONTAL;
            searchTextField_gbc.insets = new Insets(10, 0, 10, 10);
            searchPanel.add(searchTextField, searchTextField_gbc);

            ImageIcon searchIcon = new ImageIcon("src/image/search_icon.png");
            Image searchImage = searchIcon.getImage();
            Image newSize = searchImage.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH); // resize the image
            searchIcon = new ImageIcon(newSize);

            searchButton = new JButton("Search", searchIcon);
            searchButton.addActionListener(new Search());
            GridBagConstraints searchButton_gbc = new GridBagConstraints();
            searchButton_gbc.gridx = 2;
            searchButton_gbc.gridy = 0;
            searchButton_gbc.fill = GridBagConstraints.HORIZONTAL;
            searchButton_gbc.insets = new Insets(10, 0, 10, 20);
            searchPanel.add(searchButton, searchButton_gbc);

            add(searchPanel, BorderLayout.NORTH);
        }
        
        /* MAIN PANEL */
        {
            customerClassVector = new Vector<Customer>();
            customerNameVector = new Vector<String>();

            customerNameList = new JList<String>();
            customerNameList.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if(e.getClickCount() == 2){ // if the list is double-clicked
                       getCustomerDetails();
                    }
                }
            });

            getCustomerName();
            
            customerNameScrollPane = new JScrollPane();
            customerNameScrollPane.setViewportView(customerNameList);

            add(customerNameScrollPane, BorderLayout.CENTER);
        }

        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        ViewCustomerListPage viewCustomerListPage = new ViewCustomerListPage();
        viewCustomerListPage.setVisible(true);
    }

    private void getCustomerName(){
        file = new File("src/File_Data/Customer_Data.bin");

        try {
            fi = new FileInputStream(file);
            in = new ObjectInputStream(fi);

            while(true){
                Customer temp = (Customer) in.readObject();
                customerClassVector.add(temp);
                customerNameVector.add(temp.getCustomerName());
            }
        }
        catch(EOFException e){
            try {
                fi.close();
                in.close();

                customerNameModel = new DefaultListModel<String>();
                Collections.sort(customerNameVector);
                customerNameModel.addAll(customerNameVector);

                customerNameList.setModel(customerNameModel);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private class Search implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(searchTextField.getText().isEmpty() == false){
                searchKeyword = searchTextField.getText().toUpperCase();

                searchResultVector = new Vector<String>();

                int i;

                for(i = 0; i < customerNameVector.size(); i++){
                    String customerNameUppercase = customerNameVector.get(i).toUpperCase();

                    if(customerNameUppercase.contains(searchKeyword)){
                        searchResultVector.add(customerNameVector.get(i));
                    }
                }

                Collections.sort(searchResultVector);

                customerNameModel.clear();
                customerNameModel.addAll(searchResultVector);

                customerNameList.removeAll();
                customerNameList.setModel(customerNameModel);

                customerNameScrollPane.removeAll();
                customerNameScrollPane.setViewportView(customerNameList);

                revalidate();
                repaint();
            }
            else{
                customerNameModel.clear();
                customerNameModel.addAll(customerNameVector);

                customerNameList.removeAll();
                customerNameList.setModel(customerNameModel);

                customerNameScrollPane.removeAll();
                customerNameScrollPane.setViewportView(customerNameList);

                revalidate();
                repaint();
            }
        }
    }

    private void getCustomerDetails(){
        int index;

        selectedCustomer = customerNameList.getSelectedValue();

        for(index = 0; index < customerClassVector.size(); index++){
            if(customerClassVector.get(index).getCustomerName().equals(selectedCustomer)){
                break;
            }
        }

        try{
            if(index == customerClassVector.size()){
                throw new MyException("Customer not Found");
            }
            else if(index < customerClassVector.size()){
                CustomerDetailsDialog customerDetailsDialog = new CustomerDetailsDialog(mainFrame, customerClassVector.get(index));
                customerDetailsDialog.setVisible(true);
            }
        }
        catch(MyException e1){
            JOptionPane.showMessageDialog(null, "Customer not found", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}
