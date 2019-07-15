
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

public class MarsMission {

    public static void main(String[] args) {
        Simulation sim = new Simulation();
        ArrayList<Item> phase1_lst = sim.loadItems("phase-1.txt");
        ArrayList<Item> phase2_lst = sim.loadItems("phase-2.txt");
        ArrayList<Rocket> u1_list1 = sim.loadU1(phase1_lst);
        System.out.println("Total u1 rockets for phase_1 " + u1_list1.size());
        ArrayList<Rocket> u1_list2 = sim.loadU1(phase2_lst);
        System.out.println("Total u1 rockets for phase_2 " + u1_list2.size());
        int u1_cost = sim.runSimulation(u1_list1)+ sim.runSimulation(u1_list2);
        System.out.println("Cost of U1 rockets $" + u1_cost + " Millions");
        phase1_lst = sim.loadItems("phase-1.txt");
        phase2_lst = sim.loadItems("phase-2.txt");
        ArrayList<Rocket> u2_list1 = sim.loadU2(phase1_lst);
        System.out.println("u2 rockets phase_1 " + u2_list1.size());
        ArrayList<Rocket> u2_list2 = sim.loadU2(phase2_lst);
        System.out.println("u2 rockets phase_2 " + u2_list2.size());
        int u2_cost = sim.runSimulation(u2_list1)+ sim.runSimulation(u2_list2);
        System.out.println("Cost of U2 rockets $" + u2_cost + " Millions");

    }
}

class U1 extends Rocket{

    private final int cost = 100;
    private int weight;
    private static final int max_wt = 18000;
    private int cargo ;
    private double explsn_chnc ;
    private double crash_chnc ;

    public U1() {
        this.weight = 10000;
        this.cargo = weight;

    }

    public int getCost() {
        return cost;
    }

    public static int getMax_wt() {
        return max_wt;
    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    @Override
    public boolean launch() {
        this.explsn_chnc = 0.05 * (this.cargo/this.max_wt);
        double prob = Math.random();
        if (prob <= explsn_chnc) {
            return false;
        }
        else {
            return true;
        }

    }

    @Override
    public boolean land() {
        this.crash_chnc =  0.01 * (this.cargo/this.max_wt);
        double prob = Math.random();
        if (prob <= crash_chnc) {
            return false;
        }
        else {
            return true;
        }
    }

}


class U2 extends Rocket{
    private final int cost = 210;
    private int weight;
    private static final int max_wt = 29000;
    private int cargo;
    private double explsn_chnc ;
    private double crash_chnc ;

    public U2() {
        this.weight = 18000;
        this.cargo = weight;
    }

    public int getCost() {
        return cost;
    }

    public static int getMax_wt() {
        return max_wt;
    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    @Override
    public boolean launch() {
        this.explsn_chnc = 0.04 * (this.cargo/this.max_wt);
        double prob = Math.random();
        if (prob <= explsn_chnc) {
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public boolean land() {
        this.crash_chnc =  0.08 * (this.cargo/this.max_wt);
        double prob = Math.random();
        if (prob <= crash_chnc) {
            return false;
        }
        else {
            return true;
        }
    }
}

class Item {
    private String name;
    private int wt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWt() {
        return wt;
    }

    public void setWt(int wt) {
        this.wt = wt;
    }
}

interface Spaceship {
    boolean launch();
    boolean land();
    boolean can_carry(Item item, Rocket r);
    int carry(Item item, Rocket r);
}


class Rocket implements Spaceship {

    private final int cost = 0;
    private int weight = 0;
    private static final int max_wt = 0;
    private int cargo = 0;
    private double explsn_chnc = 0;
    private double crash_chnc = 0;

    @Override
    public boolean launch() {
        return true;
    }

    @Override
    public boolean land() {
        return true;
    }

    @Override
    public boolean can_carry(Item item, Rocket r) {
        int new_wt = 0;
        if(r instanceof U1) {
            new_wt = item.getWt() + ((U1)r).getCargo();
            //System.out.println("in can carry" + ((U1)r).getCargo());
            if (new_wt <= ((U1)r).getMax_wt()){
                return true;
            }
            else {
                return false;
            }
        }
        if(r instanceof U2) {
            new_wt = item.getWt() + ((U2)r).getCargo();
            //System.out.println("in can carry" + this.cargo);
            if (new_wt <= ((U2)r).getMax_wt()){
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    @Override
    public int carry(Item item, Rocket r) {
        if(r instanceof U1) {
            return item.getWt() + ((U1)r).getCargo();
        }
        else {
            return item.getWt() + ((U2)r).getCargo();
        }

    }
}


class Simulation{

    public ArrayList<Item> loadItems (String file_name) {
        //System.out.println("In load items");
        ArrayList<Item> itmlst = new ArrayList<Item>();
        File file = new File(file_name);
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNext()) {
                String str = scan.nextLine();
                String[] parts = str.split("=");
                Item itm = new Item();
                itm.setName(parts[0]);
                itm.setWt(Integer.parseInt(parts[1]));
                itmlst.add(itm);
            }

        }
        catch (FileNotFoundException fe) {
            System.out.println("Give correct file name");
            System.exit(-1);
        }
        return itmlst;
    }

    public ArrayList<Rocket> loadU1 (ArrayList<Item> itemlst) {
        //System.out.println("in load u1");
        ArrayList<Rocket> u1_rocket = new ArrayList<Rocket>();
        ListIterator<Item> itr = itemlst.listIterator();
        boolean obj_needed = false;
        Rocket u1 = new U1();
        do {
            Item it;
            //System.out.println("iteration "+ iteration);
            //System.out.println("objects till now "+object);
            if(obj_needed) {
                u1 = new U1();
                it = itr.previous();
            }
            else {
                it = itr.next();
            }
            if(u1.can_carry(it, u1)){
                int temp = u1.carry(it, u1);
                ((U1)u1).setCargo(temp);
                itr.remove();
                obj_needed = false;
            }
            else {
                u1_rocket.add(u1);
                obj_needed = true;
            }
        } while (itr.hasNext());
        u1_rocket.add(u1);
        return u1_rocket;
    }

    public ArrayList<Rocket> loadU2 (ArrayList<Item> itemlst) {
        ArrayList<Rocket> u2_rocket = new ArrayList<Rocket>();
        ListIterator<Item> itr = itemlst.listIterator();
        boolean obj_needed = false;
        Rocket u2 = new U2();
        do {
            Item it;
            if(obj_needed) {
                u2 = new U2();
                it = itr.previous();
            }
            else {
                it = itr.next();
            }
            if(u2.can_carry(it, u2)){
                int temp = u2.carry(it, u2);
                ((U2)u2).setCargo(temp);
                itr.remove();
                obj_needed = false;
            }
            else {
                u2_rocket.add(u2);
                obj_needed = true;
            }
        } while (itr.hasNext());
        u2_rocket.add(u2);
        return u2_rocket;
    }

    public int runSimulation(ArrayList<Rocket> rocket) {
        //System.out.println("in runsim");
        int cost = 0;
        for (Rocket rkt : rocket) {
            do {
                if (rkt instanceof U1) {
                    cost = cost + ((U1)rkt).getCost();
                }
                if (rkt instanceof U2) {
                    cost = cost + ((U2)rkt).getCost();
                }
            } while(!(rkt.launch() && rkt.land()));
        }
        return cost;
    }
}