package ca.mcmaster.se2aa4.island.teamXXX.Drone;

public class Battery {

    private int batteryLevel;

    public Battery(int amount) {
        this.batteryLevel = amount;
    }

    public int getBatteryLevel() {
        return this.batteryLevel;
    }

    public void useBattery(int amount) {
        if (amount > batteryLevel) {
            throw new IllegalArgumentException("Invalid amount");
        }
        batteryLevel -= amount;
    }

}