import java.util.*;

public class GifflerThompson {
    static ArrayList<Truck> trucks = Truck.generateTrucksFromCSV("tan_kaucuk_data.csv");
    static HashMap<Truck, Integer> arrivalTimeList = new HashMap<>();
    static ArrayList<Truck> orderedList = new ArrayList<>();
    static ArrayList<Truck> dockedOrderedList = new ArrayList<>();
    static ArrayList<Truck> notDockedOrderedList = new ArrayList<>();
    static Truck currentTruck;
    static boolean dockedAvailable = true;
    static boolean notDockedAvailable = true;
    static int availableWorkers = 5;
    static int endTime = 0;
    static int neededWorkers = 0;
    static int penaltyCost = 0;
    static int setupCost = 0;
    static boolean isDomestic = true;
    static boolean isLate = false;

    public static void main(String[] args){

        for (Truck truck : trucks) {
            arrivalTimeList.put(truck, truck.getArrivalTime());
        }

        for (int i = 0; i < trucks.size(); i++) {
            currentTruck = findNextBestArrivalAndRemove(arrivalTimeList);
            orderedList.add(currentTruck);
        }

        for(int i = 0; i < orderedList.size()-1; i++){
            for(int j = 0; j < orderedList.size()-1; j++) { // kaçtan başlaması daha doğru olur
                if (orderedList.get(i).getArrivalTime() == orderedList.get(j).getArrivalTime()) {
                    if (orderedList.get(i).getDueTime() < orderedList.get(j).getDueTime()) {
                        Truck temp;
                        temp = orderedList.get(i);
                        orderedList.set(i, orderedList.get(j));
                        orderedList.set(j, temp);
                    } else if(orderedList.get(i).getDueTime() == orderedList.get(j).getDueTime()){
                        if (orderedList.get(i).getPriority() > orderedList.get(j).getPriority()) {
                            Truck temp;
                            temp = orderedList.get(i);
                            orderedList.set(i, orderedList.get(j));
                            orderedList.set(j, temp);
                        }
                    }
                }
            }
        }

        for (Truck truck : orderedList) {
            if (truck.getIsDocked()) {
                dockedOrderedList.add(truck);
            } else {
                notDockedOrderedList.add(truck);
            }
        }

        for(int day = 0; day <= 4; day++){

            System.out.println("Day: " + (day+1));
            System.out.println("Docked ordered list according to day: ");
            System.out.println(" ");

            for (Truck value : dockedOrderedList) {
                if (value.getArrivalDay() == day) {
                    System.out.println("Truck ID: " + value.getId());
                    System.out.println("Foreign/Domestic: " + value.getIsForeign());
                    System.out.println("Critical Ratio: " + value.getCriticalRatio());
                    System.out.println("Arrival Day: " + (value.getArrivalDay() + 1));
                    System.out.println("Arrival Time: " + value.getArrivalTime());
                    System.out.println("Due Day: " + value.getDueDay());
                    System.out.println("Due Time: " + value.getDueTime());
                    System.out.println("Priority: " + value.getPriority());
                    System.out.println(" ");
                    System.out.println("--------------------------------------------------");
                }
            }

            System.out.println("Day: " + (day+1));
            System.out.println("Not docked ordered list according to day: ");
            System.out.println(" ");

            for (Truck truck : notDockedOrderedList) {
                if (truck.getArrivalDay() == day) {
                    System.out.println("Truck ID: " + truck.getId());
                    System.out.println("Foreign/Domestic: " + truck.getIsForeign());
                    System.out.println("Critical Ratio: " + truck.getCriticalRatio());
                    System.out.println("Arrival Day: " + (truck.getArrivalDay() + 1));
                    System.out.println("Arrival Time: " + truck.getArrivalTime());
                    System.out.println("Due Day: " + truck.getDueDay());
                    System.out.println("Due Time: " + truck.getDueTime());
                    System.out.println("Priority: " + truck.getPriority());
                    System.out.println(" ");
                    System.out.println("--------------------------------------------------");
                }
            }
        }

        for(int day = 0 ; day <= 4 ; day ++) { // endtime needed wrokers vs. iki sıra için ayrılacak
            System.out.println("Day: " + (day + 1));
            for (int minute = 0; minute < 480; minute++) {

                if (dockedAvailable) {
                    currentTruck = chooseCurrentTruckandRemove(dockedOrderedList,availableWorkers,day,minute);

                    if (currentTruck != null) {
                        dockedAvailable = false;
                        currentTruck.setStartTime(day * 480 + minute);
                        currentTruck.setEndTime(currentTruck.getStartTime() + currentTruck.getProcessTime());
                        endTime = currentTruck.getEndTime();
                        neededWorkers = currentTruck.getNeededWorkers();

                        if(currentTruck.getEndTime() > currentTruck.getDueTime()){
                            isLate = true;
                            if(currentTruck.getIsForeign()){ penaltyCost +=500; }
                            else { penaltyCost += 100; }
                        } else {
                            isLate = false;
                        }

                        if (currentTruck.getIsForeign() && isDomestic){
                            setupCost += 200;
                            isDomestic = false;
                        } else if(!currentTruck.getIsForeign()){
                            isDomestic = true;
                        }
                        System.out.println("Truck ID: " + currentTruck.getId() + " | Docked?: " + currentTruck.getIsDocked() + " | Arrival Time: " + currentTruck.getArrivalTime() + " | Process Time: " + currentTruck.getProcessTime() + " | End Time: " + currentTruck.getEndTime() + " | Due Time: " + currentTruck.getDueTime() + " | Number of Workers: " + currentTruck.getNeededWorkers() + " | Foreign?: " + currentTruck.getIsForeign() + " | Delay applied?: " + isLate);
                    }
                }
                if ((day * 480 + minute) == endTime) {
                    dockedAvailable = true;
                    availableWorkers += neededWorkers;
                }

                if (notDockedAvailable) {
                    currentTruck = chooseCurrentTruckandRemove(notDockedOrderedList,availableWorkers,day,minute);

                    if (currentTruck != null) {
                        notDockedAvailable = false;
                        currentTruck.setStartTime(day * 480 + minute);
                        currentTruck.setEndTime(currentTruck.getStartTime() + currentTruck.getProcessTime());
                        endTime = currentTruck.getEndTime();
                        neededWorkers = currentTruck.getNeededWorkers();

                        if(currentTruck.getEndTime() > currentTruck.getDueTime()){
                            isLate = true;
                            if(currentTruck.getIsForeign()){ penaltyCost +=500; }
                            else { penaltyCost += 100; }
                        } else {
                            isLate = false;
                        }

                        if (currentTruck.getIsForeign() && isDomestic){
                            setupCost += 200;
                            isDomestic = false;
                        } else if(!currentTruck.getIsForeign()){
                            isDomestic = true;
                        }

                        System.out.println("Truck ID: " + currentTruck.getId() + " | Docked?: " + currentTruck.getIsDocked() + " | Arrival Time: " + currentTruck.getArrivalTime() + " | Process Time: " + currentTruck.getProcessTime() + " | End Time: " + currentTruck.getEndTime() + " | Due Time: " + currentTruck.getDueTime() + " | Number of Workers: " + currentTruck.getNeededWorkers() + " | Foreign?: " + currentTruck.getIsForeign() + " | Delay applied?: " + isLate);

                    }
                } if ((day * 480 + minute) == endTime) {
                    notDockedAvailable = true;
                    availableWorkers += neededWorkers;
                }
            }
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        }
        System.out.println("Total penalty cost occurred due to delays: " + penaltyCost);
        System.out.println("Total setup cost occurred due to foreign trucks: " + setupCost);
    }

    public static Truck chooseCurrentTruckandRemove(ArrayList<Truck> orderedList, int availableWorkers, int day, int minute) {
        Truck currentTruck = null;

        for (Truck t : orderedList) {
            if(t.getIsForeign() && (day == 0 || day == 1 || day == 2)){
                continue;
            }
            if (t.getArrivalTime() <= day * 480 + minute) {
                for(Truck s: orderedList){
                    if(s.getIsForeign() && (day == 0 || day == 1 || day == 2)){
                        continue;
                    }
                    if(t.getArrivalTime() == s.getArrivalTime()){
                        if(t.getDueTime() < s.getDueTime()) {
                            if (availableWorkers >= t.getNeededWorkers()) {
                                currentTruck = t;
                                availableWorkers -= t.getNeededWorkers();
                            }
                        } else if(s.getArrivalTime() <= day * 480 + minute) {
                            if (availableWorkers >= s.getNeededWorkers()) {
                                currentTruck = s;
                                availableWorkers -= s.getNeededWorkers();
                            }
                        }
                    }
                }
            }
        }

        orderedList.remove(currentTruck);
        return currentTruck;
    }

    private static Truck findNextBestArrivalAndRemove(HashMap<Truck, Integer> arrivalTimeList) {
        Truck best = null;

        int bestArrival = Integer.MAX_VALUE;
        for (Truck s : arrivalTimeList.keySet()) {
            if (arrivalTimeList.get(s) <= bestArrival) {
                bestArrival = arrivalTimeList.get(s);
                best = s;
            }
        }

        arrivalTimeList.remove(best);
        return best;
    }
}