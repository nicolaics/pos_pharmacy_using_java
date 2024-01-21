/* The class to initialize the data of Medicine with prescription */
public class MedicineWithPrescription extends Medicine {
    public final static String[] withPrescriptionRawData = new String[]{
        "Aldisa : 100 : 15000 : 20-12-2025 : Strip : To treat cold : 2 x 1 capsule (per day)",
        "Esilgan : 125 : 35400 : 15-10-2026 : Tablet : A sleeping pill to help you sleep, if you are experiencing diffulty in sleeping : 1 x 1 tablet (per day)",
        "Rhinos : 60 : 13300 : 04-08-2027 : Strip : To treat cold : 2 x 1 capsule (per day)",
        "Elocon : 12 : 24500 : 06-05-2024 : Cream : To treat itchiness in your outer body parts : Apply the ointment 3 times a day",
        "Polidemisin Eye Drop : 3 : 18000 : 03-01-2024 : Eye Drop : To treat sore eyes and infections in the eyes : 3 x 1 drop (per day)",
        "Amoxsan : 21 : 8500 : 19-04-2025 : Strip : It is antibiotics for various kinds of virus or infections : 3 x 1 tablet (per day)",
        "Xanax : 10 : 32100 : 20-05-2025 : Tablet : It is an anti-depressant medicine : 1 x 1 tablet (per day)",
        "Ativan : 34 : 33000 : 25-08-2025 : Tablet : To treat or reduce anxiety disorder : 1 x 1 tablet (per day)",
        "Mucopect Syrup : 2 : 14600 : 17-05-2025 : Syrup : To thin the mucus in your nose or throat, so it can be expelled from your body : 3 x 5ml (per day)",
        "Pantozol 20mg : 53 : 4500 : 26-12-2026 : Strip : To treat stomach-related diseases or stomach-related aches : 1 x 1 tablet (per day)",
        "Cravit : 62 : 6300 : 14-11-2024 : Strip : It is antibiotics for various kinds of virus or infections : 1 x 1 tablet (per day)",
        "Codipront : 400 : 3100 : 06-12-2026 : Strip : To treat cough : 2 x 5ml (per day)",
        "Braxidin : 60 : 3400 : 09-10-2025 : Strip : To treat problems related to gastrointestinal : 3 x 1 tablet (per day)",
        "Analsik : 45 : 7600 : 10-10-2025 : Strip : To treat ache or pain in the outer parts of your body : 3 x 1 tablet (per day)",
    };

    @Override
    public double calculateSubtotal(double price, int amount) {
        double subtotal;
        
        subtotal = (price * amount);

        super.setSubtotal(subtotal);

        return super.getSubtotal();
    }
}
