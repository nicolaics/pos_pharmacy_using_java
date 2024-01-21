import java.io.Serializable;

/* Customer Class */
public class Customer implements Serializable{
    private static final long serialVersionUID = 28475620292L;

    private String customerName;
    private String phoneNumber;
    private String address;

    public void setCustomerName(String name){
        customerName = name;
    }

    public void setPhoneNumber(String num){
        phoneNumber = num;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getCustomerName(){
        return customerName;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getAddress(){
        return this.address;
    }
}
