package ca.mcmaster.se2aa4.island.teamXXX.Drone;

public class Battery {

    private int batteryLevel;
    private final int initialBattery;
    
    public Battery(int amount) {
        this.batteryLevel = amount;
        this.initialBattery = amount;
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

    public boolean shouldReturnHome(int x, int y, int costPerMove) {
        int estimatedReturnCost = (x + y) * costPerMove;
        return batteryLevel <= estimatedReturnCost;
    }

    public int getInitialLevel() {
        return this.initialBattery;
    }
}