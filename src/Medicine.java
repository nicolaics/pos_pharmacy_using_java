import java.io.Serializable;

/* Medicine Class */
public abstract class Medicine implements Serializable, CalculateInterface{
    private static final long serialVersionUID = 28475620290L;

    private String medicineName;
    private double stock;
    private double price;
    private String expiredDate;
    private String type;
    private String description;
    private String howToConsume;
    private int maxPurchaseAmount;
    private int amountBought;
    private double subtotal = 0;

    public void setMedicineName(String medicineName){
        this.medicineName = medicineName;
    }

    public void setStock(double stock){
        this.stock = stock;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setExpiredDate(String expiredString){
        this.expiredDate = expiredString;
    }

    public void setType(String type){
        this.type = type;
    }
    
    public void setDescription(String description){
        this.description = description;
    }

    public void setHowToConsume(String howToConsume){
        this.howToConsume = howToConsume;
    }

    public void setMaxPurchaseAmount(int purchase){
        maxPurchaseAmount = purchase;
    }

    public void setAmountBought(int amountBought){
        this.amountBought = amountBought;
    }
    
    public void setSubtotal(double subtotal){
        this.subtotal = subtotal;
    }

    public String getMedicineName(){
        return this.medicineName;
    }

    public double getStock(){
        return this.stock;
    }

    public double getPrice(){
        return this.price;
    }

    public String getExpiredDate(){
        return this.expiredDate;
    }

    public String getType(){
        return this.type;
    }

    public String getDescription(){
        return this.description;
    }

    public String getHowToConsume(){
        return this.howToConsume;
    }

    public int getMaxPurchaseAmount(){
        return maxPurchaseAmount;
    }

    public int getAmountBought(){
        return this.amountBought;
    }
    
    public double getSubtotal(){
        return this.subtotal;
    }
}
