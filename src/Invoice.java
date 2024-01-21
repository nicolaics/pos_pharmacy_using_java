import java.io.Serializable;
import java.util.LinkedList;

/* Invoice Class */
public class Invoice <T extends Medicine> implements Serializable{
    private static final long serialVersionUID = 28475620293L;
    
    private Customer customerInfo;
    private LinkedList<T> medicineBought;
    private double totalPrice;
    private boolean needPrescription;
    private String receiveMedicineMethod;
    private String extraNote;
    private String transactionTime;

    public void setCustomerInfo(Customer customer){
        customerInfo = customer;
    }

    public void setMedicineBought(LinkedList<T> medicine){
        medicineBought = medicine;
    }

    public void setTotalPrice(double totalPrice){
        this.totalPrice = totalPrice;
    }

    public void setNeedPrescription(boolean needPrescription){
        this.needPrescription = needPrescription;
    }

    public void setReceiveMedicineMethod(String receiveMedicineMethod){
        this.receiveMedicineMethod = receiveMedicineMethod;
    }

    public void setExtraNote(String extraNote){
        this.extraNote = extraNote;
    }

    public void setTransactionTime(String transactionTime){
        this.transactionTime = transactionTime;
    }

    public Customer getCustomerInfo(){
        return customerInfo;
    }

    public LinkedList<T> getMedicineBought(){
        return medicineBought;
    }

    public double getTotalPrice(){
        return this.totalPrice;
    }

    public boolean getNeedPrescription(){
        return this.needPrescription;
    }

    public String getReceiveMedicineMethod(){
        return this.receiveMedicineMethod;
    }

    public String getExtraNote(){
        return this.extraNote;
    }

    public String getTransactionTime(){
        return this.transactionTime;
    }
}
