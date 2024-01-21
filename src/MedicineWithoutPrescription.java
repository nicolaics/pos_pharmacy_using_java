/* The class to initialize the data of Medicine without prescription */
public class MedicineWithoutPrescription extends Medicine {

    public final static String[] withoutPrescriptionRawData = new String[]{
        "Actifed Green : 15 : 3500 : 13-12-2026 : Syrup : To treat cough with mucus and cold : 3 x 5ml (per day)",
        "Actifed Red : 10 : 3500 : 10-12-2026 : Syrup : To treat cough without mucus and cold : 3 x 5ml (per day)",
        "Actifed Yellow : 12 : 3500 : 10-12-2026 : Syrup : To treat flu : 3 x 5 ml (per day)",
        "Betadine : 40 : 2000 : 24-02-2026 : Liquid : If you have any injuries or bleeding, apply this medicine to the wounded part : 3-4x a day",
        "Combantrin 250mg : 140 : 1600 : 26-01-2025 : Strip : To kill worms in your stomach : Consume at night",
        "Decolgen : 500 : 1000 : 10-04-2025 : Strip : To treat flu : 3 x 1 tablet (per day)",
        "Degirol : 345 : 1100 : 06-10-2026 : Strip : To treat sore throat : 3-4 x 1 tablet (per day)",
        "Hansaplast : 621 : 100 : 05-12-2024 : Envelope : Band aid  : Stick the band aid to the wounded part",
        "Mylanta : 210 : 1300 : 08-12-2025 : Syrup : To treat gastritis : 3 x 10ml (per day before eating)",
        "Panadol Blue : 710 : 400 : 16-08-2026 : Strip : To treat fever : 3-4 x 1 tablet (per day)",
        "Panadol Green : 600 : 400 : 16-08-2026 : Strip : To treat fever and flu : 3 x 1 tablet (per day)",
        "Panadol Red : 750 : 400 : 14-09-2026 : Strip : To treat headache : 3 x 1 tablet (per day)",
        "Tolak Angin : 1200 : 200 : 16-11-2025 : Sachet : It is a herbal medicine used to treat if you feel that your condition is not that good. Or if you feel like you are going to catch flu : 3-4 x 1 sachet (per day)",
        "Woods Blue : 24 : 4000 : 05-01-2026 : Syrup : To treat cough with mucus : 3 x 5ml (per day)",
        "Woods Red : 10 : 4000 : 04-02-2026 : Syrup : To treat cough without mucus : 3 x 5ml (per day)",
        "Canesten : 4 : 5100 : 21-01-2024 : Cream : To treat fungus infections or fungal infections or yeast infections : Apply the ointment to the infected part 3 times a day",
        "Counterpain : 6 : 5000 : 20-05-2025 : Cream : If you have any pain or ache in your outer body parts, apply this ointment : Apply the ointment 3 times a day",
        "Visine : 2 : 1300 : 10-12-2023 : Eye Drop : To treat sore eyes : 3 x 1 drop (per day)",
        "Insto : 7 : 1500 : 25-01-2024 : Eye Drop : To treat sore eyes : 3 x 1 drop (per day)"
    };

    @Override
    public double calculateSubtotal(double price, int amount) {
        double subtotal;
        
        subtotal = (price * amount);

        super.setSubtotal(subtotal);

        return super.getSubtotal();
    }
}
