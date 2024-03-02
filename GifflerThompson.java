import java.util.*;
public class GifflerThompson {
    //public static final int[] daysOfWeek = {0, 1, 2, 3, 4}; // Example: Operating on weekdays
    //public static final int NUMBER_OF_WORKERS = 5;
    //public static final double OPERATING_HOURS_PER_DAY = 8 * 60; // 8 hours in minutes

    /*Step 1: Bütün truckları okuycak listeleri doldurucak (done)
    Step 2: Bütün trucklar için critical ratio hesaplanacak ve bir listede storelanacak (done)
    Step 3: Critical ratio listesi sıralanacak ((due time-arrival time)/process time) (done)
    Step 4: Worker, due time, foreign/domestic constraintlerine göre sıralar hesaplacak (setup cost??)

        1) foreign ise sadece perşembe/cuma işlenebilir
        2) aynı anda docked ve not docked çalışabilir, iki tane aynı tip çalışamaz
        3) aynı anda çalışan kamyonların toplam worker sayısı en fazla 5 olabilir
        4) critical ratio negatif olursa sıranın en sonuna at (sadece bir taneden penalty yemek için)
    */
    public static void main(String[] args) {

        ArrayList<Truck> trucks = Truck.generateTrucksFromCSV("tan_kaucuk_data.csv");
        ArrayList<Truck> orderedList = new ArrayList<Truck>();
        HashMap<Truck, Double> criticalRatioList = new HashMap<>();

        for (int i = 0; i < trucks.size(); i++) {
            criticalRatioList.put(trucks.get(i), trucks.get(i).getCriticalRatio());
        }

        for (int i = 0; i < trucks.size(); i++) {
            orderedList.add(findNextBestRatioAndRemove(criticalRatioList));
            System.out.println("order: " + (i + 1) + " truck id: " + orderedList.get(i).getId() + " critical ratio: " + String.format("%.2f",orderedList.get(i).getCriticalRatio()));
        }
        /*System.out.println("********************");
        for (int i = 0; i < 19; i++) { //0'dan 18'e
            if(orderedList.get(i).getCriticalRatio() == orderedList.get(i+1).getCriticalRatio()){
                if(orderedList.get(i).getPriority() < orderedList.get(i+1).getPriority()){
                    Truck temp = new Truck(999);
                    temp = orderedList.get(i);
                    orderedList.remove(i);
                    orderedList.add(i,orderedList.get(i+1));
                    orderedList.remove(i+1);
                    orderedList.add(i+1,temp);
                }
            }
            System.out.println("order: " + (i + 1) + " truck id: " + orderedList.get(i).getId() + " critical ratio: " + String.format("%.2f",orderedList.get(i).getCriticalRatio()));
        }*/
    }
    private static Truck findNextBestRatioAndRemove(HashMap<Truck,Double> criticalRatioList) {
        Truck best = null;
        double bestRatio = -Integer.MAX_VALUE;
        for (Truck s: criticalRatioList.keySet()){
            if(criticalRatioList.get(s)>bestRatio){
                bestRatio=criticalRatioList.get(s);
                best=s;
            }
        }
        criticalRatioList.remove(best);
        return best;
    }

}
