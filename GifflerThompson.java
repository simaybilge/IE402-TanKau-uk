import java.sql.SQLOutput;
import java.util.*;

public class GifflerThompson {
    //public static final int[] daysOfWeek = {0, 1, 2, 3, 4}; // Example: Operating on weekdays
    //public static final int NUMBER_OF_WORKERS = 5;
    //public static final double OPERATING_HOURS_PER_DAY = 8 * 60; // 8 hours in minutes

    /*Step 1: Bütün truckları okuycak listeleri doldurucak (DONE)
    Step 2: Bütün trucklar için critical ratio hesaplanacak ve bir listede storelanacak (DONE)
    Step 3: Critical ratio listesi sıralanacak ((due time-arrival time)/process time) (DONE)
    Step 4: Priority'si büyük olanı önce alacak şekilde sıralanacak (DONE)
    Step 4: Worker, due time, foreign/domestic constraintlerine göre sıralar hesaplacak

        1) foreign ise sadece perşembe/cuma işlenebilir
        2) foreign kamyonların priority'si daha yüksek olacak
        3) aynı anda docked ve not docked çalışabilir, iki tane aynı tip çalışamaz
        4) aynı anda çalışan kamyonların toplam worker sayısı en fazla 5 olabilir
        5) critical ratio negatif olursa sıranın en sonuna at (sadece bir taneden penalty yemek için) (DONE)

    */
    public static void main(String[] args) {

        ArrayList<Truck> trucks = Truck.generateTrucksFromCSV("tan_kaucuk_data.csv");
        ArrayList<Truck> orderedList = new ArrayList<Truck>();
        ArrayList<Truck> dockedOrderedList = new ArrayList<>();
        ArrayList<Truck> notDockedOrderedList = new ArrayList<>();
        HashMap<Truck, Double> criticalRatioList = new HashMap<>();
        for (int i = 0; i < trucks.size(); i++) {
            criticalRatioList.put(trucks.get(i), trucks.get(i).getCriticalRatio());
        }

        System.out.println("Version ordered according to priority: ");
        System.out.println(" ");
        for (int i = 0; i < trucks.size(); i++) {
            orderedList.add(findNextBestRatioAndRemove(criticalRatioList));

        }

        for (int i = 0; i < 19; i++) { //0'dan 18'e
            if(orderedList.get(i).getCriticalRatio() == orderedList.get(i+1).getCriticalRatio()){
                if(orderedList.get(i).getPriority() < orderedList.get(i+1).getPriority()){
                    Truck temp = null;
                    temp = orderedList.get(i);
                    orderedList.set(i,orderedList.get(i+1));
                    orderedList.set(i+1,temp);
                }
            }
        }

        for(Truck t: orderedList){
            if(t.getIsDocked()){
                dockedOrderedList.add(t);
            } else {
                notDockedOrderedList.add(t);
            }
        }

        System.out.println("List of docked trucks ordered according to critical ratio and priority: ");
        System.out.println(" ");

        for(int i = 0; i < dockedOrderedList.size(); i++) {
            System.out.println("Order: " + (i+1) + " Truck ID: " + dockedOrderedList.get(i).getId() + " Critical ratio: " + String.format("%.2f",dockedOrderedList.get(i).getCriticalRatio()) + " Priority: " + dockedOrderedList.get(i).getPriority() + " Is it a foreign truck?:  " + dockedOrderedList.get(i).getIsForeign());
        }

        System.out.println(" ");
        System.out.println("List of not docked trucks ordered according to critical ratio and priority: ");
        System.out.println(" ");

        for(int i = 0; i < notDockedOrderedList.size(); i++) {
            System.out.println("Order: " + (i+1) + " Truck ID: " + notDockedOrderedList.get(i).getId() + " Critical ratio: " + String.format("%.2f",notDockedOrderedList.get(i).getCriticalRatio()) + " Priority: " + notDockedOrderedList.get(i).getPriority() + " Is it a foreign truck?:  " + notDockedOrderedList.get(i).getIsForeign());
        }

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
