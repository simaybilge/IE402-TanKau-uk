import java.io.*;
import java.util.*;
    public class Truck {
        private static final int DOCKED_PROCESS_TIME_PER_PALLET = 4;
        private static final int NOT_DOCKED_PROCESS_TIME_PER_PALLET = 3;

        private final int id;
        private boolean isForeign = false;
        private int arrivalDay;
        private int arrivalTime;
        private int palletCount; // for each queue order and day
        private int priority; //yeni
        private int dueDay;
        private int dueTime;
        private int neededWorkers; // for each queue order and day
        private boolean isDocked;
        private double criticalRatio;


        // Constructor, getters,and setters
        public Truck(int id) {
            this.id = id;
            this.isForeign = false;
            this.arrivalDay = 0;
            this.arrivalTime = 0;
            this.palletCount = 0;
            this.priority = 0;
            this.dueDay = 0;
            this.dueTime = 0;
            this.criticalRatio = 0;
            this.neededWorkers = 0;
            this.isDocked = false;
        }

        // Read Trucks from CSV
        public static ArrayList<Truck> generateTrucksFromCSV(String trucksCSVPath) {

            try {
                BufferedReader br = new BufferedReader(new FileReader(trucksCSVPath));

                // Lists
                String truckIDLine = br.readLine();

                String[] truckIDList = truckIDLine.split(",");
                String[] foreignList = br.readLine().split(",");
                String[] arrivalDayList = br.readLine().split(",");
                String[] arrivalTimeList = br.readLine().split(",");
                String[] amountList = br.readLine().split(",");
                String[] priorityList = br.readLine().split(",");
                String[] dueDayList = br.readLine().split(",");
                String[] dueTimeList = br.readLine().split(",");
                String[] workerList = br.readLine().split(",");
                String[] dockedList = br.readLine().split(",");

                ArrayList<Truck> trucks = new ArrayList<>();

                for(int i = 1; i < truckIDList.length ; i++) {
                    Truck newTruck = new Truck(Integer.parseInt(truckIDList[i]));
                    trucks.add(newTruck);
                }

                for(int i = 1; i < foreignList.length ; i++) {
                    trucks.get(i - 1).setForeign(!foreignList[i].equals("DOMESTIC"));
                }

                for(int i = 1; i < arrivalDayList.length ; i++) {
                    trucks.get(i-1).setArrivalDay(Integer.parseInt(arrivalDayList[i]));
                }

                for(int i = 1; i < arrivalTimeList.length ; i++) {
                    trucks.get(i-1).setArrivalTime(Integer.parseInt(arrivalTimeList[i])+((trucks.get(i-1).getArrivalDay()-1))*480);
                }

                for(int i = 1; i < amountList.length ; i++) {
                    trucks.get(i - 1).setPalletCount(Integer.parseInt(amountList[i]));
                }

                for(int i = 1; i < priorityList.length ; i++) {
                    trucks.get(i-1).setPriority(Integer.parseInt(priorityList[i]));
                }

                for(int i = 1; i < dueDayList.length ; i++) {
                    trucks.get(i - 1).setDueDay(Integer.parseInt(dueDayList[i]));
                }

                for(int i = 1; i < dueTimeList.length ; i++) {
                    trucks.get(i - 1).setDueTime(Integer.parseInt(dueTimeList[i])+(trucks.get(i-1).getDueDay()-1)*480);
                }

                for(int i = 1; i < workerList.length ; i++) {
                    trucks.get(i - 1).setNeededWorkers((Integer.parseInt(workerList[i])));
                }

                for(int i = 1; i < dockedList.length ; i++) {
                    trucks.get(i - 1).setDocked(dockedList[i].equals("DOCKED"));
                }

                for(int i = 1; i < truckIDList.length ; i++) {
                    trucks.get(i - 1).setCriticalRatio((trucks.get(i - 1).getDueTime()-trucks.get(i - 1).getArrivalTime())/trucks.get(i - 1).getProcessTime());
                }

                // Print trucks
                for (Truck truck : trucks) {
                    truck.printTruck();
                }
                return trucks;

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }

        }

        public int getId() { return id; }
        public boolean getIsForeign() { return isForeign; }
        public void setForeign(boolean isForeign) { this.isForeign = isForeign; }
        public int getArrivalDay() { return arrivalDay; }
        public void setArrivalDay(int arrivalDay) { this.arrivalDay = arrivalDay; }
        public int getArrivalTime() { return arrivalTime; }
        public void setArrivalTime(int arrivalTime) { this.arrivalTime = arrivalTime; }
        public int getPalletCount() { return palletCount; }
        public void setPalletCount(int palletCount) { this.palletCount = palletCount; }
        public double getProcessTime() {
            return (isDocked ? DOCKED_PROCESS_TIME_PER_PALLET : NOT_DOCKED_PROCESS_TIME_PER_PALLET) * palletCount;
        }
        public int getPriority(){ return priority; } //yeni
        public void setPriority(int priority) { this.priority = priority; }
        public int getDueDay() { return dueDay; }
        public void setDueDay(int dueDay) { this.dueDay = dueDay; }
        public int getDueTime() { return dueTime; }
        public void setDueTime(int dueTime) { this.dueTime = dueTime; }
        public int getNeededWorkers() { return neededWorkers; }
        public void setNeededWorkers(int neededWorkers) { this.neededWorkers = neededWorkers; }
        public boolean getIsDocked() { return isDocked; }
        public void setDocked(boolean isDocked) { this.isDocked = isDocked; }
        public double getPenaltyMultiplier() {
            if (isForeign)
                return 5;
            else
                return 1;
        }
        public double getCriticalRatio() { return criticalRatio; }
        public void setCriticalRatio(double criticalRatio) { this.criticalRatio = criticalRatio; }

       public void printTruck() {
            System.out.println("-- TRUCK ID "+getId()+" --");
            System.out.println("Is Foreign: " + getIsForeign());
            System.out.println("Arrival Day: " + getArrivalDay());
            System.out.println("Arrival Time: " + getArrivalTime());
            System.out.println("Palette Count: " + getPalletCount());
            System.out.println("Process Time: " + getProcessTime());
            System.out.println("Priority: " + getPriority()); //yeni
            System.out.println("Due Day: " + getDueDay());
            System.out.println("Due Time: " + getDueTime());
            System.out.println("Critical Ratio: " + getCriticalRatio());
            System.out.println("Number of Worker: " + getNeededWorkers());
            System.out.println("Is Docked: " + getIsDocked());
            System.out.println("---------------------------");
        }
    }




