package travel.agency;

import java.awt.Window;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author abinashmeher999
 */
public class Car implements Serializable{

    public static class Interval implements Serializable{

        public Date start;
        public Date end;

        public Interval(Date start, Date end) {
            this.start = start;
            this.end = end;
        }

    }
    
    public static class log implements Serializable{
        public int ID;
        public Interval interval;

        public log(int pID, Interval interval) {
            this.ID = pID;
            this.interval = interval;
        }
    }

    public static boolean isOverlapping(Car.Interval int1, Car.Interval int2) {
        return int1.start.before(int2.end) && int2.start.before(int1.end);
    }

    public enum Type {

        AMBASSADOR,
        TATA_SUMO,
        MARUTI_OMNI,
        MARUTI_ESTEEM,
        MAHINDRA_ARMADA
    }

    public enum State {

        REPAIR,
        AVAILABLE,
        RENTED
    }

    private int ID;
    private float minHour;
    private final Type type;
    private boolean isAC;
    private State state;
    private final float price;
    private float totalRepairCost;
    private int numSentRepair;
    private int numRented;
    private float totalearned;
    private float totalfuelCost;
    private int totalKM;
    private float fuelConsumed;
    private float perHour;
    private float perKM;
    private float mileage;
    private float advanceMoney;
    private Interval currentSession;
    private ArrayList<log> times;

    public Car(Type carType, float price, int ID, boolean isAC) {
        this.state = Car.State.AVAILABLE;
        this.minHour = 4;
        this.perHour = 20;
        this.perKM = 21;
        this.mileage = 18;
        this.advanceMoney = 2000;
        this.totalRepairCost = 0;
        this.numSentRepair = 0;
        this.numRented = 0;
        this.totalearned = 0;
        this.totalfuelCost = 0;
        this.totalRepairCost = 0;
        this.fuelConsumed = 0;
        this.price = price;
        this.type = carType;
        this.ID = ID;
        this.isAC = isAC;
        times = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public float getMinHour() {
        return minHour;
    }

    public Type getType() {
        return type;
    }

    public boolean isAC() {
        return isAC;
    }

    public State getState() {
        return state;
    }

    public float getPrice() {
        return price;
    }

    public float getTotalRepairCost() {
        return totalRepairCost;
    }

    public int getNumSentRepair() {
        return numSentRepair;
    }

    public int getNumRented() {
        return numRented;
    }

    public float getTotalearned() {
        return totalearned;
    }

    public float getTotalfuelCost() {
        return totalfuelCost;
    }

    public float getFuelConsumed() {
        return fuelConsumed;
    }

    public float getPerHour() {
        return perHour;
    }

    public float getPerKM() {
        return perKM;
    }

    public float getAdvanceMoney() {
        return advanceMoney;
    }

    public int getTotalKM() {
        return totalKM;
    }

    public void setIsAC(boolean isAC) {
        this.isAC = isAC;
    }

    public void setPerHour(float perHour) {
        this.perHour = perHour;
    }

    public void setPerKM(float perKM) {
        this.perKM = perKM;
    }

    public void setAdvanceMoney(float advanceMoney) {
        this.advanceMoney = advanceMoney;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ArrayList<log> getTimes() {
        return times;
    }

    public Interval getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Interval currentSession) {
        this.currentSession = currentSession;
    }

    public void setMinHour(float minHour) {
        this.minHour = minHour;
    }

    public void setTotalRepairCost(float totalRepairCost) {
        this.totalRepairCost = totalRepairCost;
    }

    public void setNumSentRepair(int numSentRepair) {
        this.numSentRepair = numSentRepair;
    }

    public void setNumRented(int numRented) {
        this.numRented = numRented;
    }

    public void setTotalearned(float totalearned) {
        this.totalearned = totalearned;
    }

    public void setTotalfuelCost(float totalfuelCost) {
        this.totalfuelCost = totalfuelCost;
    }

    public void setTotalKM(int totalKM) {
        this.totalKM = totalKM;
    }

    public void setFuelConsumed(float fuelConsumed) {
        this.fuelConsumed = fuelConsumed;
    }

    public float getMileage() {
        return mileage;
    }

    public void setMileage(float mileage) {
        this.mileage = mileage;
    }
    
    
}
