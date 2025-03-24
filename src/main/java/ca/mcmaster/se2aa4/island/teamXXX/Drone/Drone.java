package ca.mcmaster.se2aa4.island.teamXXX.Drone;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ExplorerSubject;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
//import ca.mcmaster.se2aa4.island.teamXXX.Map.FindIsland;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ScanningSystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashSet;
import java.util.Set;

public class Drone extends ExplorerSubject implements Movable {

    private static final Logger logger = LogManager.getLogger(Drone.class);

    private Navigator navigator = new Navigator();
    private ScanningSystem radar;
    private Directions heading;
    private Directions searchDirection;
    private Battery battery;
    private Set<String> visitedLocations;
    private Set<String> turnPoints;
    private int x;
    private int y;
    private Actions actions;

    //? remove tight coupling with Drone class later
    public Drone(Integer amount, Directions starting, ScanningSystem radar) {
        this.radar = radar;
        this.battery = new Battery(amount);
        x = 0;
        y = 0;
        this.heading = starting;
        this.searchDirection = navigator.getRight(this.heading);
        this.visitedLocations = new HashSet<>();
        this.turnPoints = new HashSet<>();
        this.actions = new Actions();
    }

    private void updateCoordinates() {
        int[] move = navigator.getForwardMovement(this.heading);
        this.x += move[0];
        this.y += move[1];
    }

    @Override
    public void moveForward() {
        int[] move = navigator.getForwardMovement(this.heading);
        this.x += move[0];
        this.y += move[1];

        update(actions.fly());
    }

    @Override
    public void turnRight() {
        updateCoordinates();
        this.searchDirection = this.heading;
        this.heading = navigator.getRight(this.heading);
        this.radar.setHeading(this.heading);
        updateCoordinates();

        update(actions.heading(this.heading));
    }

    @Override
    public void turnLeft() {
        updateCoordinates();
        this.searchDirection = this.heading;
        this.heading = navigator.getLeft(this.heading);
        this.radar.setHeading(this.heading);
        updateCoordinates();

        update(actions.heading(this.heading));
    }

    @Override
    public void stop() {
        update(actions.stop());
    }

    //? code is sort of redundant can be refactored

    @Override
    public boolean hasVisitedLocation() {
        String positionKey = x + "," + y; // unique key for (x, y)
        if (visitedLocations.contains(positionKey)) {
            return true; // already visited this location
        }
        visitedLocations.add(positionKey); // add new location to the set
        return false;
    }

    @Override
    public boolean isTurnPoint() {
        String positionKey = x + "," + y;
        if (turnPoints.contains(positionKey)) {
            return true; // current location is known to be a point to make a turn
        }
        return false;
    }

    @Override
    public void addTurnPoint() {
        String positionKey = x + "," + y;
        turnPoints.add(positionKey);
    }

    @Override
    public Directions getSearchDirection() {
        return this.searchDirection;
    }
    
    @Override
    public int getBatteryLevel() {
        return this.battery.getBatteryLevel();
    }

    @Override
    public void useBattery(int amount) {
        battery.useBattery(amount);
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public Directions getHeading() {
        return this.heading;
    }

    @Override
    public int getInitialBatteryLevel() {
        return this.battery.getInitialLevel();
    }
}
